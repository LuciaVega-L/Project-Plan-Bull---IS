package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BULL_Student {

    private String universityCode;
    private String name;
    private String surnames;
    private String mail;
    private String phoneNumber;
    private int semester;
    private String program;
    private boolean specialCondition;
    private List<BULL_Registration> registrations;

    public BULL_Student(String universityCode, String name, String surnames,
                   String mail, int semester, String program, boolean specialCondition) {
        if (universityCode == null || universityCode.trim().isEmpty()) {
            throw new IllegalArgumentException("El código universitario no puede estar vacío.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (surnames == null || surnames.trim().isEmpty()) {
            throw new IllegalArgumentException("Los apellidos no pueden estar vacíos.");
        }
        if (mail == null || !mail.contains("@")) {
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
        }
        if (semester < 1) {
            throw new IllegalArgumentException("El semestre debe ser mayor o igual a 1.");
        }
        this.universityCode = universityCode;
        this.name = name;
        this.surnames = surnames;
        this.mail = mail;
        this.phoneNumber = null;
        this.semester = semester;
        this.program = program;
        this.specialCondition = specialCondition;
        this.registrations = new ArrayList<>();
    }

    public void addRegistration(BULL_Registration registration) {
        registrations.add(registration);
    }

    public boolean tieneInscripcionActiva() {
        for (int i = 0; i < registrations.size(); i++) {
            if (registrations.get(i).estaActiva()) {
                return true;
            }
        }
        return false;
    }

    public String getUniversityCode() { return universityCode; }
    public String getName() { return name; }
    public String getSurnames() { return surnames; }
    public String getMail() { return mail; }
    public String getPhoneNumber() { return phoneNumber; }
    public int getSemester() { return semester; }
    public String getProgram() { return program; }
    public boolean isSpecialCondition() { return specialCondition; }
    public List<BULL_Registration> getRegistrations() { return Collections.unmodifiableList(registrations); }

    public void setMail(String mail) {
        if (mail == null || !mail.contains("@")) {
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
        }
        this.mail = mail;
    }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setSpecialCondition(boolean specialCondition) { this.specialCondition = specialCondition; }

    @Override
    public String toString() {
        return "Student{code='" + universityCode + "', name='" + name + " " + surnames + "'}";
    }
}
