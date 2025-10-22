package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/sigp_ducelia?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CLAVE = "1234"; // tu contraseña de MySQL

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("Error: No se encontró el driver de MySQL.");
        }
    }

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
