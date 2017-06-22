package ase.status;

import ase.status.Config.RabbitConfig;
import ase.status.Config.SwaggerConfig;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by Tommi on 14.06.2017.
 */
@SpringBootApplication
@Import({RabbitConfig.class,SwaggerConfig.class})
@EnableAsync
@EnableRabbit
@EnableAspectJAutoProxy
@EnableScheduling
@ComponentScan({"ase.status","ase.monitoring"})
public class StatusServiceApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(StatusServiceApplication.class, args);
    }



    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(StatusServiceApplication.class);
    }


    @Bean
    UUID getServiceID(@Value("${trafmon.id}") String s){
        return UUID.fromString(s);
    }
}
