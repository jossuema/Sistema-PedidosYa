/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Modelos.Pedido;
import Vistas.frmPrincipal;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author negri
 */
public class cPedido {
    ArrayList<Pedido> Lista = new ArrayList<>();
    
    public int Count() {
        return Lista.size();
    }
    
    public void Clear() {
        Lista.clear();
    }
    
    public void nuevo(Pedido e) throws IOException {
        int pos = localizar(e.Codigo_pedido);
        if (pos == -1) {//si cedula no esta registrada, se agrega nuevo estudiante
            Lista.add(e);
        } else {
            frmPrincipal.lbMensaje.setText("Codigo de pedido ya asignado");
        }
    }
    
    public void modificar(Pedido e, String cod) throws IOException {
        int pos = localizar(cod);
        if (pos > -1) {//si estudiante esta registrado se modifica
            Lista.set(pos, e);
        } else {
            throw new RuntimeException("No existe un pedido registrado con el codigo ingresado");
        }
    }
    
    public void eliminar(String cod) throws IOException {
        int pos = localizar(cod);
        if (pos > -1) {//si estudiante esta registrado se elimina
            Lista.remove(pos);
        } else {
            throw new RuntimeException("No existe un pedido registrado con el codigo ingresado");
        }
    }
    
    public DefaultTableModel getTabla() {
        //encabezados de columnas de la tabla
        String[] columnName = {"No.", "Codigo Pedido", "Cedula Cliente", "Direccion Entrega", "Descripcion", "Valor Pedido", "Fecha Pedido", "Cedula Repartidor", "Costo Delivery"};        
        DefaultTableModel tabla = new DefaultTableModel(columnName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = 0; i < Lista.size(); i++) {
            Pedido e = getPedido(i);
            Object[] row = {
                (i + 1), e.Codigo_pedido, e.Cedula_cliente, e.Direccion_entrega, e.Descripcion,
                e.Valor_pedido, e.Fecha_pedido, e.Cedula_repartidor, e.Costo_delivery
            };
            tabla.addRow(row);
        }
        return tabla;
    }
    
    public int localizar(String cod) {
        int pos = -1; //se retorna -1 si no se encuentra en el arreglo
        for (int i = 0; i < Lista.size(); i++) {
            Pedido e = getPedido(i);
            if (cod.equals(e.Codigo_pedido)) {
                pos = i; //posicion encontrada
                break; //finaliza el ciclo for
            }
        }
        return pos;
    }
    
    public cPedido buscar_codigoPedido(String cod) throws IOException {
        cPedido ob = new cPedido();
        for (int i = 0; i < Lista.size(); i++) {
            Pedido e = getPedido(i);
            if (e.Codigo_pedido.toLowerCase().startsWith(cod)) {
                ob.nuevo(e);
            }
        }
        return ob;
    }
    
    public cPedido buscar_cedulaCliente(String cedulaCli) throws IOException {
        cPedido ob = new cPedido();
        for (int i = 0; i < Lista.size(); i++) {
            Pedido e = getPedido(i);
            if (e.Cedula_cliente.toLowerCase().startsWith(cedulaCli)) {
                ob.nuevo(e);
            }
        }
        return ob;
    }
    
    public Pedido getPedido(int pos) {
        Pedido ob = null;
        if (pos >= 0 && pos < Lista.size()) {
            ob = Lista.get(pos);
        }
        return ob;
    }
    
    public static final String SEPARADOR = ";";
    public static final String QUOTE = "\"";
    //nombre del archivo csv
    public String path = Global.getPath() + "\\Data\\dataPedido.csv";

    public void leer() throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            System.out.println("Datos del archivo: ");
            String line = br.readLine();
            System.out.println(line);
            Clear(); //limpiar lista de objetos del arreglo
            line = br.readLine();
            while (line != null) {
                String[] row = line.split(SEPARADOR);
                removeTrailingQuotes(row);
                Pedido ob = new Pedido();
                ob.Codigo_pedido = row[0];
                ob.Cedula_cliente = row[1];
                ob.Direccion_entrega = row[2];
                ob.Descripcion = row[3];
                ob.Valor_pedido = Double.parseDouble(row[4]);
                ob.Fecha_pedido = row[5];
                ob.Cedula_repartidor = row[6];
                ob.Costo_delivery = Double.parseDouble(row[7]);
                nuevo(ob);//agregar a la lista	           
                System.out.println(Arrays.toString(row));
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.print(e.getMessage());
        } finally {
            if (null != br) {
                br.close();
            }
        }
    }

    //eliminar comillas
    private static String[] removeTrailingQuotes(String[] fields) {
        String result[] = new String[fields.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = fields[i].replaceAll("^" + QUOTE, "").replaceAll(QUOTE + "$", "");
        }
        return result;
    }

    public void guardar() throws IOException {
        FileWriter file;
        try {
            file = new FileWriter(path);
            final String NEXT_LINE = "\n";
            file.append("CodPedido").append(SEPARADOR);
            file.append("CedCliente").append(SEPARADOR);
            file.append("DireccionEntrega").append(SEPARADOR);
            file.append("Descripcion").append(SEPARADOR);
            file.append("ValorPedido").append(SEPARADOR);
            file.append("FechaPedido").append(SEPARADOR);
            file.append("CedRepartidor").append(SEPARADOR);
            file.append("CostoDelivery").append(NEXT_LINE);

            for (int i = 0; i < Lista.size(); i++) {
                Pedido ob = Lista.get(i);
                file.append(ob.Codigo_pedido).append(SEPARADOR);
                file.append(ob.Cedula_cliente).append(SEPARADOR);
                file.append(ob.Direccion_entrega).append(SEPARADOR);
                file.append(ob.Descripcion).append(SEPARADOR);
                file.append(String.valueOf(ob.Valor_pedido)).append(SEPARADOR);
                file.append(ob.Fecha_pedido).append(SEPARADOR);
                file.append(ob.Cedula_repartidor).append(SEPARADOR);
                file.append(String.valueOf(ob.Costo_delivery)).append(NEXT_LINE);
            }
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
}
