/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Controladores.cPedido;
import Modelos.Pedido;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author berth
 */
public class panelPedido extends javax.swing.JPanel {

    //datos globales
    public cPedido list = new cPedido();
    String clave = ""; //guarda el campo clave para editar

    //*******************METODOS PERSONALIZADOS *******************
    /**
     * Leer datos del formulario y guardar en un objeto
     * @return Objeto Estudiante
     */

    public Pedido leer() {
        Pedido ob = null;
        if (form_validado()) {
            ob = new Pedido();
            ob.Codigo_pedido= txtCodigo.getText();
            ob.Cedula_cliente =  txtCedula.getText();
            ob.Direccion_entrega = txtEntrega.getText();
            ob.Descripcion =  txtDescripcion.getText();
            ob.Valor_pedido = Double.parseDouble(txtPedido.getText());
            ob.Fecha_pedido = txtFechapedido.getText();
            ob.Cedula_repartidor =  txtCedRepartidor.getText();
            ob.Costo_delivery = Double.parseDouble(txtCostoDelivery.getText());
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
        
        if(!Validaciones.esNumero(txtCodigo)){
            ok = false;
            men += ", Codigo pedido";
        }
        if (!Validaciones.esCedula(txtCedula)) {
            ok = false;
            men += ", Cedula Cliente";
        }

        if (!Validaciones.esLetras(txtEntrega)) {
            ok = false;
            men += ", Direccion";
        }

        if (!Validaciones.esLetras(txtDescripcion)) {
            ok = false;
            men += ", Descripcion";
        }

        if (!Validaciones.esFlotante(txtPedido)) {
            ok = false;
            men += ", Valor pedido";
        }
        
        if(!Validaciones.esFecha(txtFechapedido)){
            ok = false;
            men += ", Fecha pedido";
        }
        
        if(!Validaciones.esCedula(txtCedRepartidor)){
            ok = false;
            men += ", Cedula repartidor";
        }
        
        if(!Validaciones.esFlotante(txtCostoDelivery)){
            ok = false;
            men += ", Costo Delivery";
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
        txtCodigo.setText("");
        txtEntrega.setText("");
        txtCedula.setText("");
        txtPedido.setText("");
        txtDescripcion.setText("");
        txtFechapedido.setText("");
        txtCostoDelivery.setText("");
        txtCedRepartidor.setText("");
        txtCodigo.requestFocus();  //envia curso o enfoque a la caja de texto cedula
        frmPrincipal.lbMensaje.setText("");
    }

    /*
     * quitar validaciones
     */
    public void quitar_validaciones() {
        Validaciones.pinta_text(txtCodigo);
        Validaciones.pinta_text(txtEntrega);
        Validaciones.pinta_text(txtCedula);
        Validaciones.pinta_text(txtPedido);
        Validaciones.pinta_text(txtDescripcion);
        Validaciones.pinta_text(txtFechapedido);
        Validaciones.pinta_text(txtCostoDelivery);
        Validaciones.pinta_text(txtCedRepartidor);
        frmPrincipal.lbMensaje.setText("");
    }

    /*
     * Ver registro
     */
    public final void ver_registro(int pos) {
        if (pos >= 0 && pos < list.Count()) {
            Pedido ob = list.getPedido(pos);
            txtCodigo.setText(ob.Codigo_pedido);
            txtCedula.setText(ob.Cedula_cliente);
            txtEntrega.setText(ob.Direccion_entrega);
            txtDescripcion.setText(ob.Descripcion);
            txtPedido.setText(String.valueOf(ob.Valor_pedido));
            txtFechapedido.setText(ob.Fecha_pedido);
            txtCedRepartidor.setText(ob.Cedula_repartidor);
            txtCostoDelivery.setText(String.valueOf(ob.Costo_delivery));
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
            int pos = list.localizar(txtCodigo.getText());
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
        Pedido ob = leer();
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
            cPedido p = list.buscar_codigoPedido(txtDato.getText()); //busca por cedula
            if (p.Count() == 0) {
                p = list.buscar_cedulaCliente(txtDato.getText()); //buscar por apellido
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
            list.eliminar(txtCodigo.getText());
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
        txtCodigo.setEditable(ok);
        txtEntrega.setEditable(ok);
        txtCedula.setEditable(ok);
        txtDescripcion.setEditable(ok);
        txtPedido.setEditable(ok);
        txtFechapedido.setEditable(ok);
        txtCedRepartidor.setEditable(ok);
        txtCostoDelivery.setEditable(ok);
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
    public panelPedido(String fun) {
        this.funcion =  fun;
        initComponents();
        try {
            list.leer();
        } catch (IOException ex) {
            Logger.getLogger(panelPedido.class.getName()).log(Level.SEVERE, null, ex);
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
        if(funcion.equals("Pedido")){
            limpiar_textos();
            clave = ""; //nuevo
            //habilitar textos
            habilitar_textos(true);
            //deshabilitar botones
            mostrar_botones(false);
            btCancelar.setEnabled(false);
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
        txtCedula = new javax.swing.JTextField();
        txtCodigo = new javax.swing.JTextField();
        txtEntrega = new javax.swing.JTextField();
        txtPedido = new javax.swing.JTextField();
        txtFechapedido = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        txtCedRepartidor = new javax.swing.JTextField();
        txtCostoDelivery = new javax.swing.JTextField();
        panelTabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtDato = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        panelTitulo.setBackground(new java.awt.Color(255, 51, 51));
        panelTitulo.setPreferredSize(new java.awt.Dimension(1070, 50));
        panelTitulo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Pedidos");
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

        panelDatos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Pedido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Codigo:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Cedula cliente:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Direccion entrega:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Valor Pedido:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Fecha Pedido:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Costo Delivery:");

        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCedulaKeyReleased(evt);
            }
        });

        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoKeyReleased(evt);
            }
        });

        txtEntrega.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEntregaKeyReleased(evt);
            }
        });

        txtPedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPedidoKeyReleased(evt);
            }
        });

        txtFechapedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechapedidoActionPerformed(evt);
            }
        });
        txtFechapedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFechapedidoKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Descripcion:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        txtDescripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescripcionKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(txtDescripcion);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Cedula repartidor:");

        txtCedRepartidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedRepartidorActionPerformed(evt);
            }
        });
        txtCedRepartidor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCedRepartidorKeyReleased(evt);
            }
        });

        txtCostoDelivery.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostoDeliveryKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelDatosLayout = new javax.swing.GroupLayout(panelDatos);
        panelDatos.setLayout(panelDatosLayout);
        panelDatosLayout.setHorizontalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(42, 42, 42)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelDatosLayout.createSequentialGroup()
                            .addComponent(txtFechapedido, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(34, 34, 34)
                            .addComponent(jLabel11)
                            .addGap(29, 29, 29)
                            .addComponent(txtCedRepartidor, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtEntrega, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelDatosLayout.createSequentialGroup()
                            .addComponent(txtCedula, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                            .addGap(384, 384, 384))
                        .addComponent(txtPedido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCostoDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        panelDatosLayout.setVerticalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE))
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)))
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFechapedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCedRepartidor, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCostoDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                .addComponent(barraBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBotonesLayout.createSequentialGroup()
                        .addComponent(barraBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelBotonesLayout.createSequentialGroup()
                        .addGap(0, 3, Short.MAX_VALUE)
                        .addComponent(panelDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(panelTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(341, 341, 341))))
        );

        add(panelBotones, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodigoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyReleased
        if (Validaciones.esNumero(txtCodigo))
            Validaciones.pinta_text(txtCodigo);
    }//GEN-LAST:event_txtCodigoKeyReleased

    private void txtEntregaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntregaKeyReleased
        if (Validaciones.esLetras(txtEntrega))
            Validaciones.pinta_text(txtEntrega);
    }//GEN-LAST:event_txtEntregaKeyReleased

    private void txtCedulaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyReleased
        if (Validaciones.esCedula(txtCedula))
            Validaciones.pinta_text(txtCedula);
    }//GEN-LAST:event_txtCedulaKeyReleased

    private void txtPedidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPedidoKeyReleased
        if (Validaciones.esRequerido(txtPedido))
            Validaciones.pinta_text(txtPedido);
    }//GEN-LAST:event_txtPedidoKeyReleased

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
        if (!txtCodigo.getText().trim().equals("")) {
            clave = txtCodigo.getText().trim(); //captura la cédula antes de modificar
            txtCodigo.requestFocus();  //envia curso o enfoque a la caja de texto cedula
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
            Pedido cl = leer();
            frmPrincipal.pedido = cl;
            frmPrincipal.mostrarMain("Pedido");
        }
        
    }//GEN-LAST:event_btGuardarActionPerformed

    private void btEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEliminarActionPerformed
        if (!txtCodigo.getText().trim().equals("")) {
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

    private void txtFechapedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechapedidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechapedidoActionPerformed

    private void txtFechapedidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechapedidoKeyReleased
        if (Validaciones.esFecha(txtFechapedido))
            Validaciones.pinta_text(txtFechapedido);
    }//GEN-LAST:event_txtFechapedidoKeyReleased

    private void txtCedRepartidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedRepartidorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedRepartidorActionPerformed

    private void txtCedRepartidorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedRepartidorKeyReleased
        if (Validaciones.esCedula(txtCedRepartidor))
            Validaciones.pinta_text(txtCedRepartidor);
    }//GEN-LAST:event_txtCedRepartidorKeyReleased

    private void txtDescripcionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionKeyReleased
        if (Validaciones.esLetras(txtDescripcion))
            Validaciones.pinta_text(txtDescripcion);
    }//GEN-LAST:event_txtDescripcionKeyReleased

    private void txtCostoDeliveryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoDeliveryKeyReleased
        if (Validaciones.esFlotante(txtCostoDelivery))
            Validaciones.pinta_text(txtCostoDelivery);
    }//GEN-LAST:event_txtCostoDeliveryKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraBotones;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btEliminar;
    private javax.swing.JButton btGuardar;
    private javax.swing.JButton btListar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelDatos;
    private javax.swing.JScrollPane panelTabla;
    private javax.swing.JPanel panelTitulo;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtCedRepartidor;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCostoDelivery;
    private javax.swing.JTextField txtDato;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtEntrega;
    private javax.swing.JTextField txtFechapedido;
    private javax.swing.JTextField txtPedido;
    // End of variables declaration//GEN-END:variables
}
