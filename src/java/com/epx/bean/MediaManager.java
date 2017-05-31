/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Desarrollo1
 */
@ManagedBean
@SessionScoped
public class MediaManager implements Serializable {

    public StreamedContent getStream() throws FileNotFoundException, IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE || context.getCurrentPhaseId() == PhaseId.INVOKE_APPLICATION) {
            return new DefaultStreamedContent();
        } else {
            String ruta = context.getExternalContext().getRequestParameterMap().get("ruta");
            DefaultStreamedContent dsc = new DefaultStreamedContent(
                    new FileInputStream(new File(ruta)), "application/pdf");
            dsc.setName(new File(ruta).getName());
            return dsc;
        }
    }
}
