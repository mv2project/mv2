using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.Security;
using Org.BouncyCastle.Crypto;
using System.IO;
using ISS.MV2.IO;

namespace ISS.MV2.Messaging {
    public class EncryptedMessage : MV2Message {

        private readonly AsymmetricKeyParameter publicKey;

        private readonly IMessageCryptorSettings settings;

        private DEF_MESSAGE contentMessageIdentifier;

        private readonly AsymmetricKeyParameter privateKey;

        private byte[] symmetricKey;
        private byte[] symmetricIV;

        public byte[] UsedSymmetricIV {
            get { return symmetricIV; }
        }

        public byte[] UsedSymmetricKey {
            get { return symmetricKey; }
        }

        private string symmetricAlgorithm;

        public override DEF_MESSAGE MessageType {
            get {
                return contentMessageIdentifier;
            }
        }

        public EncryptedMessage(IMessageCryptorSettings settings, AsymmetricKeyParameter privateKey)
            : base(DEF_MESSAGE.ENCRYPTED_MESSAGE) {
            this.contentMessageIdentifier = DEF_MESSAGE.UNKNOWN;
            this.settings = settings;
            this.publicKey = null;
            this.privateKey = privateKey;
        }

        public EncryptedMessage(IMessageCryptorSettings settings, AsymmetricKeyParameter publicKey, DEF_MESSAGE contentMessageIdentifier)
            : base(DEF_MESSAGE.ENCRYPTED_MESSAGE) {
            this.contentMessageIdentifier = contentMessageIdentifier;
            this.settings = settings;
            this.publicKey = publicKey;
            this.privateKey = null;
        }

        private static readonly Encoding ENCODING = Encoding.GetEncoding("us-ascii");

        protected override void DoSerialize(System.IO.Stream outputStream, Encoding encoding) {
            ISymmetricKeyGenerator keyGen = settings.KeyGenerator;
            byte[] key = keyGen.GetRandomKey(settings.DesiredSymmetricKesSize);
            byte[] iv = keyGen.GetRandomIV();
            using (MemoryStream ms = new MemoryStream()) {
                if (!keyGen.HasFixedKeyAndIV) {
                    this.symmetricIV = iv;
                    this.symmetricKey = key;
                    using (ClosableStream cryptoStream = settings.GetSymmetricEncryptionStream(ms, key, iv)) {
                        MV2Message contentMessage = ContentMessage;
                        contentMessage.Serialize(cryptoStream);
                        cryptoStream.Flush();
                        cryptoStream.Close();
                        MessageField symAlgorithmName = new MessageField(DEF_MESSAGE_FIELD.SYMMETRIC_ALGORITHM, settings.SymmetricAlgorithmName);
                        symAlgorithmName.Encoding = ENCODING;
                        MessageField asymAlgorithmName = new MessageField(DEF_MESSAGE_FIELD.ASYMMETRIC_ALGORITHM, settings.AsymmetricAlgorithmName);
                        asymAlgorithmName.Encoding = ENCODING;
                        using (MemoryStream keyOut = new MemoryStream()) {
                            using (ClosableStream keyCryptoOut = settings.GetAsymmetricEncryptionStream(keyOut, publicKey)) {
                                keyCryptoOut.Write(iv);
                                keyCryptoOut.Write(key);
                                keyCryptoOut.Flush();
                                keyCryptoOut.Close();
                            }
                            MessageField keyField = new MessageField(DEF_MESSAGE_FIELD.ENCRYPTION_KEY, keyOut.ToArray());
                            keyField.Encoding = ENCODING;
                            keyField.Serialize(outputStream);
                        }
                        symAlgorithmName.Serialize(outputStream);
                        asymAlgorithmName.Serialize(outputStream);
                    }
                } else {
                    using (ClosableStream cryptoStream = settings.GetSymmetricEncryptionStream(ms, key, iv)) {
                        MV2Message contentMessage = ContentMessage;
                        contentMessage.Serialize(cryptoStream);
                        cryptoStream.Flush();
                        cryptoStream.Close();
                    }
                }
                this.symmetricIV = iv;
                this.symmetricKey = key;
                outputStream.Write(ms.ToArray());
                outputStream.Flush();
            }
        }

        public override void Deserialize(Stream inputStream) {
            ClearFields();
            byte[] key;
            byte[] iv;
            if (!settings.KeyGenerator.HasFixedKeyAndIV) {
                int identifier, length;
                Stream buffer;
                for (int i = 0; i < 3; i++) {
                    identifier = BinaryTools.ReadInt(inputStream);
                    length = BinaryTools.ReadInt(inputStream);
                    buffer = BinaryTools.ReadBuffer(inputStream, length);
                    MessageField mf = new MessageField(DEF_MESSAGE_FIELD.Find(identifier));
                    mf.Deserialize(buffer);
                    mf.CompleteDeserialize(ENCODING);
                    SetMessageField(mf, true);
                }
                MessageField symNameField = GetFieldOrThrow(DEF_MESSAGE_FIELD.SYMMETRIC_ALGORITHM);
                MessageField asymNameField = GetFieldOrThrow(DEF_MESSAGE_FIELD.ASYMMETRIC_ALGORITHM);
                this.symmetricAlgorithm = symNameField.Content;
                if (!symNameField.Content.Equals(settings.SymmetricAlgorithmName) || !asymNameField.Content.Equals(settings.AsymmetricAlgorithmName)) throw new CryptoException();
                byte[] keyData = GetFieldDataValue(DEF_MESSAGE_FIELD.ENCRYPTION_KEY, null);
                using (MemoryStream bin = new MemoryStream(keyData)) {
                    using (ClosableStream asymIn = settings.GetAsymmetricDecryptionStream(bin, privateKey)) {
                        keyData = BinaryTools.ReadAll(asymIn);
                        asymIn.Close();
                    }
                }
                int ivlength = settings.KeyGenerator.GetRandomIV().Length;
                iv = new byte[ivlength];
                key = new byte[keyData.Length - ivlength];
                Array.Copy(keyData, 0, iv, 0, ivlength);
                Array.Copy(keyData, ivlength, key, 0, keyData.Length - ivlength);
            } else {
                ISymmetricKeyGenerator keyGen = settings.KeyGenerator;
                iv = keyGen.GetRandomIV();
                key = keyGen.GetRandomKey();
            }
            this.symmetricKey = key;
            this.symmetricIV = iv;
            using (ClosableStream symIn = settings.GetSymmetricEncryptionStream(inputStream, key, iv)) {
                using(MemoryStream content = new MemoryStream(BinaryTools.ReadAll(symIn))){
                    MessageParser parser = new MessageParser(content);
                    parser.SetPrivateKey(privateKey);
                    parser.Settings = settings;
                    MV2Message contentMessage = parser.ReadNext();
                    ClearFields();
                    contentMessageIdentifier = contentMessage.MessageType;
                    Merge(this, contentMessage);
                }
            }
        }

        public MV2Message ContentMessage {
            get {
                MV2Message m = new MV2Message(contentMessageIdentifier);
                m.Encoding = Encoding;
                m.ClearFields();
                foreach (MessageField f in Fields) {
                    m.SetMessageField(f, true);
                }
                return m;
            }
        }

    }
}
