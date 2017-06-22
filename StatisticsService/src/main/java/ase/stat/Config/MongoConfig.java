package ase.stat.Config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * Created by Tommi on 16.06.2017.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class MongoConfig {

    @Bean
    public MongoDbFactory mongoDbFactory(@Value("${mongo.hostname}") String hostname, @Value("${mongo.port}") int port, @Value("${mongo.dbname}") String dbname) throws Exception{
        return new SimpleMongoDbFactory(new MongoClient(hostname,port),dbname);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory factory) throws Exception{
        MongoTemplate template = new MongoTemplate(factory);
        return template;
    }
}
