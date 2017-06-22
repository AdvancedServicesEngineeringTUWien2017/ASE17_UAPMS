package ase.cm.MQInterface;

import ase.shared.StatusServiceModels.StatusServiceRequest;
import ase.shared.StatusServiceModels.StatusServiceResponse;
import ase.cm.Gateway.IoTGatewayService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by Tommi on 14.06.2017.
 */
@Component
public class StatusRequestReceiver {

    @Autowired
    private IoTGatewayService service;

    @Autowired
    private RabbitAdmin rabbitAdmin;


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${random.value}", durable = "false",exclusive = "false",autoDelete = "false")
            ,exchange = @Exchange(value="${mq.bcastexchange}",type = "fanout")))
    public StatusServiceResponse process(StatusServiceRequest request, Message meta) throws IOException {

        StatusServiceResponse response = service.processRequest(request);
        if(meta.getMessageProperties().getReplyTo() != null){
            return response;
        }
        return null;
    }
}
