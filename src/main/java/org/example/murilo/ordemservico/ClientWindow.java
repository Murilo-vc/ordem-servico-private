package org.example.murilo.ordemservico;

import org.example.murilo.ordemservico.domain.constants.ColorCodes;
import org.example.murilo.ordemservico.gui.LoginWindow;
import org.example.murilo.ordemservico.service.ClientService;
import org.example.murilo.ordemservico.util.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.UnknownHostException;

public class ClientWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtIp;
    private JLabel lblIpErrorMessage;
    private JTextField txtPort;
    private JLabel lblPortErrorMessage;

    /**
     * Create the frame.
     */
    public ClientWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 642, 510);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));

        JLabel lblLogin = new JLabel("<html><p align='justify'>Conecte-se a um Servidor</p></html>");
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogin.setBounds(162, 11, 301, 28);
        lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblLogin.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(lblLogin);

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblIp = new JLabel("<html><p align='justify'>IP:</p></html>");
        lblIp.setHorizontalAlignment(SwingConstants.TRAILING);
        lblIp.setForeground(new Color(224, 224, 224));
        lblIp.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblIp.setBounds(145, 110, 70, 25);
        contentPane.add(lblIp);

        txtIp = new JTextField();
        txtIp.setColumns(10);
        txtIp.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        txtIp.setForeground(Color.decode(ColorCodes.SECONDARY_TEXT_COLOR));
        txtIp.setBorder(null);
        txtIp.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtIp.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                validateIp();
                txtIp.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
            }
        });
        txtIp.setBounds(225, 114, 175, 20);
        contentPane.add(txtIp);

        JSeparator separatorIp = new JSeparator();
        separatorIp.setForeground(new Color(136, 136, 136));
        separatorIp.setBackground(new Color(136, 136, 136));
        separatorIp.setBounds(225, 133, 175, 8);
        contentPane.add(separatorIp);

        lblIpErrorMessage = new JLabel("");
        lblIpErrorMessage.setForeground(Color.decode(ColorCodes.ERROR_MESSAGE_COLOR));
        lblIpErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblIpErrorMessage.setBounds(410, 105, 201, 36);
        contentPane.add(lblIpErrorMessage);

        JLabel lblPort = new JLabel("<html><p align='justify'>Porta:</p></html>");
        lblPort.setHorizontalAlignment(SwingConstants.TRAILING);
        lblPort.setForeground(new Color(224, 224, 224));
        lblPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblPort.setBounds(145, 203, 70, 25);
        contentPane.add(lblPort);

        txtPort = new JTextField();
        txtPort.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        txtPort.setForeground(Color.decode(ColorCodes.SECONDARY_TEXT_COLOR));
        txtPort.setColumns(10);
        txtPort.setBorder(null);
        txtPort.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtPort.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                validatePort();
                txtPort.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
            }
        });
        txtPort.setBounds(225, 207, 175, 20);
        contentPane.add(txtPort);

        JSeparator separatorPort = new JSeparator();
        separatorPort.setForeground(new Color(136, 136, 136));
        separatorPort.setBackground(new Color(136, 136, 136));
        separatorPort.setBounds(225, 226, 175, 8);
        contentPane.add(separatorPort);

        JButton btnLogin = new JButton("Conectar");
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                connectToServer();
            }
        });
        btnLogin.setBounds(245, 354, 136, 36);
        btnLogin.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnLogin.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnLogin.setEnabled(true);
        contentPane.add(btnLogin);

        lblPortErrorMessage = new JLabel("");
        lblPortErrorMessage.setForeground(Color.RED);
        lblPortErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblPortErrorMessage.setBounds(410, 192, 201, 36);
        contentPane.add(lblPortErrorMessage);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientWindow frame = new ClientWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validateIp() {
        final String ip = this.txtIp.getText();
        if (StringUtils.isEmpty(ip)) {
            this.lblIpErrorMessage.setText("<html><p align='justify'>Campo Obrigatório</p></html>");
            return false;
        }
        this.lblIpErrorMessage.setText("");
        return true;
    }

    private boolean validatePort() {
        try {
            final String port = this.txtPort.getText();
            if (StringUtils.isEmpty(port)) {
                this.lblPortErrorMessage.setText("<html><p align='justify'>Campo Obrigatório</p></html>");
                return false;
            }
            Integer.parseInt(port);
            this.lblPortErrorMessage.setText("");
            return true;
        } catch (NumberFormatException e) {
            this.lblPortErrorMessage.setText("<html><p align='justify'>Valor Inválido!</p></html>");
            return false;
        }
    }

    private void connectToServer() {
        if (!validatePort() | !validateIp()) {
            return;
        }

        final String ip = this.txtIp.getText();
        final int port = Integer.parseInt(this.txtPort.getText());
        try {
            final ClientService clientService = new ClientService();
            clientService.connectServer(ip, port);
            final LoginWindow lw = new LoginWindow(clientService);
            lw.setVisible(true);
            dispose();
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "Host " + ip + " nao encontrado!", "Host desconhecido", JOptionPane.ERROR_MESSAGE);
            System.err.println("Host " + ip + " nao encontrado!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel reservar I/O para conectar com " + ip, "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Não foi possivel reservar I/O para conectar com " + ip);
        }
    }
}
