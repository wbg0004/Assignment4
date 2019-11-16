import java.io.PrintWriter;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class StoreServer {
    static String dbfile = "/Users/willgarrison/Documents/Software Engineering/Fall 2019/COMP 3700/StoreManagement.db";

    public static void main(String[] args) {
        HashMap<Integer, UserModel> activeUsers = new HashMap<Integer, UserModel>();

        int totalActiveUsers = 0;

        int port = 1000;

        if (args.length > 0) {
            System.out.println("Running arguments: ");
            for (String arg : args)
                System.out.println(arg);
            port = Integer.parseInt(args[0]);
            dbfile = args[1];
        }

        try {
            ServerSocket server = new ServerSocket(port);

            System.out.println("Server is listening at port = " + port);

            while (true) {
                Socket pipe = server.accept();
                PrintWriter out = new PrintWriter(pipe.getOutputStream(), true);
                Scanner in = new Scanner(pipe.getInputStream());

                int command = Integer.parseInt(in.nextLine());
                if (command == MessageModel.LOGIN) {
                    String username = in.nextLine();
                    String password = in.nextLine();

                    Connection conn = null;
                    try {
                        String url = "jdbc:sqlite:" + dbfile;
                        conn = DriverManager.getConnection(url);

                        String sql = "SELECT * FROM User WHERE Username = \"" + username + "\"";
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            if (rs.getString("Password").equals(password)) {
                                out.println(MessageModel.LOGIN_SUCCESS);
                                String fullName = rs.getString("FullName");
                                String userType = rs.getString("userType");
                                String customerID = rs.getString("CustomerID");
                                if (customerID == null) {
                                    customerID = "0";
                                }
                                out.println(username);
                                out.println(password);
                                out.println(fullName);
                                out.println(userType);
                                out.println(customerID);

                                UserModel user = new UserModel(username, password, fullName, Integer.parseInt(userType), Integer.parseInt(customerID));

                                int key = totalActiveUsers;
                                while (activeUsers.containsKey(key)) {
                                    key++;
                                }
                                activeUsers.put(key, user);
                                out.println(key);
                                totalActiveUsers++;
                            }
                            else {
                                out.println(MessageModel.LOGIN_WRONG_PASS);
                            }
                        }
                        else {
                            out.println(MessageModel.LOGIN_WRONG_USER);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();
                }
                else if (command == MessageModel.LOGOUT) {
                    try {
                        int key = Integer.parseInt(in.nextLine());
                        out.println(MessageModel.LOGOUT_SUCCESS);
                        activeUsers.remove(key);
                        totalActiveUsers--;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (command == MessageModel.GET_PRODUCT) {
                    String str = in.nextLine();
                    System.out.println("GET product with id = " + str);
                    int productID = Integer.parseInt(str);

                    Connection conn = null;
                    try {
                        String url = "jdbc:sqlite:" + dbfile;
                        conn = DriverManager.getConnection(url);

                        String sql = "SELECT * FROM Product WHERE ProductID = " + productID;
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            out.println(rs.getString("Name")); // send back product name!
                            out.println(rs.getDouble("Price")); // send back product price!
                            out.println(rs.getDouble("TaxRate")); // send back product tax rate!
                            out.println(rs.getDouble("Quantity")); // send back product quantity!
                            out.println(rs.getString("Vendor")); // send back product vendor!
                            out.println(rs.getString("Description")); // send back product description!
                        }
                        else
                            out.println("null");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();
                }

                else if (command == MessageModel.PUT_PRODUCT) {
                    String id = in.nextLine();  // read all information from client
                    String name = in.nextLine();
                    String price = in.nextLine();
                    String taxRate = in.nextLine();
                    String quantity = in.nextLine();
                    String vendor = in.nextLine();
                    String description = in.nextLine();

                    System.out.println("PUT command with ProductID = " + id);

                    Connection conn = null;
                    try {
                        String url = "jdbc:sqlite:" + dbfile;
                        conn = DriverManager.getConnection(url);

                        String sql = "SELECT * FROM Product WHERE ProductID = " + id;
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            rs.close();
                            stmt.execute("DELETE FROM Product WHERE ProductID = " + id);
                        }

                        sql = "INSERT INTO Product VALUES (" + id + ",\"" + name + "\","
                                + price + "," + taxRate + "," + quantity + ",\"" + vendor
                                + "\",\"" + description + "\")";
                        System.out.println("SQL for PUT: " + sql);
                        stmt.execute(sql);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();
                }
                else if (command == MessageModel.GET_CUSTOMER) {
                    String str = in.nextLine();
                    System.out.println("GET customer with id = " + str);
                    int customerID = Integer.parseInt(str);

                    Connection conn = null;
                    try {
                        String url = "jdbc:sqlite:" + dbfile;
                        conn = DriverManager.getConnection(url);

                        String sql = "SELECT * FROM Customer WHERE CustomerID = " + customerID;
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            out.println(rs.getString("Name")); // send back customer name!
                            out.println(rs.getString("Address")); // send back customer address!
                            out.println(rs.getString("Phone")); // send back customer phone number!
                            out.println(rs.getString("PaymentInfo")); // send back customer payment info!
                        }
                        else
                            out.println("null");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();
                }
                else if (command == MessageModel.PUT_CUSTOMER) {
                    String id = in.nextLine();  // read all information from client
                    String name = in.nextLine();
                    String address = in.nextLine();
                    String phone = in.nextLine();
                    String paymentInfo = in.nextLine();

                    System.out.println("PUT command with CustomerID = " + id);

                    Connection conn = null;
                    try {
                        String url = "jdbc:sqlite:" + dbfile;
                        conn = DriverManager.getConnection(url);

                        String sql = "SELECT * FROM Customer WHERE CustomerID = " + id;
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            rs.close();
                            stmt.execute("DELETE FROM Customer WHERE Customer = " + id);
                        }

                        sql = "INSERT INTO Customer VALUES (" + id + ",\"" + name + "\",\""
                                + address + "\",\"" + phone
                                + "\",\"" + paymentInfo + "\")";
                        System.out.println("SQL for PUT: " + sql);
                        stmt.execute(sql);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();
                }
                else {
                    out.println(0); // logout unsuccessful!
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}