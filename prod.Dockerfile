FROM openjdk:16-jdk-alpine
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","-Djasypt.encryptor.algorithm=PBEWithMD5AndDES", "-Djasypt.encryptor.iv-generator-classname=org.jasypt.iv.NoIvGenerator","/app.jar"]