using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Pkcs;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Asn1;
using Org.BouncyCastle.Asn1.X509;




namespace ISS.MV2.Security {
    public class CertificateSigningRequest {

        private const string ALGORITHM = "SHA384withRSA";

        public string CommonName { get; set; }
        public string OrganizationUnit { get; set; }
        public string Organization { get; set; }
        public string Locale { get; set; }
        private string country;
        public string Country {
            get {
                return country;
            }
            set {
                if (value == null) {
                    country = null;
                    return;
                }
                if (value.Length != 2) throw new ArgumentException("The length if the country code to set is invalid. It must contain exactly two characters.");
                country = value;
            }
        }

        public string State { get; set; }

        public string Name { get; set; }

        public string MailAddress { get; set; }

        public Pkcs10CertificationRequest GeneratePKCS10(AsymmetricCipherKeyPair keyPair) {
            IDictionary<DerObjectIdentifier, string> valuesDir = new Dictionary<DerObjectIdentifier, string>();
            if (!string.IsNullOrWhiteSpace(Locale)) valuesDir.Add(X509Name.L, Locale);
            if (!string.IsNullOrWhiteSpace(State)) valuesDir.Add(X509Name.ST, State);
            if (!string.IsNullOrWhiteSpace(Country)) valuesDir.Add(X509Name.C, Country);
            if (!string.IsNullOrWhiteSpace(OrganizationUnit)) valuesDir.Add(X509Name.OU, OrganizationUnit);
            if (!string.IsNullOrWhiteSpace(Organization)) valuesDir.Add(X509Name.O, Organization);
            if (!string.IsNullOrWhiteSpace(CommonName)) valuesDir.Add(X509Name.CN, CommonName);
            if (!string.IsNullOrWhiteSpace(MailAddress)) valuesDir.Add(X509Name.EmailAddress, MailAddress);
            if (!string.IsNullOrWhiteSpace(Name)) valuesDir.Add(X509Name.Name, Name);
            System.Collections.IList identifiers = new List<DerObjectIdentifier>();
            System.Collections.IList values = new List<string>();
            foreach (DerObjectIdentifier identifier in valuesDir.Keys) {
                identifiers.Add(identifier);
                values.Add(valuesDir[identifier]);
            }
            X509Name n = new X509Name(identifiers, values);
            Pkcs10CertificationRequest request = new Pkcs10CertificationRequest(ALGORITHM, n, keyPair.Public, null, keyPair.Private);
            return request;

        }


    }
}
