package org.example.murilo.ordemservico.gui;

import org.example.murilo.ordemservico.domain.constants.ColorCodes;
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

public class NewWorkOrderWindow extends JDialog {

    private static final long serialVersionUID = 1L;
    private final ClientService clientService;
    private final HomeWindow homeWindow;
    private final String token;
    private JPanel contentPane;
    private JLabel lblDescriptionErrorMessage;
    private JButton btnVoltar;
    private JTextArea txtDescription;
    private JScrollPane scrollPane;

    public NewWorkOrderWindow(final ClientService clientService,
                              final HomeWindow homeWindow,
                              final String token) {
        this.clientService = clientService;
        this.homeWindow = homeWindow;
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
                    NewWorkOrderWindow frame = new NewWorkOrderWindow(null, null, "token");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    private void createOrder() {
        try {
            if (!validateDescription()) {
                return;
            }
            final String description = this.txtDescription.getText();

            this.clientService.createWorkOrder(this.token, description);
            JOptionPane.showMessageDialog(null, "Ordem criada com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);

            this.homeWindow.populateTable();
            this.setVisible(false);
            this.dispose();
        } catch (BaseException e) {
            JOptionPane.showMessageDialog(null, "Um erro ocorreu durante a operação: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Um erro ocorreu: " + e.getMessage());
        }
    }

    /**
     * Create the frame.
     */
    public void initComponents() {
        setBounds(100, 100, 642, 400);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(this.homeWindow);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewOrder = new JLabel("Nova Ordem");
        lblNewOrder.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewOrder.setBounds(219, 11, 187, 25);
        lblNewOrder.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblNewOrder.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(lblNewOrder);

        JLabel lblDescription = new JLabel("Descrição:");
        lblDescription.setHorizontalAlignment(SwingConstants.CENTER);
        lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblDescription.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        lblDescription.setBounds(260, 74, 106, 25);
        contentPane.add(lblDescription);

        JButton btnCreate = new JButton("Criar");
        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createOrder();
            }
        });
        btnCreate.setBounds(245, 277, 136, 36);
        btnCreate.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnCreate.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnCreate.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnCreate.setEnabled(true);
        contentPane.add(btnCreate);

        JLabel lblNameInstructions = new JLabel("<html><p align='justify'>Descrição deve ter entre 3 e 150 caracteres</p></html>");
        lblNameInstructions.setHorizontalAlignment(SwingConstants.CENTER);
        lblNameInstructions.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNameInstructions.setBounds(161, 202, 304, 36);
        lblNameInstructions.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(lblNameInstructions);

        lblDescriptionErrorMessage = new JLabel("");
        lblDescriptionErrorMessage.setForeground(Color.decode(ColorCodes.ERROR_MESSAGE_COLOR));
        lblDescriptionErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblDescriptionErrorMessage.setBounds(493, 98, 123, 107);
        contentPane.add(lblDescriptionErrorMessage);

        btnVoltar = new JButton("<html><p align='justify'>Voltar</p></html>");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnVoltar.setBounds(10, 11, 89, 23);
        btnVoltar.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnVoltar.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnVoltar.setFocusable(false);
        contentPane.add(btnVoltar);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(135, 98, 356, 107);
        contentPane.add(scrollPane);

        txtDescription = new JTextArea();
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
    }
}
