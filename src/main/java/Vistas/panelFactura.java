/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Controladores.cFactura;
import Modelos.Factura;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author berth
 */
public class panelFactura extends javax.swing.JPanel {

    //datos globales
    public cFactura list = new cFactura();
    int clave = -1; //guarda el campo clave para editar

    //*******************METODOS PERSONALIZADOS *******************
    /**
     * Leer datos del formulario y guardar en un objeto
     * @return Objeto Estudiante
     */

    public Factura leer() {
        Factura ob = null;
        if (form_validado()) {
            ob = new Factura();
            ob.Num_fact = Integer.parseInt(txtNumfac.getText());
            ob.Codigo_pedido = txtCodigoPedido.getText();
            ob.Cedula_cliente = txtCedCliente.getText();
            ob.Nombre = txtNombre.getText();
            ob.Apellido = txtApellido.getText();
            ob.Telefono = txtTelefono.getText();
            ob.Direccion_domicilio = txtDireccion.getText();
            ob.Ciudad = jComboBox1.getSelectedItem().toString();
            ob.Fecha_venta = txtFechaventa.getText();
            System.out.print(ob.toString());
        }
        return ob;
    }

    /*
     * Validar formulario
     */
    public boolean form_validado() {
        boolean ok = true;
        String men = "Campos con errores";
        //validar requerido
        if (!Validaciones.esCedula(txtNumfac)) {
            ok = false;
            men += ", Numero factura";
        }

        if (!Validaciones.esNumero(txtCodigoPedido)) {
            ok = false;
            men += ", Codigo pedido";
        }

        if (!Validaciones.esCedula(txtCedCliente)) {
            ok = false;
            men += ", Cedula cliente";
        }

        if (!Validaciones.esLetras(txtApellido)) {
            ok = false;
            men += ", Apellido";
        }
        
        if(!Validaciones.esLetras(txtNombre)){
            ok = false;
            men += ", Nombre";
        }
        
        if(!Validaciones.esTelefono(txtTelefono)){
            ok = false;
            men += ", Telefono";
        }
        
        if(!Validaciones.esLetras(txtDireccion)){
            ok = false;
            men += ", Direccion";
        }
        
        if(!Validaciones.comboBox(jComboBox1)){
            ok = false;
            men += ", Ciudad";
        }
        
        if(!Validaciones.esFecha(txtFechaventa)){
            ok = false;
            men += ", Fecha venta";
        }
        
        if (!ok) {
            frmPrincipal.lbMensaje.setText(men);
        } else {
            frmPrincipal.lbMensaje.setText("");
        }
        //validar más controles
        return ok;
    }

    /*
     * Metodo para limpiar cajas de texto
     */
    public void limpiar_textos() {
        txtNumfac.setText("");
        txtCedCliente.setText("");
        txtCodigoPedido.setText("");
        txtApellido.setText("");
        txtNombre.setText("");
        txtFechaventa.setText("");
        jComboBox1.setSelectedIndex(0);
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtFechaventa.setText("");
        txtNumfac.requestFocus();  //envia curso o enfoque a la caja de texto cedula
        frmPrincipal.lbMensaje.setText("");
    }

    /*
     * quitar validaciones
     */
    public void quitar_validaciones() {
        Validaciones.pinta_text(txtNumfac);
        Validaciones.pinta_text(txtCedCliente);
        Validaciones.pinta_text(txtCodigoPedido);
        Validaciones.pinta_text(txtApellido);
        Validaciones.pinta_text(txtNombre);
        Validaciones.pinta_text(txtTelefono);
        Validaciones.pinta_text(txtDireccion);
        Validaciones.pinta_text(txtFechaventa);
        Validaciones.pinta_text(jComboBox1);
        frmPrincipal.lbMensaje.setText("");
    }

    /*
     * Ver registro
     */
    public final void ver_registro(int pos) {
        if (pos >= 0 && pos < list.Count()) {
            Factura ob = list.getFactura(pos);
            txtNumfac.setText(String.valueOf(ob.Num_fact));
            txtCodigoPedido.setText(ob.Codigo_pedido);
            txtCedCliente.setText(ob.Cedula_cliente);
            txtNombre.setText(ob.Nombre);
            txtApellido.setText(ob.Apellido);
            txtTelefono.setText(ob.Telefono);
            txtDireccion.setText(ob.Direccion_domicilio);
            txtFechaventa.setText(ob.Fecha_venta);
            jComboBox1.setSelectedItem(ob.Ciudad);
            //if (ob.Genero.equals("Femenino")) {
            //    rbFemenino.setSelected(true);
            //} else {
            //    rbMasculino.setSelected(true);
            //}
        }
    }

    /*
     * Buscar datos segun cedula
     */
    public void buscar() {
        try {
            int pos = list.localizar(Integer.parseInt(txtNumfac.getText()));
            if (pos > -1) {
                ver_registro(pos);
            } else {
                frmPrincipal.lbMensaje.setText("Registro no encontrado");
            }
        } catch (Exception ex) {
            frmPrincipal.lbMensaje.setText(ex.getMessage());
        }
    }

    /*
     * Guardar estudiante, cuando es nuevo o se modifica un existente
     */
    public void guardar() {
        Factura ob = leer();
        try {
            if (ob != null) {
                if (clave ==  -1) {//guardar un nuevo estudiante
                    list.nuevo(ob);
                } else {//guardar datos de estudiante editado
                    list.modificar(ob, clave);
                }
                frmPrincipal.lbMensaje.setText("Registro guardado exitosamente");
                list.guardar(); //guarda en el archivo csv
                tabla.setModel(list.getTabla());
                //deshabilitar textos
                habilitar_textos(false);
                //habilitar botones
                habilitar_botones(true);
            }
        } catch (IOException ex) {
            frmPrincipal.lbMensaje.setText(ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    /*
     * Buscar datos por criterios cedula y apellido
     */
    public void buscar_varios() {
        try {
            cFactura p = list.buscar_Numfactura(Integer.parseInt(txtDato.getText())); //busca por cedula
            if (p.Count() == 0) {
                p = list.buscar_CodigoPedido(txtDato.getText()); //buscar por apellido
            }
            tabla.setModel(p.getTabla());
        } catch (IOException ex) {
            frmPrincipal.lbMensaje.setText(ex.getMessage());
        }
    }

    /*
     * Eliminar datos de un estudiante
     */
    public void eliminar() {
        try {
            list.eliminar(Integer.parseInt(txtNumfac.getText()));
            list.guardar();
            tabla.setModel(list.getTabla());
            frmPrincipal.lbMensaje.setText("Registro eliminado");
        } catch (IOException ex) {
            frmPrincipal.lbMensaje.setText(ex.getMessage());
        }
    }


    /*
     * Habilitar o desabilitar textos
     */
    public final void habilitar_textos(Boolean ok) {
        txtNumfac.setEditable(ok);
        txtCedCliente.setEditable(ok);
        txtCodigoPedido.setEditable(ok);
        txtApellido.setEditable(ok);
        txtNombre.setEditable(ok);
        txtTelefono.setEditable(ok);
        txtDireccion.setEditable(ok);
        txtFechaventa.setEditable(ok);
        jComboBox1.setEnabled(ok);
    }

    /*
     * Habilitar o desabilitar botones
     */
    public final void habilitar_botones(Boolean ok) {
        btEditar.setEnabled(ok);
        //btBuscar.setEnabled(ok);
        btEliminar.setEnabled(ok);

        //hacen lo contrario de los otros botones
        btGuardar.setEnabled(!ok);
        btCancelar.setEnabled(!ok);
    }

    /**
     * Creates new form panelEstudiante
     */
    public panelFactura(String fun) {
        initComponents();
        this.funcion = fun;
        try {
            list.leer();
        } catch (IOException ex) {
            Logger.getLogger(panelFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
		//formato de la tabla
	tabla.getTableHeader().setBackground(new Color(100, 200, 200));
        tabla.getTableHeader().setForeground(Color.BLACK);
        //cargar datos en la tabla
        tabla.setModel(list.getTabla());
        if (list.Count()>0)
            ver_registro(1); //muestra el primer registro u objeto del arreglo
        //desabilitar textos
        habilitar_textos(false);
        //habilitar botones
        habilitar_botones(true);
        jButton1.setEnabled(false);
        jButton1.setVisible(false);
        if(funcion.equals("Pedido")){
            limpiar_textos();
            clave = -1; //nuevo
            //habilitar textos
            habilitar_textos(true);
            //deshabilitar botones
            mostrar_botones(false);
            btCancelar.setEnabled(false);
            tabla.setEnabled(false);
            setDatos();
        }
    }
    
    public final void mostrar_botones(Boolean ok) {
        btEditar.setVisible(ok);
        //btBuscar.setEnabled(ok);
        btEliminar.setVisible(ok);
        //hacen lo contrario de los otros botones
        btGuardar.setEnabled(!ok);
        btCancelar.setVisible(ok);
    }
    
    public void setDatos(){
            Factura ob = frmPrincipal.factura;
            txtCodigoPedido.setText(ob.Codigo_pedido);
            txtCedCliente.setText(ob.Cedula_cliente);
            txtNombre.setText(ob.Nombre);
            txtApellido.setText(ob.Apellido);
            txtTelefono.setText(ob.Telefono);
            txtDireccion.setText(ob.Direccion_domicilio);
            jComboBox1.setSelectedItem(ob.Ciudad);
    }
    
    public String funcion;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        panelTitulo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelBotones = new javax.swing.JPanel();
        barraBotones = new javax.swing.JToolBar();
        btEditar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        btGuardar = new javax.swing.JButton();
        btEliminar = new javax.swing.JButton();
        btListar = new javax.swing.JButton();
        panelDatos = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCodigoPedido = new javax.swing.JTextField();
        txtNumfac = new javax.swing.JTextField();
        txtCedCliente = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtFechaventa = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        panelTabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtDato = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        panelTitulo.setBackground(new java.awt.Color(255, 51, 51));
        panelTitulo.setPreferredSize(new java.awt.Dimension(1070, 50));
        panelTitulo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Factura");
        jLabel1.setPreferredSize(new java.awt.Dimension(500, 50));
        panelTitulo.add(jLabel1);

        add(panelTitulo, java.awt.BorderLayout.PAGE_START);

        barraBotones.setBackground(java.awt.SystemColor.controlHighlight);
        barraBotones.setOrientation(javax.swing.SwingConstants.VERTICAL);
        barraBotones.setRollover(true);
        barraBotones.setPreferredSize(new java.awt.Dimension(50, 80));

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/modificar.png"))); // NOI18N
        btEditar.setToolTipText("Editar");
        btEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btEditar.setMaximumSize(new java.awt.Dimension(80, 80));
        btEditar.setMinimumSize(new java.awt.Dimension(80, 80));
        btEditar.setPreferredSize(new java.awt.Dimension(80, 80));
        btEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });
        barraBotones.add(btEditar);

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/undo.png"))); // NOI18N
        btCancelar.setToolTipText("Cancelar");
        btCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btCancelar.setMaximumSize(new java.awt.Dimension(80, 80));
        btCancelar.setMinimumSize(new java.awt.Dimension(80, 80));
        btCancelar.setPreferredSize(new java.awt.Dimension(80, 80));
        btCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });
        barraBotones.add(btCancelar);

        btGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/save.png"))); // NOI18N
        btGuardar.setToolTipText("Guardar");
        btGuardar.setFocusable(false);
        btGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btGuardar.setMaximumSize(new java.awt.Dimension(80, 80));
        btGuardar.setMinimumSize(new java.awt.Dimension(80, 80));
        btGuardar.setPreferredSize(new java.awt.Dimension(80, 80));
        btGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGuardarActionPerformed(evt);
            }
        });
        barraBotones.add(btGuardar);

        btEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/eliminar.png"))); // NOI18N
        btEliminar.setToolTipText("Eliminar");
        btEliminar.setFocusable(false);
        btEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btEliminar.setMaximumSize(new java.awt.Dimension(80, 80));
        btEliminar.setMinimumSize(new java.awt.Dimension(80, 80));
        btEliminar.setPreferredSize(new java.awt.Dimension(80, 80));
        btEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEliminarActionPerformed(evt);
            }
        });
        barraBotones.add(btEliminar);

        btListar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/tabla.png"))); // NOI18N
        btListar.setToolTipText("Listar");
        btListar.setFocusable(false);
        btListar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btListar.setMaximumSize(new java.awt.Dimension(80, 80));
        btListar.setMinimumSize(new java.awt.Dimension(80, 80));
        btListar.setPreferredSize(new java.awt.Dimension(80, 80));
        btListar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListarActionPerformed(evt);
            }
        });
        barraBotones.add(btListar);

        panelDatos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de Factura", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Numero factura:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Codigo pedido:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Cedula Cliente:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Apellido:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Direccion:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Fecha venta:");

        txtCodigoPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoPedidoActionPerformed(evt);
            }
        });
        txtCodigoPedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoPedidoKeyReleased(evt);
            }
        });

        txtNumfac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNumfacKeyReleased(evt);
            }
        });

        txtCedCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCedClienteKeyReleased(evt);
            }
        });

        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtApellidoKeyReleased(evt);
            }
        });

        txtFechaventa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaventaActionPerformed(evt);
            }
        });
        txtFechaventa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFechaventaKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Nombre:");

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
        });

        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccionKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Telefono:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("Ciudad:");

        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyReleased(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---------------------", "Quito", "Machala", "Guayaquil", "Cuenca" }));
        jComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jComboBox1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelDatosLayout = new javax.swing.GroupLayout(panelDatos);
        panelDatos.setLayout(panelDatosLayout);
        panelDatosLayout.setHorizontalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addGap(430, 430, 430)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelDatosLayout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panelDatosLayout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(58, 58, 58)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtApellido)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDatosLayout.createSequentialGroup()
                                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtCedCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                                    .addComponent(txtCodigoPedido))
                                .addGap(384, 384, 384))
                            .addComponent(txtNombre)
                            .addComponent(txtDireccion)
                            .addComponent(txtFechaventa, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumfac, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        panelDatosLayout.setVerticalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumfac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigoPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFechaventa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        tabla.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tablaKeyReleased(evt);
            }
        });
        panelTabla.setViewportView(tabla);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Dato a buscar:");

        txtDato.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDatoKeyReleased(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 51, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Ver factura");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBotonesLayout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBotonesLayout.createSequentialGroup()
                        .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(panelDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelBotonesLayout.createSequentialGroup()
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtDato, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(panelTabla, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 802, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                        .addComponent(barraBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))))
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBotonesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(barraBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBotonesLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(panelDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(panelTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addComponent(jButton1)
                .addGap(0, 336, Short.MAX_VALUE))
        );

        add(panelBotones, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNumfacKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumfacKeyReleased
        if (Validaciones.esCedula(txtNumfac))
            Validaciones.pinta_text(txtNumfac);
    }//GEN-LAST:event_txtNumfacKeyReleased

    private void txtCedClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedClienteKeyReleased
        if (Validaciones.esCedula(txtCedCliente))
            Validaciones.pinta_text(txtCedCliente);
    }//GEN-LAST:event_txtCedClienteKeyReleased

    private void txtCodigoPedidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoPedidoKeyReleased
        if (Validaciones.esNumero(txtCodigoPedido))
            Validaciones.pinta_text(txtCodigoPedido);
    }//GEN-LAST:event_txtCodigoPedidoKeyReleased

    private void txtApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyReleased
        if (Validaciones.esLetras(txtApellido))
            Validaciones.pinta_text(txtApellido);
    }//GEN-LAST:event_txtApellidoKeyReleased

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        int pos = tabla.getSelectedRow();
        if (pos >= 0 && pos < list.Count())
            ver_registro(pos);
    }//GEN-LAST:event_tablaMouseClicked

    private void tablaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaKeyReleased
        int pos = tabla.getSelectedRow();
        if (pos >= 0 && pos < list.Count())
            ver_registro(pos);
    }//GEN-LAST:event_tablaKeyReleased

    private void txtDatoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatoKeyReleased
        buscar_varios();
    }//GEN-LAST:event_txtDatoKeyReleased

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        if (!txtNumfac.getText().trim().equals("")) {
            clave = Integer.parseInt(txtNumfac.getText().trim()); //captura la cédula antes de modificar
            txtNumfac.requestFocus();  //envia curso o enfoque a la caja de texto cedula
            frmPrincipal.lbMensaje.setText("");
            //habilitar textos
            habilitar_textos(true);
            //deshabilitar botones
            habilitar_botones(false);
            quitar_validaciones();
        } else
            frmPrincipal.lbMensaje.setText("Seleccione o busque un registro a editar");
    }//GEN-LAST:event_btEditarActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        //quitar validaciones
        quitar_validaciones();
        //desabilitar textos
        habilitar_textos(false);
        //habilitar botones
        habilitar_botones(true);
        //cargar registro anterior a la modificación
        int pos = list.localizar(clave);
        if (pos >= 0)
            ver_registro(pos);
        else
            limpiar_textos();
    }//GEN-LAST:event_btCancelarActionPerformed

    private void btGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGuardarActionPerformed
        guardar();
        if(funcion.equals("Pedido")){
            frmPrincipal.factura = leer();
            jButton1.setVisible(true);
            jButton1.setEnabled(true);
        }
    }//GEN-LAST:event_btGuardarActionPerformed

    private void btEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEliminarActionPerformed
        if (!txtNumfac.getText().trim().equals("")) {
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminiar este registro?", "Sistema Académico", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                eliminar();
            }
        } else
            frmPrincipal.lbMensaje.setText("Seleccione el registro a eliminar");
    }//GEN-LAST:event_btEliminarActionPerformed

    private void btListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btListarActionPerformed
        tabla.setModel(list.getTabla());
        tabla.getColumnModel().getColumn(0).setPreferredWidth(10);
        frmPrincipal.lbMensaje.setText("");
    }//GEN-LAST:event_btListarActionPerformed

    private void txtFechaventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaventaActionPerformed
        if(Validaciones.esFecha(txtFechaventa))
            Validaciones.pinta_text(txtFechaventa);
    }//GEN-LAST:event_txtFechaventaActionPerformed

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        if (Validaciones.esLetras(txtNombre))
            Validaciones.pinta_text(txtNombre);
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtFechaventaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaventaKeyReleased
        if(Validaciones.esFecha(txtFechaventa))
            Validaciones.pinta_text(txtFechaventa);
    }//GEN-LAST:event_txtFechaventaKeyReleased

    private void jComboBox1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyReleased
        if (Validaciones.comboBox(jComboBox1))
        Validaciones.pinta_text(jComboBox1);
    }//GEN-LAST:event_jComboBox1KeyReleased

    private void txtCodigoPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoPedidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoPedidoActionPerformed

    private void txtDireccionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyReleased
        if (Validaciones.esLetras(txtDireccion))
            Validaciones.pinta_text(txtDireccion);
    }//GEN-LAST:event_txtDireccionKeyReleased

    private void txtTelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyReleased
        if (Validaciones.esTelefono(txtTelefono))
            Validaciones.pinta_text(txtTelefono);
    }//GEN-LAST:event_txtTelefonoKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        frmPrincipal.mostrarFactura();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraBotones;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btEliminar;
    private javax.swing.JButton btGuardar;
    private javax.swing.JButton btListar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelDatos;
    private javax.swing.JScrollPane panelTabla;
    private javax.swing.JPanel panelTitulo;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCedCliente;
    private javax.swing.JTextField txtCodigoPedido;
    private javax.swing.JTextField txtDato;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtFechaventa;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNumfac;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
