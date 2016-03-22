JenkinsUSB
=========

Java application which reads a jenkins job state and sends proper codes to the USB module. It has been created to replace node.js application usb_notifier.

Installation
---------
1. Connect the USB module to computer
2. Download https://sourceforge.net/projects/libwdi/files/zadig/
3. Launch zadig.exe, select **Template** in drop-down, select **libusb** in Target Driver field and click Install
4. Run `mvn install`

Configuration
---------
Edit **config.properties** file from **/target/conf** directory

Launch
---------
1. Connect the USB module to computer
2. Run `java -jar jenkins-usb.jar` in **/target** directory