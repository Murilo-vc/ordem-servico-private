package org.example.murilo.ordemservico.gui;

import org.example.murilo.ordemservico.domain.constants.ColorCodes;
import org.example.murilo.ordemservico.domain.dto.UpdateUserDto;
import org.example.murilo.ordemservico.domain.entity.User;
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

public class ProfileWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private final ClientService clientService;
    private final UserRoleEnum role;
    private HomeWindow homeWindow;
    private UsersWindow usersWindow;
    private String token;
    private User user;
    private JPanel contentPane;
    private JTextField txtName;
    private JTextField txtUsername;
    private JPasswordField passwordField;
    private JSeparator separatorPassword;
    private JSeparator separatorUsername;
    private JSeparator separatorName;
    private JLabel lblNameInstructions;
    private JLabel lblUsernameInstructions;
    private JLabel lblPasswordInstructions;
    private JLabel lblNameErrorMessage;
    private JLabel lblUsernameErrorMessage;
    private JLabel lblPasswordErrorMessage;
    private JButton btnVoltar;
    private JButton btnEditProfile;
    private JButton btnSaveChanges;
    private JCheckBox chckbxShowPassword;
    private JButton btnCancelChanges;
    private JButton btnDeleteAccount;
    private JLabel lblRole;
    private JTextField txtRole;
    private JSeparator separatorRole;
    private ButtonGroup btnGroupRole;
    private JRadioButton rbAdm;
    private JRadioButton rbComum;

    public ProfileWindow(final HomeWindow homeWindow, final UsersWindow usersWindow, final ClientService clientService, final String token, final UserRoleEnum role, final User user) {
        this.homeWindow = homeWindow;
        this.usersWindow = usersWindow;
        this.clientService = clientService;
        this.token = token;
        this.role = role;
        this.user = user;
        this.initComponents();
        this.loadUserInfo();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final User user = new User("User", "username", "pass", UserRoleEnum.COMUM);
                    final HomeWindow hw = null;
                    ProfileWindow frame = new ProfileWindow(hw, null, null, "username", UserRoleEnum.ADM, user);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    private void saveChanges() {
        if (!this.validateName() | !this.validateUsername() | !this.validatePassword()) {
            return;
        }

        final String newName = this.txtName.getText();
        final String newUsername = this.txtUsername.getText();
        final String newPassword = new String(this.passwordField.getPassword());

        try {
            if (UserRoleEnum.ADM.equals(this.role)) {
                final UserRoleEnum newRole = this.getSelectedRole();
                this.clientService.updateUser(token, this.user.getUsername(), newName, newPassword, newRole);
                JOptionPane.showMessageDialog(null, "Usuario atualizado com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
                this.txtRole.setText(newRole.getId());
                this.toggleEditMode(false);
                return;
            }
	        
            final UpdateUserDto updatedUser = this.clientService.updateSelf(this.token, newUsername, newName, newPassword);

            if (this.token.equals(this.user.getUsername()) && !this.token.equals(updatedUser.getToken())) {
                this.token = updatedUser.getToken();
                if (this.homeWindow != null) {
                    this.homeWindow.setToken(updatedUser.getToken());
                } else {
                    this.usersWindow.setToken(updatedUser.getToken());
                }
            }

            this.user = new User(newUsername, newName, newPassword, this.role);
            this.loadUserInfo();
            this.toggleEditMode(false);
        } catch (BaseException e) {
            JOptionPane.showMessageDialog(null, "Um erro ocorreu durante a atualizacao do perfil: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Um erro ocorreu: " + e.getMessage());
        }
    }

    private void toggleEditMode(final boolean isEditing) {
        if (!UserRoleEnum.ADM.equals(this.role)) {
            this.separatorUsername.setVisible(isEditing);
            this.txtUsername.setEditable(isEditing);
            this.txtUsername.setFocusable(isEditing);
            this.lblUsernameInstructions.setVisible(isEditing);
        } else {
            this.txtRole.setVisible(!isEditing);
            this.separatorRole.setVisible(!isEditing);
            this.rbComum.setVisible(isEditing);
            this.rbAdm.setVisible(isEditing);
        }
        this.separatorName.setVisible(isEditing);
        this.separatorPassword.setVisible(isEditing);
        this.txtName.setFocusable(isEditing);
        this.txtName.setEditable(isEditing);
        this.passwordField.setEditable(isEditing);
        this.passwordField.setFocusable(isEditing);
        this.lblNameInstructions.setVisible(isEditing);
        this.lblPasswordInstructions.setVisible(isEditing);
        this.btnEditProfile.setEnabled(!isEditing);
        this.btnEditProfile.setVisible(!isEditing);
        this.btnSaveChanges.setVisible(isEditing);
        this.btnSaveChanges.setEnabled(isEditing);
        this.btnCancelChanges.setEnabled(isEditing);
        this.btnCancelChanges.setVisible(isEditing);
        this.chckbxShowPassword.setVisible(isEditing);
    }

    private void loadUserInfo() {
        final String name = this.user.getName();
        final String username = this.user.getUsername();
        final String password = this.user.getPassword();
        final UserRoleEnum role = this.user.getRole();

        this.txtName.setText(name);
        this.txtUsername.setText(username);
        this.passwordField.setText(password);
        this.txtRole.setText(role.getId());
        if (UserRoleEnum.COMUM.equals(role)) {
            this.rbComum.setSelected(true);
        } else {
            this.rbAdm.setSelected(true);
        }
    }

    private void openHomeWindow() {
        if (this.homeWindow != null) {
            this.homeWindow.setVisible(true);
        } else if (this.usersWindow != null) {
            this.usersWindow.setVisible(true);
            this.usersWindow.populateTable();
        }
        this.dispose();
    }

    private void showPassword(final JCheckBox source, final JPasswordField txt) {
        if (source.isSelected()) {
            txt.setEchoChar((char) 0);
        } else {
            txt.setEchoChar('●');
        }
    }

    private void deleteAccount() {
        try {
            if (UserRoleEnum.ADM.equals(this.role)) {
                this.clientService.deleteUser(this.token, user.getUsername());
            } else {
                this.clientService.deleteUser(this.user.getUsername());
            }
            JOptionPane.showMessageDialog(null, "Conta deletada com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            this.openHomeWindow();
        } catch (BaseException e) {
            JOptionPane.showMessageDialog(null, "Um erro ocorreu durante a operação: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Um erro ocorreu: " + e.getMessage());
        }
    }

    /**
     * Create the frame.
     */
    public void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 700);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblProfile = new JLabel("Perfil");
        lblProfile.setHorizontalAlignment(SwingConstants.CENTER);
        lblProfile.setBounds(239, 11, 106, 25);
        lblProfile.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblProfile.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(lblProfile);

        JLabel lblName = new JLabel("Nome:");
        lblName.setHorizontalAlignment(SwingConstants.TRAILING);
        lblName.setForeground(new Color(224, 224, 224));
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblName.setBounds(110, 73, 70, 25);
        contentPane.add(lblName);

        JLabel lblPassword = new JLabel("Senha:");
        lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
        lblPassword.setForeground(new Color(224, 224, 224));
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblPassword.setBounds(65, 252, 115, 25);
        contentPane.add(lblPassword);

        JLabel lblUsername = new JLabel("Nome de Usuario:");
        lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
        lblUsername.setForeground(new Color(224, 224, 224));
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblUsername.setBounds(10, 165, 170, 25);
        contentPane.add(lblUsername);

        txtName = new JTextField();
        txtName.setForeground(new Color(176, 176, 176));
        txtName.setColumns(10);
        txtName.setBorder(null);
        txtName.setBackground(new Color(18, 18, 18));
        txtName.setBounds(190, 77, 175, 20);
        txtName.setEditable(false);
        txtName.setFocusable(false);
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
        contentPane.add(txtName);

        txtUsername = new JTextField();
        txtUsername.setForeground(new Color(176, 176, 176));
        txtUsername.setColumns(10);
        txtUsername.setBorder(null);
        txtUsername.setBackground(new Color(18, 18, 18));
        txtUsername.setBounds(190, 169, 175, 20);
        txtUsername.setEditable(false);
        txtUsername.setFocusable(false);
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
        contentPane.add(txtUsername);

        passwordField = new JPasswordField();
        passwordField.setForeground(new Color(176, 176, 176));
        passwordField.setBorder(null);
        passwordField.setBackground(new Color(18, 18, 18));
        passwordField.setBounds(190, 256, 175, 20);
        passwordField.setEditable(false);
        passwordField.setFocusable(false);
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
        contentPane.add(passwordField);

        separatorName = new JSeparator();
        separatorName.setForeground(new Color(136, 136, 136));
        separatorName.setBackground(new Color(136, 136, 136));
        separatorName.setBounds(190, 96, 175, 8);
        separatorName.setVisible(false);
        contentPane.add(separatorName);

        separatorUsername = new JSeparator();
        separatorUsername.setForeground(new Color(136, 136, 136));
        separatorUsername.setBackground(new Color(136, 136, 136));
        separatorUsername.setBounds(190, 189, 175, 8);
        separatorUsername.setVisible(false);
        contentPane.add(separatorUsername);

        separatorPassword = new JSeparator();
        separatorPassword.setForeground(new Color(136, 136, 136));
        separatorPassword.setBackground(new Color(136, 136, 136));
        separatorPassword.setBounds(190, 276, 175, 8);
        separatorPassword.setVisible(false);
        contentPane.add(separatorPassword);

        lblNameInstructions = new JLabel("<html><p align='justify'>Nome deve ter entre 3 e 30 caracteres, NÃO deve conter espaço vazio e NÃO deve conter caracteres especiais</p></html>");
        lblNameInstructions.setForeground(new Color(224, 224, 224));
        lblNameInstructions.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNameInstructions.setBounds(123, 109, 304, 36);
        lblNameInstructions.setVisible(false);
        contentPane.add(lblNameInstructions);

        lblUsernameInstructions = new JLabel("<html><p align='justify'>Nome de Usuario deve ter entre 3 e 30 caracteres, NÃO deve conter espaço vazio e NÃO deve conter caracteres especiais</p></html>");
        lblUsernameInstructions.setForeground(new Color(224, 224, 224));
        lblUsernameInstructions.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblUsernameInstructions.setBounds(123, 199, 304, 36);
        lblUsernameInstructions.setVisible(false);
        contentPane.add(lblUsernameInstructions);

        lblPasswordInstructions = new JLabel("<html><p align='justify'>Senha deve ter entre 4 e 10 caracteres, NÃO deve conter espaço vazio e NÃO deve conter caracteres especiais</p></html>");
        lblPasswordInstructions.setForeground(new Color(224, 224, 224));
        lblPasswordInstructions.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblPasswordInstructions.setBounds(123, 304, 304, 36);
        lblPasswordInstructions.setVisible(false);
        contentPane.add(lblPasswordInstructions);

        chckbxShowPassword = new JCheckBox("Mostrar Senha");
        chckbxShowPassword.setForeground(new Color(224, 224, 224));
        chckbxShowPassword.setBackground(new Color(18, 18, 18));
        chckbxShowPassword.setBounds(190, 283, 109, 23);
        chckbxShowPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPassword(chckbxShowPassword, passwordField);
            }
        });
        chckbxShowPassword.setVisible(false);
        contentPane.add(chckbxShowPassword);

        lblNameErrorMessage = new JLabel("");
        lblNameErrorMessage.setForeground(Color.RED);
        lblNameErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNameErrorMessage.setBounds(375, 73, 201, 36);
        contentPane.add(lblNameErrorMessage);

        lblUsernameErrorMessage = new JLabel("");
        lblUsernameErrorMessage.setForeground(Color.RED);
        lblUsernameErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblUsernameErrorMessage.setBounds(375, 156, 201, 36);
        contentPane.add(lblUsernameErrorMessage);

        lblPasswordErrorMessage = new JLabel("");
        lblPasswordErrorMessage.setForeground(Color.RED);
        lblPasswordErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblPasswordErrorMessage.setBounds(375, 246, 201, 36);
        contentPane.add(lblPasswordErrorMessage);

        btnVoltar = new JButton("<html><p align='justify'>Voltar</p></html>");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openHomeWindow();
            }
        });
        btnVoltar.setBounds(10, 11, 89, 23);
        btnVoltar.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnVoltar.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnVoltar.setFocusable(false);
        contentPane.add(btnVoltar);

        lblRole = new JLabel("Perfil:");
        lblRole.setHorizontalAlignment(SwingConstants.TRAILING);
        lblRole.setForeground(new Color(224, 224, 224));
        lblRole.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblRole.setBounds(10, 368, 170, 25);
        contentPane.add(lblRole);

        txtRole = new JTextField();
        txtRole.setForeground(new Color(176, 176, 176));
        txtRole.setFocusable(false);
        txtRole.setEditable(false);
        txtRole.setColumns(10);
        txtRole.setBorder(null);
        txtRole.setBackground(new Color(18, 18, 18));
        txtRole.setBounds(190, 368, 175, 20);
        contentPane.add(txtRole);

        separatorRole = new JSeparator();
        separatorRole.setForeground(new Color(136, 136, 136));
        separatorRole.setBackground(new Color(136, 136, 136));
        separatorRole.setBounds(190, 276, 175, 8);
        separatorRole.setVisible(false);
        contentPane.add(separatorRole);

        rbComum = new JRadioButton("Comum");
        rbComum.setBounds(190, 368, 109, 23);
        rbComum.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        rbComum.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        rbComum.setVisible(false);
        contentPane.add(rbComum);

        rbAdm = new JRadioButton("ADM");
        rbAdm.setVisible(false);
        rbAdm.setBounds(318, 368, 109, 23);
        rbAdm.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        rbAdm.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(rbAdm);

        this.btnGroupRole = new ButtonGroup();
        btnGroupRole.add(rbComum);
        btnGroupRole.add(rbAdm);

        final boolean hasPermission = this.token.equals(this.user.getUsername()) || UserRoleEnum.ADM.equals(this.role);

        if (hasPermission) {
            btnEditProfile = new JButton("<html><p align='justify'>Editar</p></html>");
            btnEditProfile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    toggleEditMode(true);
                }
            });
            btnEditProfile.setBounds(324, 430, 89, 23);
            btnEditProfile.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            btnEditProfile.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
            btnEditProfile.setFocusable(false);
            contentPane.add(btnEditProfile);

            btnSaveChanges = new JButton("<html><p align='justify'>Salvar Alterações</p></html>");
            btnSaveChanges.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveChanges();
                }
            });
            btnSaveChanges.setBounds(416, 430, 144, 23);
            btnSaveChanges.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            btnSaveChanges.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
            btnSaveChanges.setFocusable(false);
            btnSaveChanges.setEnabled(false);
            btnSaveChanges.setVisible(false);
            contentPane.add(btnSaveChanges);

            btnDeleteAccount = new JButton("<html><p align='justify'>Excluir Conta</p></html>");
            btnDeleteAccount.setBounds(451, 11, 123, 23);
            btnDeleteAccount.setBackground(Color.decode(ColorCodes.RED_BUTTON_COLOR));
            btnDeleteAccount.setBorderPainted(false);
            btnDeleteAccount.setFocusable(false);
            btnDeleteAccount.setFont(new Font("Tahoma", Font.BOLD, 13));
            btnDeleteAccount.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteAccount();
                }
            });
            contentPane.add(btnDeleteAccount);

            btnCancelChanges = new JButton("<html><p align='justify'>Cancelar</p></html>");
            btnCancelChanges.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loadUserInfo();
                    toggleEditMode(false);
                }
            });
            btnCancelChanges.setBounds(317, 430, 89, 23);
            btnCancelChanges.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            btnCancelChanges.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
            btnCancelChanges.setFocusable(false);
            btnCancelChanges.setVisible(false);
            btnCancelChanges.setEnabled(false);
            contentPane.add(btnCancelChanges);
        }
    }
}
