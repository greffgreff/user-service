FROM openjdk:17
ADD target/userservice.jar userservice.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/userservice.jar"]