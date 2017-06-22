FROM java
WORKDIR /app
ADD . /app
EXPOSE 8080
CMD java -jar ./StatusService.jar --spring.config.location=file:Application.properties
