package ase.cm.Models;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Created by Tommi on 13.06.2017.
 */
//@Repository
public interface ParkingspotRepository extends CrudRepository<Parkingspot, Long> {


    @Query("SELECT NEW ase.cm.Models.ParkingspotResult(p,0.0) FROM Parkingspot p where p.currentState.status = :statusParam")
        //p.latitude=:latParam and :lonParam > 0.0 and :rangeParam > 0.0
    List<ParkingspotResult> findWithStatus(@Param("statusParam") ParkingspotStatus status);


    @Query("SELECT NEW ase.cm.Models.ParkingspotResult(p,(6371000 * acos(sinLatitude*sin(:latParam)  + cosLatitude * cos(:latParam) * cos(longitudeRadians - :lonParam) ))) FROM Parkingspot p where (6371000 * acos(sinLatitude*sin(:latParam)  + cosLatitude * cos(:latParam) * cos(longitudeRadians - :lonParam) )) <= :rangeParam")
    //p.latitude=:latParam and :lonParam > 0.0 and :rangeParam > 0.0
    List<ParkingspotResult> findWithinRange(@Param("latParam")double lat, @Param("lonParam")double lon, @Param("rangeParam")double range);


    @Query("SELECT NEW ase.cm.Models.ParkingspotResult(p,(6371000 * acos(sinLatitude*sin(:latParam)  + cosLatitude * cos(:latParam) * cos(longitudeRadians - :lonParam) ))) FROM Parkingspot p where p.currentState.status = :statusParam and (6371000 * acos(sinLatitude*sin(:latParam)  + cosLatitude * cos(:latParam) * cos(longitudeRadians - :lonParam) )) <= :rangeParam")
    //p.latitude=:latParam and :lonParam > 0.0 and :rangeParam > 0.0
    List<ParkingspotResult> findWithinRangeAndStatus(@Param("latParam")double lat, @Param("lonParam")double lon, @Param("rangeParam")double range, @Param("statusParam") ParkingspotStatus status);


    @Query("SELECT NEW ase.cm.Models.ParkingspotResult(p, (6371000 * acos(sinLatitude*sin(:latParam)  + cosLatitude * cos(:latParam) * cos(longitudeRadians - :lonParam) ))) FROM ase.cm.Models.Parkingspot p where (6371000 * acos(sinLatitude*sin(:latParam)  + cosLatitude * cos(:latParam) * cos(longitudeRadians - :lonParam) )) <= :rangeParam order by (6371000 * acos(sinLatitude*sin(:latParam)  + cosLatitude * cos(:latParam) * cos(longitudeRadians - :lonParam) ))")
    //@Query("SELECT NEW ase.cm.Models.ParkingspotResult(p, (6371000 * acos(sinLatitude*sin(:latParam)  + cosLatitude * cos(:latParam) * cos(longitudeRadians - :lonParam) ))) FROM ase.cm.Models.Parkingspot p ")
    List<ParkingspotResult> findResult(@Param("latParam")double lat, @Param("lonParam")double lon, @Param("rangeParam")double range);

    @Query("SELECT p from Parkingspot p where p.uniqueIdentifier = :guid")
    Parkingspot findByGuid(@Param("guid")UUID guid);

}
