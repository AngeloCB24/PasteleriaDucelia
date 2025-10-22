package vistas;

import controlador.ControladorInventario;
import modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaInventario extends JFrame {

    private final ControladorInventario controlador = new ControladorInventario();
    private JTable tabla;
    private DefaultTableModel modelo;

    public VistaInventario() {
        setTitle("Gestión de Inventario");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        modelo = new DefaultTableModel(
            new Object[]{"ID", "Código", "Nombre", "Stock", "Stock Mín.", "Precio", "Vencimiento"},
            0
        );
        tabla = new JTable(modelo);
        cargarProductos();

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarProductos());

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarProducto());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarProductos() {
        modelo.setRowCount(0);
        List<Producto> lista = controlador.listarProductos();
        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getCodigo(),
                p.getNombre(),
                p.getStock(),
                p.getStockMin(),
                p.getPrecio(),
                p.getFechaVencimiento()
            });
        }
    }

    private void agregarProducto() {
        String codigo = JOptionPane.showInputDialog("Código del producto:");
        String nombre = JOptionPane.showInputDialog("Nombre:");
        int stock = Integer.parseInt(JOptionPane.showInputDialog("Stock:"));
        int stockMin = Integer.parseInt(JOptionPane.showInputDialog("Stock mínimo:"));
        double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));

        Producto nuevo = new Producto();
        nuevo.setCodigo(codigo);
        nuevo.setNombre(nombre);
        nuevo.setStock(stock);
    }
}
