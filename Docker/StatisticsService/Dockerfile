FROM java
WORKDIR /app
ADD . /app
EXPOSE 8080
CMD java -jar ./StatisticsService.jar --spring.config.location=file:Application.properties
