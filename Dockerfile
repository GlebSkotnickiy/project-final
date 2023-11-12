FROM openjdk:17

ENV DATASOURCE_URL=jdbc:postgresql://localhost:5432/jira

WORKDIR /myapp

COPY target/jira-1.0.jar /myapp/jira-1.0.jar
COPY resources /myapp/resources

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "jira-1.0.jar"]
