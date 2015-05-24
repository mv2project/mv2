using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2;
using ISS.MV2.IO;

namespace ISS.MV2.Messaging {
    public class MessageQueryRequest : MV2Message {

        public DateTime? NotBefore {
            get {
                string value = GetFieldStringValue(DEF_MESSAGE_FIELD.NOT_BEFORE, null);
                if (value == null || string.IsNullOrWhiteSpace(value)) return null;
                return DateTimeTools.FromJavaUnixTime(long.Parse(value));
            }
            set {
                string val = "";
                if (value != null) val += "" + value.Value.ToJavaUnixTime();
                SetMessageField(DEF_MESSAGE_FIELD.NOT_BEFORE, val);
            }
        }



        public MessageQueryRequest()
            : base(DEF_MESSAGE.MESSAGE_QUERY_REQUEST) {
        }

    }
}
