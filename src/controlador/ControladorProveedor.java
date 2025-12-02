package controlador;

import dao.ProveedorDAO;
import modelo.Proveedor;

import java.util.List;

public class ControladorProveedor {

    private ProveedorDAO dao = new ProveedorDAO();

    public List<Proveedor> listar() {
        return dao.listarProveedores();
    }

    public boolean crear(Proveedor p) {
        return dao.crearProveedor(p);
    }

    public boolean actualizar(Proveedor p) {
        return dao.actualizarProveedor(p);
    }

    public boolean eliminar(int id) {
        return dao.eliminarProveedor(id);
    }
}
