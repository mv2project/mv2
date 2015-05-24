using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class ClientLoginData : MV2Message {


        public byte[] DecryptedTestPhrase {
            get {
                return GetFieldDataValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, value);
            }
        }



        public ClientLoginData()
            : base(DEF_MESSAGE.CLIENT_LOGIN_DATA) {
        
        }

    }
}
