/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Code derived from javafx.scene.control.skin.TooltipSkin.java from the OpenJFX project
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.controls;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;

public class NotificationSkin implements Skin<Notification> {

    private Label notifLabel;

    private Notification notification;

    public NotificationSkin(Notification n) {
        this.notification = n;
        notifLabel = new Label();
        notifLabel.contentDisplayProperty().bind(n.contentDisplayProperty());
        notifLabel.fontProperty().bind(n.fontProperty());
        notifLabel.graphicProperty().bind(n.graphicProperty());
        notifLabel.graphicTextGapProperty().bind(n.graphicTextGapProperty());
        notifLabel.textAlignmentProperty().bind(n.textAlignmentProperty());
        notifLabel.textOverrunProperty().bind(n.textOverrunProperty());
        notifLabel.textProperty().bind(n.textProperty());
        notifLabel.wrapTextProperty().bind(n.wrapTextProperty());
        notifLabel.minWidthProperty().bind(n.minWidthProperty());
        notifLabel.prefWidthProperty().bind(n.prefWidthProperty());
        notifLabel.maxWidthProperty().bind(n.maxWidthProperty());
        notifLabel.minHeightProperty().bind(n.minHeightProperty());
        notifLabel.prefHeightProperty().bind(n.prefHeightProperty());
        notifLabel.maxHeightProperty().bind(n.maxHeightProperty());

        notifLabel.getStyleClass().setAll(n.getStyleClass());
        notifLabel.setStyle(n.getStyle());
        notifLabel.setId(n.getId());
    }


    @Override
    public Notification getSkinnable() {
        return notification;
    }

    @Override
    public Node getNode() {
        return notifLabel;
    }

    @Override
    public void dispose() {
        notification = null;
        notifLabel = null;
    }
}
