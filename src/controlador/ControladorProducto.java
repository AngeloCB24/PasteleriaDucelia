package controlador;

import dao.ProductoDAO;
import modelo.Producto;
import java.util.List;

public class ControladorProducto {

    private final ProductoDAO dao = new ProductoDAO();

    public List<Producto> listarProductos() {
        return dao.listarTodos();
    }
}
