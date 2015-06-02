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

namespace ISS.MV2.GUI {
    public static class GUIExtension {

        /// <summary>
        /// Gibt an ob das angegebene Element ein Kind-Element ist.
        /// </summary>
        /// <param name="el"></param>
        /// <param name="second">Das zu prüfende Element.</param>
        /// <returns><code>true</code>, wenn das zu prüfende Element ein Kind oder die Instanz selbst ist.</returns>
        public static bool IsChild(this UIElement el, UIElement second) {
            if (second == el) return true;
            if (second == null) return false;
            if (!typeof(FrameworkElement).IsAssignableFrom(second.GetType())) return false;
            FrameworkElement fe = (FrameworkElement)second;
            if (fe.Parent == null) return false;
            if (!typeof(UIElement).IsAssignableFrom(fe.Parent.GetType())) return false;
            UIElement elP = (UIElement)fe.Parent;
            return IsChild(el, elP);
        }

        public static bool ContainsFocused(this UIElement el) {
            object fc = FocusManager.GetFocusedElement();
            if (fc == null) return false;
            if (typeof(UIElement).IsAssignableFrom(fc.GetType())) {
                if (fc == el) return true;
                return el.IsChild((UIElement)fc);
            }
            return false;
        }

        public static UIElement GetFocused(this UIElement e) {
            return (UIElement)FocusManager.GetFocusedElement();
        }

     

    }
}
