package ase.status.StatusService;

import ase.shared.StatusServiceModels.StatusServiceRequest;
import ase.shared.StatusServiceModels.StatusServiceResponse;
import ase.shared.Models.ParkingspotStatusChangeDTO;
import ase.status.Models.MonitoringResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tommi on 18.06.2017.
 */
@Service
public class LifeChangeService {

    @Autowired
    private StatusService statusService;

    private ConcurrentHashMap<UUID,ParkingspotStatusChangeDTO> statusMap = new ConcurrentHashMap<>();
    //private boolean initialized = false;

    @PostConstruct
    public void init(){
        /*StatusServiceRequest request = new StatusServiceRequest();
        try {
            StatusServiceResponse response = statusService.getStatus(request);
            for (StatusServiceResponse.StatusServiceResponseEntry entry : response.getEntries()) {
                ParkingspotStatusChangeDTO newEntry = new ParkingspotStatusChangeDTO();
                newEntry.setStatus(entry.getStatus());
                newEntry.setUniqueIdentifier(entry.getUniqueIdentifier());
                newEntry.setLatitude(entry.getLatitude());
                newEntry.setLongitude(entry.getLongitude());
                statusMap.put(entry.getUniqueIdentifier(), newEntry);
            }
        }
        catch (Exception e){

        }
        initialized = true;*/
    }

    public void changed(ParkingspotStatusChangeDTO dto){
        /*if(initialized){
            statusMap.put(dto.getUniqueIdentifier(), dto);
        }*/
        statusMap.put(dto.getUniqueIdentifier(), dto);
    }

    public MonitoringResponse getCurrentStatus(){
        MonitoringResponse response = new MonitoringResponse();
        response.setEntries(new ArrayList<>());
        for (ParkingspotStatusChangeDTO mapEntry:
             statusMap.values()) {
            MonitoringResponse.MonitoringResponseEntry entry = new  MonitoringResponse.MonitoringResponseEntry();
            entry.setLatitude(mapEntry.getLatitude());
            entry.setLongitude(mapEntry.getLongitude());
            entry.setUniqueIdentifier(mapEntry.getUniqueIdentifier());
            entry.setStatus(mapEntry.getStatus());
            entry.setSource(mapEntry.getSource());
            response.getEntries().add(entry);
        }
        return response;
    }
}
