/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import com.epx.dao.RecursoDAO;
import com.epx.dao.UsuarioDAO;
import com.epx.entity.Recurso;
import com.epx.entity.Usuario;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import util.Facesmethods;

/**
 *
 * @author Bottago SA
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private Usuario sessionUsuario = new Usuario();
    private String loginname;
    private String contrasena;
    private MenuModel modelMenu;
    private List<String> subMenuList = new ArrayList<>();
    

    public String authenticate() throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        setSessionUsuario(usuarioDAO.loginAction(loginname, contrasena, sessionUsuario));
        if (sessionUsuario != null) {
            if (getSessionUsuario().getActivo()== 0) {
                initMenu(getSessionUsuario());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuario", sessionUsuario);
                return "nuevo";

            }
            if (getSessionUsuario().getActivo()== 1) {
                initMenu(getSessionUsuario());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuario", sessionUsuario);
                if (sessionUsuario.getDescripcionRol().equals("ADMINISTRADOR")) {
                    return "dashboard";
                } else if (sessionUsuario.getDescripcionRol().equals("INDEXADORA")) {
                    return "dashboard";
                } else {
                    return "otros";
                }

            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Usuario o Contraseña incorrecto"));
            RequestContext.getCurrentInstance().update("growl");
            return "incorrecto";
        }
        return null;
    }
    
    public void initMenu(Usuario u) throws SQLException {
        Facesmethods fm = new Facesmethods(); 
        modelMenu = new DefaultMenuModel();
        String urlBase = fm.getApplicationUri();
        RecursoDAO daoRecurso = new RecursoDAO();
        List<Recurso> listaRecursos = daoRecurso.findAllRecursoByRol(u);
        DefaultSubMenu subItemObj = null;
        DefaultMenuItem itemObj;
        DefaultSubMenu temp = new DefaultSubMenu(), aux = new DefaultSubMenu("init");
        for (Recurso objRecurso : listaRecursos) {
            boolean flagExist = subMenuExists(objRecurso.getSubItemLabel());
            if (objRecurso.getSubItemLabel().toUpperCase().equals("NULO")) {
                itemObj = new DefaultMenuItem(objRecurso.getItemLabel());
                itemObj.setUrl(urlBase + objRecurso.getRuta());
                itemObj.setIcon(objRecurso.getItemIcon());
                modelMenu.addElement(itemObj);
            } else {
                if (!flagExist) {
                    subItemObj = new DefaultSubMenu(objRecurso.getSubItemLabel());
                    itemObj = new DefaultMenuItem(objRecurso.getItemLabel());
                    itemObj.setUrl(urlBase + objRecurso.getRuta());
                    itemObj.setIcon(objRecurso.getItemIcon());
                    subItemObj.addElement(itemObj);
                    subItemObj.setIcon(objRecurso.getSubItemIcon());
                    temp = subItemObj;
                    if (!aux.getLabel().equals(subItemObj.getLabel())) {
                        modelMenu.addElement(temp);
                    }
                } else {
                    itemObj = new DefaultMenuItem(objRecurso.getItemLabel());
                    itemObj.setUrl(urlBase + objRecurso.getRuta());
                    itemObj.setIcon(objRecurso.getItemIcon());
                    temp.addElement(itemObj);
                    //temp.setIcon(objRecurso.getSubItemIcon());
                    aux = temp;
                }
            }
        }
        listaRecursos.add(new Recurso(urlBase));
        getSessionUsuario().setListaVista(listaRecursos);
    }

    public boolean subMenuExists(String subItem) {
        if (subMenuList.contains(subItem)) {
            return true;
        } else {
            subMenuList.add(subItem);
            return false;
        }
    }

    public Usuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(Usuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public MenuModel getModelMenu() {
        return modelMenu;
    }

    public void setModelMenu(MenuModel modelMenu) {
        this.modelMenu = modelMenu;
    }
    
    
}
