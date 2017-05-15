/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import com.epx.dao.MedicoDAO;
import com.epx.dao.UsuarioDAO;
import com.epx.entity.DetalleMovimiento;
import com.epx.entity.Medico;
import com.epx.entity.Producto;
import com.epx.entity.Usuario;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import util.Facesmethods;
import util.SortFilesDate;

/**
 *
 * @author Desarrollo1
 */
@ManagedBean
@ViewScoped
public final class IngresosBean implements Serializable {

    private Usuario sessionUsuario;
    private Facesmethods fcm = new Facesmethods();
    private Date today;
    private String directorioBase = "C:\\Users\\Desarrollo1\\Desktop\\receta\\PDV\\";
    private String fileDisplay;
    private String fileName;
    private List<DetalleMovimiento> listaDetalle = new ArrayList<>();
    private List<String> listaPDVS;
    private final List<Object> listaRecetas;
    private Producto producto;
    private Medico medico = new Medico();
    private MedicoDAO daoMedico = new MedicoDAO();

    public IngresosBean() {
        sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
        today = new Date();
        listaPDVS = new UsuarioDAO().findUserPDVS(sessionUsuario);
        listaRecetas = listaRecetasOrdenadas();
        if (!listaRecetas.isEmpty()) {
            Object[] temp = (java.lang.Object[]) listaRecetas.get(0);
            fileDisplay = temp[1].toString();
            fileName = new File(fileDisplay).getName();
        } else {
            fileDisplay = null;
            fileName = "NO EXISTEN RECETAS";
        }
    }

    public void checkAuthorizedViews() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            fcm.authenticaticatedUser(sessionUsuario);
            fcm.permissionChecker(sessionUsuario.getListaVista());
        } catch (Exception e) {
        }
    }

    public void agregarDetalle() {
        DetalleMovimiento det = new DetalleMovimiento();
        det.setIdCabecera(Long.valueOf(listaDetalle.size() + 1));
        listaDetalle.add(det);
    }

    public void removerDetalle(DetalleMovimiento det) {
        listaDetalle.remove(det);
        for (int i = 0; i < listaDetalle.size(); i++) {
            listaDetalle.get(i).setIdCabecera(Long.valueOf(i + 1));
        }
    }

    public void procesarRecetas() {
        //metodo para guardar lo procesado cabecera y detalle
        nextPDF();
    }

    public void desecharRecetas() {
        //metodo para guardar lo rechazado cabecera
        nextPDF();
    }

    public void nextPDF() {
        if (!listaRecetas.isEmpty()) {
            listaRecetas.remove(0);
            Object[] temp = (java.lang.Object[]) listaRecetas.get(0);
            fileDisplay = temp[1].toString();
            fileName = new File(fileDisplay).getName();
            today = new Date();
        } else {
            fileDisplay = null;
            fileName = "RECARGA PARA VER MÁS!";
        }
    }

    public List<Medico> completarMedico(String cadena) {
        return daoMedico.autocompletarmedicos(cadena);
    }

    public void onItemSelectUsuario(SelectEvent event) {
        medico = (Medico) event.getObject();
        if (medico == null) {
            medico = new Medico();
        }
    }

    public List<Object> listaRecetasOrdenadas() {
        List<Object> lista = SortFilesDate.archivosPDVS(directorioBase, listaPDVS);
        return SortFilesDate.ordenamientoAscendente(lista);
    }

    public List<DetalleMovimiento> getListaDetalle() {
        return listaDetalle;
    }

    public void setListaDetalle(List<DetalleMovimiento> listaDetalle) {
        this.listaDetalle = listaDetalle;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public String getFileDisplay() {
        return fileDisplay;
    }

    public void setFileDisplay(String fileDisplay) {
        this.fileDisplay = fileDisplay;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

}
