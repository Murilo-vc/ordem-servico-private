package org.example.murilo.ordemservico.gui;

import org.example.murilo.ordemservico.domain.constants.ColorCodes;
import org.example.murilo.ordemservico.domain.dto.CheckOwnProfileDto;
import org.example.murilo.ordemservico.domain.dto.ProfileDto;
import org.example.murilo.ordemservico.domain.dto.SmallUserDto;
import org.example.murilo.ordemservico.domain.entity.User;
import org.example.murilo.ordemservico.enumeration.UserRoleEnum;
import org.example.murilo.ordemservico.handler.exception.BaseException;
import org.example.murilo.ordemservico.handler.exception.UserNotFoundException;
import org.example.murilo.ordemservico.service.ClientService;
import org.example.murilo.ordemservico.util.StringUtils;

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

public class UsersWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private final ClientService clientService;
    private String token;
    private UserRoleEnum role;
    private List<SmallUserDto> users;
    private JPanel contentPane;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtUsername;

    public UsersWindow(final String token, final UserRoleEnum role, final ClientService clienteService) {
        this.token = token;
        this.role = role;
        this.clientService = clienteService;
        this.users = new ArrayList<>();
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
                    UsersWindow frame = new UsersWindow("token", UserRoleEnum.ADM, null);
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

    private void openProfile() {
        try {
            final CheckOwnProfileDto response = this.clientService.checkProfile(this.token);
            final ProfileDto profile = response.getDados();
            final User user = new User(profile.getUsuario(), profile.getNome(), profile.getSenha(), this.role);
            final ProfileWindow pw = new ProfileWindow(null, this, this.clientService, this.token, this.role, user);
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

    private void openOrdersWindow() {
        final HomeWindow hw = new HomeWindow(this.token, this.role, this.clientService);
        hw.setVisible(true);
        this.dispose();
    }

    private void openCreateUserWindow() {
        final NewUserWindow nuw = new NewUserWindow(this.clientService, this, token);
        nuw.setVisible(true);
        this.setVisible(false);
    }

    public void populateTable() {
        SwingUtilities.invokeLater(() -> {
            try {
                this.users = this.clientService.findAllUsers(this.token);
                tableModel.setRowCount(0);
                for (SmallUserDto u : users) {
                    tableModel.addRow(new Object[]{u.getNome(), u.getUsuario(), u.getPerfil()});
                }
            } catch (BaseException e) {
                JOptionPane.showMessageDialog(null, "Um erro ocorreu durante o cadastro: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                System.err.println("Um erro ocorreu: " + e.getMessage());
            } catch (NullPointerException ignored) {
            }
        });
    }

    private SmallUserDto getSelectedUser() throws UserNotFoundException {
        final String username = this.txtUsername.getText();

        if (StringUtils.isEmpty(username)) {
            throw new UserNotFoundException();
        }

        for (SmallUserDto u : this.users) {
            if (u.getUsuario().equals(username)) {
                return u;
            }
        }

        throw new UserNotFoundException();
    }

    private void updateUser() {
        try {
            final SmallUserDto u = this.getSelectedUser();
            final User user = new User(u.getUsuario(), u.getNome(), "", UserRoleEnum.getById(u.getPerfil()));
            final ProfileWindow pw = new ProfileWindow(null, this, this.clientService, this.token, this.role, user);
            pw.setVisible(true);
            this.setVisible(false);
        } catch (UserNotFoundException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println(e.getMessage());
        }
    }

    private void deleteUser() {
        try {
            final String targetUser = this.getSelectedUser().getUsuario();
            this.clientService.deleteUser(this.token, targetUser);
            this.populateTable();
        } catch (BaseException e) {
            JOptionPane.showMessageDialog(null, "Um erro ocorreu durante a exclusao: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Um erro ocorreu: " + e.getMessage());
        } catch (UserNotFoundException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println(e.getMessage());
        }
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
        btnWorkOrderPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openOrdersWindow();
            }
        });
        btnWorkOrderPage.setFocusable(false);
        btnWorkOrderPage.setBounds(10, 11, 128, 31);
        navBarPane.add(btnWorkOrderPage);

        JButton btnUserPage = new JButton("Usuarios");
        btnUserPage.setEnabled(false);
        btnUserPage.setFocusable(false);
        btnUserPage.setFocusPainted(false);
        btnUserPage.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        btnUserPage.setBorder(null);
        btnUserPage.setBounds(138, 11, 128, 31);
        navBarPane.add(btnUserPage);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(24, 103, 850, 304);
        contentPane.add(scrollPane);

        this.tableModel = new DefaultTableModel(new Object[]{"Nome", "Nome de Usuario", "Perfil"}, 0);
        table = new JTable(tableModel);
        scrollPane.setViewportView(table);

        JButton btnNewUser = new JButton("Novo");
        btnNewUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCreateUserWindow();
            }
        });
        btnNewUser.setBounds(24, 418, 136, 36);
        btnNewUser.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnNewUser.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnNewUser.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnNewUser.setEnabled(true);
        btnNewUser.setFocusable(false);
        btnNewUser.setFocusPainted(false);
        contentPane.add(btnNewUser);

        JLabel lblUsers = new JLabel("Usuarios");
        lblUsers.setHorizontalAlignment(SwingConstants.CENTER);
        lblUsers.setForeground(new Color(224, 224, 224));
        lblUsers.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblUsers.setBounds(389, 53, 106, 25);
        contentPane.add(lblUsers);

        JLabel lblUsername = new JLabel("Nome de Usuario:");
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
                txtUsername.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
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
                updateUser();
            }
        });
        btnEditUser.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnEditUser.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnEditUser.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnEditUser.setFocusable(false);
        btnEditUser.setFocusPainted(false);
        btnEditUser.setBounds(435, 524, 136, 36);
        contentPane.add(btnEditUser);

        JButton btnDeleteUser = new JButton("Apagar");
        btnDeleteUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
        btnDeleteUser.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnDeleteUser.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnDeleteUser.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnDeleteUser.setFocusable(false);
        btnDeleteUser.setFocusPainted(false);
        btnDeleteUser.setBounds(601, 524, 136, 36);
        contentPane.add(btnDeleteUser);

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
        btnReloadTable.setBounds(750, 72, 124, 20);
        contentPane.add(btnReloadTable);
    }
}
