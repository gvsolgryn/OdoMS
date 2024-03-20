package constants.programs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.MYSQL;

public class SpiritDel {

    public static void main(String args[]) {
        try {
            Connection con = MYSQL.getConnection();
            ResultSet sql = con.prepareStatement("SELECT * FROM accounts").executeQuery();
            PreparedStatement ps = con.prepareStatement("DELETE FROM accounts WHERE id = ?");
            while (sql.next()) {
                if (!CheckChar(con, sql.getInt("id"))) {
                    ps.setInt(1, sql.getInt("id"));
                    ps.executeUpdate();
                    System.out.println(sql.getString("name") + "계정 삭제");
                }
            }
            ps.close();
            sql.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean CheckChar(Connection con, int i) {
        try {
            ResultSet sql = con.prepareStatement("SELECT * FROM characters WHERE accountid =" + i).executeQuery();
            if (sql.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
