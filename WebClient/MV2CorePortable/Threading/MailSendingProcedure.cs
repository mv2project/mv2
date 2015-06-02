using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.IO;
using ISS.MV2.Messaging;
using ISS.MV2.Security;

namespace ISS.MV2.Threading {
    public class MailSendingProcedure : MessageProcedure<ContentMessage, Void> {

        private readonly ClientSession session;
        private readonly IClientProvider clientProvider;


        public MailSendingProcedure(IEventDispatcher dispatcher, ClientSession session, IClientProvider clientProvider, ContentMessage message)
            : base(dispatcher, message) {
            this.session = session;
            this.clientProvider = clientProvider;
        }

        protected override Void DoProcedure(ContentMessage parameter) {
            ICommunicationPartner partner;
            EncryptedMessage encrypted;
            if (parameter.Sender == null) parameter.Sender = session.ClientCertificate;
            foreach (string receiver in parameter.Receivers) {
                Update("Sending to " + receiver);
                partner = clientProvider.GetClient(receiver);
                Update("Requesting certificate...");
                ClientCertificateRequest ccRequest = new ClientCertificateRequest() { Identifier = receiver };
                partner.Send(ccRequest);
                ClientCertificateResponse ccResponse = AssertTypeAndConvert(partner.HandleNext(), new ClientCertificateResponse());
                parameter.Sign(session.ClientPrivateKey);
                encrypted = new EncryptedMessage(session.CreateNewCryptorSettings(), ccResponse.Certificate.GetPublicKey(), parameter.MessageType);
                MV2Message.Merge(encrypted, parameter);
                MessageDeliveryRequest mdRequest = new MessageDeliveryRequest();
                mdRequest.Receiver = receiver;
                using (var ms = new System.IO.MemoryStream()) {
                    encrypted.Serialize(ms);
                    ms.Position = 0;
                    mdRequest.Content = ms.ToArray();
                }
                partner.Send(mdRequest);
                AssertType(partner.HandleNext(), DEF_MESSAGE.MESSAGE_DELIVERY_RESPONSE);
            }
            return Void.Instance;
        }
    }
}
