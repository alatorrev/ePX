/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;

import com.epx.conexion.Conexion;
import com.epx.entity.Resumen;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bottago SA
 */
public class ResumenDAO implements Serializable {

    public List<Resumen> ReporteMensual(int mes, int anio) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Resumen> lista = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "select pd.ciudad, sc.codigopdv, sc.codscanner, count(cab.idcabecera) as documentos "
                    + "from scanner sc "
                    + "left outer join cabecera cab on sc.codigopdv = cab.codigopdv "
                    + "and sc.codscanner = SUBSTRING(cab.nombrearchivo, 7, 2) "
                    + "and year(cab.fechascanner) = ? "
                    + "and month(cab.fechascanner) = ? "
                    + "inner join pdv pd on sc.codigopdv = pd.codigopdv "
                    + "group by sc.codigopdv, pd.ciudad,sc.codscanner "
                    + "order by sc.codigopdv";
            pst = con.getConnection().prepareStatement(query);
            pst.setString(1, anio + "");
            pst.setString(2, mes + "");
            rs = pst.executeQuery();
            while (rs.next()) {
                Resumen res = new Resumen();
                res.setCiudad(rs.getString(1));
                res.setCodigopdv(rs.getString(2));
                res.setScanner(rs.getString(3));
                res.setConteo(rs.getInt(4));
                lista.add(res);
            }
        } catch (Exception e) {
            System.out.println("DAO Resumen Mensual: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(ResumenDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    public List<Resumen> ReporteRango(Date desde, Date hasta) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Resumen> lista = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "select cast(cab.fechascanner as date) as fechascanner, "
                    + "pd.ciudad, sc.codigopdv, sc.codscanner, count(cab.idcabecera) as documentos "
                    + "from scanner sc "
                    + "left outer join cabecera cab on sc.codigopdv = cab.codigopdv "
                    + "and sc.codscanner = SUBSTRING(cab.nombrearchivo,7,2) "
                    + "and cast(cab.fechascanner as date) between ? and ? "
                    + "inner join pdv pd on sc.codigopdv = pd.codigopdv "
                    + "group by cast(cab.fechascanner as date), sc.codigopdv, pd.ciudad, sc.codscanner "
                    + "order by cast(cab.fechascanner as date), sc.codigopdv";
            pst = con.getConnection().prepareStatement(query);
            pst.setString(1, format.format(desde));
            pst.setString(2, format.format(hasta));
            rs = pst.executeQuery();
            while (rs.next()) {
                Resumen res = new Resumen();
                res.setFechareceta(rs.getDate(1));
                res.setCiudad(rs.getString(2));
                res.setCodigopdv(rs.getString(3));
                res.setScanner(rs.getString(4));
                res.setConteo(rs.getInt(5));
                lista.add(res);
            }
        } catch (Exception e) {
            System.out.println("DAO Resumen Rango: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(ResumenDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    public List<Resumen> ReporteCorte() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Resumen> lista = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "select cast(cab.fechascanner as date) as fechascanner, pd.ciudad, sc.codigopdv, sc.codscanner, count(cab.idcabecera) as documentos "
                    + "from scanner sc  "
                    + "left outer join cabecera cab on sc.codigopdv = cab.codigopdv "
                    + "and sc.codscanner = SUBSTRING(cab.nombrearchivo, 7, 2) "
                    + "and cast(cab.fechascanner as date) = format(getdate(), 'yyyy-MM-dd') "
                    + "inner join pdv pd on sc.codigopdv = pd.codigopdv "
                    + "group by cast(cab.fechascanner as date), sc.codigopdv, pd.ciudad, sc.codscanner "
                    + "order by cast(cab.fechascanner as date), sc.codigopdv";
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Resumen res = new Resumen();
                res.setFechareceta(rs.getDate(1));
                res.setCiudad(rs.getString(2));
                res.setCodigopdv(rs.getString(3));
                res.setScanner(rs.getString(4));
                res.setConteo(rs.getInt(5));
                lista.add(res);
            }
        } catch (Exception e) {
            System.out.println("DAO Resumen: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(ResumenDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }
}
