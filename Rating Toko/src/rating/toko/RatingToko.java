/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package rating.toko;

/**
 *
 * @author This PC
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class RatingToko {

    private JFrame frame;
    private JPanel panel;
    private JComboBox<String> tokoComboBox;
    private JSpinner ratingSpinner;
    private JButton saveButton;
    private JTable ratingTable;
    private DefaultTableModel model;

    private Map<String, Double> averageRatings = new HashMap<>();
    private Connection connection;

    public RatingToko() {
        try {
            // Koneksi database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ratingtoko",
                    "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.");
            System.exit(0);
        }

        frame = new JFrame("Rating Toko");
        frame.setSize(700, 400);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel(new FlowLayout());

        // Untuk memilih toko
        tokoComboBox = new JComboBox<>(new String[]{"All Stores", "Toko Mainan", "Toko Makanan"});
        panel.add(tokoComboBox);

        // Untuk input atau pilih rating
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 5, 1);
        ratingSpinner = new JSpinner(spinnerModel);
        panel.add(ratingSpinner);

        // Save Button
        saveButton = new JButton("Save");
        panel.add(saveButton);

        // Filter ComboBox
        JComboBox<String> filterComboBox = new JComboBox<>(new String[]{"All Stores", "Toko Mainan", "Toko Makanan"});
        panel.add(filterComboBox);

        // Filter Button
        JButton filterButton = new JButton("Filter");
        panel.add(filterButton);

        // Table to display ratings
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Customer Name");
        model.addColumn("Store");
        model.addColumn("Rating");
        model.addColumn("Timestamp");

        ratingTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(ratingTable);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRating();
            }
        });

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterRatings((String) filterComboBox.getSelectedItem());
            }
        });

        loadData(); // Memuat data dari database

        frame.setVisible(true);
    }

    private void saveRating() {
        String customerName = JOptionPane.showInputDialog("Enter Customer Name:");

        if (customerName != null && !customerName.trim().isEmpty()) {
            String store = (String) tokoComboBox.getSelectedItem();
            int rating = (int) ratingSpinner.getValue();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            try {
                // Insert rating baru ke database
                String query = "INSERT INTO seller_rating (customer_name, seller_name, rating, created_at) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, customerName);
                    ps.setString(2, store);
                    ps.setInt(3, rating);
                    ps.setString(4, timestamp);

                    int affectedRows = ps.executeUpdate();

                    if (affectedRows > 0) {
                        // Get the auto-generated ID
                        ResultSet generatedKeys = ps.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int id = generatedKeys.getInt(1);
                            // Add data pada tabel
                            model.addRow(new Object[]{id, customerName, store, rating, timestamp});
                        }
                    }
                }

                // Update average rating toko
                updateAverageRating(store, rating);

                // Clear the spinner value for the next input
                ratingSpinner.setValue(1);

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to save rating.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Customer Name cannot be empty.");
        }
    }

    private void updateAverageRating(String store, int newRating) {
        double currentAverage = averageRatings.getOrDefault(store, 0.0);
        int totalRatings = model.getRowCount();

        // Hitung average rating baru
        double newAverage = (currentAverage * (totalRatings - 1) + newRating) / totalRatings;

        // Update average rating
        averageRatings.put(store, newAverage);

        // Menampilkan rata-rata
        JOptionPane.showMessageDialog(frame, "Average Rating for " + store + ": " + newAverage);
    }

    private void filterRatings(String selectedStore) {
        model.setRowCount(0); // Clear existing rows in the table

        try {
            // Menampilkan data berdasarkan data yang ada dari database
            String query;
            if ("All Stores".equals(selectedStore)) {
                query = "SELECT * FROM seller_rating";
            } else {
                query = "SELECT * FROM seller_rating WHERE seller_name = '" + selectedStore + "'";
            }

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String customerName = resultSet.getString("customer_name");
                    String store = resultSet.getString("seller_name");
                    int rating = resultSet.getInt("rating");
                    String timestamp = resultSet.getString("created_at");

                    // Menambahkan data pada tabel
                    model.addRow(new Object[]{id, customerName, store, rating, timestamp});

                    // Update average rating toko
                    updateAverageRating(store, rating);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to load data from the database.");
        }
    }

    private void loadData() {
        try {
            // Menampilkan data dari database
            String query = "SELECT * FROM seller_rating";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String customerName = resultSet.getString("customer_name");
                    String store = resultSet.getString("seller_name");
                    int rating = resultSet.getInt("rating");
                    String timestamp = resultSet.getString("created_at");

                    // Menambahkan data pada tabel
                    model.addRow(new Object[]{id, customerName, store, rating, timestamp});

                    // Update average rating toko
                    updateAverageRating(store, rating);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to load data from the database.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RatingToko::new);
    }
}