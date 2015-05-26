using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class InvalidResponseMessageException : Exception {

        public InvalidResponseMessageException(MV2Message expected, MV2Message actual):base("The server did not respond with the expected message. Expected was '" + expected.MessageType.ToString() + "' but received '" + actual.MessageType + "'." ) {
            
        }

    }
}
