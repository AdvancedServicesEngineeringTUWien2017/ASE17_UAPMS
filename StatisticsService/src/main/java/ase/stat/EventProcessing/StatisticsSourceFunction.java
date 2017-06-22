package ase.stat.EventProcessing;

import ase.shared.Misc.TypestaticQueue;
import ase.stat.Models.ParkingspotStatusChangeProcessingModel;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.io.Serializable;

/**
 * Created by Tommi on 15.06.2017.
 */
public class StatisticsSourceFunction extends RichSourceFunction<ParkingspotStatusChangeProcessingModel> implements Serializable {

    private static final long serialVersionUID = -2822618299332191613L;
    private volatile boolean isRunning = true;

    @Override
    public void run(SourceContext<ParkingspotStatusChangeProcessingModel> sourceContext) throws Exception {
        TypestaticQueue<ParkingspotStatusChangeProcessingModel> queue = new TypestaticQueue<>(ParkingspotStatusChangeProcessingModel.class);
        try {
            ParkingspotStatusChangeProcessingModel event;
            while (isRunning && (event = queue.take()) != null) {
                sourceContext.collect(event);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
