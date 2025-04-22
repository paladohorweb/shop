package jgm.tiendaVirtual.exception;

/**
 *
 * @author Jorge
 */
// PedidoNotFoundException.java
public class PedidoNotFoundException extends RuntimeException {
    public PedidoNotFoundException(Long pedidoId) {
        super("Pedido no encontrado con ID: " + pedidoId);
    }
}

