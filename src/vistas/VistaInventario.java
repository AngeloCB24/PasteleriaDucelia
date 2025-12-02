package vistas;

import controlador.ControladorInventario;
import modelo.Producto;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;  // ✔️ ESTE ES EL CORRECTO

// Apache POI
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

// Importar componentes gráficos
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import static javax.print.attribute.standard.MediaSize.Engineering.C;
import javax.swing.table.DefaultTableModel;
import utilidades.ReporteInventarioPDF;

public class VistaInventario extends JFrame {

    private modelo.Usuario usuarioActual;

    public VistaInventario(modelo.Usuario usuario) {
        this.usuarioActual = usuario;
        init();
    }

    private JTextField txtBuscar;
    private JTable tablaInventario;
    private JButton btnBuscar, btnAgregar, btnEditar, btnEliminar, btnExportar, btnReporte;
    private JLabel lblFecha, lblHora;
    private ControladorInventario controlador = new ControladorInventario();
    private Timer timer;

    public void init() {
        setTitle("GESTIÓN DE INVENTARIO - Usuario: "
                + (usuarioActual != null ? usuarioActual.getNombreCompleto() : "Invitado"));
        setSize(1025, 625);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel de fondo escalable
        FondoPanel fondo = new FondoPanel("/images/fondoLila.png");

        fondo.setLayout(null);
        setContentPane(fondo);

        // --- Título ---
        JLabel lblTitulo = new JLabel("GESTIÓN DE INVENTARIO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(0, 10, 1000, 40);
        fondo.add(lblTitulo);

        // --- Buscar ---
        JLabel lblBuscar = new JLabel("Buscar");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBuscar.setForeground(Color.WHITE);
        lblBuscar.setBounds(30, 70, 80, 25);
        fondo.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBackground(new Color(255, 255, 255, 200));
        txtBuscar.setBounds(30, 95, 200, 30);
        fondo.add(txtBuscar);

        // 🔥 CREAR BOTONES **ANTES** de usar listeners
        btnBuscar = new JButton("Buscar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnExportar = new JButton("Exportar a Excel");
        btnAgregar = new JButton("Agregar");

        // Posiciones
        btnBuscar.setBounds(235, 95, 80, 30);
        fondo.add(btnBuscar);

        btnAgregar.setBounds(320, 95, 100, 30); // ← CENTRADO ENTRE BUSCAR Y EDITAR
        fondo.add(btnAgregar);

        btnEditar.setBounds(430, 95, 100, 30);
        fondo.add(btnEditar);

        btnEliminar.setBounds(540, 95, 100, 30);
        fondo.add(btnEliminar);

        btnExportar.setBounds(660, 95, 160, 30);
        fondo.add(btnExportar);

        JButton btnReporte = new JButton("Generar Reporte PDF");
        btnReporte.setBounds(780, 540, 180, 40);
        add(btnReporte);

        btnReporte.addActionListener(e -> {
            ReporteInventarioPDF.generar(usuarioActual);
            JOptionPane.showMessageDialog(this, "Reporte generado con exito");
        });

        // Botón Atrás
        JButton btnAtras = new JButton("Atrás");
        btnAtras.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAtras.setBounds(30, 540, 100, 35);
        add(btnAtras);

        btnAtras.addActionListener(e -> {
            new MenuPrincipal(usuarioActual != null ? usuarioActual : new modelo.Usuario()).setVisible(true);
            dispose();
        });

        // --- BOTÓN AGREGAR ---
        lblFecha = new JLabel("   --/--/--");
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFecha.setForeground(Color.WHITE);
        lblFecha.setBounds(820, 95, 120, 30);
        fondo.add(lblFecha);

        lblHora = new JLabel("   00:00");
        lblHora.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHora.setForeground(Color.WHITE);
        lblHora.setBounds(920, 95, 100, 30);
        fondo.add(lblHora);

        // Tabla
        String[] columnas = {
            "ID", "Código", "Nombre", "Stock",
            "Stock Mínimo", "Precio", "F. Venc",
            "Unidad", "Categoría", "Proveedor", "Teléfono"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tablaInventario = new JTable(modelo);
        tablaInventario.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tablaInventario);
        scroll.setBounds(30, 150, 930, 380);
        fondo.add(scroll);

        // 🔥 LISTENERS (ahora que los botones existen)
        btnBuscar.addActionListener(e -> buscarProducto());
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnExportar.addActionListener(e -> exportarExcel());

        cargarProductos();
        verificarAlertas();
        iniciarReloj();
    }

    // Panel personalizado
    static class FondoPanel extends JPanel {

        private Image imagen;

        public FondoPanel(String ruta) {
            this.imagen = new ImageIcon(getClass().getResource("/images/fondoLila.png")).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private void iniciarReloj() {
        timer = new Timer(1000, e -> {
            Date now = new Date();
            lblFecha.setText(new SimpleDateFormat("  dd/MM/yy").format(now));
            lblHora.setText(new SimpleDateFormat("   HH:mm:ss").format(now));
        });
        timer.start();
    }

    private void cargarProductos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaInventario.getModel();
        modelo.setRowCount(0);

        List<Producto> lista = controlador.listarProductos();
        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getId(), p.getCodigo(), p.getNombre(), p.getStock(),
                p.getStockMin(), p.getPrecio(), p.getFechaVencimiento(),
                p.getUnidad(), p.getCategoriaId(),
                p.getProveedorNombre(), p.getProveedorTelefono()
            });
        }
    }

    private void buscarProducto() {
        String filtro = txtBuscar.getText().trim().toLowerCase();
        DefaultTableModel modelo = (DefaultTableModel) tablaInventario.getModel();
        modelo.setRowCount(0);

        for (Producto p : controlador.listarProductos()) {
            if (p.getNombre().toLowerCase().contains(filtro)
                    || p.getCodigo().toLowerCase().contains(filtro)) {

                modelo.addRow(new Object[]{
                    p.getId(), p.getCodigo(), p.getNombre(), p.getStock(),
                    p.getStockMin(), p.getPrecio(), p.getFechaVencimiento(),
                    p.getUnidad(), p.getCategoriaId(),
                    p.getProveedorNombre(), // ← ESTA ERA LA QUE FALTABA
                    p.getProveedorTelefono()
                });

            }
        }
    }

    private void agregarProducto() {
        FormProducto form = new FormProducto(() -> cargarProductos());
        form.setVisible(true);
    }

    private void editarProducto() {
        int fila = tablaInventario.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        int id = (int) tablaInventario.getValueAt(fila, 0);

        Producto producto = controlador.obtenerProducto(id);
        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Error: No se pudo cargar el producto.");
            return;
        }

        FormProducto form = new FormProducto(producto, () -> cargarProductos());
        form.setVisible(true);
    }

    private void eliminarProducto() {
        int fila = tablaInventario.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        int id = (int) tablaInventario.getValueAt(fila, 0);
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar producto con ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (r == JOptionPane.YES_OPTION) {
            if (controlador.eliminarProducto(id)) {
                cargarProductos();
            }
        }
    }

    private void verificarAlertas() {
        List<String> alertas = controlador.verificarAlertas();

        if (!alertas.isEmpty()) {
            StringBuilder sb = new StringBuilder("Alertas detectadas:\n\n");
            for (String a : alertas) {
                sb.append(a).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Alertas", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void exportarExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventario");
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < tablaInventario.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(tablaInventario.getColumnName(i));
            }

            for (int i = 0; i < tablaInventario.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < tablaInventario.getColumnCount(); j++) {
                    Object v = tablaInventario.getValueAt(i, j);
                    row.createCell(j).setCellValue(v == null ? "" : v.toString());
                }
            }

            FileOutputStream fileOut = new FileOutputStream("Inventario.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            JOptionPane.showMessageDialog(this, "Inventario exportado correctamente.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
        }
    }
}
