/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import LazyLoads.FarmaciaLazyLoads;
import com.epx.entity.CabeceraMovimiento;
import com.epx.entity.Usuario;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import util.Facesmethods;
import util.Filesmethods;

/**
 *
 * @author Bottago SA
 */
@ManagedBean
@ViewScoped
public class VistaFarmaciaBean implements Serializable {

    private Usuario sessionUsuario;
    private Facesmethods fcm = new Facesmethods();
    private LazyDataModel<CabeceraMovimiento> listaRecetas;
    private CabeceraMovimiento row;

    public VistaFarmaciaBean() {
        sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
        listaRecetasOrdenadas();
    }

    public void checkAuthorizedViews() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            fcm.authenticaticatedUser(sessionUsuario);
            fcm.permissionChecker(sessionUsuario.getListaVista());
        } catch (Exception e) {
        }
    }

    public void verValor(SelectEvent e) {
        row = (CabeceraMovimiento) e.getObject();
    }

    public void listaRecetasOrdenadas() {
        listaRecetas = new FarmaciaLazyLoads(sessionUsuario);
    }

    public LazyDataModel<CabeceraMovimiento> getListaRecetas() {
        return listaRecetas;
    }

    public void setListaRecetas(LazyDataModel<CabeceraMovimiento> listaRecetas) {
        this.listaRecetas = listaRecetas;
    }

    public CabeceraMovimiento getRow() {
        return row;
    }

    public void setRow(CabeceraMovimiento row) {
        this.row = row;
    }

}
