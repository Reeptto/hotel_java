import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class FormJenisKamar extends JFrame {
    private JTextField txtIdJns, txtJnsKamar, txtFasilitas, txtHrgSewa;
    private JTable tableJenisKamar;
    private DefaultTableModel model;

    public FormJenisKamar() {
        setTitle("Kelola Jenis Kamar - Hotel Management App");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Panel Atas / Judul
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(15, 23, 42));
        panelHeader.setBounds(0, 0, 850, 60);
        panelHeader.setLayout(null);
        
        JLabel lblTitle = new JLabel("KELOLA DATA JENIS KAMAR");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBounds(20, 15, 400, 30);
        panelHeader.add(lblTitle);
        add(panelHeader);

        // Form Input
        JLabel lblId = new JLabel("ID Jenis:");
        lblId.setBounds(20, 80, 100, 25);
        add(lblId);

        txtIdJns = new JTextField();
        txtIdJns.setBounds(130, 80, 200, 25);
        add(txtIdJns);

        JLabel lblNama = new JLabel("Nama Jenis:");
        lblNama.setBounds(20, 120, 100, 25);
        add(lblNama);

        txtJnsKamar = new JTextField();
        txtJnsKamar.setBounds(130, 120, 200, 25);
        add(txtJnsKamar);

        JLabel lblFasilitas = new JLabel("Fasilitas:");
        lblFasilitas.setBounds(20, 160, 100, 25);
        add(lblFasilitas);

        txtFasilitas = new JTextField();
        txtFasilitas.setBounds(130, 160, 200, 25);
        add(txtFasilitas);

        JLabel lblHarga = new JLabel("Harga Sewa:");
        lblHarga.setBounds(20, 200, 100, 25);
        add(lblHarga);

        txtHrgSewa = new JTextField();
        txtHrgSewa.setBounds(130, 200, 200, 25);
        add(txtHrgSewa);

        // Tabel Data
        model = new DefaultTableModel();
        model.addColumn("ID Jenis");
        model.addColumn("Jenis Kamar");
        model.addColumn("Fasilitas");
        model.addColumn("Harga Sewa");

        tableJenisKamar = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableJenisKamar);
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

        // Load Data saat pertama dibuka
        loadData();

        // Event Klik Tabel untuk Isi Form
        tableJenisKamar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableJenisKamar.getSelectedRow();
                txtIdJns.setText(model.getValueAt(row, 0).toString());
                txtIdJns.setEditable(false); // ID tidak boleh diubah saat edit
                txtJnsKamar.setText(model.getValueAt(row, 1).toString());
                txtFasilitas.setText(model.getValueAt(row, 2).toString());
                txtHrgSewa.setText(model.getValueAt(row, 3).toString());
            }
        });

        // Event Tombol Tambah
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sql = "INSERT INTO tjnskamar VALUES (?, ?, ?, ?)";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, txtIdJns.getText());
                    pst.setString(2, txtJnsKamar.getText());
                    pst.setString(3, txtFasilitas.getText());
                    pst.setInt(4, Integer.parseInt(txtHrgSewa.getText()));
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Jenis Kamar Berhasil Disimpan!");
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
                    String sql = "UPDATE tjnskamar SET JnsKamar=?, Fasilitas=?, HrgSewa=? WHERE IdJns=?";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, txtJnsKamar.getText());
                    pst.setString(2, txtFasilitas.getText());
                    pst.setInt(3, Integer.parseInt(txtHrgSewa.getText()));
                    pst.setString(4, txtIdJns.getText());
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
                    String sql = "DELETE FROM tjnskamar WHERE IdJns=?";
                    Connection conn = Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, txtIdJns.getText());
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
            String sql = "SELECT * FROM tjnskamar";
            Connection conn = Koneksi.getKoneksi();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("IdJns"),
                    rs.getString("JnsKamar"),
                    rs.getString("Fasilitas"),
                    rs.getInt("HrgSewa")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Load Data: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtIdJns.setText("");
        txtIdJns.setEditable(true);
        txtJnsKamar.setText("");
        txtFasilitas.setText("");
        txtHrgSewa.setText("");
    }
}