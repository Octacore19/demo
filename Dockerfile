FROM openjdk
LABEL maintainer="octacore"
VOLUME /demo-app
ADD /build/libs/demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080