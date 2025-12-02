package vistas;

import controlador.ControladorProveedor;
import modelo.Proveedor;

import javax.swing.*;
import java.awt.*;

public class FormProveedor extends JFrame {

    private JTextField txtNombre, txtContacto, txtEmail, txtTelefono;
    private ControladorProveedor controlador = new ControladorProveedor();
    private Runnable actualizarTabla;

    // ---------- CONSTRUCTOR PARA CREAR ----------
    public FormProveedor(Runnable actualizarTabla) {
        this.actualizarTabla = actualizarTabla;
        init(null);
    }

    // ---------- CONSTRUCTOR PARA EDITAR ----------
    public FormProveedor(Runnable actualizarTabla, Proveedor proveedorEditar) {
        this.actualizarTabla = actualizarTabla;
        init(proveedorEditar);
    }

    // ---------- MÉTODO PRINCIPAL ----------
    private void init(Proveedor proveedorEditar) {
        setTitle(proveedorEditar == null ? "Nuevo Proveedor" : "Editar Proveedor");
        setSize(400, 330);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(30, 30, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(140, 30, 200, 25);
        add(txtNombre);

        JLabel lblContacto = new JLabel("Contacto:");
        lblContacto.setBounds(30, 70, 100, 25);
        add(lblContacto);

        txtContacto = new JTextField();
        txtContacto.setBounds(140, 70, 200, 25);
        add(txtContacto);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(30, 110, 100, 25);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(140, 110, 200, 25);
        add(txtEmail);

        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setBounds(30, 150, 100, 25);
        add(lblTelefono);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(140, 150, 200, 25);
        add(txtTelefono);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(70, 220, 120, 35);
        add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(210, 220, 120, 35);
        add(btnCancelar);

        btnCancelar.addActionListener(e -> dispose());

        // Si es edición, cargamos datos
        if (proveedorEditar != null) {
            txtNombre.setText(proveedorEditar.getNombre());
            txtContacto.setText(proveedorEditar.getContacto());
            txtEmail.setText(proveedorEditar.getEmail());
            txtTelefono.setText(String.valueOf(proveedorEditar.getTelefono()));

            btnGuardar.addActionListener(e -> {

                proveedorEditar.setNombre(txtNombre.getText());
                proveedorEditar.setContacto(txtContacto.getText());
                proveedorEditar.setEmail(txtEmail.getText());
                proveedorEditar.setTelefono(Integer.parseInt(txtTelefono.getText()));

                if (controlador.actualizar(proveedorEditar)) {
                    JOptionPane.showMessageDialog(this, "Proveedor actualizado.");
                    actualizarTabla.run();
                    dispose();
                }
            });

        } else {
            // CREAR NUEVO PROVEEDOR
            btnGuardar.addActionListener(e -> {

                Proveedor nuevo = new Proveedor();
                nuevo.setNombre(txtNombre.getText());
                nuevo.setContacto(txtContacto.getText());
                nuevo.setEmail(txtEmail.getText());
                nuevo.setTelefono(Integer.parseInt(txtTelefono.getText()));

                if (controlador.crear(nuevo)) {
                    JOptionPane.showMessageDialog(this, "Proveedor creado.");
                    actualizarTabla.run();
                    dispose();
                }
            });
        }
    }
}
