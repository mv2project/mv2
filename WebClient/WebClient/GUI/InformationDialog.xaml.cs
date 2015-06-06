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
    public partial class InformationDialog : ChildWindow {

        public string Content {
            get { return contentBlock.Text; }
            set { contentBlock.Text = value; }
        }

        public InformationDialog() {
            InitializeComponent();
        }

     
        private void OKButton_Click(object sender, RoutedEventArgs e) {
            this.DialogResult = true;
            Close();
        }
    }
}

