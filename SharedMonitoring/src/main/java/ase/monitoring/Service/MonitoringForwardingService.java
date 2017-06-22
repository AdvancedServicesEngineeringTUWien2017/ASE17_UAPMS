package ase.monitoring.Service;

import ase.shared.Misc.TypestaticQueue;
import ase.monitoring.Models.SharedMonitoringEntry;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Tommi on 16.06.2017.
 */
@Component
public class MonitoringForwardingService {

    @Autowired
    private TypestaticQueue<SharedMonitoringEntry> queue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Value("${mq.monitoringqueue}")
    private String monitoringQueue;

    @PostConstruct
    public void initQueues(){
        rabbitAdmin.declareQueue(new org.springframework.amqp.core.Queue(monitoringQueue));
    }

    @Scheduled(fixedRate = 5000)
    public void forwardMonitoringData() {
        int count = 0;
        SharedMonitoringEntry dto = null;
        while ((dto = queue.poll()) != null){
            rabbitTemplate.convertAndSend(monitoringQueue,dto);
            count++;
        }
        if(count > 0) {
            System.out.println("Job transmitted " + count + " requests");
        }
    }

    public void forward(SharedMonitoringEntry entry){
        queue.add(entry);
    }

    @Bean
    public TypestaticQueue<SharedMonitoringEntry> getMonitoringQueue(){
        return new TypestaticQueue<>(SharedMonitoringEntry.class);
    }
}
