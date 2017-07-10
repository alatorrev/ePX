/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.epx.entity.CabeceraMovimiento;
import java.io.File;
import java.util.List;

/**
 *
 * @author Desarrollo1
 */
public class Filesmethods {

    public static boolean transferFiletransaccion(String rutaOrigen, String rutaDestino, String opcion, String fileName, CabeceraMovimiento row) {
        boolean succed=false;
        File file = new File(rutaOrigen + opcion);
        if (file.exists()) {
            File temp = new File(row.getRutaArchivoDestino());
            succed=temp.renameTo(new File(rutaDestino));
        } else {
            file.mkdirs();
            File temp = new File(row.getRutaArchivoDestino());
            succed=temp.renameTo(new File(rutaDestino));
        }
        return succed;
    }

    public static List<Object[]> ordenamientoAscendente(List<Object[]> listaObj) {
        for (int i = 0; i < listaObj.size(); i++) {
            int min = i;
            for (int j = i + 1; j < listaObj.size(); j++) {
                Long secj = (Long) listaObj.get(j)[0];
                Long secmin = (Long) listaObj.get(min)[0];
                if (secj < secmin) {
                    min = j;
                }
            }
            if (i != min) {
                Object[] tempaux = listaObj.get(i);
                listaObj.set(i, listaObj.get(min));
                listaObj.set(min, tempaux);
            }
        }
        return listaObj;
    }

    public static List<Object[]> ordenamientoDescendente(List<Object[]> listaObj) {
        for (int i = 0; i < listaObj.size(); i++) {
            int max = i;
            for (int j = i + 1; j < listaObj.size(); j++) {
                Long secj = (Long) listaObj.get(j)[0];
                Long secmax = (Long) listaObj.get(max)[0];
                if (secj > secmax) {
                    max = j;
                }
            }
            if (i != max) {
                Object[] tempaux = listaObj.get(i);
                listaObj.set(i, listaObj.get(max));
                listaObj.set(max, tempaux);
            }
        }
        return listaObj;
    }
}
