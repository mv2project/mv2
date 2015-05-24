using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using ISS.MV2;


namespace ISS.MV2.Tests {
    [TestClass]
    public class ToolsTest {

        [TestMethod]
        public void TestDateTimeConversion() {
            long value = 1432492138036;
            DateTime dt = DateTimeTools.FromJavaUnixTime(value);
            Assert.AreEqual(635680961380360000, dt.Ticks);
            DateTime dt2 = DateTimeTools.FromJavaUnixTime(dt.ToJavaUnixTime());
            Assert.AreEqual(dt, dt2);
        }
    }
}
