package server.life;

import java.awt.Point;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//파이날 HP import constants.FinalMaxHpConstants;
import constants.ServerConstants;
import database.MYSQL;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import provider.WzXML.MapleDataType;
import tools.Pair;
import tools.StringUtil;

public class MapleLifeProvider {

    private static final MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File("wz/Mob.wz"));
    private static final MapleDataProvider stringDataWZ = MapleDataProviderFactory
            .getDataProvider(new File("wz/String.wz"));
    private static final MapleDataProvider etcDataWZ = MapleDataProviderFactory.getDataProvider(new File("wz/Etc.wz"));
    private static final MapleData mobStringData = stringDataWZ.getData("Mob.img");
    private static final MapleData npcStringData = stringDataWZ.getData("Npc.img");
    private static final MapleData npclocData = etcDataWZ.getData("NpcLocation.img");
    private static Map<Integer, MapleMonsterStats> monsterStats = new HashMap<Integer, MapleMonsterStats>();
    private static Map<Integer, Integer> NPCLoc = new HashMap<Integer, Integer>();

    public static AbstractLoadedMapleLife getLife(int id, String type) {
        if (type.equalsIgnoreCase("n")) {
            return getNPC(id);
        } else if (type.equalsIgnoreCase("m")) {
            return getMonster(id);
        } else {
            System.err.println("Unknown Life type: " + type + "");
            return null;
        }
    }

    public static int getNPCLocation(int npcid) {
        if (NPCLoc.containsKey(npcid)) {
            return NPCLoc.get(npcid);
        }
        final int map = MapleDataTool.getIntConvert(Integer.toString(npcid) + "/0", npclocData);
        NPCLoc.put(npcid, map);
        return map;
    }

    public static MapleMonster getMonster(int mid) {
        MapleMonsterStats stats = monsterStats.get(Integer.valueOf(mid));

        if (stats == null) {
            MapleData monsterData = data.getData(StringUtil.getLeftPaddedStr(Integer.toString(mid) + ".img", '0', 11));
            if (monsterData == null) {
                return null;
            }
            MapleData monsterInfoData = monsterData.getChildByPath("info");
            stats = new MapleMonsterStats();
            try {
                stats.setHp(MapleDataTool.getIntConvert("maxHP", monsterInfoData));
            } catch (Exception ex) {
                stats.setHp(Long.MAX_VALUE);// 임시
            }
            stats.setMp(MapleDataTool.getIntConvert("maxMP", monsterInfoData, 0));
            stats.setExp(MapleDataTool.getIntConvert("exp", monsterInfoData, 0));
            stats.setLevel((short) MapleDataTool.getIntConvert("level", monsterInfoData));
            stats.setRemoveAfter(MapleDataTool.getIntConvert("removeAfter", monsterInfoData, 0));
            stats.setrareItemDropLevel((byte) MapleDataTool.getIntConvert("rareItemDropLevel", monsterInfoData, 0));
            stats.setFixedDamage(MapleDataTool.getIntConvert("fixedDamage", monsterInfoData, -1));
            stats.setOnlyNormalAttack(MapleDataTool.getIntConvert("onlyNormalAttack", monsterInfoData, 0) > 0);
            stats.setRealBoss(MapleDataTool.getIntConvert("hpTagBgcolor", monsterInfoData, 0) > 0);
            stats.setBoss(MapleDataTool.getIntConvert("boss", monsterInfoData, 0) > 0 || mid == 8810018
                    || (mid >= 8810118 && mid <= 8810122) || mid == 9410066);
            stats.setSkeleton(MapleDataTool.getIntConvert("skeleton", monsterInfoData, 0) > 0);
            stats.setExplosiveReward(MapleDataTool.getIntConvert("explosiveReward", monsterInfoData, 0) > 0);
            stats.setFfaLoot(MapleDataTool.getIntConvert("publicReward", monsterInfoData, 0) > 0);
            stats.setUndead(MapleDataTool.getIntConvert("undead", monsterInfoData, 0) > 0);
            stats.setName(MapleDataTool.getString(mid + "/name", mobStringData, "MISSINGNO"));
            stats.setBuffToGive(MapleDataTool.getIntConvert("buff", monsterInfoData, -1));
            stats.setFriendly(MapleDataTool.getIntConvert("damagedByMob", monsterInfoData, 0) > 0);
            stats.setCP((byte) MapleDataTool.getIntConvert("getCP", monsterInfoData, 0));
            stats.setPhysicalDefense((short) MapleDataTool.getIntConvert("PDDamage", monsterInfoData, 0));
            stats.setMagicDefense((short) MapleDataTool.getIntConvert("MDDamage", monsterInfoData, 0));
            stats.setEva((short) MapleDataTool.getIntConvert("eva", monsterInfoData, 0));
            stats.setCharismaEXP(MapleDataTool.getIntConvert("charismaEXP", monsterInfoData, 0));
            stats.setChangeableMob(MapleDataTool.getIntConvert("changeableMob", monsterInfoData, 0) == 1);
            stats.setMad(MapleDataTool.getIntConvert("MADamage", monsterInfoData, 0));
            stats.setPad(MapleDataTool.getIntConvert("PADamage", monsterInfoData, 0));
            stats.setAcc(MapleDataTool.getIntConvert("acc", monsterInfoData, 0));
            stats.setSpeed(MapleDataTool.getIntConvert("speed", monsterInfoData, 0));
            if (MapleDataTool.getLongConvert("finalmaxHP", monsterInfoData) > 0L) {
                stats.setHp(stats.getHp() + MapleDataTool.getLongConvert("finalmaxHP", monsterInfoData));
            }
            if (stats.getLevel() > 15) {
                if (!stats.isBoss()) {
                    stats.setPDRate((byte) (MapleDataTool.getIntConvert("PDRate", monsterInfoData, 0)));
                    stats.setMDRate((byte) (MapleDataTool.getIntConvert("MDRate", monsterInfoData, 0)));
                } else {
                    stats.setPDRate((byte) (MapleDataTool.getIntConvert("PDRate", monsterInfoData, 0)));
                    stats.setMDRate((byte) (MapleDataTool.getIntConvert("MDRate", monsterInfoData, 0)));
                }
            } else {
                stats.setPDRate((byte) MapleDataTool.getIntConvert("PDRate", monsterInfoData, 0));
                stats.setMDRate((byte) MapleDataTool.getIntConvert("MDRate", monsterInfoData, 0));
            }
            final MapleData selfd = monsterInfoData.getChildByPath("selfDestruction");
            if (selfd != null) {
                stats.setSelfDHP(MapleDataTool.getIntConvert("hp", selfd, 0));
                stats.setSelfD((byte) MapleDataTool.getIntConvert("action", selfd, -1));
            } else {
                stats.setSelfD((byte) -1);
            }

            final MapleData firstAttackData = monsterInfoData.getChildByPath("firstAttack");
            if (firstAttackData != null) {
                if (firstAttackData.getType() == MapleDataType.FLOAT) {
                    stats.setFirstAttack(Math.round(MapleDataTool.getFloat(firstAttackData)) > 0);
                } else {
                    stats.setFirstAttack(MapleDataTool.getInt(firstAttackData) > 0);
                }
            }
            if (stats.isBoss() || isDmgSponge(mid)) {
                if (monsterInfoData.getChildByPath("hpTagColor") == null
                        || monsterInfoData.getChildByPath("hpTagBgcolor") == null) {
                    stats.setTagColor(0);
                    stats.setTagBgColor(0);
                } else {
                    stats.setTagColor(MapleDataTool.getIntConvert("hpTagColor", monsterInfoData));
                    stats.setTagBgColor(MapleDataTool.getIntConvert("hpTagBgcolor", monsterInfoData));
                }
            }

            final MapleData banishData = monsterInfoData.getChildByPath("ban");
            if (banishData != null) {
                stats.setBanishInfo(new BanishInfo(MapleDataTool.getString("banMsg", banishData),
                        MapleDataTool.getInt("banMap/0/field", banishData, -1),
                        MapleDataTool.getString("banMap/0/portal", banishData, "sp")));
            }

            final MapleData reviveInfo = monsterInfoData.getChildByPath("revive");
            if (reviveInfo != null) {
                List<Integer> revives = new LinkedList<Integer>();
                for (MapleData bdata : reviveInfo) {
                    revives.add(MapleDataTool.getInt(bdata));
                }
                stats.setRevives(revives);
            }

            final MapleData monsterSkillData = monsterInfoData.getChildByPath("skill");
            if (monsterSkillData != null) {
                int i = 0;
                List<Pair<Integer, Integer>> skills = new ArrayList<>();
                while (monsterSkillData.getChildByPath(Integer.toString(i)) != null) {
                    skills.add(new Pair<>(MapleDataTool.getInt(i + "/skill", monsterSkillData, 0),
                            MapleDataTool.getInt(i + "/level", monsterSkillData, 0)));
                    if (MapleDataTool.getInt(i + "/afterAttack", monsterSkillData, -1) != -1) {
                        stats.addAfterAttack(skills.get(i).left, skills.get(i).right,
                                MapleDataTool.getInt(i + "/afterAttack", monsterSkillData, -1));
                    }
                    if (MapleDataTool.getInt(i + "/skillAfter", monsterSkillData, -1) != -1) {
                        stats.addSkillAfter(skills.get(i).left, skills.get(i).right,
                                MapleDataTool.getInt(i + "/skillAfter", monsterSkillData, -1));
                    }

                    i++;
                }
                stats.setSkills(skills);
            }

            final MapleData monsterAttackData = monsterInfoData.getChildByPath("attack");
            if (monsterAttackData != null) {
                int i = 0;
                List<MobAttack> attacks = new ArrayList<MobAttack>();
                while (monsterAttackData.getChildByPath(Integer.toString(i)) != null) {
                    MobAttack attack = new MobAttack(MapleDataTool.getInt(i + "/action", monsterAttackData, -1),
                            MapleDataTool.getInt(i + "/afterAttack", monsterAttackData, -1),
                            MapleDataTool.getInt(i + "/fixAttack", monsterAttackData, -1),
                            MapleDataTool.getInt(i + "/onlyAfterAttack", monsterAttackData, -1),
                            MapleDataTool.getInt(i + "/cooltime", monsterAttackData, -1));
                    if (monsterAttackData.getChildByPath(Integer.toString(i) + "/callSkill") != null) {
                        MapleData callSkillData = monsterAttackData.getChildByPath(Integer.toString(i) + "/callSkill");
                        int j = 0;
                        while (callSkillData.getChildByPath(String.valueOf(j)) != null) {
                            MapleData callSkillIdxData = callSkillData.getChildByPath(String.valueOf(j));
                            attack.addSkill(MapleDataTool.getInt("skill", callSkillIdxData, 0),
                                    MapleDataTool.getInt("level", callSkillIdxData, 0));
                            j++;
                        }
                    }
                    attacks.add(attack);
                    i++;
                }
                stats.setAttacks(attacks);
            }
            

            final MapleData monsterHitParts = monsterData.getChildByPath("HitParts");
            if (monsterHitParts != null) {
                for (MapleData hitPart : monsterHitParts.getChildren()) {
                    stats.addHitPart(hitPart.getName());
                }
            }

            decodeElementalString(stats, MapleDataTool.getString("elemAttr", monsterInfoData, ""));

            // Other data which isn;t in the mob, but might in the linked data
            final int link = MapleDataTool.getIntConvert("link", monsterInfoData, 0);
            stats.setInvincible(MapleDataTool.getIntConvert("invincible", monsterInfoData, 0) == 1);

            if (link != 0) { // Store another copy, for faster processing.
                monsterData = data.getData(StringUtil.getLeftPaddedStr(link + ".img", '0', 11));
                monsterInfoData = monsterData.getChildByPath("info");
            }
            stats.setLink(link);
           /*파이날HP 
            for (int i = 0; i < FinalMaxHpConstants.finalMonsterMaxHpMobCode.size(); i++) {
                if (mid == FinalMaxHpConstants.finalMonsterMaxHpMobCode.get(i)) {
                    stats.setFinalMaxHP(FinalMaxHpConstants.finalMonsterMaxHp.get(i));
                }
            }*/
            for (MapleData idata : monsterData) {
                if (idata.getName().equals("fly")) {
                    stats.setFly(true);
                    stats.setMobile(true);
                    break;
                } else if (idata.getName().equals("move")) {
                    stats.setMobile(true);
                } else if (idata.getName().equals("invincible")) {
                    stats.setInvincible(true);
                }
            }

            byte hpdisplaytype = -1;
            if (!stats.isInvincible()) {
                if (stats.getTagColor() > 0) {
                    hpdisplaytype = 0;
                } else if (stats.isFriendly()) {
                    hpdisplaytype = 1;
                } else if (mid >= 9300184 && mid <= 9300215) { // Mulung TC mobs
                    hpdisplaytype = 2;
                } else if (!stats.isBoss()
                        || // 보스 아님
                        (mid >= 9800000 && mid <= 9800124)
                        || // 몬스터파크
                        (mid >= 9302044 && mid <= 9302048)) { // 아스완
                    hpdisplaytype = 3;
                }
            }     
            //저코보스
            if (mid == 5220002) { //파우스트 2000억
                stats.setHp(200000000000L);
            }
            if (mid == 5220000) { //킹크랑 4000억
                stats.setHp(400000000000L);
            }
            if (mid == 9300119) { //데비존 7000억
                stats.setHp(700000000000L);
            }      
            if (mid == 8300006) { //드래고니카 1조 2천억
                stats.setHp(1200000000000L);
            }
            if (mid == 8300007) { //드래곤 라이더 1조 4천억
                stats.setHp(1400000000000L);
            }      
            if (mid == 9309205) { //양철 나무꾼 2조
                stats.setHp(2000000000000L);
            }
            if (mid == 8860000) { //아카이럼 2조 8천억
                stats.setHp(2800000000000L);
            }      
            if (mid == 9309208) { //도로시의 환영 3조 4천억
                stats.setHp(3400000000000L);
            }
            if (mid == 8820001) { //핑크빈 4조
                stats.setHp(4000000000000L);
            }      
            if (mid == 8220011) { //아우프헤벤 4조 6천억
                stats.setHp(4600000000000L);
            }
            if (mid == 9010009) { //꿈속의 힐라 6조 2천억
                stats.setHp(6200000000000L);
            }      
            if (mid == 8920000) { //카오스 블러디 퀸 7조
                stats.setHp(7000000000000L);
            }
            if (mid == 8900000) { //카오스 피에르 7조 8천억
                stats.setHp(7800000000000L);
            }      
            if (mid == 8880000) { //매그너스 8조 6천억
                stats.setHp(8600000000000L);
            }
            if (mid == 8500001) { //파풀라투스의 시계 7조 6천억
                stats.setHp(7600000000000L);
            }      
            if (mid == 8500002) { //파풀라투스 2조
                stats.setHp(2000000000000L);
            }
            if (mid == 9801028) { //스우 1 페이즈 4조 8천억
                stats.setHp(4800000000000L);
            }      
            if (mid == 9801029) { //스우 2 페이즈 6조
                stats.setHp(6000000000000L);
            }
            
            //보물보스
            
            if (mid == 9390710) { //초원 황소 300조
                stats.setHp(300000000000000L);
            }      
            if (mid == 9390711) { //해골지네 660조
                stats.setHp(660000000000000L);
            }
            if (mid == 9390712) { //초원사신 1020조
                stats.setHp(1020000000000000L);
            }      
            if (mid == 8220003) { //해룡 1740조
                stats.setHp(1740000000000000L);
            }
            if (mid == 9101078) { //불꽃늑대 2160조
                stats.setHp(2160000000000000L);
            }      
            if (mid == 2600800) { //군단장 윌 2700조
                stats.setHp(2700000000000000L);  //HP 표시안댐
            }
            if (mid == 8644011) { //악화된 조화의 정령 3300조
                stats.setHp(3300000000000000L);
            }      
            if (mid == 9300890) { //데미안 4800조
                stats.setHp(4800000000000000L);
            }
            
            //서버보스
            
            if (mid == 8880140) { //루시드 1페이즈 12경
                stats.setHp(120000000000000000L);
            }      
            if (mid == 8880150) { //루시드 2페이즈 6경
                stats.setHp(60000000000000000L);
            }
            if (mid == 9440025) { //크로스 34경
                stats.setHp(340000000000000000L);
            }      
            if (mid == 8880301) { //윌 1페이즈 20경
                stats.setHp(200000000000000000L);
            }
            if (mid == 8880302) { //윌 2페이즈 40경
                stats.setHp(400000000000000000L);
            }      
            if (mid == 6500001) { //진힐라 150경
                stats.setHp(1500000000000000000L);
            }
            if (mid == 8880502) { //검은 마법사 1페이지 150경
                stats.setHp(1500000000000000000L);
            }      
            if (mid == 8880503) { //검은 마법사 2페이지 200경
                stats.setHp(2000000000000000000L);
            }
            if (mid == 9999991) { //진힐라 손 700경
                stats.setHp(7000000000000000000L);
                  hpdisplaytype = 1;
            }      
            if (mid == 9999992) { //진힐라 손 700경
                stats.setHp(7000000000000000000L);
                  hpdisplaytype = 1;
            }

            stats.setHPDisplayType(hpdisplaytype);

            monsterStats.put(Integer.valueOf(mid), stats);
        }
        return new MapleMonster(mid, stats);
    }
    /*                stats.setTagColor(10);
                stats.setTagBgColor(9);                    
*/

    public static final void decodeElementalString(MapleMonsterStats stats, String elemAttr) {
        for (int i = 0; i < elemAttr.length(); i += 2) {
            stats.setEffectiveness(Element.getFromChar(elemAttr.charAt(i)),
                    ElementalEffectiveness.getByNumber(Integer.valueOf(String.valueOf(elemAttr.charAt(i + 1)))));
        }
    }

    private static final boolean isDmgSponge(final int mid) {
        switch (mid) {
            case 8810018:
            case 8810118:
            case 8810119:
            case 8810120:
            case 8810121:
            case 8810122:
            case 8820010:
            case 8820011:
            case 8820012:
            case 8820013:
            case 8820014:
            case 8820110:
            case 8820111:
            case 8820112:
            case 8820113:
            case 8820114:
                return true;
        }
        return false;
    }

    public static MapleNPC getNPC(final int nid) {
        final String name = MapleDataTool.getString(nid + "/name", npcStringData, "MISSINGNO");
        return new MapleNPC(nid, new MapleNPCStats(name));
    }

    public static MaplePlayerNPC getPlayerNPC(final int nid) {
        MapleNPCStats stats = new MapleNPCStats("");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection con = MYSQL.getConnection();
            ps = con.prepareStatement("SELECT * FROM `playernpcs` WHERE id = " + nid);
            rs = ps.executeQuery();
            rs.next();
            stats.setCY(rs.getInt("y"));
            stats.setName(rs.getString("name"));
            stats.setFH(0);
            stats.setRX0(rs.getInt("x") - 50);
            stats.setRX1(rs.getInt("x") + 50);
            // System.out.println("x : "+rs.getInt("x")+" y : "+rs.getInt("y")+" name :
            // "+rs.getString("name")+" hair : "+rs.getInt("hair")+" face :
            // "+rs.getInt("face")+" skin : "+rs.getByte("skin")+" dir :
            // "+rs.getByte("dir"));
            // System.out.println("x : "+(npc.getRx0() + 50)+" y : "+npc.getCy()+" name :
            // "+npc.getName()+" hair : "+npc.getHair()+" face : "+npc.getFace()+" skin :
            // "+npc.getSkin()+" dir : "+npc.getDirection());

            // System.out.println("Equip Size : "+equips.size());
            MaplePlayerNPC npc = new MaplePlayerNPC(nid, stats);
            npc.setHair(rs.getInt("hair"));
            npc.setFace(rs.getInt("face"));
            npc.setSkin(rs.getByte("skin"));
            npc.setDirection(rs.getByte("dir"));
            npc.setPosition(new Point(rs.getInt("x"), stats.getCY()));
            ps.close();
            rs.close();
            ps = con.prepareStatement("SELECT * FROM `playernpcs_equip` WHERE npcid = " + nid);
            rs = ps.executeQuery();
            Map<Byte, Integer> equips = new LinkedHashMap<Byte, Integer>();

            while (rs.next()) {
                equips.put(rs.getByte("equippos"), rs.getInt("equipid"));
            }
            npc.setEquips(equips);
            rs.close();
            ps.close();
            con.close();
            return npc;
        } catch (Exception ex) {
            if (!ServerConstants.realese) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignore) {

            }
        }
        return null;
    }
}
