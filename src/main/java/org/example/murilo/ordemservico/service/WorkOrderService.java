package org.example.murilo.ordemservico.service;

import com.google.gson.Gson;
import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.domain.dto.BaseResponseDto;
import org.example.murilo.ordemservico.domain.dto.WorkOrderListDto;
import org.example.murilo.ordemservico.domain.entity.User;
import org.example.murilo.ordemservico.domain.entity.WorkOrder;
import org.example.murilo.ordemservico.domain.payload.AlterWorkOrderPayload;
import org.example.murilo.ordemservico.domain.payload.WorkOrderCreatePayload;
import org.example.murilo.ordemservico.domain.payload.WorkOrderListPayload;
import org.example.murilo.ordemservico.domain.payload.WorkOrderUpdatePayload;
import org.example.murilo.ordemservico.enumeration.*;
import org.example.murilo.ordemservico.handler.exception.*;
import org.example.murilo.ordemservico.repository.Database;
import org.example.murilo.ordemservico.repository.UserRepository;
import org.example.murilo.ordemservico.repository.WorkOrderRepository;
import org.example.murilo.ordemservico.util.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WorkOrderService {

    public String findAll(final WorkOrderListPayload payload) throws BaseException {
        try {
            final String token = payload.getToken();
            final WorkOrderFilterEnum filter = WorkOrderFilterEnum.getById(payload.getFiltro());

            if (token.isBlank()) {
                throw new InvalidTokenException(OperationEnum.LISTAR_ORDENS);
            }

            final User user = new UserRepository(Database.connect()).findOneByUsername(token);
            if (user == null) {
                throw new UserNotFoundException(OperationEnum.LISTAR_ORDENS);
            }

            String statusQuery = "1=1";
            if (!WorkOrderFilterEnum.TODAS.equals(filter)) {
                statusQuery = "wo.status = '" + filter.toString() + "'";
            }

            final List<WorkOrder> orders;
            final Map<Integer, User> authorsMap;
            if (UserRoleEnum.COMUM.equals(user.getRole())) {
                final Integer userId = user.getId();

                orders = new WorkOrderRepository(Database.connect()).findAllByUserIdAndStatusQuery(
                    userId, statusQuery
                );

                authorsMap = Map.of(user.getId(), user);
            } else {
                orders = new WorkOrderRepository(Database.connect()).findAllByStatusQuery(
                    statusQuery
                );

                final Set<Integer> authorIds = orders.parallelStream()
                    .map(WorkOrder::getCreatedById)
                    .collect(Collectors.toSet());

                final List<User> users = new UserRepository(Database.connect()).findAllByIds(authorIds);

                authorsMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
            }

            final WorkOrderListDto workOrderListDto = WorkOrderListDto.toDto(orders, authorsMap);
            return new Gson().toJson(workOrderListDto);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new BaseException(ErrorMessages.DATABASE_ERROR, OperationEnum.LISTAR_ORDENS);
        } catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage(), OperationEnum.LISTAR_ORDENS);
        }
    }

    public String create(final WorkOrderCreatePayload payload) throws BaseException {
        try {
            final String token = payload.getToken();
            final String description = payload.getDescricao();

            if (token.isBlank()) {
                throw new InvalidTokenException(OperationEnum.CADASTRAR_ORDEM);
            }

            if (!StringUtils.isValidDescription(description)) {
                throw new InvalidDescriptionException(OperationEnum.CADASTRAR_ORDEM);
            }

            final User user = new UserRepository(Database.connect()).findOneByUsername(token);
            if (user == null) {
                throw new UserNotFoundException(OperationEnum.CADASTRAR_ORDEM);
            }

            final WorkOrder order = new WorkOrder(description, WorkOrderStatusEnum.PENDENTE, user.getId());

            new WorkOrderRepository(Database.connect()).create(order);

            final BaseResponseDto response = BaseResponseDto.toDto(
                ResponseStatusEnum.SUCESSO.getId(),
                OperationEnum.CADASTRAR_ORDEM,
                "Ordem cadastrada com sucesso"
            );

            return new Gson().toJson(response);
        } catch (SQLException e) {
            throw new BaseException(ErrorMessages.DATABASE_ERROR, OperationEnum.CADASTRAR_ORDEM);
        } catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage(), OperationEnum.CADASTRAR_ORDEM);
        }
    }

    public String update(final WorkOrderUpdatePayload payload) throws BaseException {
        try {
            final Integer orderId = payload.getId_ordem();
            final String newDescription = payload.getNova_descricao();
            final String token = payload.getToken();

            if (token.isBlank()) {
                throw new InvalidTokenException(OperationEnum.EDITAR_ORDEM);
            }

            if (!StringUtils.isValidDescription(newDescription)) {
                throw new InvalidDescriptionException(OperationEnum.EDITAR_ORDEM);
            }

            final User user = new UserRepository(Database.connect()).findOneByUsername(token);
            if (user == null) {
                throw new UserNotFoundException(OperationEnum.EDITAR_ORDEM);
            }

            final WorkOrder order = new WorkOrderRepository(Database.connect()).findOneById(orderId);
            if (order == null) {
                throw new WorkOrderNotFoundException(OperationEnum.EDITAR_ORDEM);
            }

            if (UserRoleEnum.COMUM.equals(user.getRole())) {
                if (!order.getCreatedById().equals(user.getId())) {
                    throw new NotWorkOrderOwner(OperationEnum.EDITAR_ORDEM);
                }
            }

            if (!WorkOrderStatusEnum.PENDENTE.equals(order.getStatus())) {
                throw new WorkOrderClosedOrCanceledException(OperationEnum.EDITAR_ORDEM);
            }

            new WorkOrderRepository(Database.connect()).update(order);

            final BaseResponseDto response = BaseResponseDto.toDto(
                ResponseStatusEnum.SUCESSO.getId(),
                OperationEnum.EDITAR_ORDEM,
                "Ordem editada com sucesso"
            );

            return new Gson().toJson(response);
        } catch (SQLException e) {
            throw new BaseException(ErrorMessages.DATABASE_ERROR, OperationEnum.EDITAR_ORDEM);
        } catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage(), OperationEnum.EDITAR_ORDEM);
        }
    }

    public String alter(final AlterWorkOrderPayload payload) throws BaseException {
        try {
            final Integer orderId = payload.getId_ordem();
            final String newDescription = payload.getNova_descricao();
            final String newStatusId = payload.getNovo_status();
            final String token = payload.getToken();

            if (token.isBlank()) {
                throw new InvalidTokenException(OperationEnum.ALTERAR_ORDEM);
            }

            if (newDescription != null &&
                !StringUtils.isValidDescription(newDescription)) {
                throw new InvalidDescriptionException(OperationEnum.ALTERAR_ORDEM);
            }

            if (newStatusId != null &&
                !WorkOrderStatusEnum.WORK_ORDER_STATUS_STRING().contains(newStatusId)) {
                throw new InvalidWorkOrderStatusException(OperationEnum.ALTERAR_ORDEM);
            }

            final User user = new UserRepository(Database.connect()).findOneByUsername(token);
            if (user == null) {
                throw new UserNotFoundException(OperationEnum.ALTERAR_ORDEM);
            }

            if (UserRoleEnum.COMUM.equals(user.getRole())) {
                throw new InvalidTokenException(OperationEnum.ALTERAR_ORDEM);
            }

            final WorkOrder order = new WorkOrderRepository(Database.connect()).findOneById(orderId);
            if (order == null) {
                throw new WorkOrderNotFoundException(OperationEnum.ALTERAR_ORDEM);
            }

            if (newDescription != null) {
                order.setDescription(newDescription);
            }
            if (newStatusId != null) {
                final WorkOrderStatusEnum newStatus = WorkOrderStatusEnum.getById(newStatusId);
                order.setStatus(newStatus);
            }

            new WorkOrderRepository(Database.connect()).update(order);

            final BaseResponseDto response = BaseResponseDto.toDto(
                ResponseStatusEnum.SUCESSO.getId(),
                OperationEnum.ALTERAR_ORDEM,
                "Ordem alterada com sucesso"
            );

            return new Gson().toJson(response);
        } catch (SQLException e) {
            throw new BaseException(ErrorMessages.DATABASE_ERROR, OperationEnum.ALTERAR_ORDEM);
        } catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage(), OperationEnum.ALTERAR_ORDEM);
        }
    }
}
