package jgm.tiendaVirtual.dto;

import java.math.BigDecimal;
import java.util.List;

public class CarritoDTO {
    private Long usuarioId;
    private BigDecimal total;
    private List<CarritoItemDTO> items;

    public CarritoDTO() {
    }

    public CarritoDTO(Long usuarioId, BigDecimal total, List<CarritoItemDTO> items) {
        this.usuarioId = usuarioId;
        this.total = total;
        this.items = items;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<CarritoItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CarritoItemDTO> items) {
        this.items = items;
    }
}



