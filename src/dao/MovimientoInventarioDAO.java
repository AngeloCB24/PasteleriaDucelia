package dao;

import conexion.ConexionBD;
import modelo.MovimientoInventario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoInventarioDAO {

    public boolean registrarMovimiento(MovimientoInventario m) throws SQLException {
        String insertMov = "INSERT INTO inventory_movements (product_id, movement_type, quantity, note, date, user_id, reference) VALUES (?,?,?,?,?,?,?)";
        String updStockIn = "UPDATE products SET stock = stock + ? WHERE id = ?";
        String updStockOut = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

        try (Connection c = ConexionBD.getConexion()) {
            try {
                c.setAutoCommit(false);

                try (PreparedStatement psMov = c.prepareStatement(insertMov, Statement.RETURN_GENERATED_KEYS)) {
                    psMov.setInt(1, m.getProductId());
                    psMov.setString(2, m.getMovementType());
                    psMov.setInt(3, m.getQuantity());
                    psMov.setString(4, m.getNote());
                    if (m.getDate() == null) psMov.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    else psMov.setTimestamp(5, m.getDate());
                    if (m.getUserId() == null) psMov.setNull(6, Types.INTEGER); else psMov.setInt(6, m.getUserId());
                    psMov.setString(7, m.getReference());
                    psMov.executeUpdate();
                }

                if ("IN".equalsIgnoreCase(m.getMovementType())) {
                    try (PreparedStatement ps = c.prepareStatement(updStockIn)) {
                        ps.setInt(1, m.getQuantity());
                        ps.setInt(2, m.getProductId());
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = c.prepareStatement(updStockOut)) {
                        ps.setInt(1, m.getQuantity());
                        ps.setInt(2, m.getProductId());
                        ps.setInt(3, m.getQuantity());
                        int rows = ps.executeUpdate();
                        if (rows != 1) throw new SQLException("Stock insuficiente");
                    }
                }

                c.commit();
                return true;
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<MovimientoInventario> listarUltimos(int limit) {
        List<MovimientoInventario> lista = new ArrayList<>();
        String sql = "SELECT id, product_id, movement_type, quantity, note, date, user_id, reference FROM inventory_movements ORDER BY date DESC LIMIT ?";
        try (Connection c = ConexionBD.getConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MovimientoInventario m = new MovimientoInventario();
                    m.setId(rs.getInt("id"));
                    m.setProductId(rs.getInt("product_id"));
                    m.setMovementType(rs.getString("movement_type"));
                    m.setQuantity(rs.getInt("quantity"));
                    m.setNote(rs.getString("note"));
                    m.setDate(rs.getTimestamp("date"));
                    m.setUserId(rs.getInt("user_id"));
                    m.setReference(rs.getString("reference"));
                    lista.add(m);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }
}
