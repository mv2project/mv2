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
    public partial class LoadingDialogGUI : ChildWindow {
        public LoadingDialogGUI() {
            InitializeComponent();
        }


        protected ProgressBar ProgressBar { get { return progressBar; } }
        protected ListBox StateBox { get { return stateBox; } }
       
    }
}

