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

namespace ISS.MV2.GUI {
    public class Navigator {

        private static Frame navigationFrame;

        public static void SetMainFrame(Frame frame) { navigationFrame = frame; }

        public static void Navigate(Type type) {
            string name = type.FullName;
            name = name.Replace("ISS.MV2.", "");
            name = ";component/" + name.Replace('.', '/') + ".xaml";
            name =  "/" + type.Assembly.FullName.Split(',')[0]  + name;
            navigationFrame.Navigate(new Uri(name, UriKind.Relative));
        }

    }
}
