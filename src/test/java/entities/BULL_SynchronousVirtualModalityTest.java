package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BULL_SynchronousVirtualModalityTest {

    @Test
    void getMode_retornaVirtualSincronica() {
        BULL_SynchronousVirtualModality modality =
                new BULL_SynchronousVirtualModality();

        assertEquals(
                "Virtual Sincrónica",
                modality.getMode()
        );
    }

    @Test
    void addGroup_agregaGrupoCorrectamente() {
        BULL_SynchronousVirtualModality modality =
                new BULL_SynchronousVirtualModality();

        BULL_Group group = new BULL_Group(1);

        assertTrue(modality.addGroup(group).isSuccess());

        assertEquals(1, modality.getGroups().size());
    }

    @Test
    void tieneGruposDisponibles_retornaTrue_cuandoExisteGrupoConCupo() {
        BULL_SynchronousVirtualModality modality =
                new BULL_SynchronousVirtualModality();

        BULL_Group group = new BULL_Group(1);
        group.setMaxCapacity(new BULL_MaxCapacity(1));

        modality.addGroup(group);

        assertTrue(modality.tieneGruposDisponibles());
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_SynchronousVirtualModality modality =
                new BULL_SynchronousVirtualModality();

        assertEquals(
                "SynchronousVirtualModality{groups=0}",
                modality.toString()
        );
    }
}