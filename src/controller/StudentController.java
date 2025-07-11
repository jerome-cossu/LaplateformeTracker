package src.controller;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import src.dao.StudentDAO;
import src.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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
    private Button modifyButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField searchField;

    // Data access object for students
    private final StudentDAO studentDAO = new StudentDAO();

    /**
     * Initialize method called after FXML is loaded.
     * Sets up table columns and loads students into the table.
     * Also sets up listeners for selection and row clicks.
     */
    @FXML
    public void initialize() {
        // Map table columns to Student properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Load all students from the database
        List<Student> students = StudentDAO.getAllStudents();
        observableStudents = FXCollections.observableArrayList(students);
        studentTable.setItems(observableStudents);

        // Disable modify and delete buttons if no student is selected
        modifyButton.disableProperty().bind(
            studentTable.getSelectionModel().selectedItemProperty().isNull()
        );
        deleteButton.disableProperty().bind(
            studentTable.getSelectionModel().selectedItemProperty().isNull()
        );

        // Update input fields when a student is selected
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fnameField.setText(newSelection.getFirstName());
                lnameField.setText(newSelection.getLastName());
                ageField.setText(String.valueOf(newSelection.getAge()));
                gradeField.setText(String.valueOf(newSelection.getGrade()));
            } else {
                // Clear input fields if nothing is selected
                fnameField.clear();
                lnameField.clear();
                ageField.clear();
                gradeField.clear();
            }
        });

        // Allow deselecting a selected row by clicking it again
        studentTable.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    int index = row.getIndex();
                    if (studentTable.getSelectionModel().isSelected(index)) {
                        studentTable.getSelectionModel().clearSelection(index);
                        event.consume();
                    }
                }
            });
            return row;
        });
    }

    /**
     * Handles adding a new student to the database and table.
     */
    @FXML
    private void handleAddStudent(ActionEvent event) {
        String firstName = fnameField.getText();
        String lastName = lnameField.getText();
        String ageString = ageField.getText();
        String gradeString = gradeField.getText();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !ageString.isEmpty() && !gradeString.isEmpty()) {
            int age = Integer.parseInt(ageString);
            int grade = Integer.parseInt(gradeString);

            StudentDAO.insertStudent(firstName, lastName, age, grade);
            Student newStudent = StudentDAO.getLastInsertedStudent();

            observableStudents.add(newStudent);

            // Clear input fields after adding
            fnameField.clear();
            lnameField.clear();
            ageField.clear();
            gradeField.clear();
        }
    }

    /**
     * Handles modifying the selected student's details.
     */
    @FXML 
    private void handleModifyStudent(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            System.out.println("No student selected.");
            return;
        }

        String newFirstName = fnameField.getText();
        String newLastName = lnameField.getText();
        String newAgeString = ageField.getText();
        String newGradeString = gradeField.getText();

        try {
            int newAge = Integer.parseInt(newAgeString);
            int newGrade = Integer.parseInt(newGradeString);

            selectedStudent.setFirstName(newFirstName);
            selectedStudent.setLastName(newLastName);
            selectedStudent.setAge(newAge);
            selectedStudent.setGrade(newGrade);

            StudentDAO.updateStudent(selectedStudent);
            studentTable.refresh();
        } catch (NumberFormatException e) {
            System.out.println("Invalid age or grade.");
        }
    }

    /**
     * Handles deleting the selected student from the database and table.
     */
    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            System.out.println("No student selected.");
            return;
        }
        StudentDAO.deleteStudent(selectedStudent);
        observableStudents.remove(selectedStudent);
        studentTable.getSelectionModel().clearSelection();
    }

    /**
     * Filters the students in the table based on the search term.
     */
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText();
        ObservableList<Student> filtered = studentDAO.searchStudents(searchTerm);
        studentTable.setItems(filtered);
    }

    /**
     * Opens the statistics window in a new stage.
     */
    @FXML
    public void handleShowStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/statistics_page.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Student Statistics");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
