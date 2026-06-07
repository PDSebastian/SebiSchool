package ro.mycode.sebischool.professor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.users.model.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "professor")
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    public String specialty;

    @NotBlank
    private String departament;

    @NotNull
    private int yearExperience;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "professor",fetch = FetchType.LAZY)
    private Set<Course> courses=new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return yearExperience == professor.yearExperience && Objects.equals(id, professor.id) && Objects.equals(firstName, professor.firstName) && Objects.equals(lastName, professor.lastName) && Objects.equals(specialty, professor.specialty) && Objects.equals(departament, professor.departament) && Objects.equals(user, professor.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, specialty, departament, yearExperience, user);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", specialty='" + specialty + '\'' +
                ", department='" + departament + '\'' +
                ", yearExperience=" + yearExperience +
                ", user=" + user +
                '}';
    }

}
