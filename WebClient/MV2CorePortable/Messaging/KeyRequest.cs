using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class KeyRequest : MV2Message {

        public byte[] Passphrase {
            get {
                return GetFieldDataValue(DEF_MESSAGE_FIELD.HASH_BINARY, null);
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.HASH_BINARY, value);
            }
        }

        public KeyRequest()
            : base(DEF_MESSAGE.KEY_REQUEST) {
        }

    }
}
