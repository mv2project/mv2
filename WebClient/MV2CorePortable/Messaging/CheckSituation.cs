using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    [AttributeUsage(AttributeTargets.Property, AllowMultiple=false)]
    public class CheckSituation : Attribute {

        private readonly SituationType situation;
        public SituationType Situation { get { return situation; } }


        public CheckSituation(SituationType situation) {
            this.situation = situation;
        }

       



    }
}
