export JAVA_HOME=/usr/local/java8/
cd common
mvn install

cd ../comment-service
mvn clean package docker:build

cd ../like-service
mvn clean package docker:build

cd ../user-service
mvn clean package docker:build

cd ../gateway
mvn clean package docker:build

cd ../eurekaserver
mvn clean package docker:build
