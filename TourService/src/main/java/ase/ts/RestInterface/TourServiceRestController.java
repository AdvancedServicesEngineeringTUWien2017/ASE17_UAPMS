package ase.ts.RestInterface;

import ase.shared.TspModels.TspLocation;
import ase.shared.TspModels.TspRequest;
import ase.shared.TspModels.TspResult;
import ase.ts.RestInterface.Models.TourRequest;
import ase.ts.RestInterface.Models.TourResponse;
import ase.ts.TourService.TourRetrieveService;
import ase.ts.TourService.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by Tommi on 15.06.2017.
 */
@RestController
public class TourServiceRestController {

    @Autowired
    private TourService tourService;

    @Autowired
    private TourRetrieveService service;


    @RequestMapping(value = "/tour", method = RequestMethod.GET)
    public ResponseEntity<TourResponse> tourRequest(TourRequest param) throws Exception {
        TourResponse response = tourService.generateTour(param);
        return ResponseEntity.ok(response);
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<TspResult> test(){
        TspRequest request = new TspRequest();
        request.setQuality(TspRequest.TspRequestQuality.MEDIUM);
        request.setTimeout(100);
        request.setLocations(new ArrayList<>());

        /*double baseLatitude = 48.2176748842985, baseLongitude = 16.3698089695579;
        double factor = 0.000025;
        for(int i= 0;i<10;i++){
            double lat = baseLatitude * (1+i*factor);
            double lon = baseLongitude * (1+i*factor);

            TspLocation location = new TspLocation();
            location.setId(i);
            location.setLatitude(lat);
            location.setLongitude(lon);
            request.getLocations().add(location);
        }*/
        for(int i = 0;i<ary.length;i+=2){
            double lat = ary[i];
            double lon = ary[i+1];

            TspLocation location = new TspLocation();
            location.setId(i);
            location.setLatitude(lat);
            location.setLongitude(lon);
            request.getLocations().add(location);
        }
        return ResponseEntity.ok(service.retrieveTour(request, TourRequest.TourRequestPriority.HIGH, false));
    }



    private double[] ary = new double[]{48.21654865123,16.3684424790985,48.2049019447126,16.3699639970202,48.2109475227586,16.3606831667575,48.207075787499,16.3631917377056,48.2046955824067,16.3623841127784,48.2092539143727,16.3674060184684,48.21657964405,16.368956895546,48.1999274539431,16.3733596000104,48.2097864287468,16.3635700370237,48.2202308381388,16.3701823383284,48.2108164671562,16.361898560702,48.2067111881494,16.3751711073986,48.2137182984775,16.3683473089793,48.217531611454,16.3648718043201,48.2027163610194,16.3703464252717,48.2152465725028,16.3740618903325,48.2067431029361,16.3776588573068,48.1998358122696,16.3699665944385,48.2027046980302,16.3704353207781,48.207149700831,16.3680947898227,48.2046299982551,16.3691525841253,48.1986264214754,16.3711521661636,48.2180044457395,16.3643314065953};


}
