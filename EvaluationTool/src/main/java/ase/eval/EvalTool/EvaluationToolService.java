package ase.eval.EvalTool;

import ase.eval.Models.MonitoringEntry;
import ase.eval.Models.MonitoringEntryRepository;
import ase.eval.RestInterface.Models.MonitoringRequest;
import ase.eval.RestInterface.Models.MonitoringResponse;
import ase.monitoring.Models.SharedMonitoringEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Tommi on 16.06.2017.
 */
@Service
public class EvaluationToolService {

    @Autowired
    private MonitoringEntryRepository repository;

    public void persist(SharedMonitoringEntry entry){
        MonitoringEntry newEntry = get(entry);
        repository.save(newEntry);
        System.out.println("received an entry");
    }

    private MonitoringEntry get(SharedMonitoringEntry entry){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        MonitoringEntry newEntry = new MonitoringEntry();
        newEntry.setOperation(entry.getOperation());
        newEntry.setId(UUID.randomUUID().toString());
        newEntry.setRequestId(entry.getGuid());
        newEntry.setRequest(entry.getRequest());
        newEntry.setResponseTime(entry.getResponseTime());
        newEntry.setTime(now);
        newEntry.setResponse(entry.getResponse());
        return newEntry;
    }

    public MonitoringResponse query(MonitoringRequest request){

        Calendar cStart = Calendar.getInstance();
        Calendar cEnd = Calendar.getInstance();

        if(request.getTimeStart() != null){
            cStart.setTime(request.getTimeStart());
        } else {
            cStart.setTime(new Date(0));
        }

        if(request.getTimeEnd() != null){
            cEnd.setTime(request.getTimeEnd());
        } else {
            cEnd.setTime(new Date(Long.MAX_VALUE));
        }

        List<MonitoringEntry> entries = repository.findByTimeBetween(cStart, cEnd);

        MonitoringResponse response = new MonitoringResponse();
        response.setEntries(entries.stream()
                .map(x->new MonitoringResponse.MonitoringResponseEntry(
                        x.getRequestId(), x.getTime(),x.getResponseTime(),x.getOperation(), x.getRequest(), x.getResponse()))
                .collect(Collectors.toList()));
        return response;
    }
}
