using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.Threading.Tasks;

namespace ASE_2017_Simulator
{
    class Loader
    {
        public static List<Parkingspot> Load(string file)
        {
            var cult = new CultureInfo("en-US");
            var lines = File.ReadAllLines(file).ToList();

            return lines.Select(x => x.Split(','))
                .Select(
                    x =>
                        new Parkingspot
                        {
                            Guid = Guid.Parse(x[4]),
                            ID = -1,
                            Latitude = double.Parse(x[2], cult),
                            Longitude = double.Parse(x[3], cult),
                            Area = x[1],
                            LicensePlateId = null,
                            SimulationStatus = ParkingspotSimulationStatus.NONE,
                            Status = ParkingspotStatus.FREE,
                            Queue = x[5]
                        }).ToList();
        }

        public static List<string> LicensePlateIds(string file)
        {
            return File.ReadAllLines(file).ToList();
        }
    }
}
