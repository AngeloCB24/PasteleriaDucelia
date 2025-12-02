package dao;

import conexion.ConexionBD;
import modelo.MovimientoInventario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoInventarioDAO {

    /**
     * Registra un movimiento y actualiza el stock de forma transaccional.
     * Acepta movementType en forma "IN"/"OUT" o "ENTRADA"/"SALIDA".
     */
    public boolean registrarMovimiento(MovimientoInventario m) throws SQLException {
        // insert sin columna 'reference' (asegúrate que la tabla inventory_movements no tiene esa columna)
        String insertMov = "INSERT INTO inventory_movements (product_id, movement_type, quantity, note, date, user_id) VALUES (?,?,?,?,?,?)";
        String updStockIn = "UPDATE products SET stock = stock + ? WHERE id = ?";
        String updStockOut = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

        try (Connection c = ConexionBD.getConexion()) {
            try {
                c.setAutoCommit(false);

                // normalizar movement_type al enum de la tabla si viene como IN/OUT
                String mt = m.getMovementType();
                if (mt == null) {
                    mt = "";
                }
                mt = mt.trim().toUpperCase(); // normalizar

                System.out.println("MOV TYPE RECIBIDO = " + mt);

                if (mt.equals("IN") || mt.equals("ENTRADA")) {
                    mt = "ENTRADA";
                } else if (mt.equals("OUT") || mt.equals("SALIDA")) {
                    mt = "SALIDA";
                } else {
                    throw new SQLException("movement_type inválido: " + mt);
                }
                
                try (PreparedStatement psMov = c.prepareStatement(insertMov, Statement.RETURN_GENERATED_KEYS)) {
                    // product_id: si tu modelo usa int primitivo, considera que 0 o negativo = no establecido
                    int productId = m.getProductId();
                    if (productId <= 0) {
                        psMov.setNull(1, Types.INTEGER);
                    } else {
                        psMov.setInt(1, productId);
                    }

                    psMov.setString(2, mt);
                    psMov.setInt(3, m.getQuantity());
                    psMov.setString(4, m.getNote());

                    if (m.getDate() == null) {
                        psMov.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    } else {
                        psMov.setTimestamp(5, m.getDate());
                    }

                    // userId: si tu modelo usa Integer (nullable) mantenemos la comprobación
                    Integer userId = m.getUserId();
                    if (userId == null) {
                        psMov.setNull(6, Types.INTEGER);
                    } else {
                        psMov.setInt(6, userId);
                    }

                    psMov.executeUpdate();

                    try (ResultSet keys = psMov.getGeneratedKeys()) {
                        if (keys.next()) {
                            m.setId(keys.getInt(1));
                        }
                    }
                }

                // actualizar stock según tipo
                if ("ENTRADA".equalsIgnoreCase(mt)) {
                    try (PreparedStatement ps = c.prepareStatement(updStockIn)) {
                        ps.setInt(1, m.getQuantity());
                        ps.setInt(2, m.getProductId());
                        ps.executeUpdate();
                    }
                } else { // SALIDA
                    try (PreparedStatement ps = c.prepareStatement(updStockOut)) {
                        ps.setInt(1, m.getQuantity());
                        ps.setInt(2, m.getProductId());
                        ps.setInt(3, m.getQuantity());
                        int rows = ps.executeUpdate();
                        if (rows != 1) {
                            throw new SQLException("Stock insuficiente o producto no encontrado (product_id=" + m.getProductId() + ")");
                        }
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
        String sql = "SELECT id, product_id, movement_type, quantity, note, date, user_id FROM inventory_movements ORDER BY date DESC LIMIT ?";
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
                    int uid = rs.getInt("user_id");
                    if (!rs.wasNull()) m.setUserId(uid);
                    lista.add(m);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}