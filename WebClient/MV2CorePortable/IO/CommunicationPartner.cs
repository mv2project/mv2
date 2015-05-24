using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.Messaging;


namespace ISS.MV2.IO {
    public interface CommunicationPartner {

        void Send(MV2Message message);

    }
}
