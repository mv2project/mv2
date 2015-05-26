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
            ClientLoginRequest clr = new ClientLoginRequest();
            clr.Identifier = session.Identifier;
            client.Send(clr);
            ServerLoginResponse slr = AssertTypeAndConvert(client.HandleNext(), new ServerLoginResponse());
            byte[] plainData;
            using (MemoryStream ms = new MemoryStream(slr.TestPhrase)) {
                using (RSAStream rsa = new RSAStream(ms, session.ClientPrivateKey)) {
                    plainData = rsa.ReadAll();
                }
            }
            IMessageDigest digest = MessageDigestFactory.CreateDigest(slr.HashAlgorithm);
            digest.Update(plainData);
            byte[] hash = digest.Complete();
            if (!Enumerable.SequenceEqual(hash, slr.TestPhraseHash)) throw new RequestException("The login failed because the server supplied invalid data.");
            ClientLoginData cld = new ClientLoginData();
            cld.DecryptedTestPhrase = plainData;
            client.Send(cld);
            MV2Message loginResult = client.HandleNext();
            if (loginResult.MessageType == DEF_MESSAGE.UNABLE_TO_RPOCESS && loginResult.GetFieldStringValue(DEF_MESSAGE_FIELD.CAUSE, "").Equals("Invalid login data.")) return false;
            loginResult = AssertTypeAndConvert(loginResult, new MV2Message(DEF_MESSAGE.SERVER_LOGIN_RESULT));
            usedClient = client;
            return true;
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
