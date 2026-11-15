
package services;

import jakarta.ejb.LocalBean;
import entities.Reserva;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;


@Stateless
@LocalBean
public class ReservaService {

    @PersistenceContext(unitName = "Obligatorio-ejbPU")
    private EntityManager em;

    public void registrarReserva(Reserva reserva) {
    // Opcional: validar que el turno esté entre 1 y 5
    if (reserva.getTurno() < 1 || reserva.getTurno() > 5) {
        throw new IllegalArgumentException("El turno debe ser un valor entre 1 y 5.");
    }
    em.persist(reserva);
}

    public void actualizar(Reserva reserva) {
        em.merge(reserva);
    }

    public void eliminar(Long id) {
        Reserva reserva = em.find(Reserva.class, id);
        if (reserva != null) {
            em.remove(reserva);
        }
    }

    public Reserva buscarPorId(Long id) {
        return em.find(Reserva.class, id);
    }

    public List<Reserva> listarTodas() {
        return em.createQuery("SELECT r FROM Reserva r", Reserva.class).getResultList();
    }
    
    // Ejemplo de consulta personalizada útil para reservas: buscar por cliente
    public List<Reserva> listarPorCliente(Long clienteId) {
        return em.createQuery("SELECT r FROM Reserva r WHERE r.cliente.id = :clienteId", Reserva.class)
                 .setParameter("clienteId", clienteId)
                 .getResultList();
    }
    
    public List<Reserva> listarReservasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
    return em.createQuery(
        "SELECT r FROM Reserva r WHERE r.fecha >= :inicio AND r.fecha <= :fin ORDER BY r.fecha ASC, r.turno ASC", 
        Reserva.class)
        .setParameter("inicio", fechaInicio)
        .setParameter("fin", fechaFin)
        .getResultList();
    }
}
