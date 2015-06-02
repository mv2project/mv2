using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Collections.Generic;
using System.Collections;
using ISS.MV2.GUI;

namespace ISS.MV2.GUI.Controls {
    public class Popup : ToolTip {

        private IList<object> items;
        private ListBox contextMenu = new ListBox();

        public int SelectedIndex {
            get {
                return items.IndexOf(selected);
            }
        }

        public Popup(Point p, IList<object> items) {
            HorizontalAlignment = System.Windows.HorizontalAlignment.Left;
            VerticalAlignment = System.Windows.VerticalAlignment.Top;
            Margin = new Thickness(p.X, p.Y, 0, 0);
            LostFocus += ActionsPopup_LostFocus;
            this.items = items;
            Content = contextMenu;
            for (int i = 0; i < items.Count; i++) {
                ListBoxItem lbi = new ListBoxItem();
                lbi.MouseLeftButtonUp += lbi_MouseLeftButtonUp;
                lbi.Content = items[i];
                contextMenu.Items.Add(lbi);
            }
            contextMenu.SelectionChanged += contextMenu_SelectionChanged;
            contextMenu.BorderThickness = new Thickness(0);
            contextMenu.Margin = new Thickness(0);
            contextMenu.Padding = new Thickness(0);
            MouseLeftButtonUp += lbi_MouseLeftButtonUp;
            MouseLeave += Popup_MouseLeave;
                
            IsTabStop = true;
            Canvas.SetZIndex(this, 1000);
        }

        void Popup_MouseLeave(object sender, MouseEventArgs e) {
            object element = FocusManager.GetFocusedElement();
            bool isChild = this.IsChild((UIElement)element);
            if (!isChild) Close();
        }


        public Popup(Point p, params object[] elements)
            : this(p, new List<object>(elements)) {
        }

        void lbi_MouseLeftButtonUp(object sender, MouseButtonEventArgs e) {
            e.Handled = true;
            if (selected != null) {
                Close();
            }
        }

        private object selected;
     
        void contextMenu_SelectionChanged(object sender, SelectionChangedEventArgs e) {
            if (contextMenu.SelectedIndex != -1) {
                selected = items[contextMenu.SelectedIndex];
            }
        }



        void ActionsPopup_LostFocus(object sender, RoutedEventArgs e) {
            object element = FocusManager.GetFocusedElement();
            bool isChild = this.IsChild((UIElement)element);
            if (!isChild) Close();
        }

       

        public void Close() {
            if (Parent == null) return;
            if (typeof(Panel).IsAssignableFrom(Parent.GetType())) {
                Panel p = (Panel)Parent;
                p.Children.Remove(this);
            }
        }

    }
}
