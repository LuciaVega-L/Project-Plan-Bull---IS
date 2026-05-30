package entities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BULL_Course {

    private int idModule;
    private int courseNumber;
    private List<BULL_Semester> semesters;

    public int getIdModule() { return idModule; }
    public int getCourseNumber() { return courseNumber; }
    public List<BULL_Semester> getSemesters() { return Collections.unmodifiableList(semesters); }

    public BULL_Course(int idModule, int courseNumber) {
        if (idModule <= 0) {
            throw new IllegalArgumentException("El ID del módulo debe ser mayor a 0.");
        }
        if (courseNumber < 1 || courseNumber > 4) {
            throw new IllegalArgumentException("El número de curso debe estar entre 1 y 4.");
        }
        this.idModule = idModule;
        this.courseNumber = courseNumber;
        this.semesters = new ArrayList<>();
    }
    public void addSemester(BULL_Semester semester) {
        semesters.add(semester);
    }

    @Override
    public String toString() {
        return "Course{idModule=" + idModule + ", courseNumber=" + courseNumber + "}";
    }
}
