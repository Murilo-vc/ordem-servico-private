package org.example.murilo.ordemservico.gui;

import org.example.murilo.ordemservico.domain.constants.ColorCodes;
import org.example.murilo.ordemservico.domain.dto.CheckOwnProfileDto;
import org.example.murilo.ordemservico.domain.dto.ProfileDto;
import org.example.murilo.ordemservico.domain.dto.WorkOrderDto;
import org.example.murilo.ordemservico.domain.entity.User;
import org.example.murilo.ordemservico.domain.entity.WorkOrder;
import org.example.murilo.ordemservico.enumeration.UserRoleEnum;
import org.example.murilo.ordemservico.enumeration.WorkOrderFilterEnum;
import org.example.murilo.ordemservico.enumeration.WorkOrderStatusEnum;
import org.example.murilo.ordemservico.handler.exception.BaseException;
import org.example.murilo.ordemservico.service.ClientService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

public class HomeWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private String token;
    private UserRoleEnum role;
    private final ClientService clientService;
    private List<WorkOrderDto> orders;
    private JPanel contentPane;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtUsername;
    private JComboBox cbFilter;

    public HomeWindow(final String token, final UserRoleEnum role, final ClientService clientService) {
        this.token = token;
        this.role = role;
        this.clientService = clientService;
        this.orders = new ArrayList<>();
        this.initComponents();
        this.populateTable();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HomeWindow frame = new HomeWindow("token", UserRoleEnum.COMUM, null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public void populateTable() {
        SwingUtilities.invokeLater(() -> {
            try {
                final WorkOrderFilterEnum filter = (WorkOrderFilterEnum) this.cbFilter.getSelectedItem();
                this.orders = this.clientService.findAllWorkOrders(this.token, filter);
                tableModel.setRowCount(0);
                for (WorkOrderDto order : this.orders) {
                    tableModel.addRow(new Object[]{order.getId(), order.getAutor(), order.getDescricao(), order.getStatus()});
                }
            } catch (BaseException e) {
                JOptionPane.showMessageDialog(null, "Um erro ocorreu durante o carregamento das ordens: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                System.err.println("Um erro ocorreu: " + e.getMessage());
            } catch (NullPointerException ignored) {
            }
        });
    }

    private void openProfile() {
        try {
            final CheckOwnProfileDto response = this.clientService.checkProfile(this.token);
            final ProfileDto profile = response.getDados();
            final User user = new User(profile.getUsuario(), profile.getNome(), profile.getSenha(), this.role);
            final ProfileWindow pw = new ProfileWindow(this, null, this.clientService, this.token, this.role, user);
            pw.setVisible(true);
            this.setVisible(false);
        } catch (BaseException e) {
            JOptionPane.showMessageDialog(null, "Um erro ocorreu durante a leitura do perfil: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Um erro ocorreu: " + e.getMessage());
        }
    }

    private void logout() {
        try {
            this.clientService.logout(this.token);
            JOptionPane.showMessageDialog(null, "Logout realizado com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            final LoginWindow lw = new LoginWindow(this.clientService);
            lw.setVisible(true);
            this.dispose();
        } catch (BaseException e) {
            JOptionPane.showMessageDialog(null, "Um erro ocorreu durante o cadastro: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Um erro ocorreu: " + e.getMessage());
        }
    }

    private void openUsersWindow() {
        final UsersWindow uw = new UsersWindow(this.token, this.role, this.clientService);
        uw.setVisible(true);
        this.dispose();
    }

    private void openCreateWorkOrderWindow() {
        final NewWorkOrderWindow nwow = new NewWorkOrderWindow(this.clientService, this, this.token);
        nwow.setVisible(true);
    }

    private WorkOrderDto getSelectedOrder() {
        try {
            final Integer orderId = Integer.parseInt(this.txtUsername.getText());

            for (WorkOrderDto order : this.orders) {
                if (order.getId().equals(orderId)) {
                    return order;
                }
            }

            return null;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Id deve ser um número!", "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Id deve ser um número!");
            return null;
        }
    }

    private void openWorkOrderUpdateWindow() {
        final WorkOrderDto selectedOrder = this.getSelectedOrder();
        if (selectedOrder == null) {
            return;
        }

        final WorkOrder order = new WorkOrder(
            selectedOrder.getId(),
            selectedOrder.getDescricao(),
            WorkOrderStatusEnum.getById(selectedOrder.getStatus()),
            null
        );
        final WorkOrderDetailsWindow wodw = new WorkOrderDetailsWindow(
            this, order, this.clientService, this.token, this.role
        );
        this.setVisible(false);
        wodw.setVisible(true);
    }

    /**
     * Create the frame.
     */
    public void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel navBarPane = new JPanel();
        navBarPane.setBounds(0, 0, 884, 42);
        navBarPane.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        contentPane.add(navBarPane);
        navBarPane.setLayout(null);
        
        JButton btnProfile = new JButton("<html><p align='justify'>Ver Perfil</p></html>");
        btnProfile.setBounds(662, 11, 89, 23);
        navBarPane.add(btnProfile);
        btnProfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openProfile();
            }
        });
        btnProfile.setFocusable(false);
        btnProfile.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        btnProfile.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));

        JButton btnLogout = new JButton("<html><p align='justify'>Logout</p></html>");
        btnLogout.setBounds(785, 11, 89, 23);
        navBarPane.add(btnLogout);
        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        btnLogout.setFocusable(false);
        btnLogout.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        btnLogout.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));

        JButton btnWorkOrderPage = new JButton("Ordens");
        btnWorkOrderPage.setEnabled(false);
        btnWorkOrderPage.setFocusable(false);
        btnWorkOrderPage.setFocusPainted(false);
        btnWorkOrderPage.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        btnWorkOrderPage.setBorder(null);
        btnWorkOrderPage.setBounds(10, 11, 128, 31);
        navBarPane.add(btnWorkOrderPage);

        JButton btnUserPage = new JButton("Usuarios");
        btnUserPage.setFocusable(false);
        btnUserPage.setFocusPainted(false);
        btnUserPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openUsersWindow();
            }
        });
        btnUserPage.setBounds(138, 11, 128, 31);

        if (UserRoleEnum.COMUM.equals(this.role)) {
            btnUserPage.setVisible(false);
        }

        navBarPane.add(btnUserPage);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(24, 135, 850, 304);
        contentPane.add(scrollPane);

        this.tableModel = new DefaultTableModel(new Object[]{"ID", "Autor", "Descricao", "Status"}, 0);
        this.table = new JTable(tableModel);
        scrollPane.setViewportView(this.table);

        JButton btnNewUser = new JButton("Nova");
        btnNewUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCreateWorkOrderWindow();
            }
        });
        btnNewUser.setBounds(24, 450, 136, 36);
        btnNewUser.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnNewUser.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnNewUser.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnNewUser.setEnabled(true);
        btnNewUser.setFocusable(false);
        btnNewUser.setFocusPainted(false);
        contentPane.add(btnNewUser);

        JLabel lblUsers = new JLabel("Ordens");
        lblUsers.setHorizontalAlignment(SwingConstants.CENTER);
        lblUsers.setForeground(new Color(224, 224, 224));
        lblUsers.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblUsers.setBounds(389, 53, 106, 25);
        contentPane.add(lblUsers);

        JLabel lblUsername = new JLabel("Id ordem:");
        lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
        lblUsername.setForeground(new Color(224, 224, 224));
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblUsername.setBounds(24, 535, 170, 25);
        contentPane.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setForeground(new Color(176, 176, 176));
        txtUsername.setColumns(10);
        txtUsername.setBorder(null);
        txtUsername.setBackground(new Color(18, 18, 18));
        txtUsername.setBounds(204, 539, 175, 20);
        txtUsername.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtUsername.setForeground(Color.BLACK);
                txtUsername.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtUsername.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
                txtUsername.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
            }
        });
        contentPane.add(txtUsername);

        JSeparator separator_1 = new JSeparator();
        separator_1.setForeground(new Color(136, 136, 136));
        separator_1.setBackground(new Color(136, 136, 136));
        separator_1.setBounds(204, 559, 175, 8);
        contentPane.add(separator_1);

        JButton btnEditUser = new JButton("Editar");
        btnEditUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openWorkOrderUpdateWindow();
            }
        });
        btnEditUser.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnEditUser.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnEditUser.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnEditUser.setFocusable(false);
        btnEditUser.setFocusPainted(false);
        btnEditUser.setBounds(435, 524, 136, 36);
        contentPane.add(btnEditUser);

        JButton btnReloadTable = new JButton("Atualizar");
        btnReloadTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                populateTable();
            }
        });
        btnReloadTable.setForeground(new Color(224, 224, 224));
        btnReloadTable.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnReloadTable.setFocusable(false);
        btnReloadTable.setFocusPainted(false);
        btnReloadTable.setBackground(new Color(136, 136, 136));
        btnReloadTable.setBounds(750, 104, 124, 20);
        contentPane.add(btnReloadTable);

        JLabel lblFilter = new JLabel("Filtro:");
        lblFilter.setHorizontalAlignment(SwingConstants.TRAILING);
        lblFilter.setForeground(new Color(224, 224, 224));
        lblFilter.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblFilter.setBounds(24, 99, 170, 25);
        contentPane.add(lblFilter);

        cbFilter = new JComboBox();
        cbFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                populateTable();
            }
        });
        cbFilter.setModel(new DefaultComboBoxModel(WorkOrderFilterEnum.values()));
        cbFilter.setBounds(204, 99, 159, 25);
        cbFilter.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        cbFilter.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        cbFilter.setFocusable(false);
        contentPane.add(cbFilter);
    }
}
