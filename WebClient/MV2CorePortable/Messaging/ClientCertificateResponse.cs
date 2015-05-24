using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.X509;
using ISS.MV2.Security;


namespace ISS.MV2.Messaging {
    public class ClientCertificateResponse : MV2Message {

        public X509Certificate Certificate {
            get {
                byte[] encoded = GetFieldDataValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
                if (encoded == null) return null;
                return new PEMFileIO().ReadCertificate(encoded);
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, value.GetEncoded());
            }
        }



        public ClientCertificateResponse()
            : base(DEF_MESSAGE.CLIENT_CERTIFICATE_RESPONSE) {
            
        }

    }
}
