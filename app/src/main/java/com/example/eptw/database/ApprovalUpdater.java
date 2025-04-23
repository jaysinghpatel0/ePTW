package com.example.eptw.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ApprovalUpdater {

    public static void updateApproval(String imagePath, String latitude, String longitude, String deviceID, String ipAddress) {
        String sql = "UPDATE GWP_Approval SET ImagePath1=?, Latitude=?, Logitude=?, DeviceType='Tab', DeviceID=?, IPAddress=? WHERE PermitID='5' AND PermitNo='NGD/FY25/3'";

        try (Connection conn = MSSQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, imagePath);
            stmt.setString(2, latitude);
            stmt.setString(3, longitude);
            stmt.setString(4, deviceID);
            stmt.setString(5, ipAddress);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("No record updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
