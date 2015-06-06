using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class KeyPutRequest : MV2Message {

        [RequiredField]
        public byte[] Passphrase {
            get {
                return GetFieldDataValue(DEF_MESSAGE_FIELD.HASH_BINARY, null);
            }
            set {
                if (value == null || value.Length == 0) throw new ArgumentException("The passphrase may not be null or empty.");
                SetMessageField(DEF_MESSAGE_FIELD.HASH_BINARY, value);
            }
        }

        [RequiredField]
        public byte[] PrivateKey {
            get {
                return GetFieldDataValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
            }
            set {
                if (value == null || value.Length == 0) throw new ArgumentException("The key may not be null or empty.");
                SetMessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, value);
            }
        }

        public KeyPutRequest()
            : base(DEF_MESSAGE.KEY_PUT_REQUEST) {
        
        }

    }
}
