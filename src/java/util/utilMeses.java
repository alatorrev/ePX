/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Bottago SA
 */
public class utilMeses {

    private List<Object[]> listameses;
    private List<Object[]> listaanios;

    public utilMeses() {
        listameses = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Object[] obj = new Object[2];
            switch (i) {
                case 1:
                    obj[0] = i;
                    obj[1] = "Enero";
                    break;
                case 2:
                    obj[0] = i;
                    obj[1] = "Febrero";
                    break;
                case 3:
                    obj[0] = i;
                    obj[1] = "Marzo";
                    break;
                case 4:
                    obj[0] = i;
                    obj[1] = "Abril";
                    break;
                case 5:
                    obj[0] = i;
                    obj[1] = "Mayo";
                    break;
                case 6:
                    obj[0] = i;
                    obj[1] = "Junio";
                    break;
                case 7:
                    obj[0] = i;
                    obj[1] = "Julio";
                    break;
                case 8:
                    obj[0] = i;
                    obj[1] = "Agosto";
                    break;
                case 9:
                    obj[0] = i;
                    obj[1] = "Septiembre";
                    break;
                case 10:
                    obj[0] = i;
                    obj[1] = "Octubre";
                    break;
                case 11:
                    obj[0] = i;
                    obj[1] = "Noviembre";
                    break;
                case 12:
                    obj[0] = i;
                    obj[1] = "Diciembre";
                    break;
            }
            listameses.add(obj);
        }

        listaanios = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2016; i <= year; i++) {
            Object[] obje = new Object[1];
            obje[0] = i;
            listaanios.add(obje);
        }

    }

    public List<Object[]> getListameses() {
        return listameses;
    }

    public void setListameses(List<Object[]> listameses) {
        this.listameses = listameses;
    }

    public List<Object[]> getListaanios() {
        return listaanios;
    }

    public void setListaanios(List<Object[]> listaanios) {
        this.listaanios = listaanios;
    }

}
