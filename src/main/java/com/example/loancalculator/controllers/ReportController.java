package com.example.loancalculator.controllers;

import com.example.loancalculator.loans.Loan;
import com.example.loancalculator.loans.LoanPayment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ReportController {
    @FXML
    private AreaChart<String, Number> chart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Button delayButton;

    @FXML
    private TextField delayMonths;

    @FXML
    private TextField delayPercentage;

    @FXML
    private TextField delayYears;

    @FXML
    private TextField durationMonths;

    @FXML
    private TextField durationYears;

    @FXML
    private Button filterButton;

    @FXML
    private TextField fromMonth;

    @FXML
    private TextField fromYear;

    @FXML
    private Button newLoanButton;

    @FXML
    private Button saveToFileButton;

    @FXML
    private TableView<LoanPayment> TableView;

    @FXML
    private TextField toMonth;

    @FXML
    private TextField toYear;

    private Loan loan;

    @FXML
    public void initialize() {
        // Set up button handlers
        newLoanButton.setOnAction(event -> handleNewLoanButton(event));
        filterButton.setOnAction(event -> handleFilterButton());
        delayButton.setOnAction(event -> handleDelayButton());
        saveToFileButton.setOnAction(event -> handleSaveToFileButton());

        // Set up TableView columns
        setupTableColumns();
    }

    /**
     * Set up the columns for the loan payment table
     */
    private void setupTableColumns() {
        // Create columns for the table
        TableColumn<LoanPayment, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<LoanPayment, Integer> monthColumn = new TableColumn<>("Month");
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));

        TableColumn<LoanPayment, Double> paymentColumn = new TableColumn<>("Payment");
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("monthlyPayment"));

        TableColumn<LoanPayment, Double> interestColumn = new TableColumn<>("Interest");
        interestColumn.setCellValueFactory(new PropertyValueFactory<>("interest"));

        TableColumn<LoanPayment, Double> creditColumn = new TableColumn<>("Principal");
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("credit"));

        TableColumn<LoanPayment, Double> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("loanBalance"));

        // Clear existing columns and add new ones
        TableView.getColumns().clear();
        TableView.getColumns().addAll(yearColumn, monthColumn, paymentColumn,
                interestColumn, creditColumn, balanceColumn);
    }

    /**
     * Set the loan and update the UI accordingly
     * @param loan The loan to display
     */
    public void setLoan(Loan loan) {
        this.loan = loan;
        updateUI();
    }

    /**
     * Update the UI with the loan data
     */
    private void updateUI() {
        if (loan == null) {
            return;
        }

        List<LoanPayment> payments = loan.GetMonthlyPayments();

        // Update table with payments
        ObservableList<LoanPayment> tableData = FXCollections.observableArrayList(payments);
        TableView.setItems(tableData);

        // Create chart series for payment data
        XYChart.Series<String, Number> paymentSeries = new XYChart.Series<>();
        paymentSeries.setName("Monthly Payment");

        XYChart.Series<String, Number> interestSeries = new XYChart.Series<>();
        interestSeries.setName("Interest");

        XYChart.Series<String, Number> principalSeries = new XYChart.Series<>();
        principalSeries.setName("Principal");

        // Add data points to each series
        for (LoanPayment payment : payments) {
            String period = payment.getYear() + "y " + payment.getMonth() + "m";
            paymentSeries.getData().add(new XYChart.Data<>(period, payment.getMonthlyPayment()));
            interestSeries.getData().add(new XYChart.Data<>(period, payment.getInterest()));
            principalSeries.getData().add(new XYChart.Data<>(period, payment.getCredit()));
        }

        // Update chart
        chart.getData().clear();
        chart.getData().addAll(paymentSeries, interestSeries, principalSeries);
    }

    /**
     * Handle the new loan button click
     */
    private void handleNewLoanButton(javafx.event.ActionEvent event) {
        try {
            // Load the main loan calculator view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/loancalculator/Main.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.setTitle("Loan Calculator");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the filter button click
     */
    private void handleFilterButton() {
        // Get filter values
        int fromYear = getIntFromTextField(this.fromYear, 0);
        int fromMonth = getIntFromTextField(this.fromMonth, 0);
        int toYear = getIntFromTextField(this.toYear, Integer.MAX_VALUE);
        int toMonth = getIntFromTextField(this.toMonth, Integer.MAX_VALUE);

        // Filter payments
        if (loan != null) {
            List<LoanPayment> allPayments = loan.GetMonthlyPayments();
            ObservableList<LoanPayment> filteredPayments = FXCollections.observableArrayList();

            for (LoanPayment payment : allPayments) {
                if (isPaymentInRange(payment, fromYear, fromMonth, toYear, toMonth)) {
                    filteredPayments.add(payment);
                }
            }

            // Update table with filtered data
            TableView.setItems(filteredPayments);
        }
    }

    /**
     * Check if a payment is within the specified range
     */
    private boolean isPaymentInRange(LoanPayment payment, int fromYear, int fromMonth,
                                     int toYear, int toMonth) {
        int paymentTotal = payment.getYear() * 12 + payment.getMonth();
        int fromTotal = fromYear * 12 + fromMonth;
        int toTotal = toYear * 12 + toMonth;

        return paymentTotal >= fromTotal && paymentTotal <= toTotal;
    }

    /**
     * Handle the delay button click
     */
    private void handleDelayButton() {
        // Implement loan delay functionality here
        // This would typically create a modified loan with a payment delay period
    }

    /**
     * Handle the save to file button click
     */
    private void handleSaveToFileButton() {
        // Implement saving loan data to file
    }

    /**
     * Utility method to parse integer from text field with a default value
     */
    private int getIntFromTextField(TextField field, int defaultValue) {
        try {
            if (field.getText() != null && !field.getText().isEmpty()) {
                return Integer.parseInt(field.getText());
            }
        } catch (NumberFormatException e) {
            // Ignore parse errors and use default
        }
        return defaultValue;
    }
}