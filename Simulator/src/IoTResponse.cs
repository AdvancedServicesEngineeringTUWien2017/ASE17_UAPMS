using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ASE_2017_Simulator
{
    class IoTResponse
    {
        public enum IoTResponseStatus
        {
            OK,FAIL
        }

        public IoTResponseStatus status { get; set; }
        public long identifier { get; set; }
    }
}
