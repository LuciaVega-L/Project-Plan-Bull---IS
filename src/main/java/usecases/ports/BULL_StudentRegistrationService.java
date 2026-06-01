package usecases.ports;

import usecases.dto.CourseOptionDTO;
import usecases.dto.OperationResult;
import java.util.List;

public interface BULL_StudentRegistrationService {

    // Port para consultar módulos disponibles según nivel del estudiante
    interface CheckCourseInputPort {
        List<CourseOptionDTO> consultarPorEstudiante(String universityCode);
    }

    // Port para registrar la opción que el estudiante ya eligió
    interface CourseRegistrationInputPort {
        OperationResult registrar(String universityCode, CourseOptionDTO opcionElegida);
    }
}