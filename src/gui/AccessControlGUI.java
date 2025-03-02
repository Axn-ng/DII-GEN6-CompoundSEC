package gui;

import models.AccessCard;
import services.AuditTrail;
import services.CardManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class AccessControlGUI {
    private JTextField cardIdField, accessField, daysField, startTimeField, endTimeField;
    private JTable outputTable;
    private DefaultTableModel tableModel;
    private JFrame frame;

    public AccessControlGUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        frame = new JFrame("Access Control System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setSize(650, 500);

        // 🌟 Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Card Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Card ID:", "Access Levels:", "Valid for (days):", "Allowed Start Time:", "Allowed End Time:"};
        JTextField[] fields = {cardIdField = new JTextField(15), accessField = new JTextField(15),
                daysField = new JTextField(10), startTimeField = new JTextField(10), endTimeField = new JTextField(10)};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            inputPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            inputPanel.add(fields[i], gbc);
        }

        // 🌟 Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        JButton addButton = new JButton("➕ Add");
        JButton modifyButton = new JButton("✏ Modify");
        JButton revokeButton = new JButton("❌ Revoke");
        JButton checkButton = new JButton("🔍 Check Access");
        JButton showButton = new JButton("📋 Show Cards");
        JButton auditButton = new JButton("📜 Audit Logs");

        for (JButton btn : new JButton[]{addButton, modifyButton, revokeButton, checkButton, showButton, auditButton})
            buttonPanel.add(btn);

        // 🌟 Output Table
        tableModel = new DefaultTableModel(new String[]{"Card ID", "Access Levels", "Days Left", "Allowed Time"}, 0);
        outputTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(outputTable);
        scrollPane.setPreferredSize(new Dimension(600, 150));

        // 🌟 Add Panels to Frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        // 🌟 Event Listeners (ตอนนี้ทุกปุ่มสามารถกดได้!)
        addButton.addActionListener(_ -> addNewCard());
        modifyButton.addActionListener(_ -> modifyCard());
        revokeButton.addActionListener(_ -> revokeCard());
        checkButton.addActionListener(_ -> verifyAccess());
        showButton.addActionListener(_ -> showAllCards());
        auditButton.addActionListener(_ -> showAuditLogs());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addNewCard() {
        String cardId = cardIdField.getText();
        int days = Integer.parseInt(daysField.getText());
        List<String> accessLevels = Arrays.asList(accessField.getText().split(","));

        LocalDateTime validUntil = LocalDateTime.now().plusDays(days);

        try {
            LocalTime startTime = LocalTime.parse(startTimeField.getText());
            LocalTime endTime = LocalTime.parse(endTimeField.getText());

            CardManagement.addCard(cardId, validUntil, accessLevels, startTime, endTime);
            JOptionPane.showMessageDialog(frame, "Card added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid time format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifyCard() {
        String cardId = cardIdField.getText();
        if (!CardManagement.getAllCards().containsKey(cardId)) {
            JOptionPane.showMessageDialog(frame, "Card not found!");
            return;
        }

        int days = Integer.parseInt(daysField.getText());
        List<String> accessLevels = Arrays.asList(accessField.getText().split(","));
        LocalDateTime validUntil = LocalDateTime.now().plusDays(days);

        try {
            LocalTime startTime = LocalTime.parse(startTimeField.getText());
            LocalTime endTime = LocalTime.parse(endTimeField.getText());

            CardManagement.modifyCard(cardId, validUntil, accessLevels, startTime, endTime);
            JOptionPane.showMessageDialog(frame, "Card modified successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid time format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void revokeCard() {
        String cardId = cardIdField.getText();
        if (CardManagement.getCard(cardId) == null) {
            JOptionPane.showMessageDialog(frame, "Card not found!");
            return;
        }
        CardManagement.revokeCard(cardId);
        JOptionPane.showMessageDialog(frame, "Card revoked successfully!");
    }

    private void verifyAccess() {
        String cardId = cardIdField.getText().trim();
        AccessCard card = CardManagement.getCard(cardId);

        if (card == null) {
            JOptionPane.showMessageDialog(frame, "❌ Card not found!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ ให้ผู้ใช้ป้อนสถานที่ที่ต้องการตรวจสอบ
        String location = JOptionPane.showInputDialog(frame, "Enter location (e.g., Low Floor, Room 101):");

        if (location == null || location.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "❌ Invalid location!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ ตรวจสอบว่าบัตรหมดอายุหรือไม่
        if (!card.isValid()) {
            JOptionPane.showMessageDialog(frame, "❌ Access Denied: Card Expired!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ ตรวจสอบว่าบัตรใช้ในช่วงเวลาที่อนุญาตได้หรือไม่
        if (!card.isWithinAllowedTime()) {
            JOptionPane.showMessageDialog(frame, "❌ Access Denied: Out of Allowed Hours!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ ตรวจสอบว่าสถานที่อยู่ใน Access Levels หรือไม่
        boolean hasAccess = card.getAccessLevels().contains(location);

        if (hasAccess) {
            JOptionPane.showMessageDialog(frame, "✅ Access Granted to " + location, "Access Approved", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "❌ Access Denied to " + location, "Access Denied", JOptionPane.ERROR_MESSAGE);
        }

        // ✅ บันทึกลง AuditTrail
        AuditTrail.logAccess(cardId, List.of(location), hasAccess);
    }

    private void showAllCards() {
        tableModel.setRowCount(0); // เคลียร์ข้อมูลเดิมก่อนแสดงใหม่

        for (AccessCard card : CardManagement.getAllCards().values()) {
            // ✅ ใช้จำนวนวันที่ผู้ใช้กรอก ไม่แปลงเป็นวันหมดอายุ
            long daysValid = java.time.Duration.between(LocalDateTime.now(), card.getValidUntil()).toDays();

            String formattedStartTime = (card.getAllowedStartTime() != null) ? card.getAllowedStartTime().toString() : "N/A";
            String formattedEndTime = (card.getAllowedEndTime() != null) ? card.getAllowedEndTime().toString() : "N/A";

            tableModel.addRow(new Object[]{
                    card.getCardId(),
                    String.join(", ", card.getAccessLevels()),
                    daysValid + " Days", // ✅ แสดงจำนวนวันแทนวันที่หมดอายุ
                    formattedStartTime + " - " + formattedEndTime
            });
        }
    }


    private void showAuditLogs() {
        JOptionPane.showMessageDialog(frame, String.join("\n", AuditTrail.getLogs()));
    }

    public static void main(String[] args) {
        new AccessControlGUI();
    }
}
