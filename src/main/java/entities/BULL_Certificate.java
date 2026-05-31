package entities;

public class BULL_Certificate {

    private final String fileName;
    private final String fileType;
    private final String simulatedPath;

    public BULL_Certificate(String fileName, String fileType, String simulatedPath) {

        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacio.");
        }
        if (fileType == null || fileType.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de archivo no puede estar vacio.");
        }

        this.fileName      = fileName.trim();
        this.fileType      = fileType.trim().toUpperCase();
        this.simulatedPath = simulatedPath;
    }

    public String getFileName()      { return fileName; }
    public String getFileType()      { return fileType; }
    public String getSimulatedPath() { return simulatedPath; }

    @Override
    public String toString() {
        return "Certificate{file='" + fileName + "', type=" + fileType + "}";
    }
}
