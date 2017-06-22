using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ASE_2017_Simulator
{
    class IoTRequest
    {
        public IoTStateChangedDTO stateChangedDTO { get; set; }
        public IoTNewSourceDTO newSourceDTO { get; set; }
    }
}
