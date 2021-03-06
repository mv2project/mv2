﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace ISS.MV2.IO {
    public sealed class BinaryTools {

        


        private BinaryTools() {
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

        public static byte[] ToByteArray(long number) {
            byte[] arr = BitConverter.GetBytes(number);
            if (BitConverter.IsLittleEndian) Array.Reverse(arr);
            return arr;
        }

        public static long ReadLong(byte[] data) {
            if (data.Length != 8) throw new ArgumentException("The data is expected to have a length of exact eight bytes.", "data");
            byte[] dataC = new byte[8];
            Array.Copy(data, dataC, data.Length);
            if (BitConverter.IsLittleEndian) Array.Reverse(dataC);
            return BitConverter.ToInt64(dataC, 0);
        }

        public static int ReadInt(Stream stream) {
            byte[] data = Read(stream, 4);
            if (BitConverter.IsLittleEndian) Array.Reverse(data);
            return BitConverter.ToInt32(data, 0);
        }

        public static byte[] Read(Stream stream, int amount) {
            return ((MemoryStream)ReadBuffer(stream, amount)).ToArray();
        }

        public static Stream ReadBuffer(Stream stream, int amount) {
            MemoryStream buffer = new MemoryStream();
            int read;
            for (int i = 0; i < amount; i++) {
                read = stream.ReadByte();
                if (read == -1) throw new EndOfStreamException();
                buffer.WriteByte((byte)read);
            }
            buffer.Position = 0;
            return buffer;
        }



        public static byte[] ReadAll(Stream stream) {
            IList<byte> result = new List<byte>();
            int read;
            while ((read = stream.ReadByte()) != -1) {
                result.Add((byte)read);
            }
            return result.ToArray();
        }

        public static void Iterate(Stream stream, StreamExtension.BinIterationDelegate iterationDelegate) {
            stream.Iterate(iterationDelegate);
        }

        public static string ToHexString(byte[] data) {
            StringBuilder hex = new StringBuilder(data.Length * 2);
            foreach (byte b in data)
                hex.AppendFormat("{0:x2}", b);
            return hex.ToString().ToUpper();
        }

      


    }
}
