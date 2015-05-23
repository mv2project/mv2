using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.IO;
using ISS.MV2.Security;
using ISS.MV2.IO;


namespace ISS.MV2.Tests {
    [TestClass]
    public class RSATest {
        [TestMethod]
        public void TestEncryption() {
            var keyPair = RSAStream.GetRandomRSAKey(2048);
            byte[] data = new byte[] { 0, 8, 15 };
            MemoryStream ms = new MemoryStream();
            RSAStream encryptionStream = new RSAStream(ms, keyPair.Public);
            encryptionStream.Write(data);
            encryptionStream.Close();
            byte[] encrypted = ms.ToArray();
            ms = new MemoryStream(encrypted);
            RSAStream decryptionStream = new RSAStream(ms, keyPair.Private);
            byte[] dataRead = new byte[data.Length];
            decryptionStream.ReadFull(dataRead);
            decryptionStream.Close();
            CollectionAssert.AreEqual(data, dataRead);
        }
    }
}
