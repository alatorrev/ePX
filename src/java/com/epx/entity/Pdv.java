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
public class Pdv implements Serializable{
    private String codigopdv;
    private String loginname;
    private int idusuario;
    private String grupopdv;
    private int idgrupopdv;
    private String parroquia;
    private int idparroquia;
    private String pdv;
    Long coordx;
    Long coordy;

    public String getCodigopdv() {
        return codigopdv;
    }

    public void setCodigopdv(String codigopdv) {
        this.codigopdv = codigopdv;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public String getGrupopdv() {
        return grupopdv;
    }

    public void setGrupopdv(String grupopdv) {
        this.grupopdv = grupopdv;
    }

    public int getIdgrupopdv() {
        return idgrupopdv;
    }

    public void setIdgrupopdv(int idgrupopdv) {
        this.idgrupopdv = idgrupopdv;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public int getIdparroquia() {
        return idparroquia;
    }

    public void setIdparroquia(int idparroquia) {
        this.idparroquia = idparroquia;
    }

    public String getPdv() {
        return pdv;
    }

    public void setPdv(String pdv) {
        this.pdv = pdv;
    }

    public Long getCoordx() {
        return coordx;
    }

    public void setCoordx(Long coordx) {
        this.coordx = coordx;
    }

    public Long getCoordy() {
        return coordy;
    }

    public void setCoordy(Long coordy) {
        this.coordy = coordy;
    }
    
    
}
