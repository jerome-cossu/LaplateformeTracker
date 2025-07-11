package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Student;
import model.StudentDataHolder;

import java.util.List;

public class StatisticsController {

    @FXML
    private Label averageAgeLabel;

    @FXML
    private Label averageGradeLabel;

    /**
     * Initializes the statistics view by calculating
     * and displaying the average age and average grade
     * of all students.
     */
    @FXML
    public void initialize() {
        List<Student> students = StudentDataHolder.getStudents();

        // If there are no students, display N/A for both statistics
        if (students == null || students.isEmpty()) {
            averageAgeLabel.setText("Average Age: N/A");
            averageGradeLabel.setText("Average Grade: N/A");
            return;
        }

        // Calculate the average age and grade using streams
        double avgAge = students.stream()
                                .mapToInt(Student::getAge)
                                .average()
                                .orElse(0.0);

        double avgGrade = students.stream()
                                  .mapToDouble(Student::getGrade)
                                  .average()
                                  .orElse(0.0);

        // Display averages formatted with 2 decimal places
        averageAgeLabel.setText(String.format("Average Age: %.2f", avgAge));
        averageGradeLabel.setText(String.format("Average Grade: %.2f", avgGrade));
    }
}
