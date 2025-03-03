package com.example.loancalculator.controllers;

import com.example.loancalculator.loans.LoanPayment;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ReportController {
    @FXML
    private AreaChart<Number, Number> chart;

    @FXML
    private NumberAxis xAxis;

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
    private TableView<LoanPayment> table;

    @FXML
    private TextField toMonth;

    @FXML
    private TextField toYear;
}
