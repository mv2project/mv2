using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.IO;


namespace ISS.MV2.Messaging {
    public class MessageFetchResponse : MV2Message {

        public byte[] ContentMessage {
            get {
                return GetFieldDataValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, value);
            }
        }

        public DateTime Timestamp {
            get {
                return DateTimeTools.FromJavaUnixTime(BinaryTools.ReadLong(GetFieldDataValue(DEF_MESSAGE_FIELD.TIMESTAMP, BinaryTools.ToByteArray(DateTime.Now.ToJavaUnixTime()))));
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.TIMESTAMP, BinaryTools.ToByteArray(value.ToJavaUnixTime()));
            }
        }



        public MessageFetchResponse()
            : base(DEF_MESSAGE.MESSAGE_FETCH_RESPONSE) {
        
        }

    }
}
