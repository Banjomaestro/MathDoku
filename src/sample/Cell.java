package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Cell {

    private int correctValue;
    private int position;
    private boolean inCage;
    private boolean hasLeftBorder = true;
    private boolean hasTopBorder = true;
    private boolean hasBottomBorder = true;
    private boolean hasRightBorder = true;
    private TextField theTextField = new TextField();
    private Label labelAbove = new Label(" ");
    private Label labelUnder = new Label(" ");
    private VBox theVBox = new VBox();

    public Cell(){

        theTextField.setMinHeight(20);
        theTextField.setMaxHeight(10000);
        theTextField.setPrefHeight(200);
        theTextField.setMinWidth(20);
        theTextField.setMaxWidth(10000);
        theTextField.setPrefWidth(350);
        theTextField.setAlignment(Pos.CENTER);

        labelAbove.setMinHeight(15);
        labelAbove.setMaxHeight(30);
        labelAbove.setPrefHeight(25);
        labelAbove.setMinWidth(10);
        labelAbove.setMaxWidth(40);
        labelAbove.setPrefWidth(20);

        labelUnder.setMinHeight(5);
        labelUnder.setMaxHeight(30);
        labelUnder.setPrefHeight(25);
        labelUnder.setMinWidth(10);
        labelUnder.setMaxWidth(40);
        labelUnder.setPrefWidth(20);

        theVBox.getChildren().addAll(labelAbove, theTextField, labelUnder);
        VBox.setVgrow(theTextField, Priority.ALWAYS);
        theVBox.setPadding(new Insets((10)));
        theVBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

    }

    public int getCorrectValue() {
        return correctValue;
    }

    public void setCorrectValue(int correctValue) {
        this.correctValue = correctValue;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isInCage() {
        return inCage;
    }

    public void setInCage(boolean inCage) {
        this.inCage = inCage;
    }

    public boolean isHasLeftBorder() {
        return hasLeftBorder;
    }

    public void setHasLeftBorder(boolean hasLeftBorder) {
        this.hasLeftBorder = hasLeftBorder;
    }

    public boolean isHasTopBorder() {
        return hasTopBorder;
    }

    public void setHasTopBorder(boolean hasTopBorder) {
        this.hasTopBorder = hasTopBorder;
    }

    public boolean isHasBottomBorder() {
        return hasBottomBorder;
    }

    public void setHasBottomBorder(boolean hasBottomBorder) {
        this.hasBottomBorder = hasBottomBorder;
    }

    public boolean isHasRightBorder() {
        return hasRightBorder;
    }

    public void setHasRightBorder(boolean hasRightBorder) {
        this.hasRightBorder = hasRightBorder;
    }


    public BorderStrokeStyle getBorderBySide(boolean hasBorder){

        if(!hasBorder)
            return BorderStrokeStyle.DASHED;
        else
            return BorderStrokeStyle.SOLID;


    }

    public TextField getTheTextField() {
        return theTextField;
    }

    public void setTheTextField(TextField theTextField) {
        this.theTextField = theTextField;
    }

    public Label getLabelAbove() {
        return labelAbove;
    }

    public void setLabelAbove(Label labelAbove) {
        this.labelAbove = labelAbove;
    }

    public Label getLabelUnder() {
        return labelUnder;
    }

    public void setLabelUnder(Label labelUnder) {
        this.labelUnder = labelUnder;
    }

    public VBox getTheVBox() {
        return theVBox;
    }

    public void drawBorders(){
        theVBox.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                getBorderBySide(isHasTopBorder()), getBorderBySide(isHasRightBorder()), getBorderBySide(isHasBottomBorder()),getBorderBySide(isHasLeftBorder()),
                CornerRadii.EMPTY, new BorderWidths(2.25), Insets.EMPTY)));
    }
}
