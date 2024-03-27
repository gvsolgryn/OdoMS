/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import java.sql.*;
import java.util.*;

/**
 *
 * @author user
 */
public class MapleLinkSkillPreset {
    private int position;
    
    private int skillid;
    
    private int skillchar;
    
    public MapleLinkSkillPreset(final int pos, final int skill, final int charid) {
        this.position = pos;
        this.skillid = skill;
        this.skillchar = charid;
    }
    
    public MapleLinkSkillPreset MapleLinkSkillPreset(final int pos, final int skill, final int charid) {
        this.position = pos;
        this.skillid = skill;
        this.skillchar = charid;
        return this;
    }
    
    public void SaveToDB(final Connection connection) throws SQLException {
        try
        {
            final PreparedStatement lsp = connection.prepareStatement("UPDATE LinkPreset SET charid = ?, skillid = ?, skillchar = ?, preset = ?, WHERE id = ?");
            lsp.setInt(1, 1);
            lsp.setInt(2,this.skillid);
            lsp.setInt(3, this.skillchar);
            lsp.setInt(4,this.position);
        } catch(Exception ex) {
            System.out.println("LinkPreset SaveToDB Error: "+ex);
        }
    }
    
    public static ArrayList<MapleLinkSkillPreset> loadFromDB(final Connection connection, final int characterid) {
        final ArrayList<MapleLinkSkillPreset> LinkSkillPreset = new ArrayList<MapleLinkSkillPreset>();
        return LinkSkillPreset;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public void setPosition(final int position) {
        this.position = position;
    }
    
    public int getSkillid() {
        return this.skillid;
    }
    
    public void setSkillid(final int skillid) {
        this.skillid = skillid;
    }
    
    public int getSkillCharid() {
        return this.skillchar;
    }
    
    public void setSkillCharid(final int charid) {
        this.skillchar = charid;
    }
}
