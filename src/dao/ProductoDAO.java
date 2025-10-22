package dao;

import conexion.ConexionBD;
import modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY name";
        try (Connection c = ConexionBD.getConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("code"));
                p.setNombre(rs.getString("name"));
                p.setDescripcion(rs.getString("description"));
                p.setIdCategoria((Integer) rs.getObject("category_id"));
                p.setIdProveedor((Integer) rs.getObject("supplier_id"));
                p.setUnidad(rs.getString("unit"));
                p.setStock(rs.getInt("stock"));
                p.setStockMinimo(rs.getInt("stock_min"));
                p.setPrecio(rs.getDouble("price"));
                p.setFechaVencimiento(rs.getDate("expiration_date"));
                lista.add(p);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }
}
