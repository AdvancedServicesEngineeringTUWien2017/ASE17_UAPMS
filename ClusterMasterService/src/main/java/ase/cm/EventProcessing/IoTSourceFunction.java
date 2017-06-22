package ase.cm.EventProcessing;

import ase.shared.Misc.TypestaticQueue;
import ase.cm.EventProcessing.Models.IoTRequest;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.io.Serializable;

/**
 * Created by Tommi on 14.06.2017.
 */
public class IoTSourceFunction extends RichSourceFunction<IoTRequest> implements Serializable {

    private static final long serialVersionUID = -2822418299340191613L;
    private volatile boolean isRunning = true;

    @Override
    public void run(SourceContext<IoTRequest> sourceContext) throws Exception {
        TypestaticQueue<IoTRequest> queue = new TypestaticQueue<>(IoTRequest.class);
        try {
            IoTRequest event;
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
