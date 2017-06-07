/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.epx.entity.DetalleMovimiento;
import com.epx.entity.Especialidad;
import com.epx.entity.Medico;
import com.epx.entity.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BOTTAGO
 */
public class Validador {

    public static List<String> validarMedicoFrm(Medico medico, List<Especialidad> especialidades) {
        List<String> messages = new ArrayList<>();
        if (medico.getNombres() == null || medico.getNombres().length() < 1) {
            messages.add("Campo nombres obligatorio");
        }
        if (medico.getApellidos() == null || medico.getApellidos().length() < 1) {
            messages.add("Campo apellidos obligatorio");
        }
        if (especialidades.size() < 1) {
            messages.add("Debe elegir al menos una especialidad");
        }
        return messages;
    }

    public static List<String> validarProductoFrm(Producto prod) {
        List<String> messages = new ArrayList<>();
        if ((prod.getMarca() == null || prod.getMarca().length() < 1)
                && (prod.getSustituto() == null || prod.getSustituto().length() < 1)) {
            messages.add("Se debe registrar al menos una marca o sustituto");
        }
        return messages;
    }

    public static List<String> validarListaDetalleRepetida(List<DetalleMovimiento> listaDetalle, Producto pro) {
        List<String> messages = new ArrayList<>();
        for (DetalleMovimiento det : listaDetalle) {
            if (det.getProducto().getIdProducto().intValue() == pro.getIdProducto().intValue()
                    && det.getProducto().getFuente().equals(pro.getFuente())) {
                messages.add("El producto ya se encuentra detallado");
            }
        }
        return messages;
    }

}
