const { app, BrowserWindow, ipcMain, dialog } = require('electron');
const fs = require('fs');
const path = require('path');
const dataFile = () => path.join(app.getPath('userData'), 'nexus-data.json');
function readData(){try{const d=JSON.parse(fs.readFileSync(dataFile(),'utf8'));return{instances:Array.isArray(d.instances)?d.instances:[]}}catch{return{instances:[]}}}
function writeData(d){fs.mkdirSync(path.dirname(dataFile()),{recursive:true});fs.writeFileSync(dataFile(),JSON.stringify(d,null,2),'utf8')}
function createWindow(){const w=new BrowserWindow({width:1320,height:850,minWidth:850,minHeight:580,backgroundColor:'#061226',webPreferences:{preload:path.join(__dirname,'preload.js'),contextIsolation:true,nodeIntegration:false,sandbox:true}});w.loadFile(path.join(__dirname,'index.html'))}
app.whenReady().then(()=>{
ipcMain.handle('instances:list',()=>readData().instances);
ipcMain.handle('instances:create',(_,name)=>{const d=readData();d.instances.push({id:Date.now().toString()+Math.random().toString(16).slice(2,6),name:String(name||'My Minecraft').trim().slice(0,48)||'My Minecraft',version:'1.21.1',loader:'Fabric (planned)',gameDir:'',createdAt:new Date().toISOString()});writeData(d);return d.instances});
ipcMain.handle('instances:rename',(_,id,name)=>{const d=readData(),x=d.instances.find(i=>i.id===id);if(x&&String(name||'').trim())x.name=String(name).trim().slice(0,48);writeData(d);return d.instances});
ipcMain.handle('instances:delete',async(_,id)=>{const a=await dialog.showMessageBox({type:'warning',buttons:['Cancel','Remove entry'],defaultId:0,cancelId:0,message:'Remove this instance entry?',detail:'Only the launcher record is removed. Minecraft files are not deleted.'});if(a.response!==1)return readData().instances;const d=readData();d.instances=d.instances.filter(x=>x.id!==id);writeData(d);return d.instances});
ipcMain.handle('folder:choose',async(_,id)=>{const a=await dialog.showOpenDialog({title:'Choose existing Minecraft folder',properties:['openDirectory']});if(a.canceled||!a.filePaths[0])return readData().instances;const d=readData(),x=d.instances.find(i=>i.id===id);if(x)x.gameDir=a.filePaths[0];writeData(d);return d.instances});
ipcMain.handle('folder:inspect',(_,id)=>{const x=readData().instances.find(i=>i.id===id);if(!x||!x.gameDir)return{ok:false,message:'Choose a folder first.'};try{const e=fs.readdirSync(x.gameDir);const looks=['versions','mods','saves','launcher_profiles.json'].some(n=>e.includes(n));return{ok:true,message:looks?'Common Minecraft files/folders detected. Basic check only.':'Folder selected; common Minecraft folders were not detected.',entries:e.slice(0,60)}}catch(err){return{ok:false,message:'Cannot read folder: '+err.message}}});
app.on('activate',()=>{if(BrowserWindow.getAllWindows().length===0)createWindow()});createWindow()});
app.on('window-all-closed',()=>{if(process.platform!=='darwin')app.quit()});