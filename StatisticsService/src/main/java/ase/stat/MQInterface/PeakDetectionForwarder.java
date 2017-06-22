package ase.stat.MQInterface;

import ase.shared.PeakDetectionModels.PeakDetectionRequest;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by tommi on 19.06.2017.
 */
@Service
public class PeakDetectionForwarder {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Value("${mq.peakdetectionqueue}")
    private String peakDetectionQueueName;

    @PostConstruct
    public void init(){
        rabbitAdmin.declareQueue(new Queue(peakDetectionQueueName, false, false, false));
    }

    public void peakModeChanged(PeakDetectionRequest request){
        rabbitTemplate.convertAndSend(peakDetectionQueueName, request);
    }
}
