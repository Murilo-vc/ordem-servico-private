package org.example.murilo.ordemservico.gui;

import org.example.murilo.ordemservico.domain.constants.ColorCodes;
import org.example.murilo.ordemservico.domain.entity.WorkOrder;
import org.example.murilo.ordemservico.enumeration.UserRoleEnum;
import org.example.murilo.ordemservico.enumeration.WorkOrderStatusEnum;
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

public class WorkOrderDetailsWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private final ClientService clientService;
    private final HomeWindow homeWindow;
    private WorkOrder workOrder;
    private String token;
    private UserRoleEnum role;
    private JPanel contentPane;
    private JTextField txtId;
    private JButton btnVoltar;
    private JButton btnEditProfile;
    private JButton btnSaveChanges;
    private JButton btnCancelChanges;
    private JLabel lblStatus;
    private JTextField txtRole;
    private JTextArea txtDescription;
    private JLabel lblDescriptionErrorMessage;
    private JScrollPane scrollPane;
    private JComboBox cbStatus;

    /**
     * Create the frame.
     */
    public WorkOrderDetailsWindow(final HomeWindow homeWindow,
                                  final WorkOrder workOrder,
                                  final ClientService clientService,
                                  final String token,
                                  final UserRoleEnum role) {
        this.homeWindow = homeWindow;
        this.workOrder = workOrder;
        this.clientService = clientService;
        this.token = token;
        this.role = role;
        this.initComponents();
        this.loadOrderInfo();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final WorkOrder wo = new WorkOrder(1, "Description", WorkOrderStatusEnum.CANCELADA, 1);
                    WorkOrderDetailsWindow frame = new WorkOrderDetailsWindow(null, wo, null, "tokene", UserRoleEnum.COMUM);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadOrderInfo() {
        this.txtId.setText(workOrder.getId().toString());
        this.txtDescription.setText(workOrder.getDescription());
        this.txtRole.setText(workOrder.getStatus().toString());
        this.cbStatus.setSelectedItem(workOrder.getStatus());
    }

    private void toggleEditMode(final boolean isEditing) {
        txtDescription.setEnabled(isEditing);
        txtDescription.setFocusable(isEditing);
        this.cbStatus.setEnabled(isEditing);
        this.cbStatus.setVisible(isEditing);
        this.txtRole.setVisible(!isEditing);
        this.btnCancelChanges.setVisible(isEditing);
        this.btnCancelChanges.setEnabled(isEditing);
        this.btnSaveChanges.setVisible(isEditing);
        this.btnSaveChanges.setEnabled(isEditing);
        this.btnEditProfile.setVisible(!isEditing);
        this.btnEditProfile.setEnabled(!isEditing);
    }

    private void openHomeWindow() {
        this.homeWindow.setVisible(true);
        this.setVisible(false);
        this.dispose();
    }

    private boolean validateDescription() {
        final String description = this.txtDescription.getText();
        if (StringUtils.isEmpty(description)) {
            this.lblDescriptionErrorMessage.setText("<html><p align='justify'>Campo não deve ser vazio!</p></html>");
            return false;
        }
        if (description.length() < 3 || description.length() > 150) {
            this.lblDescriptionErrorMessage.setText("<html><p align='justify'>Campo deve ter entre 3 e 150 caracteres!</p></html>");
            return false;
        }

        this.lblDescriptionErrorMessage.setText("");
        return true;
    }

    private void saveChanges() {
        try {
            if (!validateDescription()) {
                return;
            }
            final Integer orderId = this.workOrder.getId();
            final String newDescription = this.txtDescription.getText();
            if (UserRoleEnum.ADM.equals(this.role)) {
                final WorkOrderStatusEnum newStatus = (WorkOrderStatusEnum) this.cbStatus.getSelectedItem();
                ;
                this.clientService.alterWorkOrder(this.token, orderId, newDescription, newStatus);
                JOptionPane.showMessageDialog(null, "Ordem alterada com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
                this.workOrder.setStatus(newStatus);
            } else {
                this.clientService.updateWorkOrder(this.token, orderId, newDescription);
                JOptionPane.showMessageDialog(null, "Ordem atualizada com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            }

            this.workOrder.setDescription(newDescription);
            this.loadOrderInfo();
            this.toggleEditMode(false);
        } catch (BaseException e) {
            JOptionPane.showMessageDialog(null, "Um erro ocorreu durante a operação: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Um erro ocorreu: " + e.getMessage());
        }
    }

    public void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 700);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblWorkOrder = new JLabel("Ordem de Serviço");
        lblWorkOrder.setHorizontalAlignment(SwingConstants.CENTER);
        lblWorkOrder.setBounds(177, 11, 229, 25);
        lblWorkOrder.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblWorkOrder.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(lblWorkOrder);

        JLabel lblId = new JLabel("ID:");
        lblId.setHorizontalAlignment(SwingConstants.TRAILING);
        lblId.setForeground(new Color(224, 224, 224));
        lblId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblId.setBounds(110, 73, 70, 25);
        contentPane.add(lblId);

        txtId = new JTextField();
        txtId.setForeground(new Color(176, 176, 176));
        txtId.setColumns(10);
        txtId.setBorder(null);
        txtId.setBackground(new Color(18, 18, 18));
        txtId.setBounds(190, 77, 175, 20);
        txtId.setEditable(false);
        txtId.setFocusable(false);
        contentPane.add(txtId);

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

        lblStatus = new JLabel("Status:");
        lblStatus.setHorizontalAlignment(SwingConstants.TRAILING);
        lblStatus.setForeground(new Color(224, 224, 224));
        lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblStatus.setBounds(10, 368, 170, 25);
        contentPane.add(lblStatus);

        txtRole = new JTextField();
        txtRole.setForeground(new Color(176, 176, 176));
        txtRole.setFocusable(false);
        txtRole.setEditable(false);
        txtRole.setColumns(10);
        txtRole.setBorder(null);
        txtRole.setBackground(new Color(18, 18, 18));
        txtRole.setBounds(190, 368, 175, 20);
        contentPane.add(txtRole);

        JLabel lblDescription = new JLabel("Descrição:");
        lblDescription.setHorizontalAlignment(SwingConstants.CENTER);
        lblDescription.setForeground(new Color(224, 224, 224));
        lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblDescription.setBounds(239, 139, 106, 25);
        contentPane.add(lblDescription);

        JLabel lblNameInstructions = new JLabel("<html><p align='justify'>Descrição deve ter entre 3 e 150 caracteres</p></html>");
        lblNameInstructions.setHorizontalAlignment(SwingConstants.CENTER);
        lblNameInstructions.setForeground(new Color(224, 224, 224));
        lblNameInstructions.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNameInstructions.setBounds(140, 267, 304, 36);
        contentPane.add(lblNameInstructions);

        lblDescriptionErrorMessage = new JLabel("");
        lblDescriptionErrorMessage.setForeground(Color.RED);
        lblDescriptionErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblDescriptionErrorMessage.setBounds(461, 163, 123, 107);
        contentPane.add(lblDescriptionErrorMessage);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(95, 163, 356, 107);
        contentPane.add(scrollPane);

        txtDescription = new JTextArea();
        txtDescription.setEnabled(false);
        txtDescription.setFocusable(false);
        txtDescription.setForeground(Color.decode(ColorCodes.SECONDARY_TEXT_COLOR));
        txtDescription.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        scrollPane.setViewportView(txtDescription);
        txtDescription.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtDescription.setForeground(Color.BLACK);
                txtDescription.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                validateDescription();
                txtDescription.setForeground(Color.decode(ColorCodes.SECONDARY_TEXT_COLOR));
                txtDescription.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
            }
        });

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

        btnCancelChanges = new JButton("<html><p align='justify'>Cancelar</p></html>");
        btnCancelChanges.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadOrderInfo();
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

        cbStatus = new JComboBox();
        cbStatus.setModel(new DefaultComboBoxModel(WorkOrderStatusEnum.values()));
        cbStatus.setBounds(190, 371, 175, 22);
        cbStatus.setVisible(false);
        cbStatus.setFocusable(false);
        contentPane.add(cbStatus);

    }
}
