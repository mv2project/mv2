using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2 {
    public static class DateTimeTools {


        public static DateTime FromJavaUnixTime(long javaUnixTime) {
            DateTime dt = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
            while (javaUnixTime > int.MaxValue) {
                dt = dt.Add(new TimeSpan(0,0,0,0,int.MaxValue));
                javaUnixTime -= int.MaxValue;
            }
            dt = dt.Add(new TimeSpan(0, 0, 0, 0, (int)javaUnixTime));
            return dt.ToLocalTime();
        }

        public static long ToJavaUnixTime(this DateTime dateTime) {
            DateTime dt = dateTime.ToUniversalTime();
            TimeSpan ts = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc).Subtract(dt);
            return (Math.Abs(ts.Ticks) / 10000);
        }

    }
}
