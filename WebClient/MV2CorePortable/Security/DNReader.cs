using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Asn1.X509;
using System.Collections.Generic;


namespace ISS.MV2.Security {
    public class DNReader {

        public const string ORGANIZATION = "O";
        public const string STATE = "ST";
        public const string COUNTRY = "C";
        public const string ORGANIZATION_UNIT = "OU";
        public const string COMMON_NAME = "CN";
        public const string LOCALE = "L";

        private readonly IDictionary<string, string> values = new Dictionary<string, string>();

        public string OrganizationUnit {
            get {
                return GetValue(ORGANIZATION_UNIT, null);
            }
        }

        public string Organization {
            get {
                return GetValue(ORGANIZATION, null);
            }
        }

        public string State {
            get {
                return GetValue(STATE, null);
            }
        }

        public string Country { get { return GetValue(COUNTRY, null); } }

        public string Locale { get { return GetValue(LOCALE, null); } }

        public string CommonName { get { return GetValue(COMMON_NAME, null); } }

        public DNReader(X509Name name) {
            string[] args = name.ToString().Split(',');
            foreach (string arg in args) {
                if (arg.Contains("=")) {
                    string[] kv = arg.Split('=');
                    if (kv.Length == 2) values[kv[0]] = kv[1];
                }
            }
        }

        public string GetValue(string identifier, string defaultValue) {
            if (!values.ContainsKey(identifier)) return defaultValue;
            return values[identifier];
        }

    }
}
