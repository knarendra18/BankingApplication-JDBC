package com.HCL.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.HCL.bank.bean.TransferBean;
import com.HCL.bank.util.DBUtil;

public class BankDAO {

    // 1️⃣ Validate account number
    public boolean validateAccount(String accountNumber) {

        boolean isValid = false;
        String sql = "SELECT ACCOUNT_NUMBER FROM ACCOUNT_TBL WHERE ACCOUNT_NUMBER = ?";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                isValid = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    // 2️⃣ Find balance
    public float findBalance(String accountNumber) {

        float balance = -1;
        String sql = "SELECT BALANCE FROM ACCOUNT_TBL WHERE ACCOUNT_NUMBER = ?";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                balance = rs.getFloat("BALANCE");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balance;
    }

    // 3️⃣ Update balance
    public boolean updateBalance(String accountNumber, float newBalance) {

        boolean isUpdated = false;
        String sql = "UPDATE ACCOUNT_TBL SET BALANCE = ? WHERE ACCOUNT_NUMBER = ?";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setFloat(1, newBalance);
            ps.setString(2, accountNumber);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                isUpdated = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    // 4️⃣ Generate transaction ID
    public int generateSequenceNumber() {

        int transactionId = 0;
        String sql = "SELECT transactionId_seq.NEXTVAL FROM dual";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                transactionId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactionId;
    }

    // 5️⃣ Insert transfer record
    public boolean transferMoney(TransferBean transferBean) {

        boolean isInserted = false;

        String sql = "INSERT INTO TRANSFER_TBL VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, transferBean.getTransactionID());
            ps.setString(2, transferBean.getFromAccountNumber());
            ps.setString(3, transferBean.getToAccountNumber());
            ps.setDate(4, new java.sql.Date(transferBean.getDateOfTransaction().getTime()));
            ps.setFloat(5, transferBean.getAmount());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                isInserted = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isInserted;
    }
}
