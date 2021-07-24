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

    private Label notificationLabel;

    private Notification notification;

    public NotificationSkin(Notification n) {
        this.notification = n;
        notificationLabel = new Label();
        notificationLabel.contentDisplayProperty().bind(n.contentDisplayProperty());
        notificationLabel.fontProperty().bind(n.fontProperty());
        notificationLabel.graphicProperty().bind(n.graphicProperty());
        notificationLabel.graphicTextGapProperty().bind(n.graphicTextGapProperty());
        notificationLabel.textAlignmentProperty().bind(n.textAlignmentProperty());
        notificationLabel.textOverrunProperty().bind(n.textOverrunProperty());
        notificationLabel.textProperty().bind(n.textProperty());
        notificationLabel.wrapTextProperty().bind(n.wrapTextProperty());
        notificationLabel.minWidthProperty().bind(n.minWidthProperty());
        notificationLabel.prefWidthProperty().bind(n.prefWidthProperty());
        notificationLabel.maxWidthProperty().bind(n.maxWidthProperty());
        notificationLabel.minHeightProperty().bind(n.minHeightProperty());
        notificationLabel.prefHeightProperty().bind(n.prefHeightProperty());
        notificationLabel.maxHeightProperty().bind(n.maxHeightProperty());

        notificationLabel.getStyleClass().setAll(n.getStyleClass());
        notificationLabel.setStyle(n.getStyle());
        notificationLabel.setId(n.getId());
    }


    @Override
    public Notification getSkinnable() {
        return notification;
    }

    @Override
    public Node getNode() {
        return notificationLabel;
    }

    @Override
    public void dispose() {
        notification = null;
        notificationLabel = null;
    }
}
