package ase.stat.EventProcessing;

import ase.shared.Misc.TypestaticQueue;
import ase.stat.Models.StatisticsRecord;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.io.Serializable;

/**
 * Created by Tommi on 15.06.2017.
 */
public class StatisticsSinkFunction implements SinkFunction<StatisticsRecord>, Serializable {

    private transient TypestaticQueue<StatisticsRecord> queue;

    @Override
    public void invoke(StatisticsRecord record) throws Exception {
        if(queue == null){
            queue = new TypestaticQueue<>(StatisticsRecord.class);
        }
        queue.add(record);
    }
}
