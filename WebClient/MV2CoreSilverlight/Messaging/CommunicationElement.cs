using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using ISS.MV2.Core.IO;

namespace ISS.MV2.Core.Messaging {

    /// <summary>
    /// The base class for all communication elements.
    /// </summary>
    public abstract class CommunicationElement {


        private readonly int _elementIdentifier;

        /// <summary>
        /// The identifier of this communication element.
        /// </summary>
        protected int ElementIdentifier { get { return _elementIdentifier; } }

        private Encoding encoding = Encoding.UTF8;

        /// <summary>
        /// The encoding of this communication element.
        /// </summary>
        public Encoding Encoding { get { return encoding; } set { encoding = value; } }

        /// <summary>
        /// Creates a new instanc of <see cref="CommunicationElement"/>.
        /// </summary>
        /// <param name="elementIdentifier"></param>
        public CommunicationElement(int elementIdentifier) {
            _elementIdentifier = elementIdentifier;
        }

        /// <summary>
        /// Serializes the content of this element.
        /// </summary>
        /// <param name="outputStream">The stream to write to.</param>
        /// <param name="encoding">The encoding to use.</param>
        protected abstract void doSerialize(Stream outputStream, Encoding encoding);

        public void serialize(Stream outputStream) {
            using (MemoryStream buffer = new MemoryStream()) {
                doSerialize(buffer, encoding);
                int length = (int)buffer.Length;
                outputStream.Write(BinaryTools.ToByteArray(_elementIdentifier));
                outputStream.Write(BinaryTools.ToByteArray(length));
                buffer.WriteTo(outputStream);
                outputStream.Flush();
            }
        }

    }
}
