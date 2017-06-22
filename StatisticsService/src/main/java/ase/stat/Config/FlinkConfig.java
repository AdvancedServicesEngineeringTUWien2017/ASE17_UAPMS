package ase.stat.Config;

import ase.shared.Misc.TypestaticQueue;
import ase.stat.EventProcessing.StatisticsExecutionEnvironment;
import ase.stat.EventProcessing.StatisticsSourceFunction;
import ase.stat.Models.ParkingspotStatusChangeProcessingModel;
import ase.stat.Models.StatisticsRecord;
import org.apache.flink.streaming.api.TimeCharacteristic;
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
        environment.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        return environment;
    }

    @Bean
    public StatisticsSourceFunction sourceFunction(){
        StatisticsSourceFunction fnc = new StatisticsSourceFunction();
        return fnc;
    }

    @Bean
    public StatisticsExecutionEnvironment IoTExecutionEnvironment(StreamExecutionEnvironment environment, StatisticsSourceFunction sourceFunction){
        StatisticsExecutionEnvironment env = new StatisticsExecutionEnvironment();
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
    TypestaticQueue<StatisticsRecord> statisticsRecordQueue(){
        TypestaticQueue<StatisticsRecord> queue = new TypestaticQueue<StatisticsRecord>(StatisticsRecord.class);
        return queue;
    }

    @Bean
    TypestaticQueue<ParkingspotStatusChangeProcessingModel> getParkingspotStatusChangedQueue(){
        TypestaticQueue<ParkingspotStatusChangeProcessingModel> queue = new TypestaticQueue<ParkingspotStatusChangeProcessingModel>(ParkingspotStatusChangeProcessingModel.class);
        return queue;
    }
}
