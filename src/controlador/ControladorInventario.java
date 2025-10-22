package controlador;

import dao.ProductoDAO;
import dao.MovimientoInventarioDAO;
import modelo.Producto;
import modelo.MovimientoInventario;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControladorInventario {

    private ProductoDAO productoDAO = new ProductoDAO();
    private MovimientoInventarioDAO movDAO = new MovimientoInventarioDAO();

    public List<Producto> listarProductos() {
        return productoDAO.listarProductos();
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

    // 🟠 Verifica si hay productos con bajo stock o próximos a vencer
    public List<String> verificarAlertas() {
        List<String> alertas = new ArrayList<>();
        List<Producto> productos = productoDAO.listarProductos();

        Date hoy = new Date();
        long tresDias = 3L * 24 * 60 * 60 * 1000; // 3 días en milisegundos

        for (Producto p : productos) {
            // Alerta por stock bajo
            if (p.getStock() <= p.getStockMin()) {
                alertas.add("⚠️ El producto '" + p.getNombre() + "' tiene stock bajo (" + p.getStock() + ").");
            }

            // Alerta por vencimiento próximo
            if (p.getFechaVencimiento() != null) {
                long diferencia = p.getFechaVencimiento().getTime() - hoy.getTime();
                if (diferencia <= tresDias && diferencia > 0) {
                    alertas.add("⏰ El producto '" + p.getNombre() + "' está próximo a vencer (" + p.getFechaVencimiento() + ").");
                } else if (diferencia <= 0) {
                    alertas.add("❌ El producto '" + p.getNombre() + "' ya ha vencido (" + p.getFechaVencimiento() + ").");
                }
            }
        }

        return alertas;
    }

}
