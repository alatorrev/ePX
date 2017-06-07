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
public class Resumen implements Serializable {

    Date fechareceta;
    String codigopdv;
    int conteo;
    String mes;

    public Date getFechareceta() {
        return fechareceta;
    }

    public void setFechareceta(Date fechareceta) {
        this.fechareceta = fechareceta;
    }

    public String getCodigopdv() {
        return codigopdv;
    }

    public void setCodigopdv(String codigopdv) {
        this.codigopdv = codigopdv;
    }

    public int getConteo() {
        return conteo;
    }

    public void setConteo(int conteo) {
        this.conteo = conteo;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

}
