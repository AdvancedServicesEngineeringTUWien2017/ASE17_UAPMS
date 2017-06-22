package ase.pm.tourworker.MQInterface;

import ase.shared.TspModels.TspRequest;
import ase.shared.TspModels.TspResult;
import ase.pm.tourworker.Solver.SolverService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tommi on 05.06.2017.
 */
@Component
public class WorkerMessageReceiver {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private SolverService solverService;

    @Value("${mq.tspjobexchange}")
    private String exchangeName;

    @Value("${mq.tspjobqueue}")
    private String queueName;



    @PostConstruct
    public void init(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",3);
        rabbitAdmin.declareExchange(new DirectExchange(exchangeName,false,false));
        rabbitAdmin.declareQueue(new org.springframework.amqp.core.Queue(queueName,false,false, false,arguments));
        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE,exchangeName,"",null);
        rabbitAdmin.declareBinding(binding);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${mq.tspjobqueue}", durable = "false",exclusive = "false",autoDelete = "false",arguments = @Argument(name = "x-max-priority", value = "3", type = "java.lang.Integer")),
            exchange = @Exchange(value = "${mq.tspjobexchange}"))
    )
    public TspResult process(TspRequest request, Message meta) throws IOException {
        if(meta.getMessageProperties().getReplyTo() != null){
            return solverService.solve(request);
        }
        return null;
    }
}
