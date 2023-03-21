package com.raunits.prettier;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Prettier extends Application {
    private static final GridPane window = new GridPane();
    private static final TextArea textArea = new TextArea();
    private static final VBox textAreaContainer = new VBox();
    private final TextArea displayArea = new TextArea();
    private final Formatter formatter = new Formatter();
    public static final VBox displayAreaContainer = new VBox();
    private static Text status = new Text("Valid");
    private static boolean textareaVisible = true;

    @Override
    public void start(Stage stage) throws IOException {
        uiSetup();
        Scene scene = new Scene(window);
        stage.setTitle("Prettier");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
//        try {
//            FileWriter fileWriter = new FileWriter(".noto");
//            String markdown = textArea.getText();
//            for (char c : markdown.toCharArray()) {
//                fileWriter.write(c);
//            }
//            fileWriter.close();
//            System.out.println("saved successfully");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void uiSetup() {
        String placeholder = "Enter XML here";
        textAreaContainer.getChildren().add(textArea);
        textAreaContainer.setFillWidth(true);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        textArea.setWrapText(true);
        textArea.setFont(Font.font("JetBrains Mono"));
        textArea.setText(placeholder);
//        addEventListener(textArea);

        displayAreaContainer.getChildren().add(displayArea);
        displayAreaContainer.setFillWidth(true);
        VBox.setVgrow(displayArea, Priority.ALWAYS);
        displayArea.setWrapText(true);
        displayArea.setFont(Font.font("JetBrains Mono"));
        displayArea.setText("Formatted code will be displayed here");

        responsivize(50, 50);

        bottomBar();

        window.add(textAreaContainer, 0, 0);
        window.add(displayAreaContainer, 1, 0);
    }

    public void responsivize(int textareaWidth, int displayareaWidth) {
        ColumnConstraints textAreaConstraints = new ColumnConstraints();
        textAreaConstraints.setPercentWidth(textareaWidth);
        ColumnConstraints displayAreaConstraints = new ColumnConstraints();
        displayAreaConstraints.setPercentWidth(displayareaWidth);
        window.getColumnConstraints().addAll(textAreaConstraints, displayAreaConstraints);

        // for responsive height
        GridPane.setVgrow(textAreaContainer, Priority.ALWAYS);
    }

    public void addEventListener(TextArea textArea) {
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String code = textArea.getText();
                if (formatter.valid(code)) {
                    status.setText("Valid");
                    status.setFill(Color.GREEN);
                }
                else {
                    status.setText("Invalid");
                    status.setFill(Color.RED);
                }
            }
        });
    }

    public void bottomBar() {
        HBox bottomBar = new HBox(12);
        bottomBar.setAlignment(Pos.CENTER);
        addFormatBtn(bottomBar);
        addStatusIndicator(bottomBar);
        textAreaContainer.getChildren().add(bottomBar);
    }

    public void addFormatBtn(HBox bar) {
        Button showEditorBtn = new Button("format");
        bar.getChildren().add(showEditorBtn);

        showEditorBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String code = textArea.getText();
                String prettyCode = formatter.format(code);
                displayArea.setText(prettyCode);
            }
        });
    }

    public void addStatusIndicator(HBox bar) {
        bar.getChildren().add(status);
    }

    public static void main(String[] args) {
        launch();
    }
}
