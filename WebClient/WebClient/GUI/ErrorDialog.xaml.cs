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

namespace ISS.MV2.GUI {
    public partial class ErrorDialog : ChildWindow {
        public ErrorDialog(Exception ex) {
            InitializeComponent();
            messageLabel.Text = ex.Message;
            typeLabel.Content = ex.GetType().FullName;
            stackTrace.Text = ex.ToString();
        }

        public ErrorDialog(string message) {
            InitializeComponent();
            messageLabel.Text = message;
            typeLabel.Content = "";
            stackTrace.Text = "";
        }

        private void CancelButton_Click(object sender, RoutedEventArgs e) {
            Close();
        }
    }
}

