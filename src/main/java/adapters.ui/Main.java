package adapters.ui;

import entities.*;
import usecases.dto.CourseOptionDTO;
import usecases.dto.OperationResult;
import usecases.services.PlanBullApp;

import java.util.List;
import java.util.Scanner;

public class Main {

    static final Scanner sc = new Scanner(System.in);
    static PlanBullApp app;

    public static void main(String[] args) {
        app = new PlanBullApp();
        System.out.println("=== PlanBull iniciado ===\n");
        menuPrincipal();
    }

    // MENÚ PRINCIPAL
    private static void menuPrincipal() {
        boolean salir = false;
        while (!salir) {
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║       PlanBull - Menú Principal      ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Módulos                          ║");
            System.out.println("║  2. Inscripciones                    ║");
            System.out.println("║  3. Espacios (aulas)                 ║");
            System.out.println("║  4. Notas                            ║");
            System.out.println("║  5. Ver datos del sistema            ║");
            System.out.println("║  6. Convocatorias                    ║");
            System.out.println("║  7. Homologaciones                   ║");
            System.out.println("║  0. Salir                            ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Opción: ");
            switch (leerEntero()) {
                case 1: menuModulos();       break;
                case 2: menuInscripciones(); break;
                case 3: menuEspacios();      break;
                case 4: menuNotas();        break;
                case 5: menuVerDatos();      break;
                case 6: menuConvocatorias(); break;
                case 7: menuHomologaciones(); break;
                case 0: salir = true; System.out.println("Hasta pronto."); break;
                default: System.out.println("[!] Opción no válida.\n");
            }
        }
    }

    // MÓDULOS
    private static void menuModulos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Módulos ---");
            System.out.println("1. Crear módulo");
            System.out.println("2. Consultar todos los módulos");
            System.out.println("3. Consultar módulo por ID");
            System.out.println("4. Eliminar módulo");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            switch (leerEntero()) {
                case 1:
                    System.out.print("ID del módulo: ");
                    int id = leerEntero();
                    System.out.print("Número de curso (1-4): ");
                    mostrar(app.crearModulo(id, leerEntero()));
                    break;
                case 2:
                    mostrar(app.consultarModulos());
                    break;
                case 3:
                    System.out.print("ID del módulo: ");
                    mostrar(app.consultarModuloPorId(leerEntero()));
                    break;
                case 4:
                    System.out.print("ID del módulo a eliminar: ");
                    mostrar(app.eliminarModulo(leerEntero()));
                    break;
                case 0: volver = true; break;
                default: System.out.println("[!] Opción no válida.");
            }
        }
    }

    // INSCRIPCIONES
    private static void menuInscripciones() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Inscripciones ---");
            System.out.println("1. Ver módulos disponibles para un estudiante");
            System.out.println("2. Inscribir estudiante en un módulo");
            System.out.println("3. Cancelar inscripción");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            switch (leerEntero()) {
                case 1: verModulosDisponibles(); break;
                case 2: inscribirEstudiante();   break;
                case 3: cancelarInscripcion();   break;
                case 0: volver = true;           break;
                default: System.out.println("[!] Opción no válida.");
            }
        }
    }

    private static void verModulosDisponibles() {
        System.out.print("Código universitario del estudiante: ");
        String codigo = leerTexto();
        List<CourseOptionDTO> opciones = app.consultarModulosDisponibles(codigo);
        if (opciones.isEmpty()) {
            System.out.println("[INFO] Sin módulos disponibles para " + codigo + ".");
            return;
        }
        imprimirOpciones(opciones);
    }



    private static void inscribirEstudiante() {
        System.out.print("Código universitario del estudiante: ");
        String codigo = leerTexto();

        List<CourseOptionDTO> opciones = app.consultarModulosDisponibles(codigo);
        if (opciones.isEmpty()) {
            System.out.println("[INFO] Sin módulos disponibles para " + codigo + ".");
            return;
        }
        imprimirOpciones(opciones);

        System.out.print("Número de opción a inscribir: ");
        mostrar(app.inscribirEstudiante(codigo, leerEntero() - 1));
    }

    private static void cancelarInscripcion() {
        System.out.print("ID de la inscripción: ");
        String idReg = leerTexto();
        System.out.print("Código universitario del estudiante: ");
        String codigo = leerTexto();
        mostrar(app.cancelarInscripcion(idReg, codigo));
    }

    private static void imprimirOpciones(List<CourseOptionDTO> opciones) {
        System.out.println("\nOpciones disponibles:");
        for (int i = 0; i < opciones.size(); i++) {
            CourseOptionDTO o = opciones.get(i);
            System.out.printf("  [%d] Grupo %-4d | Módulo %-3d | Curso %-2d | %-22s | Cupos: %-3d | Horario: %s%n",
                    i + 1, o.getIdGrupo(), o.getIdCourse(), o.getCourseNumber(),
                    o.getModalidad(), o.getCuposRestantes(), o.getHorario());
            if (o.isEsPresencial() && o.getUbicacion() != null) {
                System.out.printf("       Edificio: %s  Aula: %s%n", o.getUbicacion(), o.getNumAula());
            }
        }
    }


    // ESPACIOS
    private static void menuEspacios() {
        System.out.println("\n--- Asignar espacio a grupo presencial ---");
        System.out.print("ID del grupo: ");
        int idGrupo = leerEntero();
        System.out.print("Edificio: ");
        String edificio = leerTexto();
        System.out.print("Número de aula: ");
        String aula = leerTexto();
        mostrar(app.asignarEspacio(idGrupo, edificio, aula));
    }

    // NOTAS
    private static void menuNotas() {
        System.out.println("\n--- Cargar nota ---");
        System.out.print("ID del grupo: ");
        int idGrupo = leerEntero();
        System.out.print("Código universitario del estudiante: ");
        String universityCode = leerTexto();

        System.out.println("Tipo de corte:");
        System.out.println("  1. PRIMER_CORTE  (30%)");
        System.out.println("  2. SEGUNDO_CORTE (30%)");
        System.out.println("  3. TERCER_CORTE  (40%)");
        System.out.print("Opción: ");
        int opcionCorte = leerEntero();

        GradeType[] tipos = { GradeType.PRIMER_CORTE, GradeType.SEGUNDO_CORTE, GradeType.TERCER_CORTE };
        if (opcionCorte < 1 || opcionCorte > 3) {
            System.out.println("[!] Opción de corte no válida.");
            return;
        }

        System.out.print("Valor de la nota (0.0 - 5.0): ");
        double nota = leerDouble();
        mostrar(app.cargarNota(idGrupo, universityCode, tipos[opcionCorte - 1], nota));
    }


    private static void menuVerDatos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Ver datos del sistema ---");
            System.out.println("1. Estudiantes");
            System.out.println("2. Profesores");
            System.out.println("3. Inscripciones");
            System.out.println("4. Grupos");
            System.out.println("5. Volver");
            System.out.print("Opción: ");
            switch (leerEntero()) {
                case 1: listarEstudiantes();   break;
                case 2: listarProfesores();    break;
                case 3: listarInscripciones(); break;
                case 4: listarGrupos();        break;
                case 5: volver = true;         break;
                default: System.out.println("[!] Opción no válida.");
            }
        }
    }

    private static void listarEstudiantes() {
        List<BULL_Student> lista = app.getEstudiantes();
        if (lista.isEmpty()) { System.out.println("No hay estudiantes."); return; }
        System.out.println("\nEstudiantes:");
        for (BULL_Student s : lista) {
            System.out.printf("  %-12s | %-25s | Sem: %d | Especial: %s%n",
                    s.getUniversityCode(), s.getName() + " " + s.getSurnames(),
                    s.getSemester(), s.isSpecialCondition() ? "Sí" : "No");
        }
    }

    private static void listarProfesores() {
        List<BULL_Professor> lista = app.getProfesores();
        if (lista.isEmpty()) { System.out.println("No hay profesores."); return; }
        System.out.println("\nProfesores:");
        for (BULL_Professor p : lista) {
            System.out.printf("  %-8s | %-25s | %s%n", p.getIdTeaching(), p.getName(), p.getMail());
        }
    }

    private static void listarInscripciones() {
        List<BULL_Registration> lista = app.getInscripciones();
        if (lista.isEmpty()) { System.out.println("No hay inscripciones."); return; }
        System.out.println("\nInscripciones:");
        for (BULL_Registration r : lista) {
            System.out.printf("  %s | %-25s | Grupo: %d | Estado: %s%n",
                    r.getIdRegistration(),
                    r.getStudent().getName() + " " + r.getStudent().getSurnames(),
                    r.getGroup().getIdGroup(), r.getState());
        }
    }

    private static void listarGrupos() {
        List<BULL_Group> lista = app.getGrupos();
        if (lista.isEmpty()) { System.out.println("No hay grupos."); return; }
        System.out.println("\nGrupos:");
        for (BULL_Group g : lista) {
            String prof  = g.getProfessor()    != null ? g.getProfessor().getName() : "Sin asignar";
            String cupos = g.getMaxCapacity()  != null
                    ? g.getMaxCapacity().getCuposRestantes() + "/" + g.getMaxCapacity().getMaxCapacity()
                    : "N/A";
            String aula  = g.getUbication()    != null
                    ? g.getUbication().getBuilding() + " – " + g.getUbication().getClassroomNum()
                    : "Virtual";
            System.out.printf("  Grupo %-4d | %-22s | Cupos: %-6s | %s%n",
                    g.getIdGroup(), prof, cupos, aula);
        }
    }
    // HOMOLOGACIONES
    private static void menuHomologaciones() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Homologaciones ---");
            System.out.println("1. Solicitar homologación");
            System.out.println("2. Listar homologaciones pendientes");
            System.out.println("3. Aprobar homologación");
            System.out.println("4. Rechazar homologación");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            switch (leerEntero()) {
                case 1: solicitarHomologacion(); break;
                case 2: listarHomologaciones();  break;
                case 3: aprobarHomologacion();   break;
                case 4: rechazarHomologacion();  break;
                case 0: volver = true;           break;
                default: System.out.println("[!] Opción no válida.");
            }
        }
    }

    private static void solicitarHomologacion() {
        System.out.print("Código universitario del estudiante: ");
        String codigo = leerTexto();
        System.out.print("Nombre del archivo (ej: certificado.pdf): ");
        String fileName = leerTexto();
        System.out.print("Tipo de archivo (PDF, DOCX, TXT, JPG, PNG): ");
        String fileType = leerTexto();
        mostrar(app.solicitarHomologacion(codigo, fileName, fileType));
    }

    private static void listarHomologaciones() {
        List<BULL_Homologation> lista = app.listarHomologacionesPendientes();
        if (lista.isEmpty()) {
            System.out.println("[INFO] No hay homologaciones pendientes.");
            return;
        }
        System.out.println("\nHomologaciones pendientes:");
        for (BULL_Homologation h : lista) {
            System.out.printf("  %-12s | %-25s | Archivo: %s (%s) | Estado: %s%n",
                    h.getStudent().getUniversityCode(),
                    h.getStudent().getName() + " " + h.getStudent().getSurnames(),
                    h.getCertificate().getFileName(),
                    h.getCertificate().getFileType(),
                    h.getStatus());
        }
    }

    private static void aprobarHomologacion() {
        System.out.print("Código universitario del estudiante: ");
        String codigo = leerTexto();
        System.out.print("Número de módulo homologado: ");
        int modulo = leerEntero();
        System.out.print("Observación: ");
        String observacion = leerTexto();
        mostrar(app.aprobarHomologacion(codigo, modulo, observacion));
    }

    private static void rechazarHomologacion() {
        System.out.print("Código universitario del estudiante: ");
        String codigo = leerTexto();
        System.out.print("Razón del rechazo: ");
        String razon = leerTexto();
        mostrar(app.rechazarHomologacion(codigo, razon));
    }

    // CONVOCATORIAS
    private static void menuConvocatorias() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Convocatorias ---");
            System.out.println("1. Abrir convocatoria");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            switch (leerEntero()) {
                case 1: abrirConvocatoria(); break;
                case 0: volver = true;       break;
                default: System.out.println("[!] Opción no válida.");
            }
        }
    }

    private static void abrirConvocatoria() {
        System.out.print("ID de la convocatoria (ej: CALL-2026-1): ");
        String callId = leerTexto();
        System.out.print("Año: ");
        int year = leerEntero();
        System.out.print("Periodo (1 o 2): ");
        int period = leerEntero();
        System.out.print("Descripción: ");
        String description = leerTexto();
        mostrar(app.openCall(callId, year, period, description));
    }

    // LECTURA DE CONSOLA
    private static void mostrar(OperationResult result) {
        System.out.println((result.isSuccess() ? "[OK]    " : "[ERROR] ") + result.getMessage());
    }

    private static int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("[!] Ingrese un número entero: ");
            }
        }
    }

    private static double leerDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("[!] Ingrese un número válido (ej: 3.5): ");
            }
        }
    }

    private static String leerTexto() {
        return sc.nextLine().trim();
    }
}