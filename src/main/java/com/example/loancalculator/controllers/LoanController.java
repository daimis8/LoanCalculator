package com.example.loancalculator.controllers;

import com.example.loancalculator.loans.AnnuityLoan;
import com.example.loancalculator.loans.LinearLoan;
import com.example.loancalculator.loans.Loan;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.stage.Stage;


public class LoanController {
    @FXML
    private Label yearsAndMonthsError;

    @FXML
    private ChoiceBox<String> graphChoice;

    @FXML
    private TextField interestRate;

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

    @FXML
    public void initialize() {
        submitButton.setOnAction(this::handleSubmitButtonAction);

        graphChoice.setItems(FXCollections.observableArrayList("Annuity", "Linear"));
        graphChoice.setValue("Annuity");
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event){
        Optional<Double> loanAmount = Optional.empty();
        try {
            loanAmountError.setText("");
            if (this.loanAmount.getText().isBlank()) {
                throw new NullPointerException();
            }

            loanAmount = Optional.of(Double.parseDouble(this.loanAmount.getText()));

            if (loanAmount.get() < 0) {
                loanAmountError.setText("Loan amount has to be positive");
                loanAmount = Optional.empty();
            }
        } catch (NullPointerException e) {
            loanAmountError.setText("Loan amount cannot be blank.");
        } catch (NumberFormatException e) {
            loanAmountError.setText("Loan amount has to be a number.");
        }

        Optional<Integer> years = Optional.empty();
        try {
            yearsError.setText("");
            if (this.years.getText().isBlank()) {
                throw new NullPointerException();
            }
            years = Optional.of(Integer.parseInt(this.years.getText()));
            if (years.get() < 0) {
                yearsError.setText("Years has to be positive");
                years = Optional.empty();
            }
        } catch (NumberFormatException e) {
            yearsError.setText("Years has to be a number.");
        } catch (NullPointerException e) {
            if (this.years.getText().isBlank() || years.get() == 0 && !this.months.getText().isBlank()) {
                years = Optional.of(0);
            }
            else {
                yearsError.setText("Years can't be empty.");
            }
        }

        Optional<Integer> months = Optional.empty();
        try {
            monthsError.setText("");
            if (this.months.getText().isBlank()) {
                throw new NullPointerException();
            }
            else {
                months = Optional.of(Integer.parseInt(this.months.getText()));
                if (months.get() < 0) {
                    monthsError.setText("Months has to be positive");
                    months = Optional.empty();
                }
            }
        } catch (NumberFormatException e) {
            monthsError.setText("Months has to be a number.");
        } catch (NullPointerException e) {
            if (this.months.getText().isBlank() || months.get() == 0 && !this.years.getText().isBlank()) {
                months = Optional.of(0);
            }
            else {
                monthsError.setText("Months can't be empty.");
            }

        }



        Optional<Double> interestRate = Optional.empty();
        try {
            interestRateError.setText("");
            if (this.interestRate.getText().isBlank()) {
                throw new NullPointerException();
            }
            interestRate = Optional.of(Double.parseDouble(this.interestRate.getText()));
            if (interestRate.get() < 0) {
                interestRateError.setText("Interest rate has to be positive");
                interestRate = Optional.empty();
            }
        } catch (NullPointerException e) {
            interestRateError.setText("Interest rate cannot be empty.");
        } catch (NumberFormatException e) {
            interestRateError.setText("Interest rate has to be a number.");
        }

        if (loanAmount.isPresent() && years.isPresent() && months.isPresent() && interestRate.isPresent()) {
            try {
                Loan loan = switch (graphChoice.getValue()) {
                    case "Annuity" -> new AnnuityLoan(loanAmount.get(), months.get(), years.get(), interestRate.get());
                    case "Linear" -> new LinearLoan(loanAmount.get(), months.get(), years.get(), interestRate.get());
                    default -> new AnnuityLoan(loanAmount.get(), months.get(), years.get(), interestRate.get());
                };

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/loancalculator/Report.fxml"));
                Parent reportRoot = fxmlLoader.load();

                ReportController reportController = fxmlLoader.getController();
                reportController.setLoan(loan);

                Scene reportScene = new Scene(reportRoot);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setScene(reportScene);
                stage.setTitle("Loan Report");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Loading Report");
                alert.setHeaderText(null);
                alert.setContentText("Could not load the report view: " + e.getMessage());
                alert.showAndWait();
            }
        }

    }

}
