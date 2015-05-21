using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Core.Messaging {
    public class ContentEncodingField : MessageField {

        public ContentEncodingField(Encoding encoding)
            : base(DEF_MESSAGE_FIELD.CONTENT_ENCODING, encoding.WebName) {
            
        }
    }
}
