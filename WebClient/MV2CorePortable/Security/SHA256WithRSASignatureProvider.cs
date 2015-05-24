using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using ISS.MV2.IO;
using Org.BouncyCastle.Crypto.Signers;
using Org.BouncyCastle.Crypto.Digests;


namespace ISS.MV2.Security {
    public class SHA256WithRSASignatureProvider : ISignatureProvider {


        public byte[] Sign(System.IO.Stream data, Org.BouncyCastle.Crypto.AsymmetricKeyParameter privateKey) {
            Sha256Digest shaDigest = new Sha256Digest();
            RsaDigestSigner signer = new RsaDigestSigner(shaDigest);
            signer.Init(true, privateKey);
            data.Iterate(new StreamExtension.BinIterationDelegate((b) => {
                signer.Update(b);
            }));
            return signer.GenerateSignature();
        }

        public bool Validate(Stream data, byte[] signature, Org.BouncyCastle.Crypto.AsymmetricKeyParameter publicKey) {
            Sha256Digest shaDigest = new Sha256Digest();
            RsaDigestSigner signer = new RsaDigestSigner(shaDigest);
            signer.Init(false, publicKey);
            data.Iterate(new StreamExtension.BinIterationDelegate((b)=>{
                signer.Update(b);
            }));
            return signer.VerifySignature(signature);
        }
    }
}
