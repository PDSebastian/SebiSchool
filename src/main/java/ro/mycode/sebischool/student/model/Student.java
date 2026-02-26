package ro.mycode.sebischool.student.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.enrolment.model.Enrolment;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="student")
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = " Prenumele este obligatoriu")
    private String firstName;

    @NotBlank(message = "Numele este obligatoriu")
    private String lastName;

    @NotBlank(message = "emailul este obligatoriu")
    private String email;

    @NotNull(message = "Varsta obligatorie")
    private int age;


    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                 ",books"+books+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && age == student.age && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName) && Objects.equals(email, student.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, age);
    }
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<Book> books = new HashSet<>();

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<Enrolment> enrolments = new HashSet<>();



}
