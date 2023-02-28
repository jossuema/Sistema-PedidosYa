/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author negri
 */
public class Pedido {
    public String Codigo_pedido;
    public String Cedula_cliente;
    public String Direccion_entrega;
    public String Descripcion;
    public double Valor_pedido;
    public String Fecha_pedido;
    public String Cedula_repartidor;
    public double Costo_delivery;
    
    public Pedido() {
    }
    
    
    public Pedido(String Codigo_pedido, String Cedula_cliente, String Direccion_entrega, String Descripcion, double Valor_pedido, String Fecha_pedido, String Cedula_repartidor, double Costo_delivery) {
        this.Codigo_pedido = Codigo_pedido;
        this.Cedula_cliente = Cedula_cliente;
        this.Direccion_entrega = Direccion_entrega;
        this.Descripcion = Descripcion;
        this.Valor_pedido = Valor_pedido;
        this.Fecha_pedido = Fecha_pedido;
        this.Cedula_repartidor = Cedula_repartidor;
        this.Costo_delivery = Costo_delivery;
    }
    
}
