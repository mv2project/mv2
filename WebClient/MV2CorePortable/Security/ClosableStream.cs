using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace ISS.MV2.Security {
    public class ClosableStream : Stream {

        private readonly Stream stream;

      
        public delegate void CloseDelegate();

        private CloseDelegate closer;

        public ClosableStream(Stream stream, CloseDelegate closer) {
            this.stream = stream;
            this.closer = closer;
        }


        public void Close() {
            if (closer != null) closer();
        }

        public override bool CanRead {
            get { return stream.CanRead; }
        }

        public override bool CanSeek {
            get {return stream.CanSeek; }
        }

        public override bool CanWrite {
            get { return stream.CanWrite; }
        }

        public override void Flush() {
            stream.Flush();
        }

        public override long Length {
            get { return stream.Length; }
        }

        public override long Position {
            get {
                return stream.Position;
            }
            set {
                stream.Position = value;
            }
        }

        public override int Read(byte[] buffer, int offset, int count) {
           return stream.Read(buffer, offset, count);
        }

        public override long Seek(long offset, SeekOrigin origin) {
            return stream.Seek(offset, origin);
        }

        public override void SetLength(long value) {
            stream.SetLength(value);
        }

        public override void Write(byte[] buffer, int offset, int count) {
            stream.Write(buffer, offset, count);
        }
    }
}
