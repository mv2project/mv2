using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using Org.BouncyCastle.Crypto;
using ISS.MV2.Security;
using ISS.MV2.IO;

namespace ISS.MV2.Messaging {
    public class MessageParser {

        private readonly Stream input;

        private AsymmetricKeyParameter privateKey;

        private IMessageCryptorSettings settings = null;

        public IMessageCryptorSettings Settings {
            set { settings = value; }
            get { return settings; }
        }

        public MessageParser(Stream inputStream) {
            this.input = inputStream;

        }

        public void SetPrivateKey(AsymmetricKeyParameter privateKey) {
            this.privateKey = privateKey;
        }

        public MV2Message ReadNext() {
            int identifier = BinaryTools.ReadInt(input);
            int length = BinaryTools.ReadInt(input);
            Stream messageStream = BinaryTools.ReadBuffer(input, length);
            MV2Message message = null;
            if (identifier != DEF_MESSAGE.ENCRYPTED_MESSAGE.Identifier) {
                message = new MV2Message(DEF_MESSAGE.Find(identifier));
            } else {
                if (!settings.KeyGenerator.HasFixedKeyAndIV) {
                    if (settings == null) throw new InvalidOperationException("The settings object needed for decryption was not set.");
                    if (privateKey == null) throw new InvalidOperationException("The key needed for decryption was not set.");
                }
                message = new EncryptedMessage(settings, privateKey);
            }
            message.Deserialize(messageStream);
            return message;
        }
     

    }
}
