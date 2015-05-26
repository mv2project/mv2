using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Crypto.Digests;


namespace ISS.MV2.Security {
    public class SHA256 : BCDigest {


        public SHA256()
            : base(new CloneBouncyCastleDigestDelegate(() => { return new Sha256Digest(); }), new Sha256Digest(), "SHA-256", "SHA 256", "SHA256") {
        }

    }
}
