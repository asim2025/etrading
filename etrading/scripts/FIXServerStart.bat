@echo off
set MAVEN_LIB=C:\Users\asim\.m2\repository
set APP_HOME=C:\Users\asim\git\etrading\etrading\target\classes

set APP_LIB=C:\Users\asim\git\etrading\etrading\lib\quickfixj-core-1.5.3.jar
set APP_LIB=%APP_LIB%;C:\Users\asim\git\etrading\etrading\lib\quickfixj-msg-fix42-1.5.3.jar
set APP_LIB=%APP_LIB%;%MAVEN_LIB%\org\apache\mina\mina-core\1.1.7\mina-core-1.1.7.jar
set APP_LIB=%APP_LIB%;%MAVEN_LIB%\org\slf4j\slf4j-simple\1.7.5\slf4j-simple-1.7.5.jar
set APP_LIB=%APP_LIB%;%MAVEN_LIB%\org\slf4j\slf4j-api\1.7.5\slf4j-api-1.7.5.jar
cls

java -cp %APP_HOME%;%APP_LIB% -server -XX:+UseParNewGC -XX:+UseConcMarkSweepGC exchange.gateway.FIXGatewayServer

