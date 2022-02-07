import java.sql.*;
import java.util.Objects;
import java.util.Scanner;


public class Main {
    static Scanner sc = new Scanner(System.in);

    public static String ask(String s) {
        System.out.println(s);
        return sc.nextLine();
    }

    public static void main(String[] args) throws SQLException {
        System.out.println("hello!");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "sql");
        PreparedStatement login_query = conn.prepareStatement("select role from user where ID = ?");
        Statement st = conn.createStatement();
        String ID, role;
        while (true) {
            ID = ask("Enter your ID");
            login_query.setString(1, ID);
            ResultSet rs = login_query.executeQuery();
            if (!rs.next()) {
                System.out.println("Invalid ID");
                continue;
            }
            role = rs.getString("role");
            break;
        }
        if (Objects.equals(role, "student")) {
            while (true) {
                int ans = Integer.parseInt(ask("""
                        1. Search books?
                        2. Check status\s
                        3. Exit
                        """));
                if (ans == 3) break;
                else if (ans == 2) {
                    String q = "select * from combined_history where Student_id=" + ID;
                    DBTablePrinter.printResultSet(st.executeQuery(q));
                } else if (ans == 1) {
                    String book = ask("Enter book name");
                    String q = "select * from books where name like '%" + book + "%'";
                    System.out.println(q);
                    DBTablePrinter.printResultSet(st.executeQuery(q));
                } else {
                    System.out.println("Invalid input!!");
                }
            }
        } else {
            // for admin
            while (true) {
                int ans = Integer.parseInt(ask("Welcome back !!\n 1. View student ledger\n 2. Manage books\n 3. Exit"));
                if (ans == 3) break;
                else if (ans == 1) {
                    String q = "select * from combined_history where Student_id=" + ask("Enter student ID");
                    DBTablePrinter.printResultSet(st.executeQuery(q));
                } else if (ans == 2) {
                    while (true) {
                        int action = Integer.parseInt(ask("1. Add book\n2. Delete book \n3. Exit\n"));
                        if (action == 1) {
                            String book = ask("Provide name of the book");
                            String author = ask("Provide name of the author");
                            String q = "insert into books (name, author_name) values('" + book + "', '" + author + "')";
                            if (!st.execute(q)) {
                                System.out.println("Added a new book to the library :)");
                            } else {
                                System.out.println("Could not add :(");
                            }
                        } else if (action == 2) {
                            String q = "delete from books where ID=" + ask("Enter book's ID");
                            if (!st.execute(q)) {
                                System.out.println("Deleted book form the library");
                            } else {
                                System.out.println("No such ID");
                            }
                        } else if (action == 3) {
                            break;
                        } else {
                            System.out.println("Enter valid input!\n");
                        }
                    }
                } else {
                    System.out.println("Invalid input !!");
                }
            }


        }

    }


}
// ID
// if admin then
// 1. add,dlt,update, edit
// 2. view student ledger
// else
// 1. status
// 2. search book by name
//