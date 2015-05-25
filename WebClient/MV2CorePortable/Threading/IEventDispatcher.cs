using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Threading {
    public interface IEventDispatcher {

        void Invoke(EventInvokationDelegate eventDelegate);

    }
}
