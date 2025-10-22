package vistas;

import controlador.ControladorInventario;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// Apache POI
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

// Importar componentes gráficos específicos
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Importar componentes gráficos
import java.awt.*;

public class VistaInventario extends JFrame {

    private JTextField txtBuscar;
    private JTable tablaInventario;
    private JButton btnBuscar, btnAgregar, btnEditar, btnEliminar, btnExportar;
    private JLabel lblFecha, lblHora;
    private ControladorInventario controlador = new ControladorInventario();
    private Timer timer;

    public VistaInventario() {
        setTitle("Gestión de Inventario");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel de fondo escalable
        FondoPanel fondo = new FondoPanel("B:/Angelo/Documents/NetBeansProjects/PasteleriaDucelia/src/images/fondoLila.png");
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

        btnBuscar = new JButton("🔍");
        btnBuscar.setBounds(235, 95, 45, 30);
        fondo.add(btnBuscar);

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(300, 95, 100, 30);
        fondo.add(btnAgregar);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(410, 95, 100, 30);
        fondo.add(btnEditar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(520, 95, 100, 30);
        fondo.add(btnEliminar);

        btnExportar = new JButton("Exportar a Excel");
        btnExportar.setBounds(640, 95, 160, 30);
        fondo.add(btnExportar);

        lblFecha = new JLabel("📅 --/--/--");
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFecha.setForeground(Color.WHITE);
        lblFecha.setBounds(820, 95, 120, 30);
        fondo.add(lblFecha);

        lblHora = new JLabel("🕒 00:00");
        lblHora.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHora.setForeground(Color.WHITE);
        lblHora.setBounds(920, 95, 100, 30);
        fondo.add(lblHora);

        String[] columnas = {"ID", "Código", "Nombre", "Stock", "Stock Mínimo", "Precio", "F. Venc", "Unidad", "Categoría", "Proveedor"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tablaInventario = new JTable(modelo);
        tablaInventario.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tablaInventario);
        scroll.setBounds(30, 150, 930, 380);
        fondo.add(scroll);

        cargarProductos();
        verificarAlertas();
        iniciarReloj();

        btnBuscar.addActionListener(e -> buscarProducto());
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnExportar.addActionListener(e -> exportarExcel());
    }

    // Panel personalizado para fondo escalable
    static class FondoPanel extends JPanel {

        private Image imagen;

        public FondoPanel(String ruta) {
            this.imagen = new ImageIcon(ruta).getImage();
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
            lblFecha.setText("📅 " + new SimpleDateFormat("dd/MM/yy").format(now));
            lblHora.setText("🕒 " + new SimpleDateFormat("HH:mm:ss").format(now));
        });
        timer.start();
    }

    // 🔄 Cargar productos
    private void cargarProductos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaInventario.getModel();
        modelo.setRowCount(0);
        List<Producto> lista = controlador.listarProductos();
        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getId(), p.getCodigo(), p.getNombre(), p.getStock(),
                p.getStockMin(), p.getPrecio(), p.getFechaVencimiento(),
                p.getUnidad(), p.getCategoriaId(), p.getProveedorId()
            });
        }
    }

    // 🔍 Buscar
    private void buscarProducto() {
        String filtro = txtBuscar.getText().trim().toLowerCase();
        DefaultTableModel modelo = (DefaultTableModel) tablaInventario.getModel();
        modelo.setRowCount(0);
        List<Producto> lista = controlador.listarProductos();
        for (Producto p : lista) {
            if (p.getNombre().toLowerCase().contains(filtro)
                    || p.getCodigo().toLowerCase().contains(filtro)) {
                modelo.addRow(new Object[]{
                    p.getId(), p.getCodigo(), p.getNombre(), p.getStock(),
                    p.getStockMin(), p.getPrecio(), p.getFechaVencimiento(),
                    p.getUnidad(), p.getCategoriaId(), p.getProveedorId()
                });
            }
        }
    }

    // 🟢 Agregar
    private void agregarProducto() {
        JTextField txtCodigo = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtStock = new JTextField();
        JTextField txtPrecio = new JTextField();

        Object[] inputs = {
            "Código:", txtCodigo,
            "Nombre:", txtNombre,
            "Stock:", txtStock,
            "Precio:", txtPrecio
        };

        int result = JOptionPane.showConfirmDialog(this, inputs, "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Producto nuevo = new Producto();
                nuevo.setCodigo(txtCodigo.getText());
                nuevo.setNombre(txtNombre.getText());
                nuevo.setStock(Integer.parseInt(txtStock.getText()));
                nuevo.setPrecio(Double.parseDouble(txtPrecio.getText()));

                if (controlador.crearProducto(nuevo)) {
                    JOptionPane.showMessageDialog(this, "✅ Producto agregado correctamente.");
                    cargarProductos();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al guardar el producto.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "⚠️ Verifica los datos ingresados.");
            }
        }
    }

    // ✏️ Editar
    private void editarProducto() {
        int fila = tablaInventario.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para editar.");
            return;
        }

        int id = (int) tablaInventario.getValueAt(fila, 0);
        String nombre = (String) tablaInventario.getValueAt(fila, 2);
        int stock = (int) tablaInventario.getValueAt(fila, 3);
        double precio = (double) tablaInventario.getValueAt(fila, 5);

        JTextField txtNombre = new JTextField(nombre);
        JTextField txtStock = new JTextField(String.valueOf(stock));
        JTextField txtPrecio = new JTextField(String.valueOf(precio));

        Object[] inputs = {"Nombre:", txtNombre, "Stock:", txtStock, "Precio:", txtPrecio};

        int result = JOptionPane.showConfirmDialog(this, inputs, "Editar Producto", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Producto p = new Producto();
            p.setId(id);
            p.setNombre(txtNombre.getText());
            p.setStock(Integer.parseInt(txtStock.getText()));
            p.setPrecio(Double.parseDouble(txtPrecio.getText()));

            if (controlador.actualizarProducto(p)) {
                JOptionPane.showMessageDialog(this, "✅ Producto actualizado correctamente.");
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al actualizar el producto.");
            }
        }
    }

    // 🗑️ Eliminar
    private void eliminarProducto() {
        int fila = tablaInventario.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
            return;
        }

        int id = (int) tablaInventario.getValueAt(fila, 0);
        int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar producto con ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            if (controlador.eliminarProducto(id)) {
                JOptionPane.showMessageDialog(this, "✅ Producto eliminado correctamente.");
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al eliminar el producto.");
            }
        }
    }

    // ⚠️ Verifica alertas de inventario
    private void verificarAlertas() {
        List<String> alertas = controlador.verificarAlertas();
        if (!alertas.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Se detectaron las siguientes alertas:\n\n");
            for (String a : alertas) {
                mensaje.append(a).append("\n");
            }
            JOptionPane.showMessageDialog(this, mensaje.toString(), "⚠️ Alertas de Inventario", JOptionPane.WARNING_MESSAGE);
        }
    }

    // 📊 Exportar a Excel
    private void exportarExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventario");
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < tablaInventario.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(tablaInventario.getColumnName(i));

                CellStyle style = workbook.createCellStyle();
                style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                org.apache.poi.ss.usermodel.Font poiFont = workbook.createFont();
                poiFont.setBold(true);
                style.setFont(poiFont);

                cell.setCellStyle(style);
            }

            for (int i = 0; i < tablaInventario.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < tablaInventario.getColumnCount(); j++) {
                    Object valor = tablaInventario.getValueAt(i, j);
                    row.createCell(j).setCellValue(valor == null ? "" : valor.toString());
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream("Inventario.xlsx")) {
                workbook.write(fileOut);
            }

            JOptionPane.showMessageDialog(this, "✅ Inventario exportado a Excel correctamente.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error al exportar: " + ex.getMessage());
        }
    }
}
