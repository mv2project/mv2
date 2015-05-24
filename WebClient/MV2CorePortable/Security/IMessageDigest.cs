using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Security {
    public interface IMessageDigest {

        void Update(byte[] data);
        void Update(byte data);

        byte[] Complete();
        void Reset();

        IMessageDigest Clone();

    }
}
