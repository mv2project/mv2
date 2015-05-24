using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class MessageDeliveryRequest : MV2Message {

        public byte[] Content {
            get {
                return GetFieldDataValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, value);
            }
        }

        public string Receiver {
            get {
                return GetFieldStringValue(DEF_MESSAGE_FIELD.RECEIVER, null);
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.RECEIVER, value);
            }
        }

        public MessageDeliveryRequest()
            : base(DEF_MESSAGE.MESSAGE_DELIVERY_REQUEST) {
            
        }

    }
}
