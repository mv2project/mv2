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
using ISS.MV2.Threading;

namespace ISS.MV2.GUI {
    public class LoadingDialog<P, R> : LoadingDialogGUI, IProcedureListener<P, R>, IEventDispatcher  {


        private readonly IEventDispatcher eventDispatcher;
        private string currentState = null;

        public bool RemainOpened { get; set; }

        public LoadingDialog() {
            eventDispatcher = new WindowsDispatcher(this);
            RemainOpened = false;
        }

        public void Completed(MessageProcedure<P, R> sender, R result) {
            if (RemainOpened) return;
            Close();
        }

        public void Updated(MessageProcedure<P, R> sender, string state, int progress) {
            if (progress > 0) {
                ProgressBar.IsIndeterminate = false;
                ProgressBar.Value = progress;
            }
            if (!string.Equals(currentState, state)) {
                currentState = state;
                StateBox.Items.Insert(0, state);
            }
        }

        public void Failed(MessageProcedure<P, R> sender, Exception exception) {
            System.Diagnostics.Debug.WriteLine(exception);
            if (RemainOpened) return;
            Close();
        }

        public void Invoke(EventInvokationDelegate eventDelegate) {
            eventDispatcher.Invoke(eventDelegate);
        }
    }
}
