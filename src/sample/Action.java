package sample;

import javafx.scene.control.TextField;

public class Action {

    private TextField theTextField;
    private String Value;

    public Action(TextField theTextField, String value) {
        this.theTextField = theTextField;
        Value = value;
    }

    public TextField getTheTextField() {
        return theTextField;
    }

    public void setTheTextField(TextField theTextField) {
        this.theTextField = theTextField;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
