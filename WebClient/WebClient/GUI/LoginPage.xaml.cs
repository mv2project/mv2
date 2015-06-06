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
using ISS.MV2.Threading;
using ISS.MV2.Messaging;
using System.Threading;

namespace ISS.MV2.GUI {
    public partial class LoginPage : Page {
        public LoginPage() {
            InitializeComponent();
        }

        // Executes when the user navigates to this page.
        protected override void OnNavigatedTo(NavigationEventArgs e) {

        }

        private void Login_Click(object sender, RoutedEventArgs e) {
            
            ClientSession session = LocalSession.Current;
            session.Passphrase = passwordField.Password.ToCharArray();
            identifierField.Text = identifierField.Text.ToLower();
            session.Identifier = identifierField.Text;
            LoadingDialog<ClientSession, bool> dialog = new LoadingDialog<ClientSession, bool>();
            dialog.Show();

            LoginProcedure loginProcedure = new LoginProcedure(dialog, session);
            loginProcedure.Failed += loginProcedure_Failed;
            loginProcedure.AddListener(dialog);
            loginProcedure.Completed += loginProcedure_Completed;
            loginProcedure.Execute();
        }

        void loginProcedure_Failed(MessageProcedure<ClientSession, bool> sender, Exception ex) {
            new ErrorDialog(ex).Show();
        }

        void loginProcedure_Completed(MessageProcedure<ClientSession, bool> sender, bool result) {
            if (!result) {
                new ErrorDialog("The login failed.").Show();
                return;
            }
            Navigator.Navigate(typeof(HomePage));
        }

        private void creatAccountLink_Click(object sender, RoutedEventArgs e) {
            Navigator.Navigate(typeof(SpaceCreationPage));
        }

    }
}
