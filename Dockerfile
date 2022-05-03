FROM openjdk:17
ADD target/userservice.jar userservice.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/userservice.jar"]