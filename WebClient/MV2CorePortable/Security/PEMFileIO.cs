using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.X509;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Security;
using System.IO;

namespace ISS.MV2.Security {
    public class PEMFileIO {

        public X509Certificate ReadCertificate(Stream input) {
            X509CertificateParser parser = new X509CertificateParser();
            X509Certificate result = parser.ReadCertificate(input);
            return result;
        }

        public X509Certificate ReadCertificate(byte[] input) {
            return new X509CertificateParser().ReadCertificate(input);
        }

        public byte[] WriteCertificate(Stream output, X509Certificate certificate) {
            byte[] encoded = certificate.GetEncoded();
            output.Write(encoded, 0, encoded.Length);
            output.Flush();
            return encoded;
        }

        public AsymmetricKeyParameter ReadPrivateKey(byte[] encoded) {
            return PrivateKeyFactory.CreateKey(encoded);
        }

        public AsymmetricKeyParameter ReadPrivateKey(Stream input) {
            return PrivateKeyFactory.CreateKey(input);
        }


    }
}
