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

        // ✅ Panel con imagen de fondo personalizada
        JPanel panelFondo = new JPanel() {
            Image fondo = new ImageIcon("B:/Angelo/Documents/NetBeansProjects/PasteleriaDucelia/src/images/portadaLogin.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Escalar la imagen al tamaño del panel
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelFondo.setLayout(new GridBagLayout());

        // Configuración del panel de login
        JPanel panelLogin = new JPanel(new GridBagLayout());
        panelLogin.setOpaque(false);
        panelLogin.setPreferredSize(new Dimension(400, 250));
        panelLogin.setBorder(BorderFactory.createTitledBorder("Acceso al sistema"));
        panelLogin.setBackground(new Color(255, 255, 255, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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

        panelFondo.add(panelLogin, gbc);
        add(panelFondo, BorderLayout.CENTER);

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
