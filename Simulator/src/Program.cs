using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ASE_2017_Simulator
{
    class Program
    {
        static void Main(string[] args)
        {
            double steptime = 0.5;
            var parkingspots = Loader.Load("newout.txt");
            var lpIds = Loader.LicensePlateIds("newlp.txt");
            var channel = RabbitChannel.Instance;

            var initializer = new Initializer();
            var changer = new StateChanger(channel, lpIds);
            initializer.InitializeParkingspots(parkingspots, channel);

            Debug.Assert(parkingspots.TrueForAll(x => x.SimulationStatus == ParkingspotSimulationStatus.CREATED && x.ID >= 0));
            Console.WriteLine("Press T for Turbo Mode, N for Normal Mode");
            Console.WriteLine("init done: press enter to contine");
            Console.ReadLine();
            var random = new Random(42);
            while (true)
            {
                Thread.Sleep((int)(steptime * 1000));
                var parkingspot = parkingspots[random.Next(parkingspots.Count)];
                changer.switchState(parkingspot);

                if (Console.KeyAvailable)
                {
                    if (Console.ReadKey().Key == ConsoleKey.T)
                    {
                        Console.WriteLine("\r\nTurbo Mode enabled!");
                        steptime = 0.1;
                    }
                    else if (Console.ReadKey().Key == ConsoleKey.N)
                    {
                        Console.WriteLine("\r\nNormal Mode enabled!");
                        steptime = 0.5;
                    }
                }
            }
            RabbitChannel.Close();
        }
      
    }
}
