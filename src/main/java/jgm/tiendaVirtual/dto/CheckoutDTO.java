package jgm.tiendaVirtual.dto;

import jgm.tiendaVirtual.model.MetodoPago;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutDTO {
    private Long usuarioId;
    private MetodoPago metodoPago;
}

