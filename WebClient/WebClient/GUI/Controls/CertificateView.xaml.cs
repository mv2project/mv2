using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using Org.BouncyCastle.X509;
using ISS.MV2.Security;
using ISS.MV2.IO;



namespace ISS.MV2.GUI.Controls {
    public partial class CertificateView : UserControl {

        private X509Certificate certificate;

        private const string ISSUER_HEADING = "Issued by: ";
        private const string EXPIRATION_HEADING = "Expires: ";

        public X509Certificate Certificate {
            get { return certificate; }
            set {
                certificate = value;
                UpdateUI();
            }

        }

        public CertificateView() {
            InitializeComponent();
        }


        private void UpdateUI() {
            if (certificate == null) {
                return;
            }
            DNReader issureReader = new DNReader(certificate.IssuerDN);
            DNReader subjectReader = new DNReader(certificate.SubjectDN);
            subjectCommonNameLabel.Content = subjectReader.CommonName;
            issuerLabel.Content = ISSUER_HEADING + issureReader.CommonName;
            expirationDateLabel.Content = EXPIRATION_HEADING + certificate.NotAfter.ToLongDateString() + " " + certificate.NotAfter.ToLongTimeString();
            issurerView.Principal = certificate.IssuerDN;
            subjectView.Principal = certificate.SubjectDN;
            var key = certificate.GetPublicKey();
            var keyInfo = SubjectPublicKeyInfoFactory.CreateSubjectPublicKeyInfo(key);
            var algIdentifier = keyInfo.AlgorithmID;
            keyAlgorithmLabel.Content = algIdentifier.ObjectID.Id;
            keyDataBlock.Text = IO.BinaryTools.ToHexString(keyInfo.GetEncoded()).Insert(" ", 2);
        }

    }
}
