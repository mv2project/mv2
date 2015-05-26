using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
   public class ServerLoginResponse : MV2Message {

       public byte[] TestPhrase {
           get {
               return GetFieldDataValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
           }
           set {
               SetMessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, value);
           }
       }

       public string HashAlgorithm {
           get {
               return GetFieldStringValue(DEF_MESSAGE_FIELD.HASH_ALGORITHM, "");
           }
           set {
               SetMessageField(DEF_MESSAGE_FIELD.HASH_ALGORITHM, value);
           }
       }

       public byte[] TestPhraseHash {
           get {
               return GetFieldDataValue(DEF_MESSAGE_FIELD.HASH_BINARY, null);
           }
           set {
               SetMessageField(DEF_MESSAGE_FIELD.HASH_BINARY, value);
           }
       }

       public ServerLoginResponse()
           : base(DEF_MESSAGE.SERVER_LOGIN_RESPONSE) {
       }

    }
}
