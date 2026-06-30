package ro.mycode.sebischool;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// @ActiveProfiles("test") -> foloseste H2 (application-test.yaml) in loc de MySQL,
// ca testul de context sa porneasca fara o baza de date reala pe masina.
@SpringBootTest
@ActiveProfiles("test")
class SebiSchoolApplicationTests {

    @Test
    void contextLoads() {
    }

}
