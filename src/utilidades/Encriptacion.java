package utilidades;

import org.mindrot.jbcrypt.BCrypt;

public class Encriptacion {

    public static String generarHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean verificar(String password, String hash) {
        if (hash == null || hash.trim().isEmpty()) {
            return false;
        }
        return BCrypt.checkpw(password, hash);
    }
}
