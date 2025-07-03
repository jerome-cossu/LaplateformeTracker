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

    @Override
    public String toString() {
        return id + " | " + first_name + " | " + last_name + " | " + age + " | " + grade;
    }
}
