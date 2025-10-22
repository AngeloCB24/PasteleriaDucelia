package utilidades;

import org.mindrot.jbcrypt.BCrypt;

public class TestBCrypt {
    public static void main(String[] args) {
        String password = "1234";
        String hash = "$2a$12$YBkcLIl7dDDBs6ZZb8djqucx6mnZxufqkiuT8y1TkTQX5Mb8f7E4a";
        
        System.out.println("Coincide? " + BCrypt.checkpw(password, hash));
    }
}
