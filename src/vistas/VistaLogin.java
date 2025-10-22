package vistas;

import controlador.ControladorLogin;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VistaLogin extends JFrame {
    private JTextField txtUsuario = new JTextField(20);
    private JPasswordField txtClave = new JPasswordField(20);
    private JButton btnIngresar = new JButton("Ingresar");
    private final ControladorLogin controlador = new ControladorLogin();

    public VistaLogin() {
        setTitle("Inicio de Sesión - SIGP Ducelia");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(3,2,5,5));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtClave);
        panel.add(new JLabel(""));
        panel.add(btnIngresar);
        add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);

        btnIngresar.addActionListener((ActionEvent e) -> {
            String u = txtUsuario.getText().trim();
            String c = new String(txtClave.getPassword());
            if (u.isEmpty() || c.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }
            Usuario user = controlador.iniciarSesion(u, c);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido " + user.getNombreCompleto());
                new VistaPrincipal(user).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales inválidas.");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaLogin().setVisible(true));
    }
}
