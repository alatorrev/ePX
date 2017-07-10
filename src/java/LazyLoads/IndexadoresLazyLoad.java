/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LazyLoads;

import com.epx.dao.TransaccionDAO;
import com.epx.entity.CabeceraMovimiento;
import com.epx.entity.Usuario;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author BOTTAGO
 */
public class IndexadoresLazyLoad extends LazyDataModel<CabeceraMovimiento> {

    private List<CabeceraMovimiento> lista;
    private Usuario sessionUsuario;
    private int estado;

    public IndexadoresLazyLoad(Usuario sessionUsuario, int estado) {
        this.sessionUsuario = sessionUsuario;
        this.estado = estado;
    }
    
    @Override
    public List<CabeceraMovimiento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        StringBuilder sb = new StringBuilder();
        if (filters != null) {
            for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                try {
                    String filterProperty = it.next();
                    Object filterValue = filters.get(filterProperty);
                    sb.append("AND CONVERT(NVARCHAR,"+filterProperty+", 120) like '"+filterValue+"%' \n");
                } catch (Exception e) {
                }
            }
        }
        this.setRowCount(new TransaccionDAO().totalRecordsDigitador(sessionUsuario, estado,sb));
        lista = new TransaccionDAO().loadWorkingAreaDigitador(sessionUsuario, estado, first, pageSize,sb);
        return lista;
    }

    @Override
    public Object getRowKey(CabeceraMovimiento object) {
        return object.getNombreArchivo();
    }

    @Override
    public CabeceraMovimiento getRowData(String rowKey) {
        for (CabeceraMovimiento objTemp : lista) {
            if (objTemp.getNombreArchivo().equals(rowKey)) {
                return objTemp;
            }
        }
        return null;
    }
}
