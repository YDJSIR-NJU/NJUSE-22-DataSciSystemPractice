#! /bin/sh
echo "********************************************************"
echo "Starting Gateway"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom \
     -Deureka.client.serviceUrl.defaultZone=$EUREKA_URL -jar /usr/local/gateway/@project.build.finalName@.jar
