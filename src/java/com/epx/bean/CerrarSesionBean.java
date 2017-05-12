/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Bottago SA
 */
@ManagedBean
public class CerrarSesionBean implements Serializable {

    public void logout() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Has cerrado sesión", "Gracias por usar nuestro sistema"));
        ec.getFlash().setKeepMessages(true);
        ec.redirect(ec.getRequestContextPath() + "/");
        System.out.println("sesión cerrada");
        
    }
    
     public void logoutOnError() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "¡Atención!", "Por seguridad hemos cerrado tu sesión"));
        ec.getFlash().setKeepMessages(true);
        ec.redirect(ec.getRequestContextPath() + "/");        
    }
     
     public void logoutOnTimeExpired() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "¡Atención!", "Tiempo de inactividad cumplido"));
        ec.getFlash().setKeepMessages(true);
        ec.redirect(ec.getRequestContextPath() + "/");        
    }
}
