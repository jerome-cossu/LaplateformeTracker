package src.model;

public class Student {
    private int id;
    private String first_name;
    private String last_name;
    private int age;
    private int grade;

    public Student(int id, String first_name, String last_name, int age, int grade) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public int getAge() {
        return age;
    }

    public int getGrade() {
        return grade;
    }

}
