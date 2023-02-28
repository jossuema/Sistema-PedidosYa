/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Modelos.Factura;
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
public class cFactura {
    ArrayList<Factura> Lista = new ArrayList<>();
    
    public int Count() {
        return Lista.size();
    }
    
    public void Clear() {
        Lista.clear();
    }
    
    public void nuevo(Factura e) throws IOException {
        int pos = localizar(e.Num_fact);
        if (pos == -1) {//si cedula no esta registrada, se agrega nuevo estudiante
            Lista.add(e);
        } else {
            frmPrincipal.lbMensaje.setText("# de Factura ya asignado a otra factura");
        }
    }
    
    public void modificar(Factura e, int fac) throws IOException {
        int pos = localizar(fac);
        if (pos > -1) {//si estudiante esta registrado se modifica
            Lista.set(pos, e);
        } else {
            throw new RuntimeException("No existe una factura registrado con el numero ingresada");
        }
    }
    
    public void eliminar(int fac) throws IOException {
        int pos = localizar(fac);
        if (pos > -1) {//si estudiante esta registrado se elimina
            Lista.remove(pos);
        } else {
            throw new RuntimeException("No existe una factura registrada con la el numero ingresado");
        }
    }
    
    public DefaultTableModel getTabla() {
        //encabezados de columnas de la tabla
        String[] columnName = {"No.", "Numero Factura" ,"Codigo", "Cedula Cliente", "Nombre", "Apellido", "Telefono", "Direccion", "Ciudad", "Fecha"};        
        DefaultTableModel tabla = new DefaultTableModel(columnName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = 0; i < Lista.size(); i++) {
            Factura e = getFactura(i);
            Object[] row = {
                (i + 1),e.Num_fact, e.Codigo_pedido, e.Cedula_cliente, e.Nombre,e.Apellido, e.Telefono,
                e.Direccion_domicilio, e.Ciudad, e.Fecha_venta
            };
            tabla.addRow(row);
        }
        return tabla;
    }
    
    public int localizar(int fact) {
        int pos = -1; //se retorna -1 si no se encuentra en el arreglo
        for (int i = 0; i < Lista.size(); i++) {
            Factura e = getFactura(i);
            if (fact == e.Num_fact) {
                pos = i; //posicion encontrada
                break; //finaliza el ciclo for
            }
        }
        return pos;
    }
    
    public cFactura buscar_Numfactura(int fac) throws IOException {
        cFactura ob = new cFactura();
        for (int i = 0; i < Lista.size(); i++) {
            Factura e = getFactura(i);
            if (String.valueOf(e.Num_fact).startsWith(String.valueOf(fac))) {
                ob.nuevo(e);
            }
        }
        return ob;
    }
    
    public cFactura buscar_CodigoPedido(String codpe) throws IOException {
        cFactura ob = new cFactura();
        for (int i = 0; i < Lista.size(); i++) {
            Factura e = getFactura(i);
            if (e.Codigo_pedido.toLowerCase().startsWith(codpe)) {
                ob.nuevo(e);
            }
        }
        return ob;
    }
    
    public Factura getFactura(int pos) {
        Factura ob = null;
        if (pos >= 0 && pos < Lista.size()) {
            ob = Lista.get(pos);
        }
        return ob;
    }
    
    public static final String SEPARADOR = ";";
    public static final String QUOTE = "\"";
    //nombre del archivo csv
    public String path = Global.getPath() + "\\Data\\dataFactura.csv";

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
                Factura ob = new Factura();
                ob.Num_fact = Integer.parseInt(row[0]);
                ob.Codigo_pedido = row[1];
                ob.Cedula_cliente = row[2];
                ob.Nombre = row[3];
                ob.Apellido = row[4];
                ob.Telefono = row[5];
                ob.Direccion_domicilio = row[6];
                ob.Ciudad = row[7];
                ob.Fecha_venta = row[8];
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
            file.append("NumFact").append(SEPARADOR);
            file.append("CodigoPedido").append(SEPARADOR);
            file.append("Cedula").append(SEPARADOR);
            file.append("Nombre").append(SEPARADOR);
            file.append("Apellido").append(SEPARADOR);
            file.append("Telefono").append(SEPARADOR);
            file.append("Direccion").append(SEPARADOR);
            file.append("Ciudad").append(SEPARADOR);
            file.append("FechaVenta").append(NEXT_LINE);

            for (int i = 0; i < Lista.size(); i++) {
                Factura ob = Lista.get(i);
                file.append(String.valueOf(ob.Num_fact)).append(SEPARADOR);
                file.append(ob.Codigo_pedido).append(SEPARADOR);
                file.append(ob.Cedula_cliente).append(SEPARADOR);
                file.append(ob.Nombre).append(SEPARADOR);
                file.append(ob.Apellido).append(SEPARADOR);
                file.append(ob.Telefono).append(SEPARADOR);
                file.append(ob.Direccion_domicilio).append(SEPARADOR);
                file.append(ob.Ciudad).append(SEPARADOR);
                file.append(ob.Fecha_venta).append(NEXT_LINE);
            }
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
}
