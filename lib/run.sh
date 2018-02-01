#!/bin/sh

# Setup with inserting this line to sudo nano /etc/xdg/lxsession/LXDE-pi/autostart
#	sudo /bin/sh /home/pi/domokit/run.sh

java -Djava.library.path=/usr/lib/jni -jar /home/pi/domokit/HouseStation.jar