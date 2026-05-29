package entities;

public class BULL_Ubication {

    private String building;
    private String classroomNum;

    public BULL_Ubication(String building, String classroomNum) {
        if (building == null || building.trim().isEmpty()) {
            throw new IllegalArgumentException("El edificio no puede estar vacío.");
        }
        if (classroomNum == null || classroomNum.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de aula no puede estar vacío.");
        }
        this.building = building;
        this.classroomNum = classroomNum;
    }

    public String getBuilding() { return building; }
    public String getClassroomNum() { return classroomNum; }

    public void setBuilding(String building) {
        if (building == null || building.trim().isEmpty()) {
            throw new IllegalArgumentException("El edificio no puede estar vacío.");
        }
        this.building = building;
    }

    public void setClassroomNum(String classroomNum) {
        if (classroomNum == null || classroomNum.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de aula no puede estar vacío.");
        }
        this.classroomNum = classroomNum;
    }

    @Override
    public String toString() {
        return "Ubication{building='" + building + "', classroom='" + classroomNum + "'}";
    }
}
