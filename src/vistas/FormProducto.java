package vistas;

import controlador.ControladorInventario;
import dao.MovimientoInventarioDAO;
import modelo.Producto;
import modelo.Categoria;
import modelo.Proveedor;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import modelo.MovimientoInventario;
import modelo.Usuario;

public class FormProducto extends JFrame {

    private Usuario usuarioActual;
    private JTextField txtCodigo, txtNombre, txtStock, txtStockMin, txtPrecio, txtDescripcion;
    private JComboBox<String> cmbUnidad;
    private JComboBox<Categoria> cbCategoria;
    private JComboBox<Proveedor> cbProveedor;
    private JTextField txtTelefono;
    private JSpinner spFecha;

    private ControladorInventario controlador = new ControladorInventario();
    private Runnable callbackActualizarTabla;
    private Producto productoEdicion = null; // si no es null => modo EDIT

    // ---------- CONSTRUCTOR para CREAR ----------
    public FormProducto(Runnable actualizarTabla, Usuario usuarioActual) {
        this.callbackActualizarTabla = actualizarTabla;
        this.usuarioActual = usuarioActual;
        initUI();
        setTitle("Agregar Producto");
    }

    // ---------- CONSTRUCTOR para EDITAR ----------
    public FormProducto(Producto producto, Runnable actualizarTabla, Usuario usuarioActual) {
        this.callbackActualizarTabla = actualizarTabla;
        this.usuarioActual = usuarioActual;
        this.productoEdicion = producto;
        initUI();
        cargarDatosParaEdicion(producto);
        setTitle("Editar Producto");
    }

    // ---------- INICIALIZA UI COMÚN ----------
    private void initUI() {
        setSize(450, 620);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblTitulo = new JLabel(productoEdicion == null ? "Nuevo Producto" : "Editar Producto", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setBounds(0, 10, 450, 30);
        add(lblTitulo);

        int y = 60;

        addLabel("Código:", 30, y);
        txtCodigo = addField(150, y);

        y += 40;
        addLabel("Nombre:", 30, y);
        txtNombre = addField(150, y);

        y += 40;
        addLabel("Stock:", 30, y);
        txtStock = addField(150, y);

        y += 40;
        addLabel("Stock Mínimo:", 30, y);
        txtStockMin = addField(150, y);

        y += 40;
        addLabel("Precio:", 30, y);
        txtPrecio = addField(150, y);

        y += 40;
        addLabel("Unidad:", 30, y);
        String[] unidades = {"Unidad", "Pack", "Docena", "Media Docena", "Porción", "Caja", "Bolsa"};
        cmbUnidad = new JComboBox<>(unidades);
        cmbUnidad.setBounds(150, y, 200, 25);
        add(cmbUnidad);

        y += 40;
        addLabel("Fecha Vencimiento:", 30, y);
        spFecha = new JSpinner(new javax.swing.SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spFecha, "yyyy-MM-dd");
        spFecha.setEditor(editor);
        spFecha.setBounds(150, y, 200, 25);
        add(spFecha);

        y += 40;
        addLabel("Categoría:", 30, y);
        cbCategoria = new JComboBox<>();
        cbCategoria.setBounds(150, y, 200, 25);
        add(cbCategoria);

        y += 40;
        addLabel("Proveedor:", 30, y);
        cbProveedor = new JComboBox<>();
        cbProveedor.setBounds(150, y, 200, 25);
        add(cbProveedor);

        y += 40;
        addLabel("Teléfono:", 30, y);
        txtTelefono = addField(150, y);
        txtTelefono.setEditable(false);

        y += 40;
        addLabel("Descripción:", 30, y);
        txtDescripcion = addField(150, y);

        // cargar combos
        cargarCategorias();
        cargarProveedores();

        // al cambiar proveedor actualiza teléfono
        cbProveedor.addActionListener(e -> actualizarTelefono());

        // BOTONES
        JButton btnGuardar = new JButton(productoEdicion == null ? "Guardar" : "Guardar Cambios");
        btnGuardar.setBounds(80, 520, 140, 35);
        add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(240, 520, 120, 35);
        add(btnCancelar);

        btnCancelar.addActionListener(e -> dispose());

        btnGuardar.addActionListener(e -> {
            if (productoEdicion == null) {
                guardarProducto();
            } else {
                guardarCambios();
            }
        });
    }

    // Carga categorías en el combo
    private void cargarCategorias() {
        cbCategoria.removeAllItems();
        List<Categoria> categorias = controlador.listarCategorias();
        for (Categoria c : categorias) {
            cbCategoria.addItem(c);
        }
    }

    // Carga proveedores en el combo
    private void cargarProveedores() {
        cbProveedor.removeAllItems();
        List<Proveedor> proveedores = controlador.listarProveedores();
        for (Proveedor p : proveedores) {
            cbProveedor.addItem(p);
        }
    }

    // cuando editamos, precargamos datos
    private void cargarDatosParaEdicion(Producto producto) {
        if (producto == null) {
            return;
        }

        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        txtStock.setText(String.valueOf(producto.getStock()));
        txtStockMin.setText(String.valueOf(producto.getStockMin()));
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
        if (producto.getUnidad() != null) {
            cmbUnidad.setSelectedItem(producto.getUnidad());
        }

        // fecha (puede ser null)
        if (producto.getFechaVencimiento() != null) {
            spFecha.setValue(new java.util.Date(producto.getFechaVencimiento().getTime()));
        }

        // Seleccionar categoría por id (si existe)
        if (producto.getCategoriaId() != null) {
            for (int i = 0; i < cbCategoria.getItemCount(); i++) {
                Categoria c = cbCategoria.getItemAt(i);
                if (c != null && c.getId() == producto.getCategoriaId()) {
                    cbCategoria.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Seleccionar proveedor por id (si existe)
        if (producto.getProveedorId() != null) {
            for (int i = 0; i < cbProveedor.getItemCount(); i++) {
                Proveedor pr = cbProveedor.getItemAt(i);
                if (pr != null && pr.getId() == producto.getProveedorId()) {
                    cbProveedor.setSelectedIndex(i);
                    txtTelefono.setText(String.valueOf(pr.getTelefono()));
                    break;
                }
            }
        }

        if (producto.getDescripcion() != null) {
            txtDescripcion.setText(producto.getDescripcion());
        }
    }

    private void actualizarTelefono() {
        Proveedor p = (Proveedor) cbProveedor.getSelectedItem();
        if (p != null) {
            txtTelefono.setText(String.valueOf(p.getTelefono()));
        } else {
            txtTelefono.setText("");
        }
    }

    // Crear nuevo producto
    private void guardarProducto() {
        try {
            Producto p = leerProductoDesdeFormulario();
            if (!controlador.crearProducto(p)) {
                JOptionPane.showMessageDialog(this, "Error al guardar.");
                return;
            }

            MovimientoInventarioDAO movDAO = new MovimientoInventarioDAO();
            MovimientoInventario mov = new MovimientoInventario();

            mov.setProductId(p.getId());
            mov.setUserId(usuarioActual.getId()); // <-- CAMBIA ESTO por tu usuario actual
            mov.setMovementType("IN");
            mov.setQuantity(p.getStock());
            mov.setNote("Registro inicial de producto");

            movDAO.registrarMovimiento(mov);

            JOptionPane.showMessageDialog(this, "Producto guardado correctamente.");
            if (callbackActualizarTabla != null) {
                callbackActualizarTabla.run();
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage());
        }
    }

    // Actualizar producto existente
    private void guardarCambios() {
        if (productoEdicion == null) {
            return;
        }

        try {
            Producto p = leerProductoDesdeFormulario();
            p.setId(productoEdicion.getId());

            int stockAnterior = productoEdicion.getStock();
            int stockNuevo = p.getStock();
            int diferencia = stockNuevo - stockAnterior;

            if (diferencia != 0) {
                MovimientoInventarioDAO movDAO = new MovimientoInventarioDAO();
                MovimientoInventario mov = new MovimientoInventario();

                mov.setProductId(productoEdicion.getId());
                mov.setUserId(usuarioActual.getId()); // <-- CAMBIA ESTO por el usuario actual
                mov.setQuantity(Math.abs(diferencia));
                mov.setMovementType(diferencia > 0 ? "IN" : "OUT");
                mov.setNote("Actualización de stock desde FormProducto");

                movDAO.registrarMovimiento(mov);
            }

            if (!controlador.actualizarProducto(p)) {
                JOptionPane.showMessageDialog(this, "Error al actualizar.");
                return;
            }
            JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
            if (callbackActualizarTabla != null) {
                callbackActualizarTabla.run();
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage());
        }
    }

    // Lee y valida los campos del formulario y retorna Producto
    private Producto leerProductoDesdeFormulario() {
        Producto p = new Producto();

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String stockTxt = txtStock.getText().trim();
        String stockMinTxt = txtStockMin.getText().trim();
        String precioTxt = txtPrecio.getText().trim();

        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("Nombre requerido");
        }
        if (codigo.isEmpty()) {
            throw new IllegalArgumentException("Código requerido");
        }

        int stock = stockTxt.isEmpty() ? 0 : Integer.parseInt(stockTxt);
        int stockMin = stockMinTxt.isEmpty() ? 0 : Integer.parseInt(stockMinTxt);
        double precio = precioTxt.isEmpty() ? 0.0 : Double.parseDouble(precioTxt);

        p.setCodigo(codigo);
        p.setNombre(nombre);
        p.setStock(stock);
        p.setStockMin(stockMin);
        p.setPrecio(precio);
        p.setUnidad((String) cmbUnidad.getSelectedItem());

        java.util.Date fecha = (java.util.Date) spFecha.getValue();
        if (fecha != null) {
            p.setFechaVencimiento(new Date(fecha.getTime()));
        } else {
            p.setFechaVencimiento(null);
        }

        Categoria cat = (Categoria) cbCategoria.getSelectedItem();
        Proveedor prov = (Proveedor) cbProveedor.getSelectedItem();

        if (cat != null) {
            p.setCategoriaId(cat.getId());
        }
        if (prov != null) {
            p.setProveedorId(prov.getId());
        }

        // opcional: descripción no está en el formulario; si tu modelo la tiene y quieres añadirlo, hazlo aquí.
        p.setDescripcion(txtDescripcion.getText().trim());

        return p;
    }

    // Helpers UI
    private void addLabel(String t, int x, int y) {
        JLabel lbl = new JLabel(t);
        lbl.setBounds(x, y, 140, 25);
        add(lbl);
    }

    private JTextField addField(int x, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, 200, 25);
        add(txt);
        return txt;
    }
}
