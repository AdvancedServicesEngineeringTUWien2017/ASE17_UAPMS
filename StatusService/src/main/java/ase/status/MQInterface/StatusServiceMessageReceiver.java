package ase.status.MQInterface;

import ase.shared.StatusServiceModels.StatusServiceRequest;
import ase.shared.StatusServiceModels.StatusServiceResponse;
import ase.status.StatusService.StatusService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Tommi on 15.06.2017.
 */
@Component
public class StatusServiceMessageReceiver {

    @Autowired
    private StatusService statusService;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Value("${mq.statusqueue}")
    private String statusServiceQueueName;

    @PostConstruct
    public void init(){
        rabbitAdmin.declareQueue(new org.springframework.amqp.core.Queue(statusServiceQueueName));
    }

    @RabbitListener(queues = "${mq.statusqueue}")
    public StatusServiceResponse process(StatusServiceRequest request, Message meta) {
        try {
            StatusServiceResponse response = statusService.getStatus(request);
            if(meta.getMessageProperties().getReplyTo() != null){
                return response;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
