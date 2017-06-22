package ase.stat.StatisticsService;

import ase.shared.Misc.IQueueListener;
import ase.shared.Misc.QueueListener;
import ase.shared.Misc.TypestaticQueue;
import ase.shared.Models.ParkingspotStatusChangeDTO;
import ase.shared.Models.SharedParkingspotStatus;
import ase.shared.PeakDetectionModels.PeakDetectionRequest;
import ase.stat.MQInterface.PeakDetectionForwarder;
import ase.stat.Models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Tommi on 15.06.2017.
 */
@Service
public class StatisticsService  implements IQueueListener<StatisticsRecord>{

    @Autowired
    private MongoTemplate template;

    @Autowired
    private TypestaticQueue<StatisticsRecord> statisticsRecordQueue;

    @Autowired
    private TypestaticQueue<ParkingspotStatusChangeProcessingModel> rawdataQueue;

    @Autowired
    private StatisticsRecordRepository repository;

    @Autowired
    private StatusRecordRepository statusRecordRepository;


    @PostConstruct
    public void initializeListener(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(1);
        executor.initialize();
        QueueListener<StatisticsRecord> stateChangedQueueListener = new QueueListener<StatisticsRecord>(statisticsRecordQueue, (IQueueListener)this);
        executor.execute(stateChangedQueueListener);
    }

    public void processStatisticsData(ParkingspotStatusChangeDTO dto){
        ParkingspotStatusChangeProcessingModel model = new ParkingspotStatusChangeProcessingModel(dto.getUniqueIdentifier(), dto.getLicensePlateId(), dto.getLatitude(), dto.getLongitude(), dto.getStatus(), dto.getTimestamp());
        rawdataQueue.add(model);
        this.storeStatus(dto);
    }

    @Override
    public void onReceive(StatisticsRecord p) {
        repository.save(p);
    }




    public LicenseplateStatisticsModel getLicenseplateIdDistrictStatistics(double latitude, double longitude, Double distance, Calendar start, Calendar end) throws Exception{

        latitude = Math.toRadians(latitude);
        longitude = Math.toRadians(longitude);

        String mapFunction = Helper.readFile("lps_map.js", StandardCharsets.UTF_8);
        String reduceFunction = Helper.readFile("lps_reduce.js", StandardCharsets.UTF_8);

        mapFunction = parameterizeMap(mapFunction, latitude, longitude, distance, start, end);

        MapReduceResults<LPStatisticsResult> results = template.mapReduce("statisticsRecord",mapFunction, reduceFunction,LPStatisticsResult.class);
        LicenseplateStatisticsModel model = new LicenseplateStatisticsModel();
        model.setEntries(new ArrayList<>());
        results.forEach(x->model.getEntries().add(new LicenseplateStatisticsModel.LicensePlateStatisticsEntry(x.getId(),x.getValue())));
        return model;
    }


    public HourStatisticsModel getHourStatistics(double latitude, double longitude, Double distance, Calendar start, Calendar end) throws Exception{

        latitude = Math.toRadians(latitude);
        longitude = Math.toRadians(longitude);

        String mapFunction = Helper.readFile("hour_map.js", StandardCharsets.UTF_8);
        String reduceFunction = Helper.readFile("hour_reduce.js", StandardCharsets.UTF_8);

        mapFunction = parameterizeMap(mapFunction, latitude, longitude, distance, start, end);

        MapReduceResults<LPStatisticsResult> results = template.mapReduce("statisticsRecord",mapFunction, reduceFunction,LPStatisticsResult.class);
        HourStatisticsModel model = new HourStatisticsModel();
        model.setEntries(new ArrayList<>());
        results.forEach(x->model.getEntries().add(new HourStatisticsModel.HourStatisticsModelEntry(x.getId(),x.getValue())));
        return model;
    }

    private String parameterizeMap(String mapFunction, double latitude, double longitude, Double distance, Calendar start, Calendar end){
        mapFunction = Helper.setParameter(mapFunction, "LAT", String.format(Locale.ENGLISH, "%.8f",latitude));
        mapFunction = Helper.setParameter(mapFunction, "LON", String.format(Locale.ENGLISH, "%.8f",longitude));
        if(distance != null){
            mapFunction = Helper.setParameter(mapFunction, "DIST", String.format(Locale.ENGLISH, "%.8f",latitude));
            mapFunction =Helper. setParameter(mapFunction, "NODIST", "false");
        } else{
            mapFunction = Helper.setParameter(mapFunction, "DIST", "0");
            mapFunction = Helper.setParameter(mapFunction, "NODIST", "true");
        }
        if(start != null){
            mapFunction = Helper.setParameter(mapFunction, "TIMEMIN", String.valueOf(start.getTime().getTime()));
            mapFunction = Helper.setParameter(mapFunction, "NOMINTIME", "false");
        }
        else {
            mapFunction = Helper.setParameter(mapFunction, "TIMEMIN", "0");
            mapFunction = Helper.setParameter(mapFunction, "NOMINTIME", "true");
        }
        if(end != null){
            mapFunction = Helper.setParameter(mapFunction, "TIMEMAX", String.valueOf(end.getTime().getTime()));
            mapFunction = Helper.setParameter(mapFunction, "NOMAXTIME", "false");
        }
        else {
            mapFunction = Helper.setParameter(mapFunction, "TIMEMAX", "0");
            mapFunction = Helper.setParameter(mapFunction, "NOMAXTIME", "true");
        }
        return mapFunction;
    }


    private void storeStatus(ParkingspotStatusChangeDTO dto){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        StatusRecord record = new StatusRecord();
        record.setParkingSpotId(dto.getUniqueIdentifier());
        record.setLatitude(dto.getLatitude());
        record.setLongitude(dto.getLongitude());
        record.setStatus(dto.getStatus());
        record.setStateChangeTime(c);
        record.setLicensePlateId(dto.getLicensePlateId());
        record.setSource(dto.getSource());
        statusRecordRepository.save(record);
    }

}
