package ase.pm.tourworker;

import ase.pm.tourworker.Config.RabbitConfig;
import ase.pm.tourworker.Models.TspEdge;
import ase.pm.tourworker.Models.TspInstance;
import ase.pm.tourworker.Models.TspSolution;
import ase.pm.tourworker.Models.TspVertex;
import ase.pm.tourworker.Solver.MILPTspSolver;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tommi on 04.06.2017.
 */
@SpringBootApplication
@EnableRabbit
@Import(RabbitConfig.class)
public class TourWorkerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TourWorkerApplication.class, args);
    }


    @Bean
    UUID getServiceID(@Value("${trafmon.id}") String s){
        return UUID.fromString(s);
    }
}
