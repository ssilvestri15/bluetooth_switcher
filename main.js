const { app, BrowserWindow, ipcMain} = require('electron')
const path = require('path')

function createWindow () {
  const win = new BrowserWindow({
    width: 800,
    height: 600,
    minWidth: 800,
    minHeight: 600,
    resizable: false,
    maximizable: false,
    transparent: false,
    frame: false,
    titleBarStyle: 'hidden',
    titleBarOverlay: true,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  })

  win.loadFile('./splash_window.html')
}

ipcMain.on('electron-store-get-data', (event, arg) => {
  // Here you should handle the message and return the appropriate data.
  // For example, you might interact with Electron's main process APIs or perform some operations.
  // After processing, you can send the result back to the renderer process using event.returnValue.
  // For example:
  event.returnValue = 'Data from main process'; // Replace this with the actual data you want to return.
});

app.whenReady().then(() => {

  ipcMain.on('askForConnection', (event, args) => {
    const json = JSON.stringify(args)
    console.log("Asking for connection: " + json);
    const window = BrowserWindow.getFocusedWindow();
    window.loadFile('./askconnection/askconnection.html');
  
    // Send data to the renderer process
    window.webContents.on('did-finish-load', ()=>{
      window.webContents.send('connectionData', json);
    })
  });

  ipcMain.on('navigate', (event, arg) => {
    console.log(arg) // prints "ping"

    switch(arg){
      case 'home':
        BrowserWindow.getFocusedWindow().loadFile('./index.html')
        break;
      case 'onboarding':
        BrowserWindow.getFocusedWindow().loadFile('./on_boarding/on_boarding.html')
        break;
      default:
        break;  
    }
  });

  createWindow()

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
      createWindow()
    }
  })
})

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
})