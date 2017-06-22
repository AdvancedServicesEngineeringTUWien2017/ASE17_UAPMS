package ase.cm.Gateway;

import ase.shared.Misc.TypestaticQueue;
import ase.cm.MQInterface.StatisticsForwarder;
import ase.shared.Models.ParkingspotStatusChangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Tommi on 15.06.2017.
 */
@Component
public class ForwardEventsTask {

    @Autowired
    private TypestaticQueue<ParkingspotStatusChangeDTO> statisticsQueue;

    @Autowired
    private StatisticsForwarder forwarder;

    @Scheduled(fixedRate = 2500)
    public void forwardStatistics() {
        int count = 0;
        ParkingspotStatusChangeDTO dto = null;
        while ((dto = statisticsQueue.poll()) != null){
            forwarder.forward(dto);
            count++;
        }
        if(count > 0) {
            System.out.println("Job transmitted " + count + " requests");
        }
    }
}
