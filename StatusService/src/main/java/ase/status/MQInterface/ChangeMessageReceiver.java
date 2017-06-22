package ase.status.MQInterface;

import ase.shared.Models.ParkingspotStatusChangeDTO;
import ase.status.StatusService.LifeChangeService;
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
 * Created by Tommi on 18.06.2017.
 */
@Component
public class ChangeMessageReceiver {

    @Autowired
    private LifeChangeService service;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Value("${mq.statisticsexchange}")
    private String statisticsExchangeName;

    @PostConstruct
    public void initQueues(){
        rabbitAdmin.declareExchange(new FanoutExchange(statisticsExchangeName, false, false));
    }

    @RabbitListener(bindings = @QueueBinding(value = @org.springframework.amqp.rabbit.annotation.Queue(value = "${random.value}", durable = "false",exclusive = "false",autoDelete = "false")
            ,exchange = @Exchange(value="${mq.statisticsexchange}",type = "fanout")))
    public void process(ParkingspotStatusChangeDTO request){
        service.changed(request);
    }
}
