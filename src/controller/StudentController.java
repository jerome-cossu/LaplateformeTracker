package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import src.dao.StudentDAO;

public class StudentController {
    @FXML
    private TextField fnameField;
    @FXML
    private TextField lnameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField gradeField;

    @FXML
    private void handleAddStudent() {
        String first_name = fnameField.getText();
        String last_name = lnameField.getText();
        String ageString = ageField.getText();
        int age = Integer.parseInt(ageString);
        String gradeString = gradeField.getText();
        int grade = Integer.parseInt(gradeString);

        if (!first_name.isEmpty() && !last_name.isEmpty() && !ageString.isEmpty() && !gradeString.isEmpty()) {
            StudentDAO.insertStudent(first_name, last_name, age, grade);
            fnameField.clear();
        }
    }
}