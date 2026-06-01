package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BULL_UbicationTest {

    @Test
    void constructor_creaUbicacionCorrectamente() {
        BULL_Ubication ubication =
                new BULL_Ubication(
                        "A",
                        "101"
                );

        assertEquals(
                "A",
                ubication.getBuilding()
        );

        assertEquals(
                "101",
                ubication.getClassroomNum()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoBuildingEsNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Ubication(
                        null,
                        "101"
                )
        );

        assertEquals(
                "El edificio no puede estar vacío.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoBuildingEsVacio() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Ubication(
                        "   ",
                        "101"
                )
        );

        assertEquals(
                "El edificio no puede estar vacío.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoClassroomEsNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Ubication(
                        "A",
                        null
                )
        );

        assertEquals(
                "El número de aula no puede estar vacío.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoClassroomEsVacio() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Ubication(
                        "A",
                        "   "
                )
        );

        assertEquals(
                "El número de aula no puede estar vacío.",
                exception.getMessage()
        );
    }

    @Test
    void setBuilding_actualizaEdificioCorrectamente() {
        BULL_Ubication ubication =
                new BULL_Ubication(
                        "A",
                        "101"
                );

        ubication.setBuilding("B");

        assertEquals(
                "B",
                ubication.getBuilding()
        );
    }

    @Test
    void setBuilding_lanzaExcepcion_cuandoBuildingEsNulo() {
        BULL_Ubication ubication =
                new BULL_Ubication(
                        "A",
                        "101"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> ubication.setBuilding(null)
        );
    }

    @Test
    void setBuilding_lanzaExcepcion_cuandoBuildingEsVacio() {
        BULL_Ubication ubication =
                new BULL_Ubication(
                        "A",
                        "101"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> ubication.setBuilding(" ")
        );
    }

    @Test
    void setClassroomNum_actualizaAulaCorrectamente() {
        BULL_Ubication ubication =
                new BULL_Ubication(
                        "A",
                        "101"
                );

        ubication.setClassroomNum("202");

        assertEquals(
                "202",
                ubication.getClassroomNum()
        );
    }

    @Test
    void setClassroomNum_lanzaExcepcion_cuandoClassroomEsNulo() {
        BULL_Ubication ubication =
                new BULL_Ubication(
                        "A",
                        "101"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> ubication.setClassroomNum(null)
        );
    }

    @Test
    void setClassroomNum_lanzaExcepcion_cuandoClassroomEsVacio() {
        BULL_Ubication ubication =
                new BULL_Ubication(
                        "A",
                        "101"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> ubication.setClassroomNum(" ")
        );
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_Ubication ubication =
                new BULL_Ubication(
                        "Bloque A",
                        "101"
                );

        assertEquals(
                "Ubication{building='Bloque A', classroom='101'}",
                ubication.toString()
        );
    }
}