
package entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cancha")
public class Cancha implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String deporte;

    // Constructores, Getters y Setters
    public Cancha() {}

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDeporte() { return deporte; }
    public void setDeporte(String deporte) { this.deporte = deporte; }
}
