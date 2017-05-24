/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

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
    private Date today;
    private String fileDisplay;
    private String fileName;
    private List<String> listaPDVS;
    private List<Object[]> listaRecetas;
    private Object[] row;

    public VistaFarmaciaBean() {
        sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
        today = new Date();
        listaRecetasOrdenadas();
        if (!listaRecetas.isEmpty()) {
            Object[] temp = (java.lang.Object[]) listaRecetas.get(0);
            fileDisplay = temp[1].toString();
            fileName = new File(fileDisplay).getName();
        } else {
            fileDisplay = null;
            fileName = "NO EXISTEN RECETAS";
        }
    }

    public void checkAuthorizedViews() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            fcm.authenticaticatedUser(sessionUsuario);
            fcm.permissionChecker(sessionUsuario.getListaVista());
        } catch (Exception e) {
        }
    }

    public void verValor(SelectEvent e){
        Object[] temp=(java.lang.Object[])e.getObject();
        row=temp;
        System.out.println(temp[2].toString());
    }
    
    public void listaRecetasOrdenadas() {
        List<String> listaUsuarioFarmacia = new ArrayList<>();
        listaUsuarioFarmacia.add(sessionUsuario.getLoginname());
        List<Object[]> lista = Filesmethods.archivosPDVS(listaUsuarioFarmacia);
        listaRecetas = Filesmethods.ordenamientoDescendente(lista);
    }

    public List<Object[]> getListaRecetas() {
        return listaRecetas;
    }

    public void setListaRecetas(List<Object[]> listaRecetas) {
        this.listaRecetas = listaRecetas;
    }

    public Object[] getRow() {
        return row;
    }

    public void setRow(Object[] row) {
        this.row = row;
    }
}
