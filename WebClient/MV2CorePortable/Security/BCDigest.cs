using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Crypto;


namespace ISS.MV2.Security {

    

    public class BCDigest : IMessageDigest {

        public delegate IDigest CloneBouncyCastleDigestDelegate();

        private readonly string[] names;
        private readonly IDigest bcDigest;
        private readonly CloneBouncyCastleDigestDelegate cloner;

        public BCDigest(CloneBouncyCastleDigestDelegate cloner, IDigest digest, params string[] names) {
            bcDigest = digest;
            List<string> namesList = new List<string>(names);
            if (!namesList.Contains(digest.AlgorithmName)) namesList.Add(digest.AlgorithmName);
            this.names = namesList.ToArray();
            if (cloner == null) cloner = new CloneBouncyCastleDigestDelegate(() => { return this.bcDigest; });
            this.cloner = cloner;
        }

        public void Update(byte[] data) {
            bcDigest.BlockUpdate(data, 0, data.Length);
        }

        public void Update(byte data) {
            bcDigest.Update(data);
        }

        public byte[] Complete() {
            byte[] result = new byte[bcDigest.GetDigestSize()];
            bcDigest.DoFinal(result, 0);
            bcDigest.Reset();
            return result;
        }

        public void Reset() {
            bcDigest.Reset();
        }

        public IMessageDigest Clone() {
            return new BCDigest(cloner, cloner(), names);
        }

        public string[] AlgorithmNames {
            get { return names; }
        }
    }
}
