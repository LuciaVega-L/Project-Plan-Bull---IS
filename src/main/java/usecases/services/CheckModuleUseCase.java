package usecases.services;

import entities.BULL_Course;
import entities.BULL_Group;
import entities.BULL_Modality;
import entities.BULL_OnSitePresencial;
import usecases.dto.ModuleOptionDTO;
import usecases.dto.OperationResult;
import usecases.ports.BULL_CourseRepository;
import usecases.ports.BULL_GroupRepository;
import usecases.ports.BULL_ModalityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CheckModuleUseCase {

    private final BULL_CourseRepository courseRepository;
    private final BULL_ModalityRepository modalityRepository;
    private final BULL_GroupRepository groupRepository;

    public CheckModuleUseCase(BULL_CourseRepository courseRepository,
                              BULL_ModalityRepository modalityRepository,
                              BULL_GroupRepository groupRepository) {
        this.courseRepository   = courseRepository;
        this.modalityRepository = modalityRepository;
        this.groupRepository    = groupRepository;
    }


    public OperationResult consultarModulos() {

        // Paso 3: consultar todos los cursos disponibles
        List<BULL_Course> cursos = courseRepository.findAll();

        if (cursos.isEmpty()) {
            return OperationResult.fail("No hay módulos disponibles en este momento.");
        }


        List<BULL_Modality> modalidades = modalityRepository.findAll();

        if (modalidades.isEmpty()) {
            return OperationResult.fail("No hay modalidades configuradas en el sistema.");
        }

        List<BULL_Group> grupos = groupRepository.findAll();

        if (grupos.isEmpty()) {
            return OperationResult.fail("No hay grupos disponibles en este momento.");
        }

        boolean hayGrupoConCupo = false;
        for (int i = 0; i < grupos.size(); i++) {
            if (grupos.get(i).tieneCupoDisponible()) {
                hayGrupoConCupo = true;
                break;
            }
        }

        if (!hayGrupoConCupo) {
            return OperationResult.fail("Todos los grupos están llenos. No es posible inscribirse.");
        }

        return OperationResult.ok(
                "Consulta exitosa. Cursos: " + cursos.size() +
                        ", Modalidades: " + modalidades.size() +
                        ", Grupos con cupo: " + contarGruposConCupo(grupos) + "."
        );
    }


    public List<ModuleOptionDTO> consultarModulosComoLista() {

        List<ModuleOptionDTO> opciones = new ArrayList<>();


        List<BULL_Course> cursos = courseRepository.findAll();


        List<BULL_Modality> modalidades = modalityRepository.findAll();


        List<BULL_Group> grupos = groupRepository.findAll();


        for (int g = 0; g < grupos.size(); g++) {
            BULL_Group grupo = grupos.get(g);


            if (!grupo.estaListoParaOperar() || !grupo.tieneCupoDisponible()) {
                continue;
            }

            BULL_Modality modalidad = grupo.getModality();

            String ubicacion    = null;
            String numAula      = null;
            boolean esPresencial = modalidad instanceof BULL_OnSitePresencial;

            if (esPresencial && grupo.getUbication() != null) {
                ubicacion = grupo.getUbication().getUbication();
                numAula   = grupo.getUbication().getClassroomNum();
            }

            BULL_Course cursoAsociado = buscarCursoPorModalidad(cursos, modalidad);

            // Construir el DTO de opción
            ModuleOptionDTO opcion = new ModuleOptionDTO(
                    cursoAsociado != null ? cursoAsociado.getIdModule()    : -1,
                    cursoAsociado != null ? cursoAsociado.getCourseNumber(): -1,
                    modalidad.getMode(),
                    grupo.getIdGroup(),
                    grupo.getMaxCapacity() != null ? grupo.getMaxCapacity().getCuposRestantes() : 0,
                    grupo.getSchedule() != null ? grupo.getSchedule().getHourlay().toString() : "Sin horario",
                    esPresencial,
                    ubicacion,
                    numAula
            );

            opciones.add(opcion);
        }

        return opciones;
    }


    public List<BULL_Group> consultarGruposPorModulo(int idModule) {

        List<BULL_Group> resultado = new ArrayList<>();

        Optional<BULL_Course> cursoOpt = courseRepository.findByIdModule(idModule);
        if (!cursoOpt.isPresent()) {
            return resultado;
        }


        List<BULL_Modality> modalidades = modalityRepository.findAll();
        List<BULL_Group> todosGrupos    = groupRepository.findAll();

        for (int g = 0; g < todosGrupos.size(); g++) {
            BULL_Group grupo = todosGrupos.get(g);
            if (grupo.estaListoParaOperar() && grupo.tieneCupoDisponible()) {
                resultado.add(grupo);
            }
        }

        return resultado;
    }

    private int contarGruposConCupo(List<BULL_Group> grupos) {
        int count = 0;
        for (int i = 0; i < grupos.size(); i++) {
            if (grupos.get(i).tieneCupoDisponible()) {
                count++;
            }
        }
        return count;
    }


    private BULL_Course buscarCursoPorModalidad(List<BULL_Course> cursos,
                                                BULL_Modality modalidad) {
        if (modalidad == null || modalidad.getSemester() == null) {
            return null;
        }

        for (int c = 0; c < cursos.size(); c++) {
            BULL_Course curso = cursos.get(c);
            List<entities.BULL_Semester> semestres = curso.getSemesters();
            for (int s = 0; s < semestres.size(); s++) {
                if (semestres.get(s).equals(modalidad.getSemester())) {
                    return curso;
                }
            }
        }
        return null;
    }
}