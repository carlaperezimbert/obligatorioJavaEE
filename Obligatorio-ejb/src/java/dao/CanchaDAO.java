package dao;


import entities.Cancha;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
public class CanchaDAO {
    public void guardar(Cancha cancha) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cancha);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    public void actualizar(Cancha cancha) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(cancha);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

  public boolean eliminar(int codigo) throws IllegalStateException {
        if (tieneReservas(codigo)) {
            throw new IllegalStateException("No se puede eliminar la cancha: registra reservas asociadas.");
        }
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cancha cancha = em.find(Cancha.class, codigo);
            if (cancha != null) {
                em.remove(cancha);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    public Cancha buscarPorCodigo(int codigo) {
        EntityManager em = JPAUtil.getEntityManager();
        try { return em.find(Cancha.class, codigo); } finally { em.close(); }
    }
    public List listarTodas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery query = em.createQuery("SELECT c FROM Cancha c ORDER BY c.codigo ASC", Cancha.class);
            return query.getResultList();
        } finally { em.close(); }
    }
    public boolean tieneReservas(int codigoCancha) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(r) FROM Reserva r WHERE r.cancha.codigo = :codigo", Long.class);
            query.setParameter("codigo", codigoCancha);
            return query.getSingleResult() > 0;
        } finally { em.close(); }
    }
}