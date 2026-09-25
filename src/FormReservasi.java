import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDate;

public class FormReservasi extends JFrame {
    private JTextField txtNoReservasi, txtTglReservasi, txtTglCheckin, txtTglCheckout;
    private JComboBox<String> cmbIdWaiters, cmbIdTamu, cmbIdKamar;
    private JTable tableReservasi;
    private DefaultTableModel model;

    public FormReservasi() {
        setTitle("Transaksi Reservasi - Hotel Management App");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Panel Atas / Judul
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(15, 23, 42));
        panelHeader.setBounds(0, 0, 900, 60);
        panelHeader.setLayout(null);

        JLabel lblTitle = new JLabel("TRANSAKSI RESERVASI KAMAR");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBounds(20, 15, 450, 30);
        panelHeader.add(lblTitle);
        add(panelHeader);

        // Form Input
        JLabel lblNoRes = new JLabel("No. Reservasi:");
        lblNoRes.setBounds(20, 80, 110, 25);
        add(lblNoRes);

        txtNoReservasi = new JTextField();
        txtNoReservasi.setBounds(135, 80, 190, 25);
        add(txtNoReservasi);

        JLabel lblTglRes = new JLabel("Tgl. Reservasi:");
        lblTglRes.setBounds(20, 120, 110, 25);
        add(lblTglRes);

        txtTglReservasi = new JTextField(LocalDate.now().toString());
        txtTglReservasi.setBounds(135, 120, 190, 25);
        add(txtTglReservasi);

        JLabel lblWaiters = new JLabel("ID Waiters:");
        lblWaiters.setBounds(20, 160, 110, 25);
        add(lblWaiters);

        cmbIdWaiters = new JComboBox<>();
        cmbIdWaiters.setBounds(135, 160, 190, 25);
        cmbIdWaiters.setBackground(Color.WHITE);
        add(cmbIdWaiters);

        JLabel lblTamu = new JLabel("ID Tamu:");
        lblTamu.setBounds(20, 200, 110, 25);
        add(lblTamu);

        cmbIdTamu = new JComboBox<>();
        cmbIdTamu.setBounds(135, 200, 190, 25);
        cmbIdTamu.setBackground(Color.WHITE);
        add(cmbIdTamu);

        JLabel lblKamar = new JLabel("ID Kamar:");
        lblKamar.setBounds(20, 240, 110, 25);
        add(lblKamar);

        cmbIdKamar = new JComboBox<>();
        cmbIdKamar.setBounds(135, 240, 190, 25);
        cmbIdKamar.setBackground(Color.WHITE);
        add(cmbIdKamar);

        JLabel lblCheckin = new JLabel("Tgl. Check-in:");
        lblCheckin.setBounds(20, 280, 110, 25);
        add(lblCheckin);

        txtTglCheckin = new JTextField();
        txtTglCheckin.setBounds(135, 280, 190, 25);
        add(txtTglCheckin);

        JLabel lblCheckout = new JLabel("Tgl. Check-out:");
        lblCheckout.setBounds(20, 320, 110, 25);
        add(lblCheckout);

        txtTglCheckout = new JTextField();
        txtTglCheckout.setBounds(135, 320, 190, 25);
        add(txtTglCheckout);

        JLabel lblHelper = new JLabel("*Format tgl: YYYY-MM-DD");
        lblHelper.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHelper.setForeground(Color.GRAY);
        lblHelper.setBounds(135, 350, 190, 20);
        add(lblHelper);

        // Tombol Aksi (CRUD)
        JButton btnSimpan = new JButton("Tambah");
        btnSimpan.setBounds(20, 385, 95, 35);
        btnSimpan.setBackground(new Color(37, 99, 235));
        btnSimpan.setForeground(Color.WHITE);
        add(btnSimpan);

        JButton btnEdit = new JButton("Edit");
        btnEdit.setBounds(125, 385, 95, 35);
        btnEdit.setBackground(new Color(217, 119, 6));
        btnEdit.setForeground(Color.WHITE);
        add(btnEdit);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(230, 385, 95, 35);
        btnHapus.setBackground(new Color(220, 38, 38));
        btnHapus.setForeground(Color.WHITE);
        add(btnHapus);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(125, 430, 95, 35);
        add(btnReset);

        // Tabel Data
        model = new DefaultTableModel();
        model.addColumn("No. Reservasi");
        model.addColumn("Tgl. Reservasi");
        model.addColumn("ID Waiters");
        model.addColumn("ID Tamu");
        model.addColumn("ID Kamar");
        model.addColumn("Check-in");
        model.addColumn("Check-out");

        tableReservasi = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableReservasi);
        scrollPane.setBounds(345, 80, 520, 470);
        add(scrollPane);

        // Load ComboBox dan Data Tabel awal
        loadComboWaiters();
        loadComboTamu();
        loadComboKamar();
        loadData();

        // Event Klik Tabel untuk Isi Form
        tableReservasi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableReservasi.getSelectedRow();
                if (row == -1) return;

                txtNoReservasi.setText(model.getValueAt(row, 0).toString());
                txtNoReservasi.setEditable(false);

                txtTglReservasi.setText(model.getValueAt(row, 1) != null ? model.getValueAt(row, 1).toString() : "");

                if (model.getValueAt(row, 2) != null) {
                    cmbIdWaiters.setSelectedItem(model.getValueAt(row, 2).toString());
                }
                if (model.getValueAt(row, 3) != null) {
                    cmbIdTamu.setSelectedItem(model.getValueAt(row, 3).toString());
                }
                if (model.getValueAt(row, 4) != null) {
                    cmbIdKamar.setSelectedItem(model.getValueAt(row, 4).toString());
                }

                txtTglCheckin.setText(model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "");
                txtTglCheckout.setText(model.getValueAt(row, 6) != null ? model.getValueAt(row, 6).toString() : "");
            }
        });

        // Event Tombol Tambah
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtNoReservasi.getText().trim().isEmpty() ||
                    txtTglReservasi.getText().trim().isEmpty() ||
                    txtTglCheckin.getText().trim().isEmpty() ||
                    txtTglCheckout.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (cmbIdWaiters.getSelectedItem() == null ||
                    cmbIdTamu.getSelectedItem() == null ||
                    cmbIdKamar.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Data Waiters, Tamu, dan Kamar tidak boleh kosong!\nPastikan data master sudah terisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    Date dReservasi = Date.valueOf(txtTglReservasi.getText().trim());
                    Date dCheckin = Date.valueOf(txtTglCheckin.getText().trim());
                    Date dCheckout = Date.valueOf(txtTglCheckout.getText().trim());

                    if (dCheckout.before(dCheckin)) {
                        JOptionPane.showMessageDialog(null, "Tanggal Check-out tidak boleh sebelum Tanggal Check-in!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String sql = "INSERT INTO treservasi (NoReservasi, TglReservasi, IdWaiters, IdTamu, IdKamar, TglCheckin, TglCheckout) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, txtNoReservasi.getText().trim());
                    pst.setDate(2, dReservasi);
                    pst.setString(3, cmbIdWaiters.getSelectedItem().toString());
                    pst.setString(4, cmbIdTamu.getSelectedItem().toString());
                    pst.setString(5, cmbIdKamar.getSelectedItem().toString());
                    pst.setDate(6, dCheckin);
                    pst.setDate(7, dCheckout);
                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Data Reservasi Berhasil Disimpan!");
                    loadData();
                    resetForm();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, "Format tanggal salah! Gunakan format YYYY-MM-DD\nContoh: " + LocalDate.now(), "Format Tanggal Salah", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal Menyimpan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Event Tombol Edit
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtNoReservasi.getText().trim().isEmpty() ||
                    txtTglReservasi.getText().trim().isEmpty() ||
                    txtTglCheckin.getText().trim().isEmpty() ||
                    txtTglCheckout.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Pilih data reservasi dari tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    Date dReservasi = Date.valueOf(txtTglReservasi.getText().trim());
                    Date dCheckin = Date.valueOf(txtTglCheckin.getText().trim());
                    Date dCheckout = Date.valueOf(txtTglCheckout.getText().trim());

                    if (dCheckout.before(dCheckin)) {
                        JOptionPane.showMessageDialog(null, "Tanggal Check-out tidak boleh sebelum Tanggal Check-in!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String sql = "UPDATE treservasi SET TglReservasi=?, IdWaiters=?, IdTamu=?, IdKamar=?, TglCheckin=?, TglCheckout=? WHERE NoReservasi=?";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setDate(1, dReservasi);
                    pst.setString(2, cmbIdWaiters.getSelectedItem().toString());
                    pst.setString(3, cmbIdTamu.getSelectedItem().toString());
                    pst.setString(4, cmbIdKamar.getSelectedItem().toString());
                    pst.setDate(5, dCheckin);
                    pst.setDate(6, dCheckout);
                    pst.setString(7, txtNoReservasi.getText().trim());
                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Data Reservasi Berhasil Diperbarui!");
                    loadData();
                    resetForm();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, "Format tanggal salah! Gunakan format YYYY-MM-DD\nContoh: " + LocalDate.now(), "Format Tanggal Salah", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal Edit: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Event Tombol Hapus
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String noRes = txtNoReservasi.getText().trim();
                if (noRes.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data reservasi No: " + noRes + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        String sql = "DELETE FROM treservasi WHERE NoReservasi=?";
                        Connection conn = Koneksi.getKoneksi();
                        PreparedStatement pst = conn.prepareStatement(sql);
                        pst.setString(1, noRes);
                        pst.execute();

                        JOptionPane.showMessageDialog(null, "Data Reservasi Berhasil Dihapus!");
                        loadData();
                        resetForm();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Gagal Hapus: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Event Tombol Reset
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });
    }

    private void loadComboWaiters() {
        cmbIdWaiters.removeAllItems();
        try {
            Connection conn = Koneksi.getKoneksi();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT IdWaiters FROM twaiters");
            while (rs.next()) {
                cmbIdWaiters.addItem(rs.getString("IdWaiters"));
            }
        } catch (Exception e) {
            System.err.println("Gagal load combo waiters: " + e.getMessage());
        }
    }

    private void loadComboTamu() {
        cmbIdTamu.removeAllItems();
        try {
            Connection conn = Koneksi.getKoneksi();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT IdTamu FROM ttamu");
            while (rs.next()) {
                cmbIdTamu.addItem(rs.getString("IdTamu"));
            }
        } catch (Exception e) {
            System.err.println("Gagal load combo tamu: " + e.getMessage());
        }
    }

    private void loadComboKamar() {
        cmbIdKamar.removeAllItems();
        try {
            Connection conn = Koneksi.getKoneksi();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT IdKamar FROM tkamar");
            while (rs.next()) {
                cmbIdKamar.addItem(rs.getString("IdKamar"));
            }
        } catch (Exception e) {
            System.err.println("Gagal load combo kamar: " + e.getMessage());
        }
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM treservasi";
            Connection conn = Koneksi.getKoneksi();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("NoReservasi"),
                    rs.getDate("TglReservasi"),
                    rs.getString("IdWaiters"),
                    rs.getString("IdTamu"),
                    rs.getString("IdKamar"),
                    rs.getDate("TglCheckin"),
                    rs.getDate("TglCheckout")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Load Data: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtNoReservasi.setText("");
        txtNoReservasi.setEditable(true);
        txtTglReservasi.setText(LocalDate.now().toString());
        txtTglCheckin.setText("");
        txtTglCheckout.setText("");
        loadComboWaiters();
        loadComboTamu();
        loadComboKamar();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormReservasi().setVisible(true));
    }
}
