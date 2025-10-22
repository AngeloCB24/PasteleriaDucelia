package utilidades;

import org.mindrot.jbcrypt.BCrypt;

public class GenerarHashLocal {
    public static void main(String[] args) {
        String hash = BCrypt.hashpw("1234", BCrypt.gensalt(12));
        System.out.println("Hash generado con esta librería:");
        System.out.println(hash);
    }
}
