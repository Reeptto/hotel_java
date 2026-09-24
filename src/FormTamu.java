import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class FormTamu extends JFrame {
    private JTextField txtIdTamu, txtNIK, txtNmTamu, txtAlamat, txtTelp, txtEmail;
    private JTable tableTamu;
    private DefaultTableModel model;

    public FormTamu() {
        setTitle("Kelola Tamu - Hotel Management App");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Panel Atas / Judul
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(15, 23, 42));
        panelHeader.setBounds(0, 0, 850, 60);
        panelHeader.setLayout(null);
        
        JLabel lblTitle = new JLabel("KELOLA DATA TAMU");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBounds(20, 15, 400, 30);
        panelHeader.add(lblTitle);
        add(panelHeader);

        // Form Input
        JLabel lblId = new JLabel("ID Tamu:");
        lblId.setBounds(20, 80, 100, 25);
        add(lblId);

        txtIdTamu = new JTextField();
        txtIdTamu.setBounds(130, 80, 200, 25);
        add(txtIdTamu);

        JLabel lblNIK = new JLabel("NIK:");
        lblNIK.setBounds(20, 120, 100, 25);
        add(lblNIK);

        txtNIK = new JTextField();
        txtNIK.setBounds(130, 120, 200, 25);
        add(txtNIK);

        JLabel lblNm = new JLabel("Nama Tamu:");
        lblNm.setBounds(20, 160, 100, 25);
        add(lblNm);

        txtNmTamu = new JTextField();
        txtNmTamu.setBounds(130, 160, 200, 25);
        add(txtNmTamu);

        JLabel lblAlamat = new JLabel("Alamat:");
        lblAlamat.setBounds(20, 200, 100, 25);
        add(lblAlamat);

        txtAlamat = new JTextField();
        txtAlamat.setBounds(130, 200, 200, 25);
        add(txtAlamat);

        JLabel lblTelp = new JLabel("No. Telp:");
        lblTelp.setBounds(20, 240, 100, 25);
        add(lblTelp);

        txtTelp = new JTextField();
        txtTelp.setBounds(130, 240, 200, 25);
        add(txtTelp);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(20, 280, 100, 25);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(130, 280, 200, 25);
        add(txtEmail);

        // Tabel Data
        model = new DefaultTableModel();
        model.addColumn("ID Tamu");
        model.addColumn("NIK");
        model.addColumn("Nama Tamu");
        model.addColumn("Alamat");
        model.addColumn("No. Telp");
        model.addColumn("Email");

        tableTamu = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableTamu);
        scrollPane.setBounds(350, 80, 465, 430);
        add(scrollPane);

        // Tombol Aksi (CRUD)
        JButton btnSimpan = new JButton("Tambah");
        btnSimpan.setBounds(20, 330, 95, 35);
        btnSimpan.setBackground(new Color(37, 99, 235));
        btnSimpan.setForeground(Color.WHITE);
        add(btnSimpan);

        JButton btnEdit = new JButton("Edit");
        btnEdit.setBounds(125, 330, 95, 35);
        btnEdit.setBackground(new Color(217, 119, 6));
        btnEdit.setForeground(Color.WHITE);
        add(btnEdit);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(230, 330, 95, 35);
        btnHapus.setBackground(new Color(220, 38, 38));
        btnHapus.setForeground(Color.WHITE);
        add(btnHapus);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(125, 380, 95, 35);
        add(btnReset);

        // Load Data saat pertama dibuka
        loadData();

        // Event Klik Tabel untuk Isi Form
        tableTamu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableTamu.getSelectedRow();
                txtIdTamu.setText(model.getValueAt(row, 0).toString());
                txtIdTamu.setEditable(false); // ID tidak boleh diubah saat edit
                txtNIK.setText(model.getValueAt(row, 1) != null ? model.getValueAt(row, 1).toString() : "");
                txtNmTamu.setText(model.getValueAt(row, 2) != null ? model.getValueAt(row, 2).toString() : "");
                txtAlamat.setText(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "");
                txtTelp.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");
                txtEmail.setText(model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "");
            }
        });

        // Event Tombol Tambah
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sql = "INSERT INTO ttamu (IdTamu, NIK, NmTamu, Alamat, Telp, Email) VALUES (?, ?, ?, ?, ?, ?)";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, txtIdTamu.getText());
                    pst.setString(2, txtNIK.getText());
                    pst.setString(3, txtNmTamu.getText());
                    pst.setString(4, txtAlamat.getText());
                    pst.setString(5, txtTelp.getText());
                    pst.setString(6, txtEmail.getText());
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Tamu Berhasil Disimpan!");
                    loadData();
                    resetForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal Menyimpan: " + ex.getMessage());
                }
            }
        });

        // Event Tombol Edit
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sql = "UPDATE ttamu SET NIK=?, NmTamu=?, Alamat=?, Telp=?, Email=? WHERE IdTamu=?";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, txtNIK.getText());
                    pst.setString(2, txtNmTamu.getText());
                    pst.setString(3, txtAlamat.getText());
                    pst.setString(4, txtTelp.getText());
                    pst.setString(5, txtEmail.getText());
                    pst.setString(6, txtIdTamu.getText());
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Berhasil Diperbarui!");
                    loadData();
                    resetForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal Edit: " + ex.getMessage());
                }
            }
        });

        // Event Tombol Hapus
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sql = "DELETE FROM ttamu WHERE IdTamu=?";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, txtIdTamu.getText());
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus!");
                    loadData();
                    resetForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal Hapus: " + ex.getMessage());
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

    private void loadData() {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM ttamu";
            Connection conn = Koneksi.getKoneksi();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("IdTamu"),
                    rs.getString("NIK"),
                    rs.getString("NmTamu"),
                    rs.getString("Alamat"),
                    rs.getString("Telp"),
                    rs.getString("Email")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Load Data: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtIdTamu.setText("");
        txtIdTamu.setEditable(true);
        txtNIK.setText("");
        txtNmTamu.setText("");
        txtAlamat.setText("");
        txtTelp.setText("");
        txtEmail.setText("");
    }
}
