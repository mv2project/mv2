using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.X509;
using Org.BouncyCastle.Crypto;
using ISS.MV2.Security;
using System.IO;

namespace ISS.MV2.Messaging {
    public class ContentMessage : MV2Message {

        public string Subject {
            get {
                return GetFieldStringValue(DEF_MESSAGE_FIELD.SUBJECT, "");
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.SUBJECT, value);
            }
        }

        public string[] Receivers {
            get {
                string resultVal = GetFieldStringValue(DEF_MESSAGE_FIELD.RECEIVER, "").Trim();
                if (!resultVal.Contains(";")) return new string[] { resultVal };
                string[] res = resultVal.Split(';');
                for (int i = 0; i < res.Length; i++) {
                    res[i] = res[i].Trim();
                }
                return res;
            }
            set {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < value.Length; i++) {
                    if (i > 0) sb.Append(";");
                    sb.Append(value[i]);
                }
                SetMessageField(DEF_MESSAGE_FIELD.RECEIVER, sb.ToString());
            }
        }

        public X509Certificate Sender {
            get {
                byte[] encoded = GetFieldDataValue(DEF_MESSAGE_FIELD.SENDER_CERTIFICATE, null);
                if (encoded == null) return null;
                return new PEMFileIO().ReadCertificate(encoded);
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.SENDER_CERTIFICATE, value.GetEncoded());
            }
        }

        public string[] CarbonCopyAddresses {
            get {
                string resultVal = GetFieldStringValue(DEF_MESSAGE_FIELD.CARBON_COPY, "").Trim();
                if (!resultVal.Contains(";")) return new string[] { resultVal };
                string[] res = resultVal.Split(';');
                for (int i = 0; i < res.Length; i++) {
                    res[i] = res[i].Trim();
                }
                return res;
            }
            set {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < value.Length; i++) {
                    if (i > 0) sb.Append(";");
                    sb.Append(value[i]);
                }
                SetMessageField(DEF_MESSAGE_FIELD.CARBON_COPY, sb.ToString());
            }
        }

        public string Content {
            get {
                return GetFieldStringValue(DEF_MESSAGE_FIELD.CONTENT_PLAIN, "");
            }
            set {
                SetMessageField(DEF_MESSAGE_FIELD.CONTENT_PLAIN, value);
            }
        }

        private Stream GetContentToSign() {
            List<MessageField> fields = new List<MessageField>();
            CleanUp();
            foreach(MessageField mf in Fields){
                if (mf.FieldType.Identifier == DEF_MESSAGE_FIELD.SIGNATURE.Identifier) continue;
                fields.Add(mf);
            }
            fields.Sort(new Comparison<MessageField>((a, b) => {
                return a.FieldType.Identifier.CompareTo(b.FieldType.Identifier);
            }));
            MemoryStream ms = new MemoryStream();
            foreach (MessageField mf in fields) {
                mf.Serialize(ms);
            }
            ms.Position = 0;
            return ms;
        }


        public byte[] Signature {
            get {
                return GetFieldDataValue(DEF_MESSAGE_FIELD.SIGNATURE, null);
            }
        }


        public ContentMessage()
            : base(DEF_MESSAGE.CONTENT_MESSAGE) {
        }


        public void Sign(ISignatureProvider signatureProvider, AsymmetricKeyParameter privateKey) {
            if (!privateKey.IsPrivate) throw new ArgumentException("A private key is needed to perform this operation.", "privateKey");
            using (Stream content = GetContentToSign()) {
                byte[] signature = signatureProvider.Sign(content, privateKey);
                SetMessageField(DEF_MESSAGE_FIELD.SIGNATURE, signature);
            }
        }

        public void Sign(AsymmetricKeyParameter privateKey) {
            Sign(new SHA256WithRSASignatureProvider(), privateKey);
        }

        public bool VerifySignature(ISignatureProvider signatureProvider, AsymmetricKeyParameter publicKey) {
            if (publicKey.IsPrivate) throw new ArgumentException("A public key is needed to perform this operation.", "publicKey");
            bool result = false;
            using (Stream content = GetContentToSign()) {
                result = signatureProvider.Validate(content, Signature, publicKey);
            }
            return result;
        }

        public bool VerifySignature(AsymmetricKeyParameter publicKey) {
            return VerifySignature(new SHA256WithRSASignatureProvider(), publicKey);
        }

        public bool VerifySignature() {
            X509Certificate cert = Sender;
            if (cert == null) throw new InvalidOperationException("There is no sender certificate needed to retrieve the public key.");
            AsymmetricKeyParameter publicKey = cert.GetPublicKey();
            return VerifySignature(publicKey);

        }

    }
}
