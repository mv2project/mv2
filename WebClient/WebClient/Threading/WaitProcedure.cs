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

namespace ISS.MV2.Threading {
    /// <summary>
    /// A procedure to simulate long lasting requests.
    /// </summary>
    public class WaitProcedure : MessageProcedure<Void, Void> {

        public WaitProcedure(IEventDispatcher dispatcher)
            : base(dispatcher, Void.Instance) {
        }

        protected override Void DoProcedure(Void parameter) {
            
            for (int i = 0; i < 100; i++) {
                DateTime start = DateTime.Now;
                while (DateTime.Now.Subtract(start).TotalSeconds < 1.0) {
                }
                Update("State: " + i, i);
            }
            return Void.Instance;
        }
    }
}
