FROM java
WORKDIR /app
ADD . /app
CMD java -Djava.library.path=./cplex -jar ./TourWorker.jar --spring.config.location=file:Application.properties
