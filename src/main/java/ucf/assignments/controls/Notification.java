/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Code derived from javafx.scene.control.Tooltip.java
 *  and javafx.scene.control.ContextMenu from the OpenJFX project
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.controls;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.stage.PopupWindowHelper;
import com.sun.javafx.util.Utils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.beans.value.WritableValue;
import javafx.css.*;
import javafx.css.converter.*;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Notification extends PopupControl {

    public Notification() {
        this(null);
    }

    public Notification(String text) {
        super();
        if (text != null) setText(text);
        bridge = new CSSBridge();
        PopupWindowHelper.getContent(this).setAll(bridge);
        getStyleClass().setAll("tooltip");
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/
    /**
     * The text to display in the notification. If the text is set to null,
     * an empty string will be displayed, despite the value being null.
     *
     * @return the text property
     */
    public final StringProperty textProperty() {
        return text;
    }

    public final void setText(String value) {
        textProperty().setValue(value);
    }

    public final String getText() {
        return text.getValue() == null ? "" : text.getValue();
    }

    private final StringProperty text = new SimpleStringProperty(this, "text", "") {
        // TODO listeners?
    };


    /**
     * Specifies the behavior for lines of text <em>when text is multiline</em>.
     * Unlike {@link #contentDisplayProperty() contentDisplay} which affects the
     * graphic and text, this setting only affects multiple lines of text
     * relative to the text bounds.
     *
     * @return the text alignment property
     */
    public final ObjectProperty<TextAlignment> textAlignmentProperty() {
        return textAlignment;
    }

    public final void setTextAlignment(TextAlignment value) {
        textAlignmentProperty().setValue(value);
    }

    public final TextAlignment getTextAlignment() {
        return textAlignmentProperty().getValue();
    }

    private final ObjectProperty<TextAlignment> textAlignment =
            new SimpleStyleableObjectProperty<>(TEXT_ALIGNMENT, this, "textAlignment", TextAlignment.LEFT);

    /**
     * Specifies the behavior to use if the text of the {@code OldNotification}
     * exceeds the available space for rending text.
     *
     * @return the text overrun property
     */
    public final ObjectProperty<OverrunStyle> textOverrunProperty() {
        return textOverrun;
    }

    public final void setTextOverrun(OverrunStyle value) {
        textOverrunProperty().setValue(value);
    }

    public final OverrunStyle getTextOverrun() {
        return textOverrunProperty().getValue();
    }

    private final ObjectProperty<OverrunStyle> textOverrun =
            new SimpleStyleableObjectProperty<OverrunStyle>(TEXT_OVERRUN, this, "textOverrun", OverrunStyle.ELLIPSIS);


    /**
     * If a run of text exceeds the width of the
     * {@code OldNotification}, then this variable indicates
     * whether the text should wrap onto another line.
     *
     * @return the wrap text property
     */
    public final BooleanProperty wrapTextProperty() {
        return wrapText;
    }

    public final void setWrapText(boolean value) {
        wrapTextProperty().setValue(value);
    }

    public final boolean isWrapText() {
        return wrapTextProperty().get();
    }

    private final BooleanProperty wrapText =
            new SimpleStyleableBooleanProperty(WRAP_TEXT, this, "wrapText", false);


    /**
     * The default font to use for text in the {@code OldNotification}.
     * If the OldNotification's text is rich text then this font may
     * or may not be used depending on the font information embedded in
     * the rich text, but in any case where a default font is required,
     * this font will be used
     *
     * @return the font property
     */
    public final ObjectProperty<Font> fontProperty() {
        return font;
    }

    public final void setFont(Font value) {
        fontProperty().setValue(value);
    }

    public final Font getFont() {
        return fontProperty().getValue();
    }

    private final ObjectProperty<Font> font = new StyleableObjectProperty<Font>(Font.getDefault()) {
        private boolean fontSetByCss = false;

        @Override
        public void applyStyle(StyleOrigin newOrigin, Font value) {
            try {
                fontSetByCss = true;
                super.applyStyle(newOrigin, value);
            } catch (Exception e) {
                throw e;
            } finally {
                fontSetByCss = false;
            }
        }

        @Override
        public void set(Font value) {
            final Font oldValue = get();
            StyleOrigin origin = ((StyleableObjectProperty<Font>) font).getStyleOrigin();
            if (origin == null || (value != null ? !value.equals(oldValue) : oldValue != null)) {
                super.set(value);
            }
        }

        @Override
        protected void invalidated() {
            if (!fontSetByCss) {
                NodeHelper.reapplyCSS(Notification.this.bridge);
            }
        }

        @Override
        public CssMetaData<Notification.CSSBridge, Font> getCssMetaData() {
            return FONT;
        }

        @Override
        public Object getBean() {
            return Notification.this;
        }

        @Override
        public String getName() {
            return "font";
        }
    };

    /**
     * The duration that the notification should remain showing for until
     * it is no longer visible to the user.
     *
     * @return the show duration property
     */
    public final ObjectProperty<Duration> showDurationProperty() {
        return showDurationProperty;
    }

    public final void setShowDuration(Duration showDuration) {
        showDurationProperty.set(showDuration);
    }
    public final Duration getShowDuration() {
        return showDurationProperty.get();
    }
    private final ObjectProperty<Duration> showDurationProperty
            = new SimpleStyleableObjectProperty<>(SHOW_DURATION, this, "showDuration", new Duration(3000));


    public final ObjectProperty<Node> graphicProperty() {
        return graphic;
    }
    public final void setGraphic(Node value) {
        graphicProperty().setValue(value);
    }
    public final Node getGraphic() {
        return graphicProperty().getValue();
    }
    private final ObjectProperty<Node> graphic = new StyleableObjectProperty<Node>() {
        @Override
        public CssMetaData getCssMetaData() {
            return GRAPHIC;
        }

        @Override
        public Object getBean() {
            return Notification.this;
        }

        @Override
        public String getName() {
            return "graphic";
        }
    };

    private StyleableStringProperty imageUrlProperty() {
        if (imageUrl == null) {
            imageUrl = new StyleableStringProperty() {
                StyleOrigin origin = StyleOrigin.USER;

                @Override
                public void applyStyle(StyleOrigin origin, String v) {
                    this.origin = origin;

                    if (graphic == null || !graphic.isBound()) super.applyStyle(origin, v);

                    this.origin = StyleOrigin.USER;
                }

                @Override
                protected void invalidated() {
                    final String url = super.get();

                    if (url == null) {
                        ((StyleableProperty<Node>) (WritableValue<Node>) graphicProperty()).applyStyle(origin, null);
                    } else {
                        final Node graphicNode = Notification.this.getGraphic();
                        if (graphicNode instanceof ImageView) {
                            final ImageView imageView = (ImageView) graphicNode;
                            final Image image = imageView.getImage();
                            if (image != null) {
                                final String imageViewUrl = image.getUrl();
                                if (url.equals(imageViewUrl)) return;
                            }
                        }

                        final Image img = StyleManager.getInstance().getCachedImage(url);

                        if (img != null) {
                            ((StyleableProperty<Node>) (WritableValue<Node>) graphicProperty()).applyStyle(origin, new ImageView(img));
                        }
                    }
                }

                @Override
                public String get() {
                    final Node graphic = getGraphic();
                    if (graphic instanceof ImageView) {
                        final Image image = ((ImageView) graphic).getImage();
                        if (image != null) {
                            return image.getUrl();
                        }
                    }
                    return null;
                }

                @Override
                public StyleOrigin getStyleOrigin() {
                    return graphic != null ? ((StyleableProperty<Node>) (WritableValue<Node>) graphic).getStyleOrigin() : null;
                }

                @Override
                public Object getBean() {
                    return Notification.this;
                }

                @Override
                public String getName() {
                    return "imageUrl";
                }

                @Override
                public CssMetaData<Notification.CSSBridge, String> getCssMetaData() {
                    return GRAPHIC;
                }
            };
        }
        return imageUrl;
    }

    private StyleableStringProperty imageUrl = null;

    /**
     * Specifies the positioning of the graphic relative to the text
     *
     * @return the content display property
     */
    public final ObjectProperty<ContentDisplay> contentDisplayProperty() {
        return contentDisplay;
    }

    public final void setContentDIsplay(ContentDisplay value) {
        contentDisplayProperty().setValue(value);
    }

    public final ContentDisplay getContentDisplay() {
        return contentDisplayProperty().getValue();
    }

    public final ObjectProperty<ContentDisplay> contentDisplay =
            new SimpleStyleableObjectProperty<>(CONTENT_DISPLAY, this, "contentDisplay", ContentDisplay.LEFT);

    /**
     * The amount of space between the graphic and the text
     *
     * @return the graphic text gap property
     */
    public final DoubleProperty graphicTextGapProperty() {
        return graphicTextGap;
    }

    public final void setGraphicTextGap(double value) {
        graphicTextGapProperty().setValue(value);
    }

    public final double getGraphicTextGap() {
        return graphicTextGapProperty().getValue();
    }

    private final DoubleProperty graphicTextGap =
            new SimpleStyleableDoubleProperty(GRAPHIC_TEXT_GAP, this, "graphicTextGap", 4d);


    /***************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     **************************************************************************/

    @Override
    protected Skin<?> createDefaultSkin() {
        return new NotificationSkin(this);
    }

    /***************************************************************************
     *                                                                         *
     *                         Stylesheet Handling                             *
     *                                                                         *
     **************************************************************************/

    private static final CssMetaData<Notification.CSSBridge, Font> FONT =
            new FontCssMetaData<Notification.CSSBridge>("-fx-font", Font.getDefault()) {

                @Override
                public boolean isSettable(Notification.CSSBridge cssBridge) {
                    return !cssBridge.notification.fontProperty().isBound();
                }

                @Override
                public StyleableProperty<Font> getStyleableProperty(Notification.CSSBridge cssBridge) {
                    return (StyleableProperty<Font>) (WritableValue<Font>) cssBridge.notification.fontProperty();
                }
            };

    private static final CssMetaData<Notification.CSSBridge, TextAlignment> TEXT_ALIGNMENT =
            new CssMetaData<Notification.CSSBridge, TextAlignment>("-fx-text-alignment",
                    new EnumConverter<TextAlignment>(TextAlignment.class),
                    TextAlignment.LEFT) {

                @Override
                public boolean isSettable(Notification.CSSBridge cssBridge) {
                    return !cssBridge.notification.textAlignmentProperty().isBound();
                }

                @Override
                public StyleableProperty<TextAlignment> getStyleableProperty(Notification.CSSBridge cssBridge) {
                    return (StyleableProperty<TextAlignment>) (WritableValue<TextAlignment>) cssBridge.notification.textAlignmentProperty();
                }
            };

    private static final CssMetaData<Notification.CSSBridge, OverrunStyle> TEXT_OVERRUN =
            new CssMetaData<Notification.CSSBridge, OverrunStyle>("-fx-text-overrun",
                    new EnumConverter<OverrunStyle>(OverrunStyle.class),
                    OverrunStyle.ELLIPSIS) {

                @Override
                public boolean isSettable(Notification.CSSBridge cssBridge) {
                    return !cssBridge.notification.textOverrunProperty().isBound();
                }

                @Override
                public StyleableProperty<OverrunStyle> getStyleableProperty(Notification.CSSBridge cssBridge) {
                    return (StyleableProperty<OverrunStyle>) (WritableValue<OverrunStyle>) cssBridge.notification.textOverrunProperty();
                }
            };

    private static final CssMetaData<Notification.CSSBridge, Boolean> WRAP_TEXT =
            new CssMetaData<Notification.CSSBridge, Boolean>("-fx-wrap-text",
                    BooleanConverter.getInstance(), Boolean.FALSE) {

                @Override
                public boolean isSettable(Notification.CSSBridge cssBridge) {
                    return !cssBridge.notification.wrapTextProperty().isBound();
                }

                @Override
                public StyleableProperty<Boolean> getStyleableProperty(Notification.CSSBridge cssBridge) {
                    return (StyleableProperty<Boolean>) (WritableValue<Boolean>) cssBridge.notification.wrapTextProperty();
                }
            };

    private static final CssMetaData<Notification.CSSBridge, String> GRAPHIC =
            new CssMetaData<Notification.CSSBridge, String>("-fx-graphic",
                    StringConverter.getInstance()) {

                @Override
                public boolean isSettable(Notification.CSSBridge cssBridge) {
                    return !cssBridge.notification.graphicProperty().isBound();
                }

                @Override
                public StyleableProperty<String> getStyleableProperty(Notification.CSSBridge cssBridge) {
                    return (StyleableProperty<String>) cssBridge.notification.imageUrlProperty();
                }
            };

    private static final CssMetaData<Notification.CSSBridge, ContentDisplay> CONTENT_DISPLAY =
            new CssMetaData<Notification.CSSBridge, ContentDisplay>("-fx-content-display",
                    new EnumConverter<ContentDisplay>(ContentDisplay.class),
                    ContentDisplay.LEFT) {

                @Override
                public boolean isSettable(Notification.CSSBridge cssBridge) {
                    return !cssBridge.notification.contentDisplayProperty().isBound();
                }

                @Override
                public StyleableProperty<ContentDisplay> getStyleableProperty(Notification.CSSBridge cssBridge) {
                    return (StyleableProperty<ContentDisplay>) (WritableValue<ContentDisplay>) cssBridge.notification.contentDisplayProperty();
                }
            };

    private static final CssMetaData<Notification.CSSBridge, Number> GRAPHIC_TEXT_GAP =
            new CssMetaData<Notification.CSSBridge, Number>("-fx-graphic-text-gap",
                    SizeConverter.getInstance(), 4.0) {

                @Override
                public boolean isSettable(Notification.CSSBridge cssBridge) {
                    return !cssBridge.notification.graphicTextGapProperty().isBound();
                }

                @Override
                public StyleableProperty<Number> getStyleableProperty(Notification.CSSBridge cssBridge) {
                    return (StyleableProperty<Number>) (WritableValue<Number>) cssBridge.notification.graphicTextGapProperty();
                }
            };

    private static final CssMetaData<Notification.CSSBridge, Duration> SHOW_DURATION =
            new CssMetaData<Notification.CSSBridge, Duration>("-fx-show-duration",
                    DurationConverter.getInstance(), new Duration(5000)) {

                @Override
                public boolean isSettable(Notification.CSSBridge cssBridge) {
                    return !cssBridge.notification.showDurationProperty().isBound();
                }

                @Override
                public StyleableProperty<Duration> getStyleableProperty(Notification.CSSBridge cssBridge) {
                    return (StyleableProperty<Duration>) (WritableValue<Duration>) cssBridge.notification.showDurationProperty();
                }
            };

    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    static {
        final List<CssMetaData<? extends Styleable, ?>> styleables =
                new ArrayList<CssMetaData<? extends Styleable, ?>>(PopupControl.getClassCssMetaData());
        styleables.add(FONT);
        styleables.add(TEXT_ALIGNMENT);
        styleables.add(TEXT_OVERRUN);
        styleables.add(WRAP_TEXT);
        styleables.add(GRAPHIC);
        styleables.add(CONTENT_DISPLAY);
        styleables.add(GRAPHIC_TEXT_GAP);
        styleables.add(SHOW_DURATION);
        STYLEABLES = Collections.unmodifiableList(styleables);
    }


    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    @Override
    public Styleable getStyleableParent() {
        return null;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/
    public void show(Node anchor, Side side, double dx, double dy) {
        if (anchor == null) return;

        getScene().setNodeOrientation(anchor.getEffectiveNodeOrientation());
        HPos hpos = side == Side.LEFT ? HPos.LEFT : side == Side.RIGHT ? HPos.RIGHT : HPos.CENTER;
        VPos vpos = side == Side.TOP ? VPos.TOP : side == Side.BOTTOM ? VPos.BOTTOM : VPos.CENTER;

        // translate from anchor/hpos/vpos/dx/dy into screenX/screenY
        Point2D point = Utils.pointRelativeTo(anchor,
                prefWidth(-1), prefHeight(-1),
                hpos, vpos, dx, dy, true);
        doShow(anchor, point.getX(), point.getY());
    }

    @Override
    public void show(Node anchor, double screenX, double screenY) {
        if (anchor == null) return;
        getScene().setNodeOrientation(anchor.getEffectiveNodeOrientation());
        doShow(anchor, screenX, screenY);
    }

    /***************************************************************************
     *                                                                         *
     * Private Implementation                                                  *
     *                                                                         *
     **************************************************************************/

    private void doShow(Node anchor, double screenX, double screenY) {
        super.show(anchor, screenX, screenY);
        Timeline hideDelay = new Timeline();
        hideDelay.getKeyFrames().setAll(new KeyFrame(getShowDuration()));
        hideDelay.setOnFinished(event -> super.hide());
        hideDelay.playFromStart();
    }

    /***************************************************************************
     *                                                                         *
     * Support classes                                                         *
     *                                                                         *
     **************************************************************************/
    private final class CSSBridge extends PopupControl.CSSBridge {
        private Notification notification = Notification.this;

        CSSBridge() {
            super();
            setAccessibleRole(AccessibleRole.TOOLTIP);
            // todo accessible role?
        }
    }
}
