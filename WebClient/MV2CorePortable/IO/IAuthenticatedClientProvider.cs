using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.IO {
    public interface IAuthenticatedClientProvider {

        ICommunicationPartner GetAuthenticatedClient();

    }
}
