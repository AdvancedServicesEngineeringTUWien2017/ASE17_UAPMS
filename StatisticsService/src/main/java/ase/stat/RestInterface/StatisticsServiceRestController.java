package ase.stat.RestInterface;

import ase.stat.Models.HourStatisticsModel;
import ase.stat.Models.LicenseplateStatisticsModel;
import ase.stat.RestInterface.Models.HourStatisticsResponse;
import ase.stat.RestInterface.Models.LicensePlateStatisticsResponse;
import ase.stat.RestInterface.Models.StatisticsRequest;
import ase.stat.StatisticsService.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by Tommi on 15.06.2017.
 */
@RestController
public class StatisticsServiceRestController {



    @Autowired
    private StatisticsService service;

    @RequestMapping(value = "/lpstatistics", method = RequestMethod.GET)
    public ResponseEntity<LicensePlateStatisticsResponse> lpstatistics(StatisticsRequest param) throws Exception {
        double latitude, longitude;
        Double distance;
        Calendar starttime = null;
        Calendar endtime = null;
        if(param.getLatitude() == null || param.getLongitude() == null){
            distance = null;
            latitude = 0;
            longitude = 0;
        } else{
            distance = param.getDistance();
            latitude = param.getLatitude();
            longitude = param.getLongitude();
        }

        if(param.getMinTime() != null){
            starttime = Calendar.getInstance();
            starttime.setTime(new Date(param.getMinTime()));
        }
        if(param.getMaxTime() != null){
            endtime = Calendar.getInstance();
            endtime.setTime(new Date(param.getMaxTime()));
        }
        LicenseplateStatisticsModel result = service.getLicenseplateIdDistrictStatistics(latitude,longitude,distance, starttime, endtime);
        return ResponseEntity.ok(get(result));
    }


    @RequestMapping(value = "/hourstatistics", method = RequestMethod.GET)
    public ResponseEntity<HourStatisticsResponse> hourstatistics(StatisticsRequest param) throws Exception {
        double latitude, longitude;
        Double distance;
        Calendar starttime = null;
        Calendar endtime = null;
        if(param.getLatitude() == null || param.getLongitude() == null){
            distance = null;
            latitude = 0;
            longitude = 0;
        } else{
            distance = param.getDistance();
            latitude = param.getLatitude();
            longitude = param.getLongitude();
        }

        if(param.getMinTime() != null){
            starttime = Calendar.getInstance();
            starttime.setTime(new Date(param.getMinTime()));
        }
        if(param.getMaxTime() != null){
            endtime = Calendar.getInstance();
            endtime.setTime(new Date(param.getMaxTime()));
        }
        HourStatisticsModel result = service.getHourStatistics(latitude,longitude,distance, starttime, endtime);
        return ResponseEntity.ok(get(result));
    }

    private LicensePlateStatisticsResponse get(LicenseplateStatisticsModel m){
        LicensePlateStatisticsResponse response = new LicensePlateStatisticsResponse();
        response.setEntries(m.getEntries().stream().map(x->new LicensePlateStatisticsResponse.LicensePlateStatisticsResponseEntry(x.getDistrict(),x.getCount())).collect(Collectors.toList()));
        return response;
    }

    private HourStatisticsResponse get(HourStatisticsModel m){
        HourStatisticsResponse response = new HourStatisticsResponse();
        response.setEntries(m.getEntries().stream().map(x->new HourStatisticsResponse.HourStatisticsResponseEntry(x.getHour(),x.getCount())).collect(Collectors.toList()));
        return response;
    }
}
