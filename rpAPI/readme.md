Installing JDK 8

Ubuntu 16.04
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer

JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64"

Note: Within your IDE, you may have to specify the JDK path as well
application.properties change

Replace with .env values to connect. Currently can't read .env
spring.datasource.url=jdbc:mysql://${DISTRICTDATA_DB_HOST}:${DISTRICTDATA_DB_PORT}/${DISTRICTDATA_DB_DATABASE}
spring.datasource.username=${DISTRICTDATA_DB_USERNAME}
spring.datasource.password=${DISTRICTDATA_DB_PASSWORD}
