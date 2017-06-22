package ase.cm.Config;

import ase.shared.Misc.TypestaticQueue;
import ase.cm.EventProcessing.IoTExecutionEnvironment;
import ase.cm.EventProcessing.IoTSourceFunction;
import ase.cm.EventProcessing.Models.IoTRequest;
import ase.cm.EventProcessing.Models.ParkingspotStateChanged;
import ase.shared.Models.ParkingspotStatusChangeDTO;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by Tommi on 16.06.2017.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class FlinkConfig {
    @Bean
    public StreamExecutionEnvironment streamProcessingEnvironment(){
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.createLocalEnvironment(1);
        return environment;
    }

    @Bean
    public IoTSourceFunction sourceFunction(){
        IoTSourceFunction fnc = new IoTSourceFunction();
        return fnc;
    }

    @Bean
    public IoTExecutionEnvironment IoTExecutionEnvironment(StreamExecutionEnvironment environment, IoTSourceFunction sourceFunction){
        IoTExecutionEnvironment env = new IoTExecutionEnvironment();
        env.initialize(environment, sourceFunction);

        try {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(1);
            executor.setMaxPoolSize(1);
            executor.setQueueCapacity(1);
            executor.initialize();

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        environment.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return env;
    }

    @Bean
    TypestaticQueue<ParkingspotStateChanged> parkingspotStateChangedQueue(){
        TypestaticQueue<ParkingspotStateChanged> queue = new TypestaticQueue<ParkingspotStateChanged>(ParkingspotStateChanged.class);
        return queue;
    }

    @Bean
    TypestaticQueue<IoTRequest> getIoTRequestQueue(){
        TypestaticQueue<IoTRequest> queue = new TypestaticQueue<IoTRequest>(IoTRequest.class);
        return queue;
    }

    @Bean
    TypestaticQueue<ParkingspotStatusChangeDTO> getParkingspotStatusChangeQueue(){
        TypestaticQueue<ParkingspotStatusChangeDTO> queue = new TypestaticQueue<ParkingspotStatusChangeDTO>(ParkingspotStatusChangeDTO.class);
        return queue;
    }
}
