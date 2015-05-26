using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class RequestException : Exception {

        public RequestException(string message)
            : base(message) {
            
        }

    }
}
