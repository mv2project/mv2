using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class InvalidResponseMessageException : Exception {

        public InvalidResponseMessageException(DEF_MESSAGE expected, DEF_MESSAGE actual):base("The server did not respond with the expected message. Expected was '" + expected.ToString() + "' but received '" + actual.ToString() + "'." ) {
            
        }

    }
}
