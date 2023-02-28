/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author negri
 */
public class Repartidor {
    public String Cedula_repartidor;
    public String Nombre;
    public String Apellido;
    public String Telefono;
    public String Direccion_domicilio;
    
    public Repartidor() {
    }
    public Repartidor(String Cedula_repartidor, String Nombre, String Aepllido, String Telefono, String Direccion_domicilio) {
        this.Cedula_repartidor = Cedula_repartidor;
        this.Nombre = Nombre;
        this.Apellido = Aepllido;
        this.Telefono = Telefono;
        this.Direccion_domicilio = Direccion_domicilio;
    }
    
}
