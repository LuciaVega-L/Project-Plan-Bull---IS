# Project-Plan-Bull---IS
# 🐂 Plan Bull — Sistema de Gestión Académica

> Sistema de planificación académica para la **Universidad de los Llanos (Unillanos)**, desarrollado con **Clean Architecture** en Java para optimizar los procesos de matrícula, gestión de cursos y programación semestral del Plan BULL.

---

## Descripción

Plan Bull es una aplicación automatiza los procesos académicos clave: desde la apertura de convocatorias y la matrícula de estudiantes hasta la homologación de materias y la asignación de espacios físicos. El sistema está diseñado bajo principios de arquitectura limpia, garantizando una separación clara entre lógica de negocio, casos de uso e infraestructura.

---

## Funcionalidades principales

| Módulo | Descripción |
|---|---|
| **Convocatoria** | Anunciar y abrir convocatorias de matrícula por semestre |
| **Calendario** | Establecer el calendario académico semestral |
| **Gestión de cursos** | Crear, modificar y consultar cursos y grupos |
| **Registro** | Registro y cancelación de inscripciones de estudiantes |
| **Verificación de cursos** | Validar requisitos y disponibilidad de cursos |
| **Asignación de espacios** | Asignar aulas y ubicaciones a grupos |
| **Carga de notas** | Registrar calificaciones por grupo y profesor |
| **Homologación** | Solicitar y validar homologaciones de materias |
| **Lista de profesores** | Cargar y gestionar el listado de docentes |

---

## Arquitectura

El proyecto sigue **Clean Architecture**, organizado en las siguientes capas:

```
src/main/java/
├── entities/                  # Entidades del dominio (Student, Course, Group, etc.)
├── usecases/
│   ├── ports/                 # Interfaces de repositorios y servicios
│   ├── services/              # Casos de uso (lógica de negocio)
│   └── dto/                   # Objetos de transferencia de datos
├── infrastructure/
│   └── repositories/          # Implementaciones en memoria de los repositorios
└── adapters.ui/
    └── Main.java              # Interfaz de usuario (JavaFX)
```

### Entidades principales

- `BULL_Student` — Estudiante universitario
- `BULL_Professor` — Docente
- `BULL_Course` — Materia/asignatura
- `BULL_Group` — Grupo de una materia
- `BULL_Registration` — Inscripción de un estudiante
- `BULL_Homologation` — Solicitud de homologación
- `BULL_Schedule` — Horario
- `BULL_Semester` — Semestre académico
- `BULL_Modality` — Modalidad (presencial, virtual síncrona/asíncrona)
- `BULL_Ubication` — Espacio/aula asignada

---

## Tecnologías

- **Java 21**
- **JavaFX 21.0.8** — Interfaz gráfica de escritorio
- **Maven** — Gestión de dependencias y construcción
- **JUnit 5 / TestNG** — Pruebas unitarias

---

## Instalación y ejecución

### Clonar el repositorio

```bash
git clone https://github.com/<tu-usuario>/Project-Plan-Bull---IS.git
cd Project-Plan-Bull---IS
```

### Compilar el proyecto

```bash
mvn clean compile
```

### Ejecutar la aplicación

```bash
mvn javafx:run
```

### Ejecutar las pruebas

```bash
mvn test
```

---

## Documentación

La carpeta `Docs/Artifacts/` contiene diagramas de diseño del sistema en formato `.drawio` y `.xml`:

- **Diagrama de estados** 
- **Diagramas de secuencia** 
- **Diagramas de colaboración** 

---

## Pruebas

Las pruebas unitarias cubren tanto las entidades del dominio como los casos de uso:

```
src/test/java/
├── entities/          # Tests de entidades (Student, Course, Group, Homologation, etc.)
└── usecases/services/ # Tests de casos de uso (matrícula, homologación, asignación, etc.)
```

---

