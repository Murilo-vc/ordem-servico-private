package org.example.murilo.ordemservico.service;

import org.example.murilo.ordemservico.ServerThread;
import org.example.murilo.ordemservico.ServerWindow;
import org.example.murilo.ordemservico.domain.dto.ClientDto;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerService {

    private final ServerWindow parent;
    private final List<ClientDto> connectedClients = Collections.synchronizedList(new ArrayList<>());
    private ServerSocket serverSocket;

    public ServerService(final ServerWindow parent) {
        this.parent = parent;
        this.serverSocket = null;
    }

    public void startServer(final int port) {
        System.out.println("Servidor carregado na porta " + port);
        System.out.println("Aguardando conexao.... ");

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    parent.updateClientList(connectedClients);
                } catch (InterruptedException e) {
                    System.err.println("Erro ao atualizar lista de usuarios conectados!");
                    break;
                }
            }
        }).start();

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Criado Socket de Conexao.");
            try {
                while (true) {
                    new Thread(new ServerThread(serverSocket.accept(), this.connectedClients)).start();
                    System.out.println("Accept ativado. Esperando por uma conexao...");
                }
            } catch (IOException e) {
                System.err.println("Accept falhou!");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Nao foi possivel ouvir a porta " + port);
            System.exit(1);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("Nao foi possivel fechar a porta " + port);
                    System.exit(1);
                }
            }
        }
    }

}
