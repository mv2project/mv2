using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ISS.MV2.IO;
using System.IO;

namespace ISS.MV2.Security {
    public class PassphraseInflator {

        private IMessageDigest digest = new SHA256();

        private Encoding encoding = Encoding.UTF8;

        public IMessageDigest Digest {
            get {
                return digest;
            }
            set {
                digest = value;
            }
        }

        public Encoding Encoding {
            get {
                return encoding;
            }
            set {
                encoding = value;
            }
        }

        public PassphraseInflator() {
            
        }

        public byte[] GetPrimaryPassphraseDigest(char[] passphrase) {
            byte[] digest;
            using (MemoryStream ms = new MemoryStream()) {
                byte[] encoded = Encoding.GetBytes(passphrase);
                ms.Write(encoded);
                ms.Flush();
                IMessageDigest d = Digest.Clone();
                d.Update(ms.ToArray());
                digest = d.Complete();
            }
            return digest;
        }

        public byte[] GetSecondaryPassphraseDigest(char[] passphrase) {
            byte[] result;
            using (MemoryStream ms = new MemoryStream()) {
                ms.Write(Encoding.GetBytes(passphrase));
                ms.Flush();
                List<byte> clearList = new List<byte>(ms.ToArray());
                List<byte> primaryList = new List<byte>(GetPrimaryPassphraseDigest(passphrase));
                byte[] newData = new byte[primaryList.Count + clearList.Count];
                var clearEnum = clearList.GetEnumerator();
                var primaryEnum = primaryList.GetEnumerator();
                for (int i = 0; i < newData.Length; i++) {
                    if (i % 2 == 0) {
                        if (clearEnum.MoveNext()) {
                            newData[i] = clearEnum.Current;
                        } else {
                            primaryEnum.MoveNext();
                            newData[i] = primaryEnum.Current;
                        }
                    } else {
                        if (primaryEnum.MoveNext()) {
                            newData[i] = primaryEnum.Current;
                        } else {
                            clearEnum.MoveNext();
                            newData[i] = clearEnum.Current;
                        }
                    }
                }
                IMessageDigest digest = Digest.Clone();
                digest.Update(newData);
                result = digest.Complete();
            }
            return result;
        }

    }
}
