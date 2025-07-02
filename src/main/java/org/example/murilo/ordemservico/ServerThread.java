package org.example.murilo.ordemservico;

import org.example.murilo.ordemservico.domain.dto.ClientDto;
import org.example.murilo.ordemservico.service.OperationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ServerThread implements Runnable {

    private final Socket socket;
    private final List<ClientDto> connectedClients;
    private final OperationService operationService;

    public ServerThread(final Socket socket, final List<ClientDto> connectedClients) {
        this.socket = socket;
        this.connectedClients = connectedClients;
        this.operationService = new OperationService();
    }

    @Override
    public void run() {
        final String clientIp = socket.getInetAddress().getHostAddress();
        final int clientPort = socket.getPort();
        final ClientDto client = new ClientDto(clientIp, clientPort);
        connectedClients.add(client);
        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))
        ) {
            System.out.println("Nova thread de comunicacao iniciada.\n");

            this.operationService.checkTables();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Servidor recebeu: " + inputLine);

                final String outputJson = this.operationService.processRequest(inputLine);

                System.out.println("Servidor enviou: " + outputJson);
                out.println(outputJson);
            }
        } catch (IOException e) {
            System.err.println("Problema com Servidor de Communicacao!");
            Thread.currentThread().interrupt();
        } catch (SQLException e) {
            System.err.println("Problema com o Banco de Dados!");
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            connectedClients.remove(client);
            try {
                this.socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
