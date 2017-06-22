package ase.ts;

import ase.ts.Config.RabbitConfig;
import ase.ts.Config.SwaggerConfig;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
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
@Import({RabbitConfig.class,SwaggerConfig.class})
@EnableRabbit
@EnableAsync
@EnableAspectJAutoProxy
@EnableScheduling
@ComponentScan({"ase.ts","ase.monitoring"})
public class TourServiceApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(TourServiceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(TourServiceApplication.class);
    }


    @Bean
    UUID getServiceID(@Value("${trafmon.id}") String s){
        return UUID.fromString(s);
    }

}
