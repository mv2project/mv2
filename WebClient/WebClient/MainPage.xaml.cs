﻿using System;
using System.Collections.Generic;

using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using ISS.MV2.GUI;


namespace ISS.MV2 {
    public partial class MainPage : UserControl {
        public MainPage() {
            InitializeComponent();
            Navigator.SetMainFrame(navigationFrame);
            IO.Debugging.MessageProtocol = protocolList;
        }

       
    }
}
