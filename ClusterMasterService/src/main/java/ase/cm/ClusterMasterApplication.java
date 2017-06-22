package ase.cm;

import ase.cm.Config.FlinkConfig;
import ase.cm.Config.RabbitConfig;
import ase.cm.Config.SwaggerConfig;
import ase.cm.Models.ParkingspotRepository;
import ase.cm.Models.ParkingspotStatus;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;

/**
 * Created by Tommi on 13.06.2017.
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableRabbit
@Import({RabbitConfig.class,SwaggerConfig.class, FlinkConfig.class})
public class ClusterMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClusterMasterApplication.class, args);
    }


    @Bean
    CommandLineRunner init(ParkingspotRepository repository){
        return args->{
            repository.findAll().forEach(x->{
                x.getCurrentState().setLicensePlateId(null);
                x.getCurrentState().setStatus(ParkingspotStatus.FREE);
            });
        };
    }

    @Bean
    UUID getServiceID(@Value("${trafmon.id}") String s){
        return UUID.fromString(s);
    }



}
