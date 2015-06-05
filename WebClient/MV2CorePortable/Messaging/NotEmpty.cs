using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Messaging {
    public class NotEmpty : CheckedField {


        protected override void DoCheck(MV2Message message, System.Reflection.PropertyInfo property, object value) {
            if (value == null) return;
            if (!typeof(string).IsAssignableFrom(value.GetType())) Fail("A string is expected.");
            if (string.IsNullOrWhiteSpace((string) value)) Fail("The value must not be empty.");
        }
    }
}
