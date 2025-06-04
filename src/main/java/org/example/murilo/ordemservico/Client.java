package org.example.murilo.ordemservico;

import com.google.gson.Gson;
import org.example.murilo.ordemservico.domain.dto.*;
import org.example.murilo.ordemservico.domain.payload.*;
import org.example.murilo.ordemservico.enumeration.ResponseStatusEnum;
import org.example.murilo.ordemservico.enumeration.UserRoleEnum;
import org.example.murilo.ordemservico.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) throws IOException {
        System.out.println("Qual o IP do servidor? ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String serverIP = br.readLine();

        System.out.println("Qual a Porta do servidor? ");
        br = new BufferedReader(new InputStreamReader(System.in));
        int serverPort = Integer.parseInt(br.readLine());

        System.out.println("Tentando conectar com host " + serverIP + " na porta " + serverPort);

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(serverIP, serverPort);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Host " + serverIP + " nao encontrado!");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Não foi possivel reservar I/O para conectar com " + serverIP);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput = null;

        System.out.println("Conectado.");
        String token = null;
        UserRoleEnum role = null;
        do {
            if (token == null) {
                System.out.print(
                    """
                    O que deseja fazer?
                    \t1 - Logar
                    \t2 - Cadastrar-se
                    \t0 - Sair do aplicativo
                    """);

                int option = -1;
                do {
                    try {
                        userInput = stdIn.readLine();
                        option = switch (Integer.parseInt(userInput)) {
                            case 1 -> 1;
                            case 2 -> 2;
                            case 0 -> 0;
                            default -> {
                                System.out.println("Escolha uma opção válida!");
                                yield -1;
                            }
                        };
                    } catch (NumberFormatException e) {
                        System.out.println("Escolha uma opção válida!");
                    }
                } while (option == -1);

                if (option == 1) {
                    System.out.println("Para logar informe seus dados:\n\tNome de usuario:  ");
                    String username;
                    do {
                        username = stdIn.readLine();
                        if (username.isBlank()) {
                            System.out.println("Campo não deve ser deixado em branco!");
                        }
                    } while (username.isBlank());
                    System.out.print("\tSenha:  ");
                    String password;
                    do {
                        password = stdIn.readLine();
                        if (password.isBlank()) {
                            System.out.println("Campo não deve ser deixado em branco!");
                        }
                    } while (password.isBlank());

                    final LoginPayload loginPayload = new LoginPayload(username, password);

                    final String payloadJson = new Gson().toJson(loginPayload);
                    System.out.println("Cliente enviou: " + payloadJson);
                    out.println(payloadJson);

                    final String responseJson = in.readLine();
                    System.out.println("Servidor retornou: " + responseJson);

                    if (StringUtils.isEmpty(responseJson)) {
                        System.out.println("Um erro ocorreu durante a operação. Tente novamente!");
                        continue;
                    }

                    final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
                    final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

                    if (ResponseStatusEnum.SUCESSO.equals(status)) {
                        final LoginDto loginDto = new Gson().fromJson(responseJson, LoginDto.class);
                        token = loginDto.getToken();
                        role = UserRoleEnum.getById(loginDto.getPerfil());

                        if (StringUtils.isEmpty(token) || role == null) {
                            System.out.println("Um erro ocorreu durante a operação. Tente novamente!");
                            token = null;
                            role = null;
                            continue;
                        }

                        System.out.println("Login realizado com sucesso!");
                        continue;
                    } else {
                        final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                        final String message = error.getMensagem();

                        if (StringUtils.isEmpty(message)) {
                            System.out.println("Um erro não mapeado ocorreu durante a operação.!");
                        }

                        System.out.println(message);
                        continue;
                    }

                } else if (option == 2) {
                    System.out.println("Para cadastrar-se informe seus dados:");
                    String name;
                    do {
                        System.out.print("\tNome:  ");
                        name = stdIn.readLine();
                        if (StringUtils.isEmpty(name)) {
                            System.out.println("Campo não deve ser vazio!");
                            name = "";
                            continue;
                        }
                        if (name.length() < 3 || name.length() > 30){
                            System.out.println("Campo deve ter entre 3 e 30 caracteres!");
                            name = "";
                            continue;
                        }
                        if (name.contains(" ")) {
                            System.out.println("Campo não deve possuir espaço vazio!");
                            name = "";
                            continue;
                        }
                        if (StringUtils.containsSpecialCharacter(name)) {
                            System.out.println("Campo não deve possuir caracteres especiais!");
                            name = "";
                        }
                    } while (name.isBlank());
                    String username;
                    do {
                        System.out.print("\tNome de Usuário:  ");
                        username = stdIn.readLine();
                        if (StringUtils.isEmpty(username)) {
                            System.out.println("Campo não deve ser vazio!");
                            username = "";
                            continue;
                        }
                        if (username.length() < 3 || username.length() > 30){
                            System.out.println("Campo deve ter entre 3 e 30 caracteres!");
                            username = "";
                            continue;
                        }
                        if (username.contains(" ")) {
                            System.out.println("Campo não deve possuir espaço vazio!");
                            username = "";
                            continue;
                        }
                        if (StringUtils.containsSpecialCharacter(username)) {
                            System.out.println("Campo não deve possuir caracteres especiais!");
                            username = "";
                        }
                    } while (username.isBlank());
                    String password;
                    do {
                        System.out.print("\tSenha:  ");
                        password = stdIn.readLine();
                        if (StringUtils.isEmpty(password)) {
                            System.out.println("Campo não deve ser vazio!");
                            password = "";
                            continue;
                        }
                        if (password.length() < 4 || password.length() > 10){
                            System.out.println("Campo deve ter entre 4 e 10 caracteres!");
                            password = "";
                            continue;
                        }
                        if (password.contains(" ")) {
                            System.out.println("Campo não deve possuir espaço vazio!");
                            password = "";
                            continue;
                        }
                        if (StringUtils.containsSpecialCharacter(password)) {
                            System.out.println("Campo não deve possuir caracteres especiais!");
                            password = "";
                        }
                    } while (password.isBlank());

                    final SignUpPayload signUpPayload = new SignUpPayload(
                        name, username, password, UserRoleEnum.COMUM.getId(), null
                    );

                    final String payloadJson = new Gson().toJson(signUpPayload);
                    System.out.println("Cliente enviou: " + payloadJson);
                    out.println(payloadJson);

                    final String responseJson = in.readLine();
                    System.out.println("Servidor retornou: " + responseJson);

                    if (StringUtils.isEmpty(responseJson)) {
                        System.out.println("Um erro ocorreu durante a operação. Tente novamente!");
                        continue;
                    }

                    final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
                    final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

                    if (ResponseStatusEnum.SUCESSO.equals(status)) {
                        System.out.println("Cadastro concluido com sucesso!");
                        continue;
                    } else {
                        final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                        final String message = error.getMensagem();

                        if (StringUtils.isEmpty(message)) {
                            System.out.println("Um erro não mapeado ocorreu durante a operação.!");
                        }

                        System.out.println(message);
                        continue;
                    }

                } else {
                    break;
                }
            }

            System.out.print(
                """
                Bem Vindo!
                O que deseja fazer?
                \t1 - Ver Informações Pessoais
                \t2 - Atualizar Dados Pessoais
                \t9 - Excluir Conta
                \t0 - Logout
                """);

            int option = -1;
            do {
                try {
                    userInput = stdIn.readLine();
                    option = switch (Integer.parseInt(userInput)) {
                        case 1 -> 1;
                        case 2 -> 2;
                        case 9 -> 9;
                        case 0 -> 0;
                        default -> {
                            System.out.println("Escolha uma opção válida!");
                            yield -1;
                        }
                    };
                } catch (NumberFormatException e) {
                    System.out.println("Escolha uma opção válida!");
                }
            } while (option == -1);

            if (option == 1) {
                final CheckProfilePayload checkProfilePayload = new CheckProfilePayload(token);

                final String payloadJson = new Gson().toJson(checkProfilePayload);
                System.out.println("Cliente enviou: " + payloadJson);
                out.println(payloadJson);

                final String responseJson = in.readLine();
                System.out.println("Servidor retornou: " + responseJson);

                if (StringUtils.isEmpty(responseJson)) {
                    System.out.println("Um erro ocorreu durante a operação. Tente novamente!");
                    continue;
                }

                final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
                final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

                if (ResponseStatusEnum.SUCESSO.equals(status)) {
                    final CheckOwnProfileDto ownProfileDto = new Gson().fromJson(responseJson, CheckOwnProfileDto.class);
                    final ProfileDto profile = ownProfileDto.getDados();

                    if (profile == null) {
                        System.out.println("Um erro ocorreu durante a operação. Tente novamente!");
                        continue;
                    }

                    final String name = profile.getNome();
                    final String username = profile.getUsuario();
                    final String password = profile.getSenha();

                    if (StringUtils.isEmpty(name) ||
                        StringUtils.isEmpty(username) ||
                        StringUtils.isEmpty(password)) {
                        System.out.println("Um erro ocorreu durante a operação. Tente novamente!");
                        continue;
                    }

                    System.out.println("Seu perfil:");
                    System.out.println("\tNome:  " + name);
                    System.out.println("\tNome de Usuario:  " + username);
                    System.out.println("\tSenha:  " + password);
                    continue;
                } else {
                    final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                    final String message = error.getMensagem();

                    if (StringUtils.isEmpty(message)) {
                        System.out.println("Um erro não mapeado ocorreu durante a operação.!");
                    }

                    System.out.println(message);
                    continue;
                }
            } else if (option == 2) {
                String newName;
                do {
                    System.out.print("\tNovo Nome:  ");
                    newName = stdIn.readLine();
                    if (StringUtils.isEmpty(newName)) {
                        System.out.println("Campo não deve ser vazio!");
                        newName = "";
                        continue;
                    }
                    if (newName.length() < 3 || newName.length() > 30){
                        System.out.println("Campo deve ter entre 3 e 30 caracteres!");
                        newName = "";
                        continue;
                    }
                    if (newName.contains(" ")) {
                        System.out.println("Campo não deve possuir espaço vazio!");
                        newName = "";
                        continue;
                    }
                    if (StringUtils.containsSpecialCharacter(newName)) {
                        System.out.println("Campo não deve possuir caracteres especiais!");
                        newName = "";
                    }
                } while (newName.isBlank());
                String newUsername;
                do {
                    System.out.print("\tNovo Nome de Usuário:  ");
                    newUsername = stdIn.readLine();
                    if (StringUtils.isEmpty(newUsername)) {
                        System.out.println("Campo não deve ser vazio!");
                        newUsername = "";
                        continue;
                    }
                    if (newUsername.length() < 3 || newUsername.length() > 30){
                        System.out.println("Campo deve ter entre 3 e 30 caracteres!");
                        newUsername = "";
                        continue;
                    }
                    if (newUsername.contains(" ")) {
                        System.out.println("Campo não deve possuir espaço vazio!");
                        newUsername = "";
                        continue;
                    }
                    if (StringUtils.containsSpecialCharacter(newUsername)) {
                        System.out.println("Campo não deve possuir caracteres especiais!");
                        newUsername = "";
                    }
                } while (newUsername.isBlank());
                String newPassword;
                do {
                    System.out.print("\tSenha:  ");
                    newPassword = stdIn.readLine();
                    if (StringUtils.isEmpty(newPassword)) {
                        System.out.println("Campo não deve ser vazio!");
                        newPassword = "";
                        continue;
                    }
                    if (newPassword.length() < 4 || newPassword.length() > 10){
                        System.out.println("Campo deve ter entre 4 e 10 caracteres!");
                        newPassword = "";
                        continue;
                    }
                    if (newPassword.contains(" ")) {
                        System.out.println("Campo não deve possuir espaço vazio!");
                        newPassword = "";
                        continue;
                    }
                    if (StringUtils.containsSpecialCharacter(newPassword)) {
                        System.out.println("Campo não deve possuir caracteres especiais!");
                        newPassword = "";
                    }
                } while (newPassword.isBlank());

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
                    System.out.println("Um erro ocorreu durante a operação. Tente novamente!");
                    continue;
                }

                final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
                final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

                if (ResponseStatusEnum.SUCESSO.equals(status)) {
                    final UpdateUserDto updateUserDto = new Gson().fromJson(responseJson, UpdateUserDto.class);
                    final String newToken = updateUserDto.getToken();

                    if (StringUtils.isEmpty(newToken)) {
                        System.out.println("Um erro ocorreu durante a operação. Tente novamente!");
                        continue;
                    }

                    System.out.println("Dados atualizados com sucesso");
                    token = newToken;
                    continue;
                } else {
                    final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                    final String message = error.getMensagem();

                    if (StringUtils.isEmpty(message)) {
                        System.out.println("Um erro não mapeado ocorreu durante a operação.");
                    }

                    System.out.println(message);
                    continue;
                }
            } else if (option == 9) {
                System.out.println("""
                    Deseja mesmo excluir sua conta?
                    \t1 - Sim
                    \t3 - Nao
                    """);
                int confirm = -1;
                do {
                    try {
                        userInput = stdIn.readLine();
                        confirm = switch (Integer.parseInt(userInput)) {
                            case 1 -> 1;
                            case 3 -> 3;
                            default -> {
                                System.out.println("Escolha uma opção válida!");
                                yield -1;
                            }
                        };
                    } catch (NumberFormatException e) {
                        System.out.println("Escolha uma opção válida!");
                    }
                } while (confirm == -1);

                if (confirm == 3) {
                    System.out.println("Operaçao cancelada!");
                    continue;
                }

                final DeleteUserPayload deleteUserPayload = new DeleteUserPayload(token);

                final String payloadJson = new Gson().toJson(deleteUserPayload);
                System.out.println("Cliente enviou: " + payloadJson);
                out.println(payloadJson);

                final String responseJson = in.readLine();
                System.out.println("Servidor retornou: " + responseJson);

                if (StringUtils.isEmpty(responseJson)) {
                    System.out.println("Um erro ocorreu durante a operação. Tente novamente!");
                    continue;
                }

                final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
                final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

                if (ResponseStatusEnum.SUCESSO.equals(status)) {
                    System.out.println("Conta excluída com sucesso");
                    token = null;
                    role = null;
                    continue;
                } else {
                    final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                    final String message = error.getMensagem();

                    if (StringUtils.isEmpty(message)) {
                        System.out.println("Um erro não mapeado ocorreu durante a operação.!");
                    }

                    System.out.println(message);
                    continue;
                }
            } else {
                final LogoutPayload logoutPayload = new LogoutPayload(token);

                final String payloadJson = new Gson().toJson(logoutPayload);
                System.out.println("Cliente enviou: " + payloadJson);
                out.println(payloadJson);

                final String responseJson = in.readLine();
                System.out.println("Servidor retornou: " + responseJson);

                if (StringUtils.isEmpty(responseJson)) {
                    System.out.println("Um erro ocorreu durante a operação. Tente novamente!");
                    continue;
                }

                final BaseResponseDto baseResponse = new Gson().fromJson(responseJson, BaseResponseDto.class);
                final ResponseStatusEnum status = ResponseStatusEnum.getById(baseResponse.getStatus());

                if (ResponseStatusEnum.SUCESSO.equals(status)) {
                    System.out.println("Logout realizado com sucesso");
                    token = null;
                    role = null;
                    continue;
                } else {
                    final ErrorDto error = new Gson().fromJson(responseJson, ErrorDto.class);
                    final String message = error.getMensagem();

                    if (StringUtils.isEmpty(message)) {
                        System.out.println("Um erro não mapeado ocorreu durante a operação.!");
                    }

                    System.out.println(message);
                }
            }
        } while (userInput != null);

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}
