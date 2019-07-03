release: cp src/main/resources/application-heroku.properties src/main/resources/application.properties
web: java -Dserver.port=$PORT $JAVA_OPTS -jar target/rtdptiCache-1.0-SNAPSHOT.jar