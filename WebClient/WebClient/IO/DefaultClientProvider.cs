using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace ISS.MV2.IO {
    public class DefaultClientProvider : IClientProvider {

        public ICommunicationPartner GetClient(string address) {
            MV2Client client = new MV2Client();
            client.Connect(address.Split('@')[1]);
            return client;
        }
    }
}
