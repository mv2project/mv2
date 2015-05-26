using System;
using System.Collections.Generic;

using System.Text;
using System.IO;

namespace ISS.MV2.IO {

    /// <summary>
    /// Provides extension methods for a Stream object.
    /// </summary>
   public static class StreamExtension {

       public delegate void BinIterationDelegate(byte data);

       /// <summary>
       /// Writes a bytes of the given array to the stream.
       /// </summary>
       /// <param name="stream">The stream to write to.</param>
       /// <param name="data">The array with the data to write.</param>
       public static void Write(this Stream stream, byte[] data) {
           stream.Write(data, 0, data.Length);
       }

       public static byte[] ReadFull(this Stream stream, byte[] buffer) {
           int read;
           for (int i = 0; i < buffer.Length; i++) {
               read = stream.ReadByte();
               if (read == -1) throw new EndOfStreamException();
               buffer[i] = (byte)read;
           }
           return buffer;
       }

       public static void Iterate(this Stream stream, BinIterationDelegate binDelegate) {
           int read;
           while ((read = stream.ReadByte()) != -1) {
               binDelegate((byte)read);
           }
       }

       public static byte[] ReadAll(this Stream stream) {
           using (MemoryStream ms = new MemoryStream()) {
               stream.Iterate(new BinIterationDelegate((b) => {
                   ms.WriteByte(b);
               }));
               return ms.ToArray();
           }
       }

    }
}
