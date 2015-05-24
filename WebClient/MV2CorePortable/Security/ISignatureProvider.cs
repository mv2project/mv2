using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using Org.BouncyCastle.Crypto;

namespace ISS.MV2.Security {
    public interface ISignatureProvider {

        byte[] Sign(Stream data, AsymmetricKeyParameter privateKey);

        bool Validate(Stream data, byte[] signature, AsymmetricKeyParameter publicKey);

    }
}
