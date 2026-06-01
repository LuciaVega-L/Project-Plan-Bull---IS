package usecases.services;

import entities.*;
import infrastructure.repositories.BULL_InMemoryHomologationRepository;
import infrastructure.repositories.BULL_InMemoryStudentRepository;
import org.junit.jupiter.api.Test;
import usecases.dto.OperationResult;
import usecases.ports.BULL_HomologationRepository;
import usecases.ports.BULL_StudentRepository;

import static org.junit.jupiter.api.Assertions.*;

class RequestHomologationUseCaseTest {

    private BULL_Student createStudent() {
        return new BULL_Student(
                "20201234",
                "David",
                "Beltran",
                "david@correo.com",
                5,
                "Ingenieria de Sistemas",
                false
        );
    }

    @Test
    void execute_falla_siCodigoEsNulo() {

        BULL_StudentRepository studentRepo =
                new BULL_InMemoryStudentRepository();

        BULL_HomologationRepository homologationRepo =
                new BULL_InMemoryHomologationRepository();

        RequestHomologationUseCase useCase =
                new RequestHomologationUseCase(studentRepo, homologationRepo);

        OperationResult result =
                useCase.execute(null, "certificado.pdf", "PDF");

        assertFalse(result.isSuccess());
    }

    @Test
    void execute_falla_siNombreArchivoEsVacio() {

        BULL_StudentRepository studentRepo =
                new BULL_InMemoryStudentRepository();

        BULL_HomologationRepository homologationRepo =
                new BULL_InMemoryHomologationRepository();

        RequestHomologationUseCase useCase =
                new RequestHomologationUseCase(studentRepo, homologationRepo);

        OperationResult result =
                useCase.execute("20201234", "", "PDF");

        assertFalse(result.isSuccess());
    }

    @Test
    void execute_falla_siTipoArchivoEsVacio() {

        BULL_StudentRepository studentRepo =
                new BULL_InMemoryStudentRepository();

        BULL_HomologationRepository homologationRepo =
                new BULL_InMemoryHomologationRepository();

        RequestHomologationUseCase useCase =
                new RequestHomologationUseCase(studentRepo, homologationRepo);

        OperationResult result =
                useCase.execute("20201234", "certificado.pdf", "");

        assertFalse(result.isSuccess());
    }

    @Test
    void execute_falla_siEstudianteNoExiste() {

        BULL_StudentRepository studentRepo =
                new BULL_InMemoryStudentRepository();

        BULL_HomologationRepository homologationRepo =
                new BULL_InMemoryHomologationRepository();

        RequestHomologationUseCase useCase =
                new RequestHomologationUseCase(studentRepo, homologationRepo);

        OperationResult result =
                useCase.execute("99999", "certificado.pdf", "PDF");

        assertFalse(result.isSuccess());
    }

    @Test
    void execute_falla_siTipoArchivoNoPermitido() {

        BULL_StudentRepository studentRepo =
                new BULL_InMemoryStudentRepository();

        BULL_HomologationRepository homologationRepo =
                new BULL_InMemoryHomologationRepository();

        studentRepo.save(createStudent());

        RequestHomologationUseCase useCase =
                new RequestHomologationUseCase(studentRepo, homologationRepo);

        OperationResult result =
                useCase.execute(
                        "20201234",
                        "certificado.exe",
                        "EXE"
                );

        assertFalse(result.isSuccess());
    }

    @Test
    void execute_creaSolicitudCorrectamente() {

        BULL_StudentRepository studentRepo =
                new BULL_InMemoryStudentRepository();

        BULL_HomologationRepository homologationRepo =
                new BULL_InMemoryHomologationRepository();

        studentRepo.save(createStudent());

        RequestHomologationUseCase useCase =
                new RequestHomologationUseCase(studentRepo, homologationRepo);

        OperationResult result =
                useCase.execute(
                        "20201234",
                        "certificado.pdf",
                        "PDF"
                );

        assertTrue(result.isSuccess());

        assertTrue(
                homologationRepo.findByStudent("20201234").isPresent()
        );
    }

    @Test
    void execute_falla_siYaTieneSolicitudPendiente() {

        BULL_StudentRepository studentRepo =
                new BULL_InMemoryStudentRepository();

        BULL_HomologationRepository homologationRepo =
                new BULL_InMemoryHomologationRepository();

        BULL_Student student = createStudent();

        studentRepo.save(student);

        BULL_Homologation homologation =
                new BULL_Homologation(
                        student,
                        new BULL_Certificate(
                                "cert.pdf",
                                "PDF",
                                "ruta"
                        )
                );

        homologationRepo.save(homologation);

        RequestHomologationUseCase useCase =
                new RequestHomologationUseCase(studentRepo, homologationRepo);

        OperationResult result =
                useCase.execute(
                        "20201234",
                        "nuevo.pdf",
                        "PDF"
                );

        assertFalse(result.isSuccess());
    }

    @Test
    void execute_falla_siYaTieneHomologacionAprobada() {

        BULL_StudentRepository studentRepo =
                new BULL_InMemoryStudentRepository();

        BULL_HomologationRepository homologationRepo =
                new BULL_InMemoryHomologationRepository();

        BULL_Student student = createStudent();

        studentRepo.save(student);

        BULL_Homologation homologation =
                new BULL_Homologation(
                        student,
                        new BULL_Certificate(
                                "cert.pdf",
                                "PDF",
                                "ruta"
                        )
                );

        homologation.approve(3, "Aprobada");

        homologationRepo.save(homologation);

        RequestHomologationUseCase useCase =
                new RequestHomologationUseCase(studentRepo, homologationRepo);

        OperationResult result =
                useCase.execute(
                        "20201234",
                        "nuevo.pdf",
                        "PDF"
                );

        assertFalse(result.isSuccess());
    }
}