/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author negri
 */
public class Cliente {
    public String Cedula_cliente;
    public String Apellido;
    public String Nombre;
    public String Telefono;
    public String Direccion_domicilio;
    public String Ciudad;
    public String Num_casa;

    public Cliente() {
    }
    public Cliente(String Cedula_cliente, String Nombre, String Aepllido, String Telefono, String Direccion_domicilio, String Ciudad, String NumCasa) {
        this.Cedula_cliente = Cedula_cliente;
        this.Nombre = Nombre;
        this.Apellido = Aepllido;
        this.Telefono = Telefono;
        this.Direccion_domicilio = Direccion_domicilio;
        this.Ciudad = Ciudad;
        this.Num_casa = NumCasa;
    }
    
}
