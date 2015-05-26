using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Threading {
    public interface IProcedureListener<P, R> {

        void Completed(MessageProcedure<P, R> sender, R result);

        void Updated(MessageProcedure<P, R> sender, string state, int progress);

        void Failed(MessageProcedure<P, R> sender, Exception exception);

    }
}
