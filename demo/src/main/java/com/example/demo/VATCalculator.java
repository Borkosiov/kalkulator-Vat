package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VATCalculator extends Application {
    private RadioButton nettoBrutto, bruttoNetto, dopasujVat;
    private TextField warPodstawowa, warVat, warNetto, wynik;
    private ComboBox<String> warVatBox;
    private Label resultLabel, nettoLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Kalkulator VAT netto-brutto");

        FlowPane root = new FlowPane();
        root.setPadding(new Insets(10));
        root.setVgap(10);
        root.setHgap(10);
        root.setAlignment(Pos.TOP_LEFT);

        // sposoby liczenia
        Label methodLabel = new Label("Metoda obliczeń:");
        ToggleGroup methodGroup = new ToggleGroup();
        nettoBrutto = new RadioButton("Od netto do brutto");
        nettoBrutto.setToggleGroup(methodGroup);
        bruttoNetto = new RadioButton("Od brutto do netto");
        bruttoNetto.setToggleGroup(methodGroup);
        dopasujVat = new RadioButton("Dopasuj do kwoty VAT");
        dopasujVat.setToggleGroup(methodGroup);
        nettoBrutto.setSelected(true);  // Default selection

        // Add listener to update result label and netto field visibility
        methodGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> updateWybranegoButtona());

        VBox methodBox = new VBox(5, methodLabel, nettoBrutto, bruttoNetto, dopasujVat);
        methodBox.setPadding(new Insets(10));


        Label dataLabel = new Label("Dane:");
        Label baseValueLabel = new Label("Wartość bazowa:");
        warPodstawowa = new TextField("2000.00");

        Label warVatLabel = new Label("Stawka VAT:");
        warVatBox = new ComboBox<>();
        warVatBox.getItems().addAll("23%", "8%", "5%");
        warVatBox.setValue("23%");

        VBox dataBox = new VBox(5, dataLabel, baseValueLabel, warPodstawowa, warVatLabel, warVatBox);
        dataBox.setPadding(new Insets(10));


        Button calculateButton = new Button("OBLICZ");
        Button closeButton = new Button("Zamknij");
        closeButton.setOnAction(e -> primaryStage.close());


        warVat = new TextField();
        warVat.setEditable(false);

        warNetto = new TextField();
        warNetto.setEditable(false);
        warNetto.setVisible(false);
        nettoLabel = new Label("Netto:");
        nettoLabel.setVisible(false);

        wynik = new TextField();
        wynik.setEditable(false);

        resultLabel = new Label("Brutto:");

        VBox resultBox = new VBox(5, new Label("Wyniki:"), nettoLabel, warNetto, new Label("VAT:"), warVat, resultLabel, wynik);
        resultBox.setPadding(new Insets(10));


        calculateButton.setOnAction(e -> calculateVAT());


        root.getChildren().addAll(methodBox, dataBox, calculateButton, closeButton, resultBox);

        Scene scene = new Scene(root, 350, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void calculateVAT() {
        try {
            double baseVal = Double.parseDouble(warPodstawowa.getText());
            double vatRate = Double.parseDouble(warVatBox.getValue().replace("%", "")) / 100;

            if (nettoBrutto.isSelected()) {
                double iloscVat = baseVal * vatRate;
                double bruttoVal = baseVal + iloscVat;
                warVat.setText(String.format("%.2f @ %.0f%%", iloscVat, vatRate * 100));
                wynik.setText(String.format("%.2f", bruttoVal));

            } else if (bruttoNetto.isSelected()) {
                double nettoVal = baseVal / (1 + vatRate);
                double vatAmount = baseVal - nettoVal;
                warVat.setText(String.format("%.2f @ %.0f%%", vatAmount, vatRate * 100));
                wynik.setText(String.format("%.2f", nettoVal));
            } else if (dopasujVat.isSelected()) {

                double nettoValue = baseVal / vatRate;
                double bruttoValue = nettoValue + baseVal;
                warVat.setText(String.format("%.2f @ %.0f%%", baseVal, vatRate * 100));
                warNetto.setText(String.format("%.2f", nettoValue));
                wynik.setText(String.format("%.2f", bruttoValue));
            }
        } catch (NumberFormatException e) {
            warVat.setText("no wpisz coś");
            wynik.setText("no wpisz coś");
        }
    }

    private void updateWybranegoButtona() {
        if (nettoBrutto.isSelected()) {
            resultLabel.setText("Brutto:");
            warNetto.setVisible(false);
            nettoLabel.setVisible(false);
        } else if (bruttoNetto.isSelected()) {
            resultLabel.setText("Netto:");
            warNetto.setVisible(false);
            nettoLabel.setVisible(false);
        } else if (dopasujVat.isSelected()) {
            resultLabel.setText("Brutto:");
            warNetto.setVisible(true);
            nettoLabel.setVisible(true);
        }
    }
}
