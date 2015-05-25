using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class DomainNamesResponsePreProcessor : IMessagePreProcessor {



        public MV2Message Prepare(IO.ICommunicationPartner sender, MV2Message message) {
            if (message == null || message.MessageType != DEF_MESSAGE.DOMAIN_NAMES_RESPONSE) return message;
            DomainNamesResponse dnr = new DomainNamesResponse();
            MV2Message.Merge(dnr, message9;
            return dnr;
        }
    }
}
