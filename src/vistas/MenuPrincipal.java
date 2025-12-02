package vistas;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private final Usuario usuario;

    public MenuPrincipal(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Menú Principal - Bienvenido " + usuario.getNombreCompleto());
        setSize(600, 500); // ← Agrandado para que entre el botón
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Fondo
        FondoPanel fondo = new FondoPanel("/images/fondoLila.png");
        fondo.setLayout(new GridBagLayout());
        setContentPane(fondo);

        JLabel titulo = new JLabel("¿A qué módulo deseas ingresar?");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);

        // Botones
        JButton btnVentas = new JButton("Gestión de Ventas");
        JButton btnInventario = new JButton("Gestión de Inventario");
        JButton btnProveedores = new JButton("Gestión de Proveedores");

        // Botón Cerrar Aplicación 🔴
        JButton btnCerrar = new JButton("Cerrar Aplicación");
        btnCerrar.setBackground(Color.RED);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);

        // Tamaño uniforme
        Dimension d = new Dimension(200, 40);
        btnVentas.setPreferredSize(d);
        btnInventario.setPreferredSize(d);
        btnProveedores.setPreferredSize(d);
        btnCerrar.setPreferredSize(d);

        // Acciones
        btnVentas.addActionListener(e -> {
            new VistaVentas(usuario).setVisible(true);
            dispose();
        });

        btnInventario.addActionListener(e -> {
            new VistaInventario(usuario).setVisible(true);
            dispose();
        });

        btnProveedores.addActionListener(e -> {
            new VistaProveedores(usuario).setVisible(true);
            dispose();
        });

        btnCerrar.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que deseas cerrar la aplicación?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (resp == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // --- Layout ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        gbc.gridx = 0;

        gbc.gridy = 0;
        fondo.add(titulo, gbc);

        gbc.gridy = 1;
        fondo.add(btnVentas, gbc);

        gbc.gridy = 2;
        fondo.add(btnInventario, gbc);

        gbc.gridy = 3;
        fondo.add(btnProveedores, gbc);

        gbc.gridy = 4; // ← ABAJO DEL TODO
        fondo.add(btnCerrar, gbc);
    }

    // Fondo personalizado
    class FondoPanel extends JPanel {

        private Image imagen;

        public FondoPanel(String path) {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                imagen = new ImageIcon(imgURL).getImage();
            } else {
                System.out.println("⚠ No se encontró la imagen: " + path);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
