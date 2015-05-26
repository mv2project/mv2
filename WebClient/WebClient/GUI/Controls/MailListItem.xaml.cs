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

namespace ISS.MV2.GUI.Controls {
    public partial class MailListItem : UserControl {

        private readonly DetailedContentMessage message;

        public DetailedContentMessage Message { get { return message; } }

        public MailListItem(DetailedContentMessage cm) {
            InitializeComponent();
            this.message = cm;
            subjectText.Text = cm.Subject;
            senderLabel.Content = new ISS.MV2.Security.DNReader(cm.Sender.SubjectDN).CommonName;
        }
    }
}
