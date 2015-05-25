using System;
using System.Collections.Generic;

using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace ISS.MV2 {
    public partial class MainPage : UserControl {
        public MainPage() {
            InitializeComponent();
        }

        private void Button_Click(object sender, RoutedEventArgs e) {

            var procdure = new ISS.MV2.Threading.WaitProcedure(new ISS.MV2.Threading.WindowsDispatcher(this));
            procdure.Updated += procdure_Updated;
            procdure.Completed += procdure_Completed;
            procdure.Execute();
        }

        void procdure_Completed(ISS.MV2.Threading.MessageProcedure<object, object> sender, object result) {
            progressBar.Value = 0;
        }

        void procdure_Updated(ISS.MV2.Threading.MessageProcedure<object, object> sender, string state, int progress) {
            progressBar.Value = progress;
        }
    }
}
