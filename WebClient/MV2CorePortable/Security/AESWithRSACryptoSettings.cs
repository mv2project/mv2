using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Security {
    public class AESWithRSACryptoSettings : IMessageCryptorSettings {

        private ISymmetricKeyGenerator keyGen = new AESKeyGenerator();

        private int desiredSymmetricKeySize = 32;

        public ISymmetricKeyGenerator KeyGenerator {
            get { return keyGen; }
        }

        public ClosableStream GetSymmetricEncryptionStream(System.IO.Stream baseStream, byte[] key, byte[] iv) {
            AESStream aesStream = new AESStream(baseStream, key, iv);
            return new ClosableStream(aesStream, new ClosableStream.CloseDelegate(() => {aesStream.Close(); }));
        }

        [Obsolete("Does the exact same thing like IMessageCryptorSettings.GetSymmetricEncryptionStream().")]
        public ClosableStream GetSymmetricDecryptionStream(System.IO.Stream baseStream, byte[] key, byte[] iv) {
            return GetSymmetricEncryptionStream(baseStream, key, iv);
        }

        public int DesiredSymmetricKesSize {
            get { return desiredSymmetricKeySize; }
            set { desiredSymmetricKeySize = value; }
        }

        public ClosableStream GetAsymmetricDecryptionStream(System.IO.Stream baseStream, Org.BouncyCastle.Crypto.AsymmetricKeyParameter privateKey) {
            RSAStream rsaStream = new RSAStream(baseStream, privateKey);
            ClosableStream cs = new ClosableStream(rsaStream, new ClosableStream.CloseDelegate(() => { rsaStream.Close(); }));
            return cs;
        }

        public ClosableStream GetAsymmetricEncryptionStream(System.IO.Stream baseStream, Org.BouncyCastle.Crypto.AsymmetricKeyParameter publicKey) {
            RSAStream rsaStream = new RSAStream(baseStream, publicKey);
            ClosableStream cs = new ClosableStream(rsaStream, new ClosableStream.CloseDelegate(() => { rsaStream.Close(); }));
            return cs;
           
        }

        public string SymmetricAlgorithmName {
            get { throw new NotImplementedException(); }
        }

        public string AsymmetricAlgorithmName {
            get { throw new NotImplementedException(); }
        }
    }
}
