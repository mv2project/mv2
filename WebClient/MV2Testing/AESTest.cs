using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using ISS.MV2.Security;
using ISS.MV2.IO;
using System.IO;

namespace ISS.MV2.Tests {
    [TestClass]
    public class AESTest {
        [TestMethod]
        public void TestEncryption() {
            byte[] key = AESStream.GetRandomKey(256);
            byte[] iv = AESStream.GetRandomIV();
            MemoryStream ms = new MemoryStream();
            AESStream aesStream = new AESStream(ms, key, iv);
            byte[] dataClear = System.Text.Encoding.UTF8.GetBytes("Hello World!");
            aesStream.Write(dataClear, 0, dataClear.Length);
            aesStream.Close();
            byte[] encrypted = ms.ToArray();
            ms = new MemoryStream(encrypted);
            aesStream = new AESStream(ms, key, iv);
            byte[] dataRead = new byte[dataClear.Length];
            aesStream.ReadFull(dataRead);
            CollectionAssert.AreEqual(dataClear, dataRead);
        }
    }
}
