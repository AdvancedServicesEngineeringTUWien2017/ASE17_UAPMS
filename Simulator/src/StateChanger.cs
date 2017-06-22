using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;

namespace ASE_2017_Simulator
{
    class StateChanger
    {
        private IModel channel;
        private Random random = new Random(42);
        private List<string> licenseplateIds;
        public StateChanger(IModel channel, List<string> licenseplates)
        {
            this.channel = channel;
            licenseplateIds = licenseplates;
        }


        public void switchState(Parkingspot spot)
        {
            ParkingspotStatus newstatus = Invert(spot.Status);
            string lpid = null;
            if (newstatus == ParkingspotStatus.TAKEN)
            {
                lpid = licenseplateIds[random.Next(licenseplateIds.Count)];
            }
            IoTRequest request = createChangeRequest(spot, lpid, newstatus);
            SendStateChange(channel, request, spot);
            spot.Status = newstatus;
            spot.LicensePlateId = lpid;
        }

        private ParkingspotStatus Invert(ParkingspotStatus status)
        {
            return (ParkingspotStatus) (1 - status);
        }

        static void SendStateChange(IModel channel, IoTRequest request, Parkingspot spot)
        {
            var props = channel.CreateBasicProperties();
            props.ContentType = "application/json";

            var s = JsonConvert.SerializeObject(request);
            var b = Encoding.UTF8.GetBytes(s);
            channel.BasicPublish("", spot.Queue, props, b);
        }

        private IoTRequest createChangeRequest(Parkingspot parkingspot, string lcid, ParkingspotStatus newstate)
        {
            IoTStateChangedDTO.IoTChangedState state = newstate == ParkingspotStatus.FREE
                ? IoTStateChangedDTO.IoTChangedState.LEFT
                : IoTStateChangedDTO.IoTChangedState.ARRIVED;
            return new IoTRequest
            {
                stateChangedDTO = new IoTStateChangedDTO
                {
                    guid = parkingspot.Guid,
                    id = parkingspot.ID,
                    licensePlateId = lcid,
                    state = state
                }
            };
        }
    }
}
