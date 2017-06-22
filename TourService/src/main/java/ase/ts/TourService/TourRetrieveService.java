package ase.ts.TourService;

import ase.shared.TspModels.TspRequest;
import ase.shared.TspModels.TspResult;
import ase.shared.TspModels.TspResultStatus;
import ase.ts.RestInterface.Models.TourRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Tommi on 15.06.2017.
 */
@Service
public class TourRetrieveService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private ObjectMapper mapper;

    @Value("${mq.tspjobqueue}")
    private String tspjobQueueName;

    @Value("${mq.tspjobexchange}")
    private String tspjobExchangeName;

    private final static double PREPROCESSING_FACTOR = 1.5;
    private final static int HIGH_LOAD_THRESHOLD = 8;



    private AtomicInteger currentProcessingCount = new AtomicInteger(0);

    private TourRequest.TourRequestPriority decideActualPriority(TourRequest.TourRequestPriority priority, boolean peakLoad){
        return priority;
    }

    private TourRequest.TourRequestQuality decideActualQuality(TourRequest.TourRequestQuality quality, boolean peakLoad){
        if(peakLoad && currentProcessingCount.get() >= HIGH_LOAD_THRESHOLD){
            return TourRequest.TourRequestQuality.LOW;
        } else{
            return quality;
        }
    }

    private void setPriorityAdaptions(TspResult result, TourRequest.TourRequestPriority actual, TourRequest.TourRequestPriority requested){
        if(actual != requested){
            result.setPriorityAdaptions(TspResult.TspResultElasticAdaptionResult.CHANGED);
        } else{
            result.setPriorityAdaptions(TspResult.TspResultElasticAdaptionResult.ORIGINAL);
        }
    }
    private void setQualityAdaptions(TspResult result, TourRequest.TourRequestQuality actual, TourRequest.TourRequestQuality requested){
        if(actual != requested){
            result.setQualityAdaptions(TspResult.TspResultElasticAdaptionResult.CHANGED);
        } else{
            result.setQualityAdaptions(TspResult.TspResultElasticAdaptionResult.ORIGINAL);
        }
    }

    public TspResult retrieveTour(TspRequest request, TourRequest.TourRequestPriority priority, boolean peakLoad){

        int actualTimeout = (int)Math.ceil(PREPROCESSING_FACTOR * request.getTimeout()) * 1000; //for seconds
        TourRequest.TourRequestQuality quality = get(request.getQuality());
        String responseQueue = null;
        boolean incremented = false;

        try {
            responseQueue = rabbitAdmin.declareQueue().getName();

            TourRequest.TourRequestPriority actualPriority = this.decideActualPriority(priority, peakLoad);
            TourRequest.TourRequestQuality actualQuality = this.decideActualQuality(quality, peakLoad);
            request.setQuality(get(actualQuality));

            customSend(request, actualPriority, responseQueue);
            currentProcessingCount.incrementAndGet();
            incremented = true;

            TspResult response = (TspResult)rabbitTemplate.receiveAndConvert(responseQueue, actualTimeout);
            currentProcessingCount.decrementAndGet();
            if(response != null) {
                setPriorityAdaptions(response, actualPriority, priority);
                setQualityAdaptions(response, actualQuality, quality);
                return response;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            if(incremented){
                currentProcessingCount.decrementAndGet();
            }
        }
        finally {
            if(responseQueue != null) {
                rabbitAdmin.deleteQueue(responseQueue);
            }
        }
        TspResult failedResult = new TspResult();
        failedResult.setStatus(TspResultStatus.TIMEOUT);
        return failedResult;
    }

    private TourRequest.TourRequestQuality get(TspRequest.TspRequestQuality quality){
        return TourRequest.TourRequestQuality.valueOf(quality.name());
    }

    private TspRequest.TspRequestQuality get(TourRequest.TourRequestQuality quality){
        return TspRequest.TspRequestQuality.valueOf(quality.name());
    }

    private void customSend(TspRequest request, TourRequest.TourRequestPriority priority, String responseQueue) throws JsonProcessingException {
        UUID guid = UUID.randomUUID();
        MessageProperties properties = new MessageProperties();
        properties.setReplyTo(responseQueue);
        properties.setCorrelationIdString(guid.toString());
        properties.setPriority(priority.ordinal());
        properties.setContentType("application/json");

        byte[] b = mapper.writeValueAsString(request).getBytes(UTF_8);
        Message msg = new Message(b,properties);
        rabbitTemplate.send("",tspjobQueueName,msg);
    }
}
