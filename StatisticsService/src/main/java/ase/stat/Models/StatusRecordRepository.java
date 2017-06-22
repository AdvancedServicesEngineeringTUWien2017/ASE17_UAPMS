package ase.stat.Models;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

/**
 * Created by tommi on 19.06.2017.
 */
public interface StatusRecordRepository extends MongoRepository<StatusRecord,String> {



}