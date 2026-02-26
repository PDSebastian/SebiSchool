package ro.mycode.sebischool.enrolment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.student.model.Student;

import java.time.LocalDateTime;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "enrolment")
@Entity
public class Enrolment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull(message = "Data este obligatorie")
    private LocalDateTime createdAt;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Enrolment enrolment = (Enrolment) o;
        return id == enrolment.id && Objects.equals(createdAt, enrolment.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt);
    }

    @Override
    public String toString() {
        return "Enrolment{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id",referencedColumnName = "id")
    private Course course;







}
