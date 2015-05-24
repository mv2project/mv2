using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class DomainNamesResponse : MV2Message {

        public string[] AvailableDomainNames {
            get {
                string cont = GetFieldStringValue(DEF_MESSAGE_FIELD.CONTENT_PLAIN, null);
                if (cont == null) return new string[0];
                return cont.Split(';');
            }
            set {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < value.Length; i++) {
                    if (i > 0) sb.Append(";");
                    sb.Append(value[i]);
                }
                SetMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_PLAIN, sb.ToString()), true);
            }
        }

        public DomainNamesResponse()
            : base(DEF_MESSAGE.DOMAIN_NAMES_RESPONSE) {
        
        }

    }
}
