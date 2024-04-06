FROM openjdk:17
ARG JAR_FILE=./target/*.jar
RUN mkdir ./upload
COPY ${JAR_FILE} app.jar

ENV JAVA_OPTS="-Xmx1g -Xms256m"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]