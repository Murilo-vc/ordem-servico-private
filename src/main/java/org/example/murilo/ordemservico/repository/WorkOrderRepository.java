package org.example.murilo.ordemservico.repository;

import org.example.murilo.ordemservico.domain.entity.WorkOrder;
import org.example.murilo.ordemservico.enumeration.WorkOrderStatusEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkOrderRepository {

    private final Connection conn;

    public WorkOrderRepository(final Connection conn) {
        this.conn = conn;
    }

    public void createTable() throws SQLException {
        PreparedStatement st = null;
        try {
            final String sql = "CREATE TABLE IF NOT EXISTS work_order (" +
                "   id integer PRIMARY KEY AUTOINCREMENT," +
                "   description text NOT NULL," +
                "   status VARCHAR(20) NOT NULL," +
                "   created_by_id integer NOT NULL REFERENCES user (id)" +
                ");";
            st = conn.prepareStatement(sql);
            st.executeUpdate();
        } finally {
            Database.closeStatement(st);
            Database.disconnect();
        }
    }

    public void insertDefaultData() throws SQLException {
        PreparedStatement st = null;
        try {
            final String sql = """
                INSERT OR IGNORE INTO work_order (description, status, created_by_id)
                VALUES ('Trocar lâmpada', 'PENDENTE', 3),
                       ('Revisar encanamento', 'PENDENTE', 3);
                """;
            st = conn.prepareStatement(sql);
            st.executeUpdate();
        } finally {
            Database.closeStatement(st);
            Database.disconnect();
        }
    }

    public List<WorkOrder> findAllByStatusQuery(String statusQuery) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("""
                SELECT *
                FROM work_order wo """ +
                " WHERE " + statusQuery);
            rs = st.executeQuery();

            final List<WorkOrder> orders = new ArrayList<>();
            while (rs.next()) {
                final WorkOrder order = new WorkOrder(
                    rs.getInt("id"),
                    rs.getString("description"),
                    WorkOrderStatusEnum.getById(rs.getString("status").toLowerCase()),
                    rs.getInt("created_by_id")
                );
                orders.add(order);
            }
            return orders;
        } finally {
            Database.closeStatement(st);
            Database.closeResultSet(rs);
            Database.disconnect();
        }
    }

    public List<WorkOrder> findAllByUserIdAndStatusQuery(final Integer userId,
                                                         final String statusQuery) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("""
                SELECT *
                  FROM work_order wo
                 WHERE created_by_id = ?
                   AND """ + statusQuery);
            st.setInt(1, userId);
            rs = st.executeQuery();

            final List<WorkOrder> orders = new ArrayList<>();
            while (rs.next()) {
                final WorkOrder order = new WorkOrder(
                    rs.getInt("id"),
                    rs.getString("description"),
                    WorkOrderStatusEnum.getById(rs.getString("status").toLowerCase()),
                    rs.getInt("created_by_id")
                );
                orders.add(order);
            }
            return orders;
        } finally {
            Database.closeStatement(st);
            Database.closeResultSet(rs);
            Database.disconnect();
        }
    }

    public WorkOrder findOneById(final Integer id) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM work_order wo WHERE wo.id = ? LIMIT 1");
            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()) {
                return new WorkOrder(
                    rs.getInt("id"),
                    rs.getString("description"),
                    WorkOrderStatusEnum.getById(rs.getString("status").toLowerCase()),
                    rs.getInt("created_by_id")
                );
            }

            return null;
        } finally {
            Database.closeStatement(st);
            Database.closeResultSet(rs);
            Database.disconnect();
        }
    }

    public void create(final WorkOrder workOrder) throws SQLException {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("INSERT INTO work_order (description, status, created_by_id) VALUES (?, ?, ?)");

            st.setString(1, workOrder.getDescription());
            st.setString(2, workOrder.getStatus().toString());
            st.setInt(3, workOrder.getCreatedById());

            st.executeUpdate();
        } finally {
            Database.closeStatement(st);
            Database.disconnect();
        }
    }

    public void update(final WorkOrder workOrder) throws SQLException {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("UPDATE work_order SET description = ?, status = ?, created_by_id = ? WHERE id = ?");

            st.setString(1, workOrder.getDescription());
            st.setString(2, workOrder.getStatus().toString());
            st.setInt(3, workOrder.getCreatedById());
            st.setInt(4, workOrder.getId());

            st.executeUpdate();
        } finally {
            Database.closeStatement(st);
            Database.disconnect();
        }
    }
}
