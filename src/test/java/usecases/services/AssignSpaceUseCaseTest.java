package usecases.services;

import entities.*;
import infrastructure.repositories.BULL_InMemoryGroupRepository;
import infrastructure.repositories.BULL_InMemoryModalityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecases.dto.OperationResult;
import usecases.ports.BULL_GroupRepository;
import usecases.ports.BULL_ModalityRepository;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class AssignSpaceUseCaseTest {

    private BULL_GroupRepository groupRepository;
    private BULL_ModalityRepository modalityRepository;
    private AssignSpaceUseCase assignSpaceUseCase;

    @BeforeEach
    void setUp() {
    groupRepository = new BULL_InMemoryGroupRepository();
    modalityRepository = new BULL_InMemoryModalityRepository();
    assignSpaceUseCase = new AssignSpaceUseCase(groupRepository, modalityRepository);
    }

    private BULL_Group grupoPresencial(int idGroup) {
        BULL_Group grupo = new BULL_Group(idGroup);
        grupo.setMaxCapacity(new BULL_MaxCapacity(30));

        BULL_OnSitePresencial presencial = new BULL_OnSitePresencial();
        presencial.addGroup(grupo);

        modalityRepository.save(presencial);
        groupRepository.save(grupo);
        return grupo;
    }

    private BULL_Group grupoVirtual(int idGroup) {
        BULL_Group grupo = new BULL_Group(idGroup);
        grupo.setMaxCapacity(new BULL_MaxCapacity(30));

        BULL_SynchronousVirtualModality virtual = new BULL_SynchronousVirtualModality();
        virtual.addGroup(grupo);

        modalityRepository.save(virtual);
        groupRepository.save(grupo);
        return grupo;
    }

    //el caso de uso retorna una accion true ya que si es posible asignarle un espacio a un grupo presencial
    @Test
    void asignarEspacio_GrupoPresencial() {
        grupoPresencial(101);

        OperationResult result = assignSpaceUseCase.asignarEspacio(101, "GC2", "212");

        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Espacio asignado exitosamente"));
        assertTrue(result.getMessage().contains("GC2"));
        assertTrue(result.getMessage().contains("212"));
    }

    //Los espacios solo se asignan a los grupos presenciales
    @Test
    void asignarEspacio_GrupoVirtual() {
        grupoVirtual(101);

        OperationResult result = assignSpaceUseCase.asignarEspacio(101, "GC2", "212");

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Solo los grupos presenciales"));

    }

    @Test
    void asignarEspacio_DondeIdEsMenorCero() {
        OperationResult result = assignSpaceUseCase.asignarEspacio(-5, "GC2", "212");

        assertEquals("El ID del grupo debe ser mayor a 0.", result.getMessage());
    }

    @Test
    void agregarEspacio_edificioVacio() {
        OperationResult result = assignSpaceUseCase.asignarEspacio(101, null, "212");

        assertFalse(result.isSuccess());
    }

    @Test
    void agregarEspacio_aulaVacia() {
        OperationResult result = assignSpaceUseCase.asignarEspacio(101, "GC2", null);
        assertFalse(result.isSuccess());
    }

    @Test
    void asignarEspacio_grupoNoExiste_debeRetornarFail() {
        OperationResult result = assignSpaceUseCase.asignarEspacio(999, "Edificio A", "101");

        assertFalse(result.isSuccess());
        assertEquals("No se encontró el grupo con ID 999.", result.getMessage());
    }

    @Test
    void asignarEspacio_grupoSinModalidad() {
        BULL_Group grupo = new BULL_Group(101);
        grupo.setMaxCapacity(new BULL_MaxCapacity(30));
        groupRepository.save(grupo);

        OperationResult result = assignSpaceUseCase.asignarEspacio(101, "Edificio A", "101");

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("no tiene modalidad asignada"));
    }

    @Test
    void asignarEspacio_mismoIdGroup() {
        grupoPresencial(101);

        OperationResult result = assignSpaceUseCase.asignarEspacio(101, "GC2", "212");
        assertTrue(result.isSuccess());

        OperationResult segunda = assignSpaceUseCase.asignarEspacio(101, "GC3", "213");

        assertFalse(segunda.isSuccess());
        assertTrue(segunda.getMessage().contains("ya tiene asignado"));
    }

}
