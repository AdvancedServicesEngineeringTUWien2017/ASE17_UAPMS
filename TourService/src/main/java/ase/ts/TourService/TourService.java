package ase.ts.TourService;

import ase.shared.PeakDetectionModels.PeakDetectionRequest;
import ase.shared.StatusServiceModels.StatusServiceRequest;
import ase.shared.StatusServiceModels.StatusServiceResponse;
import ase.shared.TspModels.TspLocation;
import ase.shared.TspModels.TspRequest;
import ase.shared.TspModels.TspResult;
import ase.shared.TspModels.TspResultStatus;
import ase.shared.Models.SharedParkingspotStatus;
import ase.ts.RestInterface.Models.TourRequest;
import ase.ts.RestInterface.Models.TourResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Tommi on 15.06.2017.
 */
@Service
public class TourService {

    private volatile boolean peakLoad = false;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private NodeRetrieveService nodeRetrieveService;

    @Autowired
    private TourRetrieveService tourRetrieveService;

    public TourResponse generateTour(TourRequest request) throws ExecutionException, InterruptedException {

        StatusServiceRequest statusServiceRequest = getRequest(request);
        StatusServiceResponse response = nodeRetrieveService.retrieveNodes(statusServiceRequest);
        if(response != null){
            TspRequest tspRequest = getTspRequest(request, response);
            TspResult result = tourRetrieveService.retrieveTour(tspRequest, request.getPriority(), this.peakLoad);
            return get(result);
        }
        return null;
    }

    private StatusServiceRequest getRequest(TourRequest request){
        StatusServiceRequest statusServiceRequest = new StatusServiceRequest();
        statusServiceRequest.setLatitude(request.getLatitude());
        statusServiceRequest.setLongitude(request.getLongitude());
        statusServiceRequest.setRange(request.getRange());
        statusServiceRequest.setStatus(SharedParkingspotStatus.TAKEN);
        return statusServiceRequest;
    }

    private TspRequest getTspRequest(TourRequest request, StatusServiceResponse response){
        TspRequest tspRequest = new TspRequest();
        tspRequest.setLocations(new ArrayList<>());

        TspLocation baseLocation = new TspLocation();
        baseLocation.setLatitude(request.getLatitude());
        baseLocation.setLongitude(request.getLongitude());
        baseLocation.setId(0);

        tspRequest.getLocations().add(baseLocation);

        int id = 1;
        for (StatusServiceResponse.StatusServiceResponseEntry entry :
                response.getEntries()) {
            TspLocation location = new TspLocation();
            location.setId(id);
            id++;
            location.setLatitude(entry.getLatitude());
            location.setLongitude(entry.getLongitude());
            tspRequest.getLocations().add(location);
        }

        tspRequest.setQuality(get(request.getQuality()));
        tspRequest.setTimeout(request.getTimeout());

        return tspRequest;
    }

    private TspRequest.TspRequestQuality get(TourRequest.TourRequestQuality quality){
        return TspRequest.TspRequestQuality.valueOf(quality.name());
    }

    private TourResponse.TourResponseStatusCode get(TspResultStatus quality){
        return  TourResponse.TourResponseStatusCode.valueOf(quality.name());
    }

    private TourResponse get(TspResult result){

        TourResponse response = new TourResponse();
        response.setLocations(new ArrayList<>());
        int id = 0;
        if(result.getTour() != null) {
            for (TspLocation location :
                    result.getTour()) {
                TourResponse.TourLocation loc = new TourResponse.TourLocation();
                loc.setLatitude(location.getLatitude());
                loc.setLongitude(location.getLongitude());
                loc.setPosition(id);
                id++;
                response.getLocations().add(loc);
            }
            response.setCost(result.getCost());
        }
        response.setStatusCode(get(result.getStatus()));
        return response;
    }

    public void processPeakDetection(PeakDetectionRequest request){
        System.out.println("PEAK MODE CHANGED!");
        if(request.getMode() == PeakDetectionRequest.PeakDetectionMode.PEAK_STARTED){
            peakLoad = true;
        } else{
            peakLoad = false;
        }
    }
}
