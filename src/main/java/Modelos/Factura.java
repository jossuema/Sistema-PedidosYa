/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author negri
 */
public class Factura {
    public int Num_fact;
    public String Codigo_pedido;
    public String Cedula_cliente;
    public String Nombre;
    public String Apellido;
    public String Telefono;
    public String Direccion_domicilio;
    public String Ciudad;
    public String Fecha_venta;
    
    public Factura() {
    }
    public Factura(int Num_fact, String Codigo_pedido, String Cedula_cliente, String Nombre, String Apellido, String Telefono, String Direccion_domicilio, String Ciudad, String Fecha_venta) {
        this.Num_fact = Num_fact;
        this.Codigo_pedido = Codigo_pedido;
        this.Cedula_cliente = Cedula_cliente;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Telefono = Telefono;
        this.Direccion_domicilio = Direccion_domicilio;
        this.Ciudad = Ciudad;
        this.Fecha_venta = Fecha_venta;
    }
    
    
}
