using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using ISS.MV2.IO;
using ISS.MV2.Messaging;
using ISS.MV2.Security;

namespace ISS.MV2.Threading {
    public class KeyPutProcedure : MessageProcedure<ClientSession, Void> {

        public KeyPutProcedure(IEventDispatcher dispatcher, ClientSession clientSession)
            : base(dispatcher, clientSession) {
        
        }

        protected override Void DoProcedure(ClientSession parameter) {
            PassphraseInflator passphraseInflator = new PassphraseInflator();
            byte[] primary = passphraseInflator.GetPrimaryPassphraseDigest(parameter.Passphrase);
            byte[] secondary = passphraseInflator.GetSecondaryPassphraseDigest(parameter.Passphrase);

            byte[] iv = new byte[16];
            var privateKey = parameter.ClientPrivateKey;
            for (int i = 0; i < iv.Length; i++) iv[i] = primary[i];
            byte[] encryptedKey;
            using (MemoryStream ms = new MemoryStream()) {
                using (AESStream aes = new AESStream(ms, primary, iv)) {
                    new PEMFileIO().WritePrivateKey(aes, privateKey);
                    aes.Close();
                }
                encryptedKey = ms.ToArray();
            }
            KnownKeyLoginProcedure lp = new KnownKeyLoginProcedure(EventDispatcher, parameter);
            if (!lp.ExecuteImmediate()) throw new RequestException("The login failed.");
            ICommunicationPartner client = lp.GetAuthenticatedClient();
            KeyPutRequest kpRequest = new KeyPutRequest() { Passphrase = secondary, PrivateKey = encryptedKey };
            client.Send(kpRequest);
            AssertType(client.HandleNext(), DEF_MESSAGE.KEY_PUT_RESPONSE);
            Array.Clear(iv, 0, iv.Length);
            Array.Clear(primary, 0, primary.Length);
            Array.Clear(secondary, 0, secondary.Length);
            return Void.Instance;
        }
    }
}
