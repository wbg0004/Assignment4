//package edu.auburn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CustomerUI {
    public JFrame view;

    public JButton btnMakePurchase = new JButton("Make Purchase");
    public JButton btnViewPurchases = new JButton("View Purchases");
    public JButton btnCancelPurchase = new JButton("Cancel Purchase");

    public JButton btnLogout = new JButton("Logout");

    public JLabel loginLable = new JLabel("Logged in as - Customer");

    Socket link;
    Scanner input;
    PrintWriter output;
    int mAccessToken;

    public CustomerUI(int accessToken) {
        this.view = new JFrame();
        this.mAccessToken = accessToken;

        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setTitle("Store Management System");
        view.setSize(1000, 600);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel titlePanel = new JPanel(new FlowLayout());
        JLabel title = new JLabel("Store Management System");

        title.setFont (title.getFont ().deriveFont (24.0f));
        titlePanel.add(title);
        view.getContentPane().add(titlePanel);

        JPanel logPanel = new JPanel(new FlowLayout());
        logPanel.add(loginLable);
        view.add(logPanel);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnMakePurchase);
        panelButtons.add(btnViewPurchases);
        panelButtons.add(btnCancelPurchase);
        view.getContentPane().add(panelButtons);

        view.getContentPane().add(btnLogout);

        btnMakePurchase.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        btnViewPurchases.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        btnCancelPurchase.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        btnLogout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    link = new Socket("localhost", 1000);
                    input = new Scanner(link.getInputStream());
                    output = new PrintWriter(link.getOutputStream(), true);

                    output.println(MessageModel.LOGOUT);
                    output.println(mAccessToken);
                    int res = input.nextInt();
                    System.out.println("Sent LOGOUT " + mAccessToken + " received " + res);

                    if (res != MessageModel.LOGOUT_SUCCESS)
                        JOptionPane.showMessageDialog(null, "Invalid token for logout!");
                    else {
                        JOptionPane.showMessageDialog(null, "Logout successfully = " + accessToken);
                        view.setVisible(false);
                    }
                    mAccessToken = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void run() {
        view.setVisible(true);
    }

}
