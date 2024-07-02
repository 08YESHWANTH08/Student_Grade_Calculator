package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    private GridPane grid;
    private Label[] subjectLabels;
    private TextField[] subjectInputs;
    private Button calcButton;
    private Button resetButton;
    private Label totalMarksLabel;
    private Label totalMarksResult;
    private Label avgPercentageLabel;
    private Label avgPercentageResult;
    private Label gradeLabel;
    private Label gradeResult;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Grade Calculator");

        grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(15);
        grid.setHgap(10);
        grid.getStyleClass().add("grid-pane");  // Adding CSS class to grid

        // Number of subjects input
        Label numSubjectsLabel = new Label("Number of Subjects:");
        GridPane.setConstraints(numSubjectsLabel, 0, 0);
        numSubjectsLabel.getStyleClass().add("label");  // Adding CSS class to label

        TextField numSubjectsInput = new TextField();
        GridPane.setConstraints(numSubjectsInput, 1, 0);
        numSubjectsInput.getStyleClass().add("text-field");  // Adding CSS class to text field

        // Submit button
        Button submitButton = new Button("Submit");
        GridPane.setConstraints(submitButton, 2, 0);
        submitButton.getStyleClass().add("button");  // Adding CSS class to button
        submitButton.setDefaultButton(true);  // Set submit button as default

        grid.getChildren().addAll(numSubjectsLabel, numSubjectsInput, submitButton);

        Scene scene = new Scene(grid, 450, 500);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());  // Adding CSS file to the scene
        primaryStage.setScene(scene);
        primaryStage.show();

        // Action for submit button
        submitButton.setOnAction(e -> handleSubmission(numSubjectsInput));

        // Set focus on the numSubjectsInput field
        numSubjectsInput.requestFocus();

        // Set key pressed action for numSubjectsInput to submit on Enter key
        numSubjectsInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSubmission(numSubjectsInput);
            }
        });
    }

    private void handleSubmission(TextField numSubjectsInput) {
        String input = numSubjectsInput.getText();
        if (input.isEmpty() || Integer.parseInt(input) <= 0) {
            showAlert("Invalid Input", "Number of subjects cannot be zero or empty.");
            return;
        }

        int numSubjects = Integer.parseInt(input);

        // Clear existing subject fields (if any)
        clearSubjectFields();

        // Create new subject input fields based on user input
        createSubjectFields(numSubjects);

        // Create Calculate button after subject fields
        calcButton = new Button("Calculate");
        GridPane.setConstraints(calcButton, 1, numSubjects + 1);
        calcButton.getStyleClass().add("button");  // Adding CSS class to button
        grid.getChildren().add(calcButton);
        calcButton.setDefaultButton(true);  // Set calculate button as default

        // Create Reset button
        resetButton = new Button("Reset");
        GridPane.setConstraints(resetButton, 2, numSubjects + 1);
        resetButton.getStyleClass().add("button");  // Adding CSS class to button
        grid.getChildren().add(resetButton);

        // Result labels
        totalMarksLabel = new Label("Total Marks:");
        GridPane.setConstraints(totalMarksLabel, 0, numSubjects + 2);
        totalMarksLabel.getStyleClass().add("result-label");  // Adding CSS class to label
        totalMarksResult = new Label();
        GridPane.setConstraints(totalMarksResult, 1, numSubjects + 2);
        totalMarksResult.getStyleClass().add("result-label");  // Adding CSS class to label

        avgPercentageLabel = new Label("Average Percentage:");
        GridPane.setConstraints(avgPercentageLabel, 0, numSubjects + 3);
        avgPercentageLabel.getStyleClass().add("result-label");  // Adding CSS class to label
        avgPercentageResult = new Label();
        GridPane.setConstraints(avgPercentageResult, 1, numSubjects + 3);
        avgPercentageResult.getStyleClass().add("result-label");  // Adding CSS class to label

        gradeLabel = new Label("Grade:");
        GridPane.setConstraints(gradeLabel, 0, numSubjects + 4);
        gradeLabel.getStyleClass().add("result-label");  // Adding CSS class to label
        gradeResult = new Label();
        GridPane.setConstraints(gradeResult, 1, numSubjects + 4);
        gradeResult.getStyleClass().add("result-label");  // Adding CSS class to label

        grid.getChildren().addAll(totalMarksLabel, totalMarksResult,
                avgPercentageLabel, avgPercentageResult,
                gradeLabel, gradeResult);

        // Calculate button action
        calcButton.setOnAction(ev -> calculateResults(numSubjects));

        // Reset button action
        resetButton.setOnAction(ev -> resetApplication(numSubjectsInput));
    }

    private void createSubjectFields(int numSubjects) {
        subjectLabels = new Label[numSubjects];
        subjectInputs = new TextField[numSubjects];

        for (int i = 0; i < numSubjects; i++) {
            subjectLabels[i] = new Label("Subject " + (i + 1) + " Marks:");
            GridPane.setConstraints(subjectLabels[i], 0, i + 1);
            subjectLabels[i].getStyleClass().add("label");  // Adding CSS class to label
            subjectInputs[i] = new TextField();
            GridPane.setConstraints(subjectInputs[i], 1, i + 1);
            subjectInputs[i].getStyleClass().add("text-field");  // Adding CSS class to text field

            int index = i;
            if (index < numSubjects - 1) {
                // Add key event handler to move focus to next TextField on Enter key
                subjectInputs[i].setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        subjectInputs[index + 1].requestFocus();
                    }
                });
            } else {
                // Add key event handler to calculate results on Enter key for the last TextField
                subjectInputs[i].setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        calculateResults(numSubjects);
                    }
                });
            }

            grid.getChildren().addAll(subjectLabels[i], subjectInputs[i]);
        }
    }

    private void clearSubjectFields() {
        if (subjectLabels != null && subjectInputs != null) {
            for (int i = 0; i < subjectLabels.length; i++) {
                grid.getChildren().removeAll(subjectLabels[i], subjectInputs[i]);
            }
        }
        if (calcButton != null) {
            grid.getChildren().remove(calcButton);
        }
        if (resetButton != null) {
            grid.getChildren().remove(resetButton);
        }
        if (totalMarksResult != null) {
            grid.getChildren().removeAll(totalMarksLabel, totalMarksResult,
                                         avgPercentageLabel, avgPercentageResult,
                                         gradeLabel, gradeResult);
        }
    }

    private void calculateResults(int numSubjects) {
        // Validate all subject inputs
        for (int i = 0; i < numSubjects; i++) {
            if (subjectInputs[i].getText().isEmpty()) {
                showAlert("Invalid Input", "Marks for each subject cannot be blank.");
                return;
            }
            try {
                Integer.parseInt(subjectInputs[i].getText());
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Marks must be valid integers.");
                return;
            }
        }

        // Perform calculation
        int totalMarks = 0;
        for (int i = 0; i < numSubjects; i++) {
            totalMarks += Integer.parseInt(subjectInputs[i].getText());
        }
        double avgPercentage = (double) totalMarks / numSubjects;

        // Display results
        totalMarksResult.setText(Integer.toString(totalMarks));
        avgPercentageResult.setText(String.format("%.2f", avgPercentage));

        // Determine grade
        if (avgPercentage >= 90) {
            gradeResult.setText("A");
        } else if (avgPercentage >= 80) {
            gradeResult.setText("B");
        } else if (avgPercentage >= 70) {
            gradeResult.setText("C");
        } else if (avgPercentage >= 60) {
            gradeResult.setText("D");
        } else {
            gradeResult.setText("F");
        }
    }

    private void resetApplication(TextField numSubjectsInput) {
        clearSubjectFields();
        numSubjectsInput.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
