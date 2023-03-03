#! /bin/sh
echo "********************************************************"
echo "Starting User Service"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom \
     -Deureka.client.serviceUrl.defaultZone=$EUREKA_URL -jar /usr/local/userservice/@project.build.finalName@.jar
