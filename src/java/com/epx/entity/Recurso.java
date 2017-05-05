/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.entity;

import java.io.Serializable;

/**
 *
 * @author Bottago SA
 */
public class Recurso implements Serializable {

    int idRecurso;
    int idRol;
    String itemLabel;
    String subItemLabel;
    String ruta;
    String itemIcon;
    String subItemIcon;

    public int getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(int idRecurso) {
        this.idRecurso = idRecurso;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public String getSubItemLabel() {
        return subItemLabel;
    }

    public void setSubItemLabel(String subItemLabel) {
        this.subItemLabel = subItemLabel;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(String itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getSubItemIcon() {
        return subItemIcon;
    }

    public void setSubItemIcon(String subItemIcon) {
        this.subItemIcon = subItemIcon;
    }

}
