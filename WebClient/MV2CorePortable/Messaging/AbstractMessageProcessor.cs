using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.IO;

namespace ISS.MV2.Messaging {

    public abstract class AbstractMessageProcessor : IMessageProcessor, IMessagePreProcessor {

        private bool filterNullMessage;

        public bool FilterNullMessage {
            get { return filterNullMessage; }
            set { filterNullMessage = value; }
        }


        public AbstractMessageProcessor(bool filterNullMessage) {
        }

        public AbstractMessageProcessor()
            : this(true) {
        }

        public bool Process(CommunicationPartner sender, MV2Message message) {
            if (message == null) return false;
            return DoProcess(sender, message);
        }

        protected abstract bool DoProcess(CommunicationPartner sender, MV2Message message);

        public MV2Message Prepare(CommunicationPartner sender, MV2Message message) {
            if (message == null) return null;
           return DoPrepare(sender, message);
        }

        protected abstract MV2Message DoPrepare(CommunicationPartner sender, MV2Message message);


    }
}
