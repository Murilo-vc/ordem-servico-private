package org.example.murilo.ordemservico.service;

import com.google.gson.JsonSyntaxException;
import org.example.murilo.ordemservico.DatabaseSetup;
import org.example.murilo.ordemservico.domain.dto.ErrorDto;
import org.example.murilo.ordemservico.domain.payload.*;
import org.example.murilo.ordemservico.enumeration.OperationEnum;
import org.example.murilo.ordemservico.handler.exception.BaseException;
import org.example.murilo.ordemservico.handler.exception.InvalidOperationException;
import org.example.murilo.ordemservico.util.JsonUtils;

import java.sql.SQLException;

public class OperationService {

    private final UserService userService;

    private final WorkOrderService workOrderService;

    public OperationService() {
        this.userService = new UserService();
        this.workOrderService = new WorkOrderService();
    }

    public void checkTables() throws SQLException {
        DatabaseSetup.createTables();
    }

    public String processRequest(final String inputJson) throws SQLException {
        try {
            if (inputJson == null || inputJson.isBlank()) {
                throw new InvalidOperationException();
            }

            final OperationPayload payload = JsonUtils.parseOperationPayload(inputJson);
            final OperationEnum operation = OperationEnum.getById(payload.getOperacao());

            if (operation == null) {
                throw new InvalidOperationException();
            }

            String responseJson;
            if (OperationEnum.ALL_USERS_OPERATIONS_STRING().contains(operation.getId())) {
                responseJson = this.processUserOperation(operation, inputJson);
            } else {
                responseJson = this.processWorkOrderOperation(operation, inputJson);
            }

            return responseJson;
        } catch (BaseException e) {
            ErrorDto.toDto(e);
            final ErrorDto error = ErrorDto.toDto(e);
            return JsonUtils.getErrorJson(error);
        } catch (JsonSyntaxException e) {
            final BaseException exception = new BaseException("Error on parsing JSON", null);
            final ErrorDto error = ErrorDto.toDto(exception);
            return JsonUtils.getErrorJson(error);
        }
    }

    private String processUserOperation(final OperationEnum operation,
                                        final String inputJson) throws BaseException, SQLException {
        if (OperationEnum.LOGIN.equals(operation)) {
            final LoginPayload loginPayload = JsonUtils.parseLoginPayload(inputJson);
            return this.userService.login(loginPayload);
        } else if (OperationEnum.LOGOUT.equals(operation)) {
            final LogoutPayload logoutPayload = JsonUtils.parseLogoutPayload(inputJson);
            return this.userService.logout(logoutPayload);
        } else if (OperationEnum.CADASTRO.equals(operation)) {
            final SignUpPayload signUpPayload = JsonUtils.parseSignUpPayload(inputJson);
            return this.userService.createUser(signUpPayload);
        } else if (OperationEnum.LER_DADOS.equals(operation)) {
            final CheckProfilePayload checkProfilePayload = JsonUtils.parseCheckProfilePayload(inputJson);
            return this.userService.checkOwnProfile(checkProfilePayload);
        } else if (OperationEnum.EDITAR_USUARIO.equals(operation)) {
            final UpdateUserPayload updateUserPayload = JsonUtils.parseUpdateUserPayload(inputJson);
            return this.userService.updateUser(updateUserPayload);
        } else if (OperationEnum.EXCLUIR_USUARIO.equals(operation)) {
            final DeleteUserPayload deleteUserPayload = JsonUtils.parseDeleteUserPayload(inputJson);
            return this.userService.deleteUser(deleteUserPayload);
        } else if (OperationEnum.LISTAR_USUARIOS.equals(operation)) {
            final UserListPayload userListPayload = JsonUtils.parseUserListPayload(inputJson);
            return this.userService.findAllUsers(userListPayload);
        }

        throw new InvalidOperationException();
    }

    private String processWorkOrderOperation(final OperationEnum operation,
                                             final String inputJson) throws BaseException {
        return switch (operation) {
            case CADASTRAR_ORDEM -> {
                final WorkOrderCreatePayload payload = JsonUtils.parseCreateWorkOrderPayload(inputJson);
                yield this.workOrderService.create(payload);
            }
            case LISTAR_ORDENS -> {
                final WorkOrderListPayload payload = JsonUtils.parseWorkOrderListPayload(inputJson);
                yield this.workOrderService.findAll(payload);
            }
            case EDITAR_ORDEM -> {
                final WorkOrderUpdatePayload payload = JsonUtils.parseUpdateWorkOrderPayload(inputJson);
                yield this.workOrderService.update(payload);
            }
            case ALTERAR_ORDEM -> {
                final AlterWorkOrderPayload payload = JsonUtils.parseChangeWorkOrderStatusPayload(inputJson);
                yield this.workOrderService.alter(payload);
            }
            default -> throw new InvalidOperationException();
        };
    }
}
