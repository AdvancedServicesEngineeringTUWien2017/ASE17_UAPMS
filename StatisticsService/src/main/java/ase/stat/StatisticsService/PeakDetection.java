package ase.stat.StatisticsService;

import ase.shared.PeakDetectionModels.PeakDetectionRequest;
import ase.stat.MQInterface.PeakDetectionForwarder;
import ase.stat.Models.StatusRecord;
import ase.stat.Models.ThresholdResult;
import com.mongodb.DBCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Tommi on 19.06.2017.
 */
@Component
public class PeakDetection {

    private enum PeakMode{
        PEAK, NORMAL
    }
    private PeakMode mode = PeakMode.NORMAL;

    @Value("${trafmon.peakthreshold}")
    private int PEAK_THRESHOLD;

    @Autowired
    private MongoTemplate template;

    @Autowired
    private PeakDetectionForwarder peakDetectionForwarder;

    private boolean collectionExists = false;

    @Scheduled(fixedRate = 5000)
    public void computeChanges() throws IOException {

        if(!collectionExists){
            String name = template.getCollectionName(StatusRecord.class);
            String coll = template.getCollectionNames().stream().filter(x->x.equals(name)).findFirst().orElse(null);
            if(coll == null){
                return;
            }
            collectionExists = true;
        }


        String mapFunction = Helper.readFile("threshold_map.js", StandardCharsets.UTF_8);
        String reduceFunction = Helper.readFile("threshold_reduce.js", StandardCharsets.UTF_8);

        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(new Date());
        currentTime.add(Calendar.MINUTE, -1);

        mapFunction = Helper.setParameter(mapFunction, "CURRENT", String.valueOf(currentTime.getTime().getTime()));
        MapReduceResults<ThresholdResult> results = template.mapReduce("statusRecord",mapFunction, reduceFunction,ThresholdResult.class);

        List<ThresholdResult> res = new ArrayList<>();
        results.forEach(x->res.add(x));
        if(res.size() == 1){
            int value = res.get(0).getValue();
            System.out.println(value);
            if(mode == PeakDetection.PeakMode.NORMAL){
                if(value >= PEAK_THRESHOLD){
                    mode = PeakDetection.PeakMode.PEAK;
                    peakDetectionForwarder.peakModeChanged(new PeakDetectionRequest(PeakDetectionRequest.PeakDetectionMode.PEAK_STARTED));
                }
            } else {
                if(value < PEAK_THRESHOLD){
                    mode = PeakDetection.PeakMode.NORMAL;
                    peakDetectionForwarder.peakModeChanged(new PeakDetectionRequest(PeakDetectionRequest.PeakDetectionMode.PEAK_ENDED));
                }
            }
        } else{
            if(mode != PeakDetection.PeakMode.NORMAL){
                mode = PeakDetection.PeakMode.NORMAL;
                peakDetectionForwarder.peakModeChanged(new PeakDetectionRequest(PeakDetectionRequest.PeakDetectionMode.PEAK_ENDED));
            }
        }
    }
}
