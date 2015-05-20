using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Core.IO {
    public sealed class BinaryTools {

        private BinaryTools(){
        }

        /// <summary>
        /// Returns the value of the given number encoded in the Big Endian format.
        /// </summary> 
        /// <param name="number">The number to convert.</param>
        /// <returns></returns>
        public static byte[] ToByteArray(int number) {
            byte[] arr = BitConverter.GetBytes(number);
            if (BitConverter.IsLittleEndian) Array.Reverse(arr);
            return arr;
        }



    }
}
