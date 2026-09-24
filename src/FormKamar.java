import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class FormKamar extends JFrame {
    private JTextField txtIdKamar, txtNoKamar, txtNmKamar;
    private JComboBox<String> cmbIdJns;
    private JTable tableKamar;
    private DefaultTableModel model;

    public FormKamar() {
        setTitle("Kelola Kamar - Hotel Management App");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Panel Atas / Judul
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(15, 23, 42));
        panelHeader.setBounds(0, 0, 850, 60);
        panelHeader.setLayout(null);
        
        JLabel lblTitle = new JLabel("KELOLA DATA KAMAR");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBounds(20, 15, 400, 30);
        panelHeader.add(lblTitle);
        add(panelHeader);

        // Form Input
        JLabel lblIdKamar = new JLabel("ID Kamar:");
        lblIdKamar.setBounds(20, 80, 100, 25);
        add(lblIdKamar);

        txtIdKamar = new JTextField();
        txtIdKamar.setBounds(130, 80, 200, 25);
        add(txtIdKamar);

        JLabel lblIdJns = new JLabel("ID Jenis:");
        lblIdJns.setBounds(20, 120, 100, 25);
        add(lblIdJns);

        cmbIdJns = new JComboBox<>();
        cmbIdJns.setBounds(130, 120, 200, 25);
        loadComboJenis();
        add(cmbIdJns);

        JLabel lblNoKamar = new JLabel("No. Kamar:");
        lblNoKamar.setBounds(20, 160, 100, 25);
        add(lblNoKamar);

        txtNoKamar = new JTextField();
        txtNoKamar.setBounds(130, 160, 200, 25);
        add(txtNoKamar);

        JLabel lblNmKamar = new JLabel("Nama Kamar:");
        lblNmKamar.setBounds(20, 200, 100, 25);
        add(lblNmKamar);

        txtNmKamar = new JTextField();
        txtNmKamar.setBounds(130, 200, 200, 25);
        add(txtNmKamar);

        // Tabel Data
        model = new DefaultTableModel();
        model.addColumn("ID Kamar");
        model.addColumn("ID Jenis");
        model.addColumn("No. Kamar");
        model.addColumn("Nama Kamar");

        tableKamar = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableKamar);
        scrollPane.setBounds(350, 80, 465, 330);
        add(scrollPane);

        // Tombol Aksi (CRUD)
        JButton btnSimpan = new JButton("Tambah");
        btnSimpan.setBounds(20, 260, 95, 35);
        btnSimpan.setBackground(new Color(37, 99, 235));
        btnSimpan.setForeground(Color.WHITE);
        add(btnSimpan);

        JButton btnEdit = new JButton("Edit");
        btnEdit.setBounds(125, 260, 95, 35);
        btnEdit.setBackground(new Color(217, 119, 6));
        btnEdit.setForeground(Color.WHITE);
        add(btnEdit);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(230, 260, 95, 35);
        btnHapus.setBackground(new Color(220, 38, 38));
        btnHapus.setForeground(Color.WHITE);
        add(btnHapus);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(125, 310, 95, 35);
        add(btnReset);

        // Load Data awal
        loadData();

        // Event Klik Tabel
        tableKamar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableKamar.getSelectedRow();
                txtIdKamar.setText(model.getValueAt(row, 0).toString());
                txtIdKamar.setEditable(false);
                cmbIdJns.setSelectedItem(model.getValueAt(row, 1).toString());
                txtNoKamar.setText(model.getValueAt(row, 2).toString());
                txtNmKamar.setText(model.getValueAt(row, 3).toString());
            }
        });

        // Event Tombol Tambah
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sql = "INSERT INTO tkamar VALUES (?, ?, ?, ?)";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, txtIdKamar.getText());
                    pst.setString(2, cmbIdJns.getSelectedItem().toString());
                    pst.setString(3, txtNoKamar.getText());
                    pst.setString(4, txtNmKamar.getText());
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Kamar Berhasil Disimpan!");
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
                    String sql = "UPDATE tkamar SET IdJns=?, NoKamar=?, NmKamar=? WHERE IdKamar=?";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, cmbIdJns.getSelectedItem().toString());
                    pst.setString(2, txtNoKamar.getText());
                    pst.setString(3, txtNmKamar.getText());
                    pst.setString(4, txtIdKamar.getText());
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Kamar Berhasil Diperbarui!");
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
                    String sql = "DELETE FROM tkamar WHERE IdKamar=?";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, txtIdKamar.getText());
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Kamar Berhasil Dihapus!");
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

    private void loadComboJenis() {
        try {
            Connection conn = Koneksi.getKoneksi();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT IdJns FROM tjnskamar");
            while (rs.next()) {
                cmbIdJns.addItem(rs.getString("IdJns"));
            }
        } catch (Exception e) {
            System.err.println("Gagal load combo jenis: " + e.getMessage());
        }
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM tkamar";
            Connection conn = Koneksi.getKoneksi();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("IdKamar"),
                    rs.getString("IdJns"),
                    rs.getString("NoKamar"),
                    rs.getString("NmKamar")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Load Data: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtIdKamar.setText("");
        txtIdKamar.setEditable(true);
        txtNoKamar.setText("");
        txtNmKamar.setText("");
        if (cmbIdJns.getItemCount() > 0) cmbIdJns.setSelectedIndex(0);
    }
}