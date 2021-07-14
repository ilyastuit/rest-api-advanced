FROM openjdk:8-jre-alpine
USER root
COPY ./api/build/libs/rest-api-advanced.jar /app/rest-api-advanced.jar
ENTRYPOINT ["java"]
CMD ["-jar", "/app/rest-api-advanced.jar"]