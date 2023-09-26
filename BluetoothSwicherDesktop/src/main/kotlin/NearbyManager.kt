import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import onboarding.NearbyManagerListener
import utils.KeyManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

data class ConnectedDevice(val host: String, val mac: String, val token: String, val socket: Socket)
class NearbyManager private constructor(var listener: NearbyManagerListener) {

    companion object {

        private var instance: NearbyManager? = null

        fun getInstance(listener: NearbyManagerListener? = null): NearbyManager {

            if (instance == null) {
                instance = NearbyManager(listener ?: object : NearbyManagerListener {
                    override fun onAskPermission(clientSocket: Socket, connectionRequest: ConnectionRequest) {
                    }

                    override fun onMessageReceived() {
                    }

                    override fun onDeviceApproved() {
                    }

                    override fun onNewDeviceConnected() {
                    }
                })
            }

            if (listener != null)
                instance!!.listener = listener

            return instance!!
        }

    }

    private val ipAddress = InetAddress.getLocalHost()
    private val jmdns = JmDNS.create(ipAddress)
    private val serviceName = "BLE"
    private val serviceType = "ble"

    val clients = mutableMapOf<String, ConnectedDevice>()
    private val scope = CoroutineScope(Dispatchers.IO)

    fun startAdvertising() {
        try {
            println("Local IP address: ${ipAddress.hostAddress}")
            createSocketClient()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun createSocketClient() {

        val serverSocket = ServerSocket(1977)

        scope.launch {
            launch {
                println("Starting zeroconf...")
                startZeroConf(serverSocket.localPort)
            }
            while (isActive && !serverSocket.isClosed) {
                val clientSocket = serverSocket.accept()
                handleMessage(clientSocket)
            }
        }
    }

    data class DeviceInfo(val host: String, val ip: String, val port: Int)

    private fun startZeroConf(port: Int) {
        try {
            val deviceInfo = DeviceInfo(ipAddress.hostName, ipAddress.hostAddress, port)
            val json = Gson().toJson(deviceInfo)
            println("Starting zeroconf with json: $json")
            val serviceInfo = ServiceInfo.create(serviceType, serviceName, port, json)
            jmdns.registerService(serviceInfo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    data class ConnectionRequest(val host: String, val mac: String, val token: String, val type:String)
    private suspend fun handleMessage(clientSocket: Socket) = withContext(Dispatchers.IO) {
        try {
            println("Client connected: ${clientSocket.inetAddress.hostAddress}")
            val message = BufferedReader(InputStreamReader(clientSocket.getInputStream())).readLine()

            if (message == null) {
                println("Message is null")
                return@withContext
            }

            if (!(message.startsWith("{") && message.endsWith("}"))) {
                println("Message is not json")
                return@withContext
            }

            try {
                val connectionRequest = Gson().fromJson(message, ConnectionRequest::class.java)
                handleDeviceConnection(clientSocket, connectionRequest)
            } catch (e: Exception) {
                println("Message is not valid request")
                return@withContext
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleDeviceConnection(clientSocket: Socket, connectionRequest: ConnectionRequest) {
        try {

            if (connectionRequest.type.isNullOrEmpty()){
                println("Type is null or empty")
                return
            }

            when (connectionRequest.type) {

                "auth" -> {
                    handleAuth(clientSocket, connectionRequest)
                }
                "message" -> {
                }
                else -> {
                    println("Type is not valid")
                    return
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleAuth(clientSocket: Socket, connectionRequest: ConnectionRequest) {

        if (connectionRequest.mac.isNullOrEmpty() || connectionRequest.host.isNullOrEmpty()){
            println("Null or empty")
            return
        }

        if (!checkIfUserExist(connectionRequest.mac)){
            println("User not exist")
            askForConnection(clientSocket, connectionRequest)
            return
        }

        if (connectionRequest.token.isNullOrEmpty()){
            println("Token is null or empty")
            return
        }

        val json = JsonObject()
        json.addProperty("host", connectionRequest.host)
        json.addProperty("mac", connectionRequest.mac)
        val verified = verifyToken(json.toString(), connectionRequest.token)
        if (verified.isNullOrEmpty()){
            println("Token is not valid")
            return
        }

        val connectedDevice = ConnectedDevice(connectionRequest.host, connectionRequest.mac, verified, clientSocket)
        handleDeviceApproved(connectedDevice)

    }

    private fun checkIfUserExist(mac: String): Boolean {
        return false
    }

    private fun verifyToken(json: String, token: String): String? {
        return try {
            val algorithm = Algorithm.HMAC256(KeyManager.JWT_SECRET)
            val verifier = JWT.require(algorithm) // specify an specific claim validations
                .withIssuer("ble") // reusable verifier instance
                .build()
            verifier.verify(token).token
        } catch (e: JWTDecodeException) {
            return generateJwtToken(json)
        } catch (e: Exception) {
            null
        }
    }

    private fun generateJwtToken(payload: String): String? {
        return try {
            val algorithm = Algorithm.HMAC256(KeyManager.JWT_SECRET)
            JWT.create()
                .withIssuer("ble")
                .withPayload(payload)
                .sign(algorithm)
        } catch (e: Exception) {
            null
        }
    }

    private fun askForConnection(clientSocket: Socket, connectionRequest: ConnectionRequest) {
        try {
            listener.onAskPermission(clientSocket, connectionRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun approveDevice(connectionSocket: Socket, connectionRequest: ConnectionRequest) {
        try {
            val json = JsonObject()
            json.addProperty("host", connectionRequest.host)
            json.addProperty("mac", connectionRequest.mac)
            val token = generateJwtToken(json.toString())

            if (token.isNullOrEmpty()){
                println("Token is null or empty")
                return
            }

            val connectedDevice = ConnectedDevice(connectionRequest.host, connectionRequest.mac, token, connectionSocket)
            //save mac
            clients[connectedDevice.mac] = connectedDevice

            listener.onDeviceApproved()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleDeviceApproved(connectedDevice: ConnectedDevice) {
        try {
            clients[connectedDevice.mac] = connectedDevice
            listener.onNewDeviceConnected()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopAdvertising() {
        try {
            jmdns.unregisterAllServices()
            jmdns.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        try {
            stopAdvertising()
            clients.values.forEach { it.socket.close() }
            scope.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}