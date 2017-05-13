/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.converters;

import com.epx.entity.Medico;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Desarrollo1
 */
@FacesConverter(value = "medicoConverter")
public class MedicoConverter implements Converter {

    private static Map<Object, String> entities = new HashMap<Object, String>();

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        for (Entry<Object, String> entry : entities.entrySet()) {
            if (entry.getValue().equals(string)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return null;
        }
        if (o instanceof Medico) {
            Medico p = (Medico) o;
            if (p.getIdMedico() == null) {
                return null;
            } else {
                return Long.toString(p.getIdMedico());
            }
        } else {
            throw new IllegalArgumentException(" object " + o);
        }
    }

}
