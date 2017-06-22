package ase.eval.Models;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Tommi on 16.06.2017.
 */

public interface MonitoringEntryRepository  extends MongoRepository<MonitoringEntry,String> {

    List<MonitoringEntry> findByTimeBetween(Calendar timeGT, Calendar timeLT);

}
