package src.controller;

import java.lang.annotation.Native;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import src.dao.StudentDAO;
import src.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, Integer> idColumn;
    @FXML
    private TableColumn<Student, String> firstNameColumn;
    @FXML
    private TableColumn<Student, String> lastNameColumn;
    @FXML
    private TableColumn<Student, Integer> ageColumn;
    @FXML
    private TableColumn<Student, Integer> gradeColumn;
    @FXML
    private ObservableList<Student> observableStudents;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        List<Student> students = StudentDAO.getAllStudents();
        observableStudents = FXCollections.observableArrayList(students);
        studentTable.setItems(observableStudents);
    }

    @FXML
    private void handleAddStudent() {
        String first_name = fnameField.getText();
        String last_name = lnameField.getText();
        String ageString = ageField.getText();
        String gradeString = gradeField.getText();

        if (!first_name.isEmpty() && !last_name.isEmpty() && !ageString.isEmpty() && !gradeString.isEmpty()) {
            int age = Integer.parseInt(ageString);
            int grade = Integer.parseInt(gradeString);

            StudentDAO.insertStudent(first_name, last_name, age, grade);

            Student newStudent = StudentDAO.getLastInsertedStudent();

            observableStudents.add(newStudent);

            fnameField.clear();
            lnameField.clear();
            ageField.clear();
            gradeField.clear();
        }
    }
}