using System;
using System.Collections.Generic;

using System.Text;

namespace ISS.MV2.Messaging {
    public class DEF_MESSAGE {

        private int identifier = -1;
        public int Identifier { get { return identifier; } }

        private string name;

        

        public static readonly DEF_MESSAGE UNKNOWN = new DEF_MESSAGE(-1, "UNKNOWN");
        public static readonly DEF_MESSAGE OK = new DEF_MESSAGE(1, "OK");
        public static readonly DEF_MESSAGE ENCRYPTED_MESSAGE = new DEF_MESSAGE(2, "ENCRYPTED_MESSAGE");
        public static readonly DEF_MESSAGE UNABLE_TO_RPOCESS = new DEF_MESSAGE(3, "UNABLE_TO_PROCESS");
        public static readonly DEF_MESSAGE CERT_REQUEST = new DEF_MESSAGE(4, "CERT_REQUEST");
        public static readonly DEF_MESSAGE CERT_RESPONSE = new DEF_MESSAGE(5, "CERT_RESPONSE");
        public static readonly DEF_MESSAGE SPACE_CREATION_REQUEST = new DEF_MESSAGE(6, "SPACE_CREATION_REQUEST");
        public static readonly DEF_MESSAGE SPACE_CREATION_RESPONSE = new DEF_MESSAGE(7, "SPACE_CREATION_RESPONSE");
        public static readonly DEF_MESSAGE DOMAIN_NAMES_REQUEST = new DEF_MESSAGE(8, "DOMAIN_NAMES_REQUEST");
        public static readonly DEF_MESSAGE DOMAIN_NAMES_RESPONSE = new DEF_MESSAGE(9, "DOMAIN_NAMES_RESPONSE");
        public static readonly DEF_MESSAGE HELLO = new DEF_MESSAGE(10, "HELLO");
        public static readonly DEF_MESSAGE CLIENT_LOGIN_REQUEST = new DEF_MESSAGE(11, "CLIENT_LOGIN_REQUEST");
        public static readonly DEF_MESSAGE SERVER_LOGIN_RESPONSE = new DEF_MESSAGE(12, "SERVER_LOGIN_RESPONSE");
        public static readonly DEF_MESSAGE CLIENT_LOGIN_DATA = new DEF_MESSAGE(13, "CLIENT_LOGIN_DATA");
        public static readonly DEF_MESSAGE SERVER_LOGIN_RESULT = new DEF_MESSAGE(14, "SERVER_LOGIN_RESULT");
        public static readonly DEF_MESSAGE CLIENT_CERTIFICATE_REQUEST = new DEF_MESSAGE(15, "CLIENT_CERTIFICATE_REQUEST");
        public static readonly DEF_MESSAGE CLIENT_CERTIFICATE_RESPONSE = new DEF_MESSAGE(16, "CLIENT_CERTIFICATE_RESPONSE");
        public static readonly DEF_MESSAGE MESSAGE_DELIVERY_REQUEST = new DEF_MESSAGE(17, "MESSAGE_DELIVERY_REQUEST");
        public static readonly DEF_MESSAGE MESSAGE_DELIVERY_RESPONSE = new DEF_MESSAGE(18, "MESSAGE_DELIVERY_RESPONSE");
        public static readonly DEF_MESSAGE CONTENT_MESSAGE = new DEF_MESSAGE(19, "CONTENT_MESSAGE");
        public static readonly DEF_MESSAGE MESSAGE_QUERY_REQUEST = new DEF_MESSAGE(20, "MESSAGE_QUERY_REQUEST");
        public static readonly DEF_MESSAGE MESSAGE_QUERY_RESPONSE = new DEF_MESSAGE(21, "MESSAGE_QUERY_RESPONSE");
        public static readonly DEF_MESSAGE MESSAGE_FETCH_REQUEST = new DEF_MESSAGE(22, "MESSAGE_FETCH_REQUEST");
        public static readonly DEF_MESSAGE MESSAGE_FETCH_RESPONSE = new DEF_MESSAGE(23, "MESSAGE_FETCH_RESPONSE");
        public static readonly DEF_MESSAGE KEY_PUT_REQUEST = new DEF_MESSAGE(24, "KEY_PUT_REQUEST");
        public static readonly DEF_MESSAGE KEY_PUT_RESPONSE = new DEF_MESSAGE(25, "KEY_PUT_RESPONSE");
        public static readonly DEF_MESSAGE KEY_REQUEST = new DEF_MESSAGE(26, "KEY_REQUEST");
        public static readonly DEF_MESSAGE KEY_RESPONSE = new DEF_MESSAGE(27, "KEY_RESPONSE");



        public static readonly DEF_MESSAGE[] WELL_KNOWN_MESSAGES = new DEF_MESSAGE[] {UNKNOWN, OK, ENCRYPTED_MESSAGE, UNABLE_TO_RPOCESS, CERT_REQUEST, CERT_RESPONSE, SPACE_CREATION_REQUEST, SPACE_CREATION_RESPONSE, DOMAIN_NAMES_REQUEST, DOMAIN_NAMES_RESPONSE,
        HELLO, CLIENT_LOGIN_REQUEST, SERVER_LOGIN_RESPONSE, CLIENT_LOGIN_DATA, SERVER_LOGIN_RESULT, CLIENT_CERTIFICATE_REQUEST, CLIENT_CERTIFICATE_RESPONSE, MESSAGE_DELIVERY_REQUEST, MESSAGE_DELIVERY_RESPONSE, CONTENT_MESSAGE, MESSAGE_QUERY_REQUEST, MESSAGE_QUERY_RESPONSE, 
        MESSAGE_FETCH_REQUEST, MESSAGE_FETCH_RESPONSE, KEY_PUT_REQUEST, KEY_PUT_RESPONSE, KEY_REQUEST, KEY_RESPONSE};

        private DEF_MESSAGE(int identifier, string name) {
            this.identifier = identifier;
            this.name = name;
        }

        public static DEF_MESSAGE Find(int identifier) {
            foreach (DEF_MESSAGE m in WELL_KNOWN_MESSAGES) {
                if (m.Identifier == identifier) return m;
            }
            return new DEF_MESSAGE(identifier, "");
        }

        public override string ToString() {
            return name + "[" + identifier + "]";
        }

        public override bool Equals(object obj) {
            if (obj != null) {
                if (typeof(DEF_MESSAGE).IsAssignableFrom(obj.GetType())) {
                    return (((DEF_MESSAGE)obj).Identifier == identifier);
                }
            }
            return base.Equals(obj);
        }

        public static bool operator ==(DEF_MESSAGE m1, DEF_MESSAGE m2) {
            return m1.Identifier == m2.Identifier;
        }

        public static bool operator !=(DEF_MESSAGE m1, DEF_MESSAGE m2) {
            return !(m1 == m2);
        }

    }
}