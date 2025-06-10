package org.example.murilo.ordemservico.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.example.murilo.ordemservico.domain.dto.*;
import org.example.murilo.ordemservico.domain.payload.*;
import org.example.murilo.ordemservico.enumeration.OperationEnum;
import org.example.murilo.ordemservico.enumeration.ResponseStatusEnum;
import org.example.murilo.ordemservico.enumeration.UserRoleEnum;
import org.example.murilo.ordemservico.handler.exception.BaseException;
import org.example.murilo.ordemservico.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class ClientService {

    private Socket echoSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientService() {
        this.echoSocket = null;
        this.out = null;
        this.in = null;
    }

    public void connectServer(final String ip, final int port) throws UnknownHostException, IOException {
        echoSocket = new Socket(ip, port);
        out = new PrintWriter(echoSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
    }

    public void signUp(final String name, final String username, final String password) throws BaseException {
        try {
            if (!StringUtils.isValidName(name) || !StringUtils.isValidUsername(username) || !StringUtils.isValidPassword(password)) {
                return;
            }

            final SignUpPayload signUpPayload = new SignUpPayload(
                name, username, password, UserRoleEnum.COMUM.getId(), null
            );

            final String payloadJson = new Gson().toJson(signUpPayload);
            System.out.println("Cliente enviou: " + payloadJson);
            out.println(payloadJson);

            final String responseJson = in.readLine();
            System.out.println("Servidor retornou: " + responseJson);

            if (StringUtils.isEmpty(responseJson)) {
                throw new BaseException("Servidor enviou resposta vazia", OperationEnum.CADASTRO);
            }

            final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
            final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

            if (ResponseStatusEnum.SUCESSO.equals(status)) {
                System.out.println("Cadastro concluido com sucesso!");
            } else {
                final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                String message = error.getMensagem();

                if (StringUtils.isEmpty(message)) {
                    message = "Um erro não mapeado ocorreu durante a operação!";
                }

                throw new BaseException(message, OperationEnum.CADASTRO);
            }
            return;
        } catch (JsonSyntaxException e) {
            throw new BaseException("Resposta não pode ser convertida para JSON", OperationEnum.CADASTRO);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), OperationEnum.CADASTRO);
        }
    }

    public void createUser(final String token, final String name, final String username, final String password, final UserRoleEnum role) throws BaseException {
        try {
            if (!StringUtils.isValidName(name) || !StringUtils.isValidUsername(username) || !StringUtils.isValidPassword(password) || role == null) {
                return;
            }

            final SignUpPayload signUpPayload = new SignUpPayload(
                name, username, password, role.getId(), token
            );

            final String payloadJson = new Gson().toJson(signUpPayload);
            System.out.println("Cliente enviou: " + payloadJson);
            out.println(payloadJson);

            final String responseJson = in.readLine();
            System.out.println("Servidor retornou: " + responseJson);

            if (StringUtils.isEmpty(responseJson)) {
                throw new BaseException("Servidor enviou resposta vazia", OperationEnum.CADASTRO);
            }

            final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
            final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

            if (ResponseStatusEnum.SUCESSO.equals(status)) {
                System.out.println("Usuario criado com sucesso!");
            } else {
                final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                String message = error.getMensagem();

                if (StringUtils.isEmpty(message)) {
                    message = "Um erro não mapeado ocorreu durante a operação!";
                }

                throw new BaseException(message, OperationEnum.CADASTRO);
            }
            return;
        } catch (JsonSyntaxException e) {
            throw new BaseException("Resposta não pode ser convertida para JSON", OperationEnum.CADASTRO);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), OperationEnum.CADASTRO);
        }
    }

    public LoginDto login(final String username, final String password) throws BaseException {
        try {
            final LoginPayload loginPayload = new LoginPayload(username, password);

            final String payloadJson = new Gson().toJson(loginPayload);
            System.out.println("Cliente enviou: " + payloadJson);
            out.println(payloadJson);

            final String responseJson = in.readLine();
            System.out.println("Servidor retornou: " + responseJson);

            if (StringUtils.isEmpty(responseJson)) {
                throw new BaseException("Servidor retornou resposta vazia!", OperationEnum.LOGIN);
            }

            final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
            final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

            if (ResponseStatusEnum.SUCESSO.equals(status)) {
                final LoginDto loginDto = new Gson().fromJson(responseJson, LoginDto.class);
                final String token = loginDto.getToken();
                final UserRoleEnum role = UserRoleEnum.getById(loginDto.getPerfil());

                if (StringUtils.isEmpty(token) || role == null) {
                    throw new BaseException("Um erro ocorreu durante a operação. Tente novamente!", OperationEnum.LOGIN);
                }

                System.out.println("Login realizado com sucesso!");
                return loginDto;
            } else {
                final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                String message = error.getMensagem();

                if (StringUtils.isEmpty(message)) {
                    message = "Um erro não mapeado ocorreu durante a operação!";
                }

                throw new BaseException(message, OperationEnum.LOGIN);
            }
        } catch (JsonSyntaxException e) {
            throw new BaseException("Resposta não pode ser convertida para JSON", OperationEnum.LOGIN);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), OperationEnum.LOGIN);
        }
    }

    public void logout(final String token) throws BaseException {
        try {
            final LogoutPayload logoutPayload = new LogoutPayload(token);

            final String payloadJson = new Gson().toJson(logoutPayload);
            System.out.println("Cliente enviou: " + payloadJson);
            out.println(payloadJson);

            final String responseJson = in.readLine();
            System.out.println("Servidor retornou: " + responseJson);

            if (StringUtils.isEmpty(responseJson)) {
                throw new BaseException("Servidor enviou resposta vazia!", OperationEnum.LOGOUT);
            }

            final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
            final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

            if (ResponseStatusEnum.ERRO.equals(status)) {
                final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                String message = error.getMensagem();

                if (StringUtils.isEmpty(message)) {
                    message = "Um erro não mapeado ocorreu durante a operação!";
                }

                throw new BaseException(message, OperationEnum.LOGOUT);
            }
        } catch (JsonSyntaxException e) {
            throw new BaseException("Resposta não pode ser convertida para JSON", OperationEnum.LOGOUT);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), OperationEnum.LOGOUT);
        }
    }

    public List<SmallUserDto> findAllUsers(final String token) throws BaseException {
        try {
            final UserListPayload payload = new UserListPayload(token);

            final String payloadJson = new Gson().toJson(payload);
            System.out.println("Cliente enviou: " + payloadJson);
            out.println(payloadJson);

            final String responseJson = in.readLine();
            System.out.println("Servidor retornou: " + responseJson);

            if (StringUtils.isEmpty(responseJson)) {
                throw new BaseException("Servidor enviou resposta vazia!", OperationEnum.LISTAR_USUARIOS);
            }

            final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
            final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

            if (ResponseStatusEnum.ERRO.equals(status)) {
                final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                String message = error.getMensagem();

                if (StringUtils.isEmpty(message)) {
                    message = "Um erro não mapeado ocorreu durante a operação!";
                }

                throw new BaseException(message, OperationEnum.LISTAR_USUARIOS);
            }

            final UserListDto userListDto = new Gson().fromJson(responseJson, UserListDto.class);
            return userListDto.getUsuarios();
        } catch (JsonSyntaxException e) {
            throw new BaseException("Resposta não pode ser convertida para JSON", OperationEnum.LISTAR_USUARIOS);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), OperationEnum.LISTAR_USUARIOS);
        }
    }

    public CheckOwnProfileDto checkProfile(final String token) throws BaseException {
        try {
            final CheckProfilePayload checkProfilePayload = new CheckProfilePayload(token);

            final String payloadJson = new Gson().toJson(checkProfilePayload);
            System.out.println("Cliente enviou: " + payloadJson);
            out.println(payloadJson);

            final String responseJson = in.readLine();
            System.out.println("Servidor retornou: " + responseJson);

            if (StringUtils.isEmpty(responseJson)) {
                throw new BaseException("Servidor enviou resposta vazia!", OperationEnum.LER_DADOS);
            }

            final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
            final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

            if (ResponseStatusEnum.SUCESSO.equals(status)) {
                final CheckOwnProfileDto ownProfileDto = new Gson().fromJson(responseJson, CheckOwnProfileDto.class);
                final ProfileDto profile = ownProfileDto.getDados();

                if (profile == null) {
                    throw new BaseException("Um erro ocorreu durante a operação. Tente novamente!", OperationEnum.LER_DADOS);
                }

                final String name = profile.getNome();
                final String username = profile.getUsuario();
                final String password = profile.getSenha();

                if (StringUtils.isEmpty(name) ||
                    StringUtils.isEmpty(username) ||
                    StringUtils.isEmpty(password)) {
                    throw new BaseException("Um erro ocorreu durante a operação. Tente novamente!", OperationEnum.LER_DADOS);
                }

                return ownProfileDto;
            } else {
                final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                String message = error.getMensagem();

                if (StringUtils.isEmpty(message)) {
                    message = "Um erro não mapeado ocorreu durante a operação!";
                }

                throw new BaseException(message, OperationEnum.LER_DADOS);
            }
        } catch (JsonSyntaxException e) {
            throw new BaseException("Resposta não pode ser convertida para JSON", OperationEnum.LER_DADOS);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), OperationEnum.LER_DADOS);
        }
    }

    public UpdateUserDto updateSelf(final String token, final String newUsername, final String newName, final String newPassword) throws BaseException {
        try {
            final UpdateUserPayload updateUserPayload = new UpdateUserPayload(
                token,
                newUsername,
                newName,
                newPassword
            );

            final String payloadJson = new Gson().toJson(updateUserPayload);
            System.out.println("Cliente enviou: " + payloadJson);
            out.println(payloadJson);

            final String responseJson = in.readLine();
            System.out.println("Servidor retornou: " + responseJson);

            if (StringUtils.isEmpty(responseJson)) {
                throw new BaseException("Servidor enviou resposta vazia!", OperationEnum.EDITAR_USUARIO);
            }

            final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
            final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

            if (ResponseStatusEnum.SUCESSO.equals(status)) {
                final UpdateUserDto updateUserDto = new Gson().fromJson(responseJson, UpdateUserDto.class);
                final String newToken = updateUserDto.getToken();

                if (StringUtils.isEmpty(newToken)) {
                    throw new BaseException("Um erro ocorreu durante a operação. Tente novamente!", OperationEnum.EDITAR_USUARIO);
                }

                return updateUserDto;
            } else {
                final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                String message = error.getMensagem();

                if (StringUtils.isEmpty(message)) {
                    message = "Um erro não mapeado ocorreu durante a operação.";
                }

                throw new BaseException(message, OperationEnum.EDITAR_USUARIO);
            }
        } catch (JsonSyntaxException e) {
            throw new BaseException("Resposta não pode ser convertida para JSON", OperationEnum.EDITAR_USUARIO);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), OperationEnum.EDITAR_USUARIO);
        }
    }

    public void updateUser(final String token, final String targetUser, final String newName, final String newPassword, final UserRoleEnum newRole) throws BaseException {
        try {
            final UpdateUserPayload updateUserPayload = new UpdateUserPayload(
                token,
                targetUser,
                newName,
                newPassword,
                newRole.getId()
            );

            final String payloadJson = new Gson().toJson(updateUserPayload);
            System.out.println("Cliente enviou: " + payloadJson);
            out.println(payloadJson);

            final String responseJson = in.readLine();
            System.out.println("Servidor retornou: " + responseJson);

            if (StringUtils.isEmpty(responseJson)) {
                throw new BaseException("Servidor enviou resposta vazia!", OperationEnum.EDITAR_USUARIO);
            }

            final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
            final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

            if (ResponseStatusEnum.ERRO.equals(status)) {
                final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                String message = error.getMensagem();

                if (StringUtils.isEmpty(message)) {
                    message = "Um erro não mapeado ocorreu durante a operação.";
                }

                throw new BaseException(message, OperationEnum.EDITAR_USUARIO);
            }
        } catch (JsonSyntaxException e) {
            throw new BaseException("Resposta não pode ser convertida para JSON", OperationEnum.EDITAR_USUARIO);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), OperationEnum.EDITAR_USUARIO);
        }
    }

    public void deleteUser(final String token) throws BaseException {
        try {
            final DeleteUserPayload deleteUserPayload = new DeleteUserPayload(token);

            final String payloadJson = new Gson().toJson(deleteUserPayload);
            System.out.println("Cliente enviou: " + payloadJson);
            out.println(payloadJson);

            final String responseJson = in.readLine();
            System.out.println("Servidor retornou: " + responseJson);

            if (StringUtils.isEmpty(responseJson)) {
                throw new BaseException("Um erro ocorreu durante a operação. Tente novamente!", OperationEnum.EXCLUIR_USUARIO);
            }

            final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
            final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

            if (ResponseStatusEnum.ERRO.equals(status)) {
                final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                String message = error.getMensagem();

                if (StringUtils.isEmpty(message)) {
                    message = "Um erro não mapeado ocorreu durante a operação!";
                }

                System.out.println(message);
                throw new BaseException(message, OperationEnum.EXCLUIR_USUARIO);
            }
        } catch (JsonSyntaxException e) {
            throw new BaseException("Resposta não pode ser convertida para JSON", OperationEnum.EXCLUIR_USUARIO);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), OperationEnum.EXCLUIR_USUARIO);
        }
    }

    public void deleteUser(final String token, final String targetUser) throws BaseException {
        try {
            final DeleteUserPayload deleteUserPayload = new DeleteUserPayload(token, targetUser);

            final String payloadJson = new Gson().toJson(deleteUserPayload);
            System.out.println("Cliente enviou: " + payloadJson);
            out.println(payloadJson);

            final String responseJson = in.readLine();
            System.out.println("Servidor retornou: " + responseJson);

            if (StringUtils.isEmpty(responseJson)) {
                throw new BaseException("Um erro ocorreu durante a operação. Tente novamente!", OperationEnum.EXCLUIR_USUARIO);
            }

            final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
            final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

            if (ResponseStatusEnum.ERRO.equals(status)) {
                final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                String message = error.getMensagem();

                if (StringUtils.isEmpty(message)) {
                    message = "Um erro não mapeado ocorreu durante a operação!";
                }

                System.out.println(message);
                throw new BaseException(message, OperationEnum.EXCLUIR_USUARIO);
            }
        } catch (JsonSyntaxException e) {
            throw new BaseException("Resposta não pode ser convertida para JSON", OperationEnum.EXCLUIR_USUARIO);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), OperationEnum.EXCLUIR_USUARIO);
        }
    }
}
