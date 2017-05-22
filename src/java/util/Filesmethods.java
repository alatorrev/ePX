/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.epx.dao.ParametrosDAO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Desarrollo1
 */
public class Filesmethods {

    public static List<Object[]> archivosPDVS(List<String> listaFarmacias) {
        String directorioRaiz = new ParametrosDAO().parametroDirectorioRaiz();
        List<Object[]> listaObj = new ArrayList<>();
        for (String rutas : listaFarmacias) {
            File[] files = new File(directorioRaiz + rutas).listFiles();
            if (files != null) {
                for (File receta : files) {
                    if (receta.isFile()) {
                        listaObj.add(construccionObjeto(receta, "RAIZ"));
                    } else {
                        File[] subDirectorio = new File(receta.getAbsolutePath()).listFiles();
                        for (File sub : subDirectorio) {
                            if (sub.isFile()) {
                                listaObj.add(construccionObjeto(sub, receta.getName()));
                            }
                        }
                    }
                }
            }
        }
        return listaObj;
    }

    public static Object[] archivosIndexadores(List<String> listaFarmacias) {
        List<Object[]> raiz = new ArrayList<>(),
                procesadas = new ArrayList<>(),
                desechadas = new ArrayList<>();
        String directorioRaiz = new ParametrosDAO().parametroDirectorioRaiz();
        Object[] objTempListas = new Object[3];
        for (String rutas : listaFarmacias) {
            File[] files = new File(directorioRaiz + rutas).listFiles();
            if (files != null) {
                for (File receta : files) {
                    if (receta.isFile()) {
                        raiz.add(construccionObjeto(receta, "RAIZ"));
                    } else {
                        File[] subDirectorio = new File(receta.getAbsolutePath()).listFiles();
                        for (File sub : subDirectorio) {
                            if (receta.getName().equals("PROCESADAS")) {
                                procesadas.add(construccionObjeto(sub, receta.getName()));
                            }
                            if (receta.getName().equals("DESECHADAS")) {
                                desechadas.add(construccionObjeto(sub, receta.getName()));
                            }
                        }
                    }
                }
            }
        }
        objTempListas[0] = raiz;
        objTempListas[1] = procesadas;
        objTempListas[2] = desechadas;
        return objTempListas;
    }

    public static Object[] construccionObjeto(File receta, String directorio) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Object[] objTemp = new Object[6];
        try {
            String fechaFile = receta.getName().split("-")[3];
            String horaFile = receta.getName().split("-")[4];
            //fecha transformada en valor Long
            objTemp[0] = sdf.parse(fechaFile.substring(0, 4) + "-" + fechaFile.substring(4, 6) + "-" + fechaFile.substring(6, 8) + " "
                    + horaFile.substring(0, 2) + ":" + horaFile.substring(2, 4) + ":" + horaFile.substring(4, 6)).getTime();
            //Ruta absoluta del fichero de donde se encuentra el archivo
            objTemp[1] = receta.getAbsolutePath();
            //Nombre del archivo ej: bottago.pdf
            objTemp[2] = receta.getName();
            //Cadena de texto con el formato de fecha necesario
            objTemp[3] = sdf.format(new Date((Long) objTemp[0]));
            //Directorio en el que se encuentra el archivo con respecto a una farmacia RAIZ,PROCESADAS,DESECHADAS
            objTemp[4] = directorio.toUpperCase();
            //PDV
            objTemp[5] = receta.getName().split("-")[0];
        } catch (Exception e) {
        }
        return objTemp;
    }

    public static void transferFiletransaccion(String rutaOrigen, String rutaDestino, String opcion, String fileName,Object[] row) {
        File file = new File(rutaOrigen+opcion);
        if (file.exists()) {
            File temp = new File(row[1].toString());
            temp.renameTo(new File(rutaDestino));
        } else {
            file.mkdirs();
            File temp = new File(row[1].toString());
            temp.renameTo(new File(rutaDestino));
        }

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
