using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ASE_2017_Simulator
{
    class IoTStateChangedDTO
    {
        public enum IoTChangedState
        {
            ARRIVED, LEFT
        }

        public long id { get; set; }
        public Guid guid { get; set; }
        public String licensePlateId { get; set; }
        public IoTChangedState state { get; set; }
    }
}
