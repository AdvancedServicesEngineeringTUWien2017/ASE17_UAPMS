package ase.eval.MQInterface;

import ase.eval.EvalTool.EvaluationToolService;
import ase.monitoring.Models.SharedMonitoringEntry;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Tommi on 16.06.2017.
 */
@Component
public class MonitoringMessageReceiver {

    @Autowired
    private EvaluationToolService evaluationToolService;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Value("${mq.monitoringqueue}")
    private String monitoringQueueName;

    @PostConstruct
    public void initQueues(){
        rabbitAdmin.declareQueue(new Queue(monitoringQueueName));
    }

    @RabbitListener(queues = "${mq.monitoringqueue}")
    public void process(SharedMonitoringEntry request){
        evaluationToolService.persist(request);
    }
}
