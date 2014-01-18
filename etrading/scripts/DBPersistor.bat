@echo off
set MAVEN_REPO=C:\Users\asim\.m2\repository
set APP_HOME=%MAVEN_REPO%\asim2025\ETrading\0.0.1-SNAPSHOT\ETrading-0.0.1-SNAPSHOT.jar

set APP_LIB=%MAVEN_REPO%\com\higherfrequencytrading\chronicle\1.7.2\chronicle-1.7.2.jar

cls

java -cp %APP_HOME%;%APP_LIB% -server -XX:+UseParNewGC -XX:+UseConcMarkSweepGC common.db.DBPersistor


