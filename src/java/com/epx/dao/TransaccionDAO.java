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

    public List<CabeceraMovimiento> loadWorkingAreaDigitador(Usuario sessionUsuario, int estado, int first, int pageSize, StringBuilder filters) {
        Conexion con = new Conexion();
        List<CabeceraMovimiento> lista = new ArrayList<>();
        String sql = "select * from("
                + "select idcabecera,codigopdv,nombrearchivo,rutaarchivoorigen,rutaarchivodestino,fechareceta,fechascanner,estado,"
                + "ROW_NUMBER() OVER (ORDER BY fechascanner [ORDER]) as row "
                + "from cabecera "
                + "where codigopdv "
                + "in (select codigopdv from pdv,usuario where usuario.idusuario=? and pdv.idusuario=?) "
                + "and estado=? "
                + "[FILTERS]"
                + ") a where row >? and row <=?";
        if (estado != 0) {
            sql = sql.replace("[ORDER]", "desc");
        } else {
            sql = sql.replace("[ORDER]", "asc");
        }

        if (filters.length() > 0) {
            sql = sql.replace("[FILTERS]", filters.toString());
        } else {
            sql = sql.replace("[FILTERS]", "");
        }
        try {
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            pst.setInt(1, sessionUsuario.getIdusuario());
            pst.setInt(2, sessionUsuario.getIdusuario());
            pst.setInt(3, estado);
            pst.setInt(4, first);
            pst.setInt(5, first + pageSize);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                CabeceraMovimiento cab = new CabeceraMovimiento();
                cab.setIdCabecera(rs.getLong(1));
                cab.setCodigoPDV(rs.getString(2));
                cab.setNombreArchivo(rs.getString(3));
                cab.setRutaArchivoDestino(rs.getString(5));
                cab.setFechaReceta(rs.getTimestamp(6));
                cab.setFechaArchivo(rs.getTimestamp(7));
                cab.setEstado(rs.getInt(8));
                lista.add(cab);
            }
        } catch (Exception e) {
            System.out.println("TRANSACCIONDAO LOADWORKINGAREA DIGITADOR: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("idusuario: " + sessionUsuario.getIdusuario()
                + " estado: " + estado
                + " starting record: " + first
                + " ending record: " + (first + pageSize));
        System.out.println(sql);
        return lista;
    }

    public int totalRecordsDigitador(Usuario sessionUsuario, int estado, StringBuilder filters) {
        Conexion con = new Conexion();
        int totalRecords = 0;
        String sql = "select count(*) as total "
                + "from cabecera "
                + "where codigopdv "
                + "in (select codigopdv from pdv,usuario where usuario.idusuario=? and pdv.idusuario=?) "
                + "and estado=? "
                + "[FILTERS]";
        if (filters.length() > 0) {
            sql = sql.replace("[FILTERS]", filters.toString());
        } else {
            sql = sql.replace("[FILTERS]", "");
        }

        try {
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            pst.setInt(1, sessionUsuario.getIdusuario());
            pst.setInt(2, sessionUsuario.getIdusuario());
            pst.setInt(3, estado);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                totalRecords = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                System.out.println("ERROR COUNT RECORDS LAZY :" + ex.getMessage());
            }
        }
        System.out.println("Total Records : " + totalRecords);
        return totalRecords;
    }

    public List<CabeceraMovimiento> loadWorkingAreaFarmacia(Usuario sessionUsuario, int first, int pageSize, StringBuilder filters) {
        Conexion con = new Conexion();
        List<CabeceraMovimiento> lista = new ArrayList<>();
        String sql = "select * from("
                + "select idcabecera,codigopdv,nombrearchivo,rutaarchivoorigen,rutaarchivodestino,fechareceta,fechascanner,estado,"
                + "ROW_NUMBER() OVER (ORDER BY fechascanner desc) as row "
                + "from cabecera "
                + "where codigopdv=? "
                + "[FILTERS]"
                + ") a where row >? and row<=?";

        if (filters.length() > 0) {
            sql = sql.replace("[FILTERS]", filters.toString());
        } else {
            sql = sql.replace("[FILTERS]", "");
        }
        System.out.println(sql);
        try {
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            pst.setString(1, sessionUsuario.getLoginname());
            pst.setInt(2, first);
            pst.setInt(3, first + pageSize);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                CabeceraMovimiento cab = new CabeceraMovimiento();
                cab.setIdCabecera(rs.getLong(1));
                cab.setCodigoPDV(rs.getString(2));
                cab.setNombreArchivo(rs.getString(3));
                cab.setRutaArchivoDestino(rs.getString(5));
                cab.setFechaReceta(rs.getTimestamp(6));
                cab.setFechaArchivo(rs.getTimestamp(7));
                cab.setEstado(rs.getInt(8));
                lista.add(cab);
            }
        } catch (Exception e) {
            System.out.println("TRANSACCIONDAO LOADWORKINGAREA DIGITADOR: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    public int totalRecordsFarmacia(Usuario sessionUsuario, StringBuilder filters) {
        Conexion con = new Conexion();
        int totalRecords = 0;
        String sql = "select count(*) as total "
                + "from cabecera "
                + "where codigopdv=? "
                + "[FILTERS]";
        if (filters.length() > 0) {
            sql = sql.replace("[FILTERS]", filters.toString());
        } else {
            sql = sql.replace("[FILTERS]", "");
        }

        try {
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            pst.setString(1, sessionUsuario.getLoginname());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                totalRecords = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                System.out.println("ERROR COUNT RECORDS LAZY :" + ex.getMessage());
            }
        }
        System.out.println("Total Records : " + totalRecords);
        return totalRecords;
    }

    public CabeceraMovimiento cargarTransaccion(String nombreArchivo) {
        CabeceraMovimiento cab = new CabeceraMovimiento();
        Conexion con = new Conexion();
        String sql = "select c.idcabecera,c.nombrearchivo,c.rutaarchivodestino,c.idmedico,c.fuentemedico,mb.nombres,mb.apellidos,c.fechareceta,null as cedula from cabecera c "
                + "inner join medico_bottago mb on mb.idmedico=convert(int,(convert( varchar(max),c.idmedico))) "
                + "where c.nombrearchivo=? and mb.fuentemedico=c.fuentemedico "
                + "union all "
                + "select c.idcabecera,c.nombrearchivo,c.rutaarchivodestino,c.idmedico,c.fuentemedico,md.nombres,md.apellidos,c.fechareceta,md.cedula as cedula from cabecera c "
                + "inner join medico_difare md on md.cedula=convert(varchar(max),c.idmedico) "
                + "where c.nombrearchivo=? and md.fuentemedico=c.fuentemedico";
        try {
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            pst.setString(1, nombreArchivo);
            pst.setString(2, nombreArchivo);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Medico tempMedico = new Medico();
                tempMedico.setIdMedico(rs.getString(4) == null ? null : Long.parseLong(rs.getString(4)));
                tempMedico.setFuente(rs.getString(5));
                tempMedico.setNombres(rs.getString(6));
                tempMedico.setApellidos(rs.getString(7));
                tempMedico.setCedula(rs.getString(9));
                cab.setIdCabecera(rs.getLong(1));
                cab.setNombreArchivo(rs.getString(2));
                cab.setRutaArchivoDestino(rs.getString(3));
                cab.setFechaReceta(rs.getTimestamp(8));
                cab.setMedico(tempMedico);
            }
            String sql2 = "select d.iddetalle,d.idcabecera,d.secuencial,d.cantidad,pb.idproducto,pb.fuenteproducto,pb.marca,pb.sustituto,pb.forma,pb.concentracion,null as laboratorio,d.referenciatiempo from detalle d "
                    + "inner join producto_bottago pb on pb.idproducto=d.idproducto where d.idcabecera=? "
                    + "and pb.fuenteproducto=d.fuenteproducto union all "
                    + "select d.iddetalle,d.idcabecera,d.secuencial,d.cantidad,pd.idproducto,pd.fuenteproducto,pd.marca,pd.sustituto,pd.forma1,pd.concentracion,pd.laboratorio,d.referenciatiempo from detalle d "
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
                pro.setIdProducto(rs.getLong(5));
                pro.setFuente(rs.getString(6));
                pro.setMarca(rs.getString(7));
                pro.setSustituto(rs.getString(8));
                pro.setForma(rs.getString(9));
                pro.setConcentracion(rs.getString(10));
                pro.setLaboratorio(rs.getString(11));
                DetalleMovimiento det = new DetalleMovimiento();
                det.setIdDetalle(rs.getLong(1));
                det.setIdCabecera(rs.getLong(2));
                det.setSecuencial(rs.getInt(3));
                det.setCantidad(rs.getInt(4));
                det.setReferenciaTiempo(new Date(rs.getTimestamp(12).getTime()));
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

    public CabeceraMovimiento cargarTransaccionDesechada(String nombreArchivo) {
        CabeceraMovimiento cab = new CabeceraMovimiento();
        Conexion con = new Conexion();
        String sql = "select c.idcabecera,c.nombrearchivo,c.rutaarchivodestino,c.idmedico,c.fuentemedico,c.fechareceta from cabecera c "
                + "where c.nombrearchivo= ?";
        try {
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            pst.setString(1, nombreArchivo);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Medico tempMedico = new Medico();
                tempMedico.setIdMedico(rs.getString(4) == null ? null : Long.parseLong(rs.getString(4)));
                tempMedico.setFuente(rs.getString(5));
                cab.setIdCabecera(rs.getLong(1));
                cab.setNombreArchivo(rs.getString(2));
                cab.setRutaArchivoDestino(rs.getString(3));
                cab.setFechaReceta(rs.getTimestamp(6));
                cab.setMedico(tempMedico);
            }
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

    public void procesarTransaccion(CabeceraMovimiento cab, CabeceraMovimiento row, String opcion, Usuario sessionUsuario) {
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
            pst.setString(1, cab.getMedico().getCedula());
            pst.setString(2, cab.getMedico().getFuente());
            pst.setString(3, row.getCodigoPDV());
            pst.setString(4, row.getNombreArchivo());
            pst.setString(5, row.getRutaArchivoDestino());
            String rutadestino = directorioBase + row.getCodigoPDV() + SEPARADOR + opcion + SEPARADOR + row.getNombreArchivo();
            pst.setString(6, rutadestino);
            pst.setDate(7, java.sql.Date.valueOf(sdf.format(cab.getFechaReceta())));
            pst.setTimestamp(8, new java.sql.Timestamp(row.getFechaArchivo().getTime()));
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
            Filesmethods.transferFiletransaccion(directorioBase + row.getCodigoPDV() + SEPARADOR, rutadestino, opcion, row.getNombreArchivo(), row);
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

    public boolean editarTransaccion(CabeceraMovimiento cab, CabeceraMovimiento row, String opcion, Usuario sessionUsuario, boolean esraiz) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fileMove = row.getEstado() == 0 ? "RAIZ" : row.getEstado() == 1 ? "PROCESADAS" : row.getEstado() == 2 ? "DESECHADAS" : "OTROS";
        boolean flag = false;
        String SEPARADOR = File.separator;
        Conexion con = new Conexion();
        String sql = "update cabecera set idmedico=?,fuentemedico=?,rutaarchivoorigen=?,rutaarchivodestino=?,fechareceta=?,fechapantallainit=?,"
                + "fecharegistro=?,numeroregistros=?,idusuario=?,estado=? where nombrearchivo=?";
        try {
            con.getConnection().setAutoCommit(false);
            String directorioBase = new ParametrosDAO().parametroDirectorioRaiz();
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            String idmedico = cab.getMedico().getFuente().equals("D") ? cab.getMedico().getCedula() : cab.getMedico().getIdMedico().toString();
            pst.setString(1, idmedico);
            pst.setString(2, cab.getMedico().getFuente());
            pst.setString(3, row.getRutaArchivoDestino());
            String rutadestino = directorioBase + row.getCodigoPDV() + SEPARADOR + opcion + SEPARADOR + row.getNombreArchivo();
            pst.setString(4, rutadestino);
            pst.setDate(5, java.sql.Date.valueOf(sdf.format(cab.getFechaReceta())));
            pst.setTimestamp(6, new java.sql.Timestamp(cab.getPantallaInit().getTime()));
            pst.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
            pst.setInt(8, cab.getListaDetalleProducto().size());
            pst.setString(9, sessionUsuario.getLoginname());
            pst.setInt(10, 1);
            pst.setString(11, row.getNombreArchivo());
            pst.executeUpdate();
            borrarDetalle(cab, con);
            String sql2 = "insert into detalle (idcabecera,idproducto,fuenteproducto,secuencial,cantidad,referenciatiempo) "
                    + "values (?,?,?,?,?,?)";
            pst = con.getConnection().prepareStatement(sql2);
            for (DetalleMovimiento det : cab.getListaDetalleProducto()) {
                pst.setInt(1, cab.getIdCabecera().intValue());
                pst.setInt(2, det.getProducto().getIdProducto().intValue());
                pst.setString(3, det.getProducto().getFuente());
                pst.setInt(4, det.getSecuencial());
                pst.setInt(5, det.getCantidad());
                pst.setTimestamp(6, new java.sql.Timestamp(det.getReferenciaTiempo().getTime()));
                pst.executeUpdate();
                flag = true;
            }
            if (!opcion.equals(fileMove)) {
                flag = Filesmethods.transferFiletransaccion(directorioBase + row.getCodigoPDV() + SEPARADOR, rutadestino, opcion, row.getNombreArchivo(), row);
            }
            if (flag) {
                con.getConnection().commit();
            } else {
                con.getConnection().rollback();
            }

        } catch (Exception e) {
            try {
                System.out.println("TRANSACCIONDAO EDITANDO TRANSACCION: " + e.getMessage());
                con.getConnection().rollback();
            } catch (SQLException ex) {
                Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            flag = false;
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return flag;
    }

    public boolean desecharTransaccion(CabeceraMovimiento cab, CabeceraMovimiento row, String opcion, Usuario sessionUsuario) {
        Conexion con = new Conexion();
        boolean flag = false;
        String directorioBase = new ParametrosDAO().parametroDirectorioRaiz();
        String SEPARADOR = File.separator;
        String sql = "update cabecera set estado=?,rutaarchivoorigen=?,rutaarchivodestino=?, "
                + "fechapantallainit=?,fecharegistro=?,numeroregistros=0,idusuario=? where idcabecera=?";
        try {
            con.getConnection().setAutoCommit(false);
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            pst.setInt(1, 2);
            pst.setString(2, row.getRutaArchivoDestino());
            String rutadestino = directorioBase + row.getCodigoPDV() + SEPARADOR + opcion + SEPARADOR + row.getNombreArchivo();
            pst.setString(3, rutadestino);
            pst.setTimestamp(4, new java.sql.Timestamp(cab.getPantallaInit().getTime()));
            pst.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
            pst.setString(6, sessionUsuario.getLoginname());
            pst.setInt(7, cab.getIdCabecera().intValue());
            pst.executeUpdate();
            borrarDetalle(cab, con);
            flag = Filesmethods.transferFiletransaccion(directorioBase + row.getCodigoPDV() + SEPARADOR, rutadestino, opcion, row.getNombreArchivo(), row);
            if (flag) {
                con.getConnection().commit();
            } else {
                con.getConnection().rollback();
            }
        } catch (Exception e) {
            System.out.println("TRANSACCIONDAO DESECHANDO TRANSACCION: " + e.getMessage());
            flag = false;
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return flag;
    }

    public void desecharRaiz(CabeceraMovimiento cab, CabeceraMovimiento row, String opcion, Usuario sessionUsuario) {
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
            pst.setString(1, row.getCodigoPDV());
            pst.setString(2, row.getNombreArchivo());
            pst.setString(3, row.getRutaArchivoDestino());
            String rutadestino = directorioBase + row.getCodigoPDV() + SEPARADOR + opcion + SEPARADOR + row.getNombreArchivo();
            pst.setString(4, rutadestino);
            pst.setTimestamp(5, new java.sql.Timestamp(row.getFechaArchivo().getTime()));
            pst.setTimestamp(6, new java.sql.Timestamp(cab.getPantallaInit().getTime()));
            pst.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
            pst.setString(8, sessionUsuario.getLoginname());
            pst.setInt(9, 2);
            pst.executeUpdate();
//            ResultSet rs = pst.getGeneratedKeys();
//            int generatedIdCabecera = 0;
//            if (rs.next()) {
//                generatedIdCabecera = rs.getInt(1);
//            }
//
//            String sql2 = "insert into detalle (idcabecera,idproducto,fuenteproducto,cantidad) "
//                    + "values (?,?,?,?)";
//            pst = con.getConnection().prepareStatement(sql2);
//            for (DetalleMovimiento det : cab.getListaDetalleProducto()) {
//                pst.setInt(1, generatedIdCabecera);
//                pst.setInt(2, det.getProducto().getIdProducto().intValue());
//                pst.setString(3, det.getProducto().getFuente());
//                pst.setInt(4, det.getCantidad());
//                pst.executeUpdate();
//            }
            con.getConnection().commit();
            Filesmethods.transferFiletransaccion(directorioBase + row.getCodigoPDV() + SEPARADOR, rutadestino, opcion, row.getNombreArchivo(), row);
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

    public void borrarDetalle(CabeceraMovimiento cab, Conexion con) {
        String sql = "delete from detalle where idcabecera=?";
        try {
            con.getConnection().setAutoCommit(false);
            PreparedStatement pst = con.getConnection().prepareStatement(sql);
            pst.setInt(1, cab.getIdCabecera().intValue());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("TRANSACCIONDAO BORRANDO DETALLE TRANSACCION: " + e.getMessage());
        }
    }
}
