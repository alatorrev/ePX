/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.entity;

import java.io.Serializable;
import java.util.Objects;

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

    public Recurso() {
    }

    public Recurso(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Recurso other = (Recurso) obj;
        if (!Objects.equals(this.ruta, other.ruta)) {
            return false;
        }
        return true;
    }

    
    
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
