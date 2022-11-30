package com.example.appcalendario.Objetos;

public class horario {

    String id_horario, uid_usuario, correo_usuario, fecha_hora_actual, Sesion, Paciente, Informacion, fecha_horario, hora_horario, estado;

    public horario(){

    }

    public horario(String id_horario, String uid_usuario, String correo_usuario, String fecha_hora_actual, String Sesion, String Paciente, String Informacion, String fecha_horario, String hora_horario, String estado){

        this.id_horario = id_horario;
        this.uid_usuario = uid_usuario;
        this.correo_usuario = correo_usuario;
        this.fecha_hora_actual = fecha_hora_actual;
        this.Sesion = Sesion;
        this.Paciente = Paciente;
        this.Informacion = Informacion;
        this.fecha_horario = fecha_horario;
        this.hora_horario = hora_horario;
        this.estado = estado;

    }

    public String getFecha_hora_actual() {
        return fecha_hora_actual;
    }

    public void setFecha_hora_actual(String fecha_hora_actual) {
        this.fecha_hora_actual = fecha_hora_actual;
    }

    public String getUid_usuario() {
        return uid_usuario;
    }

    public void setUid_usuario(String uid_usuario) {
        this.uid_usuario = uid_usuario;
    }

    public String getCorreo_usuario() {
        return correo_usuario;
    }

    public void setCorreo_usuario(String correo_usuario) {
        this.correo_usuario = correo_usuario;
    }

    public String getId_horario() {
        return id_horario;
    }

    public void setId_horario(String id_horario) {
        this.id_horario = id_horario;
    }

    public String getSesion() {
        return Sesion;
    }

    public void setSesion(String sesion) {
        Sesion = sesion;
    }

    public String getPaciente() {
        return Paciente;
    }

    public void setPaciente(String paciente) {
        Paciente = paciente;
    }

    public String getInformacion() {
        return Informacion;
    }

    public void setInformacion(String informacion) {
        Informacion = informacion;
    }

    public String getFecha_horario() {
        return fecha_horario;
    }

    public void setFecha_horario(String fecha_horario) {
        this.fecha_horario = fecha_horario;
    }

    public String getHora_horario() {
        return hora_horario;
    }

    public void setHora_horario(String hora_horario) {
        this.hora_horario = hora_horario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
