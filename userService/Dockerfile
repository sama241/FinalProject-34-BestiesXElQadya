FROM openjdk:25-ea-4-jdk-oraclelinux9
WORKDIR /app
COPY ./target/userService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]

#FROM openjdk:25-ea-4-jdk-oraclelinux9
#WORKDIR /app
#COPY ./target/ target/
#EXPOSE 8081
#ENTRYPOINT ["java","-jar","target/userService-0.0.1-SNAPSHOT.jar"]
