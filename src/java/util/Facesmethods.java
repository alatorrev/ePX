/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.epx.entity.Recurso;
import com.epx.entity.Usuario;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * Bottago S.A.
 *
 */
public class Facesmethods {

    public String getApplicationUri() {
        try {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            URI uri = new URI(ext.getRequestScheme(),
                    null, ext.getRequestServerName(), ext.getRequestServerPort(),
                    ext.getRequestContextPath(), null, null);
            return uri.toASCIIString();
        } catch (URISyntaxException e) {
            throw new FacesException(e);
        }
    }

    public void authenticaticatedUser(Usuario u) throws IOException {
        if (u == null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("Usuario");
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/");
        }
    }

    public void permissionChecker(List<Recurso> listaRecursosUsuarios) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Recurso recurso = new Recurso(request.getRequestURL().toString());
        if (!listaRecursosUsuarios.contains(recurso)) {
            try {
                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                response.sendError(403);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
