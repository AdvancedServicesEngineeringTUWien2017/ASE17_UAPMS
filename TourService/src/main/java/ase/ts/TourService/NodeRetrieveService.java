package ase.ts.TourService;

import ase.shared.StatusServiceModels.StatusServiceRequest;
import ase.shared.StatusServiceModels.StatusServiceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Tommi on 15.06.2017.
 */
@Service
public class NodeRetrieveService {

    private static final int RESPONSE_TIMEOUT = 5000;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private ObjectMapper mapper;

    @Value("${mq.statusqueue}")
    private String statusQueueName;

    public StatusServiceResponse retrieveNodes(StatusServiceRequest request)  {

        String responseQueue = null;
        try {
            responseQueue = rabbitAdmin.declareQueue().getName();
            customConvertAndSend(request, responseQueue);
            StatusServiceResponse response = (StatusServiceResponse)rabbitTemplate.receiveAndConvert(responseQueue, RESPONSE_TIMEOUT);
            if(response != null){
                return response;
            }
            return null;
        }
        catch (IOException e) {
            return null;
        }
        finally {
            if (responseQueue != null) {
                rabbitAdmin.deleteQueue(responseQueue);
            }
        }
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
        rabbitTemplate.send(statusQueueName,msg);
    }
}
