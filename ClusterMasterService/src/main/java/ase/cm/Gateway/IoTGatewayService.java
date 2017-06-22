package ase.cm.Gateway;

import ase.shared.Misc.IQueueListener;
import ase.shared.Misc.QueueListener;
import ase.shared.StatusServiceModels.StatusServiceRequest;
import ase.shared.StatusServiceModels.StatusServiceResponse;
import ase.cm.EventProcessing.Models.*;
import ase.shared.Misc.TypestaticQueue;
import ase.cm.Misc.Helper;
import ase.cm.Models.*;
import ase.shared.Models.ParkingspotStatusChangeDTO;
import ase.shared.Models.SharedParkingspotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Tommi on 14.06.2017.
 */
@Service
public class IoTGatewayService implements IQueueListener<ParkingspotStateChanged> {

    @Autowired
    private ParkingspotRepository repository;

    @Autowired
    private TypestaticQueue<ParkingspotStateChanged> parkingspotStateChangedQueue;

    @Autowired
    private TypestaticQueue<IoTRequest> requestQueue;


    @Autowired
    private TypestaticQueue<ParkingspotStatusChangeDTO> statisticsQueue;

    @Autowired
    private UUID gatewayID;


    @PostConstruct
    public void initializeListener(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(1);
        executor.initialize();
        QueueListener<ParkingspotStateChanged> stateChangedQueueListener = new QueueListener<ParkingspotStateChanged>(parkingspotStateChangedQueue, (IQueueListener)this);
        executor.execute(stateChangedQueueListener);
    }

    public StatusServiceResponse processRequest(StatusServiceRequest request){
        ParkingspotStatus status = Helper.convertEnum(request.getStatus());
        List<ParkingspotResult> result = null;
        if(request.getLatitude() == null && request.getLongitude() == null && request.getRange() == null){
            if(status != null){
                result = this.getParkingspots(status);
            } else{
                result = this.getParkingspots();
            }
        } else if(request.getLatitude() != null && request.getLongitude() != null && request.getRange() != null){
            if(status != null){
                result = this.getParkingspots(request.getLatitude(), request.getLongitude(), request.getRange(), status);
            } else{
                result = this.getParkingspots(request.getLatitude(), request.getLongitude(), request.getRange());
            }
        }
        if(result == null) {
            return null;
        }
        return getResponse(result);
    }


    private StatusServiceResponse getResponse(List<ParkingspotResult> results){
        StatusServiceResponse response = new StatusServiceResponse();
        response.setSource(this.gatewayID);
        List<StatusServiceResponse.StatusServiceResponseEntry> result = results.stream().map(x->new StatusServiceResponse.StatusServiceResponseEntry(x.getParkingspot().getLatitude(),
                x.getParkingspot().getLongitude(),x.getDistance(),
                Helper.convertEnum(x.getParkingspot().getCurrentState().getStatus()),
                x.getParkingspot().getUniqueIdentifier())).collect(Collectors.toList());
        response.setEntries(result);
        return response;
    }

    public List<ParkingspotResult> getParkingspots(){
        Iterable<Parkingspot> source = repository.findAll();
        List<Parkingspot> target = new ArrayList<>();
        source.forEach(target::add);
        return target.stream().map(x->new ParkingspotResult(x,0)).collect(Collectors.toList());
    }

    public List<ParkingspotResult> getParkingspots(ParkingspotStatus status){

        Iterable<ParkingspotResult> source = repository.findWithStatus(status);
        List<ParkingspotResult> target = new ArrayList<>();
        source.forEach(target::add);
        return target;
    }

    public List<ParkingspotResult> getParkingspots(double latitude, double longitude, double range){
        latitude = Math.toRadians(latitude);
        longitude = Math.toRadians(longitude);
        Iterable<ParkingspotResult> source = repository.findWithinRange(latitude, longitude, range);
        List<ParkingspotResult> target = new ArrayList<>();
        source.forEach(target::add);
        return target;
    }

    public List<ParkingspotResult> getParkingspots(double latitude, double longitude, double range, ParkingspotStatus status){
        latitude = Math.toRadians(latitude);
        longitude = Math.toRadians(longitude);
        Iterable<ParkingspotResult> source = repository.findWithinRangeAndStatus(latitude, longitude, range, status);
        List<ParkingspotResult> target = new ArrayList<>();
        source.forEach(target::add);
        return target;
    }

    @Override
    public void onReceive(ParkingspotStateChanged p) {

        Parkingspot parkingspot = repository.findOne(p.getId());
        if(parkingspot != null){
            ParkingspotStatus status = getStatus(p.getState());
            parkingspot.getCurrentState().setStatus(status);
            parkingspot.getCurrentState().setLicensePlateId(p.getLicensePlateId());
            repository.save(parkingspot);
            forwardStatistics(parkingspot);
        }
        System.out.println("parking spot state changed: " + p.toString());
    }

    private void forwardStatistics(Parkingspot p){
        long timestamp = System.currentTimeMillis();
        ParkingspotStatusChangeDTO dto = new ParkingspotStatusChangeDTO();
        dto.setLicensePlateId(p.getCurrentState().getLicensePlateId());
        dto.setLatitude(p.getLatitude());
        dto.setLongitude(p.getLongitude());
        dto.setTimestamp(timestamp);
        dto.setUniqueIdentifier(p.getUniqueIdentifier());
        dto.setStatus(getStatus(p.getCurrentState().getStatus()));
        dto.setSource(this.gatewayID);
        statisticsQueue.add(dto);
    }

    public IoTResponse processRequest(IoTRequest request){

        if(request.getNewSourceDTO() != null){
            // create new data source entry in repo and return id
            long id = processNewDatasource(request.getNewSourceDTO());
            IoTResponse response = new IoTResponse();
            response.setIdentifier(id);
            response.setStatus(IoTResponse.IoTResponseStatus.OK);
            return response;
        } else if(request.getStateChangedDTO() != null){
            // feed request into flink event processor
            requestQueue.add(request);
        }
        return null;
    }

    private long processNewDatasource(IoTNewSourceDTO p){

        System.out.println("new parking spot");

        Parkingspot existing = repository.findByGuid(p.getGuid());
        if(existing == null) {

            ParkingspotState state = new ParkingspotState(ParkingspotStatus.FREE);
            Parkingspot spot = new Parkingspot(p.getGuid(), p.getLatitude(), p.getLongitude(), state);


            double radLat = Math.toRadians(p.getLatitude());
            double radLon = Math.toRadians(p.getLongitude());

            double sinLat = Math.sin(radLat);
            double cosLat = Math.cos(radLat);

            spot.setSinLatitude(sinLat);
            spot.setCosLatitude(cosLat);
            spot.setLatitudeRadians(radLat);
            spot.setLongitudeRadians(radLon);

            repository.save(spot);
            long id = spot.getId();
            return id;
        } else{
            existing.getCurrentState().setStatus(ParkingspotStatus.FREE);
            existing.getCurrentState().setLicensePlateId(null);
            return existing.getId();
        }
    }

    private ParkingspotStatus getStatus(ParkingspotStateChanged.ParkingspotState state){
        if(state == ParkingspotStateChanged.ParkingspotState.ARRIVED){
            return ParkingspotStatus.TAKEN;
        } else if(state == ParkingspotStateChanged.ParkingspotState.LEFT){
            return ParkingspotStatus.FREE;
        }
        return ParkingspotStatus.UNAVAILABLE;
    }


    private SharedParkingspotStatus getStatus(ParkingspotStatus state){
        return SharedParkingspotStatus.valueOf(state.name());
    }
}
