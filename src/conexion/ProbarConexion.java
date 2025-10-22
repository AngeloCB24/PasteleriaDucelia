package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ProbarConexion {
    public static void main(String[] args) {
        try {
            Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sigp_ducelia?useSSL=false&serverTimezone=UTC",
                "root", "1234"
            );
            System.out.println("✅ Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
        }
    }
}
