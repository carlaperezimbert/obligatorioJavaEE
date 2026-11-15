
package services;

import jakarta.ejb.Stateless;
import jakarta.ejb.LocalBean;
import entities.Cancha;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;


@Stateless
@LocalBean
public class CanchaService {

    @PersistenceContext(unitName = "Obligatorio-ejbPU")
    private EntityManager em;

    public void crear(Cancha cancha) {
        em.persist(cancha);
    }

    public void actualizar(Cancha cancha) {
        em.merge(cancha);
    }

    public void eliminarCancha(Long codigo) {
    Cancha cancha = em.find(Cancha.class, codigo);
    if (cancha != null) {
        // Validamos si tiene reservas asociadas
        List<?> reservas = em.createQuery("SELECT r FROM Reserva r WHERE r.cancha.codigo = :cod")
                             .setParameter("cod", codigo)
                             .getResultList();
        if (!reservas.isEmpty()) {
            throw new RuntimeException("No se puede eliminar la cancha porque tiene reservas registradas.");
        }
        em.remove(cancha);
    }
}

    public Cancha buscarPorId(Long id) {
        return em.find(Cancha.class, id);
    }

    public List<Cancha> listarTodas() {
        return em.createQuery("SELECT c FROM Cancha c", Cancha.class).getResultList();
    }
}
