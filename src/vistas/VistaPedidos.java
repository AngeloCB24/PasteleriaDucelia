package vistas;

import controlador.ControladorPedido;
import modelo.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VistaPedidos extends JFrame {
    private Usuario usuario;
    private ControladorPedido controlador = new ControladorPedido();
    private List<DetallePedido> detalles = new ArrayList<>();

    public VistaPedidos(Usuario u) {
        this.usuario = u;
        setTitle("Registrar Pedido");
        setSize(500,300);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JTextField txtCliente = new JTextField(20);
        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnGuardar = new JButton("Guardar Pedido");

        add(new JLabel("Cliente:"));
        add(txtCliente);
        add(btnAgregar);
        add(btnGuardar);

        btnAgregar.addActionListener(e -> {
            try {
                int idProd = Integer.parseInt(JOptionPane.showInputDialog("ID producto:"));
                int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Cantidad:"));
                double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));
                DetallePedido d = new DetallePedido();
                d.setIdProducto(idProd);
                d.setCantidad(cantidad);
                d.setPrecio(precio);
                detalles.add(d);
                JOptionPane.showMessageDialog(this, "Producto agregado.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos.");
            }
        });

        btnGuardar.addActionListener(e -> {
            if (detalles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto.");
                return;
            }
            Pedido p = new Pedido();
            p.setCodigoExterno("PED-" + System.currentTimeMillis());
            p.setNombreCliente(txtCliente.getText().trim());
            p.setEstado("PENDIENTE");
            p.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            p.setIdUsuario(usuario.getId());
            p.setDetalles(detalles);
            p.setTotal(detalles.stream().mapToDouble(DetallePedido::getSubtotal).sum());

            if (controlador.registrarPedido(p)) {
                JOptionPane.showMessageDialog(this, "Pedido registrado correctamente. Total: " + p.getTotal());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar pedido.");
            }
        });
    }
}
