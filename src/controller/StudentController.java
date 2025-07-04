package src.controller;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import src.dao.StudentDAO;
import src.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    private TextField searchIdField;

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
        modifyButton.disableProperty().bind(
            studentTable.getSelectionModel().selectedItemProperty().isNull()
        );
        deleteButton.disableProperty().bind(
            studentTable.getSelectionModel().selectedItemProperty().isNull()
        );
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            fnameField.setText(newSelection.getFirstName());
            lnameField.setText(newSelection.getLastName());
            ageField.setText(String.valueOf(newSelection.getAge()));
            gradeField.setText(String.valueOf(newSelection.getGrade()));
        } else {
            fnameField.clear();
            lnameField.clear();
            ageField.clear();
            gradeField.clear();
            }
        });
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

    @FXML
    private void handleAddStudent(ActionEvent event) {
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

    @FXML 
    private void handleModifyStudent(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            System.out.println("Aucun étudiant sélectionné.");
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
            System.out.println("Âge ou note invalide.");
        }
    }

    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            System.out.println("Aucun étudiant sélectionné.");
            return;
        }
        StudentDAO.deleteStudent(selectedStudent);
        observableStudents.remove(selectedStudent);
        studentTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSearchByID() {
        String input = searchIdField.getText().trim();
        if (input.isEmpty()) {
            studentTable.setItems(observableStudents);
            return;
        }
        
        try {
            int id = Integer.parseInt(input);
            Student foundStudent = null;
            for (Student s : observableStudents) {
                if (s.getId() == id) {
                    foundStudent = s;
                    break;
                }
            }
            
            if (foundStudent != null) {
                studentTable.setItems(FXCollections.observableArrayList(foundStudent));
            } else {
                studentTable.setItems(FXCollections.observableArrayList());
                System.out.println("No student match this ID.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }


}