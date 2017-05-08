/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Bottago SA
 */
public class Usuario implements Serializable {

    String nombres;
    String apellidos;
    String loginname;
    String password;
    String correo;
    Date fecha_crea;
    Date fecha_modif;
    int idRol;
    String descripcionRol;
    int activo;
    List<Recurso> listaVista;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFecha_crea() {
        return fecha_crea;
    }

    public void setFecha_crea(Date fecha_crea) {
        this.fecha_crea = fecha_crea;
    }

    public Date getFecha_modif() {
        return fecha_modif;
    }

    public void setFecha_modif(Date fecha_modif) {
        this.fecha_modif = fecha_modif;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getDescripcionRol() {
        return descripcionRol;
    }

    public void setDescripcionRol(String descripcionRol) {
        this.descripcionRol = descripcionRol;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public List<Recurso> getListaVista() {
        return listaVista;
    }

    public void setListaVista(List<Recurso> listaVista) {
        this.listaVista = listaVista;
    }

}
