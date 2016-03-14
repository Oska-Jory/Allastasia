@echo off
echo Compiling handlers......
"C:/Program Files/Java/jdk1.7.0/bin/javac.exe" -d bin -cp lib/*; -sourcepath src src/org/dementhium/net/packethandlers/*.java
echo Complete
pause