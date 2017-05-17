/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import com.epx.dao.MedicoDAO;
import com.epx.dao.ProductoDAO;
import com.epx.dao.UsuarioDAO;
import com.epx.entity.DetalleMovimiento;
import com.epx.entity.Medico;
import com.epx.entity.Producto;
import com.epx.entity.Usuario;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
    private String fileDisplay;
    private String fileName;
    private String dateDisplay;
    private List<DetalleMovimiento> listaDetalle = new ArrayList<>();
    private List<Object[]>listaNoProcesada = new ArrayList<>();
    private List<Object[]>listaProcesada = new ArrayList<>();
    private List<Object[]>listaDesechada = new ArrayList<>();
    private Object[] row;

    
    private final List<String> listaPDVS;
    private Medico medico = new Medico();
    private Producto pro = new Producto();
    private MedicoDAO daoMedico = new MedicoDAO();
    private ProductoDAO daoProducto = new ProductoDAO();
    private String nomPro;
    private int canPro;

    public IngresosBean() {
        sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
        listaPDVS = new UsuarioDAO().findUserPDVS(sessionUsuario);
        listaRecetasOrdenadas();
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
        det.setSecuencial(listaDetalle.size() + 1);
        det.setCantidad(canPro);
        det.setNomPro(nomPro);
        det.setIdCabecera(Long.valueOf(listaDetalle.size() + 1));
        listaDetalle.add(det);
    }

    public void removerDetalle(DetalleMovimiento det) {
        listaDetalle.remove(det);
        for (int i = 0; i < listaDetalle.size(); i++) {
            listaDetalle.get(i).setSecuencial((i + 1));
        }
    }

    public void procesarRecetas() {
        //metodo para guardar lo procesado cabecera y detalle
    }

    public void desecharRecetas() {
        //metodo para guardar lo rechazado cabecera
    }

    public List<Medico> completarMedico(String cadena) {
        return daoMedico.autocompletarmedicos(cadena);
    }

    public List<Producto> completarProducto(String cadena) {
        return daoProducto.autocompletarproducto(cadena);
    }

    public void onOpenDialog() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        today = new Date();
//        dateDisplay = sdf.format(today);
//        fileDisplay = listaRecetas.get(0)[1].toString();
//        fileName = new File(fileDisplay).getName();
    }

    public void onItemSelectUsuario(SelectEvent event) {
        medico = (Medico) event.getObject();
        if (medico == null) {
            medico = new Medico();
        }
    }

    public void onItemSelectUsuarioP(SelectEvent event) {
        pro = (Producto) event.getObject();
        if (pro == null) {
            pro = new Producto();
        }
    }
    
    public void onTabChange(){
        row = null;
    }

    public void listaRecetasOrdenadas() {
        Object[] all =SortFilesDate.archivosIndexadores(listaPDVS);
        setListaNoProcesada(SortFilesDate.ordenamientoAscendente((List<Object[]>)all[0]));
        setListaProcesada(SortFilesDate.ordenamientoDescendente((List<Object[]>)all[1]));
        setListaDesechada(SortFilesDate.ordenamientoDescendente((List<Object[]>)all[2]));
    }

    public Object[] getRow() {
        return row;
    }

    public void setRow(Object[] row) {
        this.row = row;
    }

    public List<DetalleMovimiento> getListaDetalle() {
        return listaDetalle;
    }

    public void setListaDetalle(List<DetalleMovimiento> listaDetalle) {
        this.listaDetalle = listaDetalle;
    }

    public List<Object[]> getListaNoProcesada() {
        return listaNoProcesada;
    }

    public void setListaNoProcesada(List<Object[]> listaNoProcesada) {
        this.listaNoProcesada = listaNoProcesada;
    }

    public List<Object[]> getListaProcesada() {
        return listaProcesada;
    }

    public void setListaProcesada(List<Object[]> listaProcesada) {
        this.listaProcesada = listaProcesada;
    }

    public List<Object[]> getListaDesechada() {
        return listaDesechada;
    }

    public void setListaDesechada(List<Object[]> listaDesechada) {
        this.listaDesechada = listaDesechada;
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

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getDateDisplay() {
        return dateDisplay;
    }

    public void setDateDisplay(String dateDisplay) {
        this.dateDisplay = dateDisplay;
    }

    public String getNomPro() {
        return nomPro;
    }

    public void setNomPro(String nomPro) {
        this.nomPro = nomPro;
    }

    public int getCanPro() {
        return canPro;
    }

    public void setCanPro(int canPro) {
        this.canPro = canPro;
    }

    public Producto getPro() {
        return pro;
    }

    public void setPro(Producto pro) {
        this.pro = pro;
    }

}
