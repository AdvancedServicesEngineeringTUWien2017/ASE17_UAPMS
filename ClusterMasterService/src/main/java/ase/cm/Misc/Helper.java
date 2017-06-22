package ase.cm.Misc;

import ase.cm.Models.ParkingspotStatus;
import ase.shared.Models.SharedParkingspotStatus;

/**
 * Created by Tommi on 15.06.2017.
 */
public class Helper {

    public static ParkingspotStatus convertEnum(SharedParkingspotStatus model){
        if(model == SharedParkingspotStatus.FREE){
            return ParkingspotStatus.FREE;
        } else if(model == SharedParkingspotStatus.TAKEN){
            return ParkingspotStatus.TAKEN;
        }
        return null;
    }

    public static SharedParkingspotStatus convertEnum(ParkingspotStatus model){
        if(model == ParkingspotStatus.FREE){
            return SharedParkingspotStatus.FREE;
        } else if(model == ParkingspotStatus.TAKEN){
            return SharedParkingspotStatus.TAKEN;
        }
        return null;
    }
}
