using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Crypto.Digests;


namespace ISS.MV2.Security {
    public class SHA256 : IMessageDigest {

        private readonly Sha256Digest digest;

        public SHA256() {
            digest = new Sha256Digest();
        }

        public void Update(byte[] data) {
            digest.BlockUpdate(data, 0, data.Length);
        }

        public void Update(byte data) {
            digest.Update(data);
        }

        public byte[] Complete() {
            byte[] result = new byte[digest.GetDigestSize()];
            digest.DoFinal(result, 0);
            digest.Reset();
            return result;
        }

        public void Reset() {
            digest.Reset();
        }


        public IMessageDigest Clone() {
            IMessageDigest result = new SHA256();
            return result;
        }
    }
}
