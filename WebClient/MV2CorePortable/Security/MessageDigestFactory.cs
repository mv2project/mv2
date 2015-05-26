using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ISS.MV2.Security {
    public class MessageDigestFactory {

        private MessageDigestFactory() {
        }

        private static readonly List<IMessageDigest> knownAlgorithms = new List<IMessageDigest>(new IMessageDigest[] { new SHA256() });

        public static IMessageDigest CreateDigest(string algorithmName) {
            foreach (IMessageDigest digest in knownAlgorithms) {
                foreach (string algName in digest.AlgorithmNames) {
                    if (algName.Equals(algorithmName, StringComparison.OrdinalIgnoreCase)) {
                        return digest.Clone();
                    }
                }
            }
            throw new NotSupportedException("There is no implementation for the given algorithm name (" + algorithmName + ").");

        }


    }
}
