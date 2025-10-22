package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import utilidades.Encriptacion;

public class ControladorLogin {
    
    private UsuarioDAO dao = new UsuarioDAO();

    public Usuario iniciarSesion(String usuario, String clave) {
        Usuario u = dao.buscarPorUsuario(usuario);
        if (u == null) {
            System.out.println("⚠ Usuario no encontrado en BD");
            return null;
        }
        System.out.println("→ Clave ingresada: '" + clave + "'");
        System.out.println("→ Hash en BD: " + u.getClaveHash());
        System.out.println("→ Coincide? " + Encriptacion.verificar(clave, u.getClaveHash()));

        if (Encriptacion.verificar(clave, u.getClaveHash())) {
            return u;
        } else {
            return null;
        }
    }
}


