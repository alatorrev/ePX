/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Desarrollo1
 */
public class SortFilesDate {

    public static List<Object> archivosPDVS(String directorioBase,List<String> listaFarmacias) {
        List<Object> listaObj = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (String rutas : listaFarmacias) {
                File[] files = new File(directorioBase+rutas).listFiles();
                for (File receta : files) {
                    Object[] objTemp = new Object[2];
                    String fechaFile = receta.getName().split("-")[3];
                    String horaFile = receta.getName().split("-")[4];
                    objTemp[0] = sdf.parse(fechaFile.substring(0, 4) + "-" + fechaFile.substring(4, 6) + "-" + fechaFile.substring(6, 8) + " "
                            + horaFile.substring(0, 2) + ":" + horaFile.substring(2, 4) + ":" + horaFile.substring(4, 6)).getTime();
                    objTemp[1] = receta.getAbsolutePath();
                    listaObj.add(objTemp);
                }
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return listaObj;
    }

    public static List<Object> ordenamientoAscendente(List<Object> listaObj) {
        Object[] tempj, tempmin = null;
        for (int i = 0; i < listaObj.size(); i++) {
            int min = i;
            for (int j = i + 1; j < listaObj.size(); j++) {
                tempj = (java.lang.Object[]) listaObj.get(j);
                Long secj = (Long) tempj[0];
                tempmin = (java.lang.Object[]) listaObj.get(min);
                Long secmin = (Long) tempmin[0];
                if (secj < secmin) {
                    min = j;
                }
            }
            if (i != min) {
                Object[] tempaux = null;
                tempaux = (java.lang.Object[]) listaObj.get(i);
                listaObj.set(i, listaObj.get(min));
                listaObj.set(min, tempaux);
            }
        }
        return listaObj;
    }

    public static List<Object> ordenamientoDescendente(List<Object> listaObj) {
        Object[] tempj, tempmax = null;
        for (int i = 0; i < listaObj.size(); i++) {
            int max = i;
            for (int j = i + 1; j < listaObj.size(); j++) {
                tempj = (java.lang.Object[]) listaObj.get(j);
                Long secj = (Long) tempj[0];
                tempmax = (java.lang.Object[]) listaObj.get(max);
                Long secmax = (Long) tempmax[0];
                if (secj > secmax) {
                    max = j;
                }
            }
            if (i != max) {
                Object[] tempaux = null;
                tempaux = (java.lang.Object[]) listaObj.get(i);
                listaObj.set(i, listaObj.get(max));
                listaObj.set(max, tempaux);
            }
        }
        return listaObj;
    }
}
