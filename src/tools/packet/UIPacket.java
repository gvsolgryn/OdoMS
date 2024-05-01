/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the 
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tools.packet;

import constants.GameConstants;
import handling.SendPacketOpcode;
import tools.MaplePacketCreator;
import tools.data.MaplePacketLittleEndianWriter;

public class UIPacket {

    public static byte[] getSPMsg(byte sp, short job) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(4);
        mplew.writeShort(job);
        mplew.write(sp);

        return mplew.getPacket();
    }

    public static byte[] getGPMsg(int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        // Temporary transformed as a dragon, even with the skill ......
        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(7);
        mplew.writeInt(itemid);

        return mplew.getPacket();
    }

    public static byte[] getBPMsg(int amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        // Temporary transformed as a dragon, even with the skill ......
        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(GameConstants.GMS ? 0x15 : 0x17);
        mplew.writeInt(amount);

        return mplew.getPacket();
    }

    public static byte[] getGPContribution(int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        // Temporary transformed as a dragon, even with the skill ......
        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(8);
        mplew.writeInt(itemid);

        return mplew.getPacket();
    }

    public static byte[] getTopMsg(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TOP_MSG.getValue());
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] getMidMsg(String msg, boolean keep, int index) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MID_MSG.getValue());
        mplew.write(index); //where the message should appear on the screen
        mplew.writeMapleAsciiString(msg);
        mplew.write(keep ? 0 : 1);

        return mplew.getPacket();
    }

    public static byte[] clearMidMsg() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CLEAR_MID_MSG.getValue());

        return mplew.getPacket();
    }

    public static byte[] getStatusMsg(int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        // Temporary transformed as a dragon, even with the skill ......
        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(9);
        mplew.writeInt(itemid);

        return mplew.getPacket();
    }

    public static final byte[] MapEff(final String path) {
        return MaplePacketCreator.environmentChange(path, 3);
    }

    public static final byte[] MapNameDisplay(final int mapid) {
        return MaplePacketCreator.environmentChange("maplemap/enter/" + mapid, 3);
    }

    public static final byte[] Aran_Start() {
        return MaplePacketCreator.environmentChange("Aran/balloon", 4);
    }

    public static final byte[] AranTutInstructionalBalloon(final String data) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
        mplew.write(23);
        mplew.writeMapleAsciiString(data);
        mplew.writeInt(1);

        return mplew.getPacket();
    }

    public static final byte[] ShowWZEffect(final String data) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
        mplew.write(20); //bb +2
        mplew.writeMapleAsciiString(data);

        return mplew.getPacket();
    }

    public static byte[] summonHelper(boolean summon) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SUMMON_HINT.getValue());
        mplew.write(summon ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] summonMessage(int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SUMMON_HINT_MSG.getValue());
        mplew.write(1);
        mplew.writeInt(type);
        mplew.writeInt(7000); // probably the delay

        return mplew.getPacket();
    }

    public static byte[] summonMessage(String message) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SUMMON_HINT_MSG.getValue());
        mplew.write(0);
        mplew.writeMapleAsciiString(message);
        mplew.writeInt(200); // IDK
        mplew.writeShort(0);
        mplew.writeInt(10000); // Probably delay

        return mplew.getPacket();
    }

    public static byte[] IntroLock(boolean enable) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_LOCK.getValue());
        mplew.write(enable ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] IntroDisableUI(boolean enable) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_DISABLE_UI.getValue());
        mplew.write(enable ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] sendMacro(byte state1, byte state2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ANTI_MACRO.getValue());
        mplew.write(6);
        mplew.write(4);
        mplew.write(1);
        if (state1 == -1) {
            mplew.writeInt(0);
            return mplew.getPacket();
        }
        mplew.writeInt(state1);
        mplew.write(state2);
        return mplew.getPacket();
    }

    public static byte[] AntiMacro(byte msg, byte msg2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ANTI_MACRO.getValue());
        mplew.write(msg);
        mplew.write(msg2);
        return mplew.getPacket();
    }
}
