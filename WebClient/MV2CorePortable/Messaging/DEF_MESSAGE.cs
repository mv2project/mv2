using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Core.Messaging {
    public class DEF_MESSAGE {

        private int identifier = -1;
        public int Identifier { get { return identifier; } }


        public static readonly DEF_MESSAGE[] WELL_KNOWN_MESSAGES = new DEF_MESSAGE[] {  };

        private DEF_MESSAGE(int identifier) {
            this.identifier = identifier;
        }



    }
}
