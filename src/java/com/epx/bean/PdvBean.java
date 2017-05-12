/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import com.epx.dao.PdvDAO;
import com.epx.entity.Pdv;
import com.epx.entity.Usuario;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.Facesmethods;

/**
 *
 * @author Bottago SA
 */
@ManagedBean
@ViewScoped
public class PdvBean implements Serializable {

    private List<Pdv> listadoPdvs = new ArrayList<>();
    private List<Pdv> filteredPdvs;
    private Pdv pdv = new Pdv();
    private PdvDAO daoPdv = new PdvDAO();
    private Usuario sessionUsuario;
    private Facesmethods fcm = new Facesmethods();
    private List<Usuario> listaUsuarios = daoPdv.findAllUsuario();
    private int UsuIdSelected;
    private String UsuTextSelected;
    
    public PdvBean() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            fcm.authenticaticatedUser(sessionUsuario);
            listadoPdvs = daoPdv.findAllPdv();
            listaUsuarios = daoPdv.findAllUsuario();
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
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
    
    public void showAsignaDialog(Pdv p) {
        pdv = p;
    }
    
    public void showUpdateDialog(Pdv p) {
        pdv = p;
    }
    
    public void onCancelDialog() {
        setPdv(new Pdv());
    }

    
    public void AsignaPdvAUsuario() throws SQLException {
        boolean flag = daoPdv.asignarPdvAUsuario(pdv, UsuIdSelected);
        if (flag) {
            listadoPdvs = daoPdv.findAllPdv();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "PDV asignado correctamente"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }
    
    public void ActualizaPdvAUsuario() throws SQLException {
        boolean flag = daoPdv.actualizarPdvAUsuario(pdv, UsuIdSelected);
        if (flag) {
            listadoPdvs = daoPdv.findAllPdv();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "PDV actualizado correctamente"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }

    public List<Pdv> getListadoPdvs() {
        return listadoPdvs;
    }

    public void setListadoPdvs(List<Pdv> listadoPdvs) {
        this.listadoPdvs = listadoPdvs;
    }

    public Pdv getPdv() {
        return pdv;
    }

    public void setPdv(Pdv pdv) {
        this.pdv = pdv;
    }

    public PdvDAO getDaoPdv() {
        return daoPdv;
    }

    public void setDaoPdv(PdvDAO daoPdv) {
        this.daoPdv = daoPdv;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public int getUsuIdSelected() {
        return UsuIdSelected;
    }

    public void setUsuIdSelected(int UsuIdSelected) {
        this.UsuIdSelected = UsuIdSelected;
    }

    public String getUsuTextSelected() {
        return UsuTextSelected;
    }

    public void setUsuTextSelected(String UsuTextSelected) {
        this.UsuTextSelected = UsuTextSelected;
    }

    public List<Pdv> getFilteredPdvs() {
        return filteredPdvs;
    }

    public void setFilteredPdvs(List<Pdv> filteredPdvs) {
        this.filteredPdvs = filteredPdvs;
    }
    
    
}
