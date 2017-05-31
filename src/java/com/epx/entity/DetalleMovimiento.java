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
 * @author Desarrollo1
 */
public class DetalleMovimiento implements Serializable {
    private int  secuencial;
    private Long idDetalle;
    private Long idCabecera;
    private Producto producto;
    private int cantidad;
    private Date referenciaTiempo;
    

    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Long getIdCabecera() {
        return idCabecera;
    }

    public void setIdCabecera(Long idCabecera) {
        this.idCabecera = idCabecera;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(int secuencial) {
        this.secuencial = secuencial;
    }

    public Date getReferenciaTiempo() {
        return referenciaTiempo;
    }

    public void setReferenciaTiempo(Date referenciaTiempo) {
        this.referenciaTiempo = referenciaTiempo;
    }
    
}
