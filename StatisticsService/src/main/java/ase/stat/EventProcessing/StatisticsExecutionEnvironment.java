package ase.stat.EventProcessing;

import ase.shared.Models.ParkingspotStatusChangeDTO;
import ase.shared.Models.SharedParkingspotStatus;
import ase.stat.Models.ParkingspotStatusChangeProcessingModel;
import ase.stat.Models.StatisticsRecord;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternFlatSelectFunction;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.util.Collector;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tommi on 15.06.2017.
 */
public class StatisticsExecutionEnvironment {

    public StatisticsSinkFunction getParkingspotStateChangedSinkFunktion() {
        return sinkFunktion;
    }

    public void setParkingspotStateChangedSinkFunktion(StatisticsSinkFunction sinkFunktion) {
        this.sinkFunktion = sinkFunktion;
    }

    private StatisticsSinkFunction sinkFunktion;

    public void initialize(StreamExecutionEnvironment environment, StatisticsSourceFunction function){

        try {
            DataStream<ParkingspotStatusChangeProcessingModel> root = environment.addSource(function, TypeInformation.of(ParkingspotStatusChangeProcessingModel.class));

            DataStream<ParkingspotStatusChangeProcessingModel> keyedStream = root.keyBy(x->x.getUniqueIdentifier());

            Pattern<ParkingspotStatusChangeProcessingModel,ParkingspotStatusChangeProcessingModel> pattern =
                    Pattern.<ParkingspotStatusChangeProcessingModel>begin("taken")
                            .where(x->x.getStatus() == SharedParkingspotStatus.TAKEN)
                            .next("free")
                            .where(x->x.getStatus() == SharedParkingspotStatus.FREE);

            DataStream<StatisticsRecord> records = CEP.pattern(keyedStream,pattern).select(new PatternSelectFunction<ParkingspotStatusChangeProcessingModel, StatisticsRecord>() {
                @Override
                public StatisticsRecord select(Map<String, ParkingspotStatusChangeProcessingModel> map) throws Exception {
                    ParkingspotStatusChangeProcessingModel taken = map.get("taken");
                    ParkingspotStatusChangeProcessingModel free = map.get("free");
                    if(taken != null && free != null){
                        Calendar start = Calendar.getInstance();
                        start.setTime(new Date(taken.getTimestamp()));
                        Calendar end = Calendar.getInstance();
                        end.setTime(new Date(free.getTimestamp()));

                        int hours =  (int)Math.ceil(((double)(free.getTimestamp() - taken.getTimestamp())) / (1000 * 60 * 60));

                        StatisticsRecord record = new StatisticsRecord();
                        record.setArrival(start);
                        record.setLeft(end);
                        record.setParkingSpotId(taken.getUniqueIdentifier());
                        record.setId(UUID.randomUUID().toString());
                        record.setLatitude(taken.getLatitude());
                        record.setLongitude(taken.getLongitude());
                        record.setStartHour(start.get(Calendar.HOUR_OF_DAY));
                        record.setTotalHours(hours);
                        record.setLicensePlateId(taken.getLicensePlateId());

                        return record;
                    }
                    else{
                        return null;
                    }
                }
            });

            sinkFunktion = new StatisticsSinkFunction();
            records.addSink(sinkFunktion);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
