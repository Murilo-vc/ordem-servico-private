package org.example.murilo.ordemservico;

import org.example.murilo.ordemservico.domain.constants.ColorCodes;
import org.example.murilo.ordemservico.domain.dto.ClientDto;
import org.example.murilo.ordemservico.service.ServerService;
import org.example.murilo.ordemservico.util.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ServerWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtPort;
    private JPanel clientsPane;
    private ServerService serverService;
    private JLabel lblPortErrorMessage;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnStartServer;

    public ServerWindow() {
        this.serverService = new ServerService(this);
        this.initComponents();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerWindow frame = new ServerWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateClientList(List<ClientDto> clients) {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            for (ClientDto c : clients) {
                tableModel.addRow(new Object[]{c.getIp(), c.getPort()});
            }
        });
    }

    private boolean validatePort() {
        try {
            final String port = this.txtPort.getText();
            if (StringUtils.isEmpty(port)) {
                this.lblPortErrorMessage.setText("<html><p align='justify'>Informe a porta!</p></html>");
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

    private void startServer() {
        if (!this.validatePort()) {
            return;
        }

        final int port = Integer.parseInt(this.txtPort.getText());
        this.txtPort.setEnabled(false);
        this.btnStartServer.setEnabled(false);
        this.clientsPane.setVisible(true);
        new Thread(() -> {
            this.serverService.startServer(port);
        }).start();
    }

    /**
     * Create the frame.
     */
    public void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblStartServer = new JLabel("<html><p align='justify'>Iniciar um servidor</p></html>");
        lblStartServer.setHorizontalAlignment(SwingConstants.CENTER);
        lblStartServer.setBounds(209, 10, 166, 25);
        lblStartServer.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblStartServer.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(lblStartServer);

        JPanel startServerPane = new JPanel();
        startServerPane.setBorder(new TitledBorder(
            null,
            "Iniciar Servidor",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Tahoma", Font.PLAIN, 16),
            Color.decode(ColorCodes.PRIMARY_TEXT_COLOR)
        ));
        startServerPane.setBounds(29, 53, 522, 105);
        startServerPane.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        startServerPane.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(startServerPane);
        startServerPane.setLayout(null);

        txtPort = new JTextField();
        txtPort.setBounds(108, 45, 175, 20);
        startServerPane.add(txtPort);
        txtPort.setColumns(10);
        txtPort.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        txtPort.setForeground(Color.decode(ColorCodes.SECONDARY_TEXT_COLOR));
        txtPort.setBorder(null);
        txtPort.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtPort.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtPort.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
            }
        });

        JLabel lblPort = new JLabel("<html><p align='justify'>Porta:</p></html>");
        lblPort.setBounds(28, 41, 70, 25);
        startServerPane.add(lblPort);
        lblPort.setHorizontalAlignment(SwingConstants.TRAILING);
        lblPort.setForeground(new Color(224, 224, 224));
        lblPort.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JSeparator separatorPort = new JSeparator();
        separatorPort.setBounds(108, 64, 175, 8);
        startServerPane.add(separatorPort);
        separatorPort.setForeground(new Color(136, 136, 136));
        separatorPort.setBackground(new Color(136, 136, 136));

        btnStartServer = new JButton("<html><p align='justify'>Carregar</p></html>");
        btnStartServer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startServer();
            }
        });
        btnStartServer.setBounds(366, 44, 89, 23);
        btnStartServer.setBackground(Color.decode(ColorCodes.ACCENT_COLOR));
        btnStartServer.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        btnStartServer.setFocusable(false);
        startServerPane.add(btnStartServer);

        lblPortErrorMessage = new JLabel("");
        lblPortErrorMessage.setForeground(Color.decode(ColorCodes.ERROR_MESSAGE_COLOR));
        lblPortErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblPortErrorMessage.setBounds(108, 64, 201, 36);
        startServerPane.add(lblPortErrorMessage);

        clientsPane = new JPanel();
        clientsPane.setBounds(29, 189, 522, 372);
        clientsPane.setBorder(new TitledBorder(
            null,
            "Clientes Conectados",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            new Font("Tahoma", Font.PLAIN, 16),
            Color.decode(ColorCodes.PRIMARY_TEXT_COLOR)
        ));
        clientsPane.setBackground(Color.decode(ColorCodes.BACKGROUND_COLOR));
        clientsPane.setForeground(Color.decode(ColorCodes.PRIMARY_TEXT_COLOR));
        contentPane.add(clientsPane);
        clientsPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(24, 29, 475, 309);
        clientsPane.add(scrollPane);

        this.tableModel = new DefaultTableModel(new Object[]{"IP", "Porta"}, 0);
        table = new JTable(tableModel);
        scrollPane.setViewportView(table);
        clientsPane.setVisible(false);
    }
}
