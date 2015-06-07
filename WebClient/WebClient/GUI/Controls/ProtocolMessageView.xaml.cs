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
    public partial class ProtocolMessageView : UserControl {

        private MV2Message message;
        public MV2Message Message {
            get { return message; }
            set {
                this.message = value;
                UpdateUI();
            }
        }


        public ProtocolMessageView() {
            InitializeComponent();
        }

        private void UpdateUI() {
            if (message == null) return;
            identifierLabel.Content = message.MessageType.Identifier;
            typeLabel.Content = message.MessageType.Name;
            fieldsGrid.RowDefinitions.Clear();
            int i = 0;
            foreach (MessageField mf in message.Fields) {
                fieldsGrid.RowDefinitions.Add(new RowDefinition());
                fieldsGrid.RowDefinitions.Add(new RowDefinition() { Height = new GridLength(5) });
                Label l = new Label();
                l.VerticalAlignment = System.Windows.VerticalAlignment.Top;
                l.HorizontalAlignment = System.Windows.HorizontalAlignment.Right;
                l.Content = mf.FieldType.ToString() + ":";
                TextBlock tb = new TextBlock();
                tb.TextWrapping = TextWrapping.Wrap;
                if (mf.FieldType.IsBinary) {
                    tb.Text = Convert.ToBase64String(mf.BinaryContent);
                } else {
                    tb.Text = mf.Content;
                }
                fieldsGrid.Children.Add(l);
                fieldsGrid.Children.Add(tb);
                Grid.SetColumn(tb, 1);
                Grid.SetRow(tb, i);
                Grid.SetRow(l, i);
                i += 2;
            }
           
        }

    }
}
