import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class FormWaiters extends JFrame {
    private JTextField txtIdWaiters, txtNmLengkap, txtAlamat, txtTelp;
    private JComboBox<String> cbJnsKelamin, cbStatus;
    private JTable tableWaiters;
    private DefaultTableModel model;

    public FormWaiters() {
        setTitle("Kelola Waiters - Hotel Management App");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Panel Atas / Judul
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(15, 23, 42));
        panelHeader.setBounds(0, 0, 850, 60);
        panelHeader.setLayout(null);
        
        JLabel lblTitle = new JLabel("KELOLA DATA WAITERS");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBounds(20, 15, 400, 30);
        panelHeader.add(lblTitle);
        add(panelHeader);

        // Form Input
        JLabel lblId = new JLabel("ID Waiters:");
        lblId.setBounds(20, 80, 100, 25);
        add(lblId);

        txtIdWaiters = new JTextField();
        txtIdWaiters.setBounds(130, 80, 200, 25);
        add(txtIdWaiters);

        JLabel lblNm = new JLabel("Nama Lengkap:");
        lblNm.setBounds(20, 120, 100, 25);
        add(lblNm);

        txtNmLengkap = new JTextField();
        txtNmLengkap.setBounds(130, 120, 200, 25);
        add(txtNmLengkap);

        JLabel lblJnsKelamin = new JLabel("Jenis Kelamin:");
        lblJnsKelamin.setBounds(20, 160, 100, 25);
        add(lblJnsKelamin);

        cbJnsKelamin = new JComboBox<>(new String[]{"Laki-Laki", "Perempuan"});
        cbJnsKelamin.setBounds(130, 160, 200, 25);
        cbJnsKelamin.setBackground(Color.WHITE);
        add(cbJnsKelamin);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(20, 200, 100, 25);
        add(lblStatus);

        cbStatus = new JComboBox<>(new String[]{"Aktif", "Nonaktif"});
        cbStatus.setBounds(130, 200, 200, 25);
        cbStatus.setBackground(Color.WHITE);
        add(cbStatus);

        JLabel lblAlamat = new JLabel("Alamat:");
        lblAlamat.setBounds(20, 240, 100, 25);
        add(lblAlamat);

        txtAlamat = new JTextField();
        txtAlamat.setBounds(130, 240, 200, 25);
        add(txtAlamat);

        JLabel lblTelp = new JLabel("No. Telp:");
        lblTelp.setBounds(20, 280, 100, 25);
        add(lblTelp);

        txtTelp = new JTextField();
        txtTelp.setBounds(130, 280, 200, 25);
        add(txtTelp);

        // Tabel Data
        model = new DefaultTableModel();
        model.addColumn("ID Waiters");
        model.addColumn("Nama Lengkap");
        model.addColumn("Jenis Kelamin");
        model.addColumn("Status");
        model.addColumn("Alamat");
        model.addColumn("No. Telp");

        tableWaiters = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableWaiters);
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
        tableWaiters.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableWaiters.getSelectedRow();
                txtIdWaiters.setText(model.getValueAt(row, 0).toString());
                txtIdWaiters.setEditable(false); // ID tidak boleh diubah saat edit
                txtNmLengkap.setText(model.getValueAt(row, 1) != null ? model.getValueAt(row, 1).toString() : "");
                
                String jk = model.getValueAt(row, 2) != null ? model.getValueAt(row, 2).toString() : "";
                cbJnsKelamin.setSelectedItem(jk);
                
                String status = model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "";
                cbStatus.setSelectedItem(status);
                
                txtAlamat.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");
                txtTelp.setText(model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "");
            }
        });

        // Event Tombol Tambah
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sql = "INSERT INTO twaiters (IdWaiters, NmLengkap, JnsKelamin, Status, Alamat, Telp) VALUES (?, ?, ?, ?, ?, ?)";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, txtIdWaiters.getText());
                    pst.setString(2, txtNmLengkap.getText());
                    pst.setString(3, cbJnsKelamin.getSelectedItem().toString());
                    pst.setString(4, cbStatus.getSelectedItem().toString());
                    pst.setString(5, txtAlamat.getText());
                    pst.setString(6, txtTelp.getText());
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Waiters Berhasil Disimpan!");
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
                    String sql = "UPDATE twaiters SET NmLengkap=?, JnsKelamin=?, Status=?, Alamat=?, Telp=? WHERE IdWaiters=?";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, txtNmLengkap.getText());
                    pst.setString(2, cbJnsKelamin.getSelectedItem().toString());
                    pst.setString(3, cbStatus.getSelectedItem().toString());
                    pst.setString(4, txtAlamat.getText());
                    pst.setString(5, txtTelp.getText());
                    pst.setString(6, txtIdWaiters.getText());
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
                    String sql = "DELETE FROM twaiters WHERE IdWaiters=?";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, txtIdWaiters.getText());
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
            String sql = "SELECT * FROM twaiters";
            Connection conn = Koneksi.getKoneksi();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("IdWaiters"),
                    rs.getString("NmLengkap"),
                    rs.getString("JnsKelamin"),
                    rs.getString("Status"),
                    rs.getString("Alamat"),
                    rs.getString("Telp")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Load Data: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtIdWaiters.setText("");
        txtIdWaiters.setEditable(true);
        txtNmLengkap.setText("");
        cbJnsKelamin.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        txtAlamat.setText("");
        txtTelp.setText("");
    }
}
