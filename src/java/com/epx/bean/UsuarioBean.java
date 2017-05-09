/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import com.epx.dao.UsuarioDAO;
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
public class UsuarioBean implements Serializable {

    private List<Usuario> listadoUsuarios = new ArrayList<>();
    private List<Usuario> filteredUsers;
    private Usuario sessionUsuario;
    private Usuario usuario = new Usuario();
    private UsuarioDAO daoUsuario = new UsuarioDAO();
    private Facesmethods fcm = new Facesmethods();
    private List<Usuario> listaRoles = daoUsuario.findAllRoles();
    private int RolIdSelected;
    private String RolTextSelected;

    public UsuarioBean() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            fcm.authenticaticatedUser(sessionUsuario);
            listadoUsuarios = daoUsuario.findAllUsuarios();
            listaRoles = daoUsuario.findAllRoles();
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

    public void showEditDialog(Usuario u) {
        usuario = u;
    }
    
    public void showAsignaDialog(Usuario u) {
        usuario = u;
    }

    public void showCreateDialog() {
        usuario = new Usuario();
    }

    public void onCancelDialog() {
        setUsuario(new Usuario());
    }

    public void commitCreate() throws SQLException {
        boolean flag = daoUsuario.createUsuario(usuario, sessionUsuario);
        if (flag) {
            listadoUsuarios = daoUsuario.findAllUsuarios();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Nuevo usuario añadido correctamente al sistema, favor asignarle un rol"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }

    public void commitEdit() throws SQLException {
        boolean flag = daoUsuario.editUsuario(usuario, sessionUsuario);
        if (flag) {
            listadoUsuarios = daoUsuario.findAllUsuarios();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Datos del usuario actualizados correctamente"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }
    
    public void eliminar(Usuario u) throws SQLException {
        boolean flag = daoUsuario.deleteUsuario(u, sessionUsuario);
        if (flag) {
            listadoUsuarios = daoUsuario.findAllUsuarios();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Usuario desactivado"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }

    public void AsignaRol() throws SQLException {
        boolean flag = daoUsuario.AsignaRol(usuario, RolIdSelected);
        if (flag) {
            listadoUsuarios = daoUsuario.findAllUsuarios();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Rol asignado correctamente"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }

    public List<Usuario> getListadoUsuarios() {
        return listadoUsuarios;
    }

    public void setListadoUsuarios(List<Usuario> listadoUsuarios) {
        this.listadoUsuarios = listadoUsuarios;
    }

    public List<Usuario> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<Usuario> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<Usuario> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public int getRolIdSelected() {
        return RolIdSelected;
    }

    public void setRolIdSelected(int RolIdSelected) {
        this.RolIdSelected = RolIdSelected;
    }

    public String getRolTextSelected() {
        return RolTextSelected;
    }

    public void setRolTextSelected(String RolTextSelected) {
        this.RolTextSelected = RolTextSelected;
    }

}
