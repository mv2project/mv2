using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Threading {
    public sealed class Void {

        private static readonly Void instance = new Void();
        public static Void Instance { get { return instance; } }

        private Void() {
        }

    }
}
