using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using ISS.MV2.Security;


namespace ISS.MV2.Tests {
    [TestClass]
    public class CertificateSigningRequestTest {

        private const string TEST_CN = "Max Mustermann";
        private const string INVALID_COUNTRY = "Germany";
        private const string VALID_COUNTRY = "DE";


        [TestMethod]
        public void TestSignValid() {
            var keyPair = RSAStream.GetRandomRSAKey(1024);
            CertificateSigningRequest csr = new CertificateSigningRequest() { CommonName=TEST_CN };
            var result = csr.GeneratePKCS10(keyPair);
            Assert.IsTrue(result.Verify(keyPair.Public));
        }

        [TestMethod]
        public void TestSignInvalid() {
            var keyPair1 = RSAStream.GetRandomRSAKey(1024);
            var keyPair2 = RSAStream.GetRandomRSAKey(1024);
            var keyPair = new Org.BouncyCastle.Crypto.AsymmetricCipherKeyPair(keyPair1.Public, keyPair2.Private);
            CertificateSigningRequest csr = new CertificateSigningRequest() { CommonName = TEST_CN };
            var result = csr.GeneratePKCS10(keyPair);
            Assert.IsFalse(result.Verify(keyPair.Public));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException))]
        public void TestSetInvalidCountry() {
            CertificateSigningRequest csr = new CertificateSigningRequest();
            csr.Country = INVALID_COUNTRY;
        }

        [TestMethod]
        public void TestSetValidCountry() {
            CertificateSigningRequest csr = new CertificateSigningRequest();
            csr.Country = VALID_COUNTRY;
        }

    }
}
