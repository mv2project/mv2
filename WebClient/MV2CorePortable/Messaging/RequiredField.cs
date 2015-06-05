using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class RequiredField : CheckedField {

        protected override void DoCheck(MV2Message message, System.Reflection.PropertyInfo property, object value) {
            if (value == null) Fail("This field must not be null.");
        }
    }
}
