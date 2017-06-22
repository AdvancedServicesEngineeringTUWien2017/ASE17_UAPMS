package ase.status.MQInterface;

import ase.shared.StatusServiceModels.StatusServiceRequest;
import ase.shared.StatusServiceModels.StatusServiceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Tommi on 16.06.2017.
 */
@Component
public class ClusterStatusHandler {

    private static final int REQUEST_TIMEOUT = 3000;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private ObjectMapper mapper;

    @Value("${gateways.count}")
    private int numberOfGateways;

    @Value("${mq.bcastexchange}")
    private String broadcastExchange;

    @Value("${mq.bcastqueue}")
    private String broadcastQueue;

    @PostConstruct
    public void init(){
        rabbitAdmin.declareExchange(new FanoutExchange(broadcastExchange,false,false));
        rabbitAdmin.declareQueue(new Queue(broadcastQueue));
        Binding binding = new Binding(broadcastQueue, Binding.DestinationType.QUEUE,broadcastExchange,"",null);
        rabbitAdmin.declareBinding(binding);
    }

    public List<StatusServiceResponse> gatherClusterResult(StatusServiceRequest request) throws Exception {
        String responseQueue = rabbitAdmin.declareQueue().getName();
        customConvertAndSend(request, responseQueue);

        List<StatusServiceResponse> responses = new ArrayList<>();
        long start = System.currentTimeMillis();
        long diff = 0;
        int cnt = 0;

        do{
            try {
                StatusServiceResponse tmpResponse = (StatusServiceResponse) template.receiveAndConvert(responseQueue);
                if (tmpResponse != null) {
                    responses.add(tmpResponse);
                    cnt++;
                    if(cnt >= numberOfGateways){
                        break;
                    }
                } else{
                    Thread.sleep(100);
                }
            }catch (Exception e){

            }
            diff = System.currentTimeMillis() - start;
        }while (diff < REQUEST_TIMEOUT);

        try {
            rabbitAdmin.deleteQueue(responseQueue);
        }catch (Exception e){

        }
        return responses;
    }

    private void customConvertAndSend(StatusServiceRequest request, String responseQueue) throws JsonProcessingException {
        // required cause otherwise the replyto field could not be set :(
        UUID guid = UUID.randomUUID();
        MessageProperties properties = new MessageProperties();
        properties.setReplyTo(responseQueue);
        properties.setCorrelationIdString(guid.toString());
        properties.setContentType("application/json");

        byte[] b = mapper.writeValueAsString(request).getBytes(UTF_8);
        Message msg = new Message(b,properties);
        template.send(broadcastExchange,broadcastQueue,msg);
    }

}
