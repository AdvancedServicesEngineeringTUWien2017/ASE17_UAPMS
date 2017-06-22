package ase.eval.RestInterface;

import ase.eval.EvalTool.EvaluationToolService;
import ase.eval.RestInterface.Models.MonitoringRequest;
import ase.eval.RestInterface.Models.MonitoringResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tommi on 16.06.2017.
 */
@RestController
public class EvaluationToolRestInterface {

    @Autowired
    private EvaluationToolService service;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public MonitoringResponse get(MonitoringRequest param) {
        return service.query(param);
    }
}
