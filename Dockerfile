FROM openjdk:17-jdk-alpine

RUN adduser -D myuser

WORKDIR /home/myuser/app

COPY /target/mottuGestor-0.0.1-SNAPSHOT.jar app.jar

RUN chown -R myuser:myuser /home/myuser && \
    chmod -R 750 /home/myuser

USER myuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]