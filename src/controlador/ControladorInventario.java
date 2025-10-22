package controlador;

import dao.ProductoDAO;
import dao.MovimientoInventarioDAO;
import modelo.Producto;
import modelo.MovimientoInventario;

import java.util.List;

public class ControladorInventario {

    private ProductoDAO productoDAO = new ProductoDAO();
    private MovimientoInventarioDAO movDAO = new MovimientoInventarioDAO();

    public List<Producto> listarProductos() {
        return productoDAO.listarTodos();
    }

    public boolean crearProducto(Producto p) {
        return productoDAO.insertar(p);
    }

    public boolean actualizarProducto(Producto p) {
        return productoDAO.actualizar(p);
    }

    public boolean eliminarProducto(int id) {
        return productoDAO.eliminar(id);
    }

    public boolean registrarEntrada(int productId, int cantidad, Integer userId, String nota, String referencia) {
        MovimientoInventario m = new MovimientoInventario();
        m.setProductId(productId);
        m.setMovementType("IN");
        m.setQuantity(cantidad);
        m.setUserId(userId);
        m.setNote(nota);
        m.setReference(referencia);
        try {
            return movDAO.registrarMovimiento(m);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registrarSalida(int productId, int cantidad, Integer userId, String nota, String referencia) {
        MovimientoInventario m = new MovimientoInventario();
        m.setProductId(productId);
        m.setMovementType("OUT");
        m.setQuantity(cantidad);
        m.setUserId(userId);
        m.setNote(nota);
        m.setReference(referencia);
        try {
            return movDAO.registrarMovimiento(m);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
