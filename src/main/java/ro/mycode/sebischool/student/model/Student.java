package ro.mycode.sebischool.student.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.enrolment.model.Enrolment;
import ro.mycode.sebischool.users.model.User;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "student")
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // Identitatea (firstName, lastName, email) traieste pe User.
    // Student pastreaza doar campurile specifice rolului.
    @NotNull(message = "Varsta obligatorie")
    private int age;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Book> books = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Enrolment> enrolments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && age == student.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", age=" + age +
                ", userId=" + (user != null ? user.getId() : null) +
                '}';
    }
}
