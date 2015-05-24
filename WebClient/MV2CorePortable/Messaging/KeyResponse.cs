using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class KeyResponse : MV2Message{

        public byte[] Key {
            get {
                return GetFieldDataValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, value);
            }
        }

        public KeyResponse()
            : base(DEF_MESSAGE.KEY_RESPONSE) {
        }

    }
}
