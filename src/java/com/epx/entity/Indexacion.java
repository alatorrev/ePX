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
public class Indexacion implements Serializable {

    String fechascanner;
    String codigopdv;
    String codigoscanner;
    int idmedico;
    int idproducto;
    String fuenteproducto;
    int secuencial;
    int px;

//Variables para Productos
    String marca;
    String sustituto;
    String forma;
    String concentracion;

//Variables para Médicos
    String nombres;
    String apellidos;
    String direccion;
    String especialidad;

    public String getFechascanner() {
        return fechascanner;
    }

    public void setFechascanner(String fechascanner) {
        this.fechascanner = fechascanner;
    }

    public String getCodigopdv() {
        return codigopdv;
    }

    public void setCodigopdv(String codigopdv) {
        this.codigopdv = codigopdv;
    }

    public String getCodigoscanner() {
        return codigoscanner;
    }

    public void setCodigoscanner(String codigoscanner) {
        this.codigoscanner = codigoscanner;
    }

    public int getIdmedico() {
        return idmedico;
    }

    public void setIdmedico(int idmedico) {
        this.idmedico = idmedico;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public String getFuenteproducto() {
        return fuenteproducto;
    }

    public void setFuenteproducto(String fuenteproducto) {
        this.fuenteproducto = fuenteproducto;
    }

    public int getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(int secuencial) {
        this.secuencial = secuencial;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getSustituto() {
        return sustituto;
    }

    public void setSustituto(String sustituto) {
        this.sustituto = sustituto;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

}
