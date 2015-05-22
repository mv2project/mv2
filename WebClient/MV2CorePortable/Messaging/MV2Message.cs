using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using ISS.MV2.Core.IO;

namespace ISS.MV2.Core.Messaging {
    public class MV2Message : CommunicationElement {

        private readonly IDictionary<int, MessageField> fields = new Dictionary<int, MessageField>();

        protected ICollection<MessageField> CleanedFields {
            get {
                CleanUp();
                return fields.Values;
            }
        }

        protected ICollection<MessageField> Fields {
            get {
                return fields.Values;
            }
        }

        public MV2Message(DEF_MESSAGE messageType)
            : base(messageType.Identifier) {

        }

        public void CleanUp() {
            SetMessageField(new ContentEncodingField(Encoding), true);
            DoCleanUp();
        }

        protected virtual void DoCleanUp() {

        }

        public void SetMessageField(MessageField messageField, bool overwrite) {
            if (messageField == null) return;
            if (!overwrite && fields.ContainsKey(messageField.FieldType.Identifier)) return;
            fields[messageField.FieldType.Identifier] = messageField;
        }


        public string GetFieldStringValue(DEF_MESSAGE_FIELD fieldType, string defaultValue) {
            if (!fields.ContainsKey(fieldType.Identifier)) return defaultValue;
            return fields[fieldType.Identifier].Content;
        }

        public byte[] GetFieldDataValue(DEF_MESSAGE_FIELD fieldType, byte[] defaultValue) {
            if (!fields.ContainsKey(fieldType.Identifier)) return defaultValue;
            return fields[fieldType.Identifier].BinaryContent;
        }

        protected override void DoSerialize(System.IO.Stream outputStream, Encoding encoding) {
            CleanUp();
            foreach (MessageField mf in Fields) {
                mf.Encoding = Encoding;
                mf.Serialize(outputStream);
            }
            outputStream.Flush();
        }


        protected override void DoDeserialize(System.IO.Stream inputStream, Encoding encoding) {
        }

        public void ClearFields() {
            fields.Clear();
        }

        public override void Deserialize(System.IO.Stream inputStream) {
            base.Deserialize(inputStream);
            ClearFields();
            int identifier;
            int length;
            Stream buffer;
            try {
                while (true) {
                    identifier = BinaryTools.ReadInt(inputStream);
                    length = BinaryTools.ReadInt(inputStream);
                    buffer = BinaryTools.ReadBuffer(inputStream, length);
                    MessageField mf = new MessageField(DEF_MESSAGE_FIELD.Find(identifier));
                    mf.Deserialize(buffer);
                    fields[mf.FieldType.Identifier] = mf;
                }
            } catch (EndOfStreamException ex) {

            }
            Encoding encoding = System.Text.Encoding.GetEncoding(GetFieldStringValue(DEF_MESSAGE_FIELD.CONTENT_ENCODING, System.Text.Encoding.UTF8.WebName));
            this.Encoding = encoding;
            foreach (MessageField mf in fields.Values) {
                mf.Encoding = this.Encoding;
                mf.CompleteDeserialize(encoding);
            }
        }

        public static void Merge(MV2Message destination, MV2Message origin){
            foreach(MessageField mf in origin.CleanedFields){
                destination.SetMessageField(mf, true);
            }
        }

    }
}
