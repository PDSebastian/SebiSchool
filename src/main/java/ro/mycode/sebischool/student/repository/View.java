package ro.mycode.sebischool.student.repository;

import org.springframework.stereotype.Component;
import ro.mycode.sebischool.books.exceptions.BookNotFoundException;
import ro.mycode.sebischool.books.model.Book;
import ro.mycode.sebischool.books.repository.BookRepository;
import ro.mycode.sebischool.books.service.commandService.BookCommandService;
import ro.mycode.sebischool.books.service.dtos.BookResponse;
import ro.mycode.sebischool.books.service.dtos.Bookrequest;
import ro.mycode.sebischool.books.service.mapper.BookMapper;
import ro.mycode.sebischool.course.exceptions.CourseNotFoundException;
import ro.mycode.sebischool.course.model.Course;
import ro.mycode.sebischool.course.repository.CourseRepository;
import ro.mycode.sebischool.course.service.mapper.CourseMapper;
import ro.mycode.sebischool.student.exceptions.StudentNotFoundException;
import ro.mycode.sebischool.student.model.Student;
import ro.mycode.sebischool.student.service.commandService.StudentCommandService;
import ro.mycode.sebischool.student.service.dtos.StudentRequest;
import ro.mycode.sebischool.student.service.dtos.StudentResponse;
import ro.mycode.sebischool.student.service.mapper.StudentMapper;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class View {
    BookCommandService bookCommandService;
    StudentCommandService studentCommandService;
    StudentRepository studentRepository;
    StudentMapper studentMapper;
    BookRepository bookRepository;
    BookMapper bookMapper;
    CourseRepository cRepository;
    CourseMapper courseMapper;

    public View(StudentCommandService studentCommandService, StudentRepository studentRepository, StudentMapper studentMapper, BookRepository bookRepository,
                BookMapper bookMapper, BookCommandService bookCommandService,CourseRepository cRepository, CourseMapper courseMapper) {
        this.studentCommandService = studentCommandService;
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookCommandService = bookCommandService;
        this.cRepository = cRepository;
        this.courseMapper = courseMapper;
//        testFindStudentsByCourse();
//
//      testGetAllCoursesWithEnrollment();
//      testStudentsWithBooks();
        testGetAllStudents();
//        testFindCoursesByStudentEmail();
    }
    void testAddStudent(){
       Student s=Student.builder().
               firstName("dsdsdsdsd").
               lastName("sdfsds").
               email("AD@gmail.com").
               age(33).build();
       studentRepository.save(s);

    }
   void testDeleteStudent(){
        if(studentRepository.existsById(1L)){
            studentRepository.deleteById(1L);
        }
        else{
            throw new StudentNotFoundException("Student not found");
        }
   }
   void testUpdateStudent(){
        Student s=studentRepository.findById(2L).orElseThrow(()->new StudentNotFoundException("Student not found"));
        s.setFirstName("dsdsdsdsd");
        s.setLastName("sdfsdsdf");
        s.setAge(40);
        s.setEmail("SBgmail.com");
        Student updated=studentRepository.save(s);


   }
   void testGetAllStudents(){
        List<Student> students = studentRepository.findAll();
        students.forEach(System.out::println);
   }
   void testFindAllBooks(){
        List<Book> b=bookRepository.findAll();
        b.forEach(System.out::println);

   }
   void testAddBook(){
        Bookrequest b=Bookrequest.builder().bookName("test").createdAt(LocalDateTime.now()).build();
        bookCommandService.addBook(10L,b);

   }
   void testDeleteBook(){
        if(bookRepository.existsById(10L)){
            bookCommandService.deleteBook(10L);
        }
        else{
            throw new BookNotFoundException("Book not found");
        }
   }void testUpdateBook(){
        Book b=bookRepository.findById(11L).orElseThrow(()->new BookNotFoundException("Book not found"));
        b.setBookName("test");
        b.setCreatedAt(LocalDateTime.now());
        Book updated=bookRepository.save(b);

    }
    void testGetAllCourses(){
        List<Course> c=cRepository.findAll();
        c.forEach(System.out::println);
    }
    void testGetAllCoursesWithEnrollment(){
        List<Course> c= cRepository.findAllByEnrolmentsIsNotEmpty();
        c.forEach(System.out::println);
    }
    void testFindStudentsByCourse() {
        studentRepository.findAllByEnrolments_Course_Name("Algorithms")
                .forEach(s -> System.out.println(s.getFirstName() + " " + s.getLastName()));
    }
    void testStudentsWithBooks() {
        studentRepository.findAllByBooksBookName("Linear Algebra").forEach(s ->
                System.out.println(s.getFirstName()));
    }
    void testFindCoursesByStudentEmail() {

        List<Course> cursuri = cRepository.findAllByEnrolmentsStudentEmail("maria.stan@email.com");
        cursuri.forEach(c -> System.out.println( c.getName()));
    }






}
