using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class UnableToProcessMessage : MV2Message {

        public string Cause {
            get {
                return GetFieldStringValue(DEF_MESSAGE_FIELD.CAUSE, "");
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.CAUSE, value);
            }
        }

        public UnableToProcessMessage()
            : base(DEF_MESSAGE.UNABLE_TO_RPOCESS) {
        }

    }
}
