package com.raunits.prettier;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
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
    private static boolean textareaVisible = true;

    @Override
    public void start(Stage stage) throws IOException {
        uiSetup();
        addFormatBtn();

        Scene scene = new Scene(window);
        stage.setTitle("Prettier");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        try {
            FileWriter fileWriter = new FileWriter(".noto");
            String markdown = textArea.getText();
            for (char c : markdown.toCharArray()) {
                fileWriter.write(c);
            }
            fileWriter.close();
            System.out.println("saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uiSetup() {
        String placeholder = "Enter XML here";
        textAreaContainer.getChildren().add(textArea);
        textAreaContainer.setFillWidth(true);

        displayAreaContainer.getChildren().add(displayArea);
        displayAreaContainer.setFillWidth(true);

        VBox.setVgrow(textArea, Priority.ALWAYS);
        VBox.setVgrow(displayArea, Priority.ALWAYS);

        textArea.setWrapText(true);
        textArea.setFont(Font.font("JetBrains Mono"));

        displayArea.setWrapText(true);
        displayArea.setFont(Font.font("JetBrains Mono"));

        responsivize(50, 50);

        textArea.setText(placeholder);
        displayArea.setText("Formatted code will be displayed here");

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

    public void addFormatBtn() {
        Button showEditorBtn = new Button("format");
        HBox buttonPane = new HBox(12, showEditorBtn);
        buttonPane.setAlignment(Pos.CENTER_LEFT);
        textAreaContainer.getChildren().add(buttonPane);

        showEditorBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String code = textArea.getText();
                String prettyCode = formatter.format(code);
                displayArea.setText(prettyCode);
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}