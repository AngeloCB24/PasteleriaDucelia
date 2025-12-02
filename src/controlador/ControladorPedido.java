package controlador;

import dao.PedidoDAO;
import modelo.Pedido;

public class ControladorPedido {
    private final PedidoDAO dao = new PedidoDAO();
    public boolean registrarPedido(Pedido p) { return dao.crearPedido(p); }
}
