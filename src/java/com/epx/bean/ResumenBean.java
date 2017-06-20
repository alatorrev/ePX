/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import util.utilMeses;
import com.epx.dao.ResumenDAO;
import com.epx.entity.Resumen;
import java.io.IOException;
import com.epx.conexion.Conexion;
import com.epx.entity.Usuario;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import util.Facesmethods;

/**
 *
 * @author Bottago SA
 */
@ManagedBean
@ViewScoped
public class ResumenBean implements Serializable {

    private List<Resumen> lista = new ArrayList<>();
    private List<Resumen> filteredResumen;
    private Usuario sessionUsuario;
    ResumenDAO daoResumen = new ResumenDAO();
    Resumen res = new Resumen();
    List<Object[]> obj = new utilMeses().getListameses();
    List<Object[]> objanio = new utilMeses().getListaanios();
    Object[] mes;
    Object[] anio;
    private String tipoConsulta = "Mensual";
    private Date FechaActual = new Date();
    private Date desde = new Date(), hasta = new Date();
    private Facesmethods fcm = new Facesmethods();

    public ResumenBean() {

    }

    public void consultaLista() throws SQLException {
        if (tipoConsulta.equals("Mensual")) {
            lista = daoResumen.ReporteMensual(Integer.parseInt(mes[0].toString()), Integer.parseInt(anio[0].toString()));
        } else if (tipoConsulta.equals("Rango")) {
            lista = daoResumen.ReporteRango(desde, hasta);
        } else if (tipoConsulta.equals("FechaCorte")) {
            lista = daoResumen.ReporteCorte();
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

    public void exportpdf() throws JRException, IOException {
        Conexion con = new Conexion();
        Map<String, Object> parametros = new HashMap<String, Object>();
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servleContext = (ServletContext) context.getExternalContext().getContext();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MMM-dd");
        if (tipoConsulta.equals("Mensual")) {
            parametros.put("RutaImagen", servleContext.getRealPath("/reportes/"));
            parametros.put("mes", Integer.parseInt(mes[0].toString()));
            parametros.put("anio", Integer.parseInt(anio[0].toString()));
            String dirReporte = servleContext.getRealPath("/reportes/MensualGeneral.jasper");
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            String nomar = "ePX " + mes[1].toString() + " " + anio[0].toString() + ".pdf";
            response.addHeader("Content-disposition", "attachment;filename=\"" + nomar + "\"");
            response.setContentType("application/pdf");
            JasperPrint impres = JasperFillManager.fillReport(dirReporte, parametros, con.getConnection());
            JasperExportManager.exportReportToPdfStream(impres, response.getOutputStream());
            context.responseComplete();
        } else if (tipoConsulta.equals("Rango")) {
            parametros.put("RutaImagen", servleContext.getRealPath("/reportes/"));
            parametros.put("desde", desde);
            parametros.put("hasta", hasta);
            String dirReporte = servleContext.getRealPath("/reportes/RangoGeneral.jasper");
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            String nomar = "ePX " + fmt.format(desde) + " hasta " + fmt.format(hasta) + ".pdf";
            response.addHeader("Content-disposition", "attachment;filename=\"" + nomar + "\"");
            response.setContentType("application/pdf");
            JasperPrint impres = JasperFillManager.fillReport(dirReporte, parametros, con.getConnection());
            JasperExportManager.exportReportToPdfStream(impres, response.getOutputStream());
            context.responseComplete();
        } else if (tipoConsulta.equals("FechaCorte")) {
            parametros.put("RutaImagen", servleContext.getRealPath("/reportes/"));
            String dirReporte = servleContext.getRealPath("/reportes/CorteGeneral.jasper");
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            String nomar = "ePX Corte al " + fmt.format(FechaActual) + ".pdf";
            response.addHeader("Content-disposition", "attachment;filename=\"" + nomar + "\"");
            response.setContentType("application/pdf");
            JasperPrint impres = JasperFillManager.fillReport(dirReporte, parametros, con.getConnection());
            JasperExportManager.exportReportToPdfStream(impres, response.getOutputStream());
            context.responseComplete();
        }
    }

    public void exportexcel() throws JRException, IOException {
        Conexion con = new Conexion();
        Map<String, Object> parametros = new HashMap<String, Object>();
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servleContext = (ServletContext) context.getExternalContext().getContext();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MMM-dd");
        if (tipoConsulta.equals("Mensual")) {
            parametros.put("RutaImagen", servleContext.getRealPath("/reportes/"));
            parametros.put("mes", Integer.parseInt(mes[0].toString()));
            parametros.put("anio", Integer.parseInt(anio[0].toString()));
            parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);

            String dirReporte = servleContext.getRealPath("/reportes/MensualGeneral.jasper");
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            String nomar = "ePX " + mes[1].toString() + " " + anio[0].toString() + ".xlsx";
            response.addHeader("Content-disposition", "attachment;filename=\"" + nomar + "\"");
            response.setContentType("application/xlsx");

            JasperPrint impres = JasperFillManager.fillReport(dirReporte, parametros, con.getConnection());
            JRXlsxExporter expor = new JRXlsxExporter();
            expor.setExporterInput(new SimpleExporterInput(impres));
            expor.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

            SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
            config.setCollapseRowSpan(Boolean.FALSE);
            config.setWhitePageBackground(Boolean.FALSE);
            config.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
            config.setIgnoreTextFormatting(Boolean.TRUE);
            config.setDetectCellType(Boolean.TRUE);
            

            expor.setConfiguration(config);
            expor.exportReport();
            context.responseComplete();
        } else if (tipoConsulta.equals("Rango")) {
            parametros.put("RutaImagen", servleContext.getRealPath("/reportes/"));
            parametros.put("desde", desde);
            parametros.put("hasta", hasta);
            parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);

            String dirReporte = servleContext.getRealPath("/reportes/RangoGeneral.jasper");
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            String nomar = "ePX " + fmt.format(desde) + " hasta " + fmt.format(hasta) + ".xlsx";
            response.addHeader("Content-disposition", "attachment;filename=\""+nomar+"\"");
            response.setContentType("application/xlsx");

            JasperPrint impres = JasperFillManager.fillReport(dirReporte, parametros, con.getConnection());
            JRXlsxExporter expor = new JRXlsxExporter();
            expor.setExporterInput(new SimpleExporterInput(impres));
            expor.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

            SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
            config.setCollapseRowSpan(Boolean.FALSE);
            config.setWhitePageBackground(Boolean.FALSE);
            config.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
            config.setIgnoreTextFormatting(Boolean.TRUE);
            config.setDetectCellType(Boolean.TRUE);
            
            expor.setConfiguration(config);
            expor.exportReport();
            context.responseComplete();
        } else if (tipoConsulta.equals("FechaCorte")) {
            parametros.put("RutaImagen", servleContext.getRealPath("/reportes/"));
            parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);

            String dirReporte = servleContext.getRealPath("/reportes/CorteGeneral.jasper");
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            
            String nomar = "ePX Corte al " + fmt.format(FechaActual) + ".xlsx";
            response.addHeader("Content-disposition", "attachment;filename=\""+nomar+"\"");
            response.setContentType("application/xlsx");

            JasperPrint impres = JasperFillManager.fillReport(dirReporte, parametros, con.getConnection());
            JRXlsxExporter expor = new JRXlsxExporter();
            expor.setExporterInput(new SimpleExporterInput(impres));
            expor.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

            SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
            config.setCollapseRowSpan(Boolean.FALSE);
            config.setWhitePageBackground(Boolean.FALSE);
            config.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
            config.setIgnoreTextFormatting(Boolean.TRUE);
            config.setDetectCellType(Boolean.TRUE);

            expor.setConfiguration(config);
            expor.exportReport();
            context.responseComplete();
        }
    }

    public void limpiar() {
        lista.clear();
    }

    public List<Object[]> getObj() {
        return obj;
    }

    public void setObj(List<Object[]> obj) {
        this.obj = obj;
    }

    public Object[] getMes() {
        return mes;
    }

    public void setMes(Object[] mes) {
        this.mes = mes;
    }

    public Object[] getAnio() {
        return anio;
    }

    public void setAnio(Object[] anio) {
        this.anio = anio;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public Date getFechaActual() {
        return FechaActual;
    }

    public void setFechaActual(Date FechaActual) {
        this.FechaActual = FechaActual;
    }

    public List<Resumen> getLista() {
        return lista;
    }

    public void setLista(List<Resumen> lista) {
        this.lista = lista;
    }

    public ResumenDAO getDaoResumen() {
        return daoResumen;
    }

    public void setDaoResumen(ResumenDAO daoResumen) {
        this.daoResumen = daoResumen;
    }

    public Resumen getRes() {
        return res;
    }

    public void setRes(Resumen res) {
        this.res = res;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public List<Resumen> getFilteredResumen() {
        return filteredResumen;
    }

    public void setFilteredResumen(List<Resumen> filteredResumen) {
        this.filteredResumen = filteredResumen;
    }

    public List<Object[]> getObjanio() {
        return objanio;
    }

    public void setObjanio(List<Object[]> objanio) {
        this.objanio = objanio;
    }

}
