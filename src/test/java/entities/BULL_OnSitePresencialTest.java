package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

class BULL_OnSitePresencialTest {

    @Test
    void getMode_retornaPresencial() {
        BULL_OnSitePresencial modality = new BULL_OnSitePresencial();

        assertEquals("Presencial", modality.getMode());
    }

    @Test
    void addGroup_agregaGrupoCorrectamente() {
        BULL_OnSitePresencial modality = new BULL_OnSitePresencial();
        BULL_Group group = new BULL_Group(1);

        assertTrue(modality.addGroup(group).isSuccess());

        assertEquals(1, modality.getGroups().size());
    }

    @Test
    void addGroup_retornaError_cuandoGrupoEsNulo() {
        BULL_OnSitePresencial modality = new BULL_OnSitePresencial();

        assertFalse(modality.addGroup(null).isSuccess());
    }

    @Test
    void addGroup_retornaError_cuandoGrupoYaExiste() {
        BULL_OnSitePresencial modality = new BULL_OnSitePresencial();

        BULL_Group group1 = new BULL_Group(1);
        BULL_Group group2 = new BULL_Group(1);

        modality.addGroup(group1);

        assertFalse(modality.addGroup(group2).isSuccess());
    }

    @Test
    void getGroups_retornaListaInmodificable() {
        BULL_OnSitePresencial modality = new BULL_OnSitePresencial();

        List<BULL_Group> groups = modality.getGroups();

        assertThrows(
                UnsupportedOperationException.class,
                () -> groups.add(new BULL_Group(1))
        );
    }

    @Test
    void tieneGruposDisponibles_retornaFalse_cuandoNoHayGrupos() {
        BULL_OnSitePresencial modality = new BULL_OnSitePresencial();

        assertFalse(modality.tieneGruposDisponibles());
    }

    @Test
    void tieneGruposDisponibles_retornaTrue_cuandoExisteGrupoConCupo() {
        BULL_OnSitePresencial modality = new BULL_OnSitePresencial();

        BULL_Group group = new BULL_Group(1);
        group.setMaxCapacity(new BULL_MaxCapacity(1));

        modality.addGroup(group);

        assertTrue(modality.tieneGruposDisponibles());
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_OnSitePresencial modality = new BULL_OnSitePresencial();

        assertEquals(
                "OnSitePresencial{groups=0}",
                modality.toString()
        );
    }
}