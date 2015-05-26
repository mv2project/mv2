using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.IO;

namespace ISS.MV2.Threading {
    public class MultiMessageFetchProcedure : MessageProcedure<ClientSession, Void> {

        private IAuthenticatedClientProvider clientProvider;
        private long[] messageIDs;

        public delegate void MessageFetchedDelegate(MultiMessageFetchProcedure sender, DetailedContentMessage message);
        public event MessageFetchedDelegate MessageFetched;

        public MultiMessageFetchProcedure(IAuthenticatedClientProvider clientProvider, IEventDispatcher dispatcher, ClientSession session, long[] messageIdentifiers)
            : base(dispatcher, session) {
                this.messageIDs = messageIdentifiers;
                this.clientProvider = clientProvider;
        }

        protected override Void DoProcedure(ClientSession parameter) {
            MessageFetchProcedure mfp;
            DetailedContentMessage dcm;
            foreach(long id in messageIDs){
                mfp = new MessageFetchProcedure(clientProvider, EventDispatcher, Parameter, id);
                dcm = mfp.ExecuteImmediate();
                EventDispatcher.Invoke(new EventInvokationDelegate(() => {
                    if (MessageFetched != null) MessageFetched(this, dcm);
                }));

            }
            return Void.Instance;
        }
    }
}
