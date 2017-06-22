package ase.cm.MQInterface;

import ase.cm.EventProcessing.Models.IoTResponse;
import ase.cm.EventProcessing.Models.IoTRequest;
import ase.cm.Gateway.IoTGatewayService;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Tommi on 14.06.2017.
 */
@Component
public class IoTMessageReceiver {

    @Autowired
    private IoTGatewayService service;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Value("${mq.iotqueue}")
    private String iotQueueName;

    @PostConstruct
    public void initQueues(){
        rabbitAdmin.declareQueue(new org.springframework.amqp.core.Queue(iotQueueName));
    }

    @RabbitListener(queues = "${mq.iotqueue}")
    public IoTResponse process(IoTRequest request, Message meta){

        IoTResponse response = service.processRequest(request);
        if(meta.getMessageProperties().getReplyTo() != null){
           return response;
        }
        return null;
    }
}
