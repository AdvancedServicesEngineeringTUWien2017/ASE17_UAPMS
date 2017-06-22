package ase.status.RestInterface;

import ase.shared.StatusServiceModels.StatusServiceRequest;
import ase.shared.StatusServiceModels.StatusServiceResponse;
import ase.status.Models.MonitoringResponse;
import ase.status.StatusService.LifeChangeService;
import ase.status.StatusService.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tommi on 14.06.2017.
 */
@RestController
public class StatusServiceRestController {

    @Autowired
    private StatusService statusService;

    @Autowired
    private LifeChangeService lifeChangeService;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity<StatusServiceResponse> status(StatusServiceRequest param) {

        try {
            StatusServiceResponse response = statusService.getStatus(param);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<MonitoringResponse> all() {
        MonitoringResponse response = lifeChangeService.getCurrentStatus();
        return ResponseEntity.ok(response);
    }
}
