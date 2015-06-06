using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.X509;


namespace ISS.MV2.Messaging {
    public class SpaceCreationResponse : MV2Message {

        [RequiredField]
        public X509Certificate Certificate {
            set {
                SetMessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, value.GetEncoded());
            }
            get {
                X509CertificateParser p = new X509CertificateParser();
                return p.ReadCertificate(GetFieldDataValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, null));
            }
        }

        public SpaceCreationResponse()
            : base(DEF_MESSAGE.SPACE_CREATION_RESPONSE) {
        }

    }
}
