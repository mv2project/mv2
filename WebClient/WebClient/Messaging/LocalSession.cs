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
using ISS.MV2.IO;

namespace ISS.MV2.Messaging {
    public class LocalSession : ClientSession {

        private const string SERVER_ADDRESS = "shome1.selfhost.eu";

        private static readonly LocalSession local_session = new LocalSession();

        public static LocalSession Current { get { return local_session; } }

        public override IO.ICommunicationPartner CreateClient() {
            MV2Client client = new MV2Client();
            client.Connect(SERVER_ADDRESS);
            return client;
        }

        private LocalSession() {
        
            
        }

        public override Security.IMessageCryptorSettings CryptorSettings {
            get { return new Security.AESWithRSACryptoSettings(); }
        }

        public override Security.IMessageCryptorSettings CreateNewCryptorSettings() {
            return new Security.AESWithRSACryptoSettings();
        }
    }
}
