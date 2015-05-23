using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using ISS.MV2.Messaging;
using System.IO;
using Org.BouncyCastle.Crypto;
using ISS.MV2.Security;


namespace ISS.MV2.Tests {
    [TestClass]
    public class MessageParserTest {
        [TestMethod]
        public void TestUnencrypted() {
            MemoryStream ms = new MemoryStream();
            MessageParser parser = new MessageParser(ms);
            HelloMessage hm = new HelloMessage();
            hm.HostName = "lcoalhöst";
            hm.Serialize(ms);
            ms.Position = 0;
            MV2Message read = parser.ReadNext();
            HelloMessage readHello = new HelloMessage();
            MV2Message.Merge(readHello, read);
            Assert.AreEqual(hm.HostName, readHello.HostName);
        }

        [TestMethod]
        public void TestEncrypted() {
            AsymmetricCipherKeyPair keyPair = RSAStream.GetRandomRSAKey(1024);
            HelloMessage hm = new HelloMessage();
            hm.HostName = "localhöst";
            AESWithRSACryptoSettings settings = new AESWithRSACryptoSettings();
            EncryptedMessage encryptedMessage = new EncryptedMessage(settings, keyPair.Public, hm.MessageType);
            MV2Message.Merge(encryptedMessage, hm);
            MemoryStream ms = new MemoryStream();
            encryptedMessage.Serialize(ms);
            MessageParser parser = new MessageParser(ms);
            ms.Position = 0;
            parser.Settings = settings;
            parser.SetPrivateKey(keyPair.Private);
            MV2Message message = parser.ReadNext();
            HelloMessage helloMessage2 = new HelloMessage();
            MV2Message.Merge(helloMessage2, message);
            Assert.AreEqual(hm.HostName, helloMessage2.HostName);
        }

    }
}
