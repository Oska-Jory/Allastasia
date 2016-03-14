@echo off
title Dementhium 639
java -server -Xmx1024m -Xms512m -XX:+UseTLAB -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000 -cp bin; lib/* org.dementhium.RS2ServerBootstrap
pause