/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.converters;

import com.epx.entity.Producto;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Desarrollo1
 */
@FacesConverter(value = "productoConverter")
public class ProductoConverter implements Converter {

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
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        synchronized (entities) {
            if (!entities.containsKey(value)) {
                String uuid = UUID.randomUUID().toString();
                entities.put(value, uuid);
                return uuid;
            } else {
                return entities.get(value);
            }
        }
    }

}
