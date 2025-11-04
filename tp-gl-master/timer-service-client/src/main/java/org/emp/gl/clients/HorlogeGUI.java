package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class HorlogeGUI extends JFrame implements TimerChangeListener {
    
    
    private static final Color PRIMARY_BG = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(66, 133, 244);
    private static final Color SECONDARY_COLOR = new Color(52, 168, 83);
    private static final Color WARNING_COLOR = new Color(251, 188, 5);
    private static final Color ERROR_COLOR = new Color(234, 67, 53);
    private static final Color TEXT_PRIMARY = new Color(32, 33, 36);
    private static final Color TEXT_SECONDARY = new Color(95, 99, 104);
    
    private TimerService timerService;
    private List<CountdownTimer> countdownTimers;
    
    
    private JLabel digitalTimeLabel;
    private JLabel dateLabel;
    private JPanel countdownPanel;
    private JLabel activeTimersLabel;
    
    
    private JSpinner hoursSpinner;
    private JSpinner minutesSpinner;
    private JSpinner secondsSpinner;
    
    public HorlogeGUI(String title, TimerService timerService) {
        this.timerService = timerService;
        this.countdownTimers = new ArrayList<>();
        
        if (timerService != null) {
            timerService.addTimeChangeListener(this);
        }
        
        initializeGUI(title);
        updateTime();
    }
    
    private void initializeGUI(String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(PRIMARY_BG);
        setResizable(true);
        
        // Apply system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fall back to default look and feel
        }
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(PRIMARY_BG);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Header
        mainPanel.add(createHeaderPanel(title), BorderLayout.NORTH);
        
        // Center - Time display
        mainPanel.add(createTimeDisplayPanel(), BorderLayout.CENTER);
        
        // Bottom - Countdown section
        mainPanel.add(createCountdownSection(), BorderLayout.SOUTH);
        
        // Right - Control panel
        add(createControlPanel(), BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);
        
        // Window listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });
        
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }
    
    private JPanel createHeaderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_BG);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("Gestionnaire de Temps Moderne");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(subtitleLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTimeDisplayPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PRIMARY_BG);
        
        JPanel timeCard = createCard();
        timeCard.setLayout(new BorderLayout(0, 10));
        
        // Digital time display
        digitalTimeLabel = new JLabel("00:00:00.0", SwingConstants.CENTER);
        digitalTimeLabel.setFont(new Font("SF Mono", Font.BOLD, 48));
        digitalTimeLabel.setForeground(ACCENT_COLOR);
        
        // Date display
        dateLabel = new JLabel("Chargement...", SwingConstants.CENTER);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateLabel.setForeground(TEXT_SECONDARY);
        
        timeCard.add(digitalTimeLabel, BorderLayout.CENTER);
        timeCard.add(dateLabel, BorderLayout.SOUTH);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(timeCard, gbc);
        
        return panel;
    }
    
    private JPanel createCountdownSection() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PRIMARY_BG);
        
        // Header with stats
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_BG);
        
        JLabel titleLabel = new JLabel("Compteurs à Rebours");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        activeTimersLabel = new JLabel("0 compteur(s) actif(s)");
        activeTimersLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        activeTimersLabel.setForeground(TEXT_SECONDARY);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(activeTimersLabel, BorderLayout.EAST);
        
        // Countdown list
        countdownPanel = new JPanel();
        countdownPanel.setLayout(new BoxLayout(countdownPanel, BoxLayout.Y_AXIS));
        countdownPanel.setBackground(PRIMARY_BG);
        
        JScrollPane scrollPane = new JScrollPane(countdownPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(218, 220, 224), 1),
            new EmptyBorder(20, 15, 20, 15)
        ));
        panel.setPreferredSize(new Dimension(250, 0));
        
        JLabel titleLabel = new JLabel("Nouveau Compteur");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Time input panel
        JPanel inputPanel = createInputPanel();
        
        // Add button
        JButton addButton = createPrimaryButton("Ajouter le Compteur");
        addButton.addActionListener(e -> addCountdownTimerFromInput());
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(inputPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(addButton);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(CARD_BG);
        
        // Create spinners with number-only input
        hoursSpinner = createNumberSpinner(0, 0, 23);
        minutesSpinner = createNumberSpinner(0, 0, 59);
        secondsSpinner = createNumberSpinner(0, 0, 59);
        
        panel.add(createInputLabel("Heures:"));
        panel.add(hoursSpinner);
        panel.add(createInputLabel("Minutes:"));
        panel.add(minutesSpinner);
        panel.add(createInputLabel("Secondes:"));
        panel.add(secondsSpinner);
        
        return panel;
    }
    
    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    private JSpinner createNumberSpinner(int value, int min, int max) {
        SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, 1);
        JSpinner spinner = new JSpinner(model);
        
        // Make the editor only accept numbers
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#");
        spinner.setEditor(editor);
        
        // Get the text field and add key listener to prevent non-digit input
        JFormattedTextField textField = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });
        
        spinner.setPreferredSize(new Dimension(80, 35));
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        return spinner;
    }
    
    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_COLOR.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
        });
        
        return button;
    }
    
    private JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(218, 220, 224), 1),
            new EmptyBorder(20, 30, 20, 30)
        ));
        return card;
    }
    
    private void addCountdownTimerFromInput() {
        int hours = (Integer) hoursSpinner.getValue();
        int minutes = (Integer) minutesSpinner.getValue();
        int seconds = (Integer) secondsSpinner.getValue();
        
        if (hours > 0 || minutes > 0 || seconds > 0) {
            addCountdownTimer(hours, minutes, seconds);
            // Reset spinners
            hoursSpinner.setValue(0);
            minutesSpinner.setValue(0);
            secondsSpinner.setValue(0);
        } else {
            showErrorDialog("Veuillez entrer une durée valide (au moins 1 seconde).");
        }
    }
    
    private void addCountdownTimer(int hours, int minutes, int seconds) {
        CountdownTimer timer = new CountdownTimer(hours, minutes, seconds);
        countdownTimers.add(timer);
        
        JPanel timerCard = createTimerCard(timer);
        countdownPanel.add(timerCard);
        countdownPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        updateTimersCount();
        countdownPanel.revalidate();
        countdownPanel.repaint();
    }
    
    private JPanel createTimerCard(CountdownTimer timer) {
        JPanel card = createCard();
        card.setLayout(new BorderLayout(15, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        // Time display
        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("SF Mono", Font.BOLD, 24));
        timer.setDisplayLabel(timeLabel);
        updateTimerDisplay(timer);
        
        // Progress bar
        JProgressBar progressBar = new JProgressBar(0, timer.getInitialTenths());
        progressBar.setValue(timer.getRemainingTenths());
        progressBar.setForeground(SECONDARY_COLOR);
        progressBar.setBackground(new Color(241, 243, 244));
        progressBar.setPreferredSize(new Dimension(0, 6));
        
        // Time and progress panel
        JPanel timePanel = new JPanel(new BorderLayout(0, 5));
        timePanel.setBackground(CARD_BG);
        timePanel.add(timeLabel, BorderLayout.CENTER);
        timePanel.add(progressBar, BorderLayout.SOUTH);
        
        // Remove button
        JButton removeButton = createIconButton("×", ERROR_COLOR);
        removeButton.addActionListener(e -> removeTimer(timer, card));
        
        card.add(timePanel, BorderLayout.CENTER);
        card.add(removeButton, BorderLayout.EAST);
        
        timer.setProgressBar(progressBar);
        
        return card;
    }
    
    private JButton createIconButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(color);
        button.setBackground(CARD_BG);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(241, 243, 244));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(CARD_BG);
            }
        });
        
        return button;
    }
    
    private void removeTimer(CountdownTimer timer, JPanel card) {
        countdownTimers.remove(timer);
        countdownPanel.remove(card);
        // Remove the rigid area as well
        int cardIndex = countdownPanel.getComponentZOrder(card);
        if (cardIndex >= 0 && cardIndex < countdownPanel.getComponentCount() - 1) {
            countdownPanel.remove(cardIndex + 1);
        }
        updateTimersCount();
        countdownPanel.revalidate();
        countdownPanel.repaint();
    }
    
    private void updateTimerDisplay(CountdownTimer timer) {
        if (timer.isFinished()) {
            timer.getDisplayLabel().setText("TERMINÉ !");
            timer.getDisplayLabel().setForeground(ERROR_COLOR);
            if (timer.getProgressBar() != null) {
                timer.getProgressBar().setForeground(ERROR_COLOR);
                timer.getProgressBar().setValue(0);
            }
        } else {
            int remaining = timer.getRemainingTenths();
            int hours = remaining / 36000;
            int minutes = (remaining % 36000) / 600;
            int seconds = (remaining % 600) / 10;
            int tenths = remaining % 10;
            
            String timeStr = String.format("%02d:%02d:%02d.%d", hours, minutes, seconds, tenths);
            timer.getDisplayLabel().setText(timeStr);
            
            Color textColor = SECONDARY_COLOR;
            Color progressColor = SECONDARY_COLOR;
            
            if (remaining < 300) { // 30 seconds
                textColor = WARNING_COLOR;
                progressColor = WARNING_COLOR;
            }
            if (remaining < 100) { // 10 seconds
                textColor = ERROR_COLOR;
                progressColor = ERROR_COLOR;
            }
            
            timer.getDisplayLabel().setForeground(textColor);
            if (timer.getProgressBar() != null) {
                timer.getProgressBar().setForeground(progressColor);
                timer.getProgressBar().setValue(remaining);
            }
        }
    }
    
    private void updateTimersCount() {
        long activeCount = countdownTimers.stream().filter(t -> !t.isFinished()).count();
        activeTimersLabel.setText(activeCount + " compteur(s) actif(s)");
    }
    
    private void updateTime() {
        if (timerService != null) {
            SwingUtilities.invokeLater(() -> {
                // Update digital time
                String timeString = String.format("%02d:%02d:%02d.%d",
                    timerService.getHeures(),
                    timerService.getMinutes(),
                    timerService.getSecondes(),
                    timerService.getDixiemeDeSeconde());
                digitalTimeLabel.setText(timeString);
                
                // Update date
                dateLabel.setText(java.time.LocalDate.now().toString());
            });
        }
    }
    
    @Override
    public void propertyChange(String prop, Object oldValue, Object newValue) {
        updateTime();
        
        if (TimerChangeListener.DIXEME_DE_SECONDE_PROP.equals(prop)) {
            SwingUtilities.invokeLater(() -> {
                boolean needsUpdate = false;
                
                for (CountdownTimer timer : countdownTimers) {
                    if (!timer.isFinished()) {
                        timer.decrement();
                        updateTimerDisplay(timer);
                        needsUpdate = true;
                    }
                }
                
                if (needsUpdate) {
                    updateTimersCount();
                }
            });
        }
    }
    
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Erreur de Saisie", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    public void disconnect() {
        if (timerService != null) {
            timerService.removeTimeChangeListener(this);
        }
    }
    
    public void showWindow() {
        setVisible(true);
    }
    
    /**
     * Inner class for countdown timer management
     */
    private static class CountdownTimer {
        private final int initialTenths;
        private int remainingTenths;
        private JLabel displayLabel;
        private JProgressBar progressBar;
        
        public CountdownTimer(int hours, int minutes, int seconds) {
            this.initialTenths = (hours * 3600 + minutes * 60 + seconds) * 10;
            this.remainingTenths = initialTenths;
        }
        
        public void decrement() {
            if (remainingTenths > 0) {
                remainingTenths--;
            }
        }
        
        public boolean isFinished() {
            return remainingTenths <= 0;
        }
        
        public int getRemainingTenths() {
            return remainingTenths;
        }
        
        public int getInitialTenths() {
            return initialTenths;
        }
        
        public JLabel getDisplayLabel() {
            return displayLabel;
        }
        
        public void setDisplayLabel(JLabel displayLabel) {
            this.displayLabel = displayLabel;
        }
        
        public JProgressBar getProgressBar() {
            return progressBar;
        }
        
        public void setProgressBar(JProgressBar progressBar) {
            this.progressBar = progressBar;
        }
    }
}