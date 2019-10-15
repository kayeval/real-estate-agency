package main.model;

import javafx.scene.control.TextFormatter;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;

//SOURCE: https://stackoverflow.com/a/31043122

public class DecimalFilter implements UnaryOperator<TextFormatter.Change> {
    private DecimalFormat format = new DecimalFormat("#.0");

    @Override
    public TextFormatter.Change apply(TextFormatter.Change c) {
        if (c.getControlNewText().isEmpty()) {
            return c;
        }

        ParsePosition parsePosition = new ParsePosition(0);
        Object object = format.parse(c.getControlNewText(), parsePosition);

        if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
            return null;
        } else {
            return c;
        }
    }
}
