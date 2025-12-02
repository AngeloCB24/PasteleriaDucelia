package dao;

import conexion.ConexionBD;
import modelo.Usuario;
import java.sql.*;

public class UsuarioDAO {

    public Usuario buscarPorUsuario(String nombreUsuario) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection c = ConexionBD.getConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setUsuario(rs.getString("username"));
                    u.setClaveHash(rs.getString("password_hash"));
                    u.setNombreCompleto(rs.getString("full_name"));
                    u.setIdRol(rs.getInt("role_id"));
                    //u.setCorreo(rs.getString("email"));
                    return u;
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }
}
