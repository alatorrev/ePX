/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;
import com.epx.dao.ProductoDAO;
import com.epx.entity.Producto;
import com.epx.entity.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.Facesmethods;
/**
 *
 * @author Bottago SA
 */
@ManagedBean
@ViewScoped
public class ProductoBean implements Serializable{
    private List<Producto> listadoProductos = new ArrayList<>();
    private List<Producto> filteredProducto;
    private Producto pro = new Producto();
    private ProductoDAO daoProducto = new ProductoDAO();
    private Usuario sessionUsuario;
    private Facesmethods fcm = new Facesmethods();
    
    public ProductoBean() {
        try {
            listadoProductos = daoProducto.findAllProducto();
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
        }
    }
    
    public void showEditDialog(Producto p) {
        pro = p;
    }

    public void showCreateDialog() {
        pro = new Producto();
    }

    public void onCancelDialog() {
        setPro(new Producto());
    }
    
    public void checkAuthorizedViews() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            fcm.authenticaticatedUser(sessionUsuario);
            fcm.permissionChecker(sessionUsuario.getListaVista());
        } catch (Exception e) {
        }
    }
    
    public void commitCreate() throws SQLException {
        boolean flag = daoProducto.createProducto(pro, sessionUsuario);
        if (flag) {
            listadoProductos = daoProducto.findAllProducto();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto creado correctamente"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }
    
    public void commitEdit() throws SQLException {
        boolean flag = daoProducto.editProducto(pro, sessionUsuario);
        if (flag) {
            listadoProductos = daoProducto.findAllProducto();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Datos del Producto actualizados correctamente"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }
    
    public void eliminar(Producto r) throws SQLException {
        boolean flag = daoProducto.deleteProducto(r, sessionUsuario);
        if (flag) {
            listadoProductos = daoProducto.findAllProducto();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto eliminado correctamente"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
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

    public Producto getPro() {
        return pro;
    }

    public void setPro(Producto pro) {
        this.pro = pro;
    }

    public ProductoDAO getDaoProducto() {
        return daoProducto;
    }

    public void setDaoProducto(ProductoDAO daoProducto) {
        this.daoProducto = daoProducto;
    }
    
    
}
