/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Bottago SA
 */
public class Rol implements Serializable{
    private int idrol;
    private String descripcion;
    private String usumod;

    public String getUsumod() {
        return usumod;
    }

    public void setUsumod(String usumod) {
        this.usumod = usumod;
    }

    public int getIdrol() {
        return idrol;
    }

    public void setIdrol(int idrol) {
        this.idrol = idrol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
