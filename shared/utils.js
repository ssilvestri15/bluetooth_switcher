const Store = require('electron-store');
const store = new Store();

function checkIfUserExists(mac) {
  const users = store.get('users');

  if (users == null || users == undefined) {
    return false;
  }

  return users.has(mac);
}

function getUserLength() {

  const users = store.get('users');

  if (users == null || users == undefined) {
    return 0;
  }

  return users.size;
}

function storeToken(name, mac, token) {

  const users = store.get('users');

  let user = users.get(mac)

  if (user == null) {
    users.add(mac, {});
    user = users.get(mac);
    user.name = name;
  }
  
  if (user.mac == null) {
    user.mac = new Set();
  }

  if (user.mac.has(token)) {
    return;
  }

  user.mac.add(token);
  users.set(mac, user);
  store.set('users', user);
}

function getToken(mac) {
  return store.get(mac);
}

const ciao = require("@homebridge/ciao");
const responder = ciao.getResponder();
const os = require('os'); 
const crypto = require('crypto');
const ip = require('ip');

const dets = "{ \"host\": \""+os.hostname()+"\", \"ip\": \""+ip.address()+"\", \"port\": 1977 }";

const service = responder.createService({
  name: "BLE",
  type: 'ble',
  port: 3000, // optional, can also be set via updatePort() before advertising
  txt: { // optional
    //key: encrypt(dets)
    key: dets
  }
});

  function startBleService() {
    service.advertise().then(() => {
      // stuff you do when the service is published
      console.log("Service is published :), " + service.getTXT());
    }).catch((error) => {
      console.log("Error publishing service:", error);
    });
  }
  
  function encrypt(text) {
    const key = "bleD8Y#R6c3*Bhxv7%Lp0G@tN2AsnE^U";
    const iv = crypto.randomBytes(16);
    const cipher = crypto.createCipheriv("aes-256-cbc", key, iv);
    let encrypted = Buffer.concat([cipher.update(Buffer.from(text, "utf8")), cipher.final()]);
    return encrypted.toString("base64");
  }
  

  const http = require('http');
  const socketIo = require('socket.io');
  const { disconnect } = require('process');
  const PORT = 1977;

// Create a basic HTTP server
  const server = http.createServer();

  // Attach socket.io to the server
  const io = socketIo(server);


function startSocketServer(window) {

    //Listen for client connections
    io.on('connection', (socket) => {
        console.log('A new client connected.');

        // Listen for messages from clients
        socket.on('message', (data) => {
            handleMessages(window, data);
        });

        // Handle disconnection
        socket.on('disconnect', () => {
            console.log('Client disconnected.');
            window.utils.messageReceived('Client disconnected.');
        });
    });

    // Start the server
    try {
        server.listen(PORT, () => {
            console.log(`Socket server listening on port ${PORT}`);
        });
    } catch (error) {
        console.error(`Error starting socket server: ${error}`);
    }
}

function handleMessages(window, message) {
  
  console.log(message);

  const data = JSON.parse(message);
  console.log(data);

  if (data.type === undefined) {
    console.log("No type provided");
    return;
  }

  switch (data.type) {
    case "auth": {
      handleAuth(window, data);
      break;
    }
    case "message": {
      break;
    }
    case "disconnect": {
      break;
    }
    default: {
      break;
    }
  }

}

function handleAuth(window, data) {

  console.log("Authenticating user");

  if (data.host === undefined || data.mac === undefined) {
    return;
  }

  const host = data.host;
  const mac = data.mac;
  const token = data.token;

  if (checkIfUserExists(mac) === undefined || checkIfUserExists(mac) === null || checkIfUserExists(mac) === false) {
    console.log("User does not exist");
    window.utils.askForConnection(data);
    return;
  }

  if (data.token === undefined) {
    console.log("No token provided");
    return;
  }

  if (verifyToken(mac, token) === undefined) {
    console.log("Token invalid");
    return;
  }

  //Show device as connected
  console.log("User authenticated");
}

function askForConnection(window, host, mac) {
  
}

function verifyToken(mac, encoded) {
  const token = encoded;

  if (getToken(mac) === undefined) {
    return ;
  }

  if (!token) {
    return "No token provided";
  }

  const secretKey = 'your-secret-key';
  jwt.verify(token, secretKey, (err, decoded) => {
    if (err) {
      return 'Token invalid';
    }

    decoded; // Store the decoded payload for later use
    
  });
}

module.exports = {
    getToken: getToken,
    getUserLength: getUserLength,
    askForConnection: askForConnection,
    startBleService: startBleService,
    startSocketServer: startSocketServer
  };


