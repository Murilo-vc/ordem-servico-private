package org.example.murilo.ordemservico;

import org.example.murilo.ordemservico.service.OperationService;
import org.example.murilo.ordemservico.util.StringUtils;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ServerThread implements Runnable {

    private final Socket socket;
    private final OperationService operationService;

    public ServerThread(final Socket socket) {
        this.socket = socket;
        this.operationService = new OperationService();
        run();
    }

    @Override
    public void run() {
        try {
            System.out.println("Nova thread de comunicacao iniciada.\n");
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            this.operationService.checkTables();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Servidor recebeu: " + inputLine);

                if (StringUtils.isEmpty(inputLine)) {
                    out.println("");
                    continue;
                }

                final String outputJson = this.operationService.processRequest(inputLine);

                System.out.println("Servidor enviou: " + outputJson);
                out.println(outputJson);
            }

            out.close();
            in.close();
            this.socket.close();

        } catch (IOException e) {
            System.err.println("Problema com Servidor de Communicacao!");
            Thread.currentThread().interrupt();
        } catch (SQLException e) {
            System.err.println("Problema com o Banco de Dados!");
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }

    }
}
