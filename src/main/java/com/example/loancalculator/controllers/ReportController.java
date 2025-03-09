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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.ArrayList;

import java.io.IOException;
import java.util.List;

public class ReportController {
    @FXML
    public Label fromYearsError;
    @FXML
    public Label fromMonthsError;
    @FXML
    public Label toYearsError;
    @FXML
    public Label toMonthsError;
    @FXML
    public Label delayYearsError;
    @FXML
    public Label delayMonthsError;
    @FXML
    public Label durYearsError;
    @FXML
    public Label durMonthsError;
    @FXML
    public Label delPercentage;
    @FXML
    public Label saveToFileError;
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
    private TableView<LoanPayment> tableView;

    @FXML
    private TextField toMonth;

    @FXML
    private TextField toYear;

    private Loan loan;

    @FXML
    public void initialize() {
        newLoanButton.setOnAction(event -> handleNewLoanButton(event));
        filterButton.setOnAction(event -> handleFilterButton());
        delayButton.setOnAction(event -> handleDelayButton());
        saveToFileButton.setOnAction(event -> handleSaveToFileButton());

        setupTableColumns();

    }

    private void setupTableColumns() {
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

        tableView.getColumns().clear();
        tableView.getColumns().addAll(yearColumn, monthColumn, paymentColumn, interestColumn, creditColumn, balanceColumn);
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
        updateUI();
    }


    private void updateUI() {
        if (loan == null) {
            return;
        }

        List<LoanPayment> payments = loan.GetMonthlyPayments();

        ObservableList<LoanPayment> tableData = FXCollections.observableArrayList(payments);
        tableView.setItems(tableData);

        XYChart.Series<String, Number> paymentSeries = new XYChart.Series<>();
        paymentSeries.setName("Monthly Payment");

        XYChart.Series<String, Number> interestSeries = new XYChart.Series<>();
        interestSeries.setName("Interest");

        XYChart.Series<String, Number> principalSeries = new XYChart.Series<>();
        principalSeries.setName("Principal");

        int monthCounter = 1;

        for (LoanPayment payment : payments) {
            String label = monthCounter + "";
            paymentSeries.getData().add(new XYChart.Data<>(label, payment.getMonthlyPayment()));
            interestSeries.getData().add(new XYChart.Data<>(label, payment.getInterest()));
            principalSeries.getData().add(new XYChart.Data<>(label, payment.getCredit()));

            monthCounter++;
        }

        chart.getData().clear();
        chart.getData().addAll(paymentSeries, interestSeries, principalSeries);
    }



    private void handleNewLoanButton(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/loancalculator/Main.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Loan Calculator");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleFilterButton() {
        fromYearsError.setText("");
        fromMonthsError.setText("");
        toYearsError.setText("");
        toMonthsError.setText("");

        int fromYear = 0;
        try {
            if (this.fromYear.getText() != null && !this.fromYear.getText().isBlank()) {
                fromYear = Integer.parseInt(this.fromYear.getText());
                if (fromYear < 0) {
                    fromYearsError.setText("Year must be positive");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            fromYearsError.setText("Year must be a number");
            return;
        }

        int fromMonth = 1;
        try {
            if (this.fromMonth.getText() != null && !this.fromMonth.getText().isBlank()) {
                fromMonth = Integer.parseInt(this.fromMonth.getText());
                if (fromMonth < 1 || fromMonth > 12) {
                    fromMonthsError.setText("Month must in a an interval between 1 and 12");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            fromMonthsError.setText("Month must be a number");
            return;
        }

        int toYear = Integer.MAX_VALUE;
        try {
            if (this.toYear.getText() != null && !this.toYear.getText().isBlank()) {
                toYear = Integer.parseInt(this.toYear.getText());
                if (toYear < 0) {
                    toYearsError.setText("Year must be positive");
                    return;
                }
                if (toYear < fromYear) {
                    toYearsError.setText("To year must be higher than from year");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            toYearsError.setText("Year must be a number");
            return;
        }

        int toMonth = 12;
        try {
            if (this.toMonth.getText() != null && !this.toMonth.getText().isBlank()) {
                toMonth = Integer.parseInt(this.toMonth.getText());
                if (toMonth < 1 || toMonth > 12) {
                    toMonthsError.setText("Month must in a an interval between 1 and 12");
                    return;
                }
            }

            if (fromYear == toYear && toMonth < fromMonth) {
                toMonthsError.setText("To month must be higher than from month when years are equal");
                return;
            }
        } catch (NumberFormatException e) {
            toMonthsError.setText("Month must be a number");
            return;
        }

        if (loan != null) {
            List<LoanPayment> allPayments = loan.GetMonthlyPayments();
            ObservableList<LoanPayment> filteredPayments = FXCollections.observableArrayList();

            for (LoanPayment payment : allPayments) {
                if (isPaymentInRange(payment, fromYear, fromMonth, toYear, toMonth)) {
                    filteredPayments.add(payment);
                }
            }
            tableView.setItems(filteredPayments);
        }
    }


    private boolean isPaymentInRange(LoanPayment payment, int fromYear, int fromMonth, int toYear, int toMonth) {
        int paymentTotal = payment.getYear() * 12 + payment.getMonth();
        int fromTotal = fromYear * 12 + fromMonth;
        int toTotal = toYear * 12 + toMonth;

        return paymentTotal >= fromTotal && paymentTotal <= toTotal;
    }

    private void handleDelayButton() {
        delayYearsError.setText("");
        delayMonthsError.setText("");
        durYearsError.setText("");
        durMonthsError.setText("");
        delPercentage.setText("");

        int delayYear = 0;
        try {
            if (this.delayYears.getText() != null && !this.delayYears.getText().isBlank()) {
                delayYear = Integer.parseInt(this.delayYears.getText());
                if (delayYear < 0) {
                    delayYearsError.setText("Delay year must be positive");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            delayYearsError.setText("Delay year must be a number");
            return;
        }

        int delayMonth = 0;
        try {
            if (this.delayMonths.getText() != null && !this.delayMonths.getText().isBlank()) {
                delayMonth = Integer.parseInt(this.delayMonths.getText());
                if (delayMonth < 0 || delayMonth > 11) {
                    delayMonthsError.setText("Delay month must be 0-11");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            delayMonthsError.setText("Delay month must be a number");
            return;
        }

        if (delayYear == 0 && delayMonth == 0) {
            delayYearsError.setText("Please enter a delay period");
            return;
        }

        int durationYear = 0;
        try {
            if (this.durationYears.getText() != null && !this.durationYears.getText().isBlank()) {
                durationYear = Integer.parseInt(this.durationYears.getText());
                if (durationYear < 0) {
                    durYearsError.setText("Duration year must be positive");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            durYearsError.setText("Duration year must be a number");
            return;
        }

        int durationMonth = 0;
        try {
            if (this.durationMonths.getText() != null && !this.durationMonths.getText().isBlank()) {
                durationMonth = Integer.parseInt(this.durationMonths.getText());
                if (durationMonth < 0 || durationMonth > 12) {
                    durMonthsError.setText("Duration month must be in an interval between 1 and 12");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            durMonthsError.setText("Duration month must be a number");
            return;
        }

        if (durationYear == 0 && durationMonth == 0) {
            durYearsError.setText("Please enter a duration period");
            return;
        }
        if(durationYear > loan.getYears() || durationMonth > loan.getMonths()) {
            durYearsError.setText("Duration exceeds your loan term");
            return;
        }

        double percentage = 0.0;
        try {
            if (this.delayPercentage.getText() != null && !this.delayPercentage.getText().isBlank()) {
                percentage = Double.parseDouble(this.delayPercentage.getText());
                if (percentage < 0) {
                    delPercentage.setText("Percentage must be positive");
                    return;
                }
            } else {
                delPercentage.setText("Percentage is required");
                return;
            }
        } catch (NumberFormatException e) {
            delPercentage.setText("Percentage must be a number");
            return;
        }

        List<LoanPayment> originalPayments = loan.GetMonthlyPayments();
        List<LoanPayment> modifiedPayments = new ArrayList<>();

        int delayStartMonth = delayYear * 12 + delayMonth;
        int delayDurationMonths = durationYear * 12 + durationMonth;
        int delayEndMonth = delayStartMonth + delayDurationMonths;

        for (LoanPayment payment : originalPayments) {
            modifiedPayments.add(new LoanPayment(payment));
        }

        double totalBalance = 0.0;
        boolean isDelayActive = false;
        double additionalInterest = 0.0;

        for (int i = 0; i < modifiedPayments.size(); i++) {
            LoanPayment payment = modifiedPayments.get(i);
            int paymentMonth = payment.getYear() * 12 + payment.getMonth();

            if (paymentMonth >= delayStartMonth && paymentMonth < delayEndMonth) {
                additionalInterest += payment.getCredit() * (percentage / 100.0);
                totalBalance += payment.getCredit();
            }
        }

        double remainingBalance = 0.0;

        for (int i = 0; i < modifiedPayments.size(); i++) {
            LoanPayment payment = modifiedPayments.get(i);
            int paymentMonth = payment.getYear() * 12 + payment.getMonth();

            if (paymentMonth >= delayStartMonth && paymentMonth < delayEndMonth) {
                double oldPrincipal = payment.getCredit();
                double oldInterest = payment.getInterest();

                // Only pay interest, no principal
                payment.setCredit(0.0);
                payment.setMonthlyPayment(oldInterest);

                isDelayActive = true;

                remainingBalance = payment.getLoanBalance() + oldPrincipal;
                payment.setLoanBalance(remainingBalance);
            }
            else if (paymentMonth >= delayEndMonth && isDelayActive) {
                //Remaining
                int remainingMonths = modifiedPayments.size() - i;

                // Penalty
                double delayPenalty = totalBalance * (percentage / 100.0);
                double newTotalBalance = remainingBalance + delayPenalty;

                double additionalMonthlyAmount = newTotalBalance / remainingMonths;

                for (int j = i; j < modifiedPayments.size(); j++) {
                    LoanPayment remainingPayment = modifiedPayments.get(j);

                    double oldPayment = remainingPayment.getMonthlyPayment();
                    double newPayment = oldPayment + additionalMonthlyAmount;
                    remainingPayment.setMonthlyPayment(newPayment);

                    double oldPrincipal = remainingPayment.getCredit();
                    remainingPayment.setCredit(oldPrincipal + additionalMonthlyAmount);

                    if (j == i) {
                        remainingPayment.setLoanBalance(newTotalBalance - additionalMonthlyAmount);
                    } else {
                        LoanPayment previousPayment = modifiedPayments.get(j-1);
                        remainingPayment.setLoanBalance(previousPayment.getLoanBalance() - remainingPayment.getCredit());
                    }
                }

                isDelayActive = false;
                break;
            }
        }

        ObservableList<LoanPayment> tableData = FXCollections.observableArrayList(modifiedPayments);
        tableView.setItems(tableData);

        updateChart(modifiedPayments);

    }

    private void handleSaveToFileButton() {
        saveToFileError.setText("");

        if (tableView.getItems() == null || tableView.getItems().isEmpty()) {
            saveToFileError.setText("No data to save");
            return;
        }

        try {
            List<LoanPayment> payments = new ArrayList<>(tableView.getItems());

            java.io.File file = new java.io.File("loan.csv");
            java.io.PrintWriter writer = new java.io.PrintWriter(file);


            writer.println("# ====== LOAN SUMMARY ======");
            if (loan != null) {
                writer.println("# Loan Amount:      " + String.format("%,.2f", loan.getLoanAmount()));
                writer.println("# Interest Rate:    " + String.format("%.2f%%", loan.getInterestRate()));
                writer.println("# Loan Term:        " + loan.getYears() + " years, " + loan.getMonths() + " months");

                double totalInterest = payments.stream().mapToDouble(LoanPayment::getInterest).sum();
                writer.println("# Total Interest:   " + String.format("%,.2f", totalInterest));

                double totalPayments = payments.stream().mapToDouble(LoanPayment::getMonthlyPayment).sum();
                writer.println("# Total Amount Paid:" + String.format("%,.2f", totalPayments));
            }
            writer.println();

            writer.println("# ====== DETAILED PAYMENT SCHEDULE ======");
            writer.println("Payment_No,Year,Month,Payment,Interest,Principal,Remaining_Balance");

            int paymentNumber = 1;
            for (LoanPayment payment : payments) {
                writer.printf("%10d,%4d,%5d,%10.2f,%10.2f,%10.2f,%15.2f%n",
                        paymentNumber++,
                        payment.getYear(),
                        payment.getMonth(),
                        payment.getMonthlyPayment(),
                        payment.getInterest(),
                        payment.getCredit(),
                        payment.getLoanBalance());
            }
            writer.println();

            writer.close();

            saveToFileError.setText("Saved!");
        } catch (java.io.IOException e) {
            e.printStackTrace();
            saveToFileError.setText("Error saving file: " + e.getMessage());
        }
    }

    private void updateChart(List<LoanPayment> payments) {
        XYChart.Series<String, Number> paymentSeries = new XYChart.Series<>();
        paymentSeries.setName("Monthly Payment");

        XYChart.Series<String, Number> interestSeries = new XYChart.Series<>();
        interestSeries.setName("Interest");

        XYChart.Series<String, Number> principalSeries = new XYChart.Series<>();
        principalSeries.setName("Principal");

        int monthCounter = 1;

        for (LoanPayment payment : payments) {
            String label = monthCounter + "";
            paymentSeries.getData().add(new XYChart.Data<>(label, payment.getMonthlyPayment()));
            interestSeries.getData().add(new XYChart.Data<>(label, payment.getInterest()));
            principalSeries.getData().add(new XYChart.Data<>(label, payment.getCredit()));

            monthCounter++;
        }

        chart.getData().clear();
        chart.getData().addAll(paymentSeries, interestSeries, principalSeries);
        
        // Apply CSS to hide the symbols (circles)
        String noSymbolsCSS = 
            ".chart-area-symbol { " +
            "    -fx-background-color: transparent, transparent; " +
            "    -fx-background-insets: 0, 0; " +
            "    -fx-background-radius: 0px; " +
            "    -fx-padding: 0px; " +
            "}";
        
        chart.getStylesheets().clear();
        chart.setStyle(noSymbolsCSS);
        
        // Alternative approach: apply styling to each series node after they've been added
        for (XYChart.Series<String, Number> series : chart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setVisible(false);
                }
            }
        }
    }
}