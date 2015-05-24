using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class MessageQueryResponse : MV2Message {

        public long[] MessageIdentifiers {
            get {
                string value = GetFieldStringValue(DEF_MESSAGE_FIELD.CONTENT_PLAIN, "");
                if (value == null || string.IsNullOrEmpty(value)) return new long[0];
                string[] vals = value.Trim().Split(';');
                long[] result = new long[vals.Length];
                for (int i = 0; i < result.Length; i++) {
                    result[i] = long.Parse(vals[i]);
                }
                return result;
            }
            set {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < value.Length; i++) {
                    if (i > 0) sb.Append(";");
                    sb.Append(value[i]);
                }
                SetMessageField(DEF_MESSAGE_FIELD.CONTENT_PLAIN, sb.ToString());
            }
        }

        public MessageQueryResponse()
            : base(DEF_MESSAGE.MESSAGE_QUERY_RESPONSE) {
        }

    }
}
