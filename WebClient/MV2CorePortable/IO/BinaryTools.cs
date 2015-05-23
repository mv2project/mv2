using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace ISS.MV2.IO {
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

        public static UInt32 ReverseBytes(UInt32 value) {
            return (value & 0x000000FFU) << 24 | (value & 0x0000FF00U) << 8 |
                   (value & 0x00FF0000U) >> 8 | (value & 0xFF000000U) >> 24;
        }

        public static int ReadInt(Stream stream) {
            byte[] data = Read(stream, 4);
            if (BitConverter.IsLittleEndian) Array.Reverse(data);
            return BitConverter.ToInt32(data, 0);
        }

        public static byte[] Read(Stream stream, int amount) {
            byte[] buffer = new byte[amount];
            stream.Read(buffer, 0, buffer.Length);
            return buffer;
        }

        public static Stream ReadBuffer(Stream stream, int amount) {
            MemoryStream result = new MemoryStream();
            int read;
            for (int i = 0; i < amount; i++) {
                read = stream.ReadByte();
                if (read == -1) throw new EndOfStreamException();
                result.WriteByte((byte) read);
            }
            result.Position = 0;
            return result;
        }

        public static byte[] ReadAll(Stream stream) {
            IList<byte> result = new List<byte>();
            int read;
            while ((read = stream.ReadByte()) != -1) {
                result.Add((byte)read);
            }
            return result.ToArray();
        }



    }
}
