package ase.stat.Models;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticsRecordRepository extends MongoRepository<StatisticsRecord,String> {
}
