FROM openjdk:21-jdk

ENV TZ=America/Sao_Paulo
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring
ARG JAR_FILE=target/process-genome-0.0.1.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
