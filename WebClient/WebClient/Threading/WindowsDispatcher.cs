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
using System.Windows.Threading;

namespace ISS.MV2.Threading {
    public class WindowsDispatcher : IEventDispatcher{

        private readonly Dispatcher dispatcher;

        public WindowsDispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public WindowsDispatcher(DependencyObject dependencyObject)
            : this(dependencyObject.Dispatcher) {
        }

        public void Invoke(EventInvokationDelegate eventDelegate) {
            if (dispatcher.CheckAccess()) {
                eventDelegate();
            } else {
                dispatcher.BeginInvoke(new Action(() => { Invoke(eventDelegate); }));
            }
        }
    }
}
