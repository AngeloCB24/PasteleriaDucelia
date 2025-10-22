package vistas;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private final Usuario usuario; // 👈 guardamos el usuario actual

    public MenuPrincipal(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Menú Principal - Bienvenido " + usuario.getNombreCompleto());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        JLabel titulo = new JLabel("¿A qué módulo deseas ingresar?");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton btnVentas = new JButton("Gestión de Ventas");
        JButton btnInventario = new JButton("Gestión de Inventario");

        btnVentas.setPreferredSize(new Dimension(200, 40));
        btnInventario.setPreferredSize(new Dimension(200, 40));

        btnVentas.addActionListener(e -> {
            new VistaVentas(usuario).setVisible(true); // ✅ pasamos el usuario actual
            dispose(); // opcional: cierra el menú
        });

        btnInventario.addActionListener(e -> {
            new VistaInventario().setVisible(true); // si quieres también puedes pasar usuario
            dispose();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0; gbc.gridy = 0; add(titulo, gbc);
        gbc.gridy = 1; add(btnVentas, gbc);
        gbc.gridy = 2; add(btnInventario, gbc);
    }
}
