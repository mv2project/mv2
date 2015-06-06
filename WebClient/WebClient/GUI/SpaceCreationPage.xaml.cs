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
using System.Windows.Navigation;
using ISS.MV2.Messaging;
using ISS.MV2.Security;
using ISS.MV2.Threading;
using Void = ISS.MV2.Threading.Void;

namespace ISS.MV2.GUI {
    public partial class SpaceCreationPage : Page {
        public SpaceCreationPage() {
            InitializeComponent();
            domainLabel.Content = "@" + LocalSession.ServerAddress;
            
            for (int i = 1024; i <= 4096; i+=8) {
                keySizeBox.Items.Add("" + i);
                if (i == 2048) {
                    keySizeBox.SelectedIndex = keySizeBox.Items.Count - 1;
                }
            }

        }

        // Executes when the user navigates to this page.
        protected override void OnNavigatedTo(NavigationEventArgs e) {

        }

        private void createAccountButton_Click(object sender, RoutedEventArgs e) {
            char[] pw = passwordBox.Password.ToCharArray();
            char[] pwRepeat = passwordRepeatBox.Password.ToCharArray();
            if (!Enumerable.SequenceEqual(pw, pwRepeat)) {
                new ErrorDialog("Invalid password", "The repeatition of the password does not match the original.").Show();
                return;
            }
            if (string.IsNullOrEmpty(addressBox.Text)) {
                new ErrorDialog("Invalid address", "The address must not be empty.").Show();
                return;
            }
            addressBox.Text = addressBox.Text.ToLower();
            string address = addressBox.Text.Trim();
            if (address.Contains(" ")) {
                new ErrorDialog("Invalid address", "The address must not contain a white space.").Show();
                return;
            }
            if (address.Contains("@")) {
                new ErrorDialog("Invalid address", "The address must not contain a '@'.").Show();
                return;
            }
            if (address.StartsWith(".") || address.EndsWith(".")) {
                new ErrorDialog("Invalid address", "The address must not start or end with a point.").Show();
                return;
            }
            foreach (char c in address.ToCharArray()) {
                if (c == '.' || c == '_' || c == '-') continue;
                if (!char.IsLetterOrDigit(c)) {
                    new ErrorDialog("Invalid address", "The address contains an ivalid character ('" + c + "').").Show();
                    return;
                }
            }
            if (string.IsNullOrWhiteSpace(nameBox.Text)) {
                new ErrorDialog("Invalid or missing name", "Your name must not be null or a white sapce.").Show();
                return;
            }
            if (countryBox.Text.Length != 2 && countryBox.Text.Length != 0) {
                new ErrorDialog("Invalid country code", "The country-code must contain exactly two characters.").Show();
                return;
            }
            LoadingDialog<CertificateSigningRequest, ClientSession> loadingDialog = new LoadingDialog<CertificateSigningRequest, ClientSession>();
            loadingDialog.Show();
            CertificateSigningRequest csr = new CertificateSigningRequest();
            csr.CommonName = address + "@" + LocalSession.ServerAddress;
            csr.Name = nameBox.Text;
            csr.Locale = locationBox.Text;
            csr.State = stateBox.Text;
            csr.Country = countryBox.Text;
            csr.Organization = organizationBox.Text;
            csr.OrganizationUnit = organizationUnitBox.Text;
            SpaceCreationProcedure spaceCreationProcedure = new SpaceCreationProcedure(new WindowsDispatcher(loadingDialog), csr, int.Parse((string)keySizeBox.SelectedItem), LocalSession.Current);
            spaceCreationProcedure.AddListener(loadingDialog);
            spaceCreationProcedure.Completed += spaceCreationProcedure_Completed;
            spaceCreationProcedure.Failed += spaceCreationProcedure_Failed;
            spaceCreationProcedure.Execute();
        }

        void spaceCreationProcedure_Failed(MessageProcedure<CertificateSigningRequest, ClientSession> sender, Exception ex) {
            new ErrorDialog(ex).Show();
        }

        void spaceCreationProcedure_Completed(MessageProcedure<CertificateSigningRequest, ClientSession> sender, ClientSession result) {
            LoadingDialog<ClientSession, Void> loadingDialog = new LoadingDialog<ClientSession, Void>();
            loadingDialog.Show();
            result.Passphrase = passwordBox.Password.ToCharArray();
            KeyPutProcedure keyPutProcedure = new KeyPutProcedure(new WindowsDispatcher(loadingDialog), result);
            keyPutProcedure.AddListener(loadingDialog);
            keyPutProcedure.Completed += keyPutProcedure_Completed;
            keyPutProcedure.Failed += keyPutProcedure_Failed;
            keyPutProcedure.Execute();

        }

        void keyPutProcedure_Failed(MessageProcedure<ClientSession, Void> sender, Exception ex) {
            new ErrorDialog(ex).Show();
        }

        void keyPutProcedure_Completed(MessageProcedure<ClientSession, Void> sender, Void result) {
            GUI.Navigator.Navigate(typeof(HomePage));
        }

    }
}
