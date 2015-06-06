using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.Messaging;
using ISS.MV2.IO;
using ISS.MV2.Security;
using System.IO;
using System.Threading;

namespace ISS.MV2.Threading {
    public class LoginProcedure : MessageProcedure<ClientSession, bool>, IAuthenticatedClientProvider {

        public LoginProcedure(IEventDispatcher disptacher, ClientSession clientSession)
            : base(disptacher, clientSession) {
            
        }


        protected override bool DoProcedure(ClientSession session) {
            ICommunicationPartner client = session.CreateClient();
            if (session.ClientCertificate == null) LoadClientCert(client, session);
            if (session.ClientPrivateKey == null) LoadClientKey(client, session);
            Update("Logging in...");
            return Login(client, session);
        }

        private ICommunicationPartner usedClient;


        public bool Login(ICommunicationPartner client, ClientSession session) {
            KnownKeyLoginProcedure klp = new KnownKeyLoginProcedure(EventDispatcher, Parameter);
            klp.AddListener(this);
            bool result = klp.ExecuteImmediate();
            this.usedClient = klp.GetAuthenticatedClient();
            return result;
        }

        private void LoadClientCert(ICommunicationPartner client, ClientSession session) {
            Update("Requesting the users certificate...");
            ClientCertificateRequest certRequest = new ClientCertificateRequest();
            certRequest.Identifier = session.Identifier;
            client.Send(certRequest);
            MV2Message response = client.HandleNext();
            CheckFail(response);
            ClientCertificateResponse clientCertResponse = AssertTypeAndConvert(response, new ClientCertificateResponse());
            session.ClientCertificate = clientCertResponse.Certificate;
            Update("Received the certificate.");
        }

        private void LoadClientKey(ICommunicationPartner client, ClientSession session) {
            Update("Loading the users private key...");
            PassphraseInflator inflator = new PassphraseInflator();
            byte[] primaryKey = inflator.GetPrimaryPassphraseDigest(session.Passphrase);
            byte[] secondaryKey = inflator.GetSecondaryPassphraseDigest(session.Passphrase);
            KeyRequest request = new KeyRequest();
            request.Passphrase = secondaryKey;
            request.Identifier = session.Identifier;
            client.Send(request);
            KeyResponse respone = AssertTypeAndConvert(client.HandleNext(), new KeyResponse());
            byte[] iv = new byte[16];
            Org.BouncyCastle.Crypto.AsymmetricKeyParameter privateKey;
            for (int i = 0; i < iv.Length; i++) iv[i] = primaryKey[i];
            using (MemoryStream ms = new MemoryStream(respone.Key)) {
                using (AESStream aes = new AESStream(ms, primaryKey, iv)) {
                   privateKey = new PEMFileIO().ReadPrivateKey(aes);
                }
            }
            session.ClientPrivateKey = privateKey;
            Update("Received the private key.");
        }

        public ICommunicationPartner GetAuthenticatedClient() {
            if (usedClient == null) ExecuteImmediate();
            return usedClient;
        }
    }
}
