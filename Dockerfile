FROM openjdk:15
EXPOSE 8080
ADD target/idea-management-system-2022-docker.jar .
ENTRYPOINT ["java","-jar","/idea-management-system-2022-docker.jar."]
