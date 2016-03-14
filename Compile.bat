@echo off
echo Compiling RS2Server...
"C:/Program Files/Java/jdk1.7.0/bin/javac.exe" -d bin -cp lib/*; -sourcepath src src/org/dementhium/*.java
echo Complete
pause