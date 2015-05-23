using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using Org.BouncyCastle.Crypto;


namespace ISS.MV2.Security {
   public interface IMessageCryptorSettings {

       ISymmetricKeyGenerator KeyGenerator { get; }

       ClosableStream GetSymmetricEncryptionStream(Stream baseStream, byte[] key, byte[] iv);

       [Obsolete("Does the exact same thing like IMessageCryptorSettings.GetSymmetricEncryptionStream().")]
       ClosableStream GetSymmetricDecryptionStream(Stream baseStream, byte[] key, byte[] iv);

       int DesiredSymmetricKesSize { get; }

        ClosableStream GetAsymmetricDecryptionStream(Stream baseStream, AsymmetricKeyParameter privateKey);
        ClosableStream GetAsymmetricEncryptionStream(Stream baseStream, AsymmetricKeyParameter publicKey);

        string SymmetricAlgorithmName { get; }
        string AsymmetricAlgorithmName { get; }

    }
}
