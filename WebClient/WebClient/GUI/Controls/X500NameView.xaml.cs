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
using Org.BouncyCastle.Asn1.X509;
using ISS.MV2.Security;



namespace ISS.MV2.GUI.Controls {
    public partial class X500NameView : UserControl {

        private X509Name name;

        public X509Name Principal {
            get { return name; }
            set {
                name = value;
                UpdateUI();
            }
        }

        public X500NameView() {
            InitializeComponent();
        }

        private void UpdateUI() {
            if (name == null) {

                return;
            }
            DNReader reader = new DNReader(name);
            countryLabel.Content = reader.Country;
            stateLabel.Content = reader.State;
            organizationLabel.Content = reader.Organization;
            organizationUnitLabel.Content = reader.OrganizationUnit;
            commonNameLabel.Content = reader.CommonName;

        }

    }
}
