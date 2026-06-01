package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BULL_AsynchronousVirtualModalityTest {

    @Test
    void getMode_retornaVirtualAsincronica() {
        BULL_AsynchronousVirtualModality modality =
                new BULL_AsynchronousVirtualModality();

        assertEquals(
                "Virtual Asincronica",
                modality.getMode()
        );
    }

    @Test
    void addGroup_agregaGrupoCorrectamente() {
        BULL_AsynchronousVirtualModality modality =
                new BULL_AsynchronousVirtualModality();

        BULL_Group group = new BULL_Group(1);

        assertTrue(modality.addGroup(group).isSuccess());

        assertEquals(1, modality.getGroups().size());
    }

    @Test
    void tieneGruposDisponibles_retornaTrue_cuandoExisteGrupoConCupo() {
        BULL_AsynchronousVirtualModality modality =
                new BULL_AsynchronousVirtualModality();

        BULL_Group group = new BULL_Group(1);
        group.setMaxCapacity(new BULL_MaxCapacity(1));

        modality.addGroup(group);

        assertTrue(modality.tieneGruposDisponibles());
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_AsynchronousVirtualModality modality =
                new BULL_AsynchronousVirtualModality();

        assertEquals(
                "AsynchronousVirtualModality{groups=0}",
                modality.toString()
        );
    }
}