package ase.cm.MQInterface;

import ase.shared.Models.ParkingspotStatusChangeDTO;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Tommi on 16.06.2017.
 */
@Component
public class StatisticsForwarder {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Value("${mq.statisticsexchange}")
    private String statisticsExchangeName;

    @PostConstruct
    public void initQueues(){
        rabbitAdmin.declareExchange(new FanoutExchange(statisticsExchangeName,false,false));
    }

    public void forward(ParkingspotStatusChangeDTO dto) {
        template.convertAndSend(statisticsExchangeName, "", dto);
    }
}
