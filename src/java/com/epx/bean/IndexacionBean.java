/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.bean;

import com.epx.dao.IndexacionDAO;
import com.epx.entity.Indexacion;
import java.io.IOException;
import com.epx.conexion.Conexion;
import com.epx.entity.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
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
public class IndexacionBean implements Serializable {

    private List<Indexacion> listaIndexacion = new ArrayList<>();
    private List<Indexacion> filteredlistaIndexacion;

    private List<Indexacion> listaProductos = new ArrayList<>();
    private List<Indexacion> filteredlistaProductos;

    private List<Indexacion> listaMedicos = new ArrayList<>();
    private List<Indexacion> filteredlistaMedicos;

    IndexacionDAO daoIndexacion = new IndexacionDAO();
    Indexacion inde = new Indexacion();

    private Usuario sessionUsuario;

    private Date desde = new Date(), hasta = new Date();
    private Facesmethods fcm = new Facesmethods();

    public void consultaLista() throws SQLException {
        listaIndexacion = daoIndexacion.IndexacionesAlCorte(desde, hasta);
        listaProductos = daoIndexacion.ProductoAlCorte(desde, hasta);
        listaMedicos = daoIndexacion.MedicoAlCorte(desde, hasta);
    }

    public void checkAuthorizedViews() {
        try {
            sessionUsuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            fcm.authenticaticatedUser(sessionUsuario);
            fcm.permissionChecker(sessionUsuario.getListaVista());
        } catch (Exception e) {
        }
    }

    public void exportexcelfinal() throws JRException, IOException {
        Conexion con = new Conexion();
        Map<String, Object> parametros = new HashMap<String, Object>();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MMM-dd");
        FacesContext context = FacesContext.getCurrentInstance();
        FacesContext context2 = FacesContext.getCurrentInstance();
        FacesContext context3 = FacesContext.getCurrentInstance();
        ServletContext servleContext = (ServletContext) context.getExternalContext().getContext();
        ServletContext servleContext2 = (ServletContext) context2.getExternalContext().getContext();
        ServletContext servleContext3 = (ServletContext) context3.getExternalContext().getContext();

        parametros.put("desde", desde);
        parametros.put("hasta", hasta);
        parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);

        String dirReporte = servleContext.getRealPath("/reportes/IndexacionReporte.jasper");
        String dirReporte2 = servleContext2.getRealPath("/reportes/ProductoAlCorte.jasper");
        String dirReporte3 = servleContext3.getRealPath("/reportes/MedicoAlCorte.jasper");

        String nomar = "ePX - IE Recetas " + fmt.format(desde) + " hasta " + fmt.format(hasta) + ".xlsx";
        String nomar2 = "ePX - IE Producto " + fmt.format(desde) + " hasta " + fmt.format(hasta) + ".xlsx";
        String nomar3 = "ePX - IE Medico " + fmt.format(desde) + " hasta " + fmt.format(hasta) + ".xlsx";

//        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
//        response.addHeader("Content-disposition", "attachment;filename=\"" + nomar + "\"");
//        response.setContentType("application/xlsx");
//        HttpServletResponse response2 = (HttpServletResponse) context2.getExternalContext().getResponse();
//        response2.addHeader("Content-disposition", "attachment;filename=\"" + nomar2 + "\"");
//        response2.setContentType("application/xlsx");
//        HttpServletResponse response3 = (HttpServletResponse) context3.getExternalContext().getResponse();
//        response3.addHeader("Content-disposition", "attachment;filename=\"" + nomar3 + "\"");
//        response3.setContentType("application/xlsx");
        JasperPrint impres = JasperFillManager.fillReport(dirReporte, parametros, con.getConnection());
        JasperPrint impres2 = JasperFillManager.fillReport(dirReporte2, parametros, con.getConnection());
        JasperPrint impres3 = JasperFillManager.fillReport(dirReporte3, parametros, con.getConnection());

        String exportDir = System.getProperty("catalina.base") + "/excel/";//server
        String exportPath = exportDir + nomar;//server

        String exportDir2 = System.getProperty("catalina.base") + "/excel/";//server
        String exportPath2 = exportDir2 + nomar2;//server

        String exportDir3 = System.getProperty("catalina.base") + "/excel/";//server
        String exportPath3 = exportDir3 + nomar3;//server

        JRXlsxExporter expor = new JRXlsxExporter();
        expor.setExporterInput(new SimpleExporterInput(impres));
        expor.setExporterOutput(new SimpleOutputStreamExporterOutput(exportPath));
        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
        config.setCollapseRowSpan(Boolean.FALSE);
        config.setWhitePageBackground(Boolean.FALSE);
        config.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
        config.setIgnoreTextFormatting(Boolean.TRUE);
        config.setDetectCellType(Boolean.TRUE);
        expor.setConfiguration(config);
        expor.exportReport();

        JRXlsxExporter expor2 = new JRXlsxExporter();
        expor2.setExporterInput(new SimpleExporterInput(impres2));
        expor2.setExporterOutput(new SimpleOutputStreamExporterOutput(exportPath2));
        SimpleXlsxReportConfiguration config2 = new SimpleXlsxReportConfiguration();
        config2.setCollapseRowSpan(Boolean.FALSE);
        config2.setWhitePageBackground(Boolean.FALSE);
        config2.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
        config2.setIgnoreTextFormatting(Boolean.TRUE);
        config2.setDetectCellType(Boolean.TRUE);
        expor2.setConfiguration(config2);
        expor2.exportReport();

        JRXlsxExporter expor3 = new JRXlsxExporter();
        expor3.setExporterInput(new SimpleExporterInput(impres3));
        expor3.setExporterOutput(new SimpleOutputStreamExporterOutput(exportPath3));
        SimpleXlsxReportConfiguration config3 = new SimpleXlsxReportConfiguration();
        config3.setCollapseRowSpan(Boolean.FALSE);
        config3.setWhitePageBackground(Boolean.FALSE);
        config3.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
        config3.setIgnoreTextFormatting(Boolean.TRUE);
        config3.setDetectCellType(Boolean.TRUE);
        expor3.setConfiguration(config3);
        expor3.exportReport();

        String archivo = System.getProperty("catalina.base") + "\\excel\\" + nomar;
        String archivo2 = System.getProperty("catalina.base") + "\\excel\\" + nomar2;
        String archivo3 = System.getProperty("catalina.base") + "\\excel\\" + nomar3;
        String archivo4 = System.getProperty("catalina.base") + "\\excel\\" + "test.zip";
        File testZip = new File(System.getProperty("catalina.base") + "\\excel\\", "test.zip");
        FileOutputStream fileObject = new FileOutputStream(testZip);
        ZipOutputStream zipFile = new ZipOutputStream(fileObject);
        addToZip(archivo, nomar, zipFile);
        addToZip(archivo2, nomar2, zipFile);
        addToZip(archivo3, nomar3, zipFile);
        zipFile.close();
        fileObject.close();

        HttpServletResponse rp = (HttpServletResponse) context.getExternalContext().getResponse();
        rp.setContentType("application/zip");
        rp.setHeader("Content-Disposition", "attachment;filename=\"" + "eXP IE Recetas " + fmt.format(desde) + " hasta " + fmt.format(hasta) + ".zip" + "\"");

        File f = new File(System.getProperty("catalina.base") + "\\excel\\" + "test.zip");
        byte[] arBytes = new byte[(int) f.length()];
        FileInputStream is = new FileInputStream(f);
        is.read(arBytes);
        ServletOutputStream op = rp.getOutputStream();
        op.write(arBytes);
        op.flush();
        op.close();

        File f1 = new File(archivo);
        File f2 = new File(archivo2);
        File f3 = new File(archivo3);
        f1.delete();
        f2.delete();
        f3.delete();
    }

    private static void addToZip(String fileName, String nombrearchivo, ZipOutputStream zipFile) {
        byte[] buffer = new byte[1024];
        int len;
        ZipEntry zipEntry = null; // to add entry of file in zip fle
        FileInputStream in = null; // to read input files to be compressed
        try {
            zipEntry = new ZipEntry(nombrearchivo);
            zipEntry.setMethod(zipEntry.DEFLATED);
            zipFile.putNextEntry(zipEntry);
            in = new FileInputStream(fileName);
            while ((len = in.read(buffer)) > 0) {
                zipFile.write(buffer, 0, len);
            }
            zipFile.closeEntry();
            in.close();
        } catch (IOException e) {
            System.out.println("Exception occured " + e);
        }

    }

    public void exportexcel() throws JRException, IOException {
        Conexion con = new Conexion();
        Map<String, Object> parametros = new HashMap<String, Object>();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MMM-dd");
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servleContext = (ServletContext) context.getExternalContext().getContext();

        //Parámetros generales para los tres reportes
        parametros.put("desde", desde);
        parametros.put("hasta", hasta);
        parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);

        //Dirección de los tres reportes
        String dirReporte = servleContext.getRealPath("/reportes/IndexacionReporte.jasper");

        //Nombre de los archivos de los tres reportes
        String nomar = "ePX - IE Recetas " + fmt.format(desde) + " hasta " + fmt.format(hasta) + ".xlsx";

        //Generación para el primer reporte
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment;filename=\"" + nomar + "\"");
        response.setContentType("application/xlsx");

        JasperPrint impres = JasperFillManager.fillReport(dirReporte, parametros, con.getConnection());

        //Exportación Excel primer Reporte
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

    public void exportexcel2() throws JRException, IOException {
        Conexion con = new Conexion();
        Map<String, Object> parametros = new HashMap<String, Object>();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MMM-dd");
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servleContext = (ServletContext) context.getExternalContext().getContext();

        //Parámetros generales para los tres reportes
        parametros.put("desde", desde);
        parametros.put("hasta", hasta);
        parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);

        //Dirección de los tres reportes
        String dirReporte = servleContext.getRealPath("/reportes/ProductoAlCorte.jasper");

        //Nombre de los archivos de los tres reportes
        String nomar = "ePX - IE Producto " + fmt.format(desde) + " hasta " + fmt.format(hasta) + ".xlsx";

        //Generación para el primer reporte
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment;filename=\"" + nomar + "\"");
        response.setContentType("application/xlsx");

        JasperPrint impres = JasperFillManager.fillReport(dirReporte, parametros, con.getConnection());

        //Exportación Excel primer Reporte
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

    public void exportexcel3() throws JRException, IOException {
        Conexion con = new Conexion();
        Map<String, Object> parametros = new HashMap<String, Object>();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MMM-dd");
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servleContext = (ServletContext) context.getExternalContext().getContext();

        //Parámetros generales para los tres reportes
        parametros.put("desde", desde);
        parametros.put("hasta", hasta);
        parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);

        //Dirección de los tres reportes
        String dirReporte = servleContext.getRealPath("/reportes/MedicoAlCorte.jasper");

        //Nombre de los archivos de los tres reportes
        String nomar = "ePX - IE Medico " + fmt.format(desde) + " hasta " + fmt.format(hasta) + ".xlsx";

        //Generación para el primer reporte
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment;filename=\"" + nomar + "\"");
        response.setContentType("application/xlsx");

        JasperPrint impres = JasperFillManager.fillReport(dirReporte, parametros, con.getConnection());

        //Exportación Excel primer Reporte
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

    public List<Indexacion> getListaIndexacion() {
        return listaIndexacion;
    }

    public void setListaIndexacion(List<Indexacion> listaIndexacion) {
        this.listaIndexacion = listaIndexacion;
    }

    public List<Indexacion> getFilteredlistaIndexacion() {
        return filteredlistaIndexacion;
    }

    public void setFilteredlistaIndexacion(List<Indexacion> filteredlistaIndexacion) {
        this.filteredlistaIndexacion = filteredlistaIndexacion;
    }

    public List<Indexacion> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Indexacion> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public List<Indexacion> getFilteredlistaProductos() {
        return filteredlistaProductos;
    }

    public void setFilteredlistaProductos(List<Indexacion> filteredlistaProductos) {
        this.filteredlistaProductos = filteredlistaProductos;
    }

    public List<Indexacion> getListaMedicos() {
        return listaMedicos;
    }

    public void setListaMedicos(List<Indexacion> listaMedicos) {
        this.listaMedicos = listaMedicos;
    }

    public List<Indexacion> getFilteredlistaMedicos() {
        return filteredlistaMedicos;
    }

    public void setFilteredlistaMedicos(List<Indexacion> filteredlistaMedicos) {
        this.filteredlistaMedicos = filteredlistaMedicos;
    }

    public IndexacionDAO getDaoIndexacion() {
        return daoIndexacion;
    }

    public void setDaoIndexacion(IndexacionDAO daoIndexacion) {
        this.daoIndexacion = daoIndexacion;
    }

    public Indexacion getInde() {
        return inde;
    }

    public void setInde(Indexacion inde) {
        this.inde = inde;
    }

    public Usuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(Usuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
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

    public Facesmethods getFcm() {
        return fcm;
    }

    public void setFcm(Facesmethods fcm) {
        this.fcm = fcm;
    }

}
