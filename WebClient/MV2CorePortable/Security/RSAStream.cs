using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Crypto.IO;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Engines;
using Org.BouncyCastle.Crypto.Paddings;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.Crypto.Modes;
using Org.BouncyCastle.Crypto.Encodings;
using Org.BouncyCastle.Crypto.Generators;
using System.IO;


namespace ISS.MV2.Security {
    public class RSAStream : CipherStream2 {

        public RSAStream(Stream baseStream, AsymmetricKeyParameter keyParameter)
            : base(baseStream, GetDecryptionCipher(keyParameter), GetEncryptionCiper(keyParameter)) {
        }

        private static BufferedAsymmetricBlockCipher GetDecryptionCipher(AsymmetricKeyParameter privateKey) {
            if (!privateKey.IsPrivate) return null;
            RsaEngine rsaEngine = new RsaEngine();
            BufferedAsymmetricBlockCipher decryptionCipher = new BufferedAsymmetricBlockCipher(new Pkcs1Encoding(rsaEngine));
            decryptionCipher.Init(false, privateKey);
            return decryptionCipher;
        }

        private static BufferedAsymmetricBlockCipher GetEncryptionCiper(AsymmetricKeyParameter publicKey) {
            if (publicKey.IsPrivate) return null;
            RsaEngine rsaEngine = new RsaEngine();
            BufferedAsymmetricBlockCipher encryptionCipher = new BufferedAsymmetricBlockCipher(new Pkcs1Encoding(rsaEngine));
            encryptionCipher.Init(true, publicKey);
            return encryptionCipher;
        }

        public static AsymmetricCipherKeyPair GetRandomRSAKey(int strength) {
            RsaKeyPairGenerator generator = new RsaKeyPairGenerator();
            generator.Init(new KeyGenerationParameters(new SecureRandom(), strength));
            AsymmetricCipherKeyPair result = generator.GenerateKeyPair();
            return result;
        }


        public override void Write(byte[] buffer, int offset, int count) {
            base.Write(buffer, offset, count);
        }

      

    }
}
