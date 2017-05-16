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
import java.util.Date;
import java.util.List;

/**
 *
 * @author Desarrollo1
 */
public class SortFilesDate {

    public static List<Object[]> archivosPDVS(String directorioBase, List<String> listaFarmacias) {
        List<Object[]> listaObj = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (String rutas : listaFarmacias) {
                File[] files = new File(directorioBase + rutas).listFiles();
                if (files != null) {
                    for (File receta : files) {
                        Object[] objTemp = new Object[4];
                        String fechaFile = receta.getName().split("-")[3];
                        String horaFile = receta.getName().split("-")[4];
                        objTemp[0] = sdf.parse(fechaFile.substring(0, 4) + "-" + fechaFile.substring(4, 6) + "-" + fechaFile.substring(6, 8) + " "
                                + horaFile.substring(0, 2) + ":" + horaFile.substring(2, 4) + ":" + horaFile.substring(4, 6)).getTime();
                        objTemp[1] = receta.getAbsolutePath();
                        objTemp[2] = receta.getName();
                        objTemp[3] = sdf.format(new Date((Long)objTemp[0]));
                        listaObj.add(objTemp);
                    }
                }
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return listaObj;
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
