const { app, BrowserWindow } = require('electron');
const path = require('path');
const { spawn } = require('child_process');
const isDev = require('electron-is-dev');

let mainWindow;
let backendProcess;

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1000,
    height: 800,
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: false,
    },
  });

  mainWindow.loadURL(
    isDev
      ? 'http://localhost:3000'
      : `file://${path.join(__dirname, '../build/index.html')}`
  );

  if (isDev) mainWindow.webContents.openDevTools();
}

app.whenReady().then(() => {
  createWindow();

  // Spawn backend ONLY when NOT in development (production mode)
  if (!isDev) {
    backendProcess = spawn(
      'java',
      ['-jar', path.join(__dirname, 'backend/target/vendor-management-system-0.0.1-SNAPSHOT.jar')],
      {
        cwd: path.join(__dirname, 'backend'),
        shell: true,
      }
    );

    backendProcess.stdout.on('data', (data) => {
      console.log(`Backend: ${data}`);
    });

    backendProcess.stderr.on('data', (data) => {
      console.error(`Backend Error: ${data}`);
    });

    backendProcess.on('close', (code) => {
      console.log(`Backend process exited with code ${code}`);
    });
  }
});

app.on('window-all-closed', () => {
  // Kill backend process when app quits
  if (backendProcess) backendProcess.kill();

  if (process.platform !== 'darwin') app.quit();
});
