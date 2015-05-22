using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Core.Messaging {
    public class DEF_MESSAGE_FIELD {

        private int identifier = -1;
        private bool isBinary;

        public bool IsBinary { get { return isBinary; } }
        public int Identifier { get { return identifier; } }

        public static readonly DEF_MESSAGE_FIELD UNKNOWN = new DEF_MESSAGE_FIELD(-1);
        public static readonly DEF_MESSAGE_FIELD CONTENT_ENCODING = new DEF_MESSAGE_FIELD(1);
        public static readonly DEF_MESSAGE_FIELD CAUSE = new DEF_MESSAGE_FIELD(2);
        public static readonly DEF_MESSAGE_FIELD ENCRYPTION_KEY = new DEF_MESSAGE_FIELD(3, true);
        public static readonly DEF_MESSAGE_FIELD SYMMETRIC_ALGORITHM = new DEF_MESSAGE_FIELD(4);
        public static readonly DEF_MESSAGE_FIELD ASYMMETRIC_ALGORITHM = new DEF_MESSAGE_FIELD(5);
        public static readonly DEF_MESSAGE_FIELD CONTENT_PLAIN = new DEF_MESSAGE_FIELD(6);
        public static readonly DEF_MESSAGE_FIELD HASH_ALGORITHM = new DEF_MESSAGE_FIELD(7);
        public static readonly DEF_MESSAGE_FIELD CONTENT_BINARY = new DEF_MESSAGE_FIELD(8, true);
        public static readonly DEF_MESSAGE_FIELD HASH_BINARY = new DEF_MESSAGE_FIELD(9, true);
        public static readonly DEF_MESSAGE_FIELD RECEIVER = new DEF_MESSAGE_FIELD(10);
        public static readonly DEF_MESSAGE_FIELD SUBJECT = new DEF_MESSAGE_FIELD(11);
        public static readonly DEF_MESSAGE_FIELD SENDER_CERTIFICATE = new DEF_MESSAGE_FIELD(12, true);
        public static readonly DEF_MESSAGE_FIELD CARBON_COPY = new DEF_MESSAGE_FIELD(13);
        public static readonly DEF_MESSAGE_FIELD SIGNATURE = new DEF_MESSAGE_FIELD(14, true);
        public static readonly DEF_MESSAGE_FIELD NOT_BEFORE = new DEF_MESSAGE_FIELD(15);
        public static readonly DEF_MESSAGE_FIELD TIMESTAMP = new DEF_MESSAGE_FIELD(16, true);

        public static readonly DEF_MESSAGE_FIELD[] WELL_KNOWN_TYPES = new DEF_MESSAGE_FIELD[] {UNKNOWN, CONTENT_ENCODING, CAUSE, ENCRYPTION_KEY, SYMMETRIC_ALGORITHM, ASYMMETRIC_ALGORITHM, CONTENT_PLAIN, HASH_ALGORITHM, CONTENT_BINARY, HASH_BINARY, RECEIVER, SUBJECT, SENDER_CERTIFICATE, 
        CARBON_COPY, SIGNATURE, NOT_BEFORE, TIMESTAMP};


        private DEF_MESSAGE_FIELD(int identifier, bool isBinary) {
            this.identifier = identifier;
            this.isBinary = isBinary;
        }

        private DEF_MESSAGE_FIELD(int identifier)
            : this(identifier, false) {
        }

        public static DEF_MESSAGE_FIELD Find(int identifier) {
            foreach(DEF_MESSAGE_FIELD mf in WELL_KNOWN_TYPES){
                if(mf.Identifier == identifier) return mf;
            }
            return DEF_MESSAGE_FIELD.UNKNOWN;
        }

    }
}
