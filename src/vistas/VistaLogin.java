package vistas;

import controlador.ControladorLogin;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class VistaLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private final ControladorLogin controlador;

    public VistaLogin() {
        controlador = new ControladorLogin();
        setTitle("Inicio de Sesión - Pastelería Ducelia");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal con imagen de fondo
        JLabel fondo = new JLabel();
        fondo.setLayout(new GridBagLayout());
        fondo.setIcon(new ImageIcon("src/recursos/fondo_login.jpg")); // ← Cambia esta ruta si tu imagen está en otra carpeta

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelLogin = new JPanel(new GridBagLayout());
        panelLogin.setOpaque(false);
        panelLogin.setPreferredSize(new Dimension(400, 300));
        panelLogin.setBorder(BorderFactory.createTitledBorder("Acceso al sistema"));
        panelLogin.setBackground(new Color(255, 255, 255, 150));

        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblContrasena = new JLabel("Contraseña:");
        txtUsuario = new JTextField(20);
        txtContrasena = new JPasswordField(20);
        JButton btnLogin = new JButton("Ingresar");

        gbc.gridx = 0; gbc.gridy = 0;
        panelLogin.add(lblUsuario, gbc);
        gbc.gridx = 1;
        panelLogin.add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelLogin.add(lblContrasena, gbc);
        gbc.gridx = 1;
        panelLogin.add(txtContrasena, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelLogin.add(btnLogin, gbc);

        fondo.add(panelLogin, gbc);
        add(fondo, BorderLayout.CENTER);

        // Acción del botón
        btnLogin.addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String contrasena = new String(txtContrasena.getPassword());

        Usuario user = controlador.iniciarSesion(usuario, contrasena);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + user.getNombreCompleto());
            new MenuPrincipal(user).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales inválidas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaLogin().setVisible(true));
    }
}
