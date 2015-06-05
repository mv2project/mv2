using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.IO {
    public static class StringTools {

        /// <summary>
        /// Inserts the given value every n-th character.
        /// </summary>
        /// <param name="value">The value to be inserted.</param>
        /// <param name="characterCount">The size of the blocks to seprate.</param>
        /// <returns></returns>
        public static string Insert(this string text, string value, int n) {
            StringBuilder sb = new StringBuilder();
            char[] charArr = text.ToCharArray();
            for (int i = 0; i < charArr.Length; i++) {
                if (i % n == 0) sb.Append(value);
                sb.Append(charArr[i]);
            }
            return sb.ToString().Trim();
        }

    }
}
