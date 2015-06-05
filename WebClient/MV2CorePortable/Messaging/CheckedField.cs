using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Reflection;


namespace ISS.MV2.Messaging {

    [AttributeUsage(AttributeTargets.Property, AllowMultiple=true, Inherited=true)]
    public abstract class CheckedField : Attribute {

        private MV2Message message;
        private object value;
        private PropertyInfo property;

        public CheckedField() {

        }



        public void Check(MV2Message message, PropertyInfo property, object value) {
            this.message = message;
            this.property = property;
            this.value = value;
            try {
                DoCheck(message, property, value);
            } finally {
                this.message = null;
                this.value = null;
                this.property = null;
            }
        }

        protected abstract void DoCheck(MV2Message message, PropertyInfo property, object value);

        protected void Fail(string message) {
            throw new InvalidFieldException(this.message, property, value, message);
        }


    }
}
