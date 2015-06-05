using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Pkcs;
using System.IO;
using ISS.MV2.IO;

namespace ISS.MV2.Messaging {
    public class SpaceCreationRequest : MV2Message {


        [RequiredField]
        public Pkcs10CertificationRequest SigningRequest {
            get {
                return new Pkcs10CertificationRequest(GetFieldDataValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, new byte[0]));
            }
            set {
                byte[] data = value.GetEncoded();
                System.Diagnostics.Debug.WriteLine(System.Text.Encoding.UTF8.GetString(data, 0, data.Length));
                SetMessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, data);
            }
        }

        public SpaceCreationRequest()
            : base(DEF_MESSAGE.SPACE_CREATION_REQUEST) {
        
        }


    }
}
