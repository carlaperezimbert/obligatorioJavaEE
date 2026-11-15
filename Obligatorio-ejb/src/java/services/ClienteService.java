
package services;

import entities.Cliente;
import jakarta.ejb.Stateless;
import jakarta.ejb.LocalBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;


@Stateless
@LocalBean
public class ClienteService {

    // Inyectamos el EntityManager apuntando a tu PU
    @PersistenceContext(unitName = "Obligatorio-ejbPU")
    private EntityManager em;

    // 1. CREAR o GUARDAR un cliente
    public void crear(Cliente cliente) {
        em.persist(cliente);
    }

    // 2. ACTUALIZAR un cliente existente
    public void actualizar(Cliente cliente) {
        em.merge(cliente);
    }

    // 3. ELIMINAR un cliente por su ID
    public void eliminar(Long codigo) {
    Cliente cliente = em.find(Cliente.class, codigo);
    if (cliente != null) {
        // Consultamos si el cliente tiene reservas asociadas
        Long cantidadReservas = em.createQuery(
            "SELECT COUNT(r) FROM Reserva r WHERE r.cliente.codigo = :codigoCliente", Long.class)
            .setParameter("codigoCliente", codigo)
            .getSingleResult();

        // Si tiene una o más reservas, denegamos la eliminación
        if (cantidadReservas > 0) {
            throw new RuntimeException("No se puede eliminar el cliente porque tiene reservas registradas.");
        }

        // Si no tiene reservas, procedemos a eliminarlo
        em.remove(cliente);
    }
} 

    // 4. BUSCAR un cliente por su ID único
    public Cliente buscarPorId(Long id) {
        return em.find(Cliente.class, id);
    }

    // 5. LISTAR todos los clientes registrados
    public List<Cliente> listarTodos() {
        return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }
}
