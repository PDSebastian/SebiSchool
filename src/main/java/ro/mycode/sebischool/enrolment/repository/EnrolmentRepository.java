package ro.mycode.sebischool.enrolment.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ro.mycode.sebischool.enrolment.model.Enrolment;

import java.util.List;

public interface EnrolmentRepository extends JpaRepository<Enrolment,Long> {

    @EntityGraph(attributePaths = {"student","course"})
    List<Enrolment> findByCourseId(Long courseId);











}
