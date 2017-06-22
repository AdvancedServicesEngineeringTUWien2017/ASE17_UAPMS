using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ASE_2017_Simulator
{
    class Parkingspot
    {
        public Guid Guid { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }
        public long ID { get; set; }
        public ParkingspotStatus Status { get; set; }
        public string LicensePlateId { get; set; }
        public ParkingspotSimulationStatus SimulationStatus { get; set; }
        public string Area { get; set; }
        public string Queue { get; set; }
    }
}
