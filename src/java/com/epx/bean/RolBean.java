/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import com.epx.dao.AsignaRecursoDAO;
import com.epx.dao.RolDAO;
import com.epx.entity.AsignaRecurso;
import com.epx.entity.Rol;
import com.epx.entity.Usuario;
import java.io.IOException;
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
public class RolBean implements Serializable {

    private List<Rol> listadoRoles = new ArrayList<>();
    private List<Rol> filteredRols;
    private Rol rol = new Rol();
    private RolDAO daoRol = new RolDAO();
    private Usuario sessionUsuario;
    private final AsignaRecursoDAO daoAsigRecurso = new AsignaRecursoDAO();
    private List<AsignaRecurso> listadoPermisos = new ArrayList<>();
    private List<AsignaRecurso> filteredAccess;
    private Facesmethods fcm = new Facesmethods();

    public RolBean() {
        try {
            listadoRoles = daoRol.findAll();
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }
    }

    public void showEditDialog(Rol r) {
        rol = r;
    }

    public void showPermissionDialog(Rol r) {
        rol = r;
        setListadoPermisos(daoAsigRecurso.recursosAsignadosbyRol(rol.getIdrol()));
    }

    public void showCreateDialog() {
        rol = new Rol();
    }

    public void onCancelDialog() {
        setRol(new Rol());
    }

    public void checkAuthorizedViews() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            fcm.authenticaticatedUser(sessionUsuario);
            fcm.permissionChecker(sessionUsuario.getListaVista());
        } catch (Exception e) {
        }
    }

    public void commitEdit() throws SQLException {
        boolean flag = daoRol.editRol(rol, sessionUsuario);
        if (flag) {
            listadoRoles = daoRol.findAll();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Datos del rol actualizados correctamente"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }

    public void commitCreate() throws SQLException {
        boolean flag = daoRol.createRol(rol, sessionUsuario);
        if (flag) {
            listadoRoles = daoRol.findAll();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Rol creado correctamente"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }

    public void eliminar(Rol r) throws SQLException {
        boolean flag = daoRol.deleteRol(r, sessionUsuario);
        if (flag) {
            listadoRoles = daoRol.findAll();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Rol desactivado correctamente"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }

    public void commitPermission() throws SQLException {
        boolean flag = daoAsigRecurso.saveResourcesbyProfile(getListadoPermisos(), rol.getIdrol(), sessionUsuario);
        if (flag) {
            setListadoPermisos(daoAsigRecurso.recursosAsignadosbyRol(rol.getIdrol()));
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Vistas asignadas correctamente"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }

    public void onCancelPermissionDialog() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(fcm.getApplicationUri() + "/rolCRUD.xhtml");
    }

    public List<Rol> getListadoRoles() {
        return listadoRoles;
    }

    public void setListadoRoles(List<Rol> listadoRoles) {
        this.listadoRoles = listadoRoles;
    }

    public List<Rol> getFilteredRols() {
        return filteredRols;
    }

    public void setFilteredRols(List<Rol> filteredRols) {
        this.filteredRols = filteredRols;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<AsignaRecurso> getListadoPermisos() {
        return listadoPermisos;
    }

    public void setListadoPermisos(List<AsignaRecurso> listadoPermisos) {
        this.listadoPermisos = listadoPermisos;
    }

    public List<AsignaRecurso> getFilteredAccess() {
        return filteredAccess;
    }

    public void setFilteredAccess(List<AsignaRecurso> filteredAccess) {
        this.filteredAccess = filteredAccess;
    }
}
