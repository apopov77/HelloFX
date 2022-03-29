package com.example.hellofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.time.LocalDate;
import java.util.Optional;


public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {

        welcomeText.setText("Welcome to JavaFX Application!");

        Dialog dialog = new Dialog();
        dialog.setTitle("Calendar");

        DialogPane dialogPane = dialog.getDialogPane();

        MultiDatePicker multiDatePicker = new MultiDatePicker();
        multiDatePicker.setValue(LocalDate.now());
        multiDatePicker.withRangeSelectionMode();

        dialogPane.setContent(multiDatePicker.getPopupContent());
        dialogPane.getButtonTypes().add(ButtonType.APPLY);

        Optional result = dialog.showAndWait();
        welcomeText.setText(result.toString());

    }

    public void onDataPickerAction(ActionEvent actionEvent) {
        DatePicker picker = (DatePicker) actionEvent.getSource();
        welcomeText.setText("Data: "+picker.getValue());
    }

    public void onMultiDatePickerAction(ActionEvent actionEvent) {
        MultiDatePicker picker = (MultiDatePicker) actionEvent.getSource();
        welcomeText.setText("Data: "+picker.getSelectedDates());
    }
    public void onMultiDatePickerAction02(ActionEvent actionEvent) {
        MultiDatePicker picker = (MultiDatePicker) actionEvent.getSource();
        welcomeText.setText("Data: "+picker.getSelectedDates());
    }
}