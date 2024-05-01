package server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.ItemFlag;
import constants.GameConstants;
import client.MapleCharacter;
import client.MapleTrait.MapleTraitType;
import client.inventory.EquipAdditions;
import client.inventory.MapleInventoryType;
import database.DatabaseConnection;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import provider.MapleDataType;
import server.StructSetItem.SetItem;
import server.quest.MapleQuest;
import server.quest.MapleQuestAction;
import tools.Pair;
import tools.Triple;

public class MapleItemInformationProvider {

    private final static MapleItemInformationProvider instance = new MapleItemInformationProvider();
    protected final MapleDataProvider chrData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Character.wz"));
    protected final MapleDataProvider etcData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Etc.wz"));
    protected final MapleDataProvider itemData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Item.wz"));
    protected final Map<Integer, ItemInformation> dataCache = new HashMap<Integer, ItemInformation>();
    protected final Map<String, List<Triple<String, Point, Point>>> afterImage = new HashMap<String, List<Triple<String, Point, Point>>>();
    protected final Map<Integer, List<StructPotentialItem>> potentialCache = new HashMap<Integer, List<StructPotentialItem>>();
    protected final Map<Integer, MapleStatEffect> itemEffects = new HashMap<Integer, MapleStatEffect>();
    protected final Map<Integer, MapleStatEffect> itemEffectsEx = new HashMap<Integer, MapleStatEffect>();
    protected final Map<Integer, Integer> mobIds = new HashMap<Integer, Integer>();
    protected final Map<Integer, Pair<Integer, Integer>> potLife = new HashMap<Integer, Pair<Integer, Integer>>(); //itemid to lifeid, levels
    protected final Map<Integer, StructFamiliar> familiars = new HashMap<Integer, StructFamiliar>(); //by familiarID
    protected final Map<Integer, StructFamiliar> familiars_Item = new HashMap<Integer, StructFamiliar>(); //by cardID
    protected final Map<Integer, StructFamiliar> familiars_Mob = new HashMap<Integer, StructFamiliar>(); //by mobID
    protected final Map<Integer, Pair<List<Integer>, List<Integer>>> androids = new HashMap<Integer, Pair<List<Integer>, List<Integer>>>();
    protected final Map<Integer, Pair<Integer, Integer>> maxQuestItemQuantity = new HashMap<>(); //k : itemid (403xxxx) , v : questid, quantity
    protected final Map<Integer, Triple<Integer, List<Integer>, List<Integer>>> monsterBookSets = new HashMap<Integer, Triple<Integer, List<Integer>, List<Integer>>>();
    protected final Map<Byte, StructSetItem> setItems = new HashMap<Byte, StructSetItem>();
    protected final MapleDataProvider stringData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/String.wz"));
    protected final Map<Integer, CustomSetItemInfo> customSetItemInfo = new HashMap<>();

    protected Map<Integer, Short> petFlagInfo;

    public void runQuest() {

        //Quest Maximum Quantity Cache
//        Map<Integer, Integer> quests = new HashMap<Integer, Integer>();
        for (MapleQuest quest : MapleQuest.getAllInstances()) {
            for (MapleQuestAction act : quest.getCompleteActs()) {
                if (act.getItems() == null) {
                    continue;
                }
                for (MapleQuestAction.QuestItem qitem : act.getItems()) {
                    if (qitem.count < 0 && isQuestItem(qitem.itemid)) {
                        if (maxQuestItemQuantity.containsKey(qitem.itemid)) {
                            //System.err.println("Warning : Duplicate Quest Required Item. - ItemID : " + qitem.itemid + " Quest1 : " + quests.get(qitem.itemid) + ", Quest2 : " + quest.getId());
                        }
                        maxQuestItemQuantity.put(qitem.itemid, new Pair<>(quest.getId(), -qitem.count));
                    }
                }
            }
        }
    }

    public Pair<Integer, Integer> getQuestItemInfo(int itemid) {
        return maxQuestItemQuantity.get(Integer.valueOf(itemid));
    }

    public void runEtc() {
        if (!setItems.isEmpty() || !potentialCache.isEmpty() || !customSetItemInfo.isEmpty()) {
            return;
        }
        final MapleData setsData = etcData.getData("SetItemInfo.img");
        StructSetItem itemz;
        //SetItem itez;
        StructSetItem.SetItem itez;
        for (MapleData dat : setsData) {
            itemz = new StructSetItem();
            itemz.setItemID = Byte.parseByte(dat.getName());
            itemz.completeCount = (byte) MapleDataTool.getIntConvert("completeCount", dat, 0);
            for (MapleData level : dat.getChildByPath("ItemID")) {
                if (level.getType() != MapleDataType.INT) {
                    for (MapleData leve : level) {
                        itemz.itemIDs.add(MapleDataTool.getInt(leve));
                    }
                } else {
                    itemz.itemIDs.add(MapleDataTool.getInt(level));
                }
            }
            for (MapleData level : dat.getChildByPath("Effect")) {
                itez = new SetItem();
                itez.incPDD = MapleDataTool.getIntConvert("incPDD", level, 0);
                itez.incMDD = MapleDataTool.getIntConvert("incMDD", level, 0);
                itez.incSTR = MapleDataTool.getIntConvert("incSTR", level, 0);
                itez.incDEX = MapleDataTool.getIntConvert("incDEX", level, 0);
                itez.incINT = MapleDataTool.getIntConvert("incINT", level, 0);
                itez.incLUK = MapleDataTool.getIntConvert("incLUK", level, 0);
                itez.incACC = MapleDataTool.getIntConvert("incACC", level, 0);
                itez.incPAD = MapleDataTool.getIntConvert("incPAD", level, 0);
                itez.incMAD = MapleDataTool.getIntConvert("incMAD", level, 0);
                itez.incSpeed = MapleDataTool.getIntConvert("incSpeed", level, 0);
                itez.incMHP = MapleDataTool.getIntConvert("incMHP", level, 0);
                itez.incMMP = MapleDataTool.getIntConvert("incMMP", level, 0);
                itez.incMHPr = MapleDataTool.getIntConvert("incMHPr", level, 0);
                itez.incMMPr = MapleDataTool.getIntConvert("incMMPr", level, 0);
                itez.incAllStat = MapleDataTool.getIntConvert("incAllStat", level, 0);
                itez.option1 = MapleDataTool.getIntConvert("Option/1/option", level, 0);
                itez.option2 = MapleDataTool.getIntConvert("Option/2/option", level, 0);
                itez.option1Level = MapleDataTool.getIntConvert("Option/1/level", level, 0);
                itez.option2Level = MapleDataTool.getIntConvert("Option/2/level", level, 0);
                itemz.items.put(Integer.parseInt(level.getName()), itez);
            }
            setItems.put(itemz.setItemID, itemz);
            customSetItemInfo.put((int) itemz.setItemID, new CustomSetItemInfo(dat));
        }
        final MapleData potsData = itemData.getData("ItemOption.img");
        StructPotentialItem item;
        List<StructPotentialItem> items;
        for (MapleData dat : potsData) {
            items = new LinkedList<StructPotentialItem>();
            for (MapleData level : dat.getChildByPath("level")) {
                item = new StructPotentialItem();
                item.optionType = MapleDataTool.getIntConvert("info/optionType", dat, 0);
                item.reqLevel = MapleDataTool.getIntConvert("info/reqLevel", dat, 0);
                item.face = MapleDataTool.getString("face", level, "");
                item.boss = MapleDataTool.getIntConvert("boss", level, 0) > 0;
                item.potentialID = Integer.parseInt(dat.getName());
                item.attackType = (short) MapleDataTool.getIntConvert("attackType", level, 0);
                item.incMHP = (short) MapleDataTool.getIntConvert("incMHP", level, 0);
                item.incMMP = (short) MapleDataTool.getIntConvert("incMMP", level, 0);

                item.incSTR = (byte) MapleDataTool.getIntConvert("incSTR", level, 0);
                item.incDEX = (byte) MapleDataTool.getIntConvert("incDEX", level, 0);
                item.incINT = (byte) MapleDataTool.getIntConvert("incINT", level, 0);
                item.incLUK = (byte) MapleDataTool.getIntConvert("incLUK", level, 0);
                item.incACC = (byte) MapleDataTool.getIntConvert("incACC", level, 0);
                item.incEVA = (byte) MapleDataTool.getIntConvert("incEVA", level, 0);
                item.incSpeed = (byte) MapleDataTool.getIntConvert("incSpeed", level, 0);
                item.incJump = (byte) MapleDataTool.getIntConvert("incJump", level, 0);
                item.incPAD = (byte) MapleDataTool.getIntConvert("incPAD", level, 0);
                item.incMAD = (byte) MapleDataTool.getIntConvert("incMAD", level, 0);
                item.incPDD = (byte) MapleDataTool.getIntConvert("incPDD", level, 0);
                item.incMDD = (byte) MapleDataTool.getIntConvert("incMDD", level, 0);
                item.prop = (byte) MapleDataTool.getIntConvert("prop", level, 0);
                item.time = (byte) MapleDataTool.getIntConvert("time", level, 0);
                item.incSTRr = (byte) MapleDataTool.getIntConvert("incSTRr", level, 0);
                item.incDEXr = (byte) MapleDataTool.getIntConvert("incDEXr", level, 0);
                item.incINTr = (byte) MapleDataTool.getIntConvert("incINTr", level, 0);
                item.incLUKr = (byte) MapleDataTool.getIntConvert("incLUKr", level, 0);
                item.incMHPr = (byte) MapleDataTool.getIntConvert("incMHPr", level, 0);
                item.incMMPr = (byte) MapleDataTool.getIntConvert("incMMPr", level, 0);
                item.incACCr = (byte) MapleDataTool.getIntConvert("incACCr", level, 0);
                item.incEVAr = (byte) MapleDataTool.getIntConvert("incEVAr", level, 0);
                item.incPADr = (byte) MapleDataTool.getIntConvert("incPADr", level, 0);
                item.incMADr = (byte) MapleDataTool.getIntConvert("incMADr", level, 0);
                item.incPDDr = (byte) MapleDataTool.getIntConvert("incPDDr", level, 0);
                item.incMDDr = (byte) MapleDataTool.getIntConvert("incMDDr", level, 0);
                item.incCr = (byte) MapleDataTool.getIntConvert("incCr", level, 0);
                item.incDAMr = (byte) MapleDataTool.getIntConvert("incDAMr", level, 0);
                item.RecoveryHP = (byte) MapleDataTool.getIntConvert("RecoveryHP", level, 0);
                item.RecoveryMP = (byte) MapleDataTool.getIntConvert("RecoveryMP", level, 0);
                item.HP = (byte) MapleDataTool.getIntConvert("HP", level, 0);
                item.MP = (byte) MapleDataTool.getIntConvert("MP", level, 0);
                item.level = (byte) MapleDataTool.getIntConvert("level", level, 0);
                item.ignoreTargetDEF = (byte) MapleDataTool.getIntConvert("ignoreTargetDEF", level, 0);
                item.ignoreDAM = (byte) MapleDataTool.getIntConvert("ignoreDAM", level, 0);
                item.DAMreflect = (byte) MapleDataTool.getIntConvert("DAMreflect", level, 0);
                item.mpconReduce = (byte) MapleDataTool.getIntConvert("mpconReduce", level, 0);
                item.mpRestore = (byte) MapleDataTool.getIntConvert("mpRestore", level, 0);
                item.incMesoProp = (byte) MapleDataTool.getIntConvert("incMesoProp", level, 0);
                item.incRewardProp = (byte) MapleDataTool.getIntConvert("incRewardProp", level, 0);
                item.incAllskill = (byte) MapleDataTool.getIntConvert("incAllskill", level, 0);
                item.ignoreDAMr = (byte) MapleDataTool.getIntConvert("ignoreDAMr", level, 0);
                item.RecoveryUP = (byte) MapleDataTool.getIntConvert("RecoveryUP", level, 0);
                switch (item.potentialID) {
                    case 31001:
                    case 31002:
                    case 31003:
                    case 31004:
                        item.skillID = (short) (item.potentialID - 23001);
                        break;
                    case 41005:
                    case 41006:
                    case 41007:
                        item.skillID = (short) (item.potentialID - 33001);
                        break;
                    default:
                        item.skillID = 0;
                        break;
                }
                items.add(item);
            }
            potentialCache.put(Integer.parseInt(dat.getName()), items);
        }
        List<Triple<String, Point, Point>> thePointK = new ArrayList<Triple<String, Point, Point>>();
        List<Triple<String, Point, Point>> thePointA = new ArrayList<Triple<String, Point, Point>>();

        final MapleDataDirectoryEntry a = (MapleDataDirectoryEntry) chrData.getRoot().getEntry("Afterimage");
        for (MapleDataEntry b : a.getFiles()) {
            final MapleData iz = chrData.getData("Afterimage/" + b.getName());
            List<Triple<String, Point, Point>> thePoint = new ArrayList<Triple<String, Point, Point>>();
            Map<String, Pair<Point, Point>> dummy = new HashMap<String, Pair<Point, Point>>();
            for (MapleData i : iz) {
                for (MapleData xD : i) {
                    if (xD.getName().contains("prone") || xD.getName().contains("double") || xD.getName().contains("triple")) {
                        continue;
                    }
                    if ((b.getName().contains("bow") || b.getName().contains("Bow")) && !xD.getName().contains("shoot")) {
                        continue;
                    }
                    if ((b.getName().contains("gun") || b.getName().contains("cannon")) && !xD.getName().contains("shot")) {
                        continue;
                    }
                    if (dummy.containsKey(xD.getName())) {
                        if (xD.getChildByPath("lt") != null) {
                            Point lt = (Point) xD.getChildByPath("lt").getData();
                            Point ourLt = dummy.get(xD.getName()).left;
                            if (lt.x < ourLt.x) {
                                ourLt.x = lt.x;
                            }
                            if (lt.y < ourLt.y) {
                                ourLt.y = lt.y;
                            }
                        }
                        if (xD.getChildByPath("rb") != null) {
                            Point rb = (Point) xD.getChildByPath("rb").getData();
                            Point ourRb = dummy.get(xD.getName()).right;
                            if (rb.x > ourRb.x) {
                                ourRb.x = rb.x;
                            }
                            if (rb.y > ourRb.y) {
                                ourRb.y = rb.y;
                            }
                        }
                    } else {
                        Point lt = null, rb = null;
                        if (xD.getChildByPath("lt") != null) {
                            lt = (Point) xD.getChildByPath("lt").getData();
                        }
                        if (xD.getChildByPath("rb") != null) {
                            rb = (Point) xD.getChildByPath("rb").getData();
                        }
                        dummy.put(xD.getName(), new Pair<Point, Point>(lt, rb));
                    }
                }
            }
            for (Entry<String, Pair<Point, Point>> ez : dummy.entrySet()) {
                if (ez.getKey().length() > 2 && ez.getKey().substring(ez.getKey().length() - 2, ez.getKey().length() - 1).equals("D")) { //D = double weapon
                    thePointK.add(new Triple<String, Point, Point>(ez.getKey(), ez.getValue().left, ez.getValue().right));
                } else if (ez.getKey().contains("PoleArm")) { //D = double weapon
                    thePointA.add(new Triple<String, Point, Point>(ez.getKey(), ez.getValue().left, ez.getValue().right));
                } else {
                    thePoint.add(new Triple<String, Point, Point>(ez.getKey(), ez.getValue().left, ez.getValue().right));
                }
            }
            afterImage.put(b.getName().substring(0, b.getName().length() - 4), thePoint);
        }
        afterImage.put("katara", thePointK); //hackish
        afterImage.put("aran", thePointA); //hackish
    }

    public void runItems() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();

            // Load Item Data
            ps = con.prepareStatement("SELECT * FROM wz_itemdata");

            rs = ps.executeQuery();
            while (rs.next()) {
                initItemInformation(rs);
            }
            rs.close();
            ps.close();

            // Load Item Equipment Data
            ps = con.prepareStatement("SELECT * FROM wz_itemequipdata ORDER BY itemid");
            rs = ps.executeQuery();
            while (rs.next()) {
                initItemEquipData(rs);
            }
            rs.close();
            ps.close();

            // Load Item Addition Data
            ps = con.prepareStatement("SELECT * FROM wz_itemadddata ORDER BY itemid");
            rs = ps.executeQuery();
            while (rs.next()) {
                initItemAddData(rs);
            }
            rs.close();
            ps.close();

            // Load Item Reward Data
            ps = con.prepareStatement("SELECT * FROM wz_itemrewarddata ORDER BY itemid");
            rs = ps.executeQuery();
            while (rs.next()) {
                initItemRewardData(rs);
            }
            rs.close();
            ps.close();

            // Load Item BonusExp 쿼리 
            //System.out.println("캐싱잘되여 " );
            ps = con.prepareStatement("SELECT * FROM wz_itembonusexpdata ORDER BY itemid");
            rs = ps.executeQuery();
            while (rs.next()) {
                initItemBonusExpData(rs);
            }
            rs.close();
            ps.close();

            // Finalize all Equipments
            for (Entry<Integer, ItemInformation> entry : dataCache.entrySet()) {
                if (GameConstants.getInventoryType(entry.getKey()) == MapleInventoryType.EQUIP) {
                    finalizeEquipData(entry.getValue());
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                }
            }
        }
        //System.out.println(dataCache.size() + " items loaded.");
    }

    public final List<StructPotentialItem> getPotentialInfo(final int potId) {
        return potentialCache.get(potId);
    }

    public final Map<Integer, List<StructPotentialItem>> getAllPotentialInfo() {
        return potentialCache;
    }

    public final Collection<Integer> getMonsterBookList() {
        return mobIds.values();
    }

    public final Map<Integer, Integer> getMonsterBook() {
        return mobIds;
    }

    public final Pair<Integer, Integer> getPot(int f) {
        return potLife.get(f);
    }

    public final StructFamiliar getFamiliar(int f) {
        return familiars.get(f);
    }

    public final Map<Integer, StructFamiliar> getFamiliars() {
        return familiars;
    }

    public final StructFamiliar getFamiliarByItem(int f) {
        return familiars_Item.get(f);
    }

    public final StructFamiliar getFamiliarByMob(int f) {
        return familiars_Mob.get(f);
    }

    public static final MapleItemInformationProvider getInstance() {
        return instance;
    }

    public final Collection<ItemInformation> getAllItems() {
        return dataCache.values();
    }

    public final List<Pair<Integer, String>> getAllItemss() {
        // if (itemNameCache.size() != 0) {
        //     return itemNameCache;
        // }
        final List<Pair<Integer, String>> itemPairs = new ArrayList<Pair<Integer, String>>();
        MapleData itemsData;

        itemsData = stringData.getData("Cash.img");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }

        itemsData = stringData.getData("Consume.img");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }

        itemsData = stringData.getData("Eqp.img").getChildByPath("Eqp");
        for (MapleData eqpType : itemsData.getChildren()) {
            for (MapleData itemFolder : eqpType.getChildren()) {
                itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
            }
        }

        itemsData = stringData.getData("Etc.img").getChildByPath("Etc");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }

        itemsData = stringData.getData("Ins.img");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }

        itemsData = stringData.getData("Pet.img");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }
        return itemPairs;
    }

    public final Pair<List<Integer>, List<Integer>> getAndroidInfo(int i) {
        return androids.get(i);
    }

    public final Triple<Integer, List<Integer>, List<Integer>> getMonsterBookInfo(int i) {
        return monsterBookSets.get(i);
    }

    public final Map<Integer, Triple<Integer, List<Integer>, List<Integer>>> getAllMonsterBookInfo() {
        return monsterBookSets;
    }

    public boolean noCancelMouse(int itemId) {
        MapleData item = getItemData(itemId);
        if (item == null) {
            return false;
        }
        return MapleDataTool.getIntConvert("info/noCancelMouse", item, 0) == 1;
    }

    protected final MapleData getItemData(final int itemId) {
        MapleData ret = null;
        final String idStr = "0" + String.valueOf(itemId);
        MapleDataDirectoryEntry root = itemData.getRoot();
        for (final MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            // we should have .img files here beginning with the first 4 IID
            for (final MapleDataFileEntry iFile : topDir.getFiles()) {
                if (iFile.getName().equals(idStr.substring(0, 4) + ".img")) {
                    ret = itemData.getData(topDir.getName() + "/" + iFile.getName());
                    if (ret == null) {
                        return null;
                    }
                    ret = ret.getChildByPath(idStr);
                    return ret;
                } else if (iFile.getName().equals(idStr.substring(1) + ".img")) {
                    ret = itemData.getData(topDir.getName() + "/" + iFile.getName());
                    return ret;
                }
            }
        }
        //equips dont have item effects :)
        /*root = equipData.getRoot();
         for (final MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
         for (final MapleDataFileEntry iFile : topDir.getFiles()) {
         if (iFile.getName().equals(idStr + ".img")) {
         ret = equipData.getData(topDir.getName() + "/" + iFile.getName());
         return ret;
         }
         }
         }*/

        return ret;
    }

    public Integer getItemIdByMob(int mobId) {
        return mobIds.get(mobId);
    }

    public Integer getSetId(int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return Integer.valueOf(i.cardSet);
    }

    /**
     * returns the maximum of items in one slot
     */
    public final short getSlotMax(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return 0;
        }
        return i.slotMax;
    }

    public final int getWholePrice(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return 0;
        }
        return i.wholePrice;
    }

    public final double getPrice(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return -1.0;
        }
        return i.price;
    }

    protected int rand(int min, int max) {
        return Math.abs((int) Randomizer.rand(min, max));
    }

    public Equip levelUpEquip(Equip equip, Map<String, Integer> sta) {
        Equip nEquip = (Equip) equip.copy();
        //is this all the stats?
        try {
            for (Entry<String, Integer> stat : sta.entrySet()) {
                if (stat.getKey().equals("STRMin")) {
                    nEquip.setStr((short) (nEquip.getStr() + rand(stat.getValue().intValue(), sta.get("STRMax").intValue())));
                } else if (stat.getKey().equals("DEXMin")) {
                    nEquip.setDex((short) (nEquip.getDex() + rand(stat.getValue().intValue(), sta.get("DEXMax").intValue())));
                } else if (stat.getKey().equals("INTMin")) {
                    nEquip.setInt((short) (nEquip.getInt() + rand(stat.getValue().intValue(), sta.get("INTMax").intValue())));
                } else if (stat.getKey().equals("LUKMin")) {
                    nEquip.setLuk((short) (nEquip.getLuk() + rand(stat.getValue().intValue(), sta.get("LUKMax").intValue())));
                } else if (stat.getKey().equals("PADMin")) {
                    nEquip.setWatk((short) (nEquip.getWatk() + rand(stat.getValue().intValue(), sta.get("PADMax").intValue())));
                } else if (stat.getKey().equals("PDDMin")) {
                    nEquip.setWdef((short) (nEquip.getWdef() + rand(stat.getValue().intValue(), sta.get("PDDMax").intValue())));
                } else if (stat.getKey().equals("MADMin")) {
                    nEquip.setMatk((short) (nEquip.getMatk() + rand(stat.getValue().intValue(), sta.get("MADMax").intValue())));
                } else if (stat.getKey().equals("MDDMin")) {
                    nEquip.setMdef((short) (nEquip.getMdef() + rand(stat.getValue().intValue(), sta.get("MDDMax").intValue())));
                } else if (stat.getKey().equals("ACCMin")) {
                    nEquip.setAcc((short) (nEquip.getAcc() + rand(stat.getValue().intValue(), sta.get("ACCMax").intValue())));
                } else if (stat.getKey().equals("EVAMin")) {
                    nEquip.setAvoid((short) (nEquip.getAvoid() + rand(stat.getValue().intValue(), sta.get("EVAMax").intValue())));
                } else if (stat.getKey().equals("SpeedMin")) {
                    nEquip.setSpeed((short) (nEquip.getSpeed() + rand(stat.getValue().intValue(), sta.get("SpeedMax").intValue())));
                } else if (stat.getKey().equals("JumpMin")) {
                    nEquip.setJump((short) (nEquip.getJump() + rand(stat.getValue().intValue(), sta.get("JumpMax").intValue())));
                } else if (stat.getKey().equals("MHPMin")) {
                    nEquip.setHp((short) (nEquip.getHp() + rand(stat.getValue().intValue(), sta.get("MHPMax").intValue())));
                } else if (stat.getKey().equals("MMPMin")) {
                    nEquip.setMp((short) (nEquip.getMp() + rand(stat.getValue().intValue(), sta.get("MMPMax").intValue())));
                } else if (stat.getKey().equals("MaxHPMin")) {
                    nEquip.setHp((short) (nEquip.getHp() + rand(stat.getValue().intValue(), sta.get("MaxHPMax").intValue())));
                } else if (stat.getKey().equals("MaxMPMin")) {
                    nEquip.setMp((short) (nEquip.getMp() + rand(stat.getValue().intValue(), sta.get("MaxMPMax").intValue())));
                }
            }
        } catch (NullPointerException e) {
            //catch npe because obviously the wz have some error XD
            e.printStackTrace();
        }
        return nEquip;
    }

    public final EnumMap<EquipAdditions, Pair<Integer, Integer>> getEquipAdditions(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.equipAdditions;
    }

    public final Map<Integer, Map<String, Integer>> getEquipIncrements(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.equipIncs;
    }

    public final List<Integer> getEquipSkills(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.incSkill;
    }

    public final Map<String, Integer> getEquipStats(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.equipStats;
    }

    public final boolean canEquip(final Map<String, Integer> stats, final int itemid, final int level, final int job, final int fame, final int str, final int dex, final int luk, final int int_, final int supremacy) {
        if ((level + supremacy) >= (stats.containsKey("reqLevel") ? stats.get("reqLevel") : 0) && str >= (stats.containsKey("reqSTR") ? stats.get("reqSTR") : 0) && dex >= (stats.containsKey("reqDEX") ? stats.get("reqDEX") : 0) && luk >= (stats.containsKey("reqLUK") ? stats.get("reqLUK") : 0) && int_ >= (stats.containsKey("reqINT") ? stats.get("reqINT") : 0)) {
            final Integer fameReq = stats.get("reqPOP");
            if (fameReq != null && fame < fameReq) {
                return false;
            }
            return true;
        }
        return false;
    }

    public final int getReqLevel(final int itemId) {
        if (getEquipStats(itemId) == null || !getEquipStats(itemId).containsKey("reqLevel")) {
            return 0;
        }
        return getEquipStats(itemId).get("reqLevel");
    }

    public final int getSlots(final int itemId) {
        if (getEquipStats(itemId) == null || !getEquipStats(itemId).containsKey("tuc")) {
            return 0;
        }
        return getEquipStats(itemId).get("tuc");
    }

    public final Integer getSetItemID(final int itemId) {
        if (getEquipStats(itemId) == null || !getEquipStats(itemId).containsKey("setItemID")) {
            return 0;
        }
        return getEquipStats(itemId).get("setItemID");
    }

    public final StructSetItem getSetItem(final int setItemId) {
        return setItems.get((byte) setItemId);
    }
    
    public final CustomSetItemInfo getCustomSetItem(final int setItemID) {
        return customSetItemInfo.get(setItemID);
    }

    public final List<Integer> getScrollReqs(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.scrollReqs;
    }

    public final Item scrollEquipWithId(final Item equip, final Item scrollId, final boolean ws, final MapleCharacter chr, final int vegas) {
        if (equip.getType() == 1) { // See Item.java
            final Equip nEquip = (Equip) equip;
            final Map<String, Integer> stats = getEquipStats(scrollId.getItemId());
            final Map<String, Integer> eqstats = getEquipStats(equip.getItemId());
            
            final int succ = (GameConstants.isTablet(scrollId.getItemId()) ? GameConstants.getSuccessTablet(scrollId.getItemId(), nEquip.getLevel()) : ((GameConstants.isEquipScroll(scrollId.getItemId()) || GameConstants.isPotentialScroll(scrollId.getItemId()) || !stats.containsKey("success") ? 0 : stats.get("success"))));
            final int curse = (GameConstants.isTablet(scrollId.getItemId()) ? GameConstants.getCurseTablet(scrollId.getItemId(), nEquip.getLevel()) : ((GameConstants.isEquipScroll(scrollId.getItemId()) || GameConstants.isPotentialScroll(scrollId.getItemId()) || !stats.containsKey("cursed") ? 0 : stats.get("cursed"))));
            final int added = (ItemFlag.LUCKS_KEY.check(equip.getFlag()) ? 10 : 0) + (chr.getTrait(MapleTraitType.craft).getLevel() / 10);
            int success = succ + (vegas == 5610000 && succ == 10 ? 20 : (vegas == 5610001 && succ == 60 ? 30 : 0)) + added;
            if (ItemFlag.LUCKS_KEY.check(equip.getFlag()) && !GameConstants.isPotentialScroll(scrollId.getItemId()) && !GameConstants.isEquipScroll(scrollId.getItemId()) && !GameConstants.isSpecialScroll(scrollId.getItemId())) {
                equip.setFlag((short) (equip.getFlag() - ItemFlag.LUCKS_KEY.getValue()));
            }
            if (scrollId.getItemId() == 2049123 || GameConstants.isPotentialScroll(scrollId.getItemId()) || GameConstants.isEquipScroll(scrollId.getItemId()) || GameConstants.isSpecialScroll(scrollId.getItemId()) || Randomizer.nextInt(100) <= success || scrollId.getItemId() == 2049009 || scrollId.getItemId() == 2049010) { //운영자 주문서100퍼
                switch (scrollId.getItemId()) {
                    case 2049123: { //이노센트
                        int mesoUP = nEquip.getHands();
                        int mesoUPAllStat = 0, mesoUPPAD = 0, mesoUPMAD = 0;
                        for (int i = 0; i < mesoUP; i++) {
                            switch (i) {
                                case 0://1강
                                    mesoUPAllStat += 6; break;
                                case 1:
                                    mesoUPAllStat += 8; break;
                                case 2:
                                    mesoUPAllStat += 10; break;
                                case 3:
                                    mesoUPAllStat += 12; break;
                                case 4:
                                    mesoUPAllStat += 14; break;
                                case 5:
                                    mesoUPAllStat += 16; break;
                                case 6:
                                    mesoUPAllStat += 18; break;
                                case 7:
                                    mesoUPAllStat += 20; break;
                                case 8:
                                    mesoUPAllStat += 22; break;
                                case 9: //10강
                                    mesoUPAllStat += 40; break;
                                case 10:
                                    mesoUPAllStat += 45; break;
                                case 11:
                                    mesoUPAllStat += 50; break;
                                case 12:
                                    mesoUPAllStat += 55; break;
                                case 13:
                                    mesoUPAllStat += 60; break;
                                case 14:
                                    mesoUPAllStat += 65; break;
                                case 15:
                                    mesoUPAllStat += 70; break;
                                case 16:
                                    mesoUPAllStat += 75; break;
                                case 17:
                                    mesoUPAllStat += 80; break;
                                case 18:
                                    mesoUPAllStat += 85; break;
                                case 19: //20강
                                    mesoUPAllStat += 20; break;
                                case 20:
                                    mesoUPAllStat += 30; break;
                                case 21:
                                    mesoUPAllStat += 30; break;
                                case 22:
                                    mesoUPAllStat += 30; break;
                                case 23:
                                    mesoUPAllStat += 30; break;
                                case 24:
                                    mesoUPAllStat += 30; break;
                                case 25:
                                    mesoUPAllStat += 30; break;
                                case 26:
                                    mesoUPAllStat += 30; break;
                                case 27:
                                    mesoUPAllStat += 30; break;
                                case 28:
                                    mesoUPAllStat += 30; break;
                                case 29: //30강
                                    mesoUPAllStat += 30; break;
                                case 30:
                                    mesoUPAllStat += 40; break;
                                case 31:
                                    mesoUPAllStat += 40; break;
                                case 32:
                                    mesoUPAllStat += 40; break;
                                case 33:
                                    mesoUPAllStat += 40; break;
                                case 34:
                                    mesoUPAllStat += 40; break;
                                case 35:
                                    mesoUPAllStat += 40; break;
                                case 36:
                                    mesoUPAllStat += 40; break;
                                case 37:
                                    mesoUPAllStat += 40; break;
                                case 38:
                                    mesoUPAllStat += 40; break;
                                case 39: //40강
                                    mesoUPAllStat += 40; break;
                            }
                            
                            switch (i) {
                                case 0:
                                    mesoUPPAD += 0; break;
                                case 1:
                                    mesoUPPAD += 0; break;
                                case 2:
                                    mesoUPPAD += 1; break;
                                case 3:
                                    mesoUPPAD += 2; break;
                                case 4:
                                    mesoUPPAD += 3; break;
                                case 5:
                                    mesoUPPAD += 5; break;
                                case 6:
                                    mesoUPPAD += 7; break;
                                case 7:
                                    mesoUPPAD += 9; break;
                                case 8:
                                    mesoUPPAD += 11; break;
                                case 9:
                                    mesoUPPAD += 15; break;
                                case 10:
                                    mesoUPPAD += 18; break;
                                case 11:
                                    mesoUPPAD += 21; break;
                                case 12:
                                    mesoUPPAD += 24; break;
                                case 13:
                                    mesoUPPAD += 27; break;
                                case 14:
                                    mesoUPPAD += 30; break;
                                case 15:
                                    mesoUPPAD += 35; break;
                                case 16:
                                    mesoUPPAD += 40; break;
                                case 17:
                                    mesoUPPAD += 2; break;
                                case 18:
                                    mesoUPPAD += 2; break;
                                case 19:
                                    mesoUPPAD += 2; break;
                                case 20:
                                    mesoUPPAD += 3; break;
                                case 21:
                                    mesoUPPAD += 3; break;
                                case 22:
                                    mesoUPPAD += 3; break;
                                case 23:
                                    mesoUPPAD += 3; break;
                                case 24:
                                    mesoUPPAD += 3; break;
                                case 25:
                                    mesoUPPAD += 3; break;
                                case 26:
                                    mesoUPPAD += 3; break;
                                case 27:
                                    mesoUPPAD += 3; break;
                                case 28:
                                    mesoUPPAD += 3; break;
                                case 29:
                                    mesoUPPAD += 3; break;
                                case 30:
                                    mesoUPPAD += 4; break;
                                case 31:
                                    mesoUPPAD += 4; break;
                                case 32:
                                    mesoUPPAD += 4; break;
                                case 33:
                                    mesoUPPAD += 4; break;
                                case 34:
                                    mesoUPPAD += 4; break;
                                case 35:
                                    mesoUPPAD += 4; break;
                                case 36:
                                    mesoUPPAD += 4; break;
                                case 37:
                                    mesoUPPAD += 4; break;
                                case 38:
                                    mesoUPPAD += 4; break;
                                case 39:
                                    mesoUPPAD += 4; break;
                             }
                            
                            switch (i) {
                                case 0:
                                    mesoUPMAD += 0; break;
                                case 1:
                                    mesoUPMAD += 0; break;
                                case 2:
                                    mesoUPMAD += 1; break;
                                case 3:
                                    mesoUPMAD += 2; break;
                                case 4:
                                    mesoUPMAD += 3; break;
                                case 5:
                                    mesoUPMAD += 5; break;
                                case 6:
                                    mesoUPMAD += 7; break;
                                case 7:
                                    mesoUPMAD += 9; break;
                                case 8:
                                    mesoUPMAD += 11; break;
                                case 9:
                                    mesoUPMAD += 15; break;
                                case 10:
                                    mesoUPMAD += 18; break;
                                case 11:
                                    mesoUPMAD += 21; break;
                                case 12:
                                    mesoUPMAD += 24; break;
                                case 13:
                                    mesoUPMAD += 27; break;
                                case 14:
                                    mesoUPMAD += 30; break;
                                case 15:
                                    mesoUPMAD += 35; break;
                                case 16:
                                    mesoUPMAD += 40; break;
                                case 17:
                                    mesoUPMAD += 2; break;
                                case 18:
                                    mesoUPMAD += 2; break;
                                case 19:
                                    mesoUPMAD += 2; break;
                                case 20:
                                    mesoUPMAD += 3; break;
                                case 21:
                                    mesoUPMAD += 3; break;
                                case 22:
                                    mesoUPMAD += 3; break;
                                case 23:
                                    mesoUPMAD += 3; break;
                                case 24:
                                    mesoUPMAD += 3; break;
                                case 25:
                                    mesoUPMAD += 3; break;
                                case 26:
                                    mesoUPMAD += 3; break;
                                case 27:
                                    mesoUPMAD += 3; break;
                                case 28:
                                    mesoUPMAD += 3; break;
                                case 29:
                                    mesoUPMAD += 3; break;
                                case 30:
                                    mesoUPMAD += 4; break;
                                case 31:
                                    mesoUPMAD += 4; break;
                                case 32:
                                    mesoUPMAD += 4; break;
                                case 33:
                                    mesoUPMAD += 4; break;
                                case 34:
                                    mesoUPMAD += 4; break;
                                case 35:
                                    mesoUPMAD += 4; break;
                                case 36:
                                    mesoUPMAD += 4; break;
                                case 37:
                                    mesoUPMAD += 4; break;
                                case 38:
                                    mesoUPMAD += 4; break;
                                case 39:
                                    mesoUPMAD += 4; break;
                            }
                        }
                        
                        Equip oriEquip = (Equip) getEquipById(nEquip.getItemId());
                        nEquip.setStr((short) (getRandStat(oriEquip.getStr(), 0) + mesoUPAllStat));
                        nEquip.setDex((short) (getRandStat(oriEquip.getDex(), 0) + mesoUPAllStat));
                        nEquip.setInt((short) (getRandStat(oriEquip.getInt(), 0) + mesoUPAllStat));
                        nEquip.setLuk((short) (getRandStat(oriEquip.getLuk(), 0) + mesoUPAllStat));
                        nEquip.setMatk((short) (getRandStat(oriEquip.getMatk(), 0) + mesoUPPAD));
                        nEquip.setWatk((short) (getRandStat(oriEquip.getWatk(), 0) + mesoUPMAD));
                        nEquip.setAcc(getRandStat(oriEquip.getAcc(), 0));
                        nEquip.setAvoid(getRandStat(oriEquip.getAvoid(), 0));
                        nEquip.setJump(getRandStat(oriEquip.getJump(), 0));
//                        nEquip.setHands(getRandStat(nEquip.getHands(), 0)); //메소강화 수치 표현에 쓰임
                        if (nEquip.getItemId() != 1112593) { //영메링은 스피드 초기화 X ?
                            nEquip.setSpeed(getRandStat(oriEquip.getSpeed(), 0));
                        }
                        nEquip.setWdef(getRandStat(oriEquip.getWdef(), 0));
                        nEquip.setMdef(getRandStat(oriEquip.getMdef(), 0));
                        nEquip.setHp(getRandStat(oriEquip.getHp(), 0));
                        nEquip.setMp(getRandStat(oriEquip.getMp(), 0));
                        nEquip.setUpgradeSlots(oriEquip.getUpgradeSlots());
                        nEquip.setLevel((byte) 0);
                        nEquip.setEnhance((byte) 0);
                        break;
                    }
                    case 2049111: {//켄타의 물안경 전용 주문서
                        if (nEquip.getUpgradeSlots() > 0) {
                            //  || ((nEquip.getItemId() > 1100000) && (nEquip.getItemId() < 1110000))
                            //    || ((nEquip.getItemId() > 1080000) && (nEquip.getItemId() < 1083000))
                            nEquip.setStr((short) (nEquip.getStr() + 15));
                            nEquip.setDex((short) (nEquip.getDex() + 15));
                            nEquip.setInt((short) (nEquip.getInt() + 15));
                            nEquip.setLuk((short) (nEquip.getLuk() + 15));
                            nEquip.setWatk((short) (nEquip.getWatk() + 10));
                            nEquip.setMatk((short) (nEquip.getMatk() + 10));
                            break;
                        } else {
                            chr.dropMessage(1, "업그레이드 가능 횟수가 없습니다.");
                        }
                        break;
                    }   
                    case 2049000:
                    case 2049001:
                    case 2049002:
                    case 2049003:
                    case 2049004:
                    case 2049005: {
                        // item.setViciousHammer((byte) (item.getViciousHammer() + 1));
                        if (eqstats.containsKey("tuc") && nEquip.getLevel() + nEquip.getUpgradeSlots() < (eqstats.get("tuc") + nEquip.getViciousHammer())) {
                            nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 1));
                        }
                        break;
                    }
                    case 2049010:
                    case 2049009: {
                        nEquip.setGiftFrom("" + nEquip.newRebirth(scrollId.getItemId(), true));
                        break;
                    }
                    case 2049007:
                    case 2049008: {
                        if (eqstats.containsKey("tuc") && nEquip.getLevel() + nEquip.getUpgradeSlots() < (eqstats.get("tuc") + nEquip.getViciousHammer())) {
                            //저주 받은 백의 주문서 업글 횟수 7인 아이템 기준
                            //1번만 실패하고 +6작까지 했을 때 저주받은 백의 주문서 사용시 황망 바른거처럼 총 +8작까지 가능하던 점 해결
                            nEquip.setUpgradeSlots((byte) Math.min(nEquip.getUpgradeSlots() + 2, eqstats.get("tuc") + nEquip.getViciousHammer()));
                        }
                        break;
                    }
                    case 2040727: // Spikes on shoe, prevents slip
                    {
                        short flag = nEquip.getFlag();
                        flag |= ItemFlag.SPIKES.getValue();
                        nEquip.setFlag(flag);
                        break;
                    }
                    case 2041058: // Cape for Cold protection
                    {
                        short flag = nEquip.getFlag();
                        flag |= ItemFlag.COLD.getValue();
                        nEquip.setFlag(flag);
                        break;
                    }
                    case 5063000:
                    case 2530000:
                    case 2530001: {
                        short flag = nEquip.getFlag();
                        flag |= ItemFlag.LUCKS_KEY.getValue();
                        nEquip.setFlag(flag);
                        break;
                    }
                    case 5064000:
                    case 2531000: {
                        short flag = nEquip.getFlag();
                        flag |= ItemFlag.SHIELD_WARD.getValue();
                        nEquip.setFlag(flag);
                        break;
                    }
                    default: {
                        if (GameConstants.isChaosScroll(scrollId.getItemId())) {
                            if (scrollId.getItemId() == 2049122) { //긍혼
                                if (nEquip.getStr() > 0) {
                                    nEquip.setStr((short) (nEquip.getStr() + Randomizer.rand(5, 15)));
                                }
                                if (nEquip.getDex() > 0) {
                                    nEquip.setDex((short) (nEquip.getDex() + Randomizer.rand(5, 15)));
                                }
                                if (nEquip.getInt() > 0) {
                                    nEquip.setInt((short) (nEquip.getInt() + Randomizer.rand(5, 15)));
                                }
                                if (nEquip.getLuk() > 0) {
                                    nEquip.setLuk((short) (nEquip.getLuk() + Randomizer.rand(5, 15)));
                                }
                                if (nEquip.getWatk() > 0) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.rand(3, 10)));
                                }
                                if (nEquip.getMatk() > 0) {
                                    nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.rand(3, 10)));
                                }
                                /*
                                if (nEquip.getWdef() > 0) {
                                    nEquip.setWdef((short) (nEquip.getWdef() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getMdef() > 0) {
                                    nEquip.setMdef((short) (nEquip.getMdef() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getAcc() > 0) {
                                    nEquip.setAcc((short) (nEquip.getAcc() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getAvoid() > 0) {
                                    nEquip.setAvoid((short) (nEquip.getAvoid() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getSpeed() > 0) {
                                    nEquip.setSpeed((short) (nEquip.getSpeed() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getJump() > 0) {
                                    nEquip.setJump((short) (nEquip.getJump() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getHp() > 0) {
                                    nEquip.setHp((short) (nEquip.getHp() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getMp() > 0) {
                                    nEquip.setMp((short) (nEquip.getMp() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }*/
                            } else {
                                final int z = GameConstants.getChaosNumber(scrollId.getItemId());
                                if (nEquip.getStr() > 0) {
                                    nEquip.setStr((short) (nEquip.getStr() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getDex() > 0) {
                                    nEquip.setDex((short) (nEquip.getDex() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getInt() > 0) {
                                    nEquip.setInt((short) (nEquip.getInt() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getLuk() > 0) {
                                    nEquip.setLuk((short) (nEquip.getLuk() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getWatk() > 0) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getWdef() > 0) {
                                    nEquip.setWdef((short) (nEquip.getWdef() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getMatk() > 0) {
                                    nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getMdef() > 0) {
                                    nEquip.setMdef((short) (nEquip.getMdef() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getAcc() > 0) {
                                    nEquip.setAcc((short) (nEquip.getAcc() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getAvoid() > 0) {
                                    nEquip.setAvoid((short) (nEquip.getAvoid() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getSpeed() > 0 && nEquip.getItemId() != 1112593) { //영메링은 스피드 안오르게
                                    nEquip.setSpeed((short) (nEquip.getSpeed() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getJump() > 0) {
                                    nEquip.setJump((short) (nEquip.getJump() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getHp() > 0) {
                                    nEquip.setHp((short) (nEquip.getHp() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                                if (nEquip.getMp() > 0) {
                                    nEquip.setMp((short) (nEquip.getMp() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                                }
                            }
                            break;
                        } else if (GameConstants.isEquipScroll(scrollId.getItemId())) {
                            final int chanc = Math.max((scrollId.getItemId() == 2049300 || scrollId.getItemId() == 2049303 ? 100 : (scrollId.getItemId() == 2049305 ? 60 : 80)) - (nEquip.getEnhance() * 10), 10) + added;
                            if (nEquip.getEnhance() >= 20) {
                                chr.dropMessage(5, "20성 이상의 아이템에는 사용하실 수 없습니다.");
                            } else if (Randomizer.nextInt(100) <= chanc || chr.isGM()) {

                                for (int i = 0; i < (scrollId.getItemId() == 2049305 ? 4 : (scrollId.getItemId() == 2049304 ? 3 : 1)); i++) {

                                    if (!GameConstants.isWeapon(nEquip.getItemId())) {
                                        if (nEquip.getStr() > 0 || Randomizer.nextInt(50) == 1) { //1/50
                                            nEquip.setStr((short) (nEquip.getStr() + Randomizer.nextInt(5)));
                                        }
                                        if (nEquip.getDex() > 0 || Randomizer.nextInt(50) == 1) { //1/50
                                            nEquip.setDex((short) (nEquip.getDex() + Randomizer.nextInt(5)));
                                        }
                                        if (nEquip.getInt() > 0 || Randomizer.nextInt(50) == 1) { //1/50
                                            nEquip.setInt((short) (nEquip.getInt() + Randomizer.nextInt(5)));
                                        }
                                        if (nEquip.getLuk() > 0 || Randomizer.nextInt(50) == 1) { //1/50
                                            nEquip.setLuk((short) (nEquip.getLuk() + Randomizer.nextInt(5)));
                                        }
                                        if (nEquip.getWdef() > 0 || Randomizer.nextInt(40) == 1) { //1/40
                                            nEquip.setWdef((short) (nEquip.getWdef() + Randomizer.nextInt(5)));
                                        }
                                        if (nEquip.getMdef() > 0 || Randomizer.nextInt(40) == 1) { //1/40
                                            nEquip.setMdef((short) (nEquip.getMdef() + Randomizer.nextInt(5)));
                                        }
                                        if (nEquip.getAcc() > 0 || Randomizer.nextInt(20) == 1) { //1/20
                                            nEquip.setAcc((short) (nEquip.getAcc() + Randomizer.nextInt(5)));
                                        }
                                        if (nEquip.getAvoid() > 0 || Randomizer.nextInt(20) == 1) { //1/20
                                            nEquip.setAvoid((short) (nEquip.getAvoid() + Randomizer.nextInt(5)));
                                        }
                                        if (nEquip.getSpeed() > 0 || Randomizer.nextInt(10) == 1) { //1/10
                                            if (nEquip.getItemId() != 1112593) { //영메링은 스피드 안오르게
                                                nEquip.setSpeed((short) (nEquip.getSpeed() + Randomizer.nextInt(5)));
                                            }
                                        }
                                        if (nEquip.getJump() > 0 || Randomizer.nextInt(10) == 1) { //1/10
                                            nEquip.setJump((short) (nEquip.getJump() + Randomizer.nextInt(5)));
                                        }
                                        if (nEquip.getHp() > 0 || Randomizer.nextInt(5) == 1) { //1/5
                                            nEquip.setHp((short) (nEquip.getHp() + Randomizer.nextInt(5)));
                                        }
                                        if (nEquip.getMp() > 0 || Randomizer.nextInt(5) == 1) { //1/5
                                            nEquip.setMp((short) (nEquip.getMp() + Randomizer.nextInt(5)));
                                        }
                                    } else if (GameConstants.isWeapon(nEquip.getItemId())) {
                                        final int stat = (int) (Math.random() * 7);;
                                        final int vprop = Randomizer.rand(0, 10);
                                        if (nEquip.getWatk() > 0 && nEquip.getMatk() == 0) {
                                            if (stat == 0) {
                                                if (vprop == 1) { // 11개중 2개 확률로 
                                                    nEquip.setStr((short) (nEquip.getStr() + 3));
                                                } else if (vprop <= 4) { // 11개중 3개
                                                    nEquip.setStr((short) (nEquip.getStr() + 2));
                                                } else if (vprop <= 10) { // 11개중 5개
                                                    nEquip.setStr((short) (nEquip.getStr() + 1));
                                                } // 11개중 1개 는 0
                                            } else if (stat == 1) {
                                                if (vprop == 1) { // 11개중 2개 확률로 
                                                    nEquip.setDex((short) (nEquip.getDex() + 3));
                                                } else if (vprop <= 4) { // 11개중 3개
                                                    nEquip.setDex((short) (nEquip.getDex() + 2));
                                                } else if (vprop <= 10) { // 11개중 5개
                                                    nEquip.setDex((short) (nEquip.getDex() + 1));
                                                } // 11개중 1개 는 0
                                            } else if (stat == 2) {
                                                if (vprop == 1) { // 11개중 2개 확률로 
                                                    nEquip.setInt((short) (nEquip.getInt() + 3));
                                                } else if (vprop <= 4) { // 11개중 3개
                                                    nEquip.setInt((short) (nEquip.getInt() + 2));
                                                } else if (vprop <= 10) { // 11개중 5개
                                                    nEquip.setInt((short) (nEquip.getInt() + 1));
                                                } // 11개중 1개 는 0
                                            } else if (stat == 3) {
                                                if (vprop == 1) { // 11개중 2개 확률로 
                                                    nEquip.setLuk((short) (nEquip.getLuk() + 3));
                                                } else if (vprop <= 4) { // 11개중 3개
                                                    nEquip.setLuk((short) (nEquip.getLuk() + 2));
                                                } else if (vprop <= 10) { // 11개중 5개
                                                    nEquip.setLuk((short) (nEquip.getLuk() + 1));
                                                } // 11개중 1개 는 0
                                            }
                                            if (Randomizer.nextInt(10) == 1) { // 10분의1
                                                nEquip.setHp((short) (nEquip.getHp() + Randomizer.rand(30, 50)));
                                            }
                                            if (Randomizer.nextInt(10) == 1) { // 10분의1
                                                nEquip.setMp((short) (nEquip.getMp() + Randomizer.rand(30, 50)));
                                            }
                                            nEquip.setWatk((short) (nEquip.getWatk() + nEquip.getWatk() / 50 + 1));
                                        } else if (nEquip.getMatk() > 0) {
                                            if (stat == 0) {
                                                if (vprop == 1) { // 11개중 2개 확률로 
                                                    nEquip.setStr((short) (nEquip.getStr() + 3));
                                                } else if (vprop <= 4) { // 11개중 3개
                                                    nEquip.setStr((short) (nEquip.getStr() + 2));
                                                } else if (vprop <= 10) { // 11개중 5개
                                                    nEquip.setStr((short) (nEquip.getStr() + 1));
                                                } // 11개중 1개 는 0
                                            } else if (stat == 1) {
                                                if (vprop == 1) { // 11개중 2개 확률로 
                                                    nEquip.setDex((short) (nEquip.getDex() + 3));
                                                } else if (vprop <= 4) { // 11개중 3개
                                                    nEquip.setDex((short) (nEquip.getDex() + 2));
                                                } else if (vprop <= 10) { // 11개중 5개
                                                    nEquip.setDex((short) (nEquip.getDex() + 1));
                                                } // 11개중 1개 는 0
                                            } else if (stat == 2) {
                                                if (vprop == 1) { // 11개중 2개 확률로 
                                                    nEquip.setInt((short) (nEquip.getInt() + 3));
                                                } else if (vprop <= 4) { // 11개중 3개
                                                    nEquip.setInt((short) (nEquip.getInt() + 2));
                                                } else if (vprop <= 10) { // 11개중 5개
                                                    nEquip.setInt((short) (nEquip.getInt() + 1));
                                                } // 11개중 1개 는 0
                                            } else if (stat == 3) {
                                                if (vprop == 1) { // 11개중 2개 확률로 
                                                    nEquip.setLuk((short) (nEquip.getLuk() + 3));
                                                } else if (vprop <= 4) { // 11개중 3개
                                                    nEquip.setLuk((short) (nEquip.getLuk() + 2));
                                                } else if (vprop <= 10) { // 11개중 5개
                                                    nEquip.setLuk((short) (nEquip.getLuk() + 1));
                                                } // 11개중 1개 는 0
                                            }
                                            if (Randomizer.nextInt(10) == 1) { // 10분의1
                                                nEquip.setHp((short) (nEquip.getHp() + Randomizer.rand(30, 50)));
                                            }
                                            if (Randomizer.nextInt(10) == 1) { // 10분의1
                                                nEquip.setMp((short) (nEquip.getMp() + Randomizer.rand(30, 50)));
                                            }
                                            nEquip.setWatk((short) (nEquip.getWatk() + nEquip.getWatk() / 50 + 1));
                                            nEquip.setMatk((short) (nEquip.getMatk() + nEquip.getMatk() / 50 + 1));
                                        }
                                    }
                                    nEquip.setEnhance((byte) (nEquip.getEnhance() + 1));
                                    nEquip.setOwner("");
                                }
                            } else {

                                if (nEquip.getOwner() == "프로텍트 쉴드") {
                                    nEquip.setOwner("");
                                    chr.dropMessage(5, "프로텍트 쉴드의 힘으로 아이템이 파괴되지 않았습니다.");
                                } else {
                                    if (chr.haveItem(2430191) && nEquip.getEnhance() < 9) {
                                        chr.removeItem(2430191, -1);
                                        chr.dropMessage(5, "<프로텍트 실드> 효과로 인하여 아이템 파괴가 방지되었습니다. (8성 이상 장비 적용 불가)");
                                    } else {
                                        return null;
                                    }
                                }
                            }
                            break;
                        } else if (GameConstants.isPotentialScroll(scrollId.getItemId())) {
                            if (nEquip.getState() == 0) {
                                final int chanc = (scrollId.getItemId() == 2049704 || scrollId.getItemId() == 5534000 || scrollId.getItemId() == 2049402 || scrollId.getItemId() == 2049704 ? 100 : (scrollId.getItemId() == 2049400 ? 100 : 70)) + added;
                                if (Randomizer.nextInt(100) > chanc) { //잠재능력 주문서
                                    if (chr.haveItem(2430191) && nEquip.getEnhance() < 9) {
                                        chr.removeItem(2430191, -1);
                                        chr.dropMessage(5, "<프로텍트 실드> 효과로 인하여 아이템 파괴가 방지되었습니다. (8성 이상 장비 적용 불가)");
                                    } else {
                                        return null;
                                    }
                                }
                                if (scrollId.getItemId() == 2049402) { //에픽 부여 주문서
                                    int rank = -6; //에픽
                                    nEquip.setPotential1((short) rank);
                                    nEquip.setPotential2((short) (Randomizer.nextInt(5) == 0 ? rank : 0));
                                    nEquip.setPotential3((short) 0);
                                } else {
                                    nEquip.resetPotential();
                                }
                            }
                            break;
                        } else {
                            for (Entry<String, Integer> stat : stats.entrySet()) {
                                final String key = stat.getKey();

                                if (key.equals("STR")) {
                                    nEquip.setStr((short) (nEquip.getStr() + stat.getValue().intValue()));
                                } else if (key.equals("DEX")) {
                                    nEquip.setDex((short) (nEquip.getDex() + stat.getValue().intValue()));
                                } else if (key.equals("INT")) {
                                    nEquip.setInt((short) (nEquip.getInt() + stat.getValue().intValue()));
                                } else if (key.equals("LUK")) {
                                    nEquip.setLuk((short) (nEquip.getLuk() + stat.getValue().intValue()));
                                } else if (key.equals("PAD")) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + stat.getValue().intValue()));
                                } else if (key.equals("PDD")) {
                                    nEquip.setWdef((short) (nEquip.getWdef() + stat.getValue().intValue()));
                                } else if (key.equals("MAD")) {
                                    nEquip.setMatk((short) (nEquip.getMatk() + stat.getValue().intValue()));
                                } else if (key.equals("MDD")) {
                                    nEquip.setMdef((short) (nEquip.getMdef() + stat.getValue().intValue()));
                                } else if (key.equals("ACC")) {
                                    nEquip.setAcc((short) (nEquip.getAcc() + stat.getValue().intValue()));
                                } else if (key.equals("EVA")) {
                                    nEquip.setAvoid((short) (nEquip.getAvoid() + stat.getValue().intValue()));
                                } else if (key.equals("Speed")) {
                                    if (nEquip.getItemId() != 1112593) { //영메링은 스피드 안오르게
                                        nEquip.setSpeed((short) (nEquip.getSpeed() + stat.getValue().intValue()));
                                    }
                                } else if (key.equals("Jump")) {
                                    nEquip.setJump((short) (nEquip.getJump() + stat.getValue().intValue()));
                                } else if (key.equals("MHP")) {
                                    nEquip.setHp((short) (nEquip.getHp() + stat.getValue().intValue()));
                                } else if (key.equals("MMP")) {
                                    nEquip.setMp((short) (nEquip.getMp() + stat.getValue().intValue()));
                                } else if (key.equals("MHPr")) {
                                    nEquip.setHpR((short) (nEquip.getHpR() + stat.getValue().intValue()));
                                } else if (key.equals("MMPr")) {
                                    nEquip.setMpR((short) (nEquip.getMpR() + stat.getValue().intValue()));
                                }
                            }
                            break;
                        }
                    }
                }
                if (scrollId.getItemId() != 2049123 && !GameConstants.isCleanSlate(scrollId.getItemId()) && !GameConstants.isSpecialScroll(scrollId.getItemId()) && !GameConstants.isEquipScroll(scrollId.getItemId()) && !GameConstants.isPotentialScroll(scrollId.getItemId())) {
                    nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - 1));
                    nEquip.setLevel((byte) (nEquip.getLevel() + 1));
                }
            } else {
                if (!ws && !GameConstants.isCleanSlate(scrollId.getItemId()) && !GameConstants.isSpecialScroll(scrollId.getItemId()) && !GameConstants.isEquipScroll(scrollId.getItemId()) && !GameConstants.isPotentialScroll(scrollId.getItemId())) {
                    nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - 1));
                }
                if (Randomizer.nextInt(99) < curse && scrollId.getItemId() != 2049009 && scrollId.getItemId() != 2049010) {
                    if (chr.haveItem(2430191) && nEquip.getEnhance() < 9) {
                        chr.removeItem(2430191, -1);
                        chr.dropMessage(5, "<프로텍트 실드> 효과로 인하여 아이템 파괴가 방지되었습니다. (8성 이상 장비 적용 불가)");
                    } else {
                        return null;
                    }
                }
            }
        }
        return equip;
    }

    public final Item getEquipById(final int equipId) {
        return getEquipById(equipId, -1);
    }

    public final Item getEquipById(final int equipId, final int ringId) {
        final ItemInformation i = getItemInformation(equipId);
        if (i == null) {
            return new Equip(equipId, (short) 0, ringId, (byte) 0);
        }
        final Item eq = i.eq.copy();
        eq.setUniqueId(ringId);
        return eq;
    }

    protected final short getRandStatFusion(final short defaultValue, final int value1, final int value2) {
        if (defaultValue == 0) {
            return 0;
        }
        final int range = ((value1 + value2) / 2) - defaultValue;
        final int rand = Randomizer.nextInt(Math.abs(range) + 1);
        return (short) (defaultValue + (range < 0 ? -rand : rand));
    }

    protected final short getRandStat(final short defaultValue, final int maxRange) {
        if (defaultValue == 0) {
            return 0;
        }
        // vary no more than ceil of 10% of stat
        final int lMaxRange = (int) Math.min(Math.ceil(defaultValue * 0.1), maxRange);

        return (short) ((defaultValue - lMaxRange) + Randomizer.nextInt(lMaxRange * 2 + 1));
    }

    protected final short getRandStatAbove(final short defaultValue, final int maxRange) {
        if (defaultValue <= 0) {
            return 0;
        }
        final int lMaxRange = (int) Math.min(Math.ceil(defaultValue * 0.1), maxRange);

        return (short) ((defaultValue) + Randomizer.nextInt(lMaxRange + 1));
    }

    public final Equip oriStats(final Equip equip) {
        equip.setStr(getRandStat(equip.getStr(), 0));
        equip.setDex(getRandStat(equip.getDex(), 0));
        equip.setInt(getRandStat(equip.getInt(), 0));
        equip.setLuk(getRandStat(equip.getLuk(), 0));
        equip.setMatk(getRandStat(equip.getMatk(), 0));
        equip.setWatk(getRandStat(equip.getWatk(), 0));
        equip.setAcc(getRandStat(equip.getAcc(), 0));
        equip.setAvoid(getRandStat(equip.getAvoid(), 0));
        equip.setJump(getRandStat(equip.getJump(), 0));
        equip.setHands(getRandStat(equip.getHands(), 0));
        equip.setSpeed(getRandStat(equip.getSpeed(), 0));
        equip.setWdef(getRandStat(equip.getWdef(), 0));
        equip.setMdef(getRandStat(equip.getMdef(), 0));
        equip.setHp(getRandStat(equip.getHp(), 0));
        equip.setMp(getRandStat(equip.getMp(), 0));
        return equip;
    }

    public final Equip randomizeStats(final Equip equip) {
        equip.setStr(getRandStat(equip.getStr(), 5));
        equip.setDex(getRandStat(equip.getDex(), 5));
        equip.setInt(getRandStat(equip.getInt(), 5));
        equip.setLuk(getRandStat(equip.getLuk(), 5));
        equip.setMatk(getRandStat(equip.getMatk(), 5));
        equip.setWatk(getRandStat(equip.getWatk(), 5));
        equip.setAcc(getRandStat(equip.getAcc(), 5));
        equip.setAvoid(getRandStat(equip.getAvoid(), 5));
        equip.setJump(getRandStat(equip.getJump(), 5));
        equip.setHands(getRandStat(equip.getHands(), 5));
        equip.setSpeed(getRandStat(equip.getSpeed(), 5));
        equip.setWdef(getRandStat(equip.getWdef(), 10));
        equip.setMdef(getRandStat(equip.getMdef(), 10));
        equip.setHp(getRandStat(equip.getHp(), 10));
        equip.setMp(getRandStat(equip.getMp(), 10));
        return equip;
    }

    public final Equip randomizeStats_Above(final Equip equip) {
        equip.setStr(getRandStatAbove(equip.getStr(), 5));
        equip.setDex(getRandStatAbove(equip.getDex(), 5));
        equip.setInt(getRandStatAbove(equip.getInt(), 5));
        equip.setLuk(getRandStatAbove(equip.getLuk(), 5));
        equip.setMatk(getRandStatAbove(equip.getMatk(), 5));
        equip.setWatk(getRandStatAbove(equip.getWatk(), 5));
        equip.setAcc(getRandStatAbove(equip.getAcc(), 5));
        equip.setAvoid(getRandStatAbove(equip.getAvoid(), 5));
        equip.setJump(getRandStatAbove(equip.getJump(), 5));
        equip.setHands(getRandStatAbove(equip.getHands(), 5));
        equip.setSpeed(getRandStatAbove(equip.getSpeed(), 5));
        equip.setWdef(getRandStatAbove(equip.getWdef(), 10));
        equip.setMdef(getRandStatAbove(equip.getMdef(), 10));
        equip.setHp(getRandStatAbove(equip.getHp(), 10));
        equip.setMp(getRandStatAbove(equip.getMp(), 10));
        return equip;
    }

    public final Equip fuse(final Equip equip1, final Equip equip2) {
        if (equip1.getItemId() != equip2.getItemId()) {
            return equip1;
        }
        final Equip equip = (Equip) getEquipById(equip1.getItemId());
        equip.setStr(getRandStatFusion(equip.getStr(), equip1.getStr(), equip2.getStr()));
        equip.setDex(getRandStatFusion(equip.getDex(), equip1.getDex(), equip2.getDex()));
        equip.setInt(getRandStatFusion(equip.getInt(), equip1.getInt(), equip2.getInt()));
        equip.setLuk(getRandStatFusion(equip.getLuk(), equip1.getLuk(), equip2.getLuk()));
        equip.setMatk(getRandStatFusion(equip.getMatk(), equip1.getMatk(), equip2.getMatk()));
        equip.setWatk(getRandStatFusion(equip.getWatk(), equip1.getWatk(), equip2.getWatk()));
        equip.setAcc(getRandStatFusion(equip.getAcc(), equip1.getAcc(), equip2.getAcc()));
        equip.setAvoid(getRandStatFusion(equip.getAvoid(), equip1.getAvoid(), equip2.getAvoid()));
        equip.setJump(getRandStatFusion(equip.getJump(), equip1.getJump(), equip2.getJump()));
        equip.setHands(getRandStatFusion(equip.getHands(), equip1.getHands(), equip2.getHands()));
        equip.setSpeed(getRandStatFusion(equip.getSpeed(), equip1.getSpeed(), equip2.getSpeed()));
        equip.setWdef(getRandStatFusion(equip.getWdef(), equip1.getWdef(), equip2.getWdef()));
        equip.setMdef(getRandStatFusion(equip.getMdef(), equip1.getMdef(), equip2.getMdef()));
        equip.setHp(getRandStatFusion(equip.getHp(), equip1.getHp(), equip2.getHp()));
        equip.setMp(getRandStatFusion(equip.getMp(), equip1.getMp(), equip2.getMp()));
        return equip;
    }

    public final int getTotalStat(final Equip equip) { //i get COOL when my defense is higher on gms...
        return equip.getStr() + equip.getDex() + equip.getInt() + equip.getLuk() + equip.getMatk() + equip.getWatk() + equip.getAcc() + equip.getAvoid() + equip.getJump()
                + equip.getHands() + equip.getSpeed() + equip.getHp() + equip.getMp() + equip.getWdef() + equip.getMdef();
    }

    public final MapleStatEffect getItemEffect(final int itemId) {
        MapleStatEffect ret = itemEffects.get(Integer.valueOf(itemId));
        if (ret == null) {
            final MapleData item = getItemData(itemId);
            if (item == null || item.getChildByPath("spec") == null) {
                return null;
            }
            ret = MapleStatEffect.loadItemEffectFromData(item.getChildByPath("spec"), itemId);
            itemEffects.put(Integer.valueOf(itemId), ret);
        }
        return ret;
    }

    public final MapleStatEffect getItemEffectEX(final int itemId) {
        MapleStatEffect ret = itemEffectsEx.get(Integer.valueOf(itemId));
        if (ret == null) {
            final MapleData item = getItemData(itemId);
            if (item == null || item.getChildByPath("specEx") == null) {
                return null;
            }
            ret = MapleStatEffect.loadItemEffectFromData(item.getChildByPath("specEx"), itemId);
            itemEffectsEx.put(Integer.valueOf(itemId), ret);
        }
        return ret;
    }

    public final int getCreateId(final int id) {
        final ItemInformation i = getItemInformation(id);
        if (i == null) {
            return 0;
        }
        return i.create;
    }

    public final int getCardMobId(final int id) {
        final ItemInformation i = getItemInformation(id);
        if (i == null) {
            return 0;
        }
        return i.monsterBook;
    }

    public final int getBagType(final int id) {
        final ItemInformation i = getItemInformation(id);
        if (i == null) {
            return 0;
        }
        return i.flag & 0xF;
    }

    public final int getWatkForProjectile(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null || i.equipStats == null || i.equipStats.get("incPAD") == null) {
            return 0;
        }
        return i.equipStats.get("incPAD");
    }

    public final boolean canScroll(final int scrollid, final int itemid) {
        return (scrollid / 100) % 100 == (itemid / 10000) % 100;
    }

    public final String getName(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.name;
    }

    public final String getDesc(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.desc;
    }

    public final String getMsg(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.msg;
    }

    public final short getItemMakeLevel(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return 0;
        }
        return i.itemMakeLevel;
    }

    public final boolean isDropRestricted(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return ((i.flag & 0x200) != 0 || (i.flag & 0x400) != 0 || GameConstants.isDropRestricted(itemId)) && (itemId == 3012000 || itemId == 3012015 || itemId / 10000 != 301) && itemId != 2041200 && itemId != 5640000 && itemId != 4170023 && itemId != 2040124 && itemId != 2040125 && itemId != 2040126 && itemId != 2040211 && itemId != 2040212 && itemId != 2040227 && itemId != 2040228 && itemId != 2040229 && itemId != 2040230 && itemId != 1002926 && itemId != 1002906 && itemId != 1002927;
    }

    public final boolean isPickupRestricted(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return ((i.flag & 0x80) != 0 || GameConstants.isPickupRestricted(itemId)) && itemId != 4001168 && itemId != 4031306 && itemId != 4031307;
    }

    public final boolean isAccountShared(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return (i.flag & 0x100) != 0;
    }

    public final int getStateChangeItem(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return 0;
        }
        return i.stateChange;
    }

    public final int getMeso(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return 0;
        }
        return i.meso;
    }

    public final boolean isShareTagEnabled(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return (i.flag & 0x800) != 0;
    }

    public final boolean isKarmaEnabled(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return i.karmaEnabled == 1;
    }

    public final boolean isPKarmaEnabled(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return i.karmaEnabled == 2;
    }

    public final boolean isPickupBlocked(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return (i.flag & 0x40) != 0;
    }

    public final boolean isLogoutExpire(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return (i.flag & 0x20) != 0;
    }

    public final boolean cantSell(final int itemId) { //true = cant sell, false = can sell
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return (i.flag & 0x10) != 0;
    }

    public final Pair<Integer, List<StructRewardItem>> getRewardItem(final int itemid) {
        final ItemInformation i = getItemInformation(itemid);
        if (i == null) {
            return null;
        }
        return new Pair<Integer, List<StructRewardItem>>(i.totalprob, i.rewardItems);
    }

    public final boolean isMobHP(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return (i.flag & 0x1000) != 0;
    }

    public final boolean isQuestItem(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return false;
        }
        return (i.flag & 0x200) != 0 && itemId / 10000 != 301;
    }

    public final Pair<Integer, List<Integer>> questItemInfo(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return new Pair<Integer, List<Integer>>(i.questId, i.questItems);
    }

    public final Pair<Integer, String> replaceItemInfo(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return new Pair<Integer, String>(i.replaceItem, i.replaceMsg);
    }

    public final List<Triple<String, Point, Point>> getAfterImage(final String after) {
        return afterImage.get(after);
    }

    public final String getAfterImage(final int itemId) {
        final ItemInformation i = getItemInformation(itemId);
        if (i == null) {
            return null;
        }
        return i.afterImage;
    }

    public final boolean itemExists(final int itemId) {
        if (GameConstants.getInventoryType(itemId) == MapleInventoryType.UNDEFINED) {
            return false;
        }
        return getItemInformation(itemId) != null;
    }

    public final boolean isCash(final int itemId) {
        if (getEquipStats(itemId) == null) {
            return GameConstants.getInventoryType(itemId) == MapleInventoryType.CASH;
        }
        return GameConstants.getInventoryType(itemId) == MapleInventoryType.CASH || getEquipStats(itemId).get("cash") != null;
    }

    public final ItemInformation getItemInformation(final int itemId) {
        if (itemId <= 0) {
            return null;
        }
        return dataCache.get(itemId);
    }

    private ItemInformation tmpInfo = null;

    public void initItemBonusExpData(ResultSet sqlRewardData) throws SQLException {
        final int itemID = sqlRewardData.getInt("itemid");
        if (tmpInfo == null || tmpInfo.itemId != itemID) {
            if (!dataCache.containsKey(itemID)) {
                System.out.println("[initItemBonusExpData] Tried to load an item while this is not in the cache: " + itemID);
                return;
            }
            tmpInfo = dataCache.get(itemID);
        }

        if (tmpInfo.bonusExps == null) {
            tmpInfo.bonusExps = new LinkedList<StructBonusExp>();
        }

        StructBonusExp add = new StructBonusExp();
        add.incExpR = sqlRewardData.getInt("incExpR");
        add.termStart = sqlRewardData.getInt("termStart");

        tmpInfo.bonusExps.add(add);
    }

    public void initItemRewardData(ResultSet sqlRewardData) throws SQLException {
        final int itemID = sqlRewardData.getInt("itemid");
        if (tmpInfo == null || tmpInfo.itemId != itemID) {
            if (!dataCache.containsKey(itemID)) {
                System.out.println("[initItemRewardData] Tried to load an item while this is not in the cache: " + itemID);
                return;
            }
            tmpInfo = dataCache.get(itemID);
        }

        if (tmpInfo.rewardItems == null) {
            tmpInfo.rewardItems = new ArrayList<StructRewardItem>();
        }

        StructRewardItem add = new StructRewardItem();
        add.itemid = sqlRewardData.getInt("item");
        add.period = (add.itemid == 1122017 ? Math.max(sqlRewardData.getInt("period"), 7200) : sqlRewardData.getInt("period"));
        add.prob = sqlRewardData.getInt("prob");
        add.quantity = sqlRewardData.getShort("quantity");
        add.worldmsg = sqlRewardData.getString("worldMsg").length() <= 0 ? null : sqlRewardData.getString("worldMsg");
        add.effect = sqlRewardData.getString("effect");

        tmpInfo.rewardItems.add(add);
    }

    public void initItemAddData(ResultSet sqlAddData) throws SQLException {
        final int itemID = sqlAddData.getInt("itemid");
        if (tmpInfo == null || tmpInfo.itemId != itemID) {
            if (!dataCache.containsKey(itemID)) {
                System.out.println("[initItemAddData] Tried to load an item while this is not in the cache: " + itemID);
                return;
            }
            tmpInfo = dataCache.get(itemID);
        }

        if (tmpInfo.equipAdditions == null) {
            tmpInfo.equipAdditions = new EnumMap<EquipAdditions, Pair<Integer, Integer>>(EquipAdditions.class);
        }

        EquipAdditions z = EquipAdditions.fromString(sqlAddData.getString("key"));
        if (z != null) {
            tmpInfo.equipAdditions.put(z, new Pair<Integer, Integer>(sqlAddData.getInt("value1"), sqlAddData.getInt("value2")));
        }
    }

    public void initItemEquipData(ResultSet sqlEquipData) throws SQLException {
        final int itemID = sqlEquipData.getInt("itemid");
        if (tmpInfo == null || tmpInfo.itemId != itemID) {
            if (!dataCache.containsKey(itemID)) {
                System.out.println("[initItemEquipData] Tried to load an item while this is not in the cache: " + itemID);
                return;
            }
            tmpInfo = dataCache.get(itemID);
        }

        if (tmpInfo.equipStats == null) {
            tmpInfo.equipStats = new HashMap<String, Integer>();
        }

        final int itemLevel = sqlEquipData.getInt("itemLevel");
        if (itemLevel == -1) {
            tmpInfo.equipStats.put(sqlEquipData.getString("key"), sqlEquipData.getInt("value"));
        } else {
            if (tmpInfo.equipIncs == null) {
                tmpInfo.equipIncs = new HashMap<Integer, Map<String, Integer>>();
            }

            Map<String, Integer> toAdd = tmpInfo.equipIncs.get(itemLevel);
            if (toAdd == null) {
                toAdd = new HashMap<String, Integer>();
                tmpInfo.equipIncs.put(itemLevel, toAdd);
            }
            toAdd.put(sqlEquipData.getString("key"), sqlEquipData.getInt("value"));
        }
    }

    public void finalizeEquipData(ItemInformation item) {
        int itemId = item.itemId;

        // Some equips do not have equip data. So we initialize it anyway if not initialized
        // already
        // Credits: Jay :)
        if (item.equipStats == null) {
            item.equipStats = new HashMap<String, Integer>();
        }

        item.eq = new Equip(itemId, (byte) 0, -1, (byte) 0);
        short stats = GameConstants.getStat(itemId, 0);
        if (stats > 0) {
            item.eq.setStr(stats);
            item.eq.setDex(stats);
            item.eq.setInt(stats);
            item.eq.setLuk(stats);
        }
        stats = GameConstants.getATK(itemId, 0);
        if (stats > 0) {
            item.eq.setWatk(stats);
            item.eq.setMatk(stats);
        }
        stats = GameConstants.getHpMp(itemId, 0);
        if (stats > 0) {
            item.eq.setHp(stats);
            item.eq.setMp(stats);
        }
        stats = GameConstants.getDEF(itemId, 0);
        if (stats > 0) {
            item.eq.setWdef(stats);
            item.eq.setMdef(stats);
        }
        if (item.equipStats.size() > 0) {
            for (Entry<String, Integer> stat : item.equipStats.entrySet()) {
                final String key = stat.getKey();

                if (key.equals("STR")) {
                    item.eq.setStr(GameConstants.getStat(itemId, stat.getValue().intValue()));
                } else if (key.equals("DEX")) {
                    item.eq.setDex(GameConstants.getStat(itemId, stat.getValue().intValue()));
                } else if (key.equals("INT")) {
                    item.eq.setInt(GameConstants.getStat(itemId, stat.getValue().intValue()));
                } else if (key.equals("LUK")) {
                    item.eq.setLuk(GameConstants.getStat(itemId, stat.getValue().intValue()));
                } else if (key.equals("PAD")) {
                    item.eq.setWatk(GameConstants.getATK(itemId, stat.getValue().intValue()));
                } else if (key.equals("PDD")) {
                    item.eq.setWdef(GameConstants.getDEF(itemId, stat.getValue().intValue()));
                } else if (key.equals("MAD")) {
                    item.eq.setMatk(GameConstants.getATK(itemId, stat.getValue().intValue()));
                } else if (key.equals("MDD")) {
                    item.eq.setMdef(GameConstants.getDEF(itemId, stat.getValue().intValue()));
                } else if (key.equals("ACC")) {
                    item.eq.setAcc((short) stat.getValue().intValue());
                } else if (key.equals("EVA")) {
                    item.eq.setAvoid((short) stat.getValue().intValue());
                } else if (key.equals("Speed")) {
                    item.eq.setSpeed((short) stat.getValue().intValue());
                } else if (key.equals("Jump")) {
                    item.eq.setJump((short) stat.getValue().intValue());
                } else if (key.equals("MHP")) {
                    item.eq.setHp(GameConstants.getHpMp(itemId, stat.getValue().intValue()));
                } else if (key.equals("MMP")) {
                    item.eq.setMp(GameConstants.getHpMp(itemId, stat.getValue().intValue()));
                } else if (key.equals("MHPr")) {
                    item.eq.setHpR((short) stat.getValue().intValue());
                } else if (key.equals("MMPr")) {
                    item.eq.setMpR((short) stat.getValue().intValue());
                } else if (key.equals("tuc")) {
                    item.eq.setUpgradeSlots(stat.getValue().byteValue());
                } else if (key.equals("Craft")) {
                    item.eq.setHands(stat.getValue().shortValue());
                } else if (key.equals("durability")) {
                    item.eq.setDurability(stat.getValue().intValue());
                } else if (key.equals("charmEXP")) {
                    item.eq.setCharmEXP(stat.getValue().shortValue());
                } else if (key.equals("PVPDamage")) {
                    item.eq.setPVPDamage(stat.getValue().shortValue());
                }
            }
            if (item.equipStats.get("cash") != null && item.eq.getCharmEXP() <= 0) { //set the exp
                short exp = 0;
                int identifier = itemId / 10000;
                if (GameConstants.isWeapon(itemId) || identifier == 106) { //weapon overall
                    exp = 60;
                } else if (identifier == 100) { //hats
                    exp = 50;
                } else if (GameConstants.isAccessory(itemId) || identifier == 102 || identifier == 108 || identifier == 107) { //gloves shoes accessory
                    exp = 40;
                } else if (identifier == 104 || identifier == 105 || identifier == 110) { //top bottom cape
                    exp = 30;
                }
                item.eq.setCharmEXP(exp);
            }
        }
        GameConstants.getBoosterItemID(itemId);
    }

    public void initItemInformation(ResultSet sqlItemData) throws SQLException {
        final ItemInformation ret = new ItemInformation();
        final int itemId = sqlItemData.getInt("itemid");
        ret.itemId = itemId;
        ret.slotMax = GameConstants.getSlotMax(itemId) > 0 ? GameConstants.getSlotMax(itemId) : sqlItemData.getShort("slotMax");
        ret.price = Double.parseDouble(sqlItemData.getString("price"));
        ret.wholePrice = sqlItemData.getInt("wholePrice");
        ret.stateChange = sqlItemData.getInt("stateChange");
        ret.name = sqlItemData.getString("name");
        ret.desc = sqlItemData.getString("desc");
        ret.msg = sqlItemData.getString("msg");

        ret.flag = sqlItemData.getInt("flags");

        ret.karmaEnabled = sqlItemData.getByte("karma");
        ret.meso = sqlItemData.getInt("meso");
        ret.monsterBook = sqlItemData.getInt("monsterBook");
        ret.itemMakeLevel = sqlItemData.getShort("itemMakeLevel");
        ret.questId = sqlItemData.getInt("questId");
        ret.create = sqlItemData.getInt("create");
        ret.replaceItem = sqlItemData.getInt("replaceId");
        ret.replaceMsg = sqlItemData.getString("replaceMsg");
        ret.afterImage = sqlItemData.getString("afterImage");
        ret.cardSet = 0;
        if (ret.monsterBook > 0 && itemId / 10000 == 238) {
            mobIds.put(ret.monsterBook, itemId);
            for (Entry<Integer, Triple<Integer, List<Integer>, List<Integer>>> set : monsterBookSets.entrySet()) {
                if (set.getValue().mid.contains(itemId)) {
                    ret.cardSet = set.getKey();
                    break;
                }
            }
        }

        final String scrollRq = sqlItemData.getString("scrollReqs");
        if (scrollRq.length() > 0) {
            ret.scrollReqs = new ArrayList<Integer>();
            final String[] scroll = scrollRq.split(",");
            for (String s : scroll) {
                if (s.length() > 1) {
                    ret.scrollReqs.add(Integer.parseInt(s));
                }
            }
        }
        final String consumeItem = sqlItemData.getString("consumeItem");
        if (consumeItem.length() > 0) {
            ret.questItems = new ArrayList<Integer>();
            final String[] scroll = scrollRq.split(",");
            for (String s : scroll) {
                if (s.length() > 1) {
                    ret.questItems.add(Integer.parseInt(s));
                }
            }
        }

        ret.totalprob = sqlItemData.getInt("totalprob");
        final String incRq = sqlItemData.getString("incSkill");
        if (incRq.length() > 0) {
            ret.incSkill = new ArrayList<Integer>();
            final String[] scroll = incRq.split(",");
            for (String s : scroll) {
                if (s.length() > 1) {
                    ret.incSkill.add(Integer.parseInt(s));
                }
            }
        }
        dataCache.put(itemId, ret);
    }

    public Item randomizeStats2(Equip equip, boolean b) {
        equip.setStr(getRandStat(equip.getStr(), 5));
        equip.setDex(getRandStat(equip.getDex(), 5));
        equip.setInt(getRandStat(equip.getInt(), 5));
        equip.setLuk(getRandStat(equip.getLuk(), 5));
        equip.setMatk(getRandStat(equip.getMatk(), 5));
        equip.setWatk(getRandStat(equip.getWatk(), 5));
        equip.setAcc(getRandStat(equip.getAcc(), 5));
        equip.setAvoid(getRandStat(equip.getAvoid(), 5));
        equip.setJump(getRandStat(equip.getJump(), 5));
        equip.setHands(getRandStat(equip.getHands(), 5));
        equip.setSpeed(getRandStat(equip.getSpeed(), 5));
        equip.setWdef(getRandStat(equip.getWdef(), 10));
        equip.setMdef(getRandStat(equip.getMdef(), 10));
        equip.setHp(getRandStat(equip.getHp(), 10));
        equip.setMp(getRandStat(equip.getMp(), 10));
        return equip;
    }

    public List<Pair<Integer, String>> getAllEquips() {
        final List<Pair<Integer, String>> itemPairs = new ArrayList<Pair<Integer, String>>();
        MapleData itemsData;
        itemsData = stringData.getData("Eqp.img").getChildByPath("Eqp");
        for (final MapleData eqpType : itemsData.getChildren()) {
            for (final MapleData itemFolder : eqpType.getChildren()) {
                itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
            }
        }
        return itemPairs;
    }
}
