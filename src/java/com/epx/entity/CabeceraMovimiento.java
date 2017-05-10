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
public class CabeceraMovimiento implements Serializable {
    private Long idCabecera;
    private Long idMedico;
    private String fuente;
    private String codigoPDV;
    private Long idUsuario;
    private String rutaArchivoDestino;
    private Date fechaReceta;
    private Date fechaArchivo;
    private Date fechaRegistro;
    private Date pantallaInit;
    private boolean estado;

    public Long getIdCabecera() {
        return idCabecera;
    }

    public void setIdCabecera(Long idCabecera) {
        this.idCabecera = idCabecera;
    }

    public Long getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Long idMedico) {
        this.idMedico = idMedico;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getCodigoPDV() {
        return codigoPDV;
    }

    public void setCodigoPDV(String codigoPDV) {
        this.codigoPDV = codigoPDV;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getRutaArchivoDestino() {
        return rutaArchivoDestino;
    }

    public void setRutaArchivoDestino(String rutaArchivoDestino) {
        this.rutaArchivoDestino = rutaArchivoDestino;
    }

    public Date getFechaReceta() {
        return fechaReceta;
    }

    public void setFechaReceta(Date fechaReceta) {
        this.fechaReceta = fechaReceta;
    }

    public Date getFechaArchivo() {
        return fechaArchivo;
    }

    public void setFechaArchivo(Date fechaArchivo) {
        this.fechaArchivo = fechaArchivo;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getPantallaInit() {
        return pantallaInit;
    }

    public void setPantallaInit(Date pantallaInit) {
        this.pantallaInit = pantallaInit;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    
}
