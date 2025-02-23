package gui;

import models.AccessCard;
import services.AccessControlSystem;
import services.AuditTrail;
import services.CardManagement;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AccessControlGUI {
    private final JTextField cardIdField;
    private final JTextField accessField;
    private final JTextField daysField;
    private final JTextArea outputArea;

    public AccessControlGUI() {
        JFrame frame = new JFrame("Access Control System");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));

        JLabel cardIdLabel = new JLabel("Card ID:");
        cardIdField = new JTextField();

        JLabel accessLabel = new JLabel("Access Levels : (Low,Room101)");
        accessField = new JTextField();

        JLabel daysLabel = new JLabel("Valid for (days):");
        daysField = new JTextField();

        JButton addButton = new JButton("Add");
        JButton modifyButton = new JButton("Modify");
        JButton revokeButton = new JButton("Revoke");
        JButton checkButton = new JButton("Check Access");
        JButton showButton = new JButton("Show Cards");
        JButton auditButton = new JButton("Audit Logs");

        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        inputPanel.add(cardIdLabel);
        inputPanel.add(cardIdField);
        inputPanel.add(accessLabel);
        inputPanel.add(accessField);
        inputPanel.add(daysLabel);
        inputPanel.add(daysField);

        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(revokeButton);
        buttonPanel.add(checkButton);
        buttonPanel.add(showButton);
        buttonPanel.add(auditButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        addButton.addActionListener(_ -> addNewCard());
        modifyButton.addActionListener(_ -> modifyCard());
        revokeButton.addActionListener(_ -> revokeCard());
        checkButton.addActionListener(_ -> verifyAccess());
        showButton.addActionListener(_ -> showAllCards());
        auditButton.addActionListener(_ -> showAuditLogs());

        frame.setVisible(true);
    }

    private void addNewCard() {
        String cardId = cardIdField.getText();
        int days = Integer.parseInt(daysField.getText());
        List<String> accessLevels = Arrays.asList(accessField.getText().split(","));

        CardManagement.addCard(cardId, LocalDateTime.now().plusDays(days), accessLevels);
        outputArea.setText("Card added successfully!");
    }

    private void modifyCard() {
        String cardId = cardIdField.getText();
        int days = Integer.parseInt(daysField.getText());
        List<String> accessLevels = Arrays.asList(accessField.getText().split(","));

        CardManagement.modifyCard(cardId, LocalDateTime.now().plusDays(days), accessLevels);
        outputArea.setText("Card modified successfully!");
    }

    private void revokeCard() {
        String cardId = cardIdField.getText();
        CardManagement.revokeCard(cardId);
        outputArea.setText("Card revoked successfully!");
    }

    private void verifyAccess() {
        String cardId = cardIdField.getText();
        AccessCard card = CardManagement.getCard(cardId);
        if (card == null) {
            outputArea.setText("Card not found!");
            return;
        }

        String location = JOptionPane.showInputDialog("Enter location (e.g., LOW or ROOM101):");
        boolean isRoom = JOptionPane.showConfirmDialog(null, "Is this a room?", "Check Access", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        AccessControlSystem.verifyAccess(card, location, isRoom);
        outputArea.setText("Access checked. Check console for details.");
    }

    private void showAllCards() {
        StringBuilder sb = new StringBuilder("All Access Cards:\n");
        Map<String, AccessCard> cards = CardManagement.getAllCards();

        for (Map.Entry<String, AccessCard> entry : cards.entrySet()) {
            AccessCard card = entry.getValue();
            long daysRemaining = java.time.Duration.between(LocalDateTime.now(), card.getValidUntil()).toDays();
            String accessLevels = String.join(", ", card.getAccessLevels());
            sb.append("Card ID: ").append(card.getCardId())
                    .append(" | Access Levels: ").append(accessLevels)
                    .append(" | Days Remaining: ").append(daysRemaining)
                    .append("\n");
        }
        outputArea.setText(sb.toString());
    }

    private void showAuditLogs() {
        List<String> logs = AuditTrail.getLogs();
        if (logs.isEmpty()) {
            outputArea.setText("No audit logs available.");
        } else {
            StringBuilder sb = new StringBuilder("Audit Logs:\n");
            for (String log : logs) {
                sb.append(log).append("\n");
            }
            outputArea.setText(sb.toString());
        }
    }


    public static void main(String[] args) {
        new AccessControlGUI();
    }
}
