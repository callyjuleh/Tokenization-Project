import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.File;
import java.util.List;

public class TokenUI extends JFrame {
    public static Font header;
    public static Font body;
    public static Font bodyBold;

    public static Color DarkBlue = new Color(65, 112, 164);
    public static Color LightBlue = new Color(0xDDE6F0);
    public static Color LightOrange = new Color(255, 159, 66);
    public static Color DarkOrange = new Color(255, 126, 0);
    public static GradientPaint headerPaint = new GradientPaint(0, 0, new Color(0x2B4F7A), 1440, 0, new Color(0x1C3557));

    public JScrollPane scrollPane;
    public JPanel resultCardPanel;
    public JTextArea inputField;

    public Tokenizer tokenizer = new Tokenizer();
    private List<String> tokens;

    public TokenUI() {
        try { 
            header = Font.createFont(Font.TRUETYPE_FONT, new File("util/fonts/Inter_18pt-Bold.ttf"));
            header = header.deriveFont(Font.BOLD, 48);

            body = Font.createFont(Font.TRUETYPE_FONT, new File("util/fonts/Inter_18pt-Regular.ttf"));
            body = body.deriveFont(Font.PLAIN, 16);

            bodyBold = Font.createFont(Font.TRUETYPE_FONT, new File("util/fonts/Inter_18pt-Bold.ttf"));
            bodyBold = bodyBold.deriveFont(Font.BOLD, 16);

        } catch (Exception e) {
            e.printStackTrace();
        }
    
        setTitle("Tokenization Project");
        setSize(1440, 1024);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);  
        setLayout(new BorderLayout(0, 0));     

        JPanel topBar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(headerPaint);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };;
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.Y_AXIS));
        topBar.setBackground(DarkBlue);
        topBar.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Tokenization Project");
        title.setFont(header);
        title.setForeground(Color.WHITE);

        JLabel titleInfo = new JLabel("Turn commands into lexemes and tokens");
        titleInfo.setFont(body);
        titleInfo.setForeground(Color.WHITE);

        topBar.add(title);
        topBar.add(Box.createVerticalStrut(5));
        topBar.add(titleInfo);
        add(topBar, BorderLayout.NORTH);

        JPanel inputPanel = createInputPanel();
        JPanel outputPanel = createOutputPanel();

        add(inputPanel, BorderLayout.WEST);
        add(outputPanel, BorderLayout.CENTER);
        
        setVisible(true);        
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(DarkBlue);
        panel.setBorder(new EmptyBorder(20, 40, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(700, 0));

        JLabel inputTitle = new JLabel("Enter your command here");
        inputTitle.setFont(bodyBold);
        inputTitle.setForeground(Color.WHITE);
        inputTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputField = new JTextArea(5, 20);
        inputField.setFont(body);
        inputField.setForeground(Color.WHITE);
        inputField.setLineWrap(true);
        inputField.setWrapStyleWord(true);
        inputField.setOpaque(false);
        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane inputScroll = new JScrollPane(inputField);
        inputScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputScroll.setBorder(BorderFactory.createEmptyBorder());
        inputScroll.setOpaque(false);
        inputScroll.getViewport().setOpaque(false);
        
        JPanel roundedWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 100)); // semi-white
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        roundedWrapper.setOpaque(false);
        roundedWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        roundedWrapper.add(inputScroll);
        roundedWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);


        JPanel actionPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        actionPanel.setOpaque(false);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton clearBtn = createStyledButton("Clear");
        JButton analyzeBtn = createStyledButton("Analyze");

        actionPanel.add(Box.createGlue());
        actionPanel.add(Box.createGlue());
        actionPanel.add(clearBtn);
        actionPanel.add(analyzeBtn);

        panel.add(inputTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(roundedWrapper);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(actionPanel);

        clearBtn.addActionListener(e -> {
                resultCardPanel.removeAll();
                inputField.setText("");

                repaint();
                revalidate();
            }
        );
        analyzeBtn.addActionListener(e -> analyze());

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(DarkBlue);
        panel.setBorder(new EmptyBorder(20, 20, 20, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel outputTitle = new JLabel("Tokens and lexemes for the command");
        outputTitle.setFont(bodyBold);
        outputTitle.setForeground(Color.WHITE);
        outputTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        resultCardPanel = new JPanel();
        resultCardPanel.setOpaque(false);
        resultCardPanel.setLayout(new BoxLayout(resultCardPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(resultCardPanel);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        panel.add(outputTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(scrollPane);

        return panel;
    }

    private static JButton createStyledButton(String text) {
        JButton btn = new JButton(text) {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground().darker());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

                g2.dispose();
            }
        };

        btn.setFont(body);
        btn.setForeground(Color.WHITE);
        btn.setBackground(DarkOrange);

        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(DarkOrange.darker());
       
               btn.repaint();      }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(DarkOrange);
                btn.repaint();
            }
        });

        return btn;
    }

    private JPanel createResultCard(String lexeme, String token) {
        JPanel card = new JPanel(new GridLayout(1, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Rounded background
                g2.setColor(getBackground()); // subtle light background
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        card.setOpaque(false);
        card.setPreferredSize(new Dimension(100, 48));
        card.setMaximumSize(new Dimension(1000, 48));
        card.setMinimumSize(new Dimension(1000, 48));
        card.setBackground(LightBlue);

        JLabel lblLexeme = new JLabel(lexeme);
        lblLexeme.setFont(body);
        lblLexeme.setForeground(DarkBlue);

        JLabel lblToken = new JLabel(token);
        if (token.contains("Invalid")) {
            lblToken.setForeground(Color.RED);
        }
        else {
            lblToken.setForeground(DarkBlue);
        }
        lblToken.setFont(bodyBold);
        

        card.setBorder(new EmptyBorder(10, 15, 10, 15));
        card.add(lblLexeme);
        card.add(lblToken);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(LightBlue.darker());
                card.repaint();      
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(LightBlue);
                card.repaint();
            }
        });

        return card;
    }

    private void analyze() {
        List<String> lines = List.of(inputField.getText().split("\\R"));
        resultCardPanel.removeAll();

        int lineNo = 1;
        int tokenCount = 0;
        boolean isPrevComment = false;

        for (String l : lines) {
            if (!l.trim().isEmpty()) {
                JLabel lblLine = new JLabel("Line " + lineNo);
            lblLine.setFont(bodyBold);
            lblLine.setForeground(Color.WHITE);
            lblLine.setAlignmentX(Component.LEFT_ALIGNMENT);
            resultCardPanel.add(lblLine);
            }
        
            resultCardPanel.add(Box.createVerticalStrut(5));

            tokens = tokenizer.tokenize(l);
            for (String t : tokens) {
                if (t.startsWith("//")) {
                    resultCardPanel.add(createResultCard(t, "Single Line Comment"));
                    isPrevComment = false;
                }
                else if (t.startsWith("/*") && t.endsWith("*/") && t.length() >= 4) {
                    resultCardPanel.add(createResultCard(t, "Multi Line Comment"));
                    isPrevComment = false;
                }
                else if (t.startsWith("/*")) {
                    resultCardPanel.add(createResultCard(t, "Multi Line Comment"));
                    isPrevComment = true;
                }
                else if (t.contains("*/") && isPrevComment) {
                    resultCardPanel.add(createResultCard(t, "Multi Line Comment"));
                    isPrevComment = false;
                }
                else if (isPrevComment) {
                    resultCardPanel.add(createResultCard(t, "Multi Line Comment"));
                }
                else {
                    resultCardPanel.add(createResultCard(t, tokenizer.getTokenType(t)));
                }
                
                resultCardPanel.add(Box.createVerticalStrut(5));
                tokenCount++;
            }
            lineNo++;
        }

        JLabel lblCount = new JLabel("Total count of tokens: " + tokenCount);
        lblCount.setFont(bodyBold);
        lblCount.setForeground(Color.WHITE);
        lblCount.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultCardPanel.add(lblCount);
        
        repaint();
        revalidate();
    }
}
