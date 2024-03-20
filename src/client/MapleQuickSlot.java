package client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import constants.ServerConstants;
import database.MYSQL;
import tools.Pair;

public class MapleQuickSlot {

    private List<Pair<Integer, Integer>> quickslot = new ArrayList<Pair<Integer, Integer>>();
    private int cid;

    public MapleQuickSlot(int cid) {
        this.cid = cid;
    }

    public List<Pair<Integer, Integer>> getQuickSlot() {
        return quickslot;
    }

    public void loadFromDB() {
        Connection con = null;
        try {
            con = MYSQL.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM quickslot WHERE cid = ?");
            ps.setInt(1, cid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quickslot.add(new Pair<Integer, Integer>(rs.getInt("index"), rs.getInt("key")));
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            if (!ServerConstants.realese) {
                e.printStackTrace();
            }
            System.err.println("[오류] 퀵슬롯을 불러오는데 실패했습니다.");
        }
    }

    public void saveToDB() {
        Connection con = null;
        try {
            con = MYSQL.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM quickslot WHERE cid = ?");
            ps.setInt(1, cid);
            ps.executeUpdate();
            ps.close();
            for (Pair<Integer, Integer> p : quickslot) {
                ps = con.prepareStatement("INSERT INTO quickslot VALUES (?, ?, ?)");
                ps.setInt(1, cid);
                ps.setInt(2, p.getLeft());
                ps.setInt(3, p.getRight());
                ps.executeUpdate();
                ps.close();
            }
            con.close();
        } catch (Exception e) {
            if (!ServerConstants.realese) {
                e.printStackTrace();
            }
            System.err.println("[오류] 퀵슬롯을 저장하는데 실패했습니다.");
        }
    }

    public void resetQuickSlot() {
        quickslot.clear();
    }

    public void addQuickSlot(int index, int key) {
        quickslot.add(new Pair<Integer, Integer>(index, key));
    }

    public int getKeyByIndex(int index) {
        for (Pair<Integer, Integer> p : quickslot) {
            if (p.getLeft() == index) {
                return p.getRight();
            }
        }
        return -1;
    }

}
