package org.example.murilo.ordemservico.gui;

import org.example.murilo.ordemservico.domain.constants.ColorCodes;
import org.example.murilo.ordemservico.domain.dto.CheckOwnProfileDto;
import org.example.murilo.ordemservico.domain.dto.ProfileDto;
import org.example.murilo.ordemservico.domain.entity.User;
import org.example.murilo.ordemservico.enumeration.UserRoleEnum;
import org.example.murilo.ordemservico.handler.exception.BaseException;
import org.example.murilo.ordemservico.service.ClientService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private String token;
    private UserRoleEnum role;
    private final ClientService clientService;
    private JPanel contentPane;

    public HomeWindow(final String token, final UserRoleEnum role, final ClientService clientService) {
        this.token = token;
        this.role = role;
        this.clientService = clientService;
        this.initComponents();
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
        navBarPane.add(btnUserPage);
    }
}
