/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Controladores.cCliente;
import Modelos.Cliente;
import java.awt.CardLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author berth
 */
public class panelCliente extends javax.swing.JPanel {

    //datos globales
    public cCliente list = new cCliente();
    
    String clave = ""; //guarda el campo clave para editar

    //*******************METODOS PERSONALIZADOS *******************
    /**
     * Leer datos del formulario y guardar en un objeto
     * @return Objeto Estudiante
     */

    public Cliente leer() {
        Cliente ob = null;
        if (form_validado()) {
            ob = new Cliente();
            ob.Cedula_cliente= txtCedula.getText();
            ob.Apellido = txtApellidos.getText();
            ob.Nombre = txtNombres.getText();
            ob.Telefono = txtTelefono.getText();
            ob.Direccion_domicilio = txtDireccion.getText();
            ob.Ciudad = jComboBox1.getSelectedItem().toString();
            ob.Num_casa = txtNum.getText();
            System.out.print(ob);
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
        if (!Validaciones.esCedula(txtCedula)) {
            ok = false;
            men += ", Cedula";
        }

        if (!Validaciones.esLetras(txtNombres)) {
            ok = false;
            men += ", Nombre";
        }

        if (!Validaciones.esLetras(txtApellidos)) {
            ok = false;
            men += ", Apellido";
        }

        if (!Validaciones.esRequerido(txtDireccion)) {
            ok = false;
            men += ", Dirección";
        }
        
        if(!Validaciones.esTelefono(txtTelefono)){
            ok = false;
            men += ", Telefono";
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
        txtCedula.setText("");
        txtApellidos.setText("");
        txtNombres.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtNum.setText("");
        jComboBox1.setSelectedIndex(0);
        txtCedula.requestFocus();  //envia curso o enfoque a la caja de texto cedula
        frmPrincipal.lbMensaje.setText("");
    }

    /*
     * quitar validaciones
     */
    public void quitar_validaciones() {
        Validaciones.pinta_text(txtCedula);
        Validaciones.pinta_text(txtApellidos);
        Validaciones.pinta_text(txtNombres);
        Validaciones.pinta_text(txtDireccion);
        Validaciones.pinta_text(txtTelefono);
        Validaciones.pinta_text(txtNum);
        Validaciones.pinta_text(jComboBox1);
        frmPrincipal.lbMensaje.setText("");
    }

    /*
     * Ver registro
     */
    public final void ver_registro(int pos) {
        if (pos >= 0 && pos < list.Count()) {
            Cliente ob = list.getCliente(pos);
            txtCedula.setText(ob.Cedula_cliente);
            txtApellidos.setText(ob.Apellido);
            txtNombres.setText(ob.Nombre);
            txtDireccion.setText(ob.Direccion_domicilio);
            txtTelefono.setText(ob.Telefono);
            txtNum.setText(ob.Num_casa);
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
            int pos = list.localizar(txtCedula.getText());
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
        Cliente ob = leer();
        try {
            if (ob != null) {
                if (clave.equals("")) {//guardar un nuevo estudiante
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
            cCliente p = list.buscar_cedula(txtDato.getText()); //busca por cedula
            if (p.Count() == 0) {
                p = list.buscar_apellido(txtDato.getText()); //buscar por apellido
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
            list.eliminar(txtCedula.getText());
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
        txtCedula.setEditable(ok);
        txtApellidos.setEditable(ok);
        txtNombres.setEditable(ok);
        txtDireccion.setEditable(ok);
        txtTelefono.setEditable(ok);
        txtNum.setEditable(ok);
        jComboBox1.setEnabled(ok);
    }

    /*
     * Habilitar o desabilitar botones
     */
    public final void habilitar_botones(Boolean ok) {
        btNuevo.setEnabled(ok);
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
    public panelCliente(String fun) {
        this.funcion = fun;
        initComponents();
        try {
            list.leer();
        } catch (IOException ex) {
            Logger.getLogger(panelCliente.class.getName()).log(Level.SEVERE, null, ex);
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
        
        jButton1.setVisible(false);
        if(funcion.equals("Pedido")){
            jButton1.setVisible(true);
        }
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
        btNuevo = new javax.swing.JButton();
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
        txtNombres = new javax.swing.JTextField();
        txtCedula = new javax.swing.JTextField();
        txtApellidos = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtNum = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
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
        jLabel1.setText("Cliente");
        jLabel1.setPreferredSize(new java.awt.Dimension(500, 50));
        panelTitulo.add(jLabel1);

        add(panelTitulo, java.awt.BorderLayout.PAGE_START);

        barraBotones.setBackground(java.awt.SystemColor.controlHighlight);
        barraBotones.setOrientation(javax.swing.SwingConstants.VERTICAL);
        barraBotones.setRollover(true);
        barraBotones.setPreferredSize(new java.awt.Dimension(50, 80));

        btNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/nuevo.png"))); // NOI18N
        btNuevo.setToolTipText("Nuevo");
        btNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btNuevo.setMaximumSize(new java.awt.Dimension(80, 80));
        btNuevo.setMinimumSize(new java.awt.Dimension(80, 80));
        btNuevo.setPreferredSize(new java.awt.Dimension(80, 80));
        btNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNuevoActionPerformed(evt);
            }
        });
        barraBotones.add(btNuevo);

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

        panelDatos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Cédula:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Nombres:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Apellidos:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Dirección:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Ciudad:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Numero de casa:");

        txtNombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombresKeyReleased(evt);
            }
        });

        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCedulaKeyReleased(evt);
            }
        });

        txtApellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtApellidosKeyReleased(evt);
            }
        });

        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccionKeyReleased(evt);
            }
        });

        txtNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumActionPerformed(evt);
            }
        });
        txtNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNumKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Telefono:");

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
                        .addGap(195, 195, 195)
                        .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(txtNum)
                            .addComponent(txtApellidos, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                            .addComponent(txtDireccion)
                            .addComponent(txtNombres, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        panelDatosLayout.setVerticalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        jButton1.setText("Seleccionar Cliente");
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
                    .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(panelDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelBotonesLayout.createSequentialGroup()
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(txtDato, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(panelTabla, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 802, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                .addComponent(barraBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 338, Short.MAX_VALUE))
        );

        add(panelBotones, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCedulaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyReleased
        if (Validaciones.esCedula(txtCedula))
            Validaciones.pinta_text(txtCedula);
    }//GEN-LAST:event_txtCedulaKeyReleased

    private void txtApellidosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidosKeyReleased
        if (Validaciones.esLetras(txtApellidos))
            Validaciones.pinta_text(txtApellidos);
    }//GEN-LAST:event_txtApellidosKeyReleased

    private void txtNombresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombresKeyReleased
        if (Validaciones.esLetras(txtNombres))
            Validaciones.pinta_text(txtNombres);
    }//GEN-LAST:event_txtNombresKeyReleased

    private void txtDireccionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyReleased
        if (Validaciones.esRequerido(txtDireccion))
            Validaciones.pinta_text(txtDireccion);
    }//GEN-LAST:event_txtDireccionKeyReleased

    private void btNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNuevoActionPerformed
        quitar_validaciones();
        limpiar_textos();
        clave = ""; //nuevo
        //habilitar textos
        habilitar_textos(true);
        //deshabilitar botones
        habilitar_botones(false);
    }//GEN-LAST:event_btNuevoActionPerformed

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
        if (!txtCedula.getText().trim().equals("")) {
            clave = txtCedula.getText().trim(); //captura la cédula antes de modificar
            txtCedula.requestFocus();  //envia curso o enfoque a la caja de texto cedula
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
    }//GEN-LAST:event_btGuardarActionPerformed

    private void btEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEliminarActionPerformed
        if (!txtCedula.getText().trim().equals("")) {
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

    private void txtNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumActionPerformed

    private void txtTelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyReleased
        if (Validaciones.esTelefono(txtTelefono))
            Validaciones.pinta_text(txtTelefono);
    }//GEN-LAST:event_txtTelefonoKeyReleased

    private void jComboBox1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyReleased
        if (Validaciones.comboBox(jComboBox1))
            Validaciones.pinta_text(jComboBox1);
    }//GEN-LAST:event_jComboBox1KeyReleased

    private void txtNumKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumKeyReleased
        if (Validaciones.esNumero(txtNum))
            Validaciones.pinta_text(txtNum);
    }//GEN-LAST:event_txtNumKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int pos = tabla.getSelectedRow();
        if(pos>=0&&pos<list.Count()){
            Cliente cl = list.getCliente(pos);
            frmPrincipal.cliente = cl;
        
            frmPrincipal.mostrarMain("Cliente");
        }else{
            frmPrincipal.lbMensaje.setText("Escoja un cliente");
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraBotones;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btEliminar;
    private javax.swing.JButton btGuardar;
    private javax.swing.JButton btListar;
    private javax.swing.JButton btNuevo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtDato;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txtNum;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
