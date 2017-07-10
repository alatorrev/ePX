/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import LazyLoads.IndexadoresLazyLoad;
import LazyLoads.ProductosLazyLoad;
import com.epx.dao.EspecialidadDAO;
import com.epx.dao.MedicoDAO;
import com.epx.dao.ProductoDAO;
import com.epx.dao.TransaccionDAO;
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
import org.primefaces.model.LazyDataModel;
import util.Facesmethods;
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
    private LazyDataModel<CabeceraMovimiento> lazyModelNoProcesada;
    private LazyDataModel<CabeceraMovimiento> lazyModelProcesada;
    private LazyDataModel<CabeceraMovimiento> lazyModelDesechada;
    private CabeceraMovimiento row;
    private List<Especialidad> listaEspecialidades;
    private List<Especialidad> EspeIdSelected = new ArrayList<>();
    private LazyDataModel<Producto> listadoProductos;

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
        listaEspecialidades = new EspecialidadDAO().findAllEspecialidades();
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
            if (cab.getMedico().getIdMedico() != null) {
                if (row.getEstado() != 0) {
                    //edito existente con estado 1
                    cab.setIdCabecera(cabecera.getIdCabecera());
                    if (new TransaccionDAO().editarTransaccion(cab, row, "PROCESADAS", sessionUsuario, true)) {
                        listaRecetasOrdenadas();
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Transacción completada correctamente"));
                        RequestContext.getCurrentInstance().update("frm:notificacion");
                    } else {
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
                        RequestContext.getCurrentInstance().update("frm:notificacion");
                    }

                } else {
                    //guardo nuevo con estado 1
                    cab.setIdCabecera(cabecera.getIdCabecera());
                    if (new TransaccionDAO().editarTransaccion(cab, row, "PROCESADAS", sessionUsuario, false)) {
                        listaRecetasOrdenadas();
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Transacción completada correctamente"));
                        RequestContext.getCurrentInstance().update("frm:notificacion");
                    } else {
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
                        RequestContext.getCurrentInstance().update("frm:notificacion");
                    }
                }
                RequestContext.getCurrentInstance().execute("PF('wgtVisor').hide();");
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Debe ingresar un médico con un identificador válido"));
                RequestContext.getCurrentInstance().update("frmVisor:growl");
            }
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
        if (row.getEstado() != 0) {
            //cabecera existe
            cab.setIdCabecera(cabecera.getIdCabecera());
            if (new TransaccionDAO().desecharTransaccion(cab, row, "DESECHADAS", sessionUsuario)) {
                listaRecetasOrdenadas();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Transacción completada correctamente"));
                RequestContext.getCurrentInstance().update("frm:notificacion");
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
                RequestContext.getCurrentInstance().update("frm:notificacion");
            }

        } else {
            //cabecera no existe
            cab.setIdCabecera(cabecera.getIdCabecera());
            if (new TransaccionDAO().desecharTransaccion(cab, row, "DESECHADAS", sessionUsuario)) {
                listaRecetasOrdenadas();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Transacción completada correctamente"));
                RequestContext.getCurrentInstance().update("frm:notificacion");
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
                RequestContext.getCurrentInstance().update("frm:notificacion");
            }
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
        if (row.getEstado() != 0) {
            if (row.getEstado() == 1) {
                cabecera = new TransaccionDAO().cargarTransaccion(row.getNombreArchivo());
            }
            if (row.getEstado() == 2) {
                cabecera = new TransaccionDAO().cargarTransaccionDesechada(row.getNombreArchivo());
            }
            medico = cabecera.getMedico() == null ? new Medico() : cabecera.getMedico();
            listaDetalle = cabecera.getListaDetalleProducto() == null ? new ArrayList<>() : cabecera.getListaDetalleProducto();
            fechaReceta = cabecera.getFechaReceta() == null ? new Date() : cabecera.getFechaReceta();

        } else {
            cabecera = new TransaccionDAO().cargarTransaccionDesechada(row.getNombreArchivo());
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
        listaEspecialidades = new EspecialidadDAO().findAllEspecialidades();
        EspeIdSelected = new ArrayList<>();
        EspeIdSelected.add(listaEspecialidades.get(0));
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
    
    public void onOpenProductoDialog(){
        listadoProductos = new ProductosLazyLoad();
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
        lazyModelNoProcesada = new IndexadoresLazyLoad(sessionUsuario, 0);
        lazyModelProcesada = new IndexadoresLazyLoad(sessionUsuario, 1);
        lazyModelDesechada = new IndexadoresLazyLoad(sessionUsuario, 2);
    }

    public CabeceraMovimiento getRow() {
        return row;
    }

    public void setRow(CabeceraMovimiento row) {
        this.row = row;
    }

    public List<DetalleMovimiento> getListaDetalle() {
        return listaDetalle;
    }

    public void setListaDetalle(List<DetalleMovimiento> listaDetalle) {
        this.listaDetalle = listaDetalle;
    }

    public LazyDataModel<CabeceraMovimiento> getLazyModelNoProcesada() {
        return lazyModelNoProcesada;
    }

    public void setLazyModelNoProcesada(LazyDataModel<CabeceraMovimiento> lazyModelNoProcesada) {
        this.lazyModelNoProcesada = lazyModelNoProcesada;
    }

    public LazyDataModel<CabeceraMovimiento> getLazyModelProcesada() {
        return lazyModelProcesada;
    }

    public void setLazyModelProcesada(LazyDataModel<CabeceraMovimiento> lazyModelProcesada) {
        this.lazyModelProcesada = lazyModelProcesada;
    }

    public LazyDataModel<CabeceraMovimiento> getLazyModelDesechada() {
        return lazyModelDesechada;
    }

    public void setLazyModelDesechada(LazyDataModel<CabeceraMovimiento> lazyModelDesechada) {
        this.lazyModelDesechada = lazyModelDesechada;
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

    public LazyDataModel<Producto> getListadoProductos() {
        return listadoProductos;
    }

    public void setListadoProductos(LazyDataModel<Producto> listadoProductos) {
        this.listadoProductos = listadoProductos;
    }

}
