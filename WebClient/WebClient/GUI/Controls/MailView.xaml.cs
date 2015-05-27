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


namespace ISS.MV2.GUI.Controls {
    public partial class MailView : UserControl {

        private DetailedContentMessage message;

        public DetailedContentMessage Message {
            get {
                return message;
            }
            set {
                message = value;
                if(value == null) {
                    Clear();
                    return;
                }
                UpdateView();
            }
        }

        public MailView() {
            InitializeComponent();
            Clear();
        }

        private void Clear() {
            subjectTextBox.Text = "";
            receivedLabel.Content = "";
            senderLabel.Content = "";
            contentTextBlock.Text = "";
        }

        private void UpdateView() {
            subjectTextBox.Text = message.Subject;
            senderLabel.Content = new ISS.MV2.Security.DNReader(message.Sender.SubjectDN).CommonName;
            DateTime timestamp = message.Timestamp;
            receivedLabel.Content = timestamp.ToShortDateString() + " " + timestamp.ToShortTimeString();
            contentTextBlock.Text = message.Content;
            UpdateLayout();
        }

    }
}
