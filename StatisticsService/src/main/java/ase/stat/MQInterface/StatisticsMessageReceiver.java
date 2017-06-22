package ase.stat.MQInterface;

import ase.shared.Models.ParkingspotStatusChangeDTO;
import ase.stat.StatisticsService.StatisticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Tommi on 15.06.2017.
 */
@Component
public class StatisticsMessageReceiver {

    @Autowired
    private StatisticsService statisticsService;

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
        statisticsService.processStatisticsData(request);
    }
}
