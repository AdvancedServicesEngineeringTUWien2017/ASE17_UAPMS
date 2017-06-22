using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RabbitMQ.Client;

namespace ASE_2017_Simulator
{
    public class RabbitChannel
    {
        private static ConnectionFactory factory;
        private static IConnection connection;
        private static IModel channel;

        private RabbitChannel()
        {
            
        }

        public static IModel Instance
        {
            get
            {
                if (RabbitChannel.factory == null && RabbitChannel.connection == null && channel == null)
                {
                    //factory = new ConnectionFactory() { HostName = "192.168.99.100", Port = 40002 };
                    //factory = new ConnectionFactory() { HostName = "192.168.99.100", Port = 35002 };
                    factory = new ConnectionFactory() { HostName = "192.168.54.33", Port = 35002 };
                    connection = factory.CreateConnection();
                    channel = connection.CreateModel();
                }
                return channel;
            }
        }

        public static void Close()
        {
            channel.Close();
            connection.Close();
        }
    }
}
