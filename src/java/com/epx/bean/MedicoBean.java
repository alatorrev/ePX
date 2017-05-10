/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import com.epx.dao.MedicoDAO;
import com.epx.dao.EspecialidadDAO;
import com.epx.entity.Medico;
import com.epx.entity.Especialidad;
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
public class MedicoBean implements Serializable {

    private List<Medico> listadoMedicos = new ArrayList<>();
    private List<Medico> filteredMedico;
    private Medico med = new Medico();
    private MedicoDAO daoMedico = new MedicoDAO();

    private Especialidad esp = new Especialidad();
    private EspecialidadDAO daoEspecialidad = new EspecialidadDAO();
    private Usuario sessionUsuario;
    private List<Especialidad> listaEspecialidades = daoEspecialidad.findAllEspecialidades();
    private int EspeIdSelected;
    private String EspeTextSelected;
    private Facesmethods fcm = new Facesmethods();

    public MedicoBean() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            fcm.authenticaticatedUser(sessionUsuario);
            listadoMedicos = daoMedico.findAllMedicos();
            listaEspecialidades = daoEspecialidad.findAllEspecialidades();
        } catch (Exception e) {
            System.out.println("Bean Constructor: " + e.getMessage());
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

    public void showEditDialog(Medico m) {
        med = m;
    }

    public void showAsignaDialog(Medico m) {
        med = m;
    }

    public void showCreateDialog() {
        med = new Medico();
    }

    public void onCancelDialog() {
        setMed(new Medico());
    }

    public void commitCreate() throws SQLException {
        boolean flag = daoMedico.createMedico(med, sessionUsuario);
        if (flag) {
            listadoMedicos = daoMedico.findAllMedicos();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Nuevo médico añadido correctamente al sistema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }
    
    public void commitEdit() throws SQLException {
        boolean flag = daoMedico.editarMedico(med, sessionUsuario);
        if (flag) {
            listadoMedicos= daoMedico.findAllMedicos();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Datos del usuario actualizados correctamente"));
            RequestContext.getCurrentInstance().update("frm:growl");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Lo sentimos, ocurrió un problema"));
            RequestContext.getCurrentInstance().update("frm:growl");
        }
    }

    public List<Medico> getListadoMedicos() {
        return listadoMedicos;
    }

    public void setListadoMedicos(List<Medico> listadoMedicos) {
        this.listadoMedicos = listadoMedicos;
    }

    public List<Medico> getFilteredMedico() {
        return filteredMedico;
    }

    public void setFilteredMedico(List<Medico> filteredMedico) {
        this.filteredMedico = filteredMedico;
    }

    public Medico getMed() {
        return med;
    }

    public void setMed(Medico med) {
        this.med = med;
    }

    public Especialidad getEsp() {
        return esp;
    }

    public void setEsp(Especialidad esp) {
        this.esp = esp;
    }

    public EspecialidadDAO getDaoEspecialidad() {
        return daoEspecialidad;
    }

    public void setDaoEspecialidad(EspecialidadDAO daoEspecialidad) {
        this.daoEspecialidad = daoEspecialidad;
    }

    public List<Especialidad> getListaEspecialidades() {
        return listaEspecialidades;
    }

    public void setListaEspecialidades(List<Especialidad> listaEspecialidades) {
        this.listaEspecialidades = listaEspecialidades;
    }

    public int getEspeIdSelected() {
        return EspeIdSelected;
    }

    public void setEspeIdSelected(int EspeIdSelected) {
        this.EspeIdSelected = EspeIdSelected;
    }

    public String getEspeTextSelected() {
        return EspeTextSelected;
    }

    public void setEspeTextSelected(String EspeTextSelected) {
        this.EspeTextSelected = EspeTextSelected;
    }
}
