using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.IO;

namespace ISS.MV2.Messaging {
    public interface IMessageProcessor {

        bool Process(CommunicationPartner sender, MV2Message message);

    }
}
