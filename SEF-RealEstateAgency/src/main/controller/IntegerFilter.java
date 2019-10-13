package main.controller;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class IntegerFilter implements UnaryOperator<TextFormatter.Change> {

    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        String newText = change.getControlNewText();

        if (newText.matches("([1-9][0-9]*)?")) {
            return change;
        }
        return null;

    }
}

