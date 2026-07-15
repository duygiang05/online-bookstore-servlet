FROM tomcat:10.1.52-jdk17-temurin

COPY target/online_bookstore-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080