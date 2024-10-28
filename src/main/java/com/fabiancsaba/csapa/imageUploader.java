package com.fabiancsaba.csapa;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class imageUploader {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "password";


        String imagesFolderPath = "C:\\work\\csapa\\src\\main\\resources\\images";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {


            File imagesFolder = new File(imagesFolderPath);
            File[] imageFiles = imagesFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg") || name.toLowerCase().endsWith(".png"));

            if (imageFiles != null) {
                for (File imageFile : imageFiles) {
                    String imageName = imageFile.getName().substring(0, imageFile.getName().lastIndexOf('.'));

                    try (InputStream input = new FileInputStream(imageFile)) {

                        String sql = "UPDATE filmek SET film_kep = ? WHERE cim = ?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setBinaryStream(1, input);
                        stmt.setString(2, imageName);
                        int rowsUpdated = stmt.executeUpdate();

                        if (rowsUpdated > 0) {
                            System.out.println("Image uploaded successfully for film with title: " + imageName);
                        } else {
                            System.out.println("No film found with title: " + imageName);
                        }
                    } catch (Exception e) {
                        System.out.println("Error uploading image for file: " + imageFile.getName());
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("No images found in the specified directory.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}