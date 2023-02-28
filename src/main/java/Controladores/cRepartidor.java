/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Modelos.Repartidor;
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
public class cRepartidor {
    ArrayList<Repartidor> Lista = new ArrayList<>();
    
    public int Count() {
        return Lista.size();
    }
    
    public void Clear() {
        Lista.clear();
    }
    
    public void nuevo(Repartidor e) throws IOException {
        int pos = localizar(e.Cedula_repartidor);
        if (pos == -1) {//si cedula no esta registrada, se agrega nuevo estudiante
            Lista.add(e);
        } else {
            frmPrincipal.lbMensaje.setText("# de C�dula ya asignado a otro repartidor");
        }
    }
    
    public void modificar(Repartidor e, String ced) throws IOException {
        int pos = localizar(ced);
        if (pos > -1) {//si estudiante esta registrado se modifica
            Lista.set(pos, e);
        } else {
            throw new RuntimeException("No existe un repartidor registrado con la cedula ingresada");
        }
    }
    
    public void eliminar(String ced) throws IOException {
        int pos = localizar(ced);
        if (pos > -1) {//si estudiante esta registrado se elimina
            Lista.remove(pos);
        } else {
            throw new RuntimeException("No existe un repartidor registrado con la cedula ingresada");
        }
    }
    
    public DefaultTableModel getTabla() {
        //encabezados de columnas de la tabla
        String[] columnName = {"No.", "Cedula Cliente" ,"Nombre", "Apellido", "Telefono", "Direccion Domicilio"};        
        DefaultTableModel tabla = new DefaultTableModel(columnName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = 0; i < Lista.size(); i++) {
            Repartidor e = getRepartidor(i);
            Object[] row = {
                (i + 1),e.Cedula_repartidor, e.Nombre, e.Apellido, e.Telefono,e.Direccion_domicilio
            };
            tabla.addRow(row);
        }
        return tabla;
    }
    
    public int localizar(String ced) {
        int pos = -1; //se retorna -1 si no se encuentra en el arreglo
        for (int i = 0; i < Lista.size(); i++) {
            Repartidor e = getRepartidor(i);
            if (ced.equals(e.Cedula_repartidor)) {
                pos = i; //posicion encontrada
                break; //finaliza el ciclo for
            }
        }
        return pos;
    }
    
    public cRepartidor buscar_CedulaRepartidor(String ced) throws IOException {
        cRepartidor ob = new cRepartidor();
        for (int i = 0; i < Lista.size(); i++) {
            Repartidor e = getRepartidor(i);
            if (e.Cedula_repartidor.startsWith(String.valueOf(ced))) {
                ob.nuevo(e);
            }
        }
        return ob;
    }
    
    public cRepartidor buscar_Nombre(String nom) throws IOException {
        cRepartidor ob = new cRepartidor();
        for (int i = 0; i < Lista.size(); i++) {
            Repartidor e = getRepartidor(i);
            if (e.Nombre.toLowerCase().startsWith(nom)) {
                ob.nuevo(e);
            }
        }
        return ob;
    }
    
    public Repartidor getRepartidor(int pos) {
        Repartidor ob = null;
        if (pos >= 0 && pos < Lista.size()) {
            ob = Lista.get(pos);
        }
        return ob;
    }
    
    public static final String SEPARADOR = ";";
    public static final String QUOTE = "\"";
    //nombre del archivo csv
    public String path = Global.getPath() + "\\Data\\dataRepartidor.csv";

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
                Repartidor ob = new Repartidor();
                ob.Cedula_repartidor = row[0];
                ob.Nombre = row[1];
                ob.Apellido = row[2];
                ob.Telefono = row[3];
                ob.Direccion_domicilio = row[4];
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
            file.append("CedulaRepartidor").append(SEPARADOR);
            file.append("Nombre").append(SEPARADOR);
            file.append("Apellido").append(SEPARADOR);
            file.append("Telefono").append(SEPARADOR);
            file.append("DireccionDomicilio").append(NEXT_LINE);

            for (int i = 0; i < Lista.size(); i++) {
                Repartidor ob = Lista.get(i);
                file.append(ob.Cedula_repartidor).append(SEPARADOR);
                file.append(ob.Nombre).append(SEPARADOR);
                file.append(ob.Apellido).append(SEPARADOR);
                file.append(ob.Telefono).append(SEPARADOR);
                file.append(ob.Direccion_domicilio).append(NEXT_LINE);
            }
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
}
