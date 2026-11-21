package dao;


import entities.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
public class ClienteDAO {
    public void guardar(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cliente);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    public void actualizar(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(cliente);
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
            throw new IllegalStateException("No se puede eliminar el cliente: registra reservas activas.");
        }
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cliente cliente = em.find(Cliente.class, codigo);
            if (cliente != null) {
                em.remove(cliente);
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
    public Cliente buscarPorCodigo(int codigo) {
        EntityManager em = JPAUtil.getEntityManager();
        try { return em.find(Cliente.class, codigo); } finally { em.close(); }
    }
    public List listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery query = em.createQuery("SELECT c FROM Cliente c ORDER BY c.nombre ASC", Cliente.class);
            return query.getResultList();
        } finally { em.close(); }
    }
    public boolean tieneReservas(int codigoCliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery query = em.createQuery(
                "SELECT COUNT(r) FROM Reserva r WHERE r.cliente.codigo = :codigo", Long.class);
            query.setParameter("codigo", codigoCliente);
            return query.getSingleResult() > 0;
        } finally { em.close(); }
    }
}