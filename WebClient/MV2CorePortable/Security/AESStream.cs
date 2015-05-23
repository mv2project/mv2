using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using Org.BouncyCastle.Crypto.IO;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Engines;
using Org.BouncyCastle.Crypto.Paddings;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.Crypto.Modes;

namespace ISS.MV2.Security {
    public class AESStream : CipherStream2 {



        public AESStream(Stream baseStream, byte[] key, byte[] iv) : base(baseStream, GetDecryptionCipher(key, iv), GetEncryptionCiper(key, iv)){
            
        }

        private static PaddedBufferedBlockCipher GetDecryptionCipher(byte[] key, byte[] iv) {
            AesEngine engine = new AesEngine();
            CbcBlockCipher blockCipher = new CbcBlockCipher(engine);
            PaddedBufferedBlockCipher decryptCipher = new PaddedBufferedBlockCipher(blockCipher, new Pkcs7Padding());
            decryptCipher.Init(false, new ParametersWithIV(new KeyParameter(key), iv));
            return decryptCipher;
        }

        private static PaddedBufferedBlockCipher GetEncryptionCiper(byte[] key, byte[] iv) {
            AesEngine engine = new AesEngine();
            CbcBlockCipher blockCipher = new CbcBlockCipher(engine);
            PaddedBufferedBlockCipher encryptCipher = new PaddedBufferedBlockCipher(blockCipher, new Pkcs7Padding());
            encryptCipher.Init(true, new ParametersWithIV(new KeyParameter(key), iv));

            return encryptCipher;
        }

        public static byte[] GetRandomIV() {
            byte[] iv = new byte[16];
            SecureRandom sr = new SecureRandom();
            sr.NextBytes(iv);
            return iv;
        }

        /// <summary>
        /// Creates a radom AES key.
        /// </summary>
        /// <param name="strength">The strength of the key to create in bits.</param>
        /// <returns></returns>
        public static byte[] GetRandomKey(int strength) {
            byte[] key = new byte[strength / 8];
            SecureRandom sr = new SecureRandom();
            sr.NextBytes(key);
            return key;
        }

    }
}
