package org.example.murilo.ordemservico;

import org.example.murilo.ordemservico.repository.Database;
import org.example.murilo.ordemservico.repository.UserRepository;

import java.sql.SQLException;

public class DatabaseSetup {

    private static void createTables() throws SQLException {
        new UserRepository(Database.connect()).createUserTable();
    }

    private static void insertInitialData() throws SQLException {
        new UserRepository(Database.connect()).insertUserDefaultData();
    }

    public static void main(String[] args) {
        try {
            createTables();
            insertInitialData();
        } catch (SQLException e) {
            System.out.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }
}
