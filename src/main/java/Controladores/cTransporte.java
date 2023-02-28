/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Modelos.Transporte;
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
public class cTransporte {
    ArrayList<Transporte> Lista = new ArrayList<>();
    
    public int Count() {
        return Lista.size();
    }
    
    public void Clear() {
        Lista.clear();
    }
    
    public void nuevo(Transporte e) throws IOException {
        int pos = localizar(e.Placa);
        if (pos == -1) {//si cedula no esta registrada, se agrega nuevo estudiante
            Lista.add(e);
        } else {
            frmPrincipal.lbMensaje.setText("Placa ya asignado a otro Carro");
        }
    }
    
    public void modificar(Transporte e, String pla) throws IOException {
        int pos = localizar(pla);
        if (pos > -1) {//si estudiante esta registrado se modifica
            Lista.set(pos, e);
        } else {
            throw new RuntimeException("No existe un transporte registrado con la placa ingresada");
        }
    }
    
    public void eliminar(String pla) throws IOException {
        int pos = localizar(pla);
        if (pos > -1) {//si estudiante esta registrado se elimina
            Lista.remove(pos);
        } else {
            throw new RuntimeException("No existe un transporte registrado con la placa ingresada");
        }
    }
    
    public DefaultTableModel getTabla() {
        //encabezados de columnas de la tabla
        String[] columnName = {"No.", "Placa", "Tipo de Transporte", "Descripcion", "Disponibilidad", "Cedula Repartidor", "Modelo"};        
        DefaultTableModel tabla = new DefaultTableModel(columnName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = 0; i < Lista.size(); i++) {
            Transporte e = getTransporte(i);
            Object[] row = {
                (i + 1), e.Placa, e.Tipo_transporte, e.Descripcion, e.Disponibilidad,
                e.Cedula_repartidor, e.Modelo_vehiculo
            };
            tabla.addRow(row);
        }
        return tabla;
    }
    
    public int localizar(String pla) {
        int pos = -1; //se retorna -1 si no se encuentra en el arreglo
        for (int i = 0; i < Lista.size(); i++) {
            Transporte e = getTransporte(i);
            if (pla.equals(e.Placa)) {
                pos = i; //posicion encontrada
                break; //finaliza el ciclo for
            }
        }
        return pos;
    }
    
    public cTransporte buscar_Placa(String pla) throws IOException {
        cTransporte ob = new cTransporte();
        for (int i = 0; i < Lista.size(); i++) {
            Transporte e = getTransporte(i);
            if (e.Placa.toLowerCase().startsWith(pla)) {
                ob.nuevo(e);
            }
        }
        return ob;
    }
    
    public cTransporte buscar_modelo(String modelo) throws IOException {
        cTransporte ob = new cTransporte();
        for (int i = 0; i < Lista.size(); i++) {
            Transporte e = getTransporte(i);
            if (e.Modelo_vehiculo.toLowerCase().startsWith(modelo)) {
                ob.nuevo(e);
            }
        }
        return ob;
    }
    
    public Transporte getTransporte(int pos) {
        Transporte ob = null;
        if (pos >= 0 && pos < Lista.size()) {
            ob = Lista.get(pos);
        }
        return ob;
    }
    
    public static final String SEPARADOR = ";";
    public static final String QUOTE = "\"";
    //nombre del archivo csv
    public String path = Global.getPath() + "\\Data\\dataTransporte.csv";

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
                Transporte ob = new Transporte();
                ob.Placa = row[0];
                ob.Tipo_transporte = row[1];
                ob.Descripcion = row[2];
                ob.Disponibilidad = Boolean.parseBoolean(row[3]);
                ob.Cedula_repartidor = row[4];
                ob.Modelo_vehiculo = row[5];
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
            file.append("Placa").append(SEPARADOR);
            file.append("TipoTransporte").append(SEPARADOR);
            file.append("Descripcion").append(SEPARADOR);
            file.append("Disponibilidad").append(SEPARADOR);
            file.append("CedulaRepartidor").append(SEPARADOR);
            file.append("ModeloVehiculo").append(NEXT_LINE);

            for (int i = 0; i < Lista.size(); i++) {
                Transporte ob = Lista.get(i);
                file.append(ob.Placa).append(SEPARADOR);
                file.append(ob.Tipo_transporte).append(SEPARADOR);
                file.append(ob.Descripcion).append(SEPARADOR);
                file.append(String.valueOf(ob.Disponibilidad)).append(SEPARADOR);
                file.append(ob.Cedula_repartidor).append(SEPARADOR);
                file.append(ob.Modelo_vehiculo).append(NEXT_LINE);
            }
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
}
