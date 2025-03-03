package com.example.loancalculator.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class LoanController {

    @FXML
    private ChoiceBox<String> graphChoice;

    @FXML
    private TextField insertPercentage;

    @FXML
    private Label interestRateError;

    @FXML
    private TextField loanAmount;

    @FXML
    private Label loanAmountError;

    @FXML
    private TextField months;

    @FXML
    private Label monthsError;

    @FXML
    private Button submitButton;

    @FXML
    private TextField years;

    @FXML
    private Label yearsError;

//    @FXML
//    protected void handleSubmitButtonAction(ActionEvent event) throws IOException {
//        try {
//
//        }
//
//    }

}
