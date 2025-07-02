package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.domain.entity.User;
import org.example.murilo.ordemservico.domain.entity.WorkOrder;
import org.example.murilo.ordemservico.enumeration.OperationEnum;
import org.example.murilo.ordemservico.enumeration.ResponseStatusEnum;

import java.util.List;
import java.util.Map;

public class WorkOrderListDto extends BaseResponseDto {

    private final List<WorkOrderDto> ordens;

    protected WorkOrderListDto(final String status,
                               final String operacao,
                               final String mensagem,
                               final List<WorkOrderDto> ordens) {
        super(status, operacao, mensagem);
        this.ordens = ordens;
    }

    public static WorkOrderListDto toDto(final List<WorkOrder> orders,
                                         final Map<Integer, User> authorsMap) {
        return new WorkOrderListDto(
            ResponseStatusEnum.SUCESSO.getId(),
            OperationEnum.LISTAR_ORDENS.getId(),
            null,
            WorkOrderDto.toDto(orders, authorsMap)
        );
    }

    public List<WorkOrderDto> getOrdens() {
        return ordens;
    }
}
