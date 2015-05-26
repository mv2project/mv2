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

        public bool Process(ICommunicationPartner sender, MV2Message message) {
            if (message == null) return false;
            return DoProcess(sender, message);
        }

        protected abstract bool DoProcess(ICommunicationPartner sender, MV2Message message);

        public MV2Message Prepare(ICommunicationPartner sender, MV2Message message) {
            if (message == null) return null;
           return DoPrepare(sender, message);
        }

        protected abstract MV2Message DoPrepare(ICommunicationPartner sender, MV2Message message);


    }
}
