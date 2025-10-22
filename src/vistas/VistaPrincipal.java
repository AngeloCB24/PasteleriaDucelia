package vistas;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {
    private Usuario usuarioActual;

    public VistaPrincipal(Usuario user) {
        this.usuarioActual = user;
        setTitle("Sistema de Ventas - " + user.getNombreCompleto());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900,600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Gestión de Ventas - Pastelería Ducelia", SwingConstants.CENTER);
        panel.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        JButton btnProductos = new JButton("Productos");
        JButton btnPedidos = new JButton("Registrar Pedido");
        centro.add(btnProductos);
        centro.add(btnPedidos);
        panel.add(centro, BorderLayout.CENTER);

        add(panel);

        btnProductos.addActionListener(e -> new VistaProductos().setVisible(true));
        btnPedidos.addActionListener(e -> new VistaPedidos(usuarioActual).setVisible(true));
    }
}
