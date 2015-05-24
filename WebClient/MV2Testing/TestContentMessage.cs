using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using ISS.MV2.Messaging;
using ISS.MV2.Security;


namespace ISS.MV2.Tests {
    [TestClass]
    public class TestContentMessage {


        [TestMethod]
        [ExpectedException(typeof(InvalidOperationException))]
        public void TestInvalidOperation() {
            ContentMessage cm = new ContentMessage();
            cm.VerifySignature();
        }

        [TestMethod]
        public void TestValidSignature() {
            ContentMessage cm = new ContentMessage();
            cm.Content = "Hello World!";
            cm.Subject = "Valid Signature Test";
            cm.Receivers = new string[] {"foo@bar.com"};
            cm.CarbonCopyAddresses = new string[] {"bar@foo.com" };
            var keyPair = RSAStream.GetRandomRSAKey(1024);
            cm.Sign(keyPair.Private);
            Assert.IsTrue(cm.VerifySignature(keyPair.Public));
        }

        [TestMethod]
        public void TestModifyContent() {
            ContentMessage cm = new ContentMessage();
            cm.Content = "Hello World!";
            cm.Subject = "Valid Signature Test";
            cm.Receivers = new string[] { "foo@bar.com" };
            cm.CarbonCopyAddresses = new string[] { "bar@foo.com" };
            var keyPair = RSAStream.GetRandomRSAKey(1024);
            cm.Sign(keyPair.Private);
            Assert.IsTrue(cm.VerifySignature(keyPair.Public));
            cm.Content += "Added";
            Assert.IsFalse(cm.VerifySignature(keyPair.Public));
        }


    }
}
