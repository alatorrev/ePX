/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epx.dao;

import com.epx.conexion.Conexion;
import com.epx.entity.CabeceraMovimiento;
import com.epx.entity.DetalleMovimiento;
import com.epx.entity.Medico;
import com.epx.entity.Producto;
import com.epx.entity.Usuario;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Filesmethods;

/**
 *
 * @author BOTTAGO
 */
public class TransaccionDAO {

    public CabeceraMovimiento cargarTransaccion(String nombreArchivo) {
        CabeceraMovimiento cab = new CabeceraMovimiento();
        Conexion con = new Conexion();
        String sql = "select c.idcabecera,c.nombrearchivo,c.rutaarchivodestino,c.idmedico,c.fuentemedico,mb.nombres,mb.apellidos,c.fechareceta from cabecera c "
                + "inner join medico_bottago mb on mb.idmedico=c.idmedico "
                + "where c.nombrearchivo=? and mb.fuentemedico=c.fuentemedico "
                + "union all "
                + "select c.idcabecera,c.nombrearchivo,c.rutaarchivodestino,c.idmedico,c.fuentemedico,md.nombres,md.apellidos,c.fechareceta from cabecera c "
                + "inner join medico_difare md on md.idmedico=c.idmedico "
                + "where c.nombrearchivo=? and md.fuentemedico=c.fuentemedico";
        try {
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            pst.setString(1, nombreArchivo);
            pst.setString(2, nombreArchivo);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Medico tempMedico = new Medico();
                tempMedico.setIdMedico(rs.getLong(4));
                tempMedico.setFuente(rs.getString(5));
                tempMedico.setNombres(rs.getString(6));
                tempMedico.setApellidos(rs.getString(7));
                cab.setIdCabecera(rs.getLong(1));
                cab.setNombreArchivo(rs.getString(2));
                cab.setRutaArchivoDestino(rs.getString(3));
                cab.setFechaReceta(rs.getTimestamp(8));
                cab.setMedico(tempMedico);
            }
            String sql2 = "select d.iddetalle,d.idcabecera,d.cantidad,pb.idproducto,pb.fuenteproducto,pb.marca,pb.sustituto,pb.forma,pb.concentracion,null as laboratorio from detalle d "
                    + "inner join producto_bottago pb on pb.idproducto=d.idproducto where d.idcabecera=? "
                    + "and pb.fuenteproducto=d.fuenteproducto union all "
                    + "select d.iddetalle,d.idcabecera,d.cantidad,pd.idproducto,pd.fuenteproducto,pd.marca,pd.sustituto,pd.forma1,pd.concentracion,pd.laboratorio from detalle d "
                    + "inner join producto_difare pd on pd.idproducto=d.idproducto where d.idcabecera=? "
                    + "and pd.fuenteproducto=d.fuenteproducto";
            pst = con.getConnection().prepareStatement(sql2);
            pst.setInt(1, cab.getIdCabecera().intValue());
            pst.setInt(2, cab.getIdCabecera().intValue());
            rs = pst.executeQuery();
            List<DetalleMovimiento> listaDetalle = new ArrayList<>();
            int cont = 0;
            while (rs.next()) {
                cont++;
                Producto pro = new Producto();
                pro.setIdProducto(rs.getLong(4));
                pro.setFuente(rs.getString(5));
                pro.setMarca(rs.getString(6));
                pro.setSustituto(rs.getString(7));
                pro.setForma(rs.getString(8));
                pro.setConcentracion(rs.getString(9));
                pro.setLaboratorio(rs.getString(10));
                DetalleMovimiento det = new DetalleMovimiento();
                det.setIdDetalle(rs.getLong(1));
                det.setIdCabecera(rs.getLong(2));
                det.setCantidad(rs.getInt(3));
                det.setProducto(pro);
                det.setSecuencial(cont);
                listaDetalle.add(det);
            }
            cab.setListaDetalleProducto(listaDetalle);
        } catch (Exception e) {
            System.out.println("TRANSACCIONDAO CARGAR TRANSACCION: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return cab;
    }

    public void procesarTransaccion(CabeceraMovimiento cab, Object[] row, String opcion, Usuario sessionUsuario) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String SEPARADOR = File.separator;
        Conexion con = new Conexion();
        String sql = "insert into cabecera (idmedico,fuentemedico,codigopdv,nombrearchivo,rutaarchivoorigen,rutaarchivodestino,fechareceta,"
                + "fechascanner,fechapantallainit,fecharegistro,idusuario,estado) "
                + "values (?,?,?,?,?,?,?,"
                + "?,?,?,?,?)";
        try {
            String directorioBase = new ParametrosDAO().parametroDirectorioRaiz();
            con.getConnection().setAutoCommit(false);
            PreparedStatement pst = con.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, cab.getMedico().getIdMedico().intValue());
            pst.setString(2, cab.getMedico().getFuente());
            pst.setString(3, row[5].toString());
            pst.setString(4, row[2].toString());
            pst.setString(5, row[1].toString());
            String rutadestino = directorioBase + row[5].toString() + SEPARADOR + opcion + SEPARADOR + row[2];
            pst.setString(6, rutadestino);
            pst.setDate(7, java.sql.Date.valueOf(sdf.format(cab.getFechaReceta())));
            pst.setTimestamp(8, new java.sql.Timestamp(Long.parseLong(row[0].toString())));
            pst.setTimestamp(9, new java.sql.Timestamp(cab.getPantallaInit().getTime()));
            pst.setTimestamp(10, new java.sql.Timestamp(new Date().getTime()));
            pst.setString(11, sessionUsuario.getLoginname());
            pst.setBoolean(12, true);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            int generatedIdCabecera = 0;
            if (rs.next()) {
                generatedIdCabecera = rs.getInt(1);
            }

            String sql2 = "insert into detalle (idcabecera,idproducto,fuenteproducto,cantidad) "
                    + "values (?,?,?,?)";
            pst = con.getConnection().prepareStatement(sql2);
            for (DetalleMovimiento det : cab.getListaDetalleProducto()) {
                pst.setInt(1, generatedIdCabecera);
                pst.setInt(2, det.getProducto().getIdProducto().intValue());
                pst.setString(3, det.getProducto().getFuente());
                pst.setInt(4, det.getCantidad());
                pst.executeUpdate();
            }
            con.getConnection().commit();
            Filesmethods.transferFiletransaccion(directorioBase + row[5].toString() + SEPARADOR, rutadestino, opcion, row[2].toString(),row);
        } catch (Exception e) {
            System.out.println("TRANSACCIONDAO PROCESAR TRANSACCION: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void editarTransaccion(CabeceraMovimiento cab, Object[] row, String opcion, Usuario sessionUsuario) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String SEPARADOR = File.separator;
        Conexion con = new Conexion();
        String sql = "update cabecera set idmedico=?,fuentemedico=?,rutaarchivoorigen=?,rutaarchivodestino=?,fechareceta=?,fechapantallainit=?,"
                + "fecharegistro=?,idusuario=?,estado=? where nombrearchivo=?";
        try {
            con.getConnection().setAutoCommit(false);
            String directorioBase = new ParametrosDAO().parametroDirectorioRaiz();
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            pst.setInt(1, cab.getMedico().getIdMedico().intValue());
            pst.setString(2, cab.getMedico().getFuente());
            pst.setString(3, row[1].toString());
            String rutadestino = directorioBase + row[5].toString() + SEPARADOR + opcion + SEPARADOR + row[2];
            pst.setString(4, rutadestino);
            pst.setDate(5, java.sql.Date.valueOf(sdf.format(cab.getFechaReceta())));
            pst.setTimestamp(6, new java.sql.Timestamp(cab.getPantallaInit().getTime()));
            pst.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
            pst.setString(8, sessionUsuario.getLoginname());
            pst.setBoolean(9, true);
            pst.setString(10, row[2].toString());
            pst.executeUpdate();
            borrarDetalle(cab);
            String sql2 = "insert into detalle (idcabecera,idproducto,fuenteproducto,cantidad) "
                    + "values (?,?,?,?)";
            pst = con.getConnection().prepareStatement(sql2);
            for (DetalleMovimiento det : cab.getListaDetalleProducto()) {
                pst.setInt(1, cab.getIdCabecera().intValue());
                pst.setInt(2, det.getProducto().getIdProducto().intValue());
                pst.setString(3, det.getProducto().getFuente());
                pst.setInt(4, det.getCantidad());
                pst.executeUpdate();
            }
            con.getConnection().commit();
            if (!opcion.equals(row[4].toString())) {
                Filesmethods.transferFiletransaccion(directorioBase + row[5].toString() + SEPARADOR, rutadestino, opcion, row[2].toString(),row);
            }
        } catch (Exception e) {
            System.out.println("TRANSACCIONDAO EDITANDO TRANSACCION: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void desecharTransaccion(CabeceraMovimiento cab, Object[] row, String opcion, Usuario sessionUsuario) {
        Conexion con = new Conexion();
        String directorioBase = new ParametrosDAO().parametroDirectorioRaiz();
        String SEPARADOR = File.separator;
        String sql = "update cabecera set estado=?,rutaarchivoorigen=?,rutaarchivodestino=? where idcabecera=?";
        try {
            con.getConnection().setAutoCommit(false);
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            pst.setBoolean(1, false);
            pst.setString(2, row[1].toString());
            String rutadestino = directorioBase + row[5].toString() + SEPARADOR + opcion + SEPARADOR + row[2];
            pst.setString(3, rutadestino);
            pst.setInt(4, cab.getIdCabecera().intValue());
            pst.executeUpdate();
            con.getConnection().commit();
            Filesmethods.transferFiletransaccion(directorioBase + row[5].toString() + SEPARADOR, rutadestino, opcion, row[2].toString(),row);
        } catch (Exception e) {
            System.out.println("TRANSACCIONDAO DESECHANDO TRANSACCION: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void desecharRaiz(CabeceraMovimiento cab, Object[] row, String opcion, Usuario sessionUsuario) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String SEPARADOR = File.separator;
        Conexion con = new Conexion();
        String sql = "insert into cabecera (codigopdv,nombrearchivo,rutaarchivoorigen,rutaarchivodestino,"
                + "fechascanner,fechapantallainit,fecharegistro,idusuario,estado) "
                + "values (?,?,?,?,?,?,?,?,?)";
        try {
            String directorioBase = new ParametrosDAO().parametroDirectorioRaiz();
            con.getConnection().setAutoCommit(false);
            PreparedStatement pst = con.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, row[5].toString());
            pst.setString(2, row[2].toString());
            pst.setString(3, row[1].toString());
            String rutadestino = directorioBase + row[5].toString() + SEPARADOR + opcion + SEPARADOR + row[2];
            pst.setString(4, rutadestino);
            pst.setTimestamp(5, new java.sql.Timestamp(Long.parseLong(row[0].toString())));
            pst.setTimestamp(6, new java.sql.Timestamp(cab.getPantallaInit().getTime()));
            pst.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
            pst.setString(8, sessionUsuario.getLoginname());
            pst.setBoolean(9, false);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            int generatedIdCabecera = 0;
            if (rs.next()) {
                generatedIdCabecera = rs.getInt(1);
            }

            String sql2 = "insert into detalle (idcabecera,idproducto,fuenteproducto,cantidad) "
                    + "values (?,?,?,?)";
            pst = con.getConnection().prepareStatement(sql2);
            for (DetalleMovimiento det : cab.getListaDetalleProducto()) {
                pst.setInt(1, generatedIdCabecera);
                pst.setInt(2, det.getProducto().getIdProducto().intValue());
                pst.setString(3, det.getProducto().getFuente());
                pst.setInt(4, det.getCantidad());
                pst.executeUpdate();
            }
            con.getConnection().commit();
            Filesmethods.transferFiletransaccion(directorioBase + row[5].toString() + SEPARADOR, rutadestino, opcion, row[2].toString(),row);
        } catch (Exception e) {
            System.out.println("TRANSACCIONDAO PROCESAR TRANSACCION: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void borrarDetalle(CabeceraMovimiento cab) {
        Conexion con = new Conexion();
        String sql = "delete from detalle where idcabecera=?";
        try {
            con.getConnection().setAutoCommit(false);
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            pst.setInt(1, cab.getIdCabecera().intValue());
            pst.executeUpdate();
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("TRANSACCIONDAO BORRANDO DETALLE TRANSACCION: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
