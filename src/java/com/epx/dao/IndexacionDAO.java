/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;

import com.epx.conexion.Conexion;
import com.epx.entity.Indexacion;

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
public class IndexacionDAO implements Serializable {

    public List<Indexacion> IndexacionesAlCorte(Date desde, Date hasta) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Indexacion> lista = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "select convert(VARCHAR, cab.fechascanner, 120) as fechascanner,cab.codigopdv, SUBSTRING(cab.nombrearchivo, 7,2) as codigoscanner, "
                    + "cab.idmedico, det.idproducto, det.fuenteproducto, det.secuencial, 1 as px "
                    + "from cabecera cab "
                    + "inner join detalle det on cab.idcabecera = det.idcabecera "
                    + "where cast(cab.fecharegistro as date) between ? and ? "
                    + "and cab.estado = 1";
            pst = con.getConnection().prepareStatement(query);
            pst.setString(1, format.format(desde));
            pst.setString(2, format.format(hasta));
            rs = pst.executeQuery();
            while (rs.next()) {
                Indexacion inde = new Indexacion();
                inde.setFechascanner(rs.getString(1));
                inde.setCodigopdv(rs.getString(2));
                inde.setCodigoscanner(rs.getString(3));
                inde.setIdmedico(rs.getInt(4));
                inde.setIdproducto(rs.getInt(5));
                inde.setFuenteproducto(rs.getString(6));
                inde.setSecuencial(rs.getInt(7));
                inde.setPx(rs.getInt(8));
                lista.add(inde);
            }
        } catch (Exception e) {
            System.out.println("DAO Indexaciones al corte: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(IndexacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    public List<Indexacion> ProductoAlCorte(Date desde, Date hasta) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Indexacion> lista = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "select idproducto, marca, sustituto, forma, concentracion "
                    + "from producto_bottago "
                    + "where fechacrea between ? and ?";
            pst = con.getConnection().prepareStatement(query);
            pst.setString(1, format.format(desde));
            pst.setString(2, format.format(hasta));
            rs = pst.executeQuery();
            while (rs.next()) {
                Indexacion inde = new Indexacion();
                inde.setIdproducto(rs.getInt(1));
                inde.setMarca(rs.getString(2));
                inde.setSustituto(rs.getString(3));
                inde.setForma(rs.getString(4));
                inde.setConcentracion(rs.getString(5));
                lista.add(inde);
            }
        } catch (Exception e) {
            System.out.println("DAO Productos al corte: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(IndexacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    public List<Indexacion> MedicoAlCorte(Date desde, Date hasta) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Indexacion> lista = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "Select mbo.idmedico, mbo.nombres, mbo.apellidos, mbo.direccion, "
                    + "substring((select ','+esp.descripcion  as [text()] "
                    + "from especialidad esp "
                    + "left join med_espe mesp on mbo.idmedico = mesp.idmedico "
                    + "where mesp.idespecialidad = esp.idespecialidad "
                    + "for xml path('')),2,1000) as especialidad "
                    + "from medico_bottago mbo "
                    + "where cast(mbo.fechacreacion as date) between ? and ?";
            pst = con.getConnection().prepareStatement(query);
            pst.setString(1, format.format(desde));
            pst.setString(2, format.format(hasta));
            rs = pst.executeQuery();
            while (rs.next()) {
                Indexacion inde = new Indexacion();
                inde.setIdmedico(rs.getInt(1));
                inde.setNombres(rs.getString(2));
                inde.setApellidos(rs.getString(3));
                inde.setDireccion(rs.getString(4));
                inde.setEspecialidad(rs.getString(5));
                lista.add(inde);
            }
        } catch (Exception e) {
            System.out.println("DAO Médicos al corte: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(IndexacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }
}
