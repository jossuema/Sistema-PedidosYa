/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Modelos.Cliente;
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
public class cCliente {
    
    ArrayList<Cliente> Lista = new ArrayList<>();
    
    public int Count() {
        return Lista.size();
    }
    
    public void Clear() {
        Lista.clear();
    }
    
    public void nuevo(Cliente e) throws IOException {
        int pos = localizar(e.Cedula_cliente);
        if (pos == -1) {//si cedula no esta registrada, se agrega nuevo estudiante
            Lista.add(e);
        } else {
            frmPrincipal.lbMensaje.setText("# de C�dula ya asignado a otro cliente");
        }
    }
    
    public void modificar(Cliente e, String ced) throws IOException {
        int pos = localizar(ced);
        if (pos > -1) {//si estudiante esta registrado se modifica
            Lista.set(pos, e);
        } else {
            throw new RuntimeException("No existe un cliente registrado con la cedula ingresada");
        }
    }
    
    public void eliminar(String cedula) throws IOException {
        int pos = localizar(cedula);
        if (pos > -1) {//si estudiante esta registrado se elimina
            Lista.remove(pos);
        } else {
            throw new RuntimeException("No existe un cliente registrado con la cedula ingresada");
        }
    }
    
    public DefaultTableModel getTabla() {
        //encabezados de columnas de la tabla
        String[] columnName = {"No.", "Cedula", "Apellido", "Nombre", "Telefono", "Direccion", "Ciudad", "Numero de Casa"};        
        DefaultTableModel tabla = new DefaultTableModel(columnName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = 0; i < Lista.size(); i++) {
            Cliente e = getCliente(i);
            Object[] row = {
                (i + 1), e.Cedula_cliente, e.Apellido, e.Nombre, e.Telefono,
                e.Direccion_domicilio, e.Ciudad, e.Num_casa
            };
            tabla.addRow(row);
        }
        return tabla;
    }
    
    public int localizar(String cedula) {
        int pos = -1; //se retorna -1 si no se encuentra en el arreglo
        for (int i = 0; i < Lista.size(); i++) {
            Cliente e = getCliente(i);
            if (cedula.equals(e.Cedula_cliente)) {
                pos = i; //posicion encontrada
                break; //finaliza el ciclo for
            }
        }
        return pos;
    }
    
    public cCliente buscar_cedula(String cedula) throws IOException {
        cCliente ob = new cCliente();
        for (int i = 0; i < Lista.size(); i++) {
            Cliente e = getCliente(i);
            if (e.Cedula_cliente.toLowerCase().startsWith(cedula)) {
                ob.nuevo(e);
            }
        }
        return ob;
    }
    
    public cCliente buscar_apellido(String apellido) throws IOException {
        cCliente ob = new cCliente();
        for (int i = 0; i < Lista.size(); i++) {
            Cliente e = getCliente(i);
            if (e.Apellido.toLowerCase().startsWith(apellido)) {
                ob.nuevo(e);
            }
        }
        return ob;
    }
    
    public Cliente getCliente(int pos) {
        Cliente ob = null;
        if (pos >= 0 && pos < Lista.size()) {
            ob = Lista.get(pos);
        }
        return ob;
    }
    
    public static final String SEPARADOR = ";";
    public static final String QUOTE = "\"";
    //nombre del archivo csv
    public String path = Global.getPath() + "\\Data\\dataCliente.csv";

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
                Cliente ob = new Cliente();
                ob.Cedula_cliente = row[0];
                ob.Apellido = row[1];
                ob.Nombre = row[2];
                ob.Telefono = row[3];
                ob.Direccion_domicilio = row[4];
                ob.Ciudad = row[5];
                ob.Num_casa = row[6];
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
            file.append("Cedula").append(SEPARADOR);
            file.append("Apellido").append(SEPARADOR);
            file.append("Nombre").append(SEPARADOR);
            file.append("Telefono").append(SEPARADOR);
            file.append("Direccion").append(SEPARADOR);
            file.append("Ciudad").append(SEPARADOR);
            file.append("Num_casa").append(NEXT_LINE);

            for (int i = 0; i < Lista.size(); i++) {
                Cliente ob = Lista.get(i);
                file.append(ob.Cedula_cliente).append(SEPARADOR);
                file.append(ob.Apellido).append(SEPARADOR);
                file.append(ob.Nombre).append(SEPARADOR);
                file.append(ob.Telefono).append(SEPARADOR);
                file.append(ob.Direccion_domicilio).append(SEPARADOR);
                file.append(ob.Ciudad).append(SEPARADOR);
                file.append(ob.Num_casa).append(NEXT_LINE);
            }
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
}
