FROM openjdk:16-jdk-alpine
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "-Djasypt.encryptor.password=15987ShatteredRealmsDev!","/app.jar"]
