using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.Messaging;
using ISS.MV2.IO;
using System.IO;

namespace ISS.MV2.Threading {
    public class MessageFetchProcedure : MessageProcedure<ClientSession, DetailedContentMessage> {

        private IAuthenticatedClientProvider clientProvider;
        private long messageID;

        public MessageFetchProcedure(IAuthenticatedClientProvider clientProvider, IEventDispatcher dispatcher, ClientSession session, long messageIdentifier)
            : base(dispatcher, session) {
                this.clientProvider = clientProvider;
                this.messageID = messageIdentifier;
        }




        protected override DetailedContentMessage DoProcedure(ClientSession parameter) {
            ICommunicationPartner client = clientProvider.GetAuthenticatedClient();
            MessageFetchRequest mfRequest = new MessageFetchRequest();
            mfRequest.Identifier = messageID;
            client.Send(mfRequest);
             MV2Message clearMessage;
            MessageFetchResponse mfResponse = AssertTypeAndConvert(client.HandleNext(), new MessageFetchResponse());
            using (MemoryStream ms = new MemoryStream(mfResponse.ContentMessage)) {
                MessageParser parser = new MessageParser(ms);
                parser.Settings = parameter.CryptorSettings;
                parser.SetPrivateKey(parameter.ClientPrivateKey);
                clearMessage= parser.ReadNext();
            }
            DetailedContentMessage cm = new DetailedContentMessage();
            MV2Message.Merge(cm, clearMessage);
            cm.Timestamp = mfResponse.Timestamp;
            return cm;
        }
    }
}
