package ase.ts.MQInterface;

import ase.shared.Models.ParkingspotStatusChangeDTO;
import ase.shared.PeakDetectionModels.PeakDetectionRequest;
import ase.ts.TourService.TourService;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by tommi on 19.06.2017.
 */
@Component
public class PeakDetectionMessageReceiver {

    @Autowired
    private TourService tourService;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Value("${mq.peakdetectionqueue}")
    private String peakDetectionQueueName;

    @PostConstruct
    public void initQueues(){
        rabbitAdmin.declareQueue(new Queue(peakDetectionQueueName, false,false,false));
    }

    @RabbitListener(queues = "${mq.peakdetectionqueue}")
    public void process(PeakDetectionRequest request){
        tourService.processPeakDetection(request);
    }

}
