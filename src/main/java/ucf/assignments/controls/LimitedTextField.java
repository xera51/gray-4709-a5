/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.controls;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;

public class LimitedTextField extends TextField {

    IntegerProperty beginIndex = new SimpleIntegerProperty(this, "beginIndex");
    IntegerProperty endIndex = new SimpleIntegerProperty(this, "endIndex");

    public LimitedTextField() {
        this(32);
    }

    public LimitedTextField(int endIndex) {
        this(0, endIndex, "");
    }

    public LimitedTextField(int beginIndex, int endIndex) {
        this(beginIndex, endIndex, "");
    }

    public LimitedTextField(int endIndex, String text) {
        this(0, endIndex, text);
    }

    public LimitedTextField(int beginIndex, int endIndex, String text) {
        super(text);
        if (validate(beginIndex, endIndex)) {
            this.beginIndex.set(beginIndex);
            this.endIndex.set(endIndex);
        }
    }

    public int getBeginIndex() {
        return beginIndex.get();
    }

    public void setBeginIndex(int beginIndex) {
        if (validate(beginIndex, this.getEndIndex())) {
            this.beginIndex.set(beginIndex);
        }
    }

    public IntegerProperty beginIndexProperty() {
        return beginIndex;
    }

    public int getEndIndex() {
        return endIndex.get();
    }

    public void setEndIndex(int endIndex) {
        if (validate(this.getBeginIndex(), endIndex)) {
            this.endIndex.set(endIndex);
        }
    }

    public IntegerProperty endIndexProperty() {
        return endIndex;
    }

    private boolean validate(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IllegalArgumentException("beginning index must be greater the or equal to 0");
        } else if (beginIndex > endIndex) {
            throw new IllegalArgumentException("beginning index must be less than or equal to end index");
        } else {
            return true;
        }
    }

    @Override
    public void replaceText(int start, int end, String text) {
        String oldText = this.getText() == null ? "" : this.getText();
        String newText = oldText.substring(0, start) + text + oldText.substring(end);

        // If the oldText length is greater than the endIndex, then resize
        if (oldText.length() - 1 > this.getEndIndex()) {
            this.setEndIndex(oldText.length() - 1);
        }

        int num_under_min_length = this.getBeginIndex() - newText.length();
        int num_over_max_len = newText.length() - this.getEndIndex();

        if (num_over_max_len <= 0 && num_under_min_length <= 0) {
            super.replaceText(start, end, text);
        } else if (num_over_max_len > 0) {
            super.replaceText(start, end, text.substring(0, text.length() - num_over_max_len));
        } else if (oldText.length() < this.getBeginIndex()) {
            super.replaceText(start, end, text);
        }
    }
}
