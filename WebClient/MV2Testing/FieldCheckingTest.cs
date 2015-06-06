using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using ISS.MV2.Messaging;
using Out = System.Diagnostics.Debug;

namespace ISS.MV2.Tests {
    [TestClass]
    public class FieldCheckingTest {



        [TestMethod]
        public void TestMessageDeliveryRequest() {
            MessageDeliveryRequest mdr = new MessageDeliveryRequest();
            System.Collections.IList idlist = new System.Collections.Generic.List<object>();
            idlist.Add(Org.BouncyCastle.Asn1.X509.X509Name.Name);
            System.Collections.IList valuelist = new System.Collections.Generic.List<object>();
            valuelist.Add("MaxMustermann");
            var smth = new Org.BouncyCastle.Asn1.X509.X509Name(idlist, valuelist);
            bool thrown = false;
            try {
                mdr.CheckFields(SituationType.SENDING);
            } catch (InvalidFieldException e) {
                Out.WriteLine(e);
                thrown = true;
            }
            Assert.IsTrue(thrown);
            thrown = false;
            mdr.Receiver = "   ";
            try {
                mdr.CheckFields(SituationType.SENDING);
            } catch (InvalidFieldException e) {
                thrown = true;
                Out.WriteLine(e);
            }
            Assert.IsTrue(thrown);
            thrown = false;
            mdr.Receiver = "valid@test.de";
            mdr.CheckFields(SituationType.SENDING);
        }
    }
}
