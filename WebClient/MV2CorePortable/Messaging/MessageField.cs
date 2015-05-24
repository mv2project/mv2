using System;
using System.Collections.Generic;
using System.Text;
using ISS.MV2.IO;
using System.IO;

namespace ISS.MV2.Messaging {
    public class MessageField : CommunicationElement {

        private string content;
        public string Content { get { return content; } set { content = value; } }


        private byte[] binaryContent = null;
        public byte[] BinaryContent { get { return binaryContent; } set { binaryContent = value; } }

        public DEF_MESSAGE_FIELD FieldType { get { return DEF_MESSAGE_FIELD.Find(ElementIdentifier); } }


        public MessageField(DEF_MESSAGE_FIELD field, string content)
            : base(field.Identifier) {
            this.content = content;
        }

        public MessageField(DEF_MESSAGE_FIELD field, byte[] content)
            : base(field.Identifier) {
            this.binaryContent = content;
        }

        public MessageField(DEF_MESSAGE_FIELD field)
            : base(field.Identifier) {

        }


        protected override void DoSerialize(System.IO.Stream outputStream, Encoding encoding) {
            if (binaryContent != null) {
                outputStream.Write(binaryContent);
                outputStream.Flush();
                return;
            }
            outputStream.Write(encoding.GetBytes(Content));
            outputStream.Flush();
        }


        private Stream deserializeInput;

        public override void Deserialize(Stream inputStream) {
            this.deserializeInput = inputStream;
        }


        public void CompleteDeserialize(Encoding encoding) {
            if (deserializeInput == null) return;
            DoDeserialize(deserializeInput, encoding);
            deserializeInput.Dispose();
            deserializeInput = null;
        }

        protected override void DoDeserialize(System.IO.Stream inputStream, Encoding encoding) {
            Encoding = encoding;
            if (FieldType.IsBinary) {
                DoDeserializeBinary(inputStream);
            } else {
                DoDeserializeString(inputStream, encoding);
            }
        }

        private void DoDeserializeBinary(Stream inputStream) {
            binaryContent = BinaryTools.ReadAll(inputStream);
        }

        private void DoDeserializeString(Stream inputStream, Encoding encoding) {
            StreamReader reader = new StreamReader(inputStream, encoding);
            int read;
            StringBuilder sb = new StringBuilder();
            while ((read = reader.Read()) != -1) {
                sb.Append((char)read);
            }
            Content = sb.ToString();
        }

        public override string ToString() {
            StringBuilder sb = new StringBuilder();
            sb.Append(FieldType);
            sb.Append(": ");
            if (FieldType.IsBinary) {
                sb.Append(Convert.ToBase64String(BinaryContent));
            } else {
                sb.Append(Content);
            }
            return sb.ToString();
        }

    }
}
