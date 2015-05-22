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
using ISS.MV2.Core.Messaging;


namespace ISS.MV2.WebClient.IO {
    public class MV2Client {

        private ClientSocket connection;

        public MV2Client() {
        
        }

        public void Connect(string host) {
            if (connection != null) throw new AlreadyConnectedException();
            connection = new ClientSocket();
            connection.Connect(host);
            
        }

        public void Send(MV2Message message) {
            message.Serialize(connection);
        }

    }
}
