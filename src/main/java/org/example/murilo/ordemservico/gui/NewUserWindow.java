package org.example.murilo.ordemservico.gui;

import org.example.murilo.ordemservico.domain.constants.ColorCodes;
import org.example.murilo.ordemservico.enumeration.UserRoleEnum;
import org.example.murilo.ordemservico.handler.exception.BaseException;
import org.example.murilo.ordemservico.service.ClientService;
import org.example.murilo.ordemservico.util.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class NewUserWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private ClientService clientService;
    private UsersWindow usersWindow;
    private String token;
    private JPanel contentPane;
    private JTextField txtName;
    private JTextField txtUsername;
    private JPasswordField passwordField;
    private JCheckBox chckbxShowPassword;
    private ButtonGroup btnGroupRole;
    private JRadioButton rbAdm;
    private JRadioButton rbComum;
    private JLabel lblNameErrorMessage;
    private JLabel lblUsernameErrorMessage;
    private JLabel lblPasswordErrorMessage;
    private JButton btnVoltar;

    public NewUserWindow(final ClientService clientService, final UsersWindow usersWindow, final String token) {
        this.clientService = clientService;
        this.usersWindow = usersWindow;
        this.token = token;
        this.initComponents();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    NewUserWindow frame = new NewUserWindow(null, null, null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void openUsersWindow() {
        this.usersWindow.setVisible(true);
        this.dispose();
    }

    private void showPassword(final JCheckBox source, final JPasswordField txt) {
        if (source.isSelected()) {
            txt.setEchoChar((char) 0);
        } else {
            txt.setEchoChar('●');
        }
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

    private UserRoleEnum getSelectedRole() {
        if (this.rbComum.isSelected()) {
            return UserRoleEnum.COMUM;
        }
        return UserRoleEnum.ADM;

    }

    private void createUser() {
        if (!this.validateName() | !this.validateUsername() | !this.validatePassword()) {
            return;
        }

        final String name = this.txtName.getText();
        final String username = this.txtUsername.getText();
        final String password = new String(this.passwordField.getPassword());
        final UserRoleEnum role = this.getSelectedRole();

        try {
            this.clientService.createUser(this.token, name, username, password, role);
            JOptionPane.showMessageDialog(null, "Usuario criado com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            this.usersWindow.populateTable();
            this.usersWindow.setVisible(true);
            this.dispose();
        } catch (BaseException e) {
            JOptionPane.showMessageDialog(null, "Um erro ocorreu durante a criacao: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Um erro ocorreu: " + e.getMessage());
        }
    }

    /**
     * Create the frame.
     */
    public void initComponents() {
        setBounds(100, 100, 642, 570);
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

        JLabel lblRole = new JLabel("Perfil:");
        lblRole.setHorizontalAlignment(SwingConstants.TRAILING);
        lblRole.setForeground(new Color(224, 224, 224));
        lblRole.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblRole.setBounds(45, 368, 170, 25);
        contentPane.add(lblRole);

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

        rbComum = new JRadioButton("Comum");
        rbComum.setBounds(225, 368, 109, 23);
        rbComum.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        rbComum.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        rbComum.setSelected(true);
        contentPane.add(rbComum);

        rbAdm = new JRadioButton("ADM");
        rbAdm.setBounds(353, 368, 109, 23);
        rbAdm.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        rbAdm.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(rbAdm);

        this.btnGroupRole = new ButtonGroup();
        btnGroupRole.add(rbComum);
        btnGroupRole.add(rbAdm);

        JButton btnSignUp = new JButton("Criar");
        btnSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createUser();
            }
        });
        btnSignUp.setBounds(245, 424, 136, 36);
        btnSignUp.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnSignUp.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnSignUp.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnSignUp.setEnabled(true);
        contentPane.add(btnSignUp);

        JSeparator separatorName = new JSeparator();
        separatorName.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        separatorName.setForeground(Color.decode(ColorCodes.ACCENT_COLOR));
        separatorName.setBounds(225, 99, 175, 8);
        contentPane.add(separatorName);

        JSeparator separatorUsername = new JSeparator();
        separatorUsername.setForeground(new Color(136, 136, 136));
        separatorUsername.setBackground(new Color(136, 136, 136));
        separatorUsername.setBounds(225, 192, 175, 8);
        contentPane.add(separatorUsername);

        JSeparator separatorPassword = new JSeparator();
        separatorPassword.setForeground(new Color(136, 136, 136));
        separatorPassword.setBackground(new Color(136, 136, 136));
        separatorPassword.setBounds(225, 279, 175, 8);
        contentPane.add(separatorPassword);

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

        btnVoltar = new JButton("<html><p align='justify'>Voltar</p></html>");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openUsersWindow();
            }
        });
        btnVoltar.setBounds(10, 11, 89, 23);
        btnVoltar.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnVoltar.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnVoltar.setFocusable(false);
        contentPane.add(btnVoltar);
    }

}
