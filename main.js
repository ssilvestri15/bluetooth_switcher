const { app, BrowserWindow, ipcMain } = require("electron");
const path = require("path");

function createWindow() {
  const win = new BrowserWindow({
    width: 800,
    height: 600,
    minWidth: 800,
    minHeight: 600,
    resizable: false,
    maximizable: false,
    transparent: false,
    frame: false,
    titleBarStyle: "hidden",
    titleBarOverlay: true,
    webPreferences: {
      preload: path.join(__dirname, "preload.js"),
    },
  });

  win.loadFile("./splash_window.html");
}

ipcMain.on("electron-store-get-data", (event, arg) => {
  // Here you should handle the message and return the appropriate data.
  // For example, you might interact with Electron's main process APIs or perform some operations.
  // After processing, you can send the result back to the renderer process using event.returnValue.
  // For example:
  event.returnValue = "Data from main process"; // Replace this with the actual data you want to return.
});

app.whenReady().then(() => {

  ipcMain.on("approvedConnection", (event, args) => {
    
  });
  
  ipcMain.on("closeWindow", (event, args) => {
    console.log("Closing window: " + args);
    closeWindowById(args);
  });


  ipcMain.on("askForConnection", (event, args) => {
    const json = JSON.parse(JSON.stringify(args));
    console.log("Asking for connection: " + json);

    let window = new BrowserWindow({
      width: 800,
      height: 600,
      minWidth: 800,
      minHeight: 600,
      resizable: false,
      maximizable: false,
      transparent: false,
      frame: false,
      titleBarStyle: "hidden",
      titleBarOverlay: true,
      webPreferences: {
        preload: path.join(__dirname, "preload.js"),
      },
    });

    console.log(window.id);
    json.winId = window.id;

    const valueToPass = JSON.stringify(json);

    console.log(valueToPass);
    console.log('Opening window:' + json.winId);
    window.loadFile("./askconnection/askconnection.html");

    // Send data to the renderer process
    window.webContents.on("did-finish-load", () => {
      window.webContents.send("connectionData", valueToPass);
    });

    // Handle window close event to prevent memory leaks
    window.on("closed", () => {
      // Dereference the window object
      window = null;
    });
  });

  ipcMain.on("navigate", (event, arg) => {
    console.log(arg); // prints "ping"

    switch (arg) {
      case "home":
        BrowserWindow.getFocusedWindow().loadFile("./index.html");
        break;
      case "onboarding":
        BrowserWindow.getFocusedWindow().loadFile(
          "./on_boarding/on_boarding.html"
        );
        break;
      default:
        break;
    }
  });

  function closeWindowById(id) {
    const windowToClose = BrowserWindow.fromId(id);

    if (windowToClose) {
      windowToClose.destroy();
    } else {
      console.log(`Window with id ${id} not found.`);
    }
  }
  

  createWindow();

  app.on("activate", () => {
    if (BrowserWindow.getAllWindows().length === 0) {
      createWindow();
    }
  });
});

app.on("window-all-closed", () => {
  if (process.platform !== "darwin") {
    app.quit();
  }
});
