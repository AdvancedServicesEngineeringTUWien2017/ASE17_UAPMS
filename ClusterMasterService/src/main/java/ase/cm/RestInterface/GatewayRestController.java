package ase.cm.RestInterface;

import ase.shared.StatusServiceModels.StatusServiceRequest;
import ase.shared.StatusServiceModels.StatusServiceResponse;
import ase.cm.Gateway.IoTGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tommi on 14.06.2017.
 */
@RestController
public class GatewayRestController {

    @Autowired
    private IoTGatewayService service;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity<StatusServiceResponse> status(StatusServiceRequest param) {

        StatusServiceResponse response = service.processRequest(param);

        if(response == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(response);
    }
}
