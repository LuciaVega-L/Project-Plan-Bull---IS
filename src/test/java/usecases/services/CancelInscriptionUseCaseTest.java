package usecases.services;

import entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecases.dto.OperationResult;
import usecases.ports.BULL_GroupRepository;
import usecases.ports.BULL_RegistrationRepository;
import usecases.ports.BULL_StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CancelInscriptionUseCaseTest {

    private CancelInscriptionUseCase useCase;

    private FakeRegistrationRepository registrationRepository;
    private FakeStudentRepository studentRepository;
    private FakeGroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        registrationRepository = new FakeRegistrationRepository();
        studentRepository = new FakeStudentRepository();
        groupRepository = new FakeGroupRepository();

        useCase = new CancelInscriptionUseCase(
                registrationRepository,
                studentRepository,
                groupRepository
        );
    }

    @Test
    void cancelarInscripcion_retornaError_cuandoIdEsVacio() {

        OperationResult result =
                useCase.cancelarInscripcion("", "20201234");

        assertFalse(result.isSuccess());

        assertEquals(
                "El id de inscripción no puede estar vacío.",
                result.getMessage()
        );
    }

    @Test
    void cancelarInscripcion_retornaError_cuandoCodigoEsVacio() {

        OperationResult result =
                useCase.cancelarInscripcion("REG-1", "");

        assertFalse(result.isSuccess());

        assertEquals(
                "El código universitario no puede estar vacío.",
                result.getMessage()
        );
    }

    @Test
    void cancelarInscripcion_retornaError_cuandoInscripcionNoExiste() {

        OperationResult result =
                useCase.cancelarInscripcion(
                        "REG-INEXISTENTE",
                        "20201234"
                );

        assertFalse(result.isSuccess());

        assertTrue(
                result.getMessage().contains(
                        "No se encontró la inscripción"
                )
        );
    }

    @Test
    void cancelarInscripcion_retornaError_cuandoInscripcionPerteneceAOtroEstudiante() {

        BULL_Student student1 =
                createStudent("111");

        BULL_Student student2 =
                createStudent("222");

        BULL_Group group =
                createGroup();

        BULL_Registration registration =
                new BULL_Registration(student1, group);

        registrationRepository.save(registration);

        OperationResult result =
                useCase.cancelarInscripcion(
                        registration.getIdRegistration(),
                        student2.getUniversityCode()
                );

        assertFalse(result.isSuccess());

        assertTrue(
                result.getMessage().contains(
                        "no pertenece al estudiante"
                )
        );
    }

    @Test
    void cancelarInscripcion_retornaError_cuandoInscripcionNoEstaActiva() {

        BULL_Student student =
                createStudent("111");

        BULL_Group group =
                createGroup();

        BULL_Registration registration =
                new BULL_Registration(student, group);

        registration.cancelar();

        registrationRepository.save(registration);

        OperationResult result =
                useCase.cancelarInscripcion(
                        registration.getIdRegistration(),
                        student.getUniversityCode()
                );

        assertFalse(result.isSuccess());

        assertTrue(
                result.getMessage().contains(
                        "no puede cancelarse"
                )
        );
    }

    @Test
    void cancelarInscripcion_retornaError_cuandoEstudianteNoExiste() {

        BULL_Student student =
                createStudent("111");

        BULL_Group group =
                createGroup();

        BULL_Registration registration =
                new BULL_Registration(student, group);

        registrationRepository.save(registration);

        OperationResult result =
                useCase.cancelarInscripcion(
                        registration.getIdRegistration(),
                        student.getUniversityCode()
                );

        assertFalse(result.isSuccess());

        assertTrue(
                result.getMessage().contains(
                        "No se encontró el estudiante"
                )
        );
    }

    @Test
    void cancelarInscripcion_retornaError_cuandoGrupoNoExiste() {

        BULL_Student student =
                createStudent("111");

        studentRepository.save(student);

        BULL_Group group =
                createGroup();

        BULL_Registration registration =
                new BULL_Registration(student, group);

        registrationRepository.save(registration);

        OperationResult result =
                useCase.cancelarInscripcion(
                        registration.getIdRegistration(),
                        student.getUniversityCode()
                );

        assertFalse(result.isSuccess());

        assertTrue(
                result.getMessage().contains(
                        "No se encontró el grupo"
                )
        );
    }

    @Test
    void cancelarInscripcion_cancelaCorrectamente() {

        BULL_Student student =
                createStudent("111");

        studentRepository.save(student);

        BULL_Group group =
                createGroup();

        BULL_Registration registration =
                new BULL_Registration(student, group);

        group.addRegistration(registration);

        registrationRepository.save(registration);
        groupRepository.save(group);

        OperationResult result =
                useCase.cancelarInscripcion(
                        registration.getIdRegistration(),
                        student.getUniversityCode()
                );

        assertTrue(result.isSuccess());

        assertEquals(
                BULL_Registration.STATE_CANCELADA,
                registration.getState()
        );

        assertTrue(
                group.getRegistrations().isEmpty()
        );
    }

    // ==========================
    // Helpers
    // ==========================

    private BULL_Student createStudent(String code) {
        return new BULL_Student(
                code,
                "David",
                "Beltran",
                "david@correo.com",
                5,
                "Ingenieria de Sistemas",
                false
        );
    }

    private BULL_Group createGroup() {
        BULL_Group group = new BULL_Group(1);
        group.setMaxCapacity(
                new BULL_MaxCapacity(10)
        );
        return group;
    }



    private static class FakeRegistrationRepository
            implements BULL_RegistrationRepository {

        private final List<BULL_Registration> registrations =
                new ArrayList<>();

        @Override
        public Optional<BULL_Registration> findByIdRegistration(String idRegistration) {
            return registrations.stream()
                    .filter(r -> r.getIdRegistration().equals(idRegistration))
                    .findFirst();
        }

        @Override
        public List<BULL_Registration> findAll() {
            return registrations;
        }

        @Override
        public void save(BULL_Registration registration) {
            if (!registrations.contains(registration)) {
                registrations.add(registration);
            }
        }

        @Override
        public void deleteByIdRegistration(String idRegistration) {
            registrations.removeIf(
                    r -> r.getIdRegistration().equals(idRegistration)
            );
        }
    }

    private static class FakeStudentRepository
            implements BULL_StudentRepository {

        private final List<BULL_Student> students =
                new ArrayList<>();

        @Override
        public Optional<BULL_Student> findByUniversityCode(String universityCode) {
            return students.stream()
                    .filter(s -> s.getUniversityCode().equals(universityCode))
                    .findFirst();
        }

        @Override
        public List<BULL_Student> findAll() {
            return students;
        }

        @Override
        public void save(BULL_Student student) {
            if (!students.contains(student)) {
                students.add(student);
            }
        }

        @Override
        public void deleteByUniversityCode(String universityCode) {
            students.removeIf(
                    s -> s.getUniversityCode().equals(universityCode)
            );
        }
    }

    private static class FakeGroupRepository
            implements BULL_GroupRepository {

        private final List<BULL_Group> groups =
                new ArrayList<>();

        @Override
        public Optional<BULL_Group> findByIdGroup(int idGroup) {
            return groups.stream()
                    .filter(g -> g.getIdGroup() == idGroup)
                    .findFirst();
        }

        @Override
        public List<BULL_Group> findAll() {
            return groups;
        }

        @Override
        public void save(BULL_Group group) {
            if (!groups.contains(group)) {
                groups.add(group);
            }
        }

        @Override
        public void deleteByIdGroup(int idGroup) {
            groups.removeIf(
                    g -> g.getIdGroup() == idGroup
            );
        }
    }
}