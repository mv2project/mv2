using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Crypto.Digests;

namespace ISS.MV2.Security {
    public class SHA384 : BCDigest {

        public SHA384()
            : base(new CloneBouncyCastleDigestDelegate(() => { return new Sha384Digest(); }), new Sha384Digest(), "SHA-384", "SHA 384", "SHA384") {
        
        }

    }
}
