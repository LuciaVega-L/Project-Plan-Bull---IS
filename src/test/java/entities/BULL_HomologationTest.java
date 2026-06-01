package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BULL_HomologationTest {

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

    private BULL_Certificate createCertificate() {
        return new BULL_Certificate(
                "certificado.pdf",
                "pdf",
                "/tmp/certificado.pdf"
        );
    }

    @Test
    void constructor_creaHomologacionCorrectamente() {
        BULL_Homologation homologation =
                new BULL_Homologation(
                        createStudent(),
                        createCertificate()
                );

        assertEquals(
                HomologationStatus.PENDIENTE,
                homologation.getStatus()
        );

        assertEquals(0, homologation.getApprovedModule());
        assertNull(homologation.getMessage());
        assertTrue(homologation.isPending());
        assertFalse(homologation.isApproved());
    }

    @Test
    void constructor_lanzaExcepcion_cuandoStudentEsNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Homologation(
                        null,
                        createCertificate()
                )
        );

        assertEquals(
                "El estudiante no puede ser nulo.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoCertificateEsNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Homologation(
                        createStudent(),
                        null
                )
        );

        assertEquals(
                "El certificado no puede ser nulo.",
                exception.getMessage()
        );
    }

    @Test
    void approve_actualizaEstadoCorrectamente() {
        BULL_Homologation homologation =
                new BULL_Homologation(
                        createStudent(),
                        createCertificate()
                );

        homologation.approve(2, "Aprobada");

        assertEquals(
                HomologationStatus.APROVADO,
                homologation.getStatus()
        );

        assertEquals(
                2,
                homologation.getApprovedModule()
        );

        assertEquals(
                "Aprobada",
                homologation.getMessage()
        );

        assertTrue(homologation.isApproved());
        assertFalse(homologation.isPending());
    }

    @Test
    void approve_generaMensajePorDefecto_cuandoObservacionEsNula() {
        BULL_Homologation homologation =
                new BULL_Homologation(
                        createStudent(),
                        createCertificate()
                );

        homologation.approve(3, null);

        assertEquals(
                "Homologacion aprobada hasta el modulo 3.",
                homologation.getMessage()
        );
    }

    @Test
    void approve_generaMensajePorDefecto_cuandoObservacionEsVacia() {
        BULL_Homologation homologation =
                new BULL_Homologation(
                        createStudent(),
                        createCertificate()
                );

        homologation.approve(4, "   ");

        assertEquals(
                "Homologacion aprobada hasta el modulo 4.",
                homologation.getMessage()
        );
    }

    @Test
    void approve_lanzaExcepcion_cuandoModuloEsInvalido() {
        BULL_Homologation homologation =
                new BULL_Homologation(
                        createStudent(),
                        createCertificate()
                );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> homologation.approve(0, "Aprobada")
        );

        assertEquals(
                "El numero de modulo homologado debe ser mayor a 0.",
                exception.getMessage()
        );
    }

    @Test
    void approve_lanzaExcepcion_cuandoEstadoNoEsPendiente() {
        BULL_Homologation homologation =
                new BULL_Homologation(
                        createStudent(),
                        createCertificate()
                );

        homologation.approve(2, "Aprobada");

        assertThrows(
                IllegalStateException.class,
                () -> homologation.approve(3, "Otra aprobación")
        );
    }

    @Test
    void reject_actualizaEstadoCorrectamente() {
        BULL_Homologation homologation =
                new BULL_Homologation(
                        createStudent(),
                        createCertificate()
                );

        homologation.reject("Documentación insuficiente");

        assertEquals(
                HomologationStatus.RECHAZADO,
                homologation.getStatus()
        );

        assertEquals(
                "Documentación insuficiente",
                homologation.getMessage()
        );

        assertFalse(homologation.isApproved());
        assertFalse(homologation.isPending());
    }

    @Test
    void reject_lanzaExcepcion_cuandoRazonEsNula() {
        BULL_Homologation homologation =
                new BULL_Homologation(
                        createStudent(),
                        createCertificate()
                );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> homologation.reject(null)
        );

        assertEquals(
                "Debe indicar la razon del rechazo.",
                exception.getMessage()
        );
    }

    @Test
    void reject_lanzaExcepcion_cuandoRazonEsVacia() {
        BULL_Homologation homologation =
                new BULL_Homologation(
                        createStudent(),
                        createCertificate()
                );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> homologation.reject("   ")
        );

        assertEquals(
                "Debe indicar la razon del rechazo.",
                exception.getMessage()
        );
    }

    @Test
    void reject_lanzaExcepcion_cuandoEstadoNoEsPendiente() {
        BULL_Homologation homologation =
                new BULL_Homologation(
                        createStudent(),
                        createCertificate()
                );

        homologation.reject("Rechazada");

        assertThrows(
                IllegalStateException.class,
                () -> homologation.reject("Otro rechazo")
        );
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_Homologation homologation =
                new BULL_Homologation(
                        createStudent(),
                        createCertificate()
                );

        assertEquals(
                "Homologation{student='20201234', status=PENDIENTE, approvedModule=0}",
                homologation.toString()
        );
    }

    @Test
    void getIdHomologation_retornaNull() {
        BULL_Homologation homologation =
                new BULL_Homologation(
                        createStudent(),
                        createCertificate()
                );

        assertNull(homologation.getIdHomologation());
    }
}