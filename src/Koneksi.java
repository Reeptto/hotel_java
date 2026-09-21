import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {
    private static Connection koneksi;

    public static Connection getKoneksi() {
        if (koneksi == null) {
            try {
                // Konfigurasi koneksi ke database Hotel di Laragon
                String url = "jdbc:mysql://localhost:3306/Hotel";
                String user = "root";
                String password = ""; // Default Laragon dikosongkan

                Class.forName("com.mysql.cj.jdbc.Driver");
                koneksi = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Driver MySQL tidak ditemukan: " + e.getMessage());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Koneksi ke Database Gagal: " + e.getMessage());
            }
        }
        return koneksi;
    }
}