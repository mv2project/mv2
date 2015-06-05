using System;
using System.Collections.Generic;
using System.Reflection;
using System.Linq;
using System.Text;
using System.IO;

namespace ISS.MV2.Messaging {
    public class InvalidFieldException : Exception {

        private PropertyInfo affectedProperty;
        private object value;
        private MV2Message message;


        public PropertyInfo AffectedProperty { get { return affectedProperty; } }
        public object FieldValue { get { return value; } }
        public MV2Message Message { get { return message; } }

        public InvalidFieldException(MV2Message containingMessage, PropertyInfo property, object value, string exceptionMessage):base(BuildMessage(containingMessage, property, value, exceptionMessage)) {
            this.message = containingMessage;
            this.affectedProperty = property;
            this.value = value;
        }


        private static string BuildMessage(MV2Message containingMessage, PropertyInfo property, object value, string exceptionMessage) {
            using (StringWriter writer = new StringWriter()) {
                writer.WriteLine("The validation of the message-field '" + property.Name + "' failed: " + exceptionMessage);
                writer.Write("Current Value: ");
                writer.WriteLine(value);
                writer.WriteLine("In messgage:");
                writer.WriteLine(containingMessage);
                return writer.ToString();
            }
        }


    }
}
