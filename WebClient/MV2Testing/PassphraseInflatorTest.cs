using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using ISS.MV2.Security;

namespace ISS.MV2.Tests {
    [TestClass]
    public class PassphraseInflatorTest {

        private const string SOME_STRING = "fjklkdgjolajgjiopuä9ru 92u9)§891";
        private const string EXPECTED_PRIMARY = "eXX0zXEd+OExq1a6mqa+YgXbxsne5vwbBczzP5PNg8M=";
        private const string EXPECTED_SECONDARY = "WYuXmp/lDgJ7EGoiEpOQ+vujxzP6jamVTeb8z/VCUII=";

        [TestMethod]
        public void TestPrimary() {
            PassphraseInflator pi = new PassphraseInflator();
            byte[] a = pi.GetPrimaryPassphraseDigest(SOME_STRING.ToCharArray());
            byte[] expected = Convert.FromBase64String(EXPECTED_PRIMARY);
            CollectionAssert.AreEqual(expected, a);
        }

        [TestMethod]
        public void TestSeconary() {
            PassphraseInflator pi = new PassphraseInflator();
            byte[] a = pi.GetSecondaryPassphraseDigest(SOME_STRING.ToCharArray());
            byte[] expected = Convert.FromBase64String(EXPECTED_SECONDARY);
            CollectionAssert.AreEqual(expected, a);
        }

    }
}
