import edu.innotech.GradeCheckService;
import edu.innotech.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

public class StudentTest {

    @Test
    void testAddNormalGrade() {
        GradeCheckService gradeCheckServiceMock = Mockito.mock(GradeCheckService.class);
        Mockito.when(gradeCheckServiceMock.checkGrade(4)).thenReturn(true);

        Student student = new Student("Ivan", gradeCheckServiceMock);
        student.addGrade(4);

        Assertions.assertEquals(Arrays.asList(4), student.getGrades());
    }

    @Test
    void testAddWrongGrade() {
        GradeCheckService gradeCheckServiceMock = Mockito.mock(GradeCheckService.class);
        Mockito.when(gradeCheckServiceMock.checkGrade(8)).thenReturn(false);

        Student student = new Student("Ivan", gradeCheckServiceMock);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            student.addGrade(8);
        });

        Assertions.assertEquals("8 is wrong grade", exception.getMessage());
    }

}