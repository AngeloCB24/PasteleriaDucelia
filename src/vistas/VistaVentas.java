package vistas;

import conexion.ConexionBD;
import controlador.ControladorInventario;
import dao.ProductoDAO;
import modelo.Usuario;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.Timer;

public class VistaVentas extends JFrame {

    private Usuario usuarioActual;
    private JComboBox<String> cmbPostre;
    private JTextField txtCliente, txtPrecio, txtCantidad;
    private JTextArea txtDescripcion;
    private JLabel lblTotal, lblFecha, lblHora;
    private DefaultTableModel modelo;
    private Map<String, Producto> productosMap = new HashMap<>();

    public VistaVentas(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("GESTIÓN DE VENTAS - Usuario: " + (usuario != null ? usuario.getNombreCompleto() : "Invitado"));
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Usa la ruta relativa dentro del JAR/proyecto; si no existe, FondoPanel intentará cargar desde disco
        FondoPanel fondo = new FondoPanel("/images/fondoLila.png");

        fondo.setLayout(null);
        setContentPane(fondo);

        inicializarComponentes(fondo);
    }

    // Panel personalizado
    static class FondoPanel extends JPanel {

        private Image imagen;

        public FondoPanel(String ruta) {
            // Primero intenta cargar como recurso del JAR / classpath
            try {
                java.net.URL res = getClass().getResource(ruta);
                if (res != null) {
                    imagen = new ImageIcon(res).getImage();
                    return;
                }
            } catch (Exception ignored) {
            }

            // Si no está en resources, intenta cargar por ruta absoluta (útil en NetBeans durante desarrollo)
            try {
                imagen = new ImageIcon(ruta).getImage();
            } catch (Exception e) {
                imagen = null;
                System.out.println("No se pudo cargar imagen de fondo (" + ruta + "): " + e.getMessage());
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

    private void inicializarComponentes(JPanel fondo) {
        Font fTitulo = new Font("Segoe UI", Font.BOLD, 22);
        Font fLabel = new Font("Segoe UI", Font.BOLD, 14);

        JLabel lblTitulo = new JLabel("GESTIÓN DE VENTAS");
        lblTitulo.setFont(fTitulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(20, 10, 400, 40);
        fondo.add(lblTitulo);

        JLabel lblCliente = new JLabel("Cliente");
        lblCliente.setFont(fLabel);
        lblCliente.setForeground(Color.WHITE);
        lblCliente.setBounds(20, 60, 100, 25);
        fondo.add(lblCliente);

        txtCliente = new JTextField();
        txtCliente.setBounds(20, 85, 250, 30);
        fondo.add(txtCliente);

        JLabel lblPrecio = new JLabel("Precio");
        lblPrecio.setFont(fLabel);
        lblPrecio.setForeground(Color.WHITE);
        lblPrecio.setBounds(320, 60, 100, 25);
        fondo.add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(320, 85, 200, 30);
        txtPrecio.setEditable(false);
        fondo.add(txtPrecio);

        JLabel lblDescripcion = new JLabel("Descripción");
        lblDescripcion.setFont(fLabel);
        lblDescripcion.setForeground(Color.WHITE);
        lblDescripcion.setBounds(540, 60, 100, 25);
        fondo.add(lblDescripcion);

        txtDescripcion = new JTextArea();
        txtDescripcion.setEditable(false);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBounds(540, 85, 300, 60);
        fondo.add(scrollDesc);

        JLabel lblPostre = new JLabel("Postre");
        lblPostre.setFont(fLabel);
        lblPostre.setForeground(Color.WHITE);
        lblPostre.setBounds(20, 130, 100, 25);
        fondo.add(lblPostre);

        cmbPostre = new JComboBox<>();
        cmbPostre.setBounds(20, 155, 250, 30);
        fondo.add(cmbPostre);

        JLabel lblCantidad = new JLabel("Cantidad");
        lblCantidad.setFont(fLabel);
        lblCantidad.setForeground(Color.WHITE);
        lblCantidad.setBounds(320, 130, 100, 25);
        fondo.add(lblCantidad);

        txtCantidad = new JTextField();
        txtCantidad.setBounds(320, 155, 200, 30);
        fondo.add(txtCantidad);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(Color.YELLOW);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAgregar.setBounds(750, 160, 100, 35);
        fondo.add(btnAgregar);

        JButton btnPagar = new JButton("PAGAR");
        btnPagar.setBackground(Color.GREEN);
        btnPagar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnPagar.setBounds(750, 210, 120, 50);
        fondo.add(btnPagar);

        modelo = new DefaultTableModel(new Object[]{"ID", "Postre", "Descripción", "Precio", "Cantidad"}, 0);
        JTable tabla = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBounds(20, 270, 880, 250);
        fondo.add(scrollTabla);

        JLabel lblTotalTxt = new JLabel("Total:");
        lblTotalTxt.setFont(fLabel);
        lblTotalTxt.setForeground(Color.WHITE);
        lblTotalTxt.setBounds(20, 540, 100, 25);
        fondo.add(lblTotalTxt);

        lblTotal = new JLabel("S/0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTotal.setForeground(Color.WHITE);
        lblTotal.setBounds(70, 530, 200, 40);
        fondo.add(lblTotal);

        lblFecha = new JLabel();
        lblFecha.setFont(fLabel);
        lblFecha.setForeground(Color.WHITE);
        lblFecha.setBounds(700, 520, 100, 25);
        fondo.add(lblFecha);

        lblHora = new JLabel();
        lblHora.setFont(fLabel);
        lblHora.setForeground(Color.WHITE);
        lblHora.setBounds(820, 520, 100, 25);
        fondo.add(lblHora);

        // 🔹 Botones inferiores (ahora añadidos al fondo, no al frame)
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(Color.RED);
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(fLabel);
        btnEliminar.setBounds(480, 565, 110, 35);
        fondo.add(btnEliminar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(Color.ORANGE);
        btnLimpiar.setFont(fLabel);
        btnLimpiar.setBounds(610, 565, 110, 35);
        fondo.add(btnLimpiar);

        // 🔙 Botón Atrás (colocado a la derecha del botón "Limpiar")
        JButton btnAtras = new JButton("Atrás");
        btnAtras.setBackground(new Color(200, 200, 200));
        btnAtras.setFont(fLabel);
        btnAtras.setBounds(740, 565, 110, 35); // A la derecha del botón Limpiar
        fondo.add(btnAtras);

        // Acción del botón Atrás
        btnAtras.addActionListener(e -> {
            new MenuPrincipal(usuarioActual != null ? usuarioActual : new Usuario()).setVisible(true);
            dispose();
        });

        // Acciones
        cargarProductos();
        verificarAlertas();

        cmbPostre.addActionListener(e -> {
            String seleccionado = (String) cmbPostre.getSelectedItem();
            if (seleccionado != null && productosMap.containsKey(seleccionado)) {
                Producto p = productosMap.get(seleccionado);
                txtPrecio.setText(String.valueOf(p.precio));
                txtDescripcion.setText(p.descripcion);
            }
        });

        btnAgregar.addActionListener(e -> agregarProducto());
        btnEliminar.addActionListener(e -> eliminarFila(tabla));
        btnLimpiar.addActionListener(e -> modelo.setRowCount(0));
        btnPagar.addActionListener(e -> pagar());

        actualizarFechaHora();
    }

    private void cargarProductos() {
        try (Connection con = ConexionBD.getConexion(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT id, name, price, description FROM products")) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description")
                );
                productosMap.put(p.nombre, p);
                cmbPostre.addItem(p.nombre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }
    }
    
    private int obtenerStockActual(int idProducto) {
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement("SELECT stock FROM products WHERE id = ?")) {

            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("stock");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Si no encuentra nada o hay error
    }

    
    private void agregarProducto() {
        String postre = (String) cmbPostre.getSelectedItem();
        Producto p = productosMap.get(postre);
        String cantidadTxt = txtCantidad.getText().trim();

        if (cantidadTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa una cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTxt);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida. Ingresa solo números.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 🔥 CONSULTAR STOCK REAL DESDE LA BD
        int stockActual = obtenerStockActual(p.id);

        if (stockActual <= 0) {
            JOptionPane.showMessageDialog(this,
                    "❌ El producto \"" + p.nombre + "\" NO tiene stock disponible.",
                    "Stock insuficiente",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cantidad > stockActual) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ Stock insuficiente.\nStock disponible: " + stockActual
                    + "\nCantidad solicitada: " + cantidad,
                    "Error de Stock",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Si pasa todas las validaciones → agregar a la tabla
        modelo.addRow(new Object[]{p.id, p.nombre, p.descripcion, p.precio, cantidad});
        calcularTotal();

        txtCantidad.setText("");
    }

    private void eliminarFila(JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            modelo.removeRow(fila);
            calcularTotal();
        }
    }

    private void calcularTotal() {
        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            double precio = Double.parseDouble(modelo.getValueAt(i, 3).toString());
            int cantidad = Integer.parseInt(modelo.getValueAt(i, 4).toString());
            total += precio * cantidad;
        }
        lblTotal.setText(String.format("S/%.2f", total));
    }

    private void pagar() {
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay productos en la venta.");
            return;
        }

        ProductoDAO productoDAO = new ProductoDAO();

        // 🔥 DESCONTAR STOCK DE CADA PRODUCTO
        for (int i = 0; i < modelo.getRowCount(); i++) {
            int idProducto = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            int cantidadVendida = Integer.parseInt(modelo.getValueAt(i, 4).toString());

            boolean ok = productoDAO.actualizarStock(idProducto, -cantidadVendida);

            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "⚠️ " + idProducto
                        + ". Puede que no haya stock suficiente.",
                        "Error de Stock",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // 🔹 Generar boleta PDF
        List<Object[]> productosVenta = new ArrayList<>();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object[] fila = new Object[5];
            fila[1] = modelo.getValueAt(i, 1); // nombre
            fila[3] = modelo.getValueAt(i, 3); // precio
            fila[4] = modelo.getValueAt(i, 4); // cantidad
            productosVenta.add(fila);
        }

        String cliente = txtCliente.getText().trim();
        if (cliente.isEmpty()) {
            cliente = "Cliente sin nombre";
        }

        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            double precio = Double.parseDouble(modelo.getValueAt(i, 3).toString());
            int cant = Integer.parseInt(modelo.getValueAt(i, 4).toString());
            total += precio * cant;
        }

        // Llamar a la clase generadora de PDF
        utilidades.BoletaPDF.crearBoleta(
                cliente,
                usuarioActual != null ? usuarioActual.getNombreCompleto() : "Invitado",
                productosVenta,
                total
        );

        JOptionPane.showMessageDialog(this, "Venta registrada correctamente.\nTotal: " + lblTotal.getText());

        // 🔄 LIMPIAR TABLA Y TOTAL
        modelo.setRowCount(0);
        lblTotal.setText("S/0.00");
    }

    private void actualizarFechaHora() {
        Timer timer = new Timer(1000, e -> {
            java.util.Date ahora = new java.util.Date();
            lblFecha.setText(new SimpleDateFormat("dd/MM/yy").format(ahora));
            lblHora.setText(new SimpleDateFormat("HH:mm:ss").format(ahora));
        });
        timer.start();
    }

    // Clase interna simple para productos
    private static class Producto {

        int id;
        String nombre;
        double precio;
        String descripcion;

        Producto(int id, String nombre, double precio, String descripcion) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.descripcion = descripcion;
        }
    }

    private void verificarAlertas() {
        ControladorInventario controlador = new ControladorInventario();
        List<String> alertas = controlador.verificarAlertas();
        if (!alertas.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("⚠️ Se detectaron productos con problemas:\n\n");
            for (String a : alertas) {
                mensaje.append(a).append("\n");
            }
            JOptionPane.showMessageDialog(this, mensaje.toString(), "Alertas de Inventario", JOptionPane.WARNING_MESSAGE);
        }
    }
}
