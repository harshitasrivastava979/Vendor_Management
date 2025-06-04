   // Create this file: public/preload.js

   const { contextBridge, ipcRenderer } = require('electron');

   // Expose protected methods that allow the renderer process to use
   // the ipcRenderer without exposing the entire object
   contextBridge.exposeInMainWorld('electronAPI', {
     // File operations
     selectFile: (options) => ipcRenderer.invoke('select-file', options),
     selectFiles: (options) => ipcRenderer.invoke('select-files', options),
     saveFile: (options) => ipcRenderer.invoke('save-file', options),

     // Notifications
     showNotification: (title, body, options) =>
       ipcRenderer.invoke('show-notification', title, body, options),

     // Dialogs
     showErrorDialog: (title, content) =>
       ipcRenderer.invoke('show-error-dialog', title, content),
     showMessageDialog: (options) =>
       ipcRenderer.invoke('show-message-dialog', options),

     // Menu actions
     onMenuAction: (callback) => {
       ipcRenderer.on('menu-action', (event, action) => callback(action));
     },

     // Import/Export actions
     onImportVendors: (callback) => {
       ipcRenderer.on('import-vendors', (event, filePath) => callback(filePath));
     },

     onExportVendors: (callback) => {
       ipcRenderer.on('export-vendors', (event, filePath) => callback(filePath));
     },

     // Remove listeners
     removeMenuActionListener: () => {
       ipcRenderer.removeAllListeners('menu-action');
     },

     removeImportListener: () => {
       ipcRenderer.removeAllListeners('import-vendors');
     },

     removeExportListener: () => {
       ipcRenderer.removeAllListeners('export-vendors');
     },

     // Platform info
     platform: process.platform,

     // App version
     appVersion: '1.0.0',

     // Check if running in Electron
     isElectron: true
   });