using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Net.Sockets;
using System.IO;
using System.Threading;

namespace ISS.MV2.IO {
    public class ClientSocket : Stream {

        private Socket socket;

        private const int PORT = 4503;

        private bool connecting = false;
        private bool connected = false;

        private readonly object async_lock = new object();

        public ClientSocket() {
            socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        }

        private AutoResetEvent connect_event = new AutoResetEvent(false);

        public void Connect(string address) {
            lock (async_lock) {
                if (connecting || connected) return;
                connecting = true;
                SocketAsyncEventArgs args = new SocketAsyncEventArgs() {
                    SocketClientAccessPolicyProtocol = SocketClientAccessPolicyProtocol.Http,
                    RemoteEndPoint = new DnsEndPoint(address, PORT)
                };
                args.Completed += Connect_Completed;
                if (!socket.ConnectAsync(args)) {
                    connecting = false;
                    connected = (args.SocketError == SocketError.Success);
                    return;
                }

            }
            connect_event.WaitOne();
        }

        private void Connect_Completed(object sender, SocketAsyncEventArgs e) {
            lock (async_lock) {
                connecting = false;
                connected = (e.SocketError == SocketError.Success);
                connect_event.Set();
            }

        }


        public override bool CanRead {
            get { return true; }
        }

        public override bool CanSeek {
            get { return false; }
        }

        public override bool CanWrite {
            get { return true; }
        }

        public override void Flush() {

        }

        public override long Length {
            get { throw new NotSupportedException(); }
        }

        public override long Position {
            get {
                throw new NotSupportedException();
            }
            set {
                throw new NotSupportedException();
            }
        }

        private readonly object read_lock = new object();
        private int bytesRead;
        private AutoResetEvent read_event = new AutoResetEvent(false);

        public override int Read(byte[] buffer, int offset, int count) {
            SocketAsyncEventArgs args = new SocketAsyncEventArgs();
            args.SetBuffer(buffer, offset, count);
            args.Completed += read_Completed;
            lock (read_lock) {
                if (!socket.ReceiveAsync(args)) {
                    if (args.SocketError != SocketError.Success) throw new IOException(args.SocketError.ToString());
                    bytesRead = args.BytesTransferred;
                    return bytesRead;
                }
            }
            read_event.WaitOne();
            return bytesRead;
        }

        private void read_Completed(object sender, SocketAsyncEventArgs e) {
            lock (read_lock) {
                bytesRead = e.BytesTransferred;
                read_event.Set();
            }
        }

        public override long Seek(long offset, SeekOrigin origin) {
            throw new NotSupportedException();
        }

        public override void SetLength(long value) {
            throw new NotSupportedException();
        }


        private readonly object write_lock = new object();
        private AutoResetEvent write_event = new AutoResetEvent(false);


        public override void Write(byte[] buffer, int offset, int count) {
            SocketAsyncEventArgs e = new SocketAsyncEventArgs();
            e.SetBuffer(buffer, offset, count);
            e.Completed += write_Completed;
            lock (write_lock) {
                if (!socket.SendAsync(e)) {
                    if (e.SocketError != SocketError.Success) throw new IOException(e.SocketError.ToString());
                    return;
                }
            }
            write_event.WaitOne();
        }

        private void write_Completed(object sender, SocketAsyncEventArgs e) {
            lock (write_lock) {
                write_event.Set();
            }
        }
    }
}
