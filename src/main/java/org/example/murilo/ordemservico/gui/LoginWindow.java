package org.example.murilo.ordemservico.gui;

import org.example.murilo.ordemservico.domain.constants.ColorCodes;
import org.example.murilo.ordemservico.domain.dto.LoginDto;
import org.example.murilo.ordemservico.enumeration.UserRoleEnum;
import org.example.murilo.ordemservico.handler.exception.BaseException;
import org.example.murilo.ordemservico.service.ClientService;
import org.example.murilo.ordemservico.util.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private ClientService clientService;
    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField passwordField;
    private JLabel lblUsernameErrorMessage;
    private JLabel lblPasswordErrorMessage;

    public LoginWindow(final ClientService clientService) {
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
//                    final String ip = "";
//                    final int port = 123;
//                    final ClientService cs = new ClientService();
//                    cs.connectServer(ip, port);
                    LoginWindow frame = new LoginWindow(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openSignUpWindow() {
        final SignUpWindow sup = new SignUpWindow(this.clientService);
        sup.setVisible(true);
        this.dispose();
    }

    private void showPassword(final JCheckBox source, final JPasswordField txt) {
        if (source.isSelected()) {
            txt.setEchoChar((char) 0);
        } else {
            txt.setEchoChar('●');
        }
    }

    private boolean validateUsername() {
        final String username = this.txtUsername.getText();
        if (StringUtils.isEmpty(username)) {
            this.lblUsernameErrorMessage.setText("<html><p align='justify'>Campo não deve ser vazio!</p></html>");
            return false;
        }

        this.lblUsernameErrorMessage.setText("");
        return true;
    }

    private boolean validatePassword() {
        final String password = new String(this.passwordField.getPassword());
        if (StringUtils.isEmpty(password)) {
            this.lblPasswordErrorMessage.setText("<html><p align='justify'>Campo não deve ser vazio!</p></html>");
            return false;
        }

        this.lblPasswordErrorMessage.setText("");
        return true;
    }

    private void login() {
        if (!validateUsername() | !validatePassword()) {
            return;
        }
        final String username = this.txtUsername.getText();
        final String password = new String(this.passwordField.getPassword());

        try {
            final LoginDto response = this.clientService.login(username, password);
            JOptionPane.showMessageDialog(null, "Login realizado com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            final HomeWindow hw = new HomeWindow(response.getToken(), UserRoleEnum.getById(response.getPerfil()), this.clientService);
            hw.setVisible(true);
            dispose();
        } catch (BaseException e) {
            JOptionPane.showMessageDialog(null, "Um erro ocorreu durante o cadastro: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Um erro ocorreu: " + e.getMessage());
        }
    }

    /**
     * Create the frame.
     */
    public void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 642, 510);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));

        setContentPane(contentPane);

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogin.setBounds(259, 11, 106, 25);
        lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblLogin.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(lblLogin);

        JLabel lblDontHaveAccount = new JLabel("<html><p align='justify'>Ainda não possui uma conta? Cadastrar</p></html>");
        lblDontHaveAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openSignUpWindow();
            }
        });
        contentPane.setLayout(null);
        lblDontHaveAccount.setBounds(173, 410, 279, 25);
        lblDontHaveAccount.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        lblDontHaveAccount.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblDontHaveAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(lblDontHaveAccount);

        JLabel lblUsername = new JLabel("Nome:");
        lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
        lblUsername.setForeground(new Color(224, 224, 224));
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblUsername.setBounds(145, 110, 70, 25);
        contentPane.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setColumns(10);
        txtUsername.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        txtUsername.setForeground(Color.decode(ColorCodes.SECONDARY_TEXT_COLOR));
        txtUsername.setBorder(null);
        txtUsername.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtUsername.setForeground(Color.BLACK);
                txtUsername.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                validateUsername();
                txtUsername.setForeground(Color.decode(ColorCodes.SECONDARY_TEXT_COLOR));
                txtUsername.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
            }
        });
        txtUsername.setBounds(225, 114, 175, 20);
        contentPane.add(txtUsername);

        JSeparator separatorUsername = new JSeparator();
        separatorUsername.setForeground(new Color(136, 136, 136));
        separatorUsername.setBackground(new Color(136, 136, 136));
        separatorUsername.setBounds(225, 133, 175, 8);
        contentPane.add(separatorUsername);

        JLabel lblPassword = new JLabel("Senha:");
        lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
        lblPassword.setForeground(new Color(224, 224, 224));
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblPassword.setBounds(126, 219, 89, 24);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(225, 223, 175, 20);
        passwordField.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        passwordField.setForeground(Color.decode(ColorCodes.SECONDARY_TEXT_COLOR));
        passwordField.setBorder(null);
        contentPane.add(passwordField);
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setForeground(Color.BLACK);
                passwordField.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                validatePassword();
                passwordField.setForeground(Color.decode(ColorCodes.SECONDARY_TEXT_COLOR));
                passwordField.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
            }
        });
        contentPane.add(passwordField);

        JSeparator separatorPassword = new JSeparator();
        separatorPassword.setForeground(new Color(136, 136, 136));
        separatorPassword.setBackground(new Color(136, 136, 136));
        separatorPassword.setBounds(225, 243, 175, 8);
        contentPane.add(separatorPassword);

        JCheckBox chckbxShowPassword = new JCheckBox("Mostrar Senha");
        chckbxShowPassword.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        chckbxShowPassword.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        chckbxShowPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPassword(chckbxShowPassword, passwordField);
            }
        });
        chckbxShowPassword.setBounds(258, 250, 109, 23);
        contentPane.add(chckbxShowPassword);

        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        btnLogin.setBounds(245, 354, 136, 36);
        btnLogin.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnLogin.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnLogin.setEnabled(true);
        contentPane.add(btnLogin);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 89, 471);
        panel.setBackground(Color.decode(ColorCodes.BORDER_COLOR));
        contentPane.add(panel);

        lblUsernameErrorMessage = new JLabel("");
        lblUsernameErrorMessage.setForeground(Color.decode(ColorCodes.ERROR_MESSAGE_COLOR));
        lblUsernameErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblUsernameErrorMessage.setBounds(410, 105, 201, 36);
        contentPane.add(lblUsernameErrorMessage);

        lblPasswordErrorMessage = new JLabel("");
        lblPasswordErrorMessage.setForeground(Color.decode(ColorCodes.ERROR_MESSAGE_COLOR));
        lblPasswordErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblPasswordErrorMessage.setBounds(410, 219, 201, 36);
        contentPane.add(lblPasswordErrorMessage);
    }
}
