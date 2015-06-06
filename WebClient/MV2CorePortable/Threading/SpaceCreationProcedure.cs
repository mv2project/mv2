using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.Security;
using ISS.MV2.Messaging;
using ISS.MV2.IO;
using Org.BouncyCastle.Crypto;

namespace ISS.MV2.Threading {
    public class SpaceCreationProcedure : MessageProcedure<CertificateSigningRequest, ClientSession> {

        private readonly int keySize;
        private readonly ClientSession clientSession;


        public SpaceCreationProcedure(IEventDispatcher dispatcher, CertificateSigningRequest csr, int keySize, ClientSession clientSession)
            : base(dispatcher, csr) {
            this.keySize = keySize;
            this.clientSession = clientSession;
        }




        protected override ClientSession DoProcedure(CertificateSigningRequest parameter) {
            Update("Generating RSA keypair...");
            AsymmetricCipherKeyPair keyPair = RSAStream.GetRandomRSAKey(keySize);
            Update("Connecting to the server...");
            clientSession.Identifier = parameter.CommonName;
            ICommunicationPartner client = clientSession.CreateClient();
            var pkcs10CSR = parameter.GeneratePKCS10(keyPair);
            SpaceCreationRequest scRequest = new SpaceCreationRequest();
            scRequest.SigningRequest = pkcs10CSR;
            Update("Sending the space creation request...");
            client.Send(scRequest);
            SpaceCreationResponse scResponse = AssertTypeAndConvert(client.HandleNext(), new SpaceCreationResponse());
            clientSession.ClientCertificate = scResponse.Certificate;
            clientSession.ClientPrivateKey = keyPair.Private;
            return clientSession;
        }


    }
}
