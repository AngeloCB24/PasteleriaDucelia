package dao;

import conexion.ConexionBD;
import modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, code, name, description, category_id, supplier_id, unit, stock, stock_min, price, expiration_date FROM products ORDER BY name";
        try (Connection c = ConexionBD.getConexion(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("code"));
                p.setNombre(rs.getString("name"));
                p.setDescripcion(rs.getString("description"));
                int cat = rs.getInt("category_id");
                if (!rs.wasNull()) {
                    p.setCategoriaId(cat);
                }
                int sup = rs.getInt("supplier_id");
                if (!rs.wasNull()) {
                    p.setProveedorId(sup);
                }
                p.setUnidad(rs.getString("unit"));
                p.setStock(rs.getInt("stock"));
                p.setStockMin(rs.getInt("stock_min"));
                p.setPrecio(rs.getDouble("price"));
                p.setFechaVencimiento(rs.getDate("expiration_date"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Producto findById(int id) {
        String sql = "SELECT id, code, name, description, category_id, supplier_id, unit, stock, stock_min, price, expiration_date FROM products WHERE id = ?";
        try (Connection c = ConexionBD.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setCodigo(rs.getString("code"));
                    p.setNombre(rs.getString("name"));
                    p.setDescripcion(rs.getString("description"));
                    int cat = rs.getInt("category_id");
                    if (!rs.wasNull()) {
                        p.setCategoriaId(cat);
                    }
                    int sup = rs.getInt("supplier_id");
                    if (!rs.wasNull()) {
                        p.setProveedorId(sup);
                    }
                    p.setUnidad(rs.getString("unit"));
                    p.setStock(rs.getInt("stock"));
                    p.setStockMin(rs.getInt("stock_min"));
                    p.setPrecio(rs.getDouble("price"));
                    p.setFechaVencimiento(rs.getDate("expiration_date"));
                    return p;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertar(Producto p) {
        String sql = "INSERT INTO products (code, name, description, category_id, supplier_id, unit, stock, stock_min, price, expiration_date) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = ConexionBD.getConexion(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            if (p.getCategoriaId() == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, p.getCategoriaId());
            }
            if (p.getProveedorId() == null) {
                ps.setNull(5, Types.INTEGER);
            } else {
                ps.setInt(5, p.getProveedorId());
            }
            ps.setString(6, p.getUnidad());
            ps.setInt(7, p.getStock());
            ps.setInt(8, p.getStockMin());
            ps.setDouble(9, p.getPrecio());
            if (p.getFechaVencimiento() == null) {
                ps.setNull(10, Types.DATE);
            } else {
                ps.setDate(10, p.getFechaVencimiento());
            }
            int aff = ps.executeUpdate();
            if (aff == 1) {
                try (ResultSet k = ps.getGeneratedKeys()) {
                    if (k.next()) {
                        p.setId(k.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE products SET code=?, name=?, description=?, category_id=?, supplier_id=?, unit=?, stock=?, stock_min=?, price=?, expiration_date=? WHERE id=?";
        try (Connection c = ConexionBD.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            if (p.getCategoriaId() == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, p.getCategoriaId());
            }
            if (p.getProveedorId() == null) {
                ps.setNull(5, Types.INTEGER);
            } else {
                ps.setInt(5, p.getProveedorId());
            }
            ps.setString(6, p.getUnidad());
            ps.setInt(7, p.getStock());
            ps.setInt(8, p.getStockMin());
            ps.setDouble(9, p.getPrecio());
            if (p.getFechaVencimiento() == null) {
                ps.setNull(10, Types.DATE);
            } else {
                ps.setDate(10, p.getFechaVencimiento());
            }
            ps.setInt(11, p.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection c = ConexionBD.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // actualizar stock (positivo/negativo) de forma segura
    public boolean actualizarStock(int productId, int cantidadDelta) {
        String sql = "UPDATE products SET stock = stock + ? WHERE id = ? AND (stock + ?) >= 0";
        try (Connection c = ConexionBD.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cantidadDelta);
            ps.setInt(2, productId);
            ps.setInt(3, cantidadDelta);
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
