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
            query = "select codigopdv, count(idcabecera) as documentos "
                    + "from cabecera "
                    + "where year(fechareceta) = ? and month(fechareceta) = ? "
                    + "group by codigopdv";
            pst = con.getConnection().prepareStatement(query);
            pst.setString(1, anio + "");
            pst.setString(2, mes + "");
            rs = pst.executeQuery();
            while (rs.next()) {
                Resumen res = new Resumen();
                res.setCodigopdv(rs.getString(1));
                res.setConteo(rs.getInt(2));
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
            query = "select fechareceta, codigopdv, count(idcabecera) as documentos "
                    + "from cabecera "
                    + "where fechareceta between ? and ? "
                    + "group by fechareceta, codigopdv "
                    + "order by fechareceta";
            pst = con.getConnection().prepareStatement(query);
            pst.setString(1, format.format(desde));
            pst.setString(2, format.format(hasta));
            rs = pst.executeQuery();
            while (rs.next()) {
                Resumen res = new Resumen();
                res.setFechareceta(rs.getDate(1));
                res.setCodigopdv(rs.getString(2));
                res.setConteo(rs.getInt(3));
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
                query = "select fechareceta, codigopdv, count(idcabecera) as documentos "
                        + "from cabecera "
                        + "where fechareceta = format(getdate(), 'yyyy-MM-dd') "
                        + "group by fechareceta, codigopdv";
                pst = con.getConnection().prepareStatement(query);
                rs = pst.executeQuery();
                while (rs.next()) {
                    Resumen res = new Resumen();
                    res.setFechareceta(rs.getDate(1));
                    res.setCodigopdv(rs.getString(2));
                    res.setConteo(rs.getInt(3));
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
