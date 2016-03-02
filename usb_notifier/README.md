USB notifier
=========

Node.js desktop application.
Connects to a jenkins instance, retrieves it status and sands proper codes to the usb module.

Installation
---------
1. Connect the USB module to computer
2. Download https://sourceforge.net/projects/libwdi/files/zadig/
3. Launch zadig.exe and install USB driver from it
4. Open terminal in usb_notifier folder
5. Execute npm install

Configuration
---------
Put your jenkins URL into config.js

Launch
---------
1. Connect the USB module to computer
2. Open terminal in usb_notifier folder
3. Execute "npm run start"