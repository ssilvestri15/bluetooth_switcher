const { contextBridge, ipcRenderer  } = require('electron')
const { getToken, getUserLength, askForConnection, startBleService, startSocketServer } = require('./shared/utils.js')



contextBridge.exposeInMainWorld('versions', {
  node: () => process.versions.node,
  chrome: () => process.versions.chrome,
  electron: () => process.versions.electron
})

contextBridge.exposeInMainWorld('utils', {
  navigate: (url) => ipcRenderer.send('navigate', url),
  checkToken: () => getToken(),  
  getUserLength: () => getUserLength(),
  startBleService: () => startBleService(),
  startSocketServer: (window) => startSocketServer(window),
  messageReceived: (data) => console.log(data),
  askForConnection: (data) => ipcRenderer.send('askForConnection', data),
  getConnectionData: (channel, callback) => {
    ipcRenderer.on(channel, (_, data) => {
      callback(data);
    });
  },
  closeWindow: (id) => ipcRenderer.send('closeWindow', id)
})


