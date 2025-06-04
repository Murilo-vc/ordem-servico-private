package org.example.murilo.ordemservico;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class Server {

    private ServerSocket serverSocket;

    public Server(final ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        System.out.println("Qual porta o servidor deve usar? ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int porta = Integer.parseInt(br.readLine());

        System.out.println("Servidor carregado na porta " + porta);
        System.out.println("Aguardando conexao.... ");

        try {
            serverSocket = new ServerSocket(porta);
            System.out.println("Criado Socket de Conexao.");
            try {
                while (true) {
                    new ServerThread(serverSocket.accept());
                    System.out.println("Accept ativado. Esperando por uma conexao...");
                }
            } catch (IOException e) {
                System.err.println("Accept falhou!");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Nao foi possivel ouvir a porta " + porta);
            System.exit(1);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("Nao foi possivel fechar a porta " + porta);
                    System.exit(1);
                }
            }
        }

    }
}
