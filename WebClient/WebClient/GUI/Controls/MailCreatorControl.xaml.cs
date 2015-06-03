using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using ISS.MV2.Messaging;
using ISS.MV2.Threading;
using Void = ISS.MV2.Threading.Void;

namespace ISS.MV2.GUI.Controls {
    public partial class MailCreatorControl : UserControl {
        public MailCreatorControl() {
            InitializeComponent();
        }

        private void sendButton_Click(object sender, RoutedEventArgs e) {
            string[] receiversArr = receiversField.Text.Split(';');
            List<string> receivers = new List<string>();
            foreach (string receiver in receiversArr) {
                if (receiver == null || string.IsNullOrWhiteSpace(receiver) || !receiver.Contains("@") || receivers.Contains(receiver)) continue;
                receivers.Add(receiver);
            }
            ContentMessage cm = new ContentMessage();
            cm.Sender = LocalSession.Current.ClientCertificate;
            cm.Receivers = receivers.ToArray();
            cm.Subject = subjectField.Text;
            cm.Content = contentField.Text;
            LoadingDialog<ContentMessage, Void> loadingDialog = new LoadingDialog<ContentMessage, Void>();
            loadingDialog.Show();
            MailSendingProcedure msp = new MailSendingProcedure(new WindowsDispatcher(loadingDialog), LocalSession.Current, new IO.DefaultClientProvider(), cm);
            msp.AddListener(loadingDialog);
            msp.Completed += msp_Completed;
            msp.Execute();
        }

        void msp_Completed(MessageProcedure<ContentMessage, Void> sender, Void result) {
            System.Diagnostics.Debug.WriteLine("Completed!");
        }
    }
}
