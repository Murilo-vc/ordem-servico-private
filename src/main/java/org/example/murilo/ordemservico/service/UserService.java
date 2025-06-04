package org.example.murilo.ordemservico.service;

import com.google.gson.Gson;
import org.example.murilo.ordemservico.domain.dto.*;
import org.example.murilo.ordemservico.domain.entity.User;
import org.example.murilo.ordemservico.domain.payload.*;
import org.example.murilo.ordemservico.enumeration.OperationEnum;
import org.example.murilo.ordemservico.enumeration.UserRoleEnum;
import org.example.murilo.ordemservico.handler.exception.*;
import org.example.murilo.ordemservico.repository.Database;
import org.example.murilo.ordemservico.repository.UserRepository;
import org.example.murilo.ordemservico.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public static List<String> loggedUsernames = new ArrayList<>();

    public void createUserTable() throws SQLException {
        final Connection conn = Database.connect();
        new UserRepository(conn).createUserTable();
    }

    public String login(final LoginPayload payload)
        throws LoginInvalidFieldsException, UserAlreadyLoggedException, SQLException {
        final String username = payload.getUsuario();
        final String password = payload.getSenha();

        if (StringUtils.isEmpty(username) ||
            StringUtils.isEmpty(password)) {
            throw new LoginInvalidFieldsException();
        }

        final User user = new UserRepository(Database.connect()).findOneByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new LoginInvalidFieldsException();
        }

        final boolean isAlreadyLogged = loggedUsernames.contains(user.getUsername());
        if (isAlreadyLogged) {
            throw new UserAlreadyLoggedException();
        }

        loggedUsernames.add(user.getUsername());

        final LoginDto loginDto = LoginDto.toDto(user.getUsername(), user.getRole());

        return new Gson().toJson(loginDto);
    }

    public String logout(final LogoutPayload payload)
        throws InvalidTokenException, SQLException {
        final String token = payload.getToken();

        if (StringUtils.isEmpty(token)) {
            throw new InvalidTokenException(OperationEnum.LOGOUT);
        }

        final User user = new UserRepository(Database.connect()).findOneByUsername(token);
        if (user == null) {
            throw new InvalidTokenException(OperationEnum.LOGOUT);
        }

        loggedUsernames.remove(token);

        final LogoutDto logoutDto = LogoutDto.toDto();

        return new Gson().toJson(logoutDto);
    }

    public String createUser(final SignUpPayload payload)
        throws SignUpInvalidFieldsException, SQLException, UsernameAlreadyExistsException {
        final String name = payload.getNome();
        final String username = payload.getUsuario();
        final String password = payload.getSenha();
        final String roleId = payload.getPerfil();
        final String token = payload.getToken();

        if (!StringUtils.isValidName(name) ||
            !StringUtils.isValidUsername(username) ||
            !StringUtils.isValidPassword(password)) {
            throw new SignUpInvalidFieldsException();
        }

        final boolean usernameTaken = new UserRepository(Database.connect()).existsByUsername(username);
        if (usernameTaken) {
            throw new UsernameAlreadyExistsException(OperationEnum.CADASTRO);
        }

        if (token != null && !token.isBlank()) {
            //TODO make adm create user flow
        }

        final UserRoleEnum role = UserRoleEnum.COMUM;
        final User user = new User(username, name, password, role);

        new UserRepository(Database.connect()).create(user);

        final CreateUserDto createUserDto = CreateUserDto.toDto();

        return new Gson().toJson(createUserDto);
    }

    public String checkOwnProfile(final CheckProfilePayload payload)
        throws InvalidTokenException, SQLException {
        final String token = payload.getToken();

        if (StringUtils.isEmpty(token)) {
            throw new InvalidTokenException(OperationEnum.LER_DADOS);
        }

        final User user = new UserRepository(Database.connect()).findOneByUsername(token);
        if (user == null) {
            throw new InvalidTokenException(OperationEnum.LER_DADOS);
        }

        final CheckOwnProfileDto ownProfileDto = CheckOwnProfileDto.toDto(user);

        return new Gson().toJson(ownProfileDto);
    }

    public String updateUser(final UpdateUserPayload payload)
        throws InvalidTokenException, UpdateUserInvalidFieldsException, SQLException, UsernameAlreadyExistsException {
        final String token = payload.getToken();
        final String newUsername = payload.getNovo_usuario();
        final String newPassword = payload.getNova_senha();
        final String newName = payload.getNovo_nome();

        if (!StringUtils.isValidUsername(newUsername) ||
            !StringUtils.isValidPassword(newPassword) ||
            !StringUtils.isValidName(newName)) {
            throw new UpdateUserInvalidFieldsException();
        }

        if (StringUtils.isEmpty(token)) {
            throw new InvalidTokenException(OperationEnum.EDITAR_USUARIO);
        }

        final User user = new UserRepository(Database.connect()).findOneByUsername(token);
        final boolean isValidToken = user != null;
        if (!isValidToken) {
            throw new InvalidTokenException(OperationEnum.EDITAR_USUARIO);
        }

        final boolean usernameTaken = new UserRepository(Database.connect()).existsByUsername(newUsername);
        if (usernameTaken) {
            throw new UsernameAlreadyExistsException(OperationEnum.EDITAR_USUARIO);
        }

        final User updatedUser = new User(user.getId(), newUsername, newName, newPassword, user.getRole());

        final Connection conn = Database.connect();
        new UserRepository(conn).update(updatedUser);

        final String finalToken = token.equals(newUsername) ? token : newUsername;

        final UpdateUserDto updateUserDto = UpdateUserDto.toDto(finalToken);

        return new Gson().toJson(updateUserDto);
    }

    public String deleteUser(final DeleteUserPayload payload) throws InvalidTokenException, SQLException {
        final String token = payload.getToken();

        final User user = new UserRepository(Database.connect()).findOneByUsername(token);
        final boolean isValidToken = user != null;
        if (!isValidToken) {
            throw new InvalidTokenException(OperationEnum.EXCLUIR_USUARIO);
        }

        new UserRepository(Database.connect()).delete(user.getId());

        final DeleteUserDto deleteUserDto = DeleteUserDto.toDto();

        return new Gson().toJson(deleteUserDto);
    }
}
