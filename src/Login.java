import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame {
    private CardLayout cardLayout;
    private JPanel formContainer;

    private JTextField txtLoginUser;
    private JPasswordField txtLoginPass;

    private JTextField txtRegUser;
    private JPasswordField txtRegPass, txtRegConfirm;

    private final Color COLOR_BG_SIDE = new Color(15, 23, 42); // Deep Midnight Slate
    private final Color COLOR_GOLD = new Color(217, 119, 6); // Amber Gold
    private final Color COLOR_PRIMARY = new Color(37, 99, 235); // Royal Blue
    private final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);

    public Login() {
        setTitle("Grand Luxe Hotel - Authentication");
        setSize(860, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // 1. Sisi Kiri: Visual Branding Hotel
        mainPanel.add(createBrandingPanel());

        // 2. Sisi Kanan: Form Card (Login & Register Flip)
        cardLayout = new CardLayout();
        formContainer = new JPanel(cardLayout);
        formContainer.setBackground(Color.WHITE);

        formContainer.add(createLoginCard(), "CARD_LOGIN");
        formContainer.add(createRegisterCard(), "CARD_REGISTER");

        mainPanel.add(formContainer);
        add(mainPanel);
    }

    // ================= PANEL BRANDING (KIRI) =================
    private JPanel createBrandingPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background gradasi gelap elegan
                GradientPaint gp = new GradientPaint(0, 0, COLOR_BG_SIDE, getWidth(), getHeight(), new Color(30, 41, 59));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Ornamen lingkaran transparan mewah
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillOval(-60, -60, 260, 260);
                g2.fillOval(getWidth() - 140, getHeight() - 140, 220, 220);

                // Aksen garis diagonal
                g2.setColor(new Color(217, 119, 6, 40));
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(40, getHeight() - 50, getWidth() - 40, 50);

                g2.dispose();
            }
        };
        panel.setBorder(new EmptyBorder(50, 40, 50, 40));

        // Brand & Icon
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel lblStar = new JLabel("✦ ✦ ✦ ✦ ✦");
        lblStar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblStar.setForeground(COLOR_GOLD);
        lblStar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBrand = new JLabel("<html>SWISSBELLIN HOTEL<br>KARAWANG</html>");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblBrand.setBorder(new EmptyBorder(15, 0, 15, 0));

        JLabel lblTagline = new JLabel("<html>Sistem manajemen front-desk dan transaksi reservasi terpadu berbasis desktop.</html>");
        lblTagline.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTagline.setForeground(new Color(203, 213, 225));
        lblTagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(lblStar);
        content.add(lblBrand);
        content.add(lblTagline);

        panel.add(content, BorderLayout.CENTER);

        JLabel lblFooter = new JLabel("Hotel Management App • 2026");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(148, 163, 184));
        panel.add(lblFooter, BorderLayout.SOUTH);

        return panel;
    }

    // ================= KARTU LOGIN =================
    private JPanel createLoginCard() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Title Form
        JLabel lblTitle = new JLabel("Masuk Akun");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(15, 23, 42));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 4, 0);
        panel.add(lblTitle, gbc);

        JLabel lblDesc = new JLabel("Silakan masukkan identitas akun Anda.");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(COLOR_TEXT_MUTED);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(lblDesc, gbc);

        // Input Username
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(createFieldLabel("Username"), gbc);

        txtLoginUser = createRoundedTextField();
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 14, 0);
        panel.add(txtLoginUser, gbc);

        // Input Password
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(createFieldLabel("Password"), gbc);

        txtLoginPass = createRoundedPasswordField();
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 24, 0);
        panel.add(txtLoginPass, gbc);

        // Tombol Login
        JButton btnLogin = createModernButton("Login Sekarang", COLOR_PRIMARY, Color.WHITE);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 18, 0);
        panel.add(btnLogin, gbc);

        // Link Beralih ke Register
        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        switchPanel.setOpaque(false);
        JLabel lblNoAcc = new JLabel("Belum punya akun?");
        lblNoAcc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNoAcc.setForeground(COLOR_TEXT_MUTED);

        JLabel lblGoReg = new JLabel("Daftar Akun");
        lblGoReg.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblGoReg.setForeground(COLOR_PRIMARY);
        lblGoReg.setCursor(new Cursor(Cursor.HAND_CURSOR));

        switchPanel.add(lblNoAcc);
        switchPanel.add(lblGoReg);

        gbc.gridy = 7;
        panel.add(switchPanel, gbc);

        // Event Aksi
        btnLogin.addActionListener(e -> prosesLogin());
        lblGoReg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(formContainer, "CARD_REGISTER");
            }
        });

        return panel;
    }

    // ================= KARTU REGISTER =================
    private JPanel createRegisterCard() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(25, 40, 25, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel lblTitle = new JLabel("Daftar Akun Baru");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(15, 23, 42));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 4, 0);
        panel.add(lblTitle, gbc);

        JLabel lblDesc = new JLabel("Buat akun untuk akses sistem manajemen hotel.");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(COLOR_TEXT_MUTED);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 16, 0);
        panel.add(lblDesc, gbc);

        // Form Fields
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 4, 0);
        panel.add(createFieldLabel("Username Baru"), gbc);

        txtRegUser = createRoundedTextField();
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(txtRegUser, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 4, 0);
        panel.add(createFieldLabel("Password"), gbc);

        txtRegPass = createRoundedPasswordField();
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(txtRegPass, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 4, 0);
        panel.add(createFieldLabel("Konfirmasi Password"), gbc);

        txtRegConfirm = createRoundedPasswordField();
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 18, 0);
        panel.add(txtRegConfirm, gbc);

        // Tombol Register
        JButton btnRegister = createModernButton("Buat Akun", new Color(16, 185, 129), Color.WHITE);
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 16, 0);
        panel.add(btnRegister, gbc);

        // Link Beralih ke Login
        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        switchPanel.setOpaque(false);
        JLabel lblHaveAcc = new JLabel("Sudah punya akun?");
        lblHaveAcc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblHaveAcc.setForeground(COLOR_TEXT_MUTED);

        JLabel lblGoLogin = new JLabel("Masuk di sini");
        lblGoLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblGoLogin.setForeground(COLOR_PRIMARY);
        lblGoLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        switchPanel.add(lblHaveAcc);
        switchPanel.add(lblGoLogin);

        gbc.gridy = 9;
        panel.add(switchPanel, gbc);

        // Event Aksi
        btnRegister.addActionListener(e -> prosesRegister());
        lblGoLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(formContainer, "CARD_LOGIN");
            }
        });

        return panel;
    }

    // ================= LOGIKA AUTENTIKASI =================
    private void prosesLogin() {
        String username = txtLoginUser.getText().trim();
        String password = new String(txtLoginPass.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "SELECT * FROM TLogin WHERE UserNm = ? AND PassWd = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat Datang, " + username);
                new DashboardHotel (username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Gagal Login", JOptionPane.ERROR_MESSAGE);
                txtLoginPass.setText("");
                txtLoginPass.requestFocus();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Kesalahan Sistem: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prosesRegister() {
        String username = txtRegUser.getText().trim();
        String password = new String(txtRegPass.getPassword()).trim();
        String confirm = new String(txtRegConfirm.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field registrasi harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Konfirmasi password tidak cocok!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            txtRegConfirm.setText("");
            txtRegConfirm.requestFocus();
            return;
        }

        try {
            Connection conn = Koneksi.getKoneksi();
            String sqlCek = "SELECT * FROM TLogin WHERE UserNm = ?";
            PreparedStatement psCek = conn.prepareStatement(sqlCek);
            psCek.setString(1, username);
            ResultSet rs = psCek.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username '" + username + "' sudah dipakai!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sqlInsert = "INSERT INTO TLogin (UserNm, PassWd, Foto) VALUES (?, ?, NULL)";
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setString(1, username);
            psInsert.setString(2, password);

            if (psInsert.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Akun berhasil dibuat! Silakan masuk.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                txtRegUser.setText("");
                txtRegPass.setText("");
                txtRegConfirm.setText("");

                // Pindah langsung ke form login
                txtLoginUser.setText(username);
                txtLoginPass.setText("");
                txtLoginPass.requestFocus();
                cardLayout.show(formContainer, "CARD_LOGIN");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Kesalahan Sistem: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= CUSTOM UI HELPERS =================
    private JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(51, 65, 85));
        return lbl;
    }

    private JTextField createRoundedTextField() {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(248, 250, 252));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
                g2.setColor(new Color(203, 213, 225));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
            }
        };
        tf.setOpaque(false);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(280, 38));
        tf.setBorder(new EmptyBorder(5, 12, 5, 12));
        return tf;
    }

    private JPasswordField createRoundedPasswordField() {
        JPasswordField pf = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(248, 250, 252));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
                g2.setColor(new Color(203, 213, 225));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
            }
        };
        pf.setOpaque(false);
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pf.setPreferredSize(new Dimension(280, 38));
        pf.setBorder(new EmptyBorder(5, 12, 5, 12));
        return pf;
    }

    private JButton createModernButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(280, 42));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}