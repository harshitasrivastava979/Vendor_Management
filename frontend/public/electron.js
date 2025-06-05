   // Create this file: public/electron.js

  import { app, BrowserWindow, Menu, ipcMain, dialog, shell, Notification } from 'electron';
  import path from 'path';
  import isDev from 'electron-is-dev';

   let mainWindow;
   let splashWindow;

   function createSplashWindow() {
     splashWindow = new BrowserWindow({
       width: 400,
       height: 300,
       frame: false,
       alwaysOnTop: true,
       transparent: true,
       webPreferences: {
         nodeIntegration: false,
         contextIsolation: true
       }
     });

     splashWindow.loadFile(path.join(__dirname, 'splash.html'));

     splashWindow.on('closed', () => {
       splashWindow = null;
     });
   }

   function createMainWindow() {
     // Create the browser window
     mainWindow = new BrowserWindow({
       width: 1400,
       height: 900,
       minWidth: 1000,
       minHeight: 700,
       show: false,
           icon: path.join(__dirname, 'assets/icon.png'),
       webPreferences: {
         nodeIntegration: false,
         contextIsolation: true,
         preload: path.join(__dirname, 'preload.js'),
         webSecurity: true
       },
       titleBarStyle: 'default'
     });

     // Load the app
     const startUrl = isDev
       ? 'http://localhost:3000'
       : `file://${path.join(__dirname, '../build/index.html')}`;

     mainWindow.loadURL(startUrl);

     // Show window when ready
     mainWindow.once('ready-to-show', () => {
       if (splashWindow) {
         splashWindow.close();
       }
       mainWindow.show();

       // Focus on window creation
       if (isDev) {
         mainWindow.webContents.openDevTools();
       }
     });

     // Handle window closed
     mainWindow.on('closed', () => {
       mainWindow = null;
     });

     // Create application menu
     createApplicationMenu();
   }

   function createApplicationMenu() {
     const template = [
       {
         label: 'File',
         submenu: [
           {
             label: 'Add New Vendor',
             accelerator: 'CmdOrCtrl+N',
             click: () => {
               mainWindow.webContents.send('menu-action', 'add-vendor');
             }
           },
           { type: 'separator' },
           {
             label: 'Import Vendors',
             accelerator: 'CmdOrCtrl+I',
             click: () => {
               handleImportVendors();
             }
           },
           {
             label: 'Export Vendors',
             accelerator: 'CmdOrCtrl+E',
             click: () => {
               handleExportVendors();
             }
           },
           { type: 'separator' },
           {
             label: 'Exit',
             accelerator: process.platform === 'darwin' ? 'Cmd+Q' : 'Ctrl+Q',
             click: () => {
               app.quit();
             }
           }
         ]
       },
       {
         label: 'Edit',
         submenu: [
           { role: 'undo' },
           { role: 'redo' },
           { type: 'separator' },
           { role: 'cut' },
           { role: 'copy' },
           { role: 'paste' },
           { role: 'selectall' }
         ]
       },
       {
         label: 'View',
         submenu: [
           { role: 'reload' },
           { role: 'forceReload' },
           { role: 'toggleDevTools' },
           { type: 'separator' },
           { role: 'resetZoom' },
           { role: 'zoomIn' },
           { role: 'zoomOut' },
           { type: 'separator' },
           { role: 'togglefullscreen' }
         ]
       },
       {
         label: 'Tools',
         submenu: [
           {
             label: 'Refresh Vendors',
             accelerator: 'F5',
             click: () => {
               mainWindow.webContents.send('menu-action', 'refresh-vendors');
             }
           },
           {
             label: 'Backup Database',
             click: () => {
               mainWindow.webContents.send('menu-action', 'backup-db');
             }
           },
           { type: 'separator' },
           {
             label: 'Settings',
             accelerator: 'CmdOrCtrl+,',
             click: () => {
               mainWindow.webContents.send('menu-action', 'settings');
             }
           }
         ]
       },
       {
         label: 'Help',
         submenu: [
           {
             label: 'About Vendor Management System',
             click: () => {
               showAboutDialog();
             }
           },
           {
             label: 'Learn More',
             click: () => {
               shell.openExternal('https://github.com/harshitasrivastava979/Vendor_Management');
             }
           }
         ]
       }
     ];

     // macOS specific menu adjustments
     if (process.platform === 'darwin') {
       template.unshift({
         label: app.getName(),
         submenu: [
           { role: 'about' },
           { type: 'separator' },
           { role: 'services' },
           { type: 'separator' },
           { role: 'hide' },
           { role: 'hideothers' },
           { role: 'unhide' },
           { type: 'separator' },
           { role: 'quit' }
         ]
       });
     }

     const menu = Menu.buildFromTemplate(template);
     Menu.setApplicationMenu(menu);
   }

   // IPC Handlers
   ipcMain.handle('select-file', async (event, options) => {
     const result = await dialog.showOpenDialog(mainWindow, {
       properties: ['openFile'],
       filters: options.filters || [
         { name: 'All Files', extensions: ['*'] }
       ]
     });
     return result;
   });

   ipcMain.handle('select-files', async (event, options) => {
     const result = await dialog.showOpenDialog(mainWindow, {
       properties: ['openFile', 'multiSelections'],
       filters: options.filters || [
         { name: 'Documents', extensions: ['pdf', 'doc', 'docx', 'jpg', 'png', 'csv', 'json'] }
       ]
     });
     return result;
   });

   ipcMain.handle('save-file', async (event, options) => {
     const result = await dialog.showSaveDialog(mainWindow, {
       filters: options.filters || [
         { name: 'JSON Files', extensions: ['json'] },
         { name: 'CSV Files', extensions: ['csv'] }
       ]
     });
     return result;
   });

   ipcMain.handle('show-notification', async (event, title, body, options = {}) => {
     if (Notification.isSupported()) {
       const notification = new Notification({
         title,
         body,
         icon: path.join(__dirname, 'assets/icon.png'),
         ...options
       });

       notification.show();
       return true;
     }
     return false;
   });

   ipcMain.handle('show-error-dialog', async (event, title, content) => {
     const result = await dialog.showErrorBox(title, content);
     return result;
   });

   ipcMain.handle('show-message-dialog', async (event, options) => {
     const result = await dialog.showMessageBox(mainWindow, options);
     return result;
   });

   // Helper functions
   async function handleImportVendors() {
     try {
       const result = await dialog.showOpenDialog(mainWindow, {
         properties: ['openFile'],
         filters: [
           { name: 'CSV Files', extensions: ['csv'] },
           { name: 'JSON Files', extensions: ['json'] }
         ]
       });

       if (!result.canceled && result.filePaths.length > 0) {
         mainWindow.webContents.send('import-vendors', result.filePaths[0]);
       }
     } catch (error) {
       console.error('Import error:', error);
     }
   }

   async function handleExportVendors() {
     try {
       const result = await dialog.showSaveDialog(mainWindow, {
         filters: [
           { name: 'CSV Files', extensions: ['csv'] },
           { name: 'JSON Files', extensions: ['json'] }
         ],
         defaultPath: `vendors-export-${new Date().toISOString().split('T')[0]}.csv`
       });

       if (!result.canceled) {
         mainWindow.webContents.send('export-vendors', result.filePath);
       }
     } catch (error) {
       console.error('Export error:', error);
     }
   }

   function showAboutDialog() {
     dialog.showMessageBox(mainWindow, {
       type: 'info',
       title: 'About Vendor Management System',
       message: 'Vendor Management System',
       detail: `Version: 1.0.0
   Built with React + Spring Boot + Electron

   A modern desktop application for managing vendors, invoices, and payments with automated TDS and GST calculations.

   © 2024 Vendor Management System`,
       buttons: ['OK'],
       icon: path.join(__dirname, 'assets/icon.png')
     });
   }

   // App event handlers
   app.whenReady().then(() => {
     createSplashWindow();

     setTimeout(() => {
       createMainWindow();
     }, 2000);
   });

   app.on('window-all-closed', () => {
     if (process.platform !== 'darwin') {
       app.quit();
     }
   });

   app.on('activate', () => {
     if (BrowserWindow.getAllWindows().length === 0) {
       createMainWindow();
     }
   });

   // Security: Prevent new window creation
   app.on('web-contents-created', (event, contents) => {
     contents.on('new-window', (navigationEvent, url) => {
       navigationEvent.preventDefault();
       shell.openExternal(url);
     });
   });

   // Handle certificate errors
   app.on('certificate-error', (event, webContents, url, error, certificate, callback) => {
     if (isDev) {
       // In development, ignore certificate errors
       event.preventDefault();
       callback(true);
     } else {
       // In production, use default behavior
       callback(false);
     }
   });