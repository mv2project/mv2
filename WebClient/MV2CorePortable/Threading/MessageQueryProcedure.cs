using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.Messaging;
using ISS.MV2.IO;


namespace ISS.MV2.Threading {
    public class MessageQueryProcedure : MessageProcedure<ClientSession, long[]>, IAuthenticatedClientProvider {


        public MessageQueryProcedure(IEventDispatcher dispatcher, ClientSession session)
            : base(dispatcher, session) {
        
        }

        private ICommunicationPartner usedClient;
        
        protected override long[] DoProcedure(ClientSession parameter) {
            LoginProcedure lp = new LoginProcedure(EventDispatcher, parameter);
            bool loggedIn = lp.ExecuteImmediate();
            if (!loggedIn) throw new Exception("Invalid login.");
            ICommunicationPartner client = lp.GetAuthenticatedClient();
            MessageQueryRequest mqr = new MessageQueryRequest();
            client.Send(mqr);
            MessageQueryResponse queryResponse = AssertTypeAndConvert(client.HandleNext(), new MessageQueryResponse());
            usedClient = client;
            return queryResponse.MessageIdentifiers;
        }

        public ICommunicationPartner GetAuthenticatedClient() {
            return usedClient;
        }
    }
}
