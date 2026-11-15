
package entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Table(name = "reserva")
public class Reserva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private int turno;

    // Relación con Cliente (Muchos a Uno)
    @ManyToOne
    @JoinColumn(name = "codigo_cliente", nullable = false)
    private Cliente cliente;

    // Relación con Cancha (Muchos a Uno)
    @ManyToOne
    @JoinColumn(name = "codigo_cancha", nullable = false)
    private Cancha cancha;

    // Constructores, Getters y Setters
    public Reserva() {}

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public int getTurno() { return turno; }
    public void setTurno(int turno) { this.turno = turno; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Cancha getCancha() { return cancha; }
    public void setCancha(Cancha cancha) { this.cancha = cancha; }
}
