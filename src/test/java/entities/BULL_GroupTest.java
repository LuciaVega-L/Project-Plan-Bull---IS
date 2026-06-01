package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import usecases.dto.OperationResult;

import java.util.List;

class BULL_GroupTest {

    private BULL_Student createStudent() {
        return new BULL_Student(
                "20201234",
                "David",
                "Beltran",
                "david@correo.com",
                5,
                "Sistemas",
                false
        );
    }

    private BULL_Registration createRegistration(BULL_Group group) {
        return new BULL_Registration(createStudent(), group);
    }

    @Test
    void constructor_creaGrupoCorrectamente() {
        BULL_Group group = new BULL_Group(10);

        assertEquals(10, group.getIdGroup());
        assertTrue(group.getRegistrations().isEmpty());
    }

    @Test
    void constructor_lanzaExcepcion_cuandoIdEsInvalido() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Group(0)
        );

        assertEquals(
                "El id del grupo debe ser mayor a 0.",
                exception.getMessage()
        );
    }

    @Test
    void setMaxCapacity_asignaCapacidadCorrectamente() {
        BULL_Group group = new BULL_Group(1);
        BULL_MaxCapacity capacity = new BULL_MaxCapacity(20);

        group.setMaxCapacity(capacity);

        assertEquals(capacity, group.getMaxCapacity());
    }

    @Test
    void setUbication_retornaError_cuandoPresencialNoTieneUbicacion() {
        BULL_Group group = new BULL_Group(1);

        OperationResult result = group.setUbication(
                null,
                new BULL_OnSitePresencial()
        );

        assertFalse(result.isSuccess());
        assertEquals(
                "Un grupo presencial debe tener ubicación.",
                result.getMessage()
        );
    }

    @Test
    void setUbication_retornaError_cuandoVirtualTieneUbicacion() {
        BULL_Group group = new BULL_Group(1);

        OperationResult result = group.setUbication(
                new BULL_Ubication("A", "101"),
                new BULL_AsynchronousVirtualModality()
        );

        assertFalse(result.isSuccess());
        assertEquals(
                "Solo grupos presenciales pueden tener ubicación.",
                result.getMessage()
        );
    }

    @Test
    void setUbication_asignaUbicacionCorrectamente() {
        BULL_Group group = new BULL_Group(1);

        BULL_Ubication ubication =
                new BULL_Ubication("A", "101");

        OperationResult result = group.setUbication(
                ubication,
                new BULL_OnSitePresencial()
        );

        assertTrue(result.isSuccess());
        assertEquals(ubication, group.getUbication());
    }

    @Test
    void addRegistration_retornaError_cuandoRegistrationEsNula() {
        BULL_Group group = new BULL_Group(1);
        group.setMaxCapacity(new BULL_MaxCapacity(10));

        OperationResult result =
                group.addRegistration(null);

        assertFalse(result.isSuccess());
        assertEquals(
                "La inscripción no puede ser nula.",
                result.getMessage()
        );
    }

    @Test
    void addRegistration_retornaError_cuandoNoHayCapacidadDefinida() {
        BULL_Group group = new BULL_Group(1);

        OperationResult result =
                group.addRegistration(createRegistration(group));

        assertFalse(result.isSuccess());
        assertEquals(
                "El grupo 1 no tiene cupo máximo definido.",
                result.getMessage()
        );
    }

    @Test
    void addRegistration_retornaError_cuandoNoHayCuposDisponibles() {
        BULL_Group group = new BULL_Group(1);

        BULL_MaxCapacity capacity =
                new BULL_MaxCapacity(1);

        capacity.incrementEnrollment();

        group.setMaxCapacity(capacity);

        OperationResult result =
                group.addRegistration(createRegistration(group));

        assertFalse(result.isSuccess());
        assertEquals(
                "El grupo 1 no tiene cupo disponible.",
                result.getMessage()
        );
    }

    @Test
    void addRegistration_agregaInscripcionCorrectamente() {
        BULL_Group group = new BULL_Group(1);

        BULL_MaxCapacity capacity =
                new BULL_MaxCapacity(10);

        group.setMaxCapacity(capacity);

        BULL_Registration registration =
                createRegistration(group);

        OperationResult result =
                group.addRegistration(registration);

        assertTrue(result.isSuccess());
        assertEquals(1, group.getRegistrations().size());
        assertEquals(1, capacity.getCurrentEnrollment());
    }

    @Test
    void addRegistration_retornaError_cuandoInscripcionYaExiste() {
        BULL_Group group = new BULL_Group(1);

        BULL_MaxCapacity capacity =
                new BULL_MaxCapacity(10);

        group.setMaxCapacity(capacity);

        BULL_Registration registration =
                createRegistration(group);

        group.addRegistration(registration);

        OperationResult result =
                group.addRegistration(registration);

        assertFalse(result.isSuccess());
        assertTrue(
                result.getMessage().contains("Ya existe la inscripción")
        );
    }

    @Test
    void removeRegistration_eliminaInscripcionCorrectamente() {
        BULL_Group group = new BULL_Group(1);

        BULL_MaxCapacity capacity =
                new BULL_MaxCapacity(10);

        group.setMaxCapacity(capacity);

        BULL_Registration registration =
                createRegistration(group);

        group.addRegistration(registration);

        OperationResult result =
                group.removeRegistration(
                        registration.getIdRegistration()
                );

        assertTrue(result.isSuccess());
        assertTrue(group.getRegistrations().isEmpty());
        assertEquals(0, capacity.getCurrentEnrollment());
    }

    @Test
    void removeRegistration_retornaError_cuandoNoExiste() {
        BULL_Group group = new BULL_Group(1);
        group.setMaxCapacity(new BULL_MaxCapacity(10));

        OperationResult result =
                group.removeRegistration("ID-INEXISTENTE");

        assertFalse(result.isSuccess());
    }

    @Test
    void tieneCupoDisponible_retornaFalse_cuandoNoHayCapacidad() {
        BULL_Group group = new BULL_Group(1);

        assertFalse(group.tieneCupoDisponible());
    }

    @Test
    void tieneCupoDisponible_retornaTrue_cuandoHayCupos() {
        BULL_Group group = new BULL_Group(1);

        group.setMaxCapacity(
                new BULL_MaxCapacity(5)
        );

        assertTrue(group.tieneCupoDisponible());
    }

    @Test
    void getRegistrations_retornaListaInmodificable() {
        BULL_Group group = new BULL_Group(1);

        List<BULL_Registration> registrations =
                group.getRegistrations();

        assertThrows(
                UnsupportedOperationException.class,
                () -> registrations.add(null)
        );
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_Group group = new BULL_Group(7);

        assertEquals(
                "Group{idGroup=7, inscritos=0}",
                group.toString()
        );
    }
}