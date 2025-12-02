package dao;

import conexion.ConexionBD;
import modelo.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public List<Proveedor> listarProveedores() {
        List<Proveedor> lista = new ArrayList<>();

        String sql = "SELECT id, name, contact, email, phone FROM suppliers";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Proveedor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact"), // <-- nuevo campo
                        rs.getString("email"), // <-- nuevo campo
                        rs.getInt("phone")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean crearProveedor(Proveedor p) {
        String sql = "INSERT INTO suppliers (name, contact, email, phone) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getContacto());
            ps.setString(3, p.getEmail());
            ps.setInt(4, p.getTelefono());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarProveedor(Proveedor p) {
        String sql = "UPDATE suppliers SET name = ?, contact = ?, email = ?, phone = ? WHERE id = ?";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getContacto());
            ps.setString(3, p.getEmail());
            ps.setInt(4, p.getTelefono());
            ps.setInt(5, p.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarProveedor(int id) {
        String sql = "DELETE FROM suppliers WHERE id = ?";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
