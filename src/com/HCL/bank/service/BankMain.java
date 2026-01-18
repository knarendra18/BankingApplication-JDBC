package com.HCL.bank.service;

import java.util.Date;

import com.HCL.bank.bean.TransferBean;
import com.HCL.bank.dao.BankDAO;
import com.HCL.bank.util.InsufficientFundsException;

public class BankMain {

    BankDAO bankDAO = new BankDAO();

    // 1️⃣ Check balance
    public String checkBalance(String accountNumber) {

        boolean isValid = bankDAO.validateAccount(accountNumber);

        if (!isValid) {
            return "ACCOUNT NUMBER INVALID";
        }

        float balance = bankDAO.findBalance(accountNumber);

        return "BALANCE IS: " + balance;
    }

    // 2️⃣ Transfer money
    public String transfer(TransferBean transferBean) {

        if (transferBean == null) {
            return "INVALID";
        }

        String fromAcc = transferBean.getFromAccountNumber();
        String toAcc = transferBean.getToAccountNumber();
        float amount = transferBean.getAmount();

        // Validate accounts
        if (!bankDAO.validateAccount(fromAcc) || !bankDAO.validateAccount(toAcc)) {
            return "INVALID ACCOUNT";
        }

        float fromBalance = bankDAO.findBalance(fromAcc);

        try {
            // Check insufficient funds
            if (fromBalance < amount) {
                throw new InsufficientFundsException();
            }

            // Generate transaction ID
            int transactionId = bankDAO.generateSequenceNumber();
            transferBean.setTransactionID(transactionId);
            transferBean.setDateOfTransaction(new Date());

            // Update balances
            bankDAO.updateBalance(fromAcc, fromBalance - amount);
            float toBalance = bankDAO.findBalance(toAcc);
            bankDAO.updateBalance(toAcc, toBalance + amount);

            // Insert transaction record
            bankDAO.transferMoney(transferBean);

            return "SUCCESS";

        } catch (InsufficientFundsException e) {
            return e.toString();
        }
    }

    // 3️⃣ MAIN METHOD FOR TESTING
    public static void main(String[] args) {

        BankMain bankMain = new BankMain();

        // Check balance
        System.out.println(bankMain.checkBalance("1234567890"));

        // Transfer money
        TransferBean tb = new TransferBean();
        tb.setFromAccountNumber("1234567890");
        tb.setToAccountNumber("1234567891");
        tb.setAmount(500);

        System.out.println(bankMain.transfer(tb));

        // Check balance again
        System.out.println(bankMain.checkBalance("1234567890"));
        System.out.println(bankMain.checkBalance("1234567891"));
    }
}
