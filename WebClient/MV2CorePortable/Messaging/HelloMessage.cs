using System;
using System.Collections.Generic;

using System.Text;

namespace ISS.MV2.Core.Messaging {
    public class HelloMessage : MV2Message {

        public string HostName {
            get {
                return GetFieldStringValue(DEF_MESSAGE_FIELD.CONTENT_PLAIN, "");
            }
            set {
                SetMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_PLAIN, value), true);
            }
        }

        public HelloMessage()
            : base(DEF_MESSAGE.HELLO) {
            
        }

        

    }
}
