package org.example.murilo.ordemservico.service;

import com.google.gson.JsonSyntaxException;
import org.example.murilo.ordemservico.DatabaseSetup;
import org.example.murilo.ordemservico.domain.dto.ErrorDto;
import org.example.murilo.ordemservico.domain.payload.*;
import org.example.murilo.ordemservico.enumeration.OperationEnum;
import org.example.murilo.ordemservico.handler.exception.BaseException;
import org.example.murilo.ordemservico.util.JsonUtils;

import java.sql.SQLException;

public class OperationService {

    private final UserService userService;

    public OperationService() {
        this.userService = new UserService();
    }

    public void checkTables() throws SQLException {
        DatabaseSetup.createTables();
        DatabaseSetup.insertInitialData();
    }

    public String processRequest(final String inputJson) throws SQLException {
        try {
            final OperationPayload payload = JsonUtils.parseOperationPayload(inputJson);
            final OperationEnum operation = OperationEnum.getById(payload.getOperacao());

            if (operation == null) {
                return "";
            }

            String responseJson = "";
            if (OperationEnum.ALL_USERS_OPERATIONS_STRING().contains(operation.getId())) {
                responseJson = this.processUserOperation(operation, inputJson);
            }

            return responseJson;
        } catch (BaseException e) {
            ErrorDto.toDto(e);
            final ErrorDto error = ErrorDto.toDto(e);
            return JsonUtils.getErrorJson(error);
        } catch (JsonSyntaxException e) {
            System.err.println("Error on parsing JSON");
            return "";
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

        return null;
    }
}
