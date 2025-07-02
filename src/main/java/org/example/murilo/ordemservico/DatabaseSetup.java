package org.example.murilo.ordemservico;

import org.example.murilo.ordemservico.repository.Database;
import org.example.murilo.ordemservico.repository.UserRepository;
import org.example.murilo.ordemservico.repository.WorkOrderRepository;

import java.sql.SQLException;

public class DatabaseSetup {

    public static void createTables() throws SQLException {
        new UserRepository(Database.connect()).createUserTable();
        new WorkOrderRepository(Database.connect()).createTable();
    }

    public static void insertInitialData() throws SQLException {
        new UserRepository(Database.connect()).insertUserDefaultData();
        new WorkOrderRepository(Database.connect()).insertDefaultData();
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
