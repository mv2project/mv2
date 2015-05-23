using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Security {
   public interface ISymmetricKeyGenerator {

        byte[] GetRandomKey();
        byte[] GetRandomKey(int length);

        byte[] GetRandomIV();

        void SetFixedIV(byte[] iv);

        void SetFixedKey(byte[] key);

        bool HasFixedKeyAndIV { get; }

    }
}
