using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class CertificateResponsePreProcessor : IMessagePreProcessor {
        public MV2Message Prepare(IO.ICommunicationPartner sender, MV2Message message) {
            if (message == null) return null;
            if (message.MessageType != DEF_MESSAGE.CERT_RESPONSE) return message;
            CertificateResponseMessage certResponse = new CertificateResponseMessage();
            MV2Message.Merge(certResponse, message);
            return certResponse;
        }
    }
}
