package ase.status.Config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by Tommi on 14.06.2017.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class RabbitConfig {

    @Bean
    public ConnectionFactory connectionFactory(@Value("${mq.hostname}") String hostname, @Value("${mq.port}") int port){
        return new CachingConnectionFactory(hostname, port);
    }


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ObjectMapper getObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return mapper;
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper mapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(mapper);
        ContentTypeDelegatingMessageConverter messageConverter = new ContentTypeDelegatingMessageConverter();
        messageConverter.addDelgate("application/json", converter);
        messageConverter.addDelgate("application/octet-stream", converter);
        return messageConverter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(MessageConverter converter, org.springframework.amqp.rabbit.connection.ConnectionFactory f) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(f);
        factory.setMessageConverter(converter);
        return factory;
    }


    @Bean
    public RabbitTemplate getRabbitTemplate(ConnectionFactory connectionFactory, MessageConverter converter){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
