using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Security.Cryptography;

namespace ISS.MV2.Core.Security {
    public class AESStream : Stream{

        private readonly Stream baseStream;



        public AESStream(Stream baseStream, byte[] key, byte[] iv) {
            this.baseStream = baseStream;
            AesManaged aesManaged = new AesManaged();
            aesManaged.Mode
            ICryptoTransform encryptor = aesManaged.CreateEncryptor(key, iv);
            
        }


        public override bool CanRead {
            get { return baseStream.CanRead; }
        }

        public override bool CanSeek {
            get { return baseStream.CanRead; }
        }

        public override bool CanWrite {
            get { return baseStream.CanWrite; }
        }

        public override void Flush() {
            baseStream.Close();
        }

        public override long Length {
            get { return baseStream.Length; }
        }

        public override long Position {
            get {
                return baseStream.Position;
            }
            set {
                baseStream.Position = value;
            }
        }

        public override int Read(byte[] buffer, int offset, int count) {
            throw new NotImplementedException();
        }

        public override long Seek(long offset, SeekOrigin origin) {
           return baseStream.Seek(offset, origin);
        }

        public override void SetLength(long value) {
            baseStream.SetLength(value);
        }

        public override void Write(byte[] buffer, int offset, int count) {
            baseStream.Write(buffer, offset, count);
        }
    }
}
