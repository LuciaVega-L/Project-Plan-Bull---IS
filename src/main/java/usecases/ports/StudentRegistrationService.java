package usecases.ports;

import usecases.dto.ModuleOptionDTO;
import usecases.dto.OperationResult;
import java.util.List;

public interface StudentRegistrationService {
    // Port para el primer caso de uso
    public interface CheckModuleInputPort {
        List<ModuleOptionDTO> consultarPorEstudiante(String universityCode);
    }

    // Port para el segundo caso de uso
    public interface CourseRegistrationInputPort {
        OperationResult registrar(String universityCode, int idGrupo);
    }
}