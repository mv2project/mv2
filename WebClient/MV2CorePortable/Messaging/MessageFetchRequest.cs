using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.IO;


namespace ISS.MV2.Messaging {
    public class MessageFetchRequest : MV2Message {

        public long Identifier {
            get {
                return BinaryTools.ReadLong(GetFieldDataValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, BinaryTools.ToByteArray((long)0)));
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, BinaryTools.ToByteArray(value));
            }
        }



        public MessageFetchRequest():base(DEF_MESSAGE.MESSAGE_FETCH_REQUEST) {
            
        }

    }
}
