package org.example.murilo.ordemservico.gui;

import org.example.murilo.ordemservico.domain.constants.ColorCodes;
import org.example.murilo.ordemservico.handler.exception.BaseException;
import org.example.murilo.ordemservico.service.ClientService;
import org.example.murilo.ordemservico.util.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class SignUpWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private ClientService clientService;
    private JPanel contentPane;
    private JTextField txtName;
    private JTextField txtUsername;
    private JPasswordField passwordField;
    private JCheckBox chckbxShowPassword;
    private JLabel lblNameErrorMessage;
    private JLabel lblUsernameErrorMessage;
    private JLabel lblPasswordErrorMessage;

    public SignUpWindow(final ClientService clientService) {
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
                    final String ip = "";
                    final int port = 123;
                    final ClientService cs = new ClientService();
                    cs.connectServer(ip, port);
                    SignUpWindow frame = new SignUpWindow(cs);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showPassword(final JCheckBox source, final JPasswordField txt) {
        if (source.isSelected()) {
            txt.setEchoChar((char) 0);
        } else {
            txt.setEchoChar('●');
        }
    }

    private void openLoginWindow() {
        final LoginWindow lw = new LoginWindow(this.clientService);
        lw.setVisible(true);
        this.dispose();
    }

    private boolean validateName() {
        final String name = this.txtName.getText();
        if (StringUtils.isEmpty(name)) {
            this.lblNameErrorMessage.setText("<html><p align='justify'>Campo não deve ser vazio!</p></html>");
            return false;
        }
        if (name.length() < 3 || name.length() > 30) {
            this.lblNameErrorMessage.setText("<html><p align='justify'>Campo deve ter entre 3 e 30 caracteres!</p></html>");
            return false;
        }
        if (name.contains(" ")) {
            this.lblNameErrorMessage.setText("<html><p align='justify'>Campo não deve possuir espaço vazio!</p></html>");
            return false;
        }
        if (StringUtils.containsSpecialCharacter(name)) {
            this.lblNameErrorMessage.setText("<html><p align='justify'>Campo não deve possuir caracteres especiais!</p></html>");
            return false;
        }

        this.lblNameErrorMessage.setText("");
        return true;
    }

    private boolean validateUsername() {
        final String username = this.txtUsername.getText();
        if (StringUtils.isEmpty(username)) {
            this.lblUsernameErrorMessage.setText("<html><p align='justify'>Campo não deve ser vazio!</p></html>");
            return false;
        }
        if (username.length() < 3 || username.length() > 30) {
            this.lblUsernameErrorMessage.setText("<html><p align='justify'>Campo deve ter entre 3 e 30 caracteres!</p></html>");
            return false;
        }
        if (username.contains(" ")) {
            this.lblUsernameErrorMessage.setText("<html><p align='justify'>Campo não deve possuir espaço vazio!</p></html>");
            return false;
        }
        if (StringUtils.containsSpecialCharacter(username)) {
            this.lblUsernameErrorMessage.setText("<html><p align='justify'>Campo não deve possuir caracteres especiais!</p></html>");
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
        if (password.length() < 4 || password.length() > 10) {
            this.lblPasswordErrorMessage.setText("<html><p align='justify'>Campo deve ter entre 4 e 10 caracteres!</p></html>");
            return false;
        }
        if (password.contains(" ")) {
            this.lblPasswordErrorMessage.setText("<html><p align='justify'>Campo não deve possuir espaço vazio!</p></html>");
            return false;
        }
        if (StringUtils.containsSpecialCharacter(password)) {
            this.lblPasswordErrorMessage.setText("<html><p align='justify'>Campo não deve possuir caracteres especiais!</p></html>");
            return false;
        }

        this.lblPasswordErrorMessage.setText("");
        return true;
    }

    private void signUp() {
        if (!this.validateName() | !this.validateUsername() | !this.validatePassword()) {
            return;
        }

        final String name = this.txtName.getText();
        final String username = this.txtUsername.getText();
        final String password = new String(this.passwordField.getPassword());

        try {
            this.clientService.signUp(name, username, password);
            JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
        } catch (BaseException e) {
            JOptionPane.showMessageDialog(null, "Um erro ocorreu durante o cadastro: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Um erro ocorreu: " + e.getMessage());
        }
    }

    /**
     * Create the frame.
     */
    public void initComponents() {
        setBounds(100, 100, 642, 510);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblSignUp = new JLabel("Cadastre-se");
        lblSignUp.setHorizontalAlignment(SwingConstants.CENTER);
        lblSignUp.setBounds(259, 11, 106, 25);
        lblSignUp.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblSignUp.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(lblSignUp);

        JLabel lblName = new JLabel("Nome:");
        lblName.setHorizontalAlignment(SwingConstants.TRAILING);
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblName.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        lblName.setBounds(145, 76, 70, 25);
        contentPane.add(lblName);

        JLabel lblPassword = new JLabel("Senha:");
        lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblPassword.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        lblPassword.setBounds(100, 255, 115, 25);
        contentPane.add(lblPassword);

        JLabel lblUsername = new JLabel("Nome de Usuario:");
        lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblUsername.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        lblUsername.setBounds(45, 168, 170, 25);
        contentPane.add(lblUsername);

        txtName = new JTextField();
        txtName.setBounds(225, 80, 175, 20);
        contentPane.add(txtName);
        txtName.setColumns(10);
        txtName.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        txtName.setForeground(Color.decode(ColorCodes.SECONDARY_TEXT_COLOR));
        txtName.setBorder(null);
        txtName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtName.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                validateName();
                txtName.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
            }
        });

        txtUsername = new JTextField();
        txtUsername.setBounds(225, 172, 175, 20);
        contentPane.add(txtUsername);
        txtUsername.setColumns(10);
        txtUsername.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        txtUsername.setForeground(Color.decode(ColorCodes.SECONDARY_TEXT_COLOR));
        txtUsername.setBorder(null);
        txtUsername.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtUsername.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                validateUsername();
                txtUsername.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
            }
        });

        passwordField = new JPasswordField();
        passwordField.setBounds(225, 259, 175, 20);
        passwordField.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        passwordField.setForeground(Color.decode(ColorCodes.SECONDARY_TEXT_COLOR));
        passwordField.setBorder(null);
        contentPane.add(passwordField);
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                validatePassword();
                passwordField.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
            }
        });

        JButton btnSignUp = new JButton("Cadastrar");
        btnSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
        btnSignUp.setBounds(245, 354, 136, 36);
        btnSignUp.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnSignUp.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnSignUp.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnSignUp.setEnabled(true);
        contentPane.add(btnSignUp);

        JSeparator separator = new JSeparator();
        separator.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        separator.setForeground(Color.decode(ColorCodes.ACCENT_COLOR));
        separator.setBounds(225, 99, 175, 8);
        contentPane.add(separator);

        JSeparator separator_1 = new JSeparator();
        separator_1.setForeground(new Color(136, 136, 136));
        separator_1.setBackground(new Color(136, 136, 136));
        separator_1.setBounds(225, 192, 175, 8);
        contentPane.add(separator_1);

        JSeparator separator_2 = new JSeparator();
        separator_2.setForeground(new Color(136, 136, 136));
        separator_2.setBackground(new Color(136, 136, 136));
        separator_2.setBounds(225, 279, 175, 8);
        contentPane.add(separator_2);

        JLabel lblNameInstructions = new JLabel("<html><p align='justify'>Nome deve ter entre 3 e 30 caracteres, NÃO deve conter espaço vazio e NÃO deve conter caracteres especiais</p></html>");
        lblNameInstructions.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNameInstructions.setBounds(158, 112, 304, 36);
        lblNameInstructions.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(lblNameInstructions);

        JLabel lblUsernameInstructions = new JLabel("<html><p align='justify'>Nome de Usuario deve ter entre 3 e 30 caracteres, NÃO deve conter espaço vazio e NÃO deve conter caracteres especiais</p></html>");
        lblUsernameInstructions.setForeground(new Color(224, 224, 224));
        lblUsernameInstructions.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblUsernameInstructions.setBounds(158, 202, 304, 36);
        contentPane.add(lblUsernameInstructions);

        JLabel lblPasswordInstructions = new JLabel("<html><p align='justify'>Senha deve ter entre 4 e 10 caracteres, NÃO deve conter espaço vazio e NÃO deve conter caracteres especiais</p></html>");
        lblPasswordInstructions.setForeground(new Color(224, 224, 224));
        lblPasswordInstructions.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblPasswordInstructions.setBounds(158, 307, 304, 36);
        contentPane.add(lblPasswordInstructions);

        chckbxShowPassword = new JCheckBox("Mostrar Senha");
        chckbxShowPassword.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        chckbxShowPassword.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        chckbxShowPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPassword(chckbxShowPassword, passwordField);
            }
        });
        chckbxShowPassword.setBounds(225, 286, 109, 23);
        contentPane.add(chckbxShowPassword);

        JLabel lblAlreadyHaveAccount = new JLabel("<html><p align='justify'>Ja possui uma conta? Entrar</p></html>");
        lblAlreadyHaveAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openLoginWindow();
            }
        });
        lblAlreadyHaveAccount.setBounds(220, 410, 186, 25);
        lblAlreadyHaveAccount.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        lblAlreadyHaveAccount.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblAlreadyHaveAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(lblAlreadyHaveAccount);

        lblNameErrorMessage = new JLabel("");
        lblNameErrorMessage.setForeground(Color.decode(ColorCodes.ERROR_MESSAGE_COLOR));
        lblNameErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNameErrorMessage.setBounds(410, 76, 201, 36);
        contentPane.add(lblNameErrorMessage);

        lblUsernameErrorMessage = new JLabel("");
        lblUsernameErrorMessage.setForeground(Color.decode(ColorCodes.ERROR_MESSAGE_COLOR));
        lblUsernameErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblUsernameErrorMessage.setBounds(410, 159, 201, 36);
        contentPane.add(lblUsernameErrorMessage);

        lblPasswordErrorMessage = new JLabel("");
        lblPasswordErrorMessage.setForeground(Color.decode(ColorCodes.ERROR_MESSAGE_COLOR));
        lblPasswordErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblPasswordErrorMessage.setBounds(410, 249, 201, 36);
        contentPane.add(lblPasswordErrorMessage);
    }
}
