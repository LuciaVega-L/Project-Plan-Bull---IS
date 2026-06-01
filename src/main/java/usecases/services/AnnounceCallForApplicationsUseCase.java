package usecases.services;

import entities.BULL_Call;
import entities.BULL_Student;
import usecases.dto.OperationResult;
import usecases.ports.BULL_StudentRepository;

import java.util.List;

public class AnnounceCallForApplicationsUseCase {

    private final BULL_StudentRepository studentRepository;

    public AnnounceCallForApplicationsUseCase(BULL_StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public OperationResult announce(BULL_Call call) {

        List<BULL_Student> students = studentRepository.findAll();

        if (students.isEmpty())
            return OperationResult.fail("No hay estudiantes registrados para notificar.");

        System.out.println("\n[SIMULACIÓN] Enviando notificaciones para convocatoria "
                + call.getCallId() + " (" + call.getYear() + "-" + call.getPeriod() + ")");
        System.out.println("─".repeat(55));

        for (BULL_Student s : students) {
            System.out.printf("  ✉  Mensaje enviado a: %-25s | %s%n",
                    s.getName() + " " + s.getSurnames(), s.getMail());
        }

        System.out.println("─".repeat(55));

        return OperationResult.ok("Notificación simulada enviada a " + students.size() + " estudiante(s).");
    }
}