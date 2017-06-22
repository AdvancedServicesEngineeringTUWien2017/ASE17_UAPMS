package ase.stat;

import ase.stat.Config.FlinkConfig;
import ase.stat.Config.MongoConfig;
import ase.stat.Config.RabbitConfig;
import ase.stat.Config.SwaggerConfig;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;

/**
 * Created by Tommi on 15.06.2017.
 */
@SpringBootApplication
@Import({RabbitConfig.class,SwaggerConfig.class, MongoConfig.class, FlinkConfig.class})
@EnableAsync
@EnableRabbit
@EnableAspectJAutoProxy
@EnableScheduling
@ComponentScan({"ase.stat","ase.monitoring"})
public class StatisticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticsServiceApplication.class, args);
    }



    @Bean
    UUID getServiceID(@Value("${trafmon.id}") String s){
        return UUID.fromString(s);
    }
}
