FROM openjdk:16-jdk-alpine
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ADD startProd.sh start.sh
RUN chmod +x /start.sh
ENTRYPOINT /start.sh