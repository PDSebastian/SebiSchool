package ro.mycode.sebischool.professor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String specialty;

    @NotBlank
    private String departament;

    @Min(0)
    private int yearExperience;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",unique = true,nullable = false)

    private User user;

    @OneToMany(mappedBy = "professor",fetch = FetchType.LAZY)
    private Set<Course> courses;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return yearExperience == professor.yearExperience && Objects.equals(id, professor.id) && Objects.equals(specialty, professor.specialty) && Objects.equals(departament, professor.departament) && Objects.equals(user, professor.user) && Objects.equals(courses, professor.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, specialty, departament, yearExperience, user, courses);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", specialty='" + specialty + '\'' +
                ", departament='" + departament + '\'' +
                ", yearExperience=" + yearExperience +
                ", user=" + user +
                ", courses=" + courses +
                '}';
    }

}
