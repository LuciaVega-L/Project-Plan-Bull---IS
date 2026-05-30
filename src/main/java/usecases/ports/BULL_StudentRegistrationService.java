package usecases.ports;

import usecases.dto.ModuleOptionDTO;
import usecases.dto.OperationResult;
import java.util.List;

public interface BULL_StudentRegistrationService {

    // Port para consultar módulos disponibles según nivel del estudiante
    interface CheckModuleInputPort {
        List<ModuleOptionDTO> consultarPorEstudiante(String universityCode);
    }

    // Port para registrar la opción que el estudiante ya eligió
    interface CourseRegistrationInputPort {
        OperationResult registrar(String universityCode, ModuleOptionDTO opcionElegida);
    }
}