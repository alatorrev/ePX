/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import com.epx.dao.EspecialidadDAO;
import com.epx.dao.MedicoDAO;
import com.epx.dao.ProductoDAO;
import com.epx.dao.TransaccionDAO;
import com.epx.dao.UsuarioDAO;
import com.epx.entity.CabeceraMovimiento;
import com.epx.entity.DetalleMovimiento;
import com.epx.entity.Especialidad;
import com.epx.entity.Medico;
import com.epx.entity.Producto;
import com.epx.entity.Usuario;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import util.Facesmethods;
import util.Filesmethods;
import util.Validador;

/**
 *
 * @author Desarrollo1
 */
@ManagedBean
@ViewScoped
public final class IngresosBean implements Serializable {

    private Usuario sessionUsuario;
    private Facesmethods fcm = new Facesmethods();
    private List<DetalleMovimiento> listaDetalle = new ArrayList<>();
    private List<Object[]> listaNoProcesada = new ArrayList<>();
    private List<Object[]> listaProcesada = new ArrayList<>();
    private List<Object[]> listaDesechada = new ArrayList<>();
    private Object[] row;
    private List<Especialidad> listaEspecialidades = new EspecialidadDAO().findAllEspecialidades();
    private List<Especialidad> EspeIdSelected = new ArrayList<>();
    private List<Producto> listadoProductos = new ArrayList<>();
    private List<Producto> filteredProducto;

    private final List<String> listaPDVS;
    private Medico medico = new Medico();
    private Producto pro = new Producto();
    private Date pantallaDatetime;
    private Date fechaReceta;
    private CabeceraMovimiento cabecera;
    private MedicoDAO daoMedico = new MedicoDAO();
    private ProductoDAO daoProducto = new ProductoDAO();
    private int canPro = 1;
    
    public IngresosBean() {
        sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
        listaPDVS = new UsuarioDAO().findUserPDVS(sessionUsuario);
        listadoProductos = daoProducto.findAllProductoSearch();
        EspeIdSelected.add(listaEspecialidades.get(0));
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
        List<String> msgs = Validador.validarListaDetalleRepetida(listaDetalle, pro);
        if (msgs.size() < 1) {
            DetalleMovimiento det = new DetalleMovimiento();
            det.setSecuencial(listaDetalle.size() + 1);
            det.setProducto(pro);
            det.setCantidad(canPro);
            det.setReferenciaTiempo(new Date());
            det.setIdCabecera(Long.valueOf(listaDetalle.size() + 1));
            listaDetalle.add(det);
            resetDataEntry();
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", msgs.get(0)));
            RequestContext.getCurrentInstance().update("frmVisor:growl");
        }
    }

    public void removerDetalle(DetalleMovimiento det) {
        listaDetalle.remove(det);
        for (int i = 0; i < listaDetalle.size(); i++) {
            listaDetalle.get(i).setSecuencial((i + 1));
        }
    }

    public void resetDataEntry() {
        pro = new Producto();
        medico = new Medico();
        canPro = 1;
    }

    public void procesarRecetas() {
        //metodo para guardar lo procesado cabecera y detalle
        if (listaDetalle.size() > 0) {
            CabeceraMovimiento cab = new CabeceraMovimiento();
            cab.setMedico(medico);
            cab.setEstado(1);
            cab.setFechaReceta(fechaReceta);
            cab.setPantallaInit(pantallaDatetime);
            cab.setIdUsuario(sessionUsuario.getLoginname());
            cab.setListaDetalleProducto(listaDetalle);
            if (!row[4].toString().equals("RAIZ")) {
                //edito existente con estado 1
                cab.setIdCabecera(cabecera.getIdCabecera());
                new TransaccionDAO().editarTransaccion(cab, row, "PROCESADAS", sessionUsuario);
                listaRecetasOrdenadas();

            } else {
                //guardo nuevo con estado 1
                cab.setIdCabecera(cabecera.getIdCabecera());
                new TransaccionDAO().editarTransaccion(cab, row, "PROCESADAS", sessionUsuario);
                listaRecetasOrdenadas();
            }
            RequestContext.getCurrentInstance().execute("PF('wgtVisor').hide();");

        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "El detalle debe tener al menos un producto para ser procesada"));
            RequestContext.getCurrentInstance().update("frmVisor:growl");
        }

    }

    public void desecharRecetas() {
        //metodo para guardar lo rechazado cabecera
        CabeceraMovimiento cab = new CabeceraMovimiento();
        cab.setMedico(medico);
        cab.setEstado(2);
        cab.setFechaReceta(fechaReceta);
        cab.setPantallaInit(pantallaDatetime);
        cab.setIdUsuario(sessionUsuario.getLoginname());
        cab.setListaDetalleProducto(listaDetalle);
        if (!row[4].toString().equals("RAIZ")) {
            //cabecera existe
            cab.setIdCabecera(cabecera.getIdCabecera());
            new TransaccionDAO().desecharTransaccion(cab, row, "DESECHADAS", sessionUsuario);
            listaRecetasOrdenadas();
        } else {
            //cabecera no existe
            cab.setIdCabecera(cabecera.getIdCabecera());
            new TransaccionDAO().desecharTransaccion(cab, row, "DESECHADAS", sessionUsuario);
            listaRecetasOrdenadas();
        }
    }

    public List<Medico> completarMedico(String cadena) {
        return daoMedico.autocompletarmedicos(cadena);
    }

    public List<Producto> completarProducto(String cadena) {
        return daoProducto.autocompletarproducto(cadena);
    }

    public void onOpenDialog() {
        fechaReceta = new Date();
        pantallaDatetime = new Date();
        if (!row[4].toString().equals("RAIZ")) {
            if (row[4].toString().equals("PROCESADAS")) {
                cabecera = new TransaccionDAO().cargarTransaccion(row[2].toString());
            }
            if (row[4].toString().equals("DESECHADAS")) {
                cabecera = new TransaccionDAO().cargarTransaccionDesechada(row[2].toString());
            }
            medico = cabecera.getMedico() == null ? new Medico() : cabecera.getMedico();
            listaDetalle = cabecera.getListaDetalleProducto() == null ? new ArrayList<>() : cabecera.getListaDetalleProducto();
            fechaReceta = cabecera.getFechaReceta() == null ? new Date() : cabecera.getFechaReceta();

        } else {
            cabecera = new TransaccionDAO().cargarTransaccionDesechada(row[2].toString());
            medico = new Medico();
            listaDetalle = new ArrayList<>();
            fechaReceta = cabecera.getFechaReceta() == null ? new Date() : cabecera.getFechaReceta();
        }
    }

    public void onItemSelectUsuario(SelectEvent event) {
        medico = (Medico) event.getObject();
        if (medico == null) {
            medico = new Medico();
        }
    }

    public void commitCreateMedico() throws SQLException {
        List<String> messagesValidation = Validador.validarMedicoFrm(medico, EspeIdSelected);
        if (messagesValidation.size() < 1) {
            boolean flag = daoMedico.createOnFlyMedico(medico, EspeIdSelected, sessionUsuario);
            if (flag) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Nuevo médico añadido correctamente al sistema"));
                RequestContext.getCurrentInstance().update("frmVisor:growl");
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
                RequestContext.getCurrentInstance().update("frmVisor:growl");
            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            for (String msg : messagesValidation) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", msg));
            }
            RequestContext.getCurrentInstance().update("frmVisor:growl");
        }
        setMedico(new Medico());
    }

    public void commitCreateProducto() throws SQLException {
        List<String> messagesValidation = Validador.validarProductoFrm(pro);
        if (messagesValidation.size() < 1) {
            boolean flag = daoProducto.createProducto(pro, sessionUsuario);
            if (flag) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto creado correctamente"));
                RequestContext.getCurrentInstance().update("frmVisor:growl");
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
                RequestContext.getCurrentInstance().update("frmVisor:growl");
            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            for (String msg : messagesValidation) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", msg));
            }
            RequestContext.getCurrentInstance().update("frmVisor:growl");
        }
        setPro(new Producto());
    }

    public void onCancelMedicoCRUDDialog() {
        setMedico(new Medico());
    }

    public void onCancelProductoCRUDDialog() {
        setPro(new Producto());
    }

    public void onItemSelectUsuarioP(SelectEvent event) {
        pro = (Producto) event.getObject();
        if (pro == null) {
            pro = new Producto();
        }
    }

    public void onTabChange() {
        row = null;
    }

    public void listaRecetasOrdenadas() {
        Object[] all = Filesmethods.archivosIndexadores(listaPDVS);
        setListaNoProcesada(Filesmethods.ordenamientoAscendente((List<Object[]>) all[0]));
        setListaProcesada(Filesmethods.ordenamientoDescendente((List<Object[]>) all[1]));
        setListaDesechada(Filesmethods.ordenamientoDescendente((List<Object[]>) all[2]));
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

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Date getFechaReceta() {
        return fechaReceta;
    }

    public void setFechaReceta(Date fechaReceta) {
        this.fechaReceta = fechaReceta;
    }

    public List<Especialidad> getListaEspecialidades() {
        return listaEspecialidades;
    }

    public void setListaEspecialidades(List<Especialidad> listaEspecialidades) {
        this.listaEspecialidades = listaEspecialidades;
    }

    public List<Especialidad> getEspeIdSelected() {
        return EspeIdSelected;
    }

    public void setEspeIdSelected(List<Especialidad> EspeIdSelected) {
        this.EspeIdSelected = EspeIdSelected;
    }   

    public List<Producto> getListadoProductos() {
        return listadoProductos;
    }

    public void setListadoProductos(List<Producto> listadoProductos) {
        this.listadoProductos = listadoProductos;
    }

    public List<Producto> getFilteredProducto() {
        return filteredProducto;
    }

    public void setFilteredProducto(List<Producto> filteredProducto) {
        this.filteredProducto = filteredProducto;
    }
    
    
}
