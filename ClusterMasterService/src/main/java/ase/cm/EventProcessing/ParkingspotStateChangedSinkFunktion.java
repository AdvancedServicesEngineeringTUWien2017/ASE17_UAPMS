package ase.cm.EventProcessing;

import ase.shared.Misc.TypestaticQueue;
import ase.cm.EventProcessing.Models.ParkingspotStateChanged;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.io.Serializable;

/**
 * Created by Tommi on 14.06.2017.
 */
public class ParkingspotStateChangedSinkFunktion implements SinkFunction<ParkingspotStateChanged>, Serializable {

    private transient TypestaticQueue<ParkingspotStateChanged> queue;

    @Override
    public void invoke(ParkingspotStateChanged newParkingspot) throws Exception {
        if(queue == null){
            queue = new TypestaticQueue<>(ParkingspotStateChanged.class);
        }
        queue.add(newParkingspot);
    }
}
