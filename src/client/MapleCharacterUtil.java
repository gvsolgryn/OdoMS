package client;

import constants.GameConstants;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.world.World;
import tools.Triple;
import tools.packet.CSPacket;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class MapleCharacterUtil {
    private static final Pattern namePattern = Pattern.compile("[a-zA-Z0-9]{4,12}");

    private static final Pattern name2Pattern = Pattern.compile("[가-힣a-zA-Z0-9\\w\\s]{4,12}");

    private static final Pattern petPattern = Pattern.compile("[a-zA-Z0-9]{4,12}");

    public static boolean canCreateChar(String name, boolean gm) {
        return (name.getBytes()).length >= 4 && (name.getBytes()).length <= 13 && getIdByName(name) == -1;
    }

    public static boolean canCreateChar(String name) {
        return (name.getBytes()).length >= 2 && (name.getBytes()).length <= 13;
    }

    public static boolean isEligibleCharName(String name, boolean gm) {
        if (name.length() > 12)
            return false;
        if (gm)
            return true;
        if (name.length() < 3 || !namePattern.matcher(name).matches())
            return false;
        for (String z : GameConstants.RESERVED) {
            if (name.contains(z))
                return false;
        }
        return true;
    }

    public static boolean isEligibleCharNameTwo(String name, boolean gm) {
        if (name.length() > 12)
            return false;
        if (gm)
            return true;
        for (String z : GameConstants.RESERVED) {
            if (name.contains(z))
                return false;
        }
        return true;
    }

    public static boolean canChangePetName(String name) {
        if ((name.getBytes(Charset.forName("MS949"))).length > 12)
            return false;
        if ((name.getBytes(Charset.forName("MS949"))).length < 3)
            return false;
        if (petPattern.matcher(name).matches()) {
            for (String z : GameConstants.RESERVED) {
                if (name.contains(z))
                    return false;
            }
            return true;
        }
        return false;
    }

    public static String makeMapleReadable(String in) {
        String wui = in.replace('I', 'i');

        wui = wui.replace('l', 'L');
        wui = wui.replace("rn", "Rn");
        wui = wui.replace("vv", "Vv");
        wui = wui.replace("VV", "Vv");

        return wui;
    }

    public static int getIdByName(String name) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT id FROM characters WHERE name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            int id = rs.getInt("id");
            rs.close();
            ps.close();
            return id;
        } catch (SQLException e) {
            System.err.println("error 'getIdByName' " + e);
        } finally {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException se) {
                // se.printStackTrace();
                System.err.println(se.getMessage());
            }
        }
        return -1;
    }

    public static int getAccByName(String name) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT accountid FROM characters WHERE name LIKE ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            int id = rs.getInt("accountid");
            rs.close();
            ps.close();
            return id;
        } catch (SQLException e) {
            System.err.println("error 'getIdByName' " + e);
        } finally {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException se) {
                // se.printStackTrace();
                System.err.println(se.getMessage());
            }
        }
        return -1;
    }

    public static int Change_SecondPassword(int AccId, String password, String newPassword) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String SHA1HashedSecond;
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * from accounts where id = ?");
            ps.setInt(1, AccId);
            rs = ps.executeQuery();
                if (!rs.next()) {
                    rs.close();
                    ps.close();
                    return -1;
                }
            String secondPassword = rs.getString("2ndpassword");
            String salt2 = rs.getString("salt2");
            if (secondPassword != null && salt2 != null) {
                secondPassword = LoginCrypto.rand_r(secondPassword);
            } else if (secondPassword == null && salt2 == null) {
                rs.close();
                ps.close();
            return 0;
            }
            if (!check_ifPasswordEquals(secondPassword, password, salt2)) {
              rs.close();
              ps.close();
              return 1;
            }
            rs.close();
            ps.close();
            try {
              SHA1HashedSecond = LoginCryptoLegacy.encodeSHA1(newPassword);
            } catch (Exception e) {
              return -2;
            }
            ps = con.prepareStatement("UPDATE accounts set 2ndpassword = ?, salt2 = ? where id = ?");
            ps.setString(1, SHA1HashedSecond);
            ps.setString(2, null);
            ps.setInt(3, AccId);
            if (!ps.execute()) {
              ps.close();
              return 2;
            }
            ps.close();
            return -2;
        } catch (SQLException e) {
            System.err.println("error 'getIdByName' " + e);
            return -2;
        } finally {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException se) {
                System.err.println("[Err] 2차 비번 변경중 SQL err 발생\r\n" + se.getMessage());
            }
        }
    }

    private static boolean check_ifPasswordEquals(String passHash, String pwd, String salt) {
        if (LoginCryptoLegacy.isLegacyPassword(passHash) && LoginCryptoLegacy.checkPassword(pwd, passHash))
            return true;

        if (salt == null && LoginCrypto.checkSha1Hash(passHash, pwd))
            return true;

        return LoginCrypto.checkSaltedSha512Hash(passHash, pwd, salt);
    }

    public static Triple<Integer, Integer, Integer> getInfoByName(String name, int world) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT * FROM characters WHERE name = ? AND world = ?");
                ps.setString(1, name);
                ps.setInt(2, world);
                rs = ps.executeQuery();
                if (!rs.next()) {
                    rs.close();
                    ps.close();
                    return null;
                }
                Triple<Integer, Integer, Integer> id = new Triple<>(rs.getInt("id"), rs.getInt("accountid"), rs.getInt("gender"));
                rs.close();
                ps.close();
                con.close();
                return id;
            } catch (Exception e) {
                // e.printStackTrace();
                System.err.println(e.getMessage());
            } finally {
                try {
                    if (con != null)
                        con.close();
                    if (ps != null)
                        ps.close();
                    if (rs != null)
                        rs.close();
                } catch (SQLException se) {
                    // se.printStackTrace();
                    System.err.println(se.getMessage());
                }
            }
        return null;
    }

    public static void setNXCodeUsed(String name, String code) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE nxcode SET `user` = ?, `valid` = 0 WHERE code = ?");
            ps.setString(1, name);
            ps.setString(2, code);
            ps.execute();
            ps.close();
            con.close();
        } catch (Exception exception) {
            try {
                if (con != null)
                  con.close();
                if (ps != null)
                  ps.close();
            } catch (SQLException se) {
                // se.printStackTrace();
                System.err.println(se.getMessage());
            }
        } finally {
            try {
                if (con != null)
                  con.close();
                if (ps != null)
                  ps.close();
            } catch (SQLException se) {
                //se.printStackTrace();
                System.err.println(se.getMessage());
            }
        }
    }

    public static void sendNote(String to, String name, String msg, int fame, int type, int senderid) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("INSERT INTO notes (`to`, `from`, `message`, `timestamp`, `gift`, `show`, `type`, `senderid`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, to);
            ps.setString(2, name);
            ps.setString(3, msg);
            ps.setLong(4, System.currentTimeMillis());
            ps.setInt(5, fame);
            ps.setInt(6, 1);
            ps.setInt(7, type);
            ps.setInt(8, senderid);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Unable to send note" + e);
        } finally {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
            } catch (SQLException se) {
                System.err.println(se.getMessage());
            }
        }
        try {
            if (World.Find.findChannel(to) >= 0) {
                MapleCharacter chr = ChannelServer.getInstance(World.Find.findChannel(to)).getPlayerStorage().getCharacterByName(to);
                if (chr != null)
                    chr.getClient().send(CSPacket.NoteHandler(16, 0));
            }
        }
        catch (Exception ex) {
            System.err.println("[Err] SendNote Err : " + ex);
        }
    }

    public static Triple<Boolean, Integer, Integer> getNXCodeInfo(String code) throws SQLException {
        Triple<Boolean, Integer, Integer> ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT `valid`, `type`, `item` FROM nxcode WHERE code LIKE ?");
            ps.setString(1, code);
            rs = ps.executeQuery();
            if (rs.next())
                ret = new Triple<>(rs.getInt("valid") > 0, rs.getInt("type"), rs.getInt("item"));
            rs.close();
            ps.close();
            con.close();
        } catch (Exception exception) {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException se) {
                // se.printStackTrace();
                System.err.println(se.getMessage());
            }
        } finally {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException se) {
                // se.printStackTrace();
                System.err.println(se.getMessage());
            }
        }
        return ret;
    }
}
