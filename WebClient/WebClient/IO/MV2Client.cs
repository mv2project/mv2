using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using ISS.MV2.Messaging;
using ISS.MV2.Security;
using Org.BouncyCastle.X509;
using System.Collections.Generic;

namespace ISS.MV2.IO {
    public class MV2Client : ICommunicationPartner {

        private ClientSocket connection;
        private MessageParser parser;

        private X509Certificate serverCerificate;

        public X509Certificate Certificate {
            get {
                return serverCerificate;
            }
        }

        public IMessageCryptorSettings CryptorSettings {
            get {
                if (parser == null) return null;
                return parser.Settings;
            }
            set {
                if (parser == null) throw new InvalidOperationException();
                parser.Settings = value;
            }
        }

        private string[] availableDomains = new string[0];
        public string[] AvailableDomains { get { return availableDomains; } }

        private readonly IList<IMessagePreProcessor> preProcessors = new List<IMessagePreProcessor>();
        private readonly IList<IMessageProcessor> processors = new List<IMessageProcessor>();

        public MV2Client() {
            AddPreProcessor(new CertificateResponsePreProcessor());
            AddPreProcessor(new DomainNamesResponsePreProcessor());
        }

        public void Connect(string host) {
            if (connection != null) throw new AlreadyConnectedException();
            connection = new ClientSocket();
            connection.Connect(host);
            parser = new MessageParser(connection);
            parser.Settings = new AESWithRSACryptoSettings();
            HelloMessage helloMessage = new HelloMessage();
            helloMessage.HostName = host;
            Send(helloMessage);
            HandleNext();
            Send(new MV2Message(DEF_MESSAGE.CERT_REQUEST));
            HandleNext();
        }

        public void AddPreProcessor(IMessagePreProcessor preProcessor) {
            if(!preProcessors.Contains(preProcessor)) preProcessors.Add(preProcessor);
        }

        public void AddProcessort(IMessageProcessor processor) {
            if (!processors.Contains(processor)) processors.Add(processor);
        }

        public MV2Message HandleNext() {
            MV2Message message = parser.ReadNext();
            foreach (IMessagePreProcessor pp in preProcessors) {
                message = pp.Prepare(this, message);
            }
            if (message is CertificateResponseMessage) {
                CertificateResponseMessage crm = (CertificateResponseMessage)message;
                X509Certificate cert = crm.Certificate;
                if (VerifyCertificate(cert)) serverCerificate = cert;
            }
            if (message is DomainNamesResponse) {
                availableDomains = ((DomainNamesResponse)message).AvailableDomainNames;
            }
            foreach (IMessageProcessor p in processors) {
                p.Process(this, message);
            }
            return message;
        }

        private bool VerifyCertificate(X509Certificate cert) {
            return true;
        }

        public void Send(MV2Message message) {
            bool isEncrypted = false;
            if (Certificate != null && message.MessageType != DEF_MESSAGE.ENCRYPTED_MESSAGE) {
                EncryptedMessage encrypted = new EncryptedMessage(parser.Settings, Certificate.GetPublicKey(), message.MessageType);
                MV2Message.Merge(encrypted, message);
                message = encrypted;
                isEncrypted = true;
            }
            message.Serialize(connection);
            if (isEncrypted && !parser.Settings.KeyGenerator.HasFixedKeyAndIV) {
                EncryptedMessage encrypted = (EncryptedMessage)message;
                parser.Settings.KeyGenerator.SetFixedIV(encrypted.UsedSymmetricIV);
                parser.Settings.KeyGenerator.SetFixedKey(encrypted.UsedSymmetricKey);
            }
        }
    }
}
