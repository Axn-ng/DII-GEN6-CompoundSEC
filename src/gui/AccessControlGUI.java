package gui;

import models.AccessCard;
import services.AuditTrail;
import services.CardManagement;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AccessControlGUI {
    private JTextField cardIdField;
    private JTextField accessField;
    private JTextField daysField;
    private JTextArea outputArea;
    private JFrame frame;  // ประกาศ frame ที่นี่
    private JTextField startTimeField;
    private JTextField endTimeField;

    public AccessControlGUI() {
        frame = new JFrame("Access Control System");
        frame.setSize(550, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout()); // ใช้ GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // ระยะห่างระหว่างช่อง
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel cardIdLabel = new JLabel("Card ID: ");
        cardIdField = new JTextField(15);

        JLabel accessLabel = new JLabel("Access Levels: ");
        accessField = new JTextField(15);

        JLabel daysLabel = new JLabel("Valid for (days): ");
        daysField = new JTextField(15);

        JLabel startTimeLabel = new JLabel("Allowed Start Time (HH:mm): ");
        startTimeField = new JTextField(15);

        JLabel endTimeLabel = new JLabel("Allowed End Time (HH:mm): ");
        endTimeField = new JTextField(15);

        // จัดช่องให้เรียงกันสวยงาม
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(cardIdLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(cardIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(accessLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(accessField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(daysLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(daysField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(startTimeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(startTimeField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(endTimeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(endTimeField, gbc);

        // สร้างปุ่มต่างๆ
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        JButton addButton = new JButton("Add");
        JButton modifyButton = new JButton("Modify");
        JButton revokeButton = new JButton("Revoke");
        JButton checkButton = new JButton("Check Access");
        JButton showButton = new JButton("Show Cards");
        JButton auditButton = new JButton("Audit Logs");

        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(revokeButton);
        buttonPanel.add(checkButton);
        buttonPanel.add(showButton);
        buttonPanel.add(auditButton);

        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        // เพิ่ม ActionListener สำหรับปุ่ม
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

        LocalDateTime validUntil = LocalDateTime.now().plusDays(days);

        LocalTime startTime;
        LocalTime endTime;
        try {
            startTime = LocalTime.parse(startTimeField.getText());
            endTime = LocalTime.parse(endTimeField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid time format! Please use HH:mm (e.g., 08:00)");
            return;
        }

        CardManagement.addCard(cardId, validUntil, accessLevels, startTime, endTime);

        JOptionPane.showMessageDialog(frame, "Card added successfully with time-based access!");
    }

    private void modifyCard() {
        String cardId = cardIdField.getText().trim();
        if (cardId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Card ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int days;
        try {
            days = Integer.parseInt(daysField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid number of days!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> accessLevels = Arrays.asList(accessField.getText().trim().split(","));

        LocalDateTime newValidUntil = LocalDateTime.now().plusDays(days);

        LocalTime newStartTime;
        LocalTime newEndTime;
        try {
            newStartTime = LocalTime.parse(startTimeField.getText().trim());
            newEndTime = LocalTime.parse(endTimeField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid time format! Use HH:mm (e.g., 08:00)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ ส่งค่าเวลาไปที่ `modifyCard()`
        CardManagement.modifyCard(cardId, newValidUntil, accessLevels, newStartTime, newEndTime);

        JOptionPane.showMessageDialog(frame, "Card modified successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }


    private void revokeCard() {
        String cardId = cardIdField.getText();
        CardManagement.revokeCard(cardId);
        JOptionPane.showMessageDialog(frame, "Card revoked successfully!");
    }


    private void verifyAccess() {
        String cardId = cardIdField.getText().trim();
        AccessCard card = CardManagement.getCard(cardId);

        if (card == null) {
            JOptionPane.showMessageDialog(frame, "Card not found!");
            return;
        }

        String location = JOptionPane.showInputDialog("Enter location (e.g., Low Floor, Room 101):");

        if (!card.isValid()) {
            JOptionPane.showMessageDialog(frame, "Access Denied: Card Expired!");
            return;
        }

        if (!card.isWithinAllowedTime()) {
            JOptionPane.showMessageDialog(frame, "Access Denied: Out of Allowed Hours!");
            return;
        }

        boolean hasAccess = card.getAccessLevels().contains(location);

        if (hasAccess) {
            JOptionPane.showMessageDialog(frame, "Access Granted to " + location);
        } else {
            JOptionPane.showMessageDialog(frame, "Access Denied to " + location);
        }

        AuditTrail.logAccess(cardId, List.of(location), hasAccess);  // Log the access information to AuditTrail
    }

    private boolean verifyAccess(AccessCard card, String location) {
        List<String> accessLevels = card.getAccessLevels();
        return accessLevels.contains(location);
    }

    private void showAllCards() {
        StringBuilder sb = new StringBuilder("All Access Cards:\n");
        Map<String, AccessCard> cards = CardManagement.getAllCards();

        if (cards.isEmpty()) {
            outputArea.setText("No access cards available.");
            return;
        }

        for (Map.Entry<String, AccessCard> entry : cards.entrySet()) {
            AccessCard card = entry.getValue();
            long daysRemaining = java.time.Duration.between(LocalDateTime.now(), card.getValidUntil()).toDays();
            String accessLevels = String.join(", ", card.getAccessLevels());

            // ✅ เช็คว่าค่าเวลามีค่าหรือไม่ ถ้าไม่มีให้แสดง "N/A"
            String startTime = (card.getAllowedStartTime() != null) ? card.getAllowedStartTime().toString() : "N/A";
            String endTime = (card.getAllowedEndTime() != null) ? card.getAllowedEndTime().toString() : "N/A";

            sb.append("Card ID: ").append(card.getCardId())
                    .append(" | Access Levels: ").append(accessLevels)
                    .append(" | Days Remaining: ").append(daysRemaining)
                    .append(" | Allowed Time: ").append(startTime).append(" - ").append(endTime)
                    .append("\n");
        }
        outputArea.setText(sb.toString());
    }


    private void showAuditLogs() {

        if (AuditTrail.getLogs().isEmpty()) {
            outputArea.setText("No audit logs available.");
        } else {

            StringBuilder sb = new StringBuilder("Audit Logs:\n");
            for (String log : AuditTrail.getLogs()) {
                sb.append(log).append("\n");
            }
            outputArea.setText(sb.toString());
        }
    }

    public static void main(String[] args) {
        new AccessControlGUI();
    }
}
