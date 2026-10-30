const { contextBridge, ipcRenderer } = require('electron');
contextBridge.exposeInMainWorld('nexus',{
 listInstances:()=>ipcRenderer.invoke('instances:list'),
 createInstance:n=>ipcRenderer.invoke('instances:create',n),
 renameInstance:(id,n)=>ipcRenderer.invoke('instances:rename',id,n),
 configureInstance:(id,patch)=>ipcRenderer.invoke('instances:configure',id,patch),
 deleteInstance:id=>ipcRenderer.invoke('instances:delete',id),
 chooseFolder:id=>ipcRenderer.invoke('folder:choose',id),
 inspectFolder:id=>ipcRenderer.invoke('folder:inspect',id),
 chooseRoot:()=>ipcRenderer.invoke('folder:choose-root'),
 exportInstance:id=>ipcRenderer.invoke('instance:export',id),
 importInstance:()=>ipcRenderer.invoke('instance:import'),
 getSettings:()=>ipcRenderer.invoke('settings:get'),
 saveSettings:s=>ipcRenderer.invoke('settings:save',s)
});