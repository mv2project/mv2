using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Security {
    public class AESKeyGenerator : ISymmetricKeyGenerator {

        private byte[] fixedKey = null;
        private byte[] fixedIV = null;

        public byte[] GetRandomKey() {
            if (fixedKey != null) return fixedKey;
            return GetRandomKey(16);
        }

        public byte[] GetRandomKey(int length) {
            if (fixedKey != null && fixedKey.Length == length) return fixedKey;
            return AESStream.GetRandomKey(length * 8);
        }

        public byte[] getRandomIV(int length) {
            if (fixedIV != null) return fixedIV;
            return AESStream.GetRandomIV();
        }

        public void SetFixedIV(byte[] iv) {
            this.fixedIV = iv;
        }

        public void SetFixedKey(byte[] key) {
            this.fixedKey = key;
        }

        public bool HasFixedKeyAndIV {
            get { return (fixedKey != null && fixedIV != null); }
        }
    }
}
