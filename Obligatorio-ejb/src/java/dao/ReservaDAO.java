package dao;


import entities.Reserva;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
public class ReservaDAO {
    /**
     * Requerimiento 3 y Nota 3: Guarda la reserva validando turno (1-5) y límite de 5 por fecha.
     */
    public void guardar(Reserva reserva) throws IllegalArgumentException, IllegalStateException {
        if (reserva.getTurno() < 1 || reserva.getTurno() > 5) {
            throw new IllegalArgumentException("El turno debe ser un entero entre 1 y 5.");
        }
        if (contarPorFecha(reserva.getFecha()) >= 5) {
            throw new IllegalStateException("Límite alcanzado: máximo 5 reservas por fecha.");
        }
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(reserva);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

 public List listarEntreFechas(Date fechaInicio, Date fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery query = em.createQuery(
                "SELECT r FROM Reserva r JOIN FETCH r.cliente JOIN FETCH r.cancha " +
                "WHERE r.fecha BETWEEN :desde AND :hasta " +
                "ORDER BY r.fecha ASC, r.turno ASC", Reserva.class);
            query.setParameter("desde", fechaInicio);
            query.setParameter("hasta", fechaFin);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    /**
     * Nota 3: Cuenta la cantidad de reservas registradas para una fecha dada.
     */
    public long contarPorFecha(Date fecha) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery query = em.createQuery(
                "SELECT COUNT(r) FROM Reserva r WHERE r.fecha = :fecha", Long.class);
            query.setParameter("fecha", fecha);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}