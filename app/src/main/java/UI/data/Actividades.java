package UI.data;

public class Actividades {
    String nombreActividad;
    String descripcionActividad;
    String fechaInicio;
    String fechaFinal;

    public Actividades(String nombreActividad, String trabajadores, String fechaInicio, String fechaFinal) {
        this.nombreActividad = saltoRanil(nombreActividad);
        this.fechaInicio = obtenerfechasinsegundos(fechaInicio);
        this.fechaFinal = obtenerfechasinsegundos(fechaFinal);
        this.trabajadores = saltoRanil(trabajadores);
    }

    private String saltoRanil(String nombres) {
        if (nombres.contains(" ")) {
            nombres = nombres.replace(" ", "\n");
            return nombres;
        } else {
            return nombres;
        }
    }

    private String obtenerfechasinsegundos(String fecha) {
        if (fecha.length() >= 19) {
            return fecha.substring(5, 16);
        } else {
            return fecha;
        }
    }

    @Override
    public String toString() {
        return "Actividades{" +
                "nombreActividad='" + nombreActividad + '\'' +
                ", descripcionActividad='" + descripcionActividad + '\'' +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", fechaFinal='" + fechaFinal + '\'' +
                ", nameGrupo='" + nameGrupo + '\'' +
                ", nameEvento='" + nameEvento + '\'' +
                ", trabajadores='" + trabajadores + '\'' +
                '}';
    }

    public Actividades() {

    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = saltoRanil(nombreActividad);
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = obtenerfechasinsegundos(fechaInicio);
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = obtenerfechasinsegundos(fechaFinal);
    }

    public String getNameGrupo() {
        return nameGrupo;
    }

    public void setNameGrupo(String nameGrupo) {
        this.nameGrupo = nameGrupo;
    }

    public String getNameEvento() {
        return nameEvento;
    }

    public void setNameEvento(String nameEvento) {
        this.nameEvento = nameEvento;
    }

    public String getTrabajadores() {
        return trabajadores;
    }

    public void setTrabajadores(String trabajadores) {
        this.trabajadores = trabajadores;
    }

    String nameGrupo;
    String nameEvento;

    public Actividades(String nombreActividad, String descripcionActividad, String fechaInicio, String fechaFinal, String nameGrupo, String nameEvento, String trabajadores) {
        this.nombreActividad = nombreActividad;
        this.descripcionActividad = descripcionActividad;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.nameGrupo = nameGrupo;
        this.nameEvento = nameEvento;
        this.trabajadores = trabajadores;
    }

    String trabajadores;
}
