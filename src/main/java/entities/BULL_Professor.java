package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BULL_Professor {

    private String idTeaching;
    private String name;
    private String mail;
    private String phoneNumber;
    private List<BULL_Group> groups;

    public BULL_Professor(String idTeaching, String name, String mail) {
        if (idTeaching == null || idTeaching.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID docente no puede estar vacío.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del profesor no puede estar vacío.");
        }
        if (mail == null || !mail.contains("@")) {
            throw new IllegalArgumentException("El correo del profesor no tiene un formato válido.");
        }
        this.idTeaching = idTeaching;
        this.name = name;
        this.mail = mail;
        this.phoneNumber = null;
        this.groups = new ArrayList<>();
    }

    public void addGroup(BULL_Group group) {
        groups.add(group);
    }

    public String getIdTeaching() { return idTeaching; }
    public String getName() { return name; }
    public String getMail() { return mail; }
    public String getPhoneNumber() { return phoneNumber; }
    public List<BULL_Group> getGroups() { return Collections.unmodifiableList(groups); }

    public void setMail(String mail) {
        if (mail == null || !mail.contains("@")) {
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
        }
        this.mail = mail;
    }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return "Professor{id='" + idTeaching + "', name='" + name + "'}";
    }
}
