import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DashboardHotel extends JFrame {

    private String username;
    private byte[] fotoData;

    private JLabel lblUserNav;
    private JLabel lblFotoNav;

    // Konstruktor 1 Parameter
    public DashboardHotel(String username) {
        this(username, null);
    }

    // Konstruktor 2 Parameter
    public DashboardHotel(String username, byte[] fotoData) {
        this.username = (username == null || username.isEmpty()) ? "Admin" : username;
        this.fotoData = fotoData;

        // Ambil data foto langsung dari database jika belum di-passing
        if (this.fotoData == null) {
            loadFotoDariDatabase();
        }

        initUI();
    }

    private void loadFotoDariDatabase() {
        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "SELECT Foto FROM TLogin WHERE UserNm = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.fotoData = rs.getBytes("Foto");
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat foto profil: " + e.getMessage());
        }
    }

    private void initUI() {
        setTitle("Hotel Management App - Dashboard");
        setSize(980, 620);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= 1. SIDEBAR KIRI (MENU NAVIGASI) =================
        JPanel panelSidebar = new JPanel(new BorderLayout());
        panelSidebar.setPreferredSize(new Dimension(230, 620));
        panelSidebar.setBackground(new Color(24, 32, 47)); // Navy Gelap

        // Brand Logo Atas Sidebar
        JPanel panelBrand = new JPanel(new GridLayout(2, 1));
        panelBrand.setOpaque(false);
        panelBrand.setBorder(new EmptyBorder(25, 20, 20, 20));

        JLabel lblBrandTitle = new JLabel("SWISSBELLIN HOTEL");
        lblBrandTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBrandTitle.setForeground(new Color(245, 158, 11)); // Emas Amber

        JLabel lblBrandSub = new JLabel("KARAWANG");
        lblBrandSub.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBrandSub.setForeground(new Color(148, 163, 184));

        panelBrand.add(lblBrandTitle);
        panelBrand.add(lblBrandSub);
        panelSidebar.add(panelBrand, BorderLayout.NORTH);

        // List Tombol Modul Anggota
        JPanel panelMenu = new JPanel(new GridLayout(6, 1, 0, 8));
        panelMenu.setOpaque(false);
        panelMenu.setBorder(new EmptyBorder(10, 16, 25, 16));

        JButton btnKamar = createMenuButton("Kelola Kamar");
        JButton btnJenisKamar = createMenuButton("Kelola Jenis Kamar");
        JButton btnTamu = createMenuButton("Kelola Tamu");
        JButton btnWaiters = createMenuButton("Kelola Waiters");
        JButton btnReservasi = createMenuButton("Transaksi Reservasi");
        JButton btnLogout = createMenuButton("Logout");
        btnLogout.setBackground(new Color(220, 38, 38)); // Merah

        // Event Klik Tiap Tombol
        btnKamar.addActionListener(e -> bukaModul("CRUD Kamar"));
        btnJenisKamar.addActionListener(e -> bukaModul("CRUD Jenis Kamar"));
        btnTamu.addActionListener(e -> bukaModul("CRUD Tamu"));
        btnWaiters.addActionListener(e -> bukaModul("CRUD Waiters"));
        btnReservasi.addActionListener(e -> bukaModul("CRUD Reservasi"));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah Anda yakin ingin keluar?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new Login().setVisible(true);
                this.dispose();
            }
        });

        panelMenu.add(btnKamar);
        panelMenu.add(btnJenisKamar);
        panelMenu.add(btnTamu);
        panelMenu.add(btnWaiters);
        panelMenu.add(btnReservasi);
        panelMenu.add(btnLogout);

        panelSidebar.add(panelMenu, BorderLayout.CENTER);
        add(panelSidebar, BorderLayout.WEST);

        // ================= 2. KONTEN KANAN UTAMA =================
        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.setBackground(new Color(248, 250, 252));

        // ----- A. HEADER / NAVBAR ATAS -----
        JPanel panelNavbar = new JPanel(new BorderLayout());
        panelNavbar.setBackground(Color.WHITE);
        panelNavbar.setPreferredSize(new Dimension(0, 75));
        panelNavbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                new EmptyBorder(12, 30, 12, 30)));

        // Judul Aplikasi di Kiri Navbar
        JPanel panelTitle = new JPanel(new GridLayout(2, 1));
        panelTitle.setOpaque(false);

        JLabel lblTitle = new JLabel("HOTEL MANAGEMENT APP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(15, 23, 42));

        JLabel lblSubtitle = new JLabel("Sistem Informasi Manajemen Reservasi & Operasional Hotel");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(100, 116, 139));

        panelTitle.add(lblTitle);
        panelTitle.add(lblSubtitle);
        panelNavbar.add(panelTitle, BorderLayout.WEST);

        // Profil User & Avatar di Kanan Navbar
        JPanel panelUserRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelUserRight.setOpaque(false);

        // Nama & Status
        JPanel panelNameTag = new JPanel(new GridLayout(2, 1));
        panelNameTag.setOpaque(false);

        lblUserNav = new JLabel(this.username, SwingConstants.RIGHT);
        lblUserNav.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUserNav.setForeground(new Color(30, 41, 59));

        JLabel lblStatus = new JLabel("● Online (Klik foto)", SwingConstants.RIGHT);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(16, 185, 129));

        panelNameTag.add(lblUserNav);
        panelNameTag.add(lblStatus);

        // Avatar Bulat (Bisa di-klik untuk ganti foto)
        lblFotoNav = new JLabel();
        lblFotoNav.setPreferredSize(new Dimension(48, 48));
        lblFotoNav.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblFotoNav.setToolTipText("Klik untuk mengubah/mengunggah foto profil");
        tampilkanFotoBulat();

        lblFotoNav.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pilihDanSimpanFoto();
            }
        });

        panelUserRight.add(panelNameTag);
        panelUserRight.add(lblFotoNav);
        panelNavbar.add(panelUserRight, BorderLayout.EAST);

        panelMain.add(panelNavbar, BorderLayout.NORTH);

        // ----- B. BODY BERANDA (KARTU KOTAK COMPACT) -----
        JPanel panelBody = new JPanel(new BorderLayout());
        panelBody.setOpaque(false);
        panelBody.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Area Kartu yang Dibuat Berukuran Kotak Wajar (Bukan memanjang)
        JPanel panelCards = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelCards.setOpaque(false);

        panelCards.add(createSquareCard("Kamar Tersedia", "Siap Disewa", "24 Kamar", new Color(14, 165, 233)));
        panelCards.add(createSquareCard("Reservasi Aktif", "Hari Ini", "12 Tamu", new Color(16, 185, 129)));
        panelCards.add(createSquareCard("Tamu Menginap", "Total Terisi", "18 Kamar", new Color(245, 158, 11)));

        panelBody.add(panelCards, BorderLayout.NORTH);

        // Footer di Bawah
        JLabel lblFooter = new JLabel("Kelompok II - Aplikasi Berbasis Java Desktop");
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblFooter.setForeground(new Color(148, 163, 184));
        panelBody.add(lblFooter, BorderLayout.SOUTH);

        panelMain.add(panelBody, BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);
    }

    // Helper membuat Kartu Kotak Bersih
    private JPanel createSquareCard(String title, String subtitle, String statValue, Color badgeColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(new Color(226, 232, 240));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        // Dimensi kotak proporsional
        card.setPreferredSize(new Dimension(215, 140));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        // Strip Atas Aksen Warna
        JPanel strip = new JPanel();
        strip.setBackground(badgeColor);
        strip.setPreferredSize(new Dimension(35, 4));

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(strip, BorderLayout.WEST);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(100, 116, 139));
        topContainer.add(lblSub, BorderLayout.EAST);

        card.add(topContainer, BorderLayout.NORTH);

        // Nilai Angka Besar
        JLabel lblStat = new JLabel(statValue);
        lblStat.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblStat.setForeground(new Color(15, 23, 42));
        lblStat.setBorder(new EmptyBorder(8, 0, 0, 0));

        // Label Judul
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(new Color(71, 85, 105));

        JPanel midPanel = new JPanel(new GridLayout(2, 1));
        midPanel.setOpaque(false);
        midPanel.add(lblStat);
        midPanel.add(lblTitle);

        card.add(midPanel, BorderLayout.CENTER);

        return card;
    }

    // Helper membuat Avatar Bulat (Circular Avatar)
    private void tampilkanFotoBulat() {
        int diameter = 48;
        BufferedImage circleImg = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));

        if (this.fotoData != null && this.fotoData.length > 0) {
            try {
                BufferedImage original = ImageIO.read(new ByteArrayInputStream(this.fotoData));
                if (original != null) {
                    g2.drawImage(original, 0, 0, diameter, diameter, null);
                } else {
                    renderDefaultAvatar(g2, diameter);
                }
            } catch (Exception ex) {
                renderDefaultAvatar(g2, diameter);
            }
        } else {
            renderDefaultAvatar(g2, diameter);
        }

        g2.setClip(null);
        g2.setColor(new Color(203, 213, 225));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(0, 0, diameter - 1, diameter - 1);
        g2.dispose();

        lblFotoNav.setIcon(new ImageIcon(circleImg));
    }

    private void renderDefaultAvatar(Graphics2D g2, int size) {
        g2.setColor(new Color(226, 232, 240));
        g2.fillRect(0, 0, size, size);
        g2.setColor(new Color(100, 116, 139));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        String inisial = (username != null && !username.isEmpty()) ? username.substring(0, 1).toUpperCase() : "U";
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(inisial)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(inisial, x, y);
    }

    // Fungsi Upload & Update Foto ke Kolom 'Foto' di Database
    private void pilihDanSimpanFoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Pilih Foto Profil");
        chooser.setFileFilter(new FileNameExtensionFilter("Gambar (JPG, PNG, JPEG)", "jpg", "png", "jpeg"));

        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            try {
                byte[] bytesBaru = new byte[(int) selectedFile.length()];
                try (FileInputStream fis = new FileInputStream(selectedFile)) {
                    fis.read(bytesBaru);
                }

                // Update ke Database MySQL
                Connection conn = Koneksi.getKoneksi();
                String sql = "UPDATE TLogin SET Foto = ? WHERE UserNm = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setBytes(1, bytesBaru);
                ps.setString(2, this.username);

                int updated = ps.executeUpdate();
                if (updated > 0) {
                    this.fotoData = bytesBaru;
                    tampilkanFotoBulat();
                    JOptionPane.showMessageDialog(this, "Foto profil berhasil diperbarui!", "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengupdate foto profil di database.", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal memproses gambar: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(36, 47, 65));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void bukaModul(String namaModul) {
        if (namaModul.equals("CRUD Jenis Kamar")) {
            new FormJenisKamar().setVisible(true);
        } else if (namaModul.equals("CRUD Kamar")) {
            new FormKamar().setVisible(true);
        } else if (namaModul.equals("CRUD Tamu")) {
            new FormTamu().setVisible(true);
        } else if (namaModul.equals("CRUD Waiters")) {
            new FormWaiters().setVisible(true);
        } else if (namaModul.equals("CRUD Reservasi") || namaModul.equals("Transaksi Reservasi")) {
            new FormReservasi().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Membuka modul: " + namaModul);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardHotel("Waode", null).setVisible(true));
    }
}