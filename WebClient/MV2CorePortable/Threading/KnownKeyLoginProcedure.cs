using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using Org.BouncyCastle.Crypto;
using ISS.MV2.IO;
using ISS.MV2.Messaging;
using ISS.MV2.Security;

namespace ISS.MV2.Threading {
    public class KnownKeyLoginProcedure : MessageProcedure<ClientSession, bool>, IAuthenticatedClientProvider {

        private ICommunicationPartner usedClient;
        private readonly ClientSession clientSession;

        public KnownKeyLoginProcedure(IEventDispatcher dispatcher, ClientSession clientSession)
            : base(dispatcher, clientSession) {
                this.clientSession = clientSession;
        }
        
        protected override bool DoProcedure(ClientSession session) {
            ICommunicationPartner client = session.CreateClient();
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

        public ICommunicationPartner GetAuthenticatedClient() {
            if (usedClient == null) {
                if (!DoProcedure(clientSession)) throw new RequestException("Could not authenticate.");
            }
            return usedClient;
        }
    }
}
