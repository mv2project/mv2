using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class ClientLoginRequest : MV2Message {

        public string Identifier {
            get {
                return GetFieldStringValue(DEF_MESSAGE_FIELD.CONTENT_PLAIN, "");
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.CONTENT_PLAIN, value);
            }
        }

        public ClientLoginRequest()
            : base(DEF_MESSAGE.CLIENT_LOGIN_REQUEST) {
        
        }

    }
}
