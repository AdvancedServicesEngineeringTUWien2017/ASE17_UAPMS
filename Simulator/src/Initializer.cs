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
    class Initializer
    {
        public void InitializeParkingspots(List<Parkingspot> parkingspots, IModel channel)
        {
            foreach (var parkingspot in parkingspots)
            {
                IoTRequest request = createInitRequest(parkingspot);
                long id = SendNewSource(request, channel, parkingspot.Queue);
                parkingspot.SimulationStatus = ParkingspotSimulationStatus.CREATED;
                parkingspot.ID = id;
            }
        }


        private int SendNewSource(IoTRequest request, IModel channel, string queue)
        {
            channel.QueueDeclare(queue: queue, durable: true, exclusive: false, autoDelete: false);

            var replyQueueName = channel.QueueDeclare().QueueName;

            var props = channel.CreateBasicProperties();
            props.ReplyTo = replyQueueName;
            props.CorrelationId = Guid.NewGuid().ToString();
            props.ContentType = "application/json";
            var s = JsonConvert.SerializeObject(request);
            var b = Encoding.UTF8.GetBytes(s);

            channel.BasicPublish("", queue, props, b);

            var consumer = new QueueingBasicConsumer(channel);
            channel.BasicConsume(replyQueueName, true, consumer);
            var ea = (BasicDeliverEventArgs)consumer.Queue.Dequeue();
            try
            {
                if (ea.BasicProperties.CorrelationId == props.CorrelationId)
                {
                    IoTResponse resp = JsonConvert.DeserializeObject<IoTResponse>(Encoding.UTF8.GetString(ea.Body));
                    return (int) resp.identifier;
                }
            }
            catch (Exception e)
            {

            }
            finally
            {
                channel.QueueDelete(replyQueueName);
            }
            return -1;
        }

        private IoTRequest createInitRequest(Parkingspot parkingspot)
        {
            return new IoTRequest
            {
                newSourceDTO = new IoTNewSourceDTO
                {
                    guid = parkingspot.Guid,
                    Latitude = parkingspot.Latitude,
                    Longitude = parkingspot.Longitude
                }
            };
        }
    }
}
