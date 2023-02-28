/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author negri
 */
public class Transporte {
    public String Placa;
    public String Tipo_transporte;
    public String Descripcion;
    public boolean Disponibilidad;
    public String Cedula_repartidor;
    public String Modelo_vehiculo;
    
    public Transporte() {
    }
    public Transporte(String Placa, String Tipo_transporte, String Descripcion, boolean Disponibilidad, String Cedula_repartidor, String Modelo_vehiculo) {
        this.Placa = Placa;
        this.Tipo_transporte = Tipo_transporte;
        this.Descripcion = Descripcion;
        this.Disponibilidad = Disponibilidad;
        this.Cedula_repartidor = Cedula_repartidor;
        this.Modelo_vehiculo = Modelo_vehiculo;
    }
    
    
    
}
