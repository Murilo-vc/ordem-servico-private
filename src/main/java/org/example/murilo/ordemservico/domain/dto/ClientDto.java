package org.example.murilo.ordemservico.domain.dto;

public class ClientDto {
    private final String ip;
    private final int port;

    public ClientDto(final String ip,
                     final int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
