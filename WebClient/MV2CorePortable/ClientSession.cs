using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.X509;
using ISS.MV2.IO;


namespace ISS.MV2 {
    public abstract class ClientSession {

        public X509Certificate ClientCertificate { get; set; }
        public AsymmetricKeyParameter ClientPrivateKey { get; set; }

        public string Identifier { get; set; }

        public char[] Passphrase { get; set; }

        public abstract ICommunicationPartner CreateClient();

    }
}
