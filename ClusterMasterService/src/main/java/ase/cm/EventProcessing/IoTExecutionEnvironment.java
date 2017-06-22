package ase.cm.EventProcessing;

import ase.cm.EventProcessing.Models.*;
import ase.cm.Models.ParkingSpotEvent;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.runtime.util.event.EventListener;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created by Tommi on 14.06.2017.
 */
public class IoTExecutionEnvironment {

    public ParkingspotStateChangedSinkFunktion getParkingspotStateChangedSinkFunktion() {
        return parkingspotStateChangedSinkFunktion;
    }

    public void setParkingspotStateChangedSinkFunktion(ParkingspotStateChangedSinkFunktion parkingspotStateChangedSinkFunktion) {
        this.parkingspotStateChangedSinkFunktion = parkingspotStateChangedSinkFunktion;
    }

    private ParkingspotStateChangedSinkFunktion parkingspotStateChangedSinkFunktion;

    public void initialize(StreamExecutionEnvironment environment, IoTSourceFunction function){

        try {
            DataStream<IoTRequest> root = environment.addSource(function, TypeInformation.of(IoTRequest.class));

            DataStream<IoTStateChangedDTO> stateChangedStream = root.filter(x->x.getStateChangedDTO() != null).map(x->x.getStateChangedDTO());
            DataStream<ParkingspotStateChanged> parkingspotStateChangedStream = stateChangedStream.map(x->new ParkingspotStateChanged(x));

            parkingspotStateChangedSinkFunktion = new ParkingspotStateChangedSinkFunktion();
            parkingspotStateChangedStream.addSink(parkingspotStateChangedSinkFunktion);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
