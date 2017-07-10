/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LazyLoads;

import com.epx.dao.ProductoDAO;
import com.epx.entity.Producto;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author BOTTAGO
 */
public class ProductosLazyLoad extends LazyDataModel<Producto> {

    private List<Producto> lista;

    @Override
    public List<Producto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        StringBuilder sb = new StringBuilder();
        int cont = 0;
        if (filters != null) {
            for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                cont++;
                try {
                    if (cont <= 1) {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        sb.append("WHERE CONVERT(NVARCHAR," + filterProperty + ", 120) like '" + filterValue + "%' \n");
                    } else {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        sb.append("AND CONVERT(NVARCHAR," + filterProperty + ", 120) like '" + filterValue + "%' \n");
                    }
                } catch (Exception e) {
                }
            }
        }
        this.setRowCount(new ProductoDAO().totalRecordsProducto(sb));
        lista = new ProductoDAO().findAllProductoSearch(first, pageSize, sb);
        return lista;
    }

    @Override
    public Object getRowKey(Producto object) {
        return object.getIdProducto().toString() + object.getFuente();
    }

    @Override
    public Producto getRowData(String rowKey) {
        for (Producto prod : lista) {
            if ((prod.getIdProducto().toString() + prod.getFuente()).equals(rowKey)) {
                return prod;
            }
        }
        return null;
    }

}
