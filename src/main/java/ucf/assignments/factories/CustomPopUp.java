package ucf.assignments.factories;

import com.sun.javafx.util.Utils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Tooltip;
import javafx.stage.Window;

public class CustomPopUp extends PopupControl {

//    ContextMenu
//    new Tooltip
//
//    public void show(Node anchor, Side side, double dx, double dy) {
//        if (anchor == null) return;
//        if (getItems().size() == 0) return;
//
//        getScene().setNodeOrientation(anchor.getEffectiveNodeOrientation());
//        // FIXME because Side is not yet in javafx.geometry, we have to convert
//        // to the old HPos/VPos API here, as Utils can not refer to Side in the
//        // charting API.
//        HPos hpos = side == Side.LEFT ? HPos.LEFT : side == Side.RIGHT ? HPos.RIGHT : HPos.CENTER;
//        VPos vpos = side == Side.TOP ? VPos.TOP : side == Side.BOTTOM ? VPos.BOTTOM : VPos.CENTER;
//
//        // translate from anchor/hpos/vpos/dx/dy into screenX/screenY
//        Point2D point = Utils.pointRelativeTo(anchor,
//                prefWidth(-1), prefHeight(-1),
//                hpos, vpos, dx, dy, true);
//        doShow(anchor, point.getX(), point.getY());
//        Side.
//    }
//
//    private void doShow(Node anchor, double screenX, double screenY) {
//        Event.fireEvent(this, new Event(Menu.ON_SHOWING));
//        if(isShowRelativeToWindow()) {
//            final Scene scene = (anchor == null) ? null : anchor.getScene();
//            final Window win = (scene == null) ? null : scene.getWindow();
//            if (win == null) return;
//            super.show(win, screenX, screenY);
//        } else {
//            super.show(anchor, screenX, screenY);
//        }
//        Event.fireEvent(this, new Event(Menu.ON_SHOWN));
//
//    }
}
