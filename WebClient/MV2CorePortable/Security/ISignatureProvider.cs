using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using Org.BouncyCastle.Crypto;

namespace ISS.MV2.Security {
    public interface ISignatureProvider {

        public byte[] Sign(Stream data, AsymmetricKeyParameter privateKey);

        public bool Validate(Stream data, byte[] signature, AsymmetricKeyParameter publicKey);

    }
}
