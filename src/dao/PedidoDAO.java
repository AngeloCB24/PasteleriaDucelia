package dao;

import conexion.ConexionBD;
import modelo.Pedido;
import modelo.DetallePedido;

import java.sql.*;

public class PedidoDAO {

    public boolean crearPedido(Pedido pedido) {
        String sqlPedido = "INSERT INTO orders (external_code, customer_name, total, status, created_at, user_id, notes) VALUES (?,?,?,?,?,?,?)";
        String sqlDetalle = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?,?,?,?)";
        String sqlStock = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

        try (Connection c = ConexionBD.getConexion()) {
            c.setAutoCommit(false);

            try (PreparedStatement psPedido = c.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                psPedido.setString(1, pedido.getCodigoExterno());
                psPedido.setString(2, pedido.getNombreCliente());
                psPedido.setDouble(3, pedido.getTotal());
                psPedido.setString(4, pedido.getEstado());
                psPedido.setTimestamp(5, pedido.getFechaCreacion());
                if (pedido.getIdUsuario() == null) psPedido.setNull(6, Types.INTEGER);
                else psPedido.setInt(6, pedido.getIdUsuario());
                psPedido.setString(7, pedido.getNotas());
                psPedido.executeUpdate();

                try (ResultSet keys = psPedido.getGeneratedKeys()) {
                    if (keys.next()) pedido.setId(keys.getInt(1));
                }
            }

            for (DetallePedido d : pedido.getDetalles()) {
                try (PreparedStatement psStock = c.prepareStatement(sqlStock)) {
                    psStock.setInt(1, d.getCantidad());
                    psStock.setInt(2, d.getIdProducto());
                    psStock.setInt(3, d.getCantidad());
                    int rows = psStock.executeUpdate();
                    if (rows != 1) throw new SQLException("Stock insuficiente para producto ID: " + d.getIdProducto());
                }

                try (PreparedStatement psDet = c.prepareStatement(sqlDetalle)) {
                    psDet.setInt(1, pedido.getId());
                    psDet.setInt(2, d.getIdProducto());
                    psDet.setInt(3, d.getCantidad());
                    psDet.setDouble(4, d.getPrecio());
                    psDet.executeUpdate();
                }
            }

            c.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
