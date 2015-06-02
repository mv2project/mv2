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
using ISS.MV2.GUI.Controls;


namespace ISS.MV2.GUI {
    public partial class HomePage : Page {
        public HomePage() {
            InitializeComponent();
            tabControl.MouseRightButtonDown += tabControl_MouseRightButtonDown;
            tabControl.MouseRightButtonUp += tabControl_MouseRightButtonUp;
        }

        void tabControl_MouseRightButtonUp(object sender, MouseButtonEventArgs e) {
            if (tabControl.SelectedIndex == 0) return;
            Button closeButton = new Button();
            Popup pp = new Popup(e.GetPosition(LayoutRoot), closeButton);
            closeButton.Content = "Close";
            closeButton.Click += new RoutedEventHandler((o, ev) => {
                pp.Close();
                tabControl.Items.Remove(tabControl.SelectedItem);
            });
           
            LayoutRoot.Children.Add(pp);
        }

        


        void tabControl_MouseRightButtonDown(object sender, MouseButtonEventArgs e) {
            e.Handled = true;
        }

        // Executes when the user navigates to this page.
        protected override void OnNavigatedTo(NavigationEventArgs e) {
            Refresh();
        }

        private void Refresh() {
            mailList.Items.Clear();
            MessageQueryProcedure mqp = new MessageQueryProcedure(new WindowsDispatcher(this), LocalSession.Current);
            mqp.Failed += mqp_Failed;
            mqp.Completed += mqp_Completed;

            mqp.Execute();
        }

        void mqp_Completed(MessageProcedure<ClientSession, long[]> sender, long[] result) {
            MultiMessageFetchProcedure mmfp = new MultiMessageFetchProcedure((MessageQueryProcedure)sender, new WindowsDispatcher(this), LocalSession.Current, result);
            mmfp.Failed += mmfp_Failed;
            mmfp.MessageFetched += mmfp_MessageFetched;
            mmfp.Execute();
        }

        void mmfp_Failed(MessageProcedure<ClientSession, Threading.Void> sender, Exception ex) {
            new ErrorDialog(ex).Show();
        }

        void mmfp_MessageFetched(MultiMessageFetchProcedure sender, DetailedContentMessage message) {
            mailList.Items.Add(new Controls.MailListItem(message));
        }

        void mqp_Failed(MessageProcedure<ClientSession, long[]> sender, Exception ex) {
            new ErrorDialog(ex).Show();
        }

        private void Button_Click(object sender, RoutedEventArgs e) {
            Refresh();
        }

        private void mailList_SelectionChanged(object sender, SelectionChangedEventArgs e) {
            DetailedContentMessage message = null;
            if (mailList.SelectedItem != null) {
                message = ((Controls.MailListItem)mailList.SelectedItem).Message;
            }
            mailView.Message = message;
        }

        private void NewMailButton_Clicked(object sender, RoutedEventArgs e) {
            var creatorControl = new Controls.MailCreatorControl();
            TabItem item = new TabItem() { Header = "New Mail", Content = creatorControl };
            tabControl.Items.Add(item);
            tabControl.SelectedItem = item;
        }

    }
}
