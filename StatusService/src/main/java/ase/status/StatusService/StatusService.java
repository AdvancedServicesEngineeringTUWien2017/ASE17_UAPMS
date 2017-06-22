package ase.status.StatusService;

import ase.shared.StatusServiceModels.StatusServiceRequest;
import ase.shared.StatusServiceModels.StatusServiceResponse;
import ase.status.MQInterface.ClusterStatusHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Tommi on 14.06.2017.
 */
@Service
public class StatusService {

    @Autowired
    private ClusterStatusHandler clusterStatusHandler;

    @Autowired
    private UUID serviceUUID;

    public StatusServiceResponse getStatus(StatusServiceRequest request) throws Exception {
        List<StatusServiceResponse> responses = clusterStatusHandler.gatherClusterResult(request);
        StatusServiceResponse response = new StatusServiceResponse();
        response.setSource(serviceUUID);
        response.setEntries(new ArrayList<>());

        responses.stream().forEach(x->response.getEntries().addAll(x.getEntries()));
        return response;
    }
}
