package client.skills;

import java.io.File;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.concurrent.ScheduledFuture;

import client.PlayerStats;
import client.MapleCharacter;
import client.stats.BuffStats;
import client.stats.PlayerStat;
import client.stats.DiseaseStats;
import client.stats.MonsterStatus;
import client.stats.MonsterStatusEffect;
import client.items.Equip;
import client.items.IItem;
import client.items.IEquip;
import client.items.MapleInventory;
import client.items.MapleInventoryType;
import client.items.StructPotentialItem;

import constants.GameConstants;
import constants.ServerConstants;
import handler.channel.MapleMechDoor;
import handler.channel.PlayerHandler;
import launch.ChannelServer;
import launch.holder.MapleCoolDownValueHolder;

import packet.creators.UIPacket;
import packet.creators.MobPacket;
import packet.creators.MainPacketCreator;
import packet.creators.SecondaryStat;
import packet.skills.KaiserSkill;
import packet.skills.MechanicSkill;
import packet.skills.AngelicBusterSkill;

import provider.MapleData;
import provider.MapleDataTool;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;

import server.items.ItemInformation;
import server.items.InventoryManipulator;
import server.life.MapleMonster;
import server.life.MobSkillFactory;
import server.maps.MapleDoor;
import server.maps.MapleMap;
import server.maps.MapleMist;
import server.maps.MapleSummon;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.SummonMovementType;

import tools.Pair;
import tools.Triple;
import tools.ArrayMap;
import tools.CaltechEval;
import tools.Timer.BuffTimer;
import tools.RandomStream.Randomizer;

public class SkillStatEffect {

    private int sourceid;
    private boolean overTime, skill, absstats = true;
    private List<Triple<BuffStats, Integer, Boolean>> statups;
    private Map<MonsterStatus, Integer> monsterStatus;
    private Point lt, rb;
    private SkillStats effects;
    private List<DiseaseStats> cureDebuffs;
    private int stackskill;
    private byte level;
    private double hpR;
    private double mpR;
    private int mhpX = 0;
    private int mhpR = 0;
    private int lv2mhp = 0;
    private int mmpX = 0;
    private int mmpR = 0;
    private int lv2mmp = 0;
    private boolean isRune = false;
    private int time = 0;

    public static final SkillStatEffect loadItemEffectFromData(final MapleData source, final int itemid) {
        return loadFromData(source, itemid, false, false, 0);
    }

    public static final SkillStatEffect loadSkillEffectFromData(final MapleData source, final int skillid, final boolean overtime) {
        return loadFromData(source, skillid, true, overtime, 1);
    }

    public static final SkillStatEffect loadSkillEffectFromData(final MapleData source, final int skillid, final boolean overtime, final int level) {
        return loadFromData(source, skillid, true, overtime, level);
    }

    private static final void addBuffStatPairToListIfNotZero(final List<Triple<BuffStats, Integer, Boolean>> list, final BuffStats buffstat, final Integer val, boolean overlap) {
        if (val.intValue() != 0) {
            boolean isExist = false;
            for (Triple<BuffStats, Integer, Boolean> stats : list) {
                if (stats.getFirst() == buffstat) {
                    isExist = true;
                }
            }
            if (!isExist) {
                list.add(new Triple<BuffStats, Integer, Boolean>(buffstat, val, overlap));
            }
        }
    }

    private static SkillStatEffect loadFromData(final MapleData source, final int sourceid, final boolean skill, final boolean overTime, final int level) {
        final SkillStatEffect ret = new SkillStatEffect();
        ret.effects = new SkillStats(level);
        for (MapleData d : source.getChildren()) {
            try {
                if (!d.getName().equals("hs") && !d.getName().equals("lt") && !d.getName().equals("rb")
                        && !d.getName().equals("lt2") && !d.getName().equals("rb2") && !d.getName().equals("hit")
                        && !d.getName().equals("hit") && !d.getName().equals("ball") && !d.getName().equals("action")
                        && !d.getName().equals("Point") && !d.getName().equals("variableRect")
                        && !d.getName().equals("property") && !d.getName().equals("mob")) {
                    if (String.valueOf(d.getData()).contains("log40") || String.valueOf(d.getData()).contains("log70") || String.valueOf(d.getData()).contains("log20") || d.getName().equals("memo")) {
                        continue;
                    }
                    if (sourceid == 2321001) {
                        ret.effects.setStats(d.getName(), String.valueOf(d.getData()), true);
                    } else {
                        ret.effects.setStats(d.getName(), String.valueOf(d.getData()), false);
                    }
                }
            } catch (Exception e) {
                //if (!ServerConstants.realese) {
                System.out.println("[���] ��ų�� �ε��� �߸��� ���� ���ԵǾ����ϴ�. : " + sourceid + " : " + d.getName() + " : " + d.getData());
                //}
            }
        }
        ret.sourceid = sourceid;
        ret.skill = skill;
        ret.mhpR = ret.effects.getStats("mhpR");
        ret.time = ret.effects.getStats("time");
        if (ret.mhpR > 0) {
            if (!ServerConstants.hp_skillid_dummy.contains(String.valueOf(ret.sourceid))) {
                ServerConstants.hp_skillid_dummy = ServerConstants.hp_skillid_dummy + ret.sourceid + ",";
            }
        }
        ret.mhpX = ret.effects.getStats("mhpX");
        if (ret.mhpX > 0) {
            if (!ServerConstants.hp_skillid_dummy.contains(String.valueOf(ret.sourceid))) {
                ServerConstants.hp_skillid_dummy = ServerConstants.hp_skillid_dummy + ret.sourceid + ",";
            }
        }
        ret.lv2mhp = ret.effects.getStats("lv2mhp");
        if (ret.lv2mhp > 0) {
            if (!ServerConstants.hp_skillid_dummy.contains(String.valueOf(ret.sourceid))) {
                ServerConstants.hp_skillid_dummy = ServerConstants.hp_skillid_dummy + ret.sourceid + ",";
            }
        }
        ret.mmpR = ret.effects.getStats("mmpR");
        if (ret.mmpR > 0) {
            if (!ServerConstants.hp_skillid_dummy.contains(String.valueOf(ret.sourceid))) {
                ServerConstants.hp_skillid_dummy = ServerConstants.hp_skillid_dummy + ret.sourceid + ",";
            }
        }
        ret.mmpX = ret.effects.getStats("mmpX");
        if (ret.mmpX > 0) {
            if (!ServerConstants.hp_skillid_dummy.contains(String.valueOf(ret.sourceid))) {
                ServerConstants.hp_skillid_dummy = ServerConstants.hp_skillid_dummy + ret.sourceid + ",";
            }
        }
        ret.lv2mmp = ret.effects.getStats("lv2mmp");
        if (ret.lv2mmp > 0) {
            if (!ServerConstants.hp_skillid_dummy.contains(String.valueOf(ret.sourceid))) {
                ServerConstants.hp_skillid_dummy = ServerConstants.hp_skillid_dummy + ret.sourceid + ",";
            }
        }
        if ((!ret.skill && ret.time > -1) || (sourceid >= 2022125 && sourceid <= 2022129)) {
            ret.overTime = true;
        } else {
            if (ret.time < 2100000000 && sourceid != 400030002) {
                ret.time = ret.time * 1000;
            }
            ret.overTime = overTime || ret.isMorph() || ret.isPirateMorph() || ret.isFinalAttack() || ret.isInflation();
        }
        final ArrayList<Triple<BuffStats, Integer, Boolean>> statups = new ArrayList<Triple<BuffStats, Integer, Boolean>>();

        List<DiseaseStats> cure = new ArrayList<DiseaseStats>(5);
        if (ret.effects.getStats("poison") > 0) {
            cure.add(DiseaseStats.POISON);
        }
        if (ret.effects.getStats("seal") > 0) {
            cure.add(DiseaseStats.SEAL);
        }
        if (ret.effects.getStats("darkness") > 0) {
            cure.add(DiseaseStats.DARKNESS);
        }
        if (ret.effects.getStats("weakness") > 0) {
            cure.add(DiseaseStats.WEAKEN);
        }
        if (ret.effects.getStats("curse") > 0) {
            cure.add(DiseaseStats.CURSE);
        }
        ret.cureDebuffs = cure;

        final MapleData ltd = source.getChildByPath("lt");
        if (ltd != null) {
            ret.lt = (Point) ltd.getData();
            ret.rb = (Point) source.getChildByPath("rb").getData();
        }
        Map<MonsterStatus, Integer> monsterStatus = new ArrayMap<MonsterStatus, Integer>();

        if (skill) {
            switch (sourceid) {
                case 1000003: // ���̾� �ٵ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PDD, ret.effects.getStats("pddX"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MHPCutR, ret.effects.getStats("mhpR"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DamAbsorbShield, ret.effects.getStats("damAbsorbShieldR"), false));
                    break;
                case 2001002: // ���� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MagicGuard, ret.effects.getStats("x"), false));
                    break;
                case 2001003: // ���� �Ƹ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PDD, ret.effects.getStats("pdd"), false));
                    break;
                case 4001005: // ���̽�Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Speed, ret.effects.getStats("speed"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Jump, ret.effects.getStats("jump"), false));
                    break;
                case 4001006: // ���� ���̽�Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Speed, ret.effects.getStats("speed"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Jump, ret.effects.getStats("jump"), false));
                    break;
                case 1101006: // �г�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PowerGuard, ret.effects.getStats("y"), false));
                    break;
                case 1101013: // �޺� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ComboCounter, 1, false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 1200014: // ������Ż ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ElementalCharge, 1, false));
                    break;
                case 1301006: // ���̾� ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PDD, ret.effects.getStats("pdd"), false));
                    break;
                case 1301007: // ������ �ٵ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MaxHP, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MaxMP, ret.effects.getStats("x"), false));
                    break;
                case 1310013: // ��Ȧ�� ���̳�Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Beholder, ret.effects.getStats("x"), false));
                    break;
                case 1301013: // ��Ȧ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Beholder, 1, false));
                    break;
                case 1310016: // ��Ȧ���� ����
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCr, ret.effects.getStats("indieCr"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EPAD, ret.effects.getStats("epad"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EPDD, ret.effects.getStats("epdd"), false));
                    break;
                case 1301012: // ���Ǿ� Ǯ��
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    ret.time = 1500;
                    break;
                case 1311015: // ũ�ν� ���� ü��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.CrossOverChain, ret.effects.getStats("x"), false));
                    break;
                case 1320019: // ����ī���̼�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Reincarnation, 1, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.NotDamaged, 1, false));
                    ret.time = ret.getTime() / 30;
                    break;
                case 1201013: // ������ ����
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 1201012: // ���ڵ� ����
                    monsterStatus.put(MonsterStatus.SPEED, ret.effects.getStats("x"));
                    break;
                case 1201011: // �÷��� ����
                    monsterStatus.put(MonsterStatus.Burned, ret.effects.getStats("dot"));
                    break;
                case 1211008: // ����Ʈ�� ����
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                /*
			case 1105: // ���̽� ����Ʈ
				statups.add(new Triple<>(BuffStats.IndieMaxDamageOver, 2050000000, true));
				ret.time = Integer.MAX_VALUE;
				ret.overTime = true;
				break;
                 */
                case 400021000: // �����ε� ����
                    statups.add(new Triple<>(BuffStats.IndiePMdR, ret.effects.getStats("z"), true));
                    break;
                case 1210016: // ���� �Ƹ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BlessingArmorIncPAD, ret.effects.getStats("epad"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BlessingArmor, ret.effects.getStats("x"), false));
                    break;
                case 1221004: // ����� ����
                    monsterStatus.put(MonsterStatus.SEAL, 1);
                    break;
                case 1221015: // ������Ż ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePMdR, ret.effects.getStats("indiePMdR"), true));
                    break;
                case 1221052: // ������Ʈ
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 1221054: // ��ũ�λ�ƼƼ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.NotDamaged, 1, false));
                    ret.overTime = true;
                    break;
                case 2101005: // ������ �극��
                    monsterStatus.put(MonsterStatus.Burned, 1);
                    break;
                case 2101010: // �̱׳���Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.WizardIgnite, 1, false));
                    break;
                case 2101001: // �޵����̼�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMAD, ret.effects.getStats("indieMad"), true));
                    break;
                case 2111010: // ������ ���̷���
                    monsterStatus.put(MonsterStatus.POISON, 1);
                    break;
                case 2111011: // ������Ż ���(��,��)
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.StackBuff, ret.effects.getStats("y"), false));
                    ret.overTime = true;
                    break;
                case 2111007: // �ڷ���Ʈ �����͸�(��,��)
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.TeleportMasteryOn, ret.effects.getStats("x"), false));
                    monsterStatus.put(MonsterStatus.Burned, 1);
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 2121006: // �䷲������
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    monsterStatus.put(MonsterStatus.Burned, 1);
                    break;
                case 2121011: // �÷��� ������
                    monsterStatus.put(MonsterStatus.Burned, 1);
                    monsterStatus.put(MonsterStatus.SHOWDOWN, 1);
                    monsterStatus.put(MonsterStatus.SPEED, ret.effects.getStats("x"));
                    ret.overTime = true;
                    break;
                case 2121052: // �ޱ⵵ �÷���
                case 2121055: // �ޱ⵵ �÷���
                    monsterStatus.put(MonsterStatus.Burned, 1);
                    break;
                case 2211007: // �ڷ���Ʈ �����͸�(��,��)
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.TeleportMasteryOn, ret.effects.getStats("x"), false));
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 2221045: // �ڷ���Ʈ �����͸�-�ֵ� ������(��,��)
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.TeleportMasteryRange, ret.effects.getStats("x"), false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 2221006: // ü�� ����Ʈ��
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 2221011: // ����¡ �극��
                    monsterStatus.put(MonsterStatus.FREEZE, 1);
//                    monsterStatus.put(MonsterStatus.MDEF, ret.effects.getStats("y"));
//                    monsterStatus.put(MonsterStatus.WDEF, ret.effects.getStats("x"));
                    break;
                case 2221054: // ���̽� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieStance, ret.effects.getStats("x"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IceAura, 1, false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 2211012: // ������Ż ���(��,��)
                    statups.add(new Triple<>(BuffStats.AntiMagicShell, 1, false));
                    break;
                case 3121014: // ���� ��
                    monsterStatus.put(MonsterStatus.SPEED, -ret.effects.getStats("w"));
                    break;
                case 3201008: // ��Ʈ ������
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 4121017: // ��ٿ� ç����
                    monsterStatus.put(MonsterStatus.SHOWDOWN, 1);
                    break;
                case 4221010: // ��緹�̵�
                case 4341011: // ��緹�̵�
                case 4121016: // ��緹�̵�
                    monsterStatus.put(MonsterStatus.Burned, 1);
                    break;
                case 4121015: // ���� �����
//                    monsterStatus.put(MonsterStatus.WATK, -ret.effects.getStats("w"));
//                    monsterStatus.put(MonsterStatus.WDEF, -ret.effects.getStats("w"));
                    monsterStatus.put(MonsterStatus.SPEED, ret.effects.getStats("x"));
                    break;
                case 4221007: // �θ޶� ����
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 4321006: // �ö��� �����
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 4331006: // �罽����
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 21110016: // �Ƶ巹���� �ν�Ʈ
                    statups.add(new Triple<>(BuffStats.AdrenalinBoost, 150, false));
                    statups.add(new Triple<>(BuffStats.IndieBooster, 1, true));
                    ret.overTime = true;
                    ret.time = 15000;
                    break;
                case 22110013: // ������Ʈ - ���ƿ�!
                    monsterStatus.put(MonsterStatus.DARKNESS, 1);
                    break;
                case 22140013: // ���̺� - ���ƿ�!
                    statups.add(new Triple<>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    break;
                case 22170064: // �극�� - ���ƿ�!
                    monsterStatus.put(MonsterStatus.Burned, 1);
                    break;
                case 25120003: // ������
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 25121006: // ��ȥ ����
                    monsterStatus.put(MonsterStatus.Burned, 1);
                    break;
                case 27121052: // �Ƹ��ٵ�
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 31111005: // ����� �극��
                    monsterStatus.put(MonsterStatus.POISON, 1);
                    break;
                case 31121001: // ���� ����Ʈ
                    monsterStatus.put(MonsterStatus.SPEED, -30);
                    break;
                case 31121003: // ���� ũ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DevilCry, 1, false));
                    monsterStatus.put(MonsterStatus.WATK, -ret.effects.getStats("x"));
                    monsterStatus.put(MonsterStatus.WDEF, -ret.effects.getStats("x"));
                    break;
                case 33101115: // ũ�ν� �ε�
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 35121055: // ���� Ÿ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BombTime, 1, false));
                    break;
                case 65001002: // ������ ũ�ν�
                    statups.add(new Triple<>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    break;
                case 65121011: // �ҿ� ��Ŀ ������Ʈ
                    statups.add(new Triple<>(BuffStats.AngelicBursterSoulSeeker, 1, false));
                    break;
                case 142111010: // ����ű ����
                    statups.add(new Triple<>(BuffStats.NewFlying, 1, false));
                    break;
                case 142111006: // ����ű �׶���
                case 142120003: // ����ű �׶���2
                    monsterStatus.put(MonsterStatus.SPEED, ret.effects.getStats("y"));
                    monsterStatus.put(MonsterStatus.WDEF, -ret.effects.getStats("s"));
                    break;
                case 37110009: // �޺���̼� Ʈ���̴�
                case 37120012: // �޺���̼� Ʈ���̴ץ�
                    statups.add(new Triple<>(BuffStats.RWCombination, 1, false));
                    break;
                case 51101004: // �ݷ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 1121010: // �η�����
                case 51121006: // �ҿ� ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Enrage, ret.effects.getStats("x") * 100 + ret.effects.getStats("mobCount"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EnrageCr, ret.effects.getStats("z"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EnrageCrDamMin, ret.effects.getStats("y"), false));
                    break;
                case 12001001: // ��������
                case 27000003: // ��������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MagicGuard, ret.effects.getStats("x"), false));
                    break;
                case 22001012: // ���� ��������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MagicGuard, ret.effects.getStats("x"), false));
                    break;
                case 22110016: // ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 13101022: // Ʈ�����ø� �� I
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.TriflingWhimOnOff, 1, false));
                    break;
                case 22171073: // ���н��� �ູ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EMAD, ret.effects.getStats("emad"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EPDD, ret.effects.getStats("epdd"), false));
                    break;
                case 2300003: // �κ󼭺�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DamAbsorbShield, ret.effects.getStats("damAbsorbShieldR"), false));
                    break;
                case 4101004: // ���̽�Ʈ
                case 4201003: // ���̽�Ʈ
                case 4301003: // ���� ���̽�Ʈ
                case 4311001: // ���� ���̽�Ʈ
                case 9001001: // ��� ���̽�Ʈ
                case 14001022: // ���̽�Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Speed, ret.effects.getStats("speed"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Jump, ret.effects.getStats("jump"), false));
                    break;
                case 2301004: // ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Bless, ret.effects.getStats("x"), false));
                    break;
                case 2300009: // ���� �ӻ��.
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BlessEnsenble, ret.effects.getStats("x"), false));
                    break;
                case 4001003: // ���谡 ��ũ ����Ʈ
                case 4330001: // ���꽺�� ��ũ ����Ʈ
                case 14001023: // ����Ʈ ��Ŀ ��ũ ����Ʈ
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DarkSight, ret.effects.getStats("x"), false));
                    break;
                case 14001027: // ������ ��Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.NightWalkerBat, 2, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Speed, ret.effects.getStats("speed"), false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 30001001: // ����
                case 30011001: // ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Speed, ret.effects.getStats("speed"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DarkSight, ret.effects.getStats("x"), false));
                    break;
                case 4211003: // ����Ŷ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PickPocket, ret.effects.getStats("x"), false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 4201011: // �޼� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MesoGuard, ret.effects.getStats("x"), false));
                    break;
                case 4111002: // ������ ��Ʈ��
                case 4211008: // ������ ��Ʈ��
                case 36111006: // ���߾� ��������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ShadowPartner, ret.effects.getStats("x"), false));
                    break;
                case 15101006: // ����Ʈ�� ����
                case 21101006: // �ƶ� - ����� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.WeaponCharge, ret.effects.getStats("x"), false));
                    break;
                case 21120021: // ���� ����II
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.AranSmashSwing, ret.effects.getStats("w"), false));
                    break;
                case 1311008: // �巡�� ��Ʈ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.STR, ret.effects.getStats("str"), false));
                    break;
                case 33101004: // ����
                    break;
                case 4330009: // ������ �̺�����
                    statups.clear();
                    ret.overTime = true;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCr, 100, true));
                    break;
                case 1001003: // ���̾� �ٵ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePDD, ret.effects.getStats("indiePdd"), true));
                    break;
                case 3101004: // �ҿ� �ַο� : Ȱ
                case 3201004: // �ҿ� �ַο� : ����
                case 13101003: // �ҿ� �ַο� : Ȱ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EPAD, ret.effects.getStats("epad"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.SoulArrow, ret.effects.getStats("x"), false));
                    break;
                case 33101003: // �ҿ� �ַο� : ũ�ν� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.SoulArrow, ret.effects.getStats("x"), false));
                    break;
                case 3121002: // ���� ������
                case 3221002: // ���� ������
                case 3321022:
                case 13121005: // ���� ������
                case 33121004: // ���� ������
                case 400001002: // ������ ���� ������
                    int value = 0;
                    value += ret.effects.getStats("y");
                    value |= ret.effects.getStats("x") << 8;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.SharpEyes, value, false));
                    break;
                case 3121007: // �Ϸ��� ����
                case 3221006: // �Ϸ��� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DEX, ret.effects.getStats("dex"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IllusionStep, ret.effects.getStats("x"),
                            false));
                    break;
                case 3121016: // ���꽺�� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.AdvancedQuiver, 1, false));
                    break;
                case 2111008: // ������Ż ����
                case 2211008: // ������Ż ����
                case 12101005: // ������Ż ����
                case 22141016: // ������Ż ���� 
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ElementalReset, ret.effects.getStats("x"), false));
                    break;
                case 22171054: // �������� �ҿ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 5100015: // ������ ����
                case 5120018: // ��Ʈ�� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.RWMovingEvar, sourceid != 5100015 ? 0 : 1, false));
                    break;
                case 142001007: // ����ű �ν���Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.KinesisPsychicEnergeShield, 1, false));
                    break;
                case 142001003: // ESP �ν���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    break;
                case 142101004: // ����ű �ǵ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.KinesisPsychicShield, ret.effects.getStats("er"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePDD, ret.effects.getStats("indiePdd"), true));
                    break;
                case 142101005: // ������ ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 142111008: // ���� ��ȭ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMADR, ret.effects.getStats("indieMadR"), true));
                    break;
                case 142121006: // ESP ��Ʋ ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMAD, ret.effects.getStats("indieMad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 1101004: // ���� �ν���
                case 1201004: // ���� �ν���
                case 1301004: // ���� �ν���
                case 2101008: // ���� �ν���
                case 2111005: // ���� �ν���
                case 2201010: // ���� �ν���
                case 2211005: // ���� �ν���
                case 2301008: // ���� �ν���
                case 3101002: // ���� �ν���
                case 3201002: // ũ�ν����� �ν���
                case 4101003: // �ں��� �ν���
                case 4201002: // ��� �ν���
                case 4311009: // �̵� �ν���
                case 5101006: // ��Ŭ �ν���
                case 5201003: // �� �ν���
                case 5301002: // ĳ�� �ν���
                case 11101001: // �ҵ� �ν���
                case 12101004: // ���� �ν���
                case 13101023: // ���� �ν���
                case 15101022: // ��Ŭ �ν���
                case 22111020: // ���� �ν���
                case 23101002: // ��󺸿�� �ν���
                case 24101005: // ���� �ν���
                case 31001001: // ���� �ν���
                case 31201002: // ���� �ν���
                case 36101004: // ���� �ν���
                case 32101005: // ������ �ν���
                case 33001003: // ũ�ν����� �ν���
                case 35101006: // ��ī�� �ν���
                case 51101003: // �ҵ� �ν���
                case 27101004: // ���� �ν���
                case 11101024: // �Ժ� �ΰ�
                case 14101022: // ������ �ν��� - ����Ʈ��Ŀ
                case 33101012: // ũ�ν����� �ν��� - ���ϵ�����
                case 37101003: // ��Ʋ�� �ν��� - ������
                case 3301010:
                case 155101005:
                case 64101003: // ���� �ν���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Booster, ret.effects.getStats("x"), false));
                    break;
                case 21001003: // ���� �ν���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Booster, -(ret.effects.getStats("y")), false));
                    break;
                case 152101007: // ���� ��Ʋ�� �ν���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Booster, -2, false));
                    break;
                case 12000022: // ������Ʈ: �÷��� 1
                case 12100026: // ������Ʈ: �÷��� 2
                case 12110024: // ������Ʈ: �÷��� 3
                case 12120007: // ������Ʈ: �÷��� 4
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MAD, ret.effects.getStats("x"), false));
                    break;
                case 12101024: // �̱״ϼ�
                    statups.add(new Triple<>(BuffStats.FlameDischarge, 1, false));
                    break;
                case 12101023: // �� ���� ���̾�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMAD, ret.effects.getStats("indieMad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    break;
                case 12111023: // �� �Ǵн�
                    statups.add(new Triple<>(BuffStats.MAD, 1, false));
                    break;
                case 12120013: // ���Ǹ� ���� �÷���
                case 12120014: // ���Ǹ� ���� �÷���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IgnoreMobDamR, ret.effects.getStats("y"), false));
                    break;
                case 12121003: // �÷��� �踮��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.FireBarrier, ret.effects.getStats("x"), false));
                    break;
                case 12121005: // ���� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 14111024: // ������ ����Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ShadowServant, 1, false));
                    break;
                case 5120011: // ī���� ����
                case 5220012: // ī���� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 20031205: // ���� ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Invisible, ret.effects.getStats("x"), false));
                    break;
                case 24111002: // �� ���� ���� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.StackBuff, 1, false));
                    break;
                case 24111003: // �̽����� �����ؼ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHPR, ret.effects.getStats("indieMhpR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMMPR, ret.effects.getStats("indieMmpR"), true));
                    break;
                case 24111005: // ������Ʈ
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieACC, ret.effects.getStats("indieAcc"), true));
                    break;
                case 24121004: // ������ ���� �Ƹ���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieIgnoreMobpdpR, ret.effects.getStats("indieIgnoreMobpdpR"), true));
                    break;
                case 5121015: // ����������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePADR, ret.effects.getStats("indiePadR"), true));
                    break;
                case 51111003: // ���̴� ����
                case 11111007: // ���̴� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 5111010: // ���ο� ����ú�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DamAbsorbShield, ret.effects.getStats("damAbsorbShieldR"), false));
                    break;
                case 5101011: // ��Ż Ŭ���Ƽ
                case 15101008: // ��Ż Ŭ���Ƽ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieACC, ret.effects.getStats("indieAcc"), true));
                    break;
                case 21111001: // ����Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EPAD, ret.effects.getStats("epad"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EPDD, ret.effects.getStats("epdd"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.KnockBack, ret.effects.getStats("x"), false));
                    break;
                case 21111012: // ���� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMAD, ret.effects.getStats("indieMad"), true));
                    break;
                case 51001006: // �ξ� ����
                case 51001007: // �ξ� ����
                case 51001008: // �ξ� ����
                case 51001009: // �ξ� ����
                case 51001010: // �ξ� ����
                    statups.add(new Triple<>(BuffStats.RoyalGuardPrepare, 1, false));
                    ret.time = 1420;
                    break;
                case 51001005: // �ξ� ����
                    statups.add(new Triple<>(BuffStats.RoyalGuardState, 1, false));
                    break;
                case 8004: // ������ �Ĺ� ������
                case 1211011: // �Ĺ������
                case 10008004: // ������ �Ĺ� ������
                case 20008004: // ������ �Ĺ� ������
                case 20018004: // ������ �Ĺ� ������
                case 20028004: // ������ �Ĺ� ������
                case 20038004: // ������ �Ĺ� ������
                case 30008004: // ������ �Ĺ� ������
                case 30018004: // ������ �Ĺ� ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.CombatOrders, ret.effects.getStats("x"), false));
                    break;
                case 5121009: // ���� �ν���
                case 15111005: // ���� �ν���
                case 15121005: // ���� �ν���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PartyBooster, ret.effects.getStats("x"), false));
                    break;
                case 64100004:
                case 64110005:
                case 64120006:
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.KadenaStack, 1, false));
                    break;
                case 400001006: // ������ ���� �ν���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PartyBooster, 1, false));
                    break;
                case 5001005: // �뽬
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Dash_Speed, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Dash_Jump, ret.effects.getStats("y"), false));
                    break;
                case 4321000: // ����̵� ����
                    ret.time = 1000;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Speed, 100 + ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Jump, ret.effects.getStats("y"), false)); // always																										// there
                    break;
                case 1101007: // �Ŀ� ���÷���
                case 1201007: // �Ŀ� ���÷���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ACC, ret.effects.getStats("x"), false));
                    break;
                case 31101003: // ��ũ ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PowerGuard, 1, false));
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 400001003: // ������ ������ �ٵ�
                case 9001008: // ������ �ٵ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MaxHP, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MaxMP, ret.effects.getStats("x"), false));
                    break;
                case 1001: // ȸ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DotHealHPPerSecond, ret.effects.getStats("x"), false));
                    break;
                case 1311006: // �巡�� �ξ�
                    ret.effects.setStats("hpR", -ret.effects.getStats("x"));
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 4341002: // ���̳� ��
                    //ret.time = 60 * 1000;
                    //ret.overTime = true;
                    //ret.effects.setStats("hpR", -ret.effects.getStats("x"));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.FinalCut, ret.effects.getStats("w"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.NotDamaged, 1, false));
                    break;
                case 4331002: // �̷� �̹�¡
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ShadowPartner, ret.effects.getStats("x"), false));
                    break;
                case 27110007: // ������ Ÿ�̴�.
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.LifeTidal, 1, false));
                    break;
                case 1121000:
                case 1221000:
                case 1321000:
                case 2121000:
                case 2221000:
                case 2321000:
                case 3121000:
                case 3221000:
                case 4121000:
                case 4221000:
                case 5121000:
                case 5221000:
                case 21121000:
                case 22171068:
                case 27121009:
                case 31121004:
                case 31221008:
                case 36121008:
                case 32121007:
                case 24121008:
                case 4341000:
                case 5321005:
                case 23121005:
                case 25121108:
                case 35121007:
                case 33121007:
                case 37121006: // ������ ������ ���
                case 51121005: // �ñ׳ʽ� ������
                case 11121000: // �ñ׳ʽ� ������
                case 12121000: // �ñ׳ʽ� ������
                case 13121000: // �ñ׳ʽ� ������
                case 14121000: // �ñ׳ʽ� ������
                case 15121000: // �ñ׳ʽ� ������
                case 65121009: // ����� ���
                case 61121014: // ����� ���
                case 100001268: // ���� ������ ��ȣ
                case 142121016: // Ű�׽ý� �̰��� ���
                case 64121004: // ����� ���
                case 152121009:
                case 3321023:
                case 155121008:
                case 164121009:
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BasicStatUp, ret.effects.getStats("x"), false));
                    break;
                case 64120007:
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.CriticalBuff, ret.effects.getStats("w"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EnrageCrDamMin, ret.effects.getStats("x"), false));
                    break;
                case 3111011: // �ͽ�Ʈ�� ���ĸ� : Ȱ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PDD, -ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PAD, ret.effects.getStats("padX"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePMdR, ret.effects.getStats("indiePMdR"), true));
                    break;
                case 3211012: // �ͽ�Ʈ�� ���ĸ� : ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ExtremeArchery, ret.effects.getStats("x") / 5 * 2, false));
                    break;
                case 37121054: // �ƽø����� ĳ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.RWMaximizeCannon, ret.effects.getStats("y"), false));
                    break;
                case 37101001: // ��ŷ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.RWMovingEvar, 90, false));
                    break;
                case 25121209: // ��ȥ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.SpiritGuard, 3, false));
                    break;
                case 25121131: // ���� ��� �ش�ȭ
                    statups.clear();
                    statups.add(0, new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(1, new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    statups.add(2, new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    statups.add(3, new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBDR, ret.effects.getStats("indieBDR"), true));
                    statups.add(4, new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieIgnoreMobpdpR, ret.effects.getStats("indieIgnoreMobpdpR"), true));
                    break;
                case 65121004: // �ҿ� ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.SoulGazeCriDamR, ret.effects.getStats("x"), false));
                    break;
                case 22151003: // ���� �������Ͻ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MagicResistance, ret.effects.getStats("x"), false));
                    break;
                case 21000000: // �ƶ� �޺�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ComboAbilityBuff, ret.effects.getStats("x"), false));
                    break;
                case 21101005: // �巹��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ComboDrain, ret.effects.getStats("x"), false));
                    break;
                case 31110004: // ��ũ �ε��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PDD, ret.effects.getStats("pddR"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, ret.effects.getStats("asrR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieTerR, ret.effects.getStats("terR"), true));
                    break;
                case 51111004: // �ҿ� �ε��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DefenseAtt, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, ret.effects.getStats("y"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieTerR, ret.effects.getStats("z"), true));
                    break;
                case 51111008: // �ҿ� ��ũ
                    statups.add(new Triple<>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    statups.add(new Triple<>(BuffStats.MichaelSoulLink, 1, false));
                    break;
                case 51121052: // ���鸮 ����
                    statups.add(new Triple<>(BuffStats.IndieDamR, 10, true));
                    break;
                case 21121058:
                    statups.add(new Triple<>(BuffStats.AdrenalinBoost, 1, false));
                    statups.add(new Triple<>(BuffStats.IndieBooster, 1, true));
                    ret.time = 15000;
                    break;
                case 31121005: // ��Ÿ�����ý�
                    int dam = ret.effects.getStats("damage") - 100;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHPR, ret.effects.getStats("indieMhpR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DamR, ret.effects.getStats("damR"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DevilishPower, dam < 0 ? -dam : dam, false));
                    ret.overTime = true;
                    break;
                case 31121007: // ���Ǵ�Ƽ ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.InfinityForce, 1, false));
                    break;
                case 31121002: // ���Ǹ� ��ġ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.VampiricTouch, ret.effects.getStats("x"), false));
                    break;
                case 22131001: // ���� �ǵ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PDD, ret.effects.getStats("pdd"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PartyBarrier, ret.effects.getStats("x"), false));
                    break;
                case 22181003: // �ҿｺ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.SoulStone, ret.effects.getStats("x"), false));
                    break;
                case 4121014: // ��ũ ������Ƽ
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 4121054: // ���� ���
                    statups.clear();
                    statups.add(new Triple<>(BuffStats.BleedingToxin, 1, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    monsterStatus.put(MonsterStatus.POISON, ret.effects.getStats("dot"));
                    break;
                case 4221013: // ������ �ν���Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 23111004: // �̱״Ͻ� �ξ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 23121004: // �ؽþ�Ʈ ���Ǹ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EMHP, ret.effects.getStats("emhp"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePADR, ret.effects.getStats("indiePadR"), true));
                    break;
                case 33001007: // ���� ��Ծ�
                case 33001008: // ���� ��Ծ�
                case 33001009: // ���� ��Ծ�
                case 33001010: // ���� ��Ծ�
                case 33001011: // ���� ��Ծ�
                case 33001012: // ���� ��Ծ�
                case 33001013: // ���� ��Ծ�
                case 33001014: // ���� ��Ծ�
                case 33001015: // ���� ��Ծ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.JaguarSummoned, 3870, false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 33101005: // �Ͽ︵
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.HowlingDefence, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.HowlingMaxMP, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.HowlingEvasion, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.HowlingCritical, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.HowlingAttackDamage, ret.effects.getStats("z"), false));
                    break;
                case 33111007: // ��Ʈ ��
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Speed, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BeastFormDamageUp, ret.effects.getStats("z"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Booster, ret.effects.getStats("w"), false));
                    break;
                case 2311009: // Ȧ�� ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.HolyMagicShell, ret.effects.getStats("x"), false));
                    ret.overTime = true;
                    break;
                case 11001022: // ������Ʈ : �ҿ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ElementSoul, 1, false));
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 11101022: // ���� ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCr, ret.effects.getStats("indieCr"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BuckShot, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PoseType, 1, false));
                    break;
                case 11101023: // �̳� Ʈ����Ʈ
                    statups.add(new Triple(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 11111022: // ����¡ ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PoseType, 2, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePMdR, ret.effects.getStats("indiePMdR"), true)); //IndieDamR
                    break;
                case 11111023: // Ʈ�� ����Ʈ
                    monsterStatus.put(MonsterStatus.WDEF, -ret.effects.getStats("v"));
                    break;
                case 11111024: // �ҿ� �����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePDD, ret.effects.getStats("indiePdd"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHP, ret.effects.getStats("indieMhp"), true));
                    break;
                case 11121054: // �ҿ� ����
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 4001002: // �𽺿���
                case 14001002: // �𽺿���
                    monsterStatus.put(MonsterStatus.WATK, ret.effects.getStats("x"));
                    monsterStatus.put(MonsterStatus.WDEF, ret.effects.getStats("y"));
                    break;
                case 1211013: // ����
                    monsterStatus.put(MonsterStatus.WATK, ret.effects.getStats("x"));
                    monsterStatus.put(MonsterStatus.WDEF, ret.effects.getStats("x"));
                    monsterStatus.put(MonsterStatus.MATK, ret.effects.getStats("x"));
                    monsterStatus.put(MonsterStatus.MDEF, ret.effects.getStats("x"));
                    monsterStatus.put(MonsterStatus.AVOID, ret.effects.getStats("z"));
                    break;
                case 22131000: // ���� �÷���
                case 51111007: // ���̴� ������
                case 27101101: // �ι��̷�����Ƽ
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 22141001:
                case 1111008:
                case 4211002:
                case 3101005:
                case 1111005:
                case 5101002:
                case 5101003:
                case 5121004:
                case 5121005:
                case 5121007:
                case 5201004:
                case 4121008:
                case 22151001:
                case 4201004:
                case 33101001:
                case 33101002:
                case 32101001:
                case 32111011:
                case 32121004:
                case 33111002:
                case 33121002:
                case 35101003:
                case 5111002:
                case 15101005:
                case 4331005:
                case 1121001:
                case 1221001:
                case 1321001:
                case 9001020:
                case 31111001:
                case 31101002:
                case 2211003:
                case 2311004:
                case 22181001:
                case 23111000:
                case 21110006:
                case 5301001:
                case 5311001:
                case 5311002:
                case 5310008:
                case 2022994:
                    if (sourceid == 5310008) { // ��Ű ���̺� �ְ� ���
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.KeyDownTimeIgnore, 1, false));
                    }
                    if (sourceid == 5311002) { // ��Ű ���̺�
                        statups.add(new Triple<>(BuffStats.DamR, 5, false));
                    }
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 1120003: // ���꽺�� �޺�
                case 11110005: // ���꽺�� �޺�
                    break;
                case 90001004: // ����
                case 4321002: // �÷��� ��
                case 1111003: // �д�
                case 11111002: // �д�
                case 51121007: // �ҿ� ���Ʈ
                    monsterStatus.put(MonsterStatus.DARKNESS, ret.effects.getStats("x"));
                    break;
                case 64001001: // ü�ξ���:��Ʈ��ũ
                case 64001006: // ü�ξ���:��Ʈ��ũ
                case 64100000: // ü�ξ���:��Ʈ��ũ 1�� ��ȭ
                case 64110000: // ü�ξ���:��Ʈ��ũ 2�� ��ȭ
                case 64120000: // ü�ξ���:��Ʈ��ũ 3�� ��ȭ
                    monsterStatus.put(MonsterStatus.SPEED, -30);
                    break;
                case 4221003: // ��ٿ�
                case 4121003: // ��ٿ�
                case 33121005: // ĳ���� ��
                    monsterStatus.put(MonsterStatus.SHOWDOWN, ret.effects.getStats("x"));
                    monsterStatus.put(MonsterStatus.MDEF, ret.effects.getStats("x"));
                    monsterStatus.put(MonsterStatus.WDEF, ret.effects.getStats("x"));
                    break;
                case 2221001: // ���
                case 3211003: // ���ڵ� ��
                case 5211005: // �� ����Ʈ
                case 22121000: // ���̽� �극��
                case 90001006: // ���
                    monsterStatus.put(MonsterStatus.FREEZE, 1);
                    ret.time = Integer.MAX_VALUE;
                    ret.overTime = true;
                    break;
                case 25111206: // �ӹڼ�
                    monsterStatus.put(MonsterStatus.FREEZE, 1);
                    break;
                case 2121009: // ������ ����
                case 2221009: // ������ ����
                case 2321010: // ������ ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MasterMagicOn, 1, false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 12101001: // ���ο�
                    monsterStatus.put(MonsterStatus.SPEED, ret.effects.getStats("x"));
                    break;
                case 31121006: // ��ũ ���ε�
                    monsterStatus.put(MonsterStatus.POISON, ret.effects.getStats("dot"));
                    monsterStatus.put(MonsterStatus.FREEZE, 1);
                    break;
                case 14001021: // ������Ʈ : ��ũ�Ͻ�
                    statups.add(new Triple(BuffStats.CygnusElementSkill, 1, false));
                    monsterStatus.put(MonsterStatus.POISON, ret.effects.getStats("dot"));
                    break;
                case 14121004: // ������ ��Ƽġ
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 21110011: // �޺�������Ʈ
                    monsterStatus.put(MonsterStatus.FREEZE, 1);
                    break;
                case 23111002: // ������ ������ũ
                    monsterStatus.put(MonsterStatus.Burned, ret.effects.getStats("x"));
                    break;
                case 2211006: // ���̽� ����
                case 2221003: // �۷��̼� ü��
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 20031209: // ������Ʈ
                    ret.overTime = true;
                    break;
                case 20031210: // ���� ������Ʈ - 120705
                    ret.overTime = true;
                    break;
                case 2121005:
                case 2221005:
                case 2321003:
                case 12111004:
                case 3201007: // ��� �̱�
                case 3101007: // �ǹ� ȣũ
                case 3211005: // ������
                case 3111005: // �Ǵн�
                case 33111005: // �ǹ� ȣũ
                case 33101011: // �ǹ� ȣũ
                case 5321003: // ���׳�ƽ ��Ŀ
                    monsterStatus.put(MonsterStatus.STUN, Integer.valueOf(1));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.SUMMON, 1, false));
                    break;
                case 1085:
                case 1087:
                case 1090:
                case 1179:
                case 10001085:
                case 10001087:
                case 10001090:
                case 10001179:
                case 20001085:
                case 20001087:
                case 20001090:
                case 20001179:
                case 20011085:
                case 20011087:
                case 20011090:
                case 20011179:
                case 20021085:
                case 20021087:
                case 20021090:
                case 20021179:
                case 20031085:
                case 20031087:
                case 20031090:
                case 20031179:
                case 20041085:
                case 20041087:
                case 20041090:
                case 20041179:
                case 20051085:
                case 20051087:
                case 30001085:
                case 30001087:
                case 30001090:
                case 30001179:
                case 30011085:
                case 30011087:
                case 30011090:
                case 30011179:
                case 30021085:
                case 30021087:
                case 30021090:
                case 30021179:
                case 50001085:
                case 50001087:
                case 50001090:
                case 50001179:
                case 60001085:
                case 60001087:
                case 60001090:
                case 60001179:
                case 60011085:
                case 60011087:
                case 60011090:
                case 60011179:
                    ret.time = Integer.MAX_VALUE;
                    ret.overTime = true;
                    break;
                case 23121002: // �����帮 ���Ǿ�
                    //monsterStatus.put(MonsterStatus.WDEF, -ret.effects.getStats("x"));
                    break;
                case 2311003: // Ȧ�� �ɺ�
                case 9001002: // Ȧ�� �ɺ�
                case 400001020: // ������ Ȧ�� �ɺ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.HolySymbol, ret.effects.getStats("x"), false));
                    break;
                case 2211004: // ��
                case 2111004: // ��
                case 12111002: // ��
                case 90001005: // ����
                    monsterStatus.put(MonsterStatus.SEAL, 1);
                    break;
                case 4111003: // ������ ��
                case 14111001: // ������ ��
                    monsterStatus.put(MonsterStatus.SHADOW_WEB, 1);
                    break;
                case 4111009: // ���Ǹ� �ں���
                case 5201008: // ���Ǵ� �Ҹ�
                case 14111025: // ���Ǹ� ������
                    statups.add(new Triple(BuffStats.NoBulletConsume, 0, false));
                    break;
                case 2121004: // ���Ǵ�Ƽ
                case 2221004: // ���Ǵ�Ƽ
                case 2321004: // ���Ǵ�Ƽ
                    statups.add(new Triple(BuffStats.Infinity, ret.effects.getStats("x"), false));
                    statups.add(new Triple(BuffStats.Stance, ret.effects.getStats("prop"), false));
                    break;
                case 1121002: // ���Ľ�
                case 1221002: // ���Ľ�
                case 1321002: // ���Ľ�
                case 32111014: // ���Ľ�
                case 51121004: // ���Ľ�
                case 50001214: // ���� ��ȣ
                case 80001140: // ���� ��ȣ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Stance, ret.effects.getStats("prop"), false));
                    break;
                case 5321010: // ���̷� ���Ǹ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Stance, ret.effects.getStats("x"), false));
                    break;
                case 1005:
                case 10001005:
                case 20001005:
                case 30001005:
                case 20011005:
                case 20021005:
                case 20031005:
                case 30011005:
                case 50001005:
                case 100001005:
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MaxLevelBuff, ret.effects.getStats("x"), false));
                    break;
                case 2121002: // ���� ���÷���
                case 2221002: // ���� ���÷���
                case 2321002: // ���� ���÷���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ManaReflection, 1, false));
                    break;
                case 2321005: // ���꽺�� ����
                case 400001005: // ������ ���꽺�� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.AdvancedBless, level, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ACC, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHP, ret.effects.getStats("indieMhp"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMMP, ret.effects.getStats("indieMmp"), true));
                    break;
                case 9001003: // ����� �ູ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Speed, ret.effects.getStats("speed"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PDD, ret.effects.getStats("pdd"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EVA, ret.effects.getStats("eva"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMAD, ret.effects.getStats("indieMad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHPR, ret.effects.getStats("indieMhpR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMMPR, ret.effects.getStats("indieMmpR"), true));
                    break;
                case 5211009: // �ҷ�����Ʈ �Ҹ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 142121032: // ����ű ����
                    statups.add(new Triple<>(BuffStats.IndieMAD, 1, true));
                    break;
                case 5221018: // ���̷� ��Ÿ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieEVA, ret.effects.getStats("indieEva"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, 30, true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieStance, 60, true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePADR, 20, true));
                    break;
                case 5301003: // ��Ű����
                case 5320008: // ������ ��Ű ����
                    statups.clear();
                    statups.add(0, new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHP, ret.effects.getStats("indieMhp"), true));
                    statups.add(1, new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMMP, ret.effects.getStats("indieMmp"), true));
                    statups.add(2, new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieJump, ret.effects.getStats("indieJump"), true));
                    statups.add(3, new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieSpeed, ret.effects.getStats("indieSpeed"), true));
                    statups.add(4, new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAllStat, ret.effects.getStats("indieAllStat"), true));
                    break;
                case 5311004: // ��ũ�� �귿
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Roulette, 4, false));
                    break;
                case 80001034: // ����Ʈ ���̹� 1�ܰ�
                case 80001035: // ����Ʈ ���̹� 2�ܰ�
                case 80001036: // ����Ʈ ���̹� 3�ܰ�
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMAD, ret.effects.getStats("indieMad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieJump, ret.effects.getStats("indieJump"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieSpeed, ret.effects.getStats("indieSpeed"), true));
                    break;
                case 5111007:
                case 5120012:
                case 5211007:
                case 5220014:
                case 35111013:
                case 35120014:
                case 15111011:
                case 5311005:
                case 5320007: // ��Ű ���̽�
                    ret.overTime = true;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Dice, 0, false));
                    break;
                case 23101003: // ���Ǹ� ��ǻ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DamageReduce, ret.effects.getStats("damage"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.CriticalBuff, ret.effects.getStats("x"), false));
                    break;
                case 23111005: // ���� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, ret.effects.getStats("terR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieTerR, ret.effects.getStats("terR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DamAbsorbShield, 1, false));
                    break;
                case 400031007:
                case 400051022:
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.SUMMON, 1, false));
                    break;
                case 400041008:
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ShadowSpear, 1, false));
                    break;
                case 80000169: // ���� �ϻ�
                case 20050286: // ���� �ϻ�
                    statups.add(new Triple<>(BuffStats.Stance, 1, false));
                    break;
                case 25101009: // �����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.HiddenPossession, 1, false));
                    break;
                case 35111016: // ����Ʃ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 35121010: // ���ø����̾�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.AmplifyDamage, ret.effects.getStats("x"), false));
                    ret.time = 60000;
                    break;
                case 35121006: // ������Ƽ
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 35120002: // ����Ʈ ���̹� ��ȭ
                case 35111008: // �����κ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MaxHP, ret.effects.getStats("hp"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MaxHP, ret.effects.getStats("hcHp"), false));
                    break;
                case 35111002: // ���׳�ƽ �ʵ�
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 35111005: // �׼�������
                    monsterStatus.put(MonsterStatus.SPEED, ret.effects.getStats("x"));
                    monsterStatus.put(MonsterStatus.WDEF, ret.effects.getStats("y"));
                    break;
                case 35101007: // ����Ʈ �Ƹ�
                    statups.add(new Triple<>(BuffStats.Guard, ret.effects.getStats("x"), false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 32001016: // ���ο� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BMageAura, level, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieSpeed, ret.effects.getStats("indieSpeed"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 32101009: // �巹�� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BMageAura, level, false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 32111012: // ��� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BMageAura, level, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, ret.effects.getStats("indieAsrR"), true));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 32111016: // ��ũ ����Ʈ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DarkLighting, 1, false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 32121010: // ��Ʋ ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Enrage, ret.effects.getStats("x") * 100 + ret.effects.getStats("mobCount"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EnrageCrDamMin, ret.effects.getStats("y"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EnrageCr, ret.effects.getStats("z"), false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 32121017: // ��ũ ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BMageAura, level, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 32001014: // ����
                case 32100010: // ���� ��Ʈ��Ʈ
                case 32110017: // ���� ��Ʈ��Ʈ2
                case 32120019: // ���� ��Ʈ��Ʈ3
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BMageDeath, 0, false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 32121018: // ����� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BMageAura, level, false));
                    break;
                case 2311007: // �ڷ���Ʈ �����͸�
                case 12111007: // �ڷ���Ʈ �����͸�
                case 22161005: // �ڷ���Ʈ �����͸�
                case 32111010: // �ڷ���Ʈ �����͸�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.TeleportMasteryOn, ret.effects.getStats("x"), false));
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 33111011: // ��ο� ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DrawBack, ret.effects.getStats("x"), false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 2320011: // ������ ����
                case 2220010: // ������ ����
                case 2120010: // ������ ����
                    ret.time = 5000;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ArcaneAim, ret.effects.getStats("x"), false));
                    break;
                case 4101011:
                    statups.add(new Triple<>(BuffStats.NightLordMark, 1, false));
                    break;
                case 3111000: // ����
                case 3211000: // ����
                case 33111009: // ����
                case 13111001: // ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EPAD, ret.effects.getStats("epad"), false));
                    break;
                case 3120006: // ���Ǹ� ��ũ
                case 3220005: // ���Ǹ� ��ũ : ������
                    ret.overTime = true;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.SpiritLink, ret.effects.getStats("x"), false));
                    break;
                case 13111005: // �˹�Ʈ�ν�
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Speed, ret.effects.getStats("speed"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Jump, ret.effects.getStats("jump"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EPDD, ret.effects.getStats("epdd"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Albatross, 1, false));
                    break;
                case 27001004: // �ͽ��ٵ� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMMPR, ret.effects.getStats("indieMmpR"), true));
                    break;
                case 27101202: // ���̵� ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.KeyDownAreaMoving, ret.effects.getStats("x"), false));
                    break;
                case 400021071:
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.LightOrDark, 1, false));
                    break;
                case 27100003: // ���� ���� ��ũ�Ͻ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BlessOfDarkness, ret.effects.getStats("y"), false));
                    ret.overTime = true;
                    break;
                case 27111005: // ����Ʈ������ ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePDD, ret.effects.getStats("indiePdd"), true));
                    break;
                case 27111006: // ��ƽ �޵����̼�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EMAD, ret.effects.getStats("emad"), false));
                    break;
                case 20040216: // �����̾�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Larkness, ret.effects.getStats("x"), false));
                    break;
                case 20040217: // ��Ŭ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Larkness, ret.effects.getStats("y"), false));
                    break;
                case 20040219: // �������긮��
                case 20040220: // �������긮��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Larkness, 2, false));
                    ret.time = 1000;
                    break;
                case 27121005: // ��ũ ũ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.StackBuff, ret.effects.getStats("x"), false));
                    break;
                case 27121006: // ��ũ�Ͻ� �Ҽ���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ElementalReset, ret.effects.getStats("y"), false));
                    break;
                case 31121053:
                case 31221053:
                case 32121053:
                case 33121053:
                case 35121053:
                case 37121053:
                case 152121042:
                case 155121042:
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 32121056: // ��Ʋ ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MASTER_OF_DEATH, 1, false));
                    break;
                case 33121054: // ���Ϸ�Ʈ ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.FinalAttackProp, ret.effects.getStats("x"), false));
                    break;
                case 14121052: // ���̴Ͼ�
                    statups.add(new Triple<>(BuffStats.IndiePMdR, ret.effects.getStats("indiePMdR"), true));
                    statups.add(new Triple<>(BuffStats.IndieCr, 100, true));
                    statups.add(new Triple<>(BuffStats.Stance, 100, false));
                    break;
                case 60001216: // �����ý���ġ : �����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ReshuffleSwitch, 1, false));
                    ret.time = Integer.MAX_VALUE;
                    ret.overTime = true;
                    break;
                case 60001217: // �����ý���ġ : ���ݸ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ReshuffleSwitch, 0, false));
                    ret.time = Integer.MAX_VALUE;
                    ret.overTime = true;
                    break;
                case 61101004: // ������ ��
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Booster, ret.effects.getStats("x"), false));
                    ret.overTime = true;
                    break;
                case 61101002: // �� ���� �ҵ�
                case 61110211: // �� ���� �ҵ� (Ʈ�����ǱԷ��̼�)
                case 61120007: // ���꽺�� �� ���� �ҵ�
                case 61121217: // ���꽺�� �� ���� �ҵ� (Ʈ�����ǱԷ��̼�)
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.StopForceAtomInfo, ret.effects.getStats("cooltime"), false));
                    ret.time = level * 12 * 1000;
                    ret.overTime = true;
                    break;
                case 61111003: // ������ ��Ʈ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, ret.effects.getStats("asrR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieTerR, ret.effects.getStats("terR"), true));
                    break;
                case 61111004: // īŻ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 61121009: // �ι���Ʈ �Ƹ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PartyBarrier, ret.effects.getStats("v"), false));
                    break;
                case 61111008: // ���̳� �ǱԷ��̼� (3��)
                case 61120008: // ���̳� �ǱԷ��̼� (4��)
                case 61121053: // ���̳� Ʈ����
                    statups.add(new Triple<>(BuffStats.Speed, ret.effects.getStats("speed"), false));
                    statups.add(new Triple<>(BuffStats.Morph, ret.effects.getStats("morph"), false));
                    statups.add(new Triple<>(BuffStats.Stance, ret.effects.getStats("prop"), false));
                    statups.add(new Triple<>(BuffStats.IndieCr, ret.effects.getStats("cr"), true));
                    statups.add(new Triple<>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    statups.add(new Triple<>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    break;
                case 5211014: // ��Ÿ ���͵�
                case 5211011: // �����ũ��
                case 5211015: // ���
                case 5211016: // ���
                case 5321004: // ����Ʈ ��Ű Ʈ����
                case 5320011: // ����Ʈ ��Ű Ʈ����2
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Stance, ret.effects.getStats("prop"), false));
                    monsterStatus.put(MonsterStatus.SPEED, ret.effects.getStats("x"));
                    ret.overTime = true;
                    break;
                case 65111003: // �� ���� ���μ�Ʈ
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 80001155: // �ҿ� ��Ʈ��Ʈ
                case 60011219: // �ҿ� ��Ʈ��Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 65101002: // �Ŀ� Ʈ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PowerTransferGauge, 1000, false));
                    break;
                case 65111004: // ���̾� ���ͽ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Stance, ret.effects.getStats("prop"), false));
                    break;
                case 65121053: // ���̳� ��Ʈ��Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.CriticalBuff, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, ret.effects.getStats("asrR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.TerR, ret.effects.getStats("terR"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieStance, ret.effects.getStats("indieStance"), true));
                    ret.overTime = true;
                    break;
                case 31201003: // ���� ������
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 31211003: // ������Ʈ �̺�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, ret.effects.getStats("y"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieTerR, ret.effects.getStats("z"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DamAbsorbShield, ret.effects.getStats("x"), false));
                    break;
                case 31211004: // ��ƺ��� ��Ŀ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DiabolikRecovery, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHPR, ret.effects.getStats("indieMhpR"), true));
                    break;
                case 31221004: // �����Թ� �Ŀ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 51121054: // ����ũ���� ť��[Hyper]
                    statups.clear();
                    statups.add(0, new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHPR, ret.effects.getStats("indieMhpR"), true));
                    statups.add(1, new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    ret.overTime = true;
                    break;
                case 51121053: // �� ���� ����ο�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    ret.time = 60 * 1000;
                    break;
                case 23121054: // ���� ����
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Stance, ret.effects.getStats("x"), false));
                    break;
                case 1121053:
                case 1221053:
                case 1321053:
                case 2121053:
                case 2221053:
                case 2321053:
                case 3121053:
                case 3221053:
                case 4121053:
                case 4221053:
                case 4341053:
                case 5121053:
                case 5221053:
                case 5321053:
                case 11121053:
                case 12121053:
                case 13121053:
                case 14121053:
                case 15121053:
                case 27121053:
                case 21121053:
                case 22171082:
                case 23121053:
                case 24121053:
                case 25121132:
                case 3321041:

                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    ret.time = 60 * 1000;
                    break;
                case 31011001: // ������ �����ε�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePMdR, ret.effects.getStats("indiePMdR"), true));
                    break;
                case 31011000: // �ͽõ� : ���� ������
                case 31201000: // �ͽõ� : ���� ��Ʈ����ũ
                case 31211000: // �ͽõ� : ������Ʈ ������
                case 31221000: // �ͽõ� : ����ť��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Exceed, 1, false));
                    break;
                case 36001002: // ��Ŭ���� �Ŀ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 36111003: // ���긮�� ����ú�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.StackBuff, ret.effects.getStats("prop"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DamageReduce, ret.effects.getStats("z"), false));
                    break;
                case 36111004: // ������ �ý���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.XenonAegisSystem, 1, false));
                    break;
                case 36001005: // ������Ʈ ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PinPointRocket, 1, false));
                    break;
                case 36101002: // ���Ͼ� �۽���Ƽ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.CriticalBuff, ret.effects.getStats("x"), false));
                    break;
                case 36101003: // ���Ǽǽ� ����������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHPR, ret.effects.getStats("indieMhpR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMMPR, ret.effects.getStats("indieMmpR"), true));
                    break;
                case 36121003: // ������ �ڵ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBDR, ret.effects.getStats("indieBDR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePMdR, ret.effects.getStats("indiePMdR"), true));
                    break;
                case 36121004: // ����ú� ��Ʈ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Stance, ret.effects.getStats("stanceProp"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IgnoreMobpdpR, ret.effects.getStats("ignoreMobpdpR"), false));
                    break;
                case 27121054: // �޸������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Larkness, 2, false));
                    break;
                case 31221054: // ����� ��Ʈ��Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 30021237: // �����̼� ����Ƽ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.NewFlying, 1, false));
                    break;
                case 4341052: // �Ƽ���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Asura, 100, false));
                    break;
                case 4341054: // ���� ���̵�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.WindBreakerFinal, ret.effects.getStats("damage") - 100, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, 10, true));
                    ret.overTime = true;
                    break;
                case 3110012: // ����Ʈ���̼�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Concentration, 1, false));
                    break;
                case 4221054: // �ø� �� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.FlipTheCoin, 1, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCr, ret.effects.getStats("x"), true));
                    break;
                case 5121054: // ��Ƽ�ķ���Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Stimulate, 1, false));
                    break;
                case 5121055: // ����Ƽ ���� �Ŀ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.UnityOfPower, 1, false));
                    break;
                case 5321054: // �� ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BuckShot, level, false));
                    break;
                case 15121004: // ���
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ShadowPartner, 1, false));
                    break;
                case 15121054: // õ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.StrikerHyperElectric, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 61121054: // ������Ƽ ���� ī����
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    break;
                case 1121054:
                    statups.clear();
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Stance, 100, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCr, ret.effects.getStats("indieCr"), true));
                    ret.overTime = true;
                    break;
                case 1321054: // ��ũ ����Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 2121054: // ���̾� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.FireAura, 1, false));
                    break;
                case 31121054: // ��� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ShadowPartner, ret.effects.getStats("x"), false));
                    break;
                case 2321055: // ����� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.HeavensDoor, 1, false));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 2321054: //������ ���� ����

                    ret.absstats = false;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IgnoreTargetDEF, 0, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.VengeanceOfAngel, 1, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMAD, 50, true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, -1, true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieIgnoreMobpdpR, 20, true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePMdR, 30, true));

                    ret.time = Integer.MAX_VALUE;
                    break;
                case 5221021: // �� ��ο�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.QuickDraw, ret.effects.getStats("damR"), false));
                    break;
                case 5221054: // ����� ��Ÿ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, ret.effects.getStats("y"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieTerR, ret.effects.getStats("v"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MaxHP, ret.effects.getStats("y"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCr, ret.effects.getStats("indieCr"), true));
                    break;
                case 15001022: // ������Ʈ : ����Ʈ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.CygnusElementSkill, 1, false));
                    break;
                case 3121054: // �����۷��̼�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Stance, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBDR, ret.effects.getStats("y"), true));
                    ret.overTime = true;
                    break;
                case 3221054: // �ҽ�����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCr, ret.effects.getStats("x"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCrMaxR, ret.effects.getStats("y"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieIgnoreMobpdpR, ret.effects.getStats("indieIgnoreMobpdpR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BullsEye, 1, false));
                    ret.overTime = true;
                    break;
                case 15120003: // ��ǳ
                case 15111022: // ��ǳ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DamR, ret.effects.getStats("y"), false));
                    break;
                case 13001022: // ������Ʈ : ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.CygnusElementSkill, 1, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    break;
                case 13101024: // ������ ���̵�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.SoulArrow, 1, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.CriticalBuff, ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 13110026: // ������ ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    break;
                case 13121004: // ���� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePADR, ret.effects.getStats("padR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IllusionStep, ret.effects.getStats("prop"), false));
                    break;
                case 13111023: // �˹�Ʈ�ν�
                    ret.absstats = false;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Albatross, level, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHP, ret.effects.getStats("indieMhp"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCr, ret.effects.getStats("indieCr"), true));
                    break;
                case 13120008: // �˹�Ʈ�ν� �ƽø�
                    ret.absstats = false;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IgnoreMobpdpR, ret.effects.getStats("ignoreMobpdpR"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Albatross, level, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHP, ret.effects.getStats("indieMhp"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, ret.effects.getStats("indieAsrR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCr, ret.effects.getStats("indieCr"), true));
                    break;
                case 13121052: // ���
                    monsterStatus.put(MonsterStatus.Burned, 1);
                    break;
                case 13121054: // ���� �긵��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.StormBringer, 1, false));
                    break;
                case 2311012: // ������ �����ؼ�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.AntiMagicShell, 1, false));
                    break;
                case 3211011: // ���� ų��
                case 27111004: // ��Ƽ ������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.AntiMagicShell, 3, false));
                    break;
                case 2201001: // �޵����̼�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMAD, ret.effects.getStats("indieMad"), true));
                    break;
                case 2201009: // ĥ�� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ChillingStep, 1, false));
                    break;
                case 1321015: // ��ũ�����̽�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBDR, ret.effects.getStats("indieBDR"), true));
                    break;
                case 21121054: // �޺� �𸮹�Ƽ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ComboUnlimited, ret.effects.getStats("cooltime"), false));
                    break;
                case 36121053: // ������ ���ʱ�
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case 36121054: // �Ƹ����� ���׷�����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.SurplusSupply, ret.effects.getStats("x"), false));
                    break;
                case 65121054: // �ҿ� ����Ʈ
                    statups.add(new Triple<>(BuffStats.IndieIgnoreMobpdpR, ret.effects.getStats("indieIgnoreMobpdpR"), true));
                    statups.add(new Triple<>(BuffStats.IndieBDR, ret.effects.getStats("indieBDR"), true));
                    ret.overTime = true;
                    break;
                case 37000006: // �ε��� �ǵ�
                    statups.add(new Triple<>(BuffStats.RWBarrier, 500, false));
                    break;
                case 400051011: // ������ ����Ʈ
                    statups.add(new Triple<>(BuffStats.EnergyBurst, 1, false));
                    ret.time = 30000;
                    break;
                case 1211014: // �Ķ��ũ ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 64001009: // ü�ξ���:ü�̽�
                case 64001010: // ü�ξ���:ü�̽�
                case 64001011: // ü�ξ���:ü�̽�
                    statups.add(new Triple<>(BuffStats.DarkSight, 10, false));
                    break;
                case 64121001: // ü�ξ���:����ũ�ٿ�
                    statups.add(new Triple<>(BuffStats.NotDamaged, 1, false));
                    break;
                case 64121053: // ������ų� ������Ʈ
                    statups.add(new Triple<>(BuffStats.SlowAttack, 2, false));
                    break;
                case 64121054: // ���δ� Ư�� ���
                    statups.add(new Triple<>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    statups.add(new Triple<>(BuffStats.IndieCr, ret.effects.getStats("indieCr"), true));
                    break;
                case 100000276: // ���ǵ� Ÿ��(����Ʈ)
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.TimeFastABuff, 1, false));
                    break;
                case 100000277: // ���ǵ� Ÿ��(�Ĺ�)
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.TimeFastBBuff, 1, false));
                    break;
                case 100001263: // ����� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ZeroAuraStr, 1, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePAD, ret.effects.getStats("indiePad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMAD, ret.effects.getStats("indieMad"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePDD, ret.effects.getStats("indiePdd"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieTerR, ret.effects.getStats("indieTerR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, ret.effects.getStats("indieAsrR"), true));
                    break;
                case 100001264: // ����� ������Ʈ
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ZeroAuraSpd, 1, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieACC, ret.effects.getStats("indieAcc"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieEVA, ret.effects.getStats("indieEva"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieJump, ret.effects.getStats("indieJump"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieSpeed, ret.effects.getStats("indieSpeed"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    break;
                case 100001274: // Ÿ�� Ȧ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.NotDamaged, 1, false));
                    ret.time = 15 * 1000;
                    ret.overTime = true;
                    break;
                case 400031012:
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.NotDamaged, 1, false));
                    ret.time = ret.effects.getStats("x") * 1000;
                    ret.overTime = true;
                    break;
                case 100001272: // Ÿ�� �����ε�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PreReviveOnce, ret.effects.getStats("x"), false));
                    ret.time = Integer.MAX_VALUE;
                    break;

                case 80001427: // �ż��� �� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieJump, ret.effects.getStats("indieJump"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieSpeed, ret.effects.getStats("indieSpeed"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieEXP, ret.effects.getStats("indieExp"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    ret.isRune = true;
                    break;
                case 80001428: // ����� �� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DotHealHPPerSecond, 1, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieEXP, ret.effects.getStats("indieExp"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, ret.effects.getStats("indieAsrR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieTerR, ret.effects.getStats("indieTerR"), true));
                    ret.isRune = true;
                    break;
                case 80001430: // �ر��� �� ����
                case 80001432: // �ĸ��� �� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieEXP, ret.effects.getStats("indieExp"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, ret.effects.getStats("indieDamR"), true));
                    ret.isRune = true;
                    break;
                case 80001875: // �ʿ��� ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.FixCoolTime, ret.effects.getStats("fixCoolTime"), false));
                    ret.isRune = true;
                    break;
                case 80001752: // õ���� �� ����ġ ����
                case 80001753: // ������ �� ����ġ ����
                case 80001878: // �ʿ��� �� ����ġ ����
                case 80001879: // ������ �� ����ġ ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieEXP, ret.effects.getStats("indieExp"), true));
                    ret.isRune = true;
                    break;
                case 80001757: // ������ �� �Ŵ� ����.
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieJump, ret.effects.getStats("indieJump"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieSpeed, ret.effects.getStats("indieSpeed"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Inflation, ret.effects.getStats("x"), false));
                    ret.isRune = true;
                    break;

                case 80001280: // ������ ���� ��������
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHPR, ret.effects.getStats("indieMhpR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMMPR, ret.effects.getStats("indieMmpR"), true));
                    break;
                case 80001218: // ȸ��Ű�
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PAD, ret.effects.getStats("x"), true));
                    break;
                case 80001455: // ����Ʈ����Ʈ ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePADR, ret.effects.getStats("indiePadR"), true));
                    break;
                case 80001456: // ��Ƽ���̴� ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ACC, 30000, true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MAD, 30000, true));
                    break;
                case 80001457: // ���� ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBDR, ret.effects.getStats("indieBDR"), true));
                    break;
                case 80001458: // �ｺ�� ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MHPCutR, -ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBDR, ret.effects.getStats("indieBDR"), true));
                    break;
                case 80001459: // ������ ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MMPCutR, -ret.effects.getStats("x"), false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieIgnoreMobpdpR, ret.effects.getStats("indieIgnoreMobpdpR"), true));
                    break;
                case 80001460: // ������Ƽ ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHPR, -ret.effects.getStats("indieMhpR"), true));
                    break;
                case 80001474: // ������Ʈ ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, ret.effects.getStats("indieBooster"), true));
                    break;
                case 80001477: // ���÷�Ƽ�� ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ACC, ret.effects.getStats("x"), false));
                    break;
                case 80001479: // ����ũ����Ŀ ��
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePADR, ret.effects.getStats("indiePadR"), true));
                    break;
                case 80001466:
                case 80001467:
                case 80001468:
                case 80001469:
                case 80001470:
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BuckShot, 1, false));
                    break;
                case 400011000: // ���� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieIgnoreMobpdpR, ret.effects.getStats("indieIgnoreMobpdpR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePMdR, ret.effects.getStats("indiePMdR"), true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.AuraWeapon, ret.effects.getStats("z"), false));
                    break;
                case 400011066: // �ٵ� ���� ��ƿ
                    statups.add(new Triple<>(BuffStats.IndieDamR, 0, true));
                    statups.add(new Triple<>(BuffStats.IndieAsrR, ret.effects.getStats("indieAsrR"), true));
                    statups.add(new Triple<>(BuffStats.Stance, 100, false));
                    break;
                case 400011016: // �ν��� ����
                    statups.add(new Triple<>(BuffStats.InstallMaha, level, false));
                    statups.add(new Triple<>(BuffStats.IndiePADR, ret.effects.getStats("indiePadR"), true));
                    break;
                case 400011011: // �� ���̾ƽ�
                    statups.add(new Triple<>(BuffStats.NotDamaged, 1, false));
                    break;
                case 400001010: // ���� �ǵ�
                    statups.add(new Triple<>(BuffStats.BlitzShield, ret.getTime(), false));
                    break;
                case 400021003: // ������
                    statups.add(new Triple<>(BuffStats.Pray, 1, false));
                    statups.add(new Triple<>(BuffStats.IndiePMdR, 40, true));
                    break;
                case 400031023: // ũ��Ƽ�� ��������
                    statups.add(new Triple<>(BuffStats.IndieCrMax, 1, true));
                    statups.add(new Triple<>(BuffStats.IndieCr, ret.getX(), true));
                    break;
                case 400031002: // �ַο� ����
                    statups.add(new Triple<>(BuffStats.IndieDamR, ret.getStat("indieDamR"), true));
                    break;
                case 400001012: // �̺���
                    statups.add(new Triple<>(BuffStats.ReflectDamR, ret.getStat("damage"), false));
                    break;
                case 400031006: // Ʈ�� ��������
                    statups.add(new Triple<>(BuffStats.TrueSniping, ret.getX(), false));
                    break;
                case 400031015: // ���ø� �ַο�
                    statups.add(new Triple<>(BuffStats.SplitArrow, 1, false));
                    break;
                case 400001023: // ��Ƽ�� ��ũ ����Ʈ
                    statups.add(new Triple<>(BuffStats.DarkSight, ret.effects.getStats("x"), false));
                    break;
                case 400041001: // �������� ���ο�
                    statups.add(new Triple<>(BuffStats.SpreadThrow, 1, false));
                    break;
                case 104:
                case 400051015: // ����Ʈ ��ũ��
                    statups.add(new Triple<>(BuffStats.DevilishPower, 1, false));
                    break;
                case 400051006: // �Ҹ� ��Ƽ
                    statups.add(new Triple<>(BuffStats.BulletParty, ret.getY(), false));
                    break;
                case 400031020: // �ܿ��� ��
                    statups.add(new Triple<>(BuffStats.SlowAttack, 2, false));
                    break;
                case 400011055: // �����ÿ�
                    statups.add(new Triple<>(BuffStats.Ellision, 1, false));
                    break;
                case 400031000: // ���̵��
                    statups.add(new Triple<>(BuffStats.GuidedArrow, ret.getZ(), false));
                    break;
                case 400051007: // �ų�����
                    statups.add(new Triple<>(BuffStats.IndieDamR, ret.getStat("indieDamR"), true));
                    statups.add(new Triple<>(BuffStats.LightningUnion, 1, false));
                    ret.overTime = true;
                    break;
                case 400031017: // ���ǵ��
                    statups.add(new Triple<>(BuffStats.IndieStance, ret.getStat("indieStance"), true));
                    statups.add(new Triple<>(BuffStats.IndiePADR, ret.getStat("indiePadR"), true));
                    statups.add(new Triple<>(BuffStats.IndieDamR_, ret.getStat("indieDamReduceR"), true));
                    statups.add(new Triple<>(BuffStats.Slpidia, 1, false));
                    break;
                case 400041009: // ��Ŀ
                    statups.add(new Triple<>(BuffStats.KeyDownMoving, 100, false));
                    break;
                case 22171083:
                    statups.add(new Triple<>(BuffStats.KeyDownMoving, 0, false));
                    ret.overTime = true;
                    ret.time = Integer.MAX_VALUE;
                    break;
                case 400041011: // ��Ŀ : ������
                    statups.add(new Triple<>(BuffStats.IndieMHPR, ret.getStat("indieMhpR"), true));
                    break;
                case 400041012: // ��Ŀ : ������ ����
                    statups.add(new Triple<>(BuffStats.IndieAsrR, ret.getStat("indieAsrR"), true));
                    statups.add(new Triple<>(BuffStats.IgnoreMobDamR, ret.getStat("z"), false));
                    break;
                case 400041013: // ��Ŀ : �𷡽ð�
                    break;
                case 400041014: // ��Ŀ : ��ī�ο� ��
                    statups.add(new Triple<>(BuffStats.IndiePMdR, ret.getStat("indiePMdR"), true));
                    break;
                case 400041015: // ��Ŀ : ��Ŀ
                    statups.add(new Triple<>(BuffStats.IndieMHPR, ret.getStat("indieMhpR"), true));
                    statups.add(new Triple<>(BuffStats.IndieAsrR, ret.getStat("indieAsrR"), true));
                    statups.add(new Triple<>(BuffStats.IndiePMdR, ret.getStat("indiePMdR"), true));
                    statups.add(new Triple<>(BuffStats.IgnoreMobDamR, ret.getStat("z"), false));
                    break;
                case 400051010: // ���� ����
                    statups.add(new Triple<>(BuffStats.IndiePMdR, ret.getStat("indiePMdR"), true));
                    statups.add(new Triple<>(BuffStats.SlowAttack, 2, false));
                    break;
                case 400011006: // ���� �����ũ��
                case 400011007: // ���� �����ũ��
                case 400011008: // ���� �����ũ��
                case 400011009: // ���� �����ũ��
                    statups.add(new Triple<>(BuffStats.IndieCr, ret.getStat("indieCr"), true));
                    break;
                case 400011010: // ���� ������
                    statups.add(new Triple<>(BuffStats.IndieAsrR, ret.getStat("s"), true));
                    statups.add(new Triple<>(BuffStats.IndieTerR, ret.getStat("s"), true));
                    break;
                case 400021006: // ���Ͽ� ����
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BMageAura, level, false));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.UnionAuraBlow, 1, false));
                    break;
                case 400031005: // ��Ծ� ����
                    statups.add(new Triple<>(BuffStats.SlowAttack, 2, false));
                    break;
                case 400011017: // ��Ŀ ������
                    statups.add(new Triple<>(BuffStats.SlowAttack, 2, false));
                    break;
                case 400041035: // ü�ξ���:ǻ��
                    statups.add(new Triple<>(BuffStats.IndieEmpty, 0, true));
                    break;
                case 400051018: // ����Ʈ����Ʈ
                    statups.add(new Triple<>(BuffStats.IndieAsrR, 0, true));
                    statups.add(new Triple<>(BuffStats.IndieCr, 0, true));
                    statups.add(new Triple<>(BuffStats.IndieStance, 0, true));
                    statups.add(new Triple<>(BuffStats.IndiePMdR, 0, true));
                    statups.add(new Triple<>(BuffStats.Spotlight, 1, false));
                    break;
                case 400041029: // �����ε� ���
                    statups.add(new Triple<>(BuffStats.OverloadMode, 1, false));
                    break;
                case 400021008: // ����ű ����̵�
                    statups.add(new Triple<>(BuffStats.PsychicTornado, 1, false));
                    break;
                case 152001005: // ũ����Ż ��Ż
                    statups.add(new Triple<>(BuffStats.NewFlying, 1, false));
                    statups.add(new Triple<>(BuffStats.IliumeFly, 1, false));
                    break;
                case 400011015: // ����Ʈ �극��ũ
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    statups.add(new Triple<>(BuffStats.IndieBooster, 2, true));
                    statups.add(new Triple<>(BuffStats.IndiePMdR, ret.getStat("indiePMdR"), true));
                    break;
                case 36121055:// ��Ʈ�ٿ� �ͽ��÷���
                    ret.time = 25000;
                    ret.overTime = true;
                    statups.add(new Triple<>(BuffStats.IndieDamR, ret.getStat("indieDamR"), true));
                    break;
                case 152001003:
                case 152101008:
                case 3311009:
                case 152121005:
                case 3321034:
                case 400051009:
                case 400021067:
                case 400041038:
                case 400051038:
                case 400011088:
                case 400021073:
                case 400051046:
                    ret.overTime = true;
                    statups.add(new Triple<>(BuffStats.IndieIliumStack, 1, true));
                    break;
                case 152001002:
                case 152120003:
                    statups.add(new Triple<>(BuffStats.CraftOf, 1, false));
                    ret.time = 2000;
                    break;
                case 152121011:
                    statups.add(new Triple<>(BuffStats.FastCharge, 1, false));
                    statups.add(new Triple<>(BuffStats.IndieFastCharge, 100, true));
                    break;
                case 3311012:
                    statups.add(new Triple<>(BuffStats.IndieAsrR, ret.getStat("s"), true));
                    break;
                case 155001001:
                    statups.add(new Triple<>(BuffStats.Speed, ret.getStat("speed"), false));
                    statups.add(new Triple<>(BuffStats.IndieStance, ret.getStat("indieStance"), true));
                    ret.time = 60000;
                    ret.overTime = true;
                    break;
                case 155101003:
                    statups.add(new Triple<>(BuffStats.IndiePAD, ret.getStat("indiePad"), true));
                    statups.add(new Triple<>(BuffStats.IndieCr, ret.getStat("indieCr"), true));
                    ret.time = 60000;
                    ret.overTime = true;
                    break;
                case 155111005:
                    statups.add(new Triple<>(BuffStats.IndieBooster, -1, true));
                    statups.add(new Triple<>(BuffStats.IndieEVAR, ret.getStat("indieEvaR"), true));
                    ret.time = 60000;
                    ret.overTime = true;
                    break;
                case 155121005:
                    statups.add(new Triple<>(BuffStats.IndieDamR, ret.getStat("indieDamR"), true));
                    statups.add(new Triple<>(BuffStats.IndieBDR, ret.getStat("indieBDR"), true));
                    statups.add(new Triple<>(BuffStats.IndieIgnoreMobpdpR, ret.getStat("indieIgnoreMobpdpR"), true));
                    ret.time = 60000;
                    ret.overTime = true;
                    break;
                case 155101006:
                    statups.add(new Triple<>(BuffStats.ArkTransform, 1, false));
                    statups.add(new Triple<>(BuffStats.IndiePAD, 30, true));
                    statups.add(new Triple<>(BuffStats.IndieStance, 100, true));
                    ret.overTime = true;
                    break;
                case 155101008:
                    statups.add(new Triple<>(BuffStats.ArkComeDie, 1, false));
                    ret.overTime = true;
                    break;
                case 152110008:
                case 152120014:
                    statups.add(new Triple<>(BuffStats.CrystalChargeMax, 1, false));
                    ret.overTime = true;
                    ret.time = 10000;
                    break;
                case 152111007:
                    statups.add(new Triple<>(BuffStats.HarmonyLink, 1, false));
                    statups.add(new Triple<>(BuffStats.IndieIliumStack, 1, true));
                    ret.overTime = true;
                    ret.time = 15000;
                    break;
                case 152111003: //ũ����Ż ��ų : �۷θ� ��
                    statups.add(new Triple<>(BuffStats.NewFlying, 1, false));
                    statups.add(new Triple<>(BuffStats.IliumeFly, 1, false));
                    statups.add(new Triple<>(BuffStats.IndiePMdR, ret.getStat("indiePMdR"), true));
                    statups.add(new Triple<>(BuffStats.IndieBDR, ret.getStat("indieBDR"), true));
                    statups.add(new Triple<>(BuffStats.IndieStance, 100, true));
                    ret.overTime = true;
                    break;
                case 3310006:
                    statups.add(new Triple<>(BuffStats.RelicGage, 1, false));
                    statups.add(new Triple<>(BuffStats.IndiePMdR, ret.getStat("indiePMdR"), true));
                    ret.overTime = true;
                    break;
                case 400031038:
                case 400031039:
                case 400031040:
                    statups.add(new Triple<>(BuffStats.Barrier, 50000, false));
                    ret.overTime = true;
                    break;
                 
                case 155121043:
                    statups.add(new Triple<>(BuffStats.ChargeSpellAmplification, 1, false));
                    break;
                case 400011005:
                    ret.overTime = true;
                    statups.add(new Triple<>(BuffStats.IndieReduceCooltime, level, true));
                    break;
                case 400011073:
                    ret.overTime = true;
                    statups.add(new Triple<>(BuffStats.ComboInstings, ret.getStat("t"), false));
                    break;
                case 400011072:
                    statups.add(new Triple<>(BuffStats.IndieObsidiunbarrier, 1, true));
                    statups.add(new Triple<>(BuffStats.IndieGrandCross, 1, true));
                    statups.add(new Triple<>(BuffStats.GrandCrossSize, 1, false));
                    statups.add(new Triple<>(BuffStats.GrandCross, 1, false));
                    break;
                case 400031028:
                    statups.add(new Triple<>(BuffStats.QuiverFullCBuster, 4, false));
                    break;
                case 400031030:
                    statups.add(new Triple<>(BuffStats.WindWall, 160, false));
                    break;
                case 400011083:
                    ret.overTime = true;
                    statups.add(new Triple<>(BuffStats.SwordOfSoulLIght, 1, false));
                    statups.add(new Triple<>(BuffStats.IndiePADR, ret.getStat("indiePadR"), true));
                    statups.add(new Triple<>(BuffStats.IndieCr, ret.getStat("indieCr"), true));
                    statups.add(new Triple<>(BuffStats.IndieIgnoreMobpdpR, ret.getStat("indieIgnoreMobpdpR"), true));
                    break;

            }

            if (GameConstants.isBeginnerJob(sourceid / 10000)) {
                switch (sourceid % 10000) {
                    case 8001:
                        break;
                    case 1011:
                        break;
                    case 1010:
                        break;
                    case 8003:
                        break;
                    case 400001004:
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.CombatOrders, 1, false));
                        break;
                    case 8005:
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.AdvancedBless, 1, false));
                        break;
                    case 8006:
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Inflation, ret.getSkillStats().getStats("x"), false));
                        break;
                    case 8002:
                        int valuez = 0;
                        valuez += ret.effects.getStats("y");
                        valuez |= ret.effects.getStats("x") << 8;
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.SharpEyes, valuez, false));
                        break;
                    case 1026:
                    case 1142:
                        ret.time = 600000;
                        break;
                }
            }
        }

        if (ret.isMonsterRiding()) {
            addBuffStatPairToListIfNotZero(statups, BuffStats.MonsterRiding, 1, false);
        }

        if (sourceid != 1105) {
            if (ret.effects.getStats("morph") > 0 || ret.isPirateMorph()) {
                addBuffStatPairToListIfNotZero(statups, BuffStats.Morph, ret.getMorph(), false);
            }
        }

        if (ret.overTime && ret.getSummonMovementType() == null && ret.absstats && sourceid != 32121010) {
            addBuffStatPairToListIfNotZero(statups, BuffStats.PAD, Integer.valueOf(ret.effects.getStats("pad")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.IndiePAD, Integer.valueOf(ret.effects.getStats("indiePad")), true);
            addBuffStatPairToListIfNotZero(statups, BuffStats.PDD, Integer.valueOf(ret.effects.getStats("pdd")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.MAD, Integer.valueOf(ret.effects.getStats("mad")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.IndieMAD, Integer.valueOf(ret.effects.getStats("indieMad")), true);
            addBuffStatPairToListIfNotZero(statups, BuffStats.ACC, Integer.valueOf(ret.effects.getStats("acc")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.EVA, Integer.valueOf(ret.effects.getStats("eva")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.Speed, Integer.valueOf(ret.effects.getStats("speed")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.Jump, Integer.valueOf(ret.effects.getStats("jump")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.EMHP, Integer.valueOf(ret.effects.getStats("emhp")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.EMMP, Integer.valueOf(ret.effects.getStats("emmp")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.EMAD, Integer.valueOf(ret.effects.getStats("emad")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.EPAD, Integer.valueOf(ret.effects.getStats("epad")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.EPDD, Integer.valueOf(ret.effects.getStats("epdd")), false);
            if (sourceid != 21101005 && sourceid != 22131001 && sourceid == 3211010 && sourceid == 3111010 && sourceid == 1100012) { // ���� �ǵ�, �巹���� �нú�ȿ����
                addBuffStatPairToListIfNotZero(statups, BuffStats.MHPCutR, ret.effects.getStats("mhpR"), false);
            }
            addBuffStatPairToListIfNotZero(statups, BuffStats.MMPCutR, ret.effects.getStats("mmpR"), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.ExpBuffRate, Integer.valueOf(ret.effects.getStats("expBuff")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.Inflation, Integer.valueOf(ret.effects.getStats("inflation")), false);
            addBuffStatPairToListIfNotZero(statups, BuffStats.IndieEXP, Integer.valueOf(ret.effects.getStats("indieExp")), true);
        }
        if (ret.isInflation()) {
            addBuffStatPairToListIfNotZero(statups, BuffStats.Inflation, Integer.valueOf(ret.getInflation()), false);
        }
        if (!skill) {
            switch (sourceid) {
                case 2022747:
                    statups.clear();
                    ret.time = 600000;
                    ret.overTime = true;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PAD, 10, true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MAD, 10, true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.RepeatEffect, 1, false));
                    break;
                case 2022746:
                case 2022764:
                    statups.clear();
                    ret.time = 600000;
                    ret.overTime = true;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PAD, 5, true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MAD, 5, true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.RepeatEffect, 1, false));
                    break;
                case 2022823:
                    statups.clear();
                    ret.time = 600000;
                    ret.overTime = true;
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PAD, 12, true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MAD, 12, true));
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.RepeatEffect, 1, false));
                    break;
            }
        }
        ret.monsterStatus = monsterStatus;
        statups.trimToSize();
        ret.statups = statups;

        return ret;
    }

    public final boolean applyTo(MapleCharacter chr) {
        return applyTo(chr, chr, true, null);
    }

    public final boolean applyTo(MapleCharacter chr, Point pos) {
        return applyTo(chr, chr, true, pos);
    }

    public final void applyTo(MapleCharacter chr, Point pos, boolean showEffect) {
        applyTo(chr, chr, showEffect, pos);
    }

    private final boolean applyTo(final MapleCharacter applyfrom, final MapleCharacter applyto, final boolean primary, Point pos) {
        int hpchange = calcHPChange(applyfrom, primary);
        int mpchange = calcMPChange(applyfrom, primary);
        if (sourceid == 142121004) {
            mpchange = 0;
        }
        final PlayerStats stat = applyto.getStat();
        if (primary) {
            if (effects.getStats("itemConNo") != 0) {
                InventoryManipulator.removeById(applyto.getClient(), GameConstants.getInventoryType(effects.getStats("itemCon")), effects.getStats("itemCon"), effects.getStats("itemConNo"), false, true);
            }
        } else if (!primary && isResurrection()) {
            hpchange = stat.getMaxHp();
            applyto.setStance(0);
        }
        if (isDispel() && makeChanceResult()) {
            applyto.dispelDebuffs();
            final Rectangle bounds = calculateBoundingBox(pos != null ? pos : applyfrom.getPosition(), applyfrom.isFacingLeft());
            int i = 0;
            List<MonsterStatus> cancel = new ArrayList<MonsterStatus>();
            cancel.add(MonsterStatus.WEAPON_DEFENSE_UP);
            cancel.add(MonsterStatus.MAGIC_DEFENSE_UP);
            cancel.add(MonsterStatus.WEAPON_ATTACK_UP);
            cancel.add(MonsterStatus.MAGIC_ATTACK_UP);
            for (MapleMapObject hmo : applyfrom.getMap().getMapObjectsInRect(bounds, Collections.singletonList(MapleMapObjectType.MONSTER))) {
                if (makeChanceResult()) {
                    MapleMonster mob = (MapleMonster) hmo;
                    for (MonsterStatus statlulz : cancel) {
                        mob.cancelStatus(statlulz);
                    }
                }
                ++i;
            }
        } else if (isHeroWill()) {
            applyto.dispelDebuff(DiseaseStats.SEDUCE);
        } else if (cureDebuffs.size() > 0) {
            for (final DiseaseStats debuff : cureDebuffs) {
                applyfrom.dispelDebuff(debuff);
            }
        } else if (isComboRecharge()) {
            ISkill comboskill = SkillFactory.getSkill(21111009);
            final int comboskillLvl = applyto.getSkillLevel(comboskill);
            final SkillStatEffect comboEffect = comboskill.getEffect(comboskillLvl);
            final long curr = System.currentTimeMillis();
            int combo = comboEffect.getY();
            final int toDecreaseHP = ((stat.getMaxHp() / 100) * comboEffect.getX());
            if (stat.getHp() > toDecreaseHP) {
                hpchange += -toDecreaseHP;
            } else {
                hpchange = stat.getHp() == 1 ? 0 : stat.getHp() - 1;
            }
            applyto.setLastCombo(curr);
            applyto.setCombo((short) combo);
            applyto.getClient().getSession().writeAndFlush(MainPacketCreator.RechargeCombo(combo));
            SkillFactory.getSkill(21000000).getEffect(applyto.getSkillLevel(21000000)).applyTo(applyto);
        } else if (isMPRecovery()) {
            final int toDecreaseHP = ((stat.getMaxHp() / 100) * 10);
            if (stat.getHp() > toDecreaseHP) {
                hpchange += -toDecreaseHP;
            } else {
                hpchange = stat.getHp() == 1 ? 0 : stat.getHp() - 1;
            }
            mpchange += ((toDecreaseHP / 100) * getY());
        }
        if (applyto.isActiveBuffedValue(400021000)) {
            if (mpchange > 0) {
                mpchange += (int) (applyto.getStat().getCurrentMaxMp() * (SkillFactory.getSkill(400021000).getEffect(applyto.getSkillLevel(400021000)).getX() / 100.0D));
            } else if (hpchange > 0) {
                hpchange += (int) (applyto.getStat().getCurrentMaxHp() * (SkillFactory.getSkill(400021000).getEffect(applyto.getSkillLevel(400021000)).getX() / 100.0D));
            }
        }
        int recoveryUP = 0;
        /*
		for (IItem item : applyto.getInventory(MapleInventoryType.EQUIPPED)) { //����
			Equip equip = (Equip) item;
			if (equip.getState() > 1) {
				int[] potentials = {equip.getPotential1(), equip.getPotential2(), equip.getPotential3(), equip.getPotential4(), equip.getPotential5(), equip.getPotential6()};
				for (Integer id : potentials)
				{
					if (id > 0) {
						ItemInformation ii = ItemInformation.getInstance();
						StructPotentialItem pot = ii.getPotentialInfo(id).get((ii.getReqLevel(equip.getItemId()) / 10) - 1);
						if (pot != null) {
							switch (id) {
							case 30551:
							case 40551:
								recoveryUP += pot.RecoveryUP;
								break;
							}
						}
					}
				}
			}
		}
         */
        if (recoveryUP > 0) {
            hpchange += (int) (hpchange / 100) * recoveryUP;
        }
        final List<Pair<PlayerStat, Long>> hpmpupdate = new ArrayList<Pair<PlayerStat, Long>>(2);
        if (hpchange != 0) {
            if (hpchange < 0 && (-hpchange) > stat.getHp() && !applyto.hasDisease(DiseaseStats.ZOMBIFY)) {
                return false;
            }
            if (hpchange > 0) {
                if (applyfrom.isActiveBuffedValue(400011010)) {
                    hpchange /= 90;
                }
            }
            stat.setHp(stat.getHp() + hpchange, applyfrom);
            if (sourceid == 2321007) {
                applyfrom.getClient().getSession().writeAndFlush(MainPacketCreator.showSkillEffect(-1, applyfrom.getLevel(), sourceid, level, (byte) 0, 1, null, null));
                applyfrom.getMap().broadcastMessage(MainPacketCreator.showSkillEffect(applyfrom.getId(), applyfrom.getLevel(), sourceid, level, (byte) 0, 1, null, null));
            }
        }
        if (mpchange != 0) {
            if (mpchange < 0 && (-mpchange) > stat.getMp()) {
                return false;
            }
            stat.setMp(stat.getMp() + mpchange);
            hpmpupdate.add(new Pair<PlayerStat, Long>(PlayerStat.MP, Long.valueOf(stat.getMp())));
        }
        hpmpupdate.add(new Pair<PlayerStat, Long>(PlayerStat.HP, Long.valueOf(stat.getHp())));
        applyto.getClient().getSession().writeAndFlush(MainPacketCreator.updatePlayerStats(hpmpupdate, true, applyto.getJob(), applyto));
        if (effects.getStats("expinc") != 0) {
            applyto.gainExp(effects.getStats("expinc"), true, true, false);
            applyto.getClient().getSession().writeAndFlush(MainPacketCreator.showSpecialEffect(0x18));
        } else if (isSpiritClaw()) {
            MapleInventory use = applyto.getInventory(MapleInventoryType.USE);
            IItem item;
            boolean itemz = false;
            for (int i = 0; i < use.getSlotLimit(); i++) {
                item = use.getItem((short) i);
                if (item != null) {
                    if ((GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId())) && item.getQuantity() >= 100) {
                        InventoryManipulator.removeFromSlot(applyto.getClient(), MapleInventoryType.USE, (short) i, (short) 100, false, true);
                        itemz = true;
                        break;
                    }
                }
            }
            if (!itemz) {
                return false;
            }
        }

        if (isSoulStone()) {
            final Rectangle bounds = calculateBoundingBox(applyfrom.getPosition(), applyfrom.isFacingLeft());
            final List<MapleMapObject> affecteds = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.PLAYER));
            final List<MapleCharacter> chrs = new LinkedList<MapleCharacter>();
            if (applyfrom.getParty() != null) {
                for (MapleMapObject mo : affecteds) {
                    MapleCharacter hp = (MapleCharacter) mo;
                    if (hp.getPartyId() == applyfrom.getPartyId()) {
                        chrs.add(hp);
                    }
                }
            }
            for (int i = 0; i < getY(); i++) {
                int rand = Randomizer.nextInt(chrs.size() - 1);
                final MapleCharacter affected = chrs.get(rand);
                applyTo(applyfrom, affected, false, null);
                affected.getClient().getSession().writeAndFlush(MainPacketCreator.showSkillEffect(-1, sourceid, level));
                affected.getMap().broadcastMessage(affected, MainPacketCreator.showSkillEffect(affected.getId(), sourceid, level), false);
                chrs.remove(rand);
            }
            return true;
        } else if (sourceid == 142110009) { // ����ű �ǵ�2(�ְ�)
            statups.clear();
            statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.KinesisPsychicShield, effects.getStats("er"), false));
            statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Stance, effects.getStats("stanceProp"), false));
            statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePDD, effects.getStats("indiePdd"), true));
        } else if (sourceid == 101120109) { // �̹� �踮��
            statups.clear();
            //    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ImmuneBarrier, (int) (applyto.getStat().getMaxHp() * (Float.valueOf(effects.getStats("x")) / 100)), false));
            statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Barrier, (int) (applyto.getStat().getMaxHp() * (Float.valueOf(effects.getStats("x")) / 100)), false));
        } else if (sourceid == 80001428) { // ����� ��
            statups.clear();
            statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DotHealHPPerSecond, applyfrom.getStat().getMaxHp() / 10, false));
            statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieEXP, effects.getStats("indieExp"), true));
            statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieAsrR, effects.getStats("indieAsrR"), true));
            statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieTerR, effects.getStats("indieTerR"), true));
        } else if (sourceid == 21001008) { // �ٵ� ������
            if (applyfrom.getBuffedValue(BuffStats.BodyPressure) == null) {
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BodyPressure, effects.getStats("x"), false));
            } else {
                return false;
            }
        } else if (sourceid == 2321001) { // ��� ���
            if (applyfrom.getCooldownLimit(2321008) > 0 && applyfrom.getBuffedValue(BuffStats.KeyDownTimeIgnore, 2321001) == null) {
                SkillStatEffect cooltime = SkillFactory.getSkill(2321008).getEffect(applyfrom.getSkillLevel(2321008));
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.KeyDownTimeIgnore, 1, false));
                time = cooltime.getCooldown() * 1000;
            } else {
                return false;
            }
        } else if (sourceid == 100001263 || sourceid == 100001264) { // ����� ������Ʈ, ����� ���� ��ø ����.
            applyfrom.cancelEffect(sourceid == 100001263 ? SkillFactory.getSkill(100001264).getEffect(applyfrom.getSkillLevel(100001264)) : SkillFactory.getSkill(100001263).getEffect(applyfrom.getSkillLevel(100001263)), false, -1);
        } else if (sourceid == 11101022 || sourceid == 11111022) { // ������, ����¡�� ��ø ����.
            int skillid = sourceid == 11101022 ? 11111022 : 11101022;
            applyfrom.cancelEffect(SkillFactory.getSkill(skillid).getEffect(applyfrom.getSkillLevel(skillid)), false, -1);
        } else if (sourceid == 11121005) { // �ַ糪 Ÿ��
            statups.clear();
            statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.GlimmeringTime, 1, false));
            int stateskill = applyfrom.getBuffedSkillEffect(BuffStats.PoseType, -1).getSourceId();
            SkillStatEffect stateeffect = SkillFactory.getSkill(11121012).getEffect(applyfrom.getSkillLevel(sourceid));
            stateeffect.applyTo(applyto);
        } else if (sourceid == 11121011) { // �ַ糪 Ÿ�� : ���� ��
            statups.clear();
            statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCr, effects.getStats("indieCr"), false));
            statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BuckShot, effects.getStats("x"), false));
        } else if (sourceid == 11121012) {// �ַ糪 Ÿ�� : ����¡ ��
            if (applyto.isActiveBuffedValue(11121012)) {
                return true;
            } else {
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, effects.getStats("indieBooster"), true));
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, effects.getStats("indieDamR"), true));
            }
        } else if (sourceid == 5221021) { // �� ��ο� �ߵ�
            statups.clear();
            statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.QuickDraw, effects.getStats("damR"), false));
        } else if (sourceid == 142121004) {
            this.overTime = true;
            statups.clear();
            if (applyto.acaneAim > effects.getStats("w")) {
                applyto.acaneAim = effects.getStats("w");
            }
            if (applyto.acaneAim > 0) {
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndiePMdR, applyto.acaneAim, true));
            }
        }
        final SummonMovementType summonMovementType = getSummonMovementType();
        if (sourceid == 5201012) {
            int[] skills = {5201012, 5201013, 5201014};
            int skill = skills[Randomizer.nextInt(skills.length)];
            if (skill != 5201012) {
                SkillFactory.getSkill(skill).getEffect(level).applyTo(applyto, pos);
                return false;
            }
        } else if (sourceid == 5210015) {
            int[] skills = {5210015, 5210016, 5210017, 5210018};
            int skill1 = skills[Randomizer.nextInt(skills.length)];
            int skill = skill1;
            for (int i = 0; i < 2; i++) {
                SkillFactory.getSkill(skill).getEffect(level).applyTo(applyto, pos);
                while (skill == skill1) {
                    skill = skills[Randomizer.nextInt(skills.length)];
                }
            }
            return false;
        }
        if (sourceid == 32001014 || sourceid == 32100010 || sourceid == 32110017 || sourceid == 32120019) {
            if (applyfrom.deathCount != getX()) {
                pos = null;
            }
        }
        if (sourceid == 400031001 || sourceid == 12120013 || sourceid == 12120014 || sourceid == 400011065 || sourceid == 152121005 || ((sourceid == 152101000 || sourceid == 152101008) && pos == null)) {
            pos = applyfrom.getPosition();
        }
        if (sourceid >= 33001007 && sourceid <= 33001015) {
            SkillStatEffect eff = SkillFactory.getSkill(33001001).getEffect(applyfrom.getSkillLevel(33001001));
            applyfrom.cancelEffect(eff, false, -1);
        }

        if (summonMovementType != null && pos != null) {
            if (sourceid == 400011001) {
                for (Entry<Integer, Pair<Integer, MapleSummon>> summons : applyfrom.getSummons().entrySet()) {
                    if (summons.getValue().left == 400011002) {
                        summons.getValue().right.removeSummon(applyfrom.getMap());
                        final MapleSummon illusion = new MapleSummon(applyfrom, 400011001, pos, SkillFactory.getSkill(400011001).getEffect(level).getSummonMovementType(), summons.getValue().right.startTime);
                        illusion.setPosition(pos);
                        applyfrom.getMap().spawnSummon(illusion, true, getDuration());
                        applyfrom.getSummons().put(illusion.getObjectId(), new Pair<>(400011001, illusion));
                        return false;
                    } else if (summons.getValue().left == 400011001) {
                        summons.getValue().right.removeSummon(applyfrom.getMap());
                        final MapleSummon illusion = new MapleSummon(applyfrom, 400011002, pos, SkillFactory.getSkill(400011002).getEffect(level).getSummonMovementType(), summons.getValue().right.startTime);
                        illusion.setPosition(pos);
                        applyfrom.getMap().spawnSummon(illusion, true, getDuration());
                        applyfrom.getSummons().put(illusion.getObjectId(), new Pair<>(400011002, illusion));
                        return false;
                    }
                }
            }
            if (sourceid != 400011001 && sourceid != 400011002 && sourceid != 400021047 && sourceid != 14100027 && sourceid != 14110029 && sourceid != 14120008) {
                for (Pair<Integer, MapleSummon> summons : new ArrayList<>(applyfrom.getSummons().values())) {
                    if (summons.getLeft() == sourceid) {
                        summons.getRight().removeSummon(applyfrom.getMap());
                        applyfrom.removeSummon(summons.getRight().getObjectId());
                    }
                    if (sourceid == 5321004) {
                        if (summons.getLeft() == 5320011) {
                            summons.getRight().removeSummon(applyfrom.getMap());
                            applyfrom.removeSummon(summons.getRight().getObjectId());
                        }
                    }
                    if (sourceid == 23111008) {
                        if (summons.getLeft() - sourceid >= 0 && summons.getLeft() - sourceid <= 2) {
                            summons.getRight().removeSummon(applyfrom.getMap());
                            applyfrom.removeSummon(summons.getRight().getObjectId());
                        }
                    }
                }
            }
            if (isStaticSummon()) {
                if (sourceid == 5321004) {
                    pos.x -= 100;
                } else if (sourceid == 400051011) {
                    pos.y -= 100;
                }
            }
            if (sourceid == 400021073) {
                pos = applyfrom.getPosition();
            }
            if (sourceid == 152101008) {
                for (MapleMapObject ob : applyto.getMap().getAllSummon()) {
                    MapleSummon summon = (MapleSummon) ob;
                    if (summon.getOwner() != null && summon.getOwner().getId() == applyto.getId() && summon.getSkill() == 152101000) {
                        pos = summon.getPosition();
                        break;
                    }
                }
            }
            if (sourceid == 61111002 || sourceid == 61111220) {
                int checkCount = 0;
                for (Pair<Integer, MapleSummon> sl : applyfrom.getSummons().values()) {
                    if (sl.getLeft() == 61111002 || sl.getLeft() == 61111220) {
                        checkCount++;
                    }
                }
                if (checkCount == 3) {
                    applyfrom.dropMessage(5, "��Ʈ�����̵�� �ִ� 3������ ��ġ �����մϴ�.");
                    return false;
                }
            }
            if (sourceid == 20051085) {
                ItemInformation ii = ItemInformation.getInstance();
                ii.getItemEffect(2022746).applyTo(applyto);
            } else if (sourceid == 20051087) {
                ItemInformation ii = ItemInformation.getInstance();
                ii.getItemEffect(2022747).applyTo(applyto);
            }
            if (!applyfrom.isFacingLeft()) {
            }
            final MapleSummon tosummon = new MapleSummon(applyfrom, sourceid, pos, summonMovementType, System.currentTimeMillis());
            tosummon.setPosition(pos);

            if (sourceid == 4341006) {
                applyfrom.cancelEffectFromBuffStat(BuffStats.ShadowPartner, 4331002);
                for (Pair<Integer, MapleSummon> s : applyfrom.getSummons().values()) {
                    if (s.getRight().getSkill() == sourceid) {
                        s.getRight().removeSummon(applyfrom.getMap());
                        applyfrom.removeSummon(s.getRight().getObjectId());
                    }
                }
            } else if (sourceid == 5211014) {
                for (Pair<Integer, MapleSummon> s : applyfrom.getSummons().values()) {
                    if (s.getRight().getSkill() == 5211014) {
                        s.getRight().removeSummon(applyfrom.getMap());
                        applyfrom.removeSummon(s.getRight().getObjectId());
                    }
                }
            }

            if (sourceid != 35111002 && sourceid != 400051023) {
                applyfrom.getMap().spawnSummon(tosummon, true, getDuration());
                applyfrom.getSummons().put(tosummon.getObjectId(), new Pair<>(sourceid, tosummon));

                if (sourceid == 400021073) {
                    applyfrom.getMap().broadcastMessage(MainPacketCreator.AssistAttackRequest(applyfrom.getId(),
                            tosummon.getObjectId()));
                }

                if (sourceid == 14121054) {
                    final MapleSummon illusion = new MapleSummon(applyfrom, 14121054, pos, summonMovementType, System.currentTimeMillis());
                    final MapleSummon illusion2 = new MapleSummon(applyfrom, 14121056, pos, summonMovementType, System.currentTimeMillis());
                    illusion.setPosition(pos);
                    illusion2.setPosition(pos);
                    applyfrom.getMap().spawnSummon(illusion, true, getDuration());
                    applyfrom.getSummons().put(illusion.getObjectId(), new Pair<>(14121054, illusion));
                    applyfrom.getMap().spawnSummon(illusion2, true, getDuration());
                    applyfrom.getSummons().put(illusion2.getObjectId(), new Pair<>(14121056, illusion2));

                } else if (sourceid == 400031007) {
                    final MapleSummon illusion = new MapleSummon(applyfrom, 400031008, pos, summonMovementType, System.currentTimeMillis());
                    final MapleSummon illusion2 = new MapleSummon(applyfrom, 400031009, pos, summonMovementType, System.currentTimeMillis());
                    illusion.setPosition(pos);
                    illusion2.setPosition(pos);
                    applyfrom.getMap().spawnSummon(illusion, true, getDuration());
                    applyfrom.getSummons().put(illusion.getObjectId(), new Pair<>(400031008, illusion));
                    applyfrom.getMap().spawnSummon(illusion2, true, getDuration());
                    applyfrom.getSummons().put(illusion2.getObjectId(), new Pair<>(400031009, illusion2));
                }

            }
            tosummon.addHP(effects.getStats("x"));
            if (sourceid == 5321004) {

                try {
                    MapleSummon tosummon1 = new MapleSummon(applyfrom, 5320011, new Point(applyfrom.getTruePosition().x + 100, applyfrom.getTruePosition().y), summonMovementType, System.currentTimeMillis());
                    tosummon1.setPosition(new Point(applyfrom.getTruePosition().x + 100, applyfrom.getTruePosition().y));
                    applyfrom.getMap().spawnSummon(tosummon1, true, getDuration());
                    applyfrom.getSummons().put(tosummon1.getObjectId(), new Pair<>(5320011, tosummon1));
                } catch (Exception e) {
                    System.err.println("[����] ����Ʈ ��Ű Ʈ���� ����");
                    if (!ServerConstants.realese) {
                        e.printStackTrace();
                    }
                }
            }
            if (sourceid == 3111005) { // 
                try {
                    if (applyfrom.getSkillLevel(3120006) > 0) { // ���Ǹ� ��ũ
                        SkillFactory.getSkill(3120006).getEffect(applyfrom.getSkillLevel(3120006)).applyTo(applyfrom, applyfrom.getPosition());
                    }
                } catch (Exception e) {
                    System.err.println("[����] ����Ʈ ��Ű Ʈ���� ����");
                    if (!ServerConstants.realese) {
                        e.printStackTrace();
                    }
                }
            }
            if (sourceid == 3211005) { // ������
                try {
                    if (applyfrom.getSkillLevel(3220005) > 0) { // ���Ǹ� ��ũ
                        SkillFactory.getSkill(3220005).getEffect(applyfrom.getSkillLevel(3220005)).applyTo(applyfrom, applyfrom.getPosition());
                    }
                } catch (Exception e) {
                    System.err.println("[����] ����Ʈ ��Ű Ʈ���� ����");
                    if (!ServerConstants.realese) {
                        e.printStackTrace();
                    }
                }
            }
            if (getSkillStats().getStats("selfDestruction") > 0) {
                applyto.getMines().add(tosummon);
            }

            if (applyfrom.isGM()) {
                applyfrom.Message("SkillID : " + sourceid + " Pos : X : " + pos.x + " Y : " + pos.y + " OID : "
                        + tosummon.getObjectId());
            }

        }

        //���� ��ų ���ӽð� ���� ����(�����Ƽ)
/*
		boolean hasBuffLonger = false;
		for (InnerSkillValueHolder isvh : applyfrom.getInnerSkills()) {
			if (isvh.getSkillId() == 70000048) {
				hasBuffLonger = true;
				break;
			}
		}
		if (hasBuffLonger) {
			int rate = SkillFactory.getSkill(70000048).getEffect(applyfrom.getSkillLevel(70000048)).getSkillStats().getStats("bufftimeR");

		}
         */
        //���� ��ų ���ӽð� ���� ��(�����Ƽ)
        if (overTime && !isEnergyCharge()) {
            applyBuffEffect(applyfrom, applyto, primary, false);
        }
        if (primary) {
            if (overTime || isHeal() && !isEnergyCharge()) {
                applyBuff(applyfrom);
            }
            if (isMonsterBuff()) {
                applyMonsterBuff(applyfrom);
            }
        }
        if (sourceid == 35121003) {
            applyfrom.getClient().getSession().writeAndFlush(MainPacketCreator.resetActions());
        }
        if (isMagicDoor()) {
            MapleMap map = applyto.getMap();
            for (MapleMapObject obj : map.getAllDoor()) {
                MapleDoor door = ((MapleDoor) obj);
                if (door.getOwner().getId() == applyto.getId()) {
                    map.removeMapObject(obj);
                }
            }
            MapleDoor door = new MapleDoor(applyto, new Point(pos == null ? applyto.getTruePosition() : pos));
            if (door.getTownPortal() != null) {
                applyto.getMap().spawnDoor(door);
                applyto.addDoor(door);

                MapleDoor townDoor = new MapleDoor(door);
                door.getTown().spawnDoor(townDoor);
                applyto.addDoor(townDoor);

                if (applyto.getParty() != null) {
                    applyto.silentPartyUpdate();
                }
            }
        } else if (isMechDoor()) { // ���� ����Ʈ
            int newId = 0;
            if (applyto.getMechDoors().size() >= 2) {
                final MapleMechDoor remove = applyto.getMechDoors().remove(0);
                newId = remove.getId();
                applyto.getMap().broadcastMessage(MechanicSkill.mechDoorRemove(remove, true));
                applyto.getMap().removeMapObject(remove);
            } else {
                for (MapleMechDoor p : applyto.getMechDoors()) {
                    if (p.getId() == newId) {
                        newId = 1;
                        break;
                    }
                }
            }
            final MapleMechDoor door = new MapleMechDoor(applyto, new Point(pos == null ? applyto.getTruePosition() : pos), newId);
            applyto.getMap().spawnMechDoor(door);
            applyto.addMechDoor(door);
            return true;
        } else if (isMist()) {
            try {
                boolean spawnMist = true;
                if (sourceid == 12121005 && pos == null) {
                    spawnMist = false;
                }
                if (sourceid == 35121052 && applyto.acaneAim == 1) {
                    spawnMist = false;
                }
                if (spawnMist) {
                    if (sourceid == 35121052) {
                        applyto.acaneAim = 1;
                    }
                    Rectangle bounds = calculateBoundingBox(pos != null ? pos : applyfrom.getPosition(), applyfrom.isFacingLeft());
//                    if (!applyfrom.isFacingLeft()) {
//                        pos.x *= -1;
//                    }
                    int time = getTime();
                    if (sourceid == 400031012) {
                        time = 2500;
                    }
                    if (sourceid == 400011098) {
                        bounds = calculateBoundingBox(new Point(applyto.getTruePosition().x, applyto.getTruePosition().y + 170), false);
                        time = 30000;
                    }
                    if (sourceid == 400021049 || sourceid == 400021050) {
                        applyto.addCooldown(400021041, System.currentTimeMillis(), 30 * 1000);
                    }
                    final MapleMist mist = new MapleMist(bounds, applyfrom, this, effects.getLevel(), pos == null ? applyto.getPosition() : pos);
                    applyfrom.getMap().spawnMist(mist, time, isMistPoison(), false, isRecovery(), isBurningRegion(), isTimeCapsule());
                    if (isTimeCapsule()) {
                        applyfrom.send(MainPacketCreator.TimeCapsule());
                        applyfrom.setChairText(null);
                        applyfrom.setChair(3010587);
                        applyfrom.getMap().broadcastMessage(applyfrom, MainPacketCreator.showChair(applyfrom.getId(), applyfrom.getChair(), applyfrom.getChairText()), false);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (isTimeLeap() || isTimeHolding()) { // Time Leap & Ÿ�� Ȧ��
            for (MapleCoolDownValueHolder i : applyto.getAllCooldowns()) {
                if (i.skillId != sourceid) {
                    applyto.removeCooldown(i.skillId);
                    applyto.getClient().getSession().writeAndFlush(MainPacketCreator.skillCooldown(i.skillId, 0));
                }
            }
        } else if (isMagicCrash()) { // ���� ũ����
            final Rectangle bounds = calculateBoundingBox(pos != null ? pos : applyfrom.getPosition(), applyfrom.isFacingLeft());
            int i = 0;
            List<MonsterStatus> cancel = new ArrayList<MonsterStatus>();
            cancel.add(MonsterStatus.WEAPON_DEFENSE_UP);
            cancel.add(MonsterStatus.MAGIC_DEFENSE_UP);
            cancel.add(MonsterStatus.WEAPON_ATTACK_UP);
            cancel.add(MonsterStatus.MAGIC_ATTACK_UP);
            cancel.add(MonsterStatus.MAGIC_IMMUNITY);
            cancel.add(MonsterStatus.WEAPON_IMMUNITY);
            for (MapleMapObject hmo : applyfrom.getMap().getMapObjectsInRect(bounds, Collections.singletonList(MapleMapObjectType.MONSTER))) {
                if (i >= 10) {
                    break;
                }
                if (makeChanceResult()) {
                    MapleMonster mob = (MapleMonster) hmo;
                    try {
                        MonsterStatusEffect mobeff = new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.MAGIC_CRASH, 1), SkillFactory.getSkill(sourceid), level, null, false);
                        mob.applyStatus(applyfrom, mobeff, getStatusDuration());
                    } catch (Exception e) {
                        if (!ServerConstants.realese) {
                            e.printStackTrace();
                        }
                    }
                    for (MonsterStatus statlulz : cancel) {
                        mob.cancelStatus(statlulz);
                    }
                }
                ++i;
            }
        } else if (isInfinity()) {
            applyto.startInfinityRegen(this);
        } else if (sourceid == 1211010) { // ��������̼�
            int recover = (int) (applyto.getStat().getCurrentMaxHp() * (getX() / 100.0D));
            applyto.addHP(recover);
        } else if (sourceid == 1281) { // ���� �� ������ (���谡)
            if (applyto.getEventInstance() == null) {
                MapleMap map = applyto.getClient().getChannelServer().getMapFactory().getMap(20000);
                applyto.changeMap(map, map.getPortal(0));
            } else {
                applyto.dropMessage(5, "�̰������� ����� �� �����ϴ�.");
            }
        } else if (sourceid == 10001245) { // ����Ȩ (�ñ׳ʽ�)
            if (applyto.getEventInstance() == null) {
                MapleMap map = applyto.getClient().getChannelServer().getMapFactory().getMap(130000000);
                applyto.changeMap(map, map.getPortal(0));
            } else {
                applyto.dropMessage(5, "�̰������� ����� �� �����ϴ�.");
            }
        } else if (sourceid == 20031203) { // ���� ���� ���� (����)
            if (applyto.getEventInstance() == null) {
                MapleMap map = applyto.getClient().getChannelServer().getMapFactory().getMap(150000000);
                applyto.changeMap(map, map.getPortal(0));
            } else {
                applyto.dropMessage(5, "�̰������� ����� �� �����ϴ�.");
            }
        } else if (sourceid == 100001262) { // ��Ʈ���̽� ���� (����)
            if (applyto.getEventInstance() == null) {
                MapleMap map = applyto.getClient().getChannelServer().getMapFactory().getMap(320000000);
                applyto.changeMap(map, map.getPortal(0));
            } else {
                applyto.dropMessage(5, "�̰������� ����� �� �����ϴ�.");
            }
        } else if (sourceid == 2311009) { // Ȧ�� ������
            if (applyto.getKeyValue("HolyMagicShell_lastReceived") == null) {
                applyto.setKeyValue("HolyMagicShell_lastReceived", System.currentTimeMillis() + "");
            }
            applyto.getStat().setHp((int) (applyto.getStat().getCurrentMaxHp() * (getSkillStats().getStats("z")) / 100.0D), applyto);
        } else if (sourceid >= 80001034 && sourceid <= 80001036) {
            applyto.getStat().addSaintSaver(-applyto.getStat().getSaintSaver());
        } else if (isSoulSkill()) {
            Equip weapon = (Equip) (applyto.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11));
            File source = new File("wz/Skill.wz");
            MapleDataProvider sourceData;
            sourceData = MapleDataProviderFactory.getDataProvider(source);
            MapleData dd = sourceData.getData(String.valueOf(sourceid).substring(0, 5) + ".img");
            final int ReqSoul = MapleDataTool.getIntConvert(dd.getChildByPath("skill/" + weapon.getSoulSkill() + "/common/soulmpCon"));
            applyto.setSoulCount((short) (applyto.getSoulCount() - ReqSoul));
            if (applyto.getSoulCount() < ReqSoul) {
                SkillStatEffect eff = applyto.getBuffedSkillEffect(BuffStats.FullSoulMP);
                if (eff != null) {
                    applyto.cancelEffect(eff, false, applyto.getBuffedStarttime(BuffStats.FullSoulMP, eff.getSourceId()));
                }
            }
        }
        return true;
    }

    public final boolean applyReturnScroll(final MapleCharacter applyto) {
        if (effects.getStats("moveTo") != -1) {
            if (applyto.getMap().getReturnMapId() != applyto.getMapId()) {
                MapleMap target;
                if (effects.getStats("moveTo") == 999999999) {
                    target = applyto.getMap().getReturnMap();
                } else {
                    target = ChannelServer.getInstance(applyto.getClient().getChannel()).getMapFactory().getMap(effects.getStats("moveTo"));
                    if (target.getId() / 10000000 != 60 && applyto.getMapId() / 10000000 != 61) {
                        if (target.getId() / 10000000 != 21 && applyto.getMapId() / 10000000 != 20) {
                            if (target.getId() / 10000000 != applyto.getMapId() / 10000000) {
                                return false;
                            }
                        }
                    }
                }
                applyto.changeMap(target, target.getPortal(0));
                return true;
            }
        }
        return false;
    }

    public final void applyBuffEffect(final MapleCharacter chr) {
        this.applyBuffEffect(chr, chr, false, false);
    }

    private boolean isContainsRiding() {
        for (Triple<BuffStats, Integer, Boolean> t : statups) {
            if (t.first == BuffStats.MonsterRiding) {
                return true;
            }
        }
        return false;
    }

    private final void applyBuff(final MapleCharacter applyfrom) {
        if (isPartyBuff() && (applyfrom.getParty() != null || isGmBuff()) && !isContainsRiding()) {
            final Rectangle bounds = calculateBoundingBox(applyfrom.getPosition(), applyfrom.isFacingLeft());
            final List<MapleMapObject> affecteds = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.PLAYER));
            for (final MapleMapObject affectedmo : affecteds) {
                final MapleCharacter affected = (MapleCharacter) affectedmo;
                if (affected != applyfrom && (isGmBuff() || applyfrom.getParty().equals(affected.getParty()))) {
                    if ((isResurrection() && !affected.isAlive()) || (!isResurrection() && affected.isAlive())) {
                        if (sourceid == 2311009) { // Ȧ�� ������ 30�� ��ȣ�� ���� �Ұ�
                            if (affected.getKeyValue("HolyMagicShell_lastReceived") != null) {
                                long lasttime = Long.parseLong(affected.getKeyValue("HolyMagicShell_lastReceived"));
                                if (lasttime + (getY() * 1000) > System.currentTimeMillis()) {
                                    continue;
                                } else {
                                    affected.setKeyValue("HolyMagicShell_lastReceived", null);
                                }
                            }
                            affected.setKeyValue2("HolyMagicShellLifeCount", getX());
                        }
                        affected.getClient().getSession().writeAndFlush(MainPacketCreator.showSkillEffect(-1, sourceid, level));
                        affected.getMap().broadcastMessage(affected, MainPacketCreator.showSkillEffect(affected.getId(), sourceid, level), false);
                    }
                    if (isTimeLeap()) {
                        for (MapleCoolDownValueHolder i : affected.getAllCooldowns()) {
                            if (i.skillId != 5121010) {
                                affected.removeCooldown(i.skillId);
                                affected.getClient().getSession().writeAndFlush(MainPacketCreator.skillCooldown(i.skillId, 0));
                            }
                        }
                    }
                }
            }
        }
    }

    public final void applyMonsterBuff(final MapleCharacter applyfrom) {
        final Rectangle bounds = calculateBoundingBox(applyfrom.getPosition(), applyfrom.isFacingLeft());
        final List<MapleMapObject> affected = sourceid == 35111005 ? applyfrom.getMap().getMapObjectsInRange(applyfrom.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER)) : applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.MONSTER));
        int i = 0;
        for (final MapleMapObject mo : affected) {
            if (makeChanceResult()) {
                MapleMonster mons = (MapleMonster) mo;
                if (sourceid == 35111005 && mons.getStats().isBoss()) {
                    break;
                }
                ((MapleMonster) mo).applyStatus(applyfrom, new MonsterStatusEffect(getMonsterStati(), SkillFactory.getSkill(sourceid), applyfrom.getSkillLevel(sourceid), null, false), getStatusDuration());
            }
            i++;
            if (i >= effects.getStats("mobCount") && sourceid != 35111005) {
                break;
            }
        }
    }

    public final void cerpentScrew(final MapleCharacter chr) {
        final Rectangle bounds = calculateBoundingBox(chr.getPosition(), chr.isFacingLeft());

        for (final MapleMapObject object : chr.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.MONSTER))) {
            long damage = getDamage(chr.getStat().getMaxAttack());
            MapleMonster mob = (MapleMonster) object;
            mob.damage(chr, damage, true);
            chr.getMap().broadcastMessage(MobPacket.damageMonster(mob.getObjectId(), damage));
            if (chr.getAddDamage() > 0) {
                long dam = chr.getAddDamage() * (chr.getDamageHit() + chr.getDamageHit2()) / 100;
                chr.send(UIPacket.detailShowInfo("���� " + chr.getName() + "���� < " + chr.getAddDamage() + " * " + (chr.getDamageHit() + chr.getDamageHit2()) + " %> �ѵ����� < " + dam + " > ", true));
                mob.damage(chr, dam, true);
            }
        }
    }

    public final void applyAtom(final MapleCharacter applyfrom, final int type) {
        final Rectangle bounds = calculateBoundingBox(applyfrom.getPosition(), applyfrom.isFacingLeft());
        int i = 0;
        for (final MapleMapObject object : applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.MONSTER))) {
            switch (type) {
                case 10:
                    applyfrom.getMap().broadcastMessage(MainPacketCreator.giveMagicArrow(applyfrom, (MapleMonster) object), applyfrom.getPosition());
                    break;
                case 7:
                    int count = 0;
                    if (sourceid == 13120003) {
                        count = Randomizer.rand(1, 5);
                    } else if (sourceid == 13110022) {
                        count = Randomizer.rand(1, 4);
                    } else if (sourceid == 13100022) {
                        count = Randomizer.rand(1, 3);
                    }
                    applyfrom.getMap().broadcastMessage(MainPacketCreator.TrifleWorm(applyfrom.getId(), sourceid, count, object.getObjectId(), 1));
                    break;
                case 3:
                    applyfrom.getMap().broadcastMessage(AngelicBusterSkill.SoulSeeker(applyfrom, 65120011, 1, object.getObjectId(), 0));
                    break;
                case 22:
                    applyfrom.getMap().broadcastMessage(MainPacketCreator.KinesisAtom(applyfrom.getId(), object.getObjectId(), i, sourceid, level));
                    break;
                case 6:
                    applyfrom.getMap().broadcastMessage(MainPacketCreator.PinPointRocket(applyfrom.getId(), object.getObjectId()));
                    break;
                case 33:
                    int cardid = applyfrom.addCardStackRunningId();
                    applyfrom.getMap().broadcastMessage(applyfrom, MainPacketCreator.absorbingCardStack(applyfrom.getId(), cardid, 400041010, false, 5), true);
                    break;
            }
            i++;
            if (i >= effects.getStats("mobCount")) {
                break;
            }
        }
    }

    public final void applyMarkOf(final MapleCharacter applyfrom, final MapleMonster mob, final int kunai, final int skillid) {
        final Rectangle bounds = calculateBoundingBox(mob.getPosition(), mob.isFacingLeft());
        int i = 0;
        List<Integer> objectId = new ArrayList<Integer>();
        int mobCount = effects.getStats("bulletCount");
        for (final MapleMapObject object : applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.MONSTER))) {
            objectId.add(object.getObjectId());
            i++;
            if (i >= mobCount) {
                break;
            }
        }
        if (objectId.size() == 0) {
            objectId.add(mob.getObjectId());
        }
        applyfrom.getMap().broadcastMessage(MainPacketCreator.markof(applyfrom, mob, objectId, kunai, skillid));
    }

    public final void applyToStrikerStack(final MapleCharacter applyto, final int stack) {
        List<Triple<BuffStats, Integer, Boolean>> statups = new ArrayList<>();
        statups.add(new Triple<>(BuffStats.IgnoreTargetDEF, this.getX() * stack, false));
        int localDuration = time;
        if (localDuration < 0) {
            localDuration = Integer.MAX_VALUE;
        }
        if (SkillFactory.getSkill(sourceid).notCancel()) {
            localDuration = Integer.MAX_VALUE;
        }
        if (sourceid == 2201009) {
            localDuration = Integer.MAX_VALUE;
        }

        applyto.cancelEffect(this, true, -1);

        if (statups.size() > 0) {
            long overlap_magic = (long) (System.currentTimeMillis() % 1000000000);
            Map<BuffStats, List<StackedSkillEntry>> stacked = applyto.getStackSkills();
            for (Triple<BuffStats, Integer, Boolean> statup : statups) {
                if (statup.getThird()) {
                    if (!stacked.containsKey(statup.getFirst())) {
                        stacked.put(statup.getFirst(), new ArrayList<StackedSkillEntry>());
                    }
                    stacked.get(statup.getFirst()).add(new StackedSkillEntry(skill ? getSourceId() : -getSourceId(), statup.getSecond(), overlap_magic, localDuration));
                }
            }
            applyto.getClient().getSession().writeAndFlush(MainPacketCreator.giveBuff((skill ? sourceid : -sourceid), localDuration, statups, this, stacked, SkillFactory.getSkill(sourceid).getAnimationTime(), applyto));
        }
        final long starttime = System.currentTimeMillis();
        final CancelEffectAction cancelAction = new CancelEffectAction(applyto, this, starttime);
        ScheduledFuture<?> schedule = null;
        if (localDuration != Integer.MAX_VALUE) {
            schedule = tools.Timer.BuffTimer.getInstance().schedule(cancelAction, ((starttime + localDuration) - System.currentTimeMillis()));
        }
        applyto.registerEffect(this, starttime, schedule);
        applyto.refreshMaxHpMp();
        applyto.getMap().broadcastMessage(applyto, MainPacketCreator.giveForeignBuff(applyto, statups), false);
    }

    public final void applyToDamageReversing(final MapleCharacter applyto, final long damage) {
        statups.clear();
        statups.add(new Triple<>(BuffStats.PowerTransferGauge, (int) (damage / effects.getStats("y")), false));
        int localDuration = time;
        if (localDuration < 0) {
            localDuration = Integer.MAX_VALUE;
        }
        if (SkillFactory.getSkill(sourceid).notCancel()) {
            localDuration = Integer.MAX_VALUE;
        }
        if (sourceid == 2201009) {
            localDuration = Integer.MAX_VALUE;
        }
        applyto.cancelEffect(this, true, -1);
        if (statups.size() > 0) {
            long overlap_magic = (long) (System.currentTimeMillis() % 1000000000);
            Map<BuffStats, List<StackedSkillEntry>> stacked = applyto.getStackSkills();
            for (Triple<BuffStats, Integer, Boolean> statup : statups) {
                if (statup.getThird()) {
                    if (!stacked.containsKey(statup.getFirst())) {
                        stacked.put(statup.getFirst(), new ArrayList<StackedSkillEntry>());
                    }
                    stacked.get(statup.getFirst()).add(new StackedSkillEntry(skill ? getSourceId() : -getSourceId(), statup.getSecond(), overlap_magic, localDuration));
                }
            }
            applyto.getClient().getSession().writeAndFlush(MainPacketCreator.giveBuff((skill ? sourceid : -sourceid), localDuration, statups, this, stacked, SkillFactory.getSkill(sourceid).getAnimationTime(), applyto));
        }
        final long starttime = System.currentTimeMillis();
        final CancelEffectAction cancelAction = new CancelEffectAction(applyto, this, starttime);
        ScheduledFuture<?> schedule = null;
        if (localDuration != Integer.MAX_VALUE) {
            schedule = tools.Timer.BuffTimer.getInstance().schedule(cancelAction, ((starttime + localDuration) - System.currentTimeMillis()));
        }
        applyto.registerEffect(this, starttime, schedule);
        applyto.refreshMaxHpMp();
        applyto.getMap().broadcastMessage(applyto, MainPacketCreator.giveForeignBuff(applyto, statups), false);
    }

    public final Rectangle calculateBoundingBox(final Point posFrom, final boolean facingLeft) {
        return calculateBoundingBox(posFrom, facingLeft, lt, rb, effects.getStats("range"));
    }

    public static Rectangle calculateBoundingBox(final Point posFrom, final boolean facingLeft, final Point lt, final Point rb, final int range) {
        if (lt == null || rb == null) {
            return new Rectangle((facingLeft ? (-200 - range) : 0) + posFrom.x, (-100 - range) + posFrom.y, 200 + range, 100 + range);
        }
        Point mylt;
        Point myrb;
        if (facingLeft) {
            mylt = new Point(lt.x + posFrom.x - range, lt.y + posFrom.y);
            myrb = new Point(rb.x + posFrom.x, rb.y + posFrom.y);
        } else {
            myrb = new Point(lt.x * -1 + posFrom.x + range, rb.y + posFrom.y);
            mylt = new Point(rb.x * -1 + posFrom.x, lt.y + posFrom.y);
        }
        return new Rectangle(mylt.x, mylt.y, myrb.x - mylt.x, myrb.y - mylt.y);
    }

    public final void silentApplyBuff(final MapleCharacter chr, final long starttime) {
        int localDuration = time;
        if (localDuration < 0) {
            localDuration = Integer.MAX_VALUE;
        }
        if (SkillFactory.getSkill(sourceid).notCancel()) {
            localDuration = Integer.MAX_VALUE;
        }
        if (sourceid == 2201009) {
            localDuration = Integer.MAX_VALUE;
        }
        if (((starttime + localDuration) - System.currentTimeMillis()) > 0) {
            int overlap_magic = (int) (System.currentTimeMillis() % 1000000000);
            Map<BuffStats, List<StackedSkillEntry>> stacked = chr.getStackSkills();
            for (Triple<BuffStats, Integer, Boolean> statup : statups) {
                if (statup.getThird()) {
                    if (!stacked.containsKey(statup.getFirst())) {
                        stacked.put(statup.getFirst(), new ArrayList<StackedSkillEntry>());
                    }
                    stacked.get(statup.getFirst()).add(new StackedSkillEntry(skill ? getSourceId() : -getSourceId(), statup.getSecond(), overlap_magic, (int) (((starttime + localDuration) - System.currentTimeMillis()) / 1000)));
                }
            }
            chr.cancelEffect(this, true, -1);
            final CancelEffectAction cancelAction = new CancelEffectAction(chr, this, starttime);
            ScheduledFuture<?> schedule = null;
            if (localDuration != Integer.MAX_VALUE) {
                schedule = tools.Timer.BuffTimer.getInstance().schedule(cancelAction, ((starttime + localDuration) - System.currentTimeMillis()));
            }
            chr.registerEffect(this, starttime, schedule);
            final SummonMovementType summonMovementType = getSummonMovementType();
            if (summonMovementType != null) {
                final MapleSummon tosummon = new MapleSummon(chr, sourceid, chr.getPosition(), summonMovementType, starttime);
                if (!tosummon.isStaticSummon()) {
                    chr.getMap().spawnSummon(tosummon, true, getDuration());
                    chr.getSummons().put(tosummon.getObjectId(), new Pair<>(sourceid, tosummon));
                    tosummon.addHP(getX());
                }
            }
        }
    }

    public void applySunfireBuff(final MapleCharacter applyto, boolean used, int attackSkill) {
        final int localDuration = 21000000;
        final List<Triple<BuffStats, Integer, Boolean>> stat = Collections.singletonList(new Triple<BuffStats, Integer, Boolean>(BuffStats.Larkness, 1, false));
        final long startTime = System.currentTimeMillis();
        if (used && (applyto.getBuffedValue(BuffStats.Larkness) != null && applyto.getBuffedValue(BuffStats.Larkness) == 20040216)) {
            if (GameConstants.isLightSkills(attackSkill)) {
                applyto.send(MainPacketCreator.checkSunfireSkill(applyto.addMinusOfGlassCTS_Morph(GameConstants.isLightSkillsGaugeCheck(attackSkill))));
            }
        } else if (GameConstants.isDarkSkills(attackSkill)) {
            applyto.send(MainPacketCreator.giveSunfireBuff(stat, 10000, effects.getStats("time")));
            applyto.send(MainPacketCreator.checkSunfireSkill(9999));
            applyto.cancelEffect(this, true, -1);
            applyto.registerEffect(this, startTime, null);
            applyto.setBuffedValue(BuffStats.Larkness, -1, 20040216);
        }
    }

    public void applyEclipseBuff(final MapleCharacter applyto, boolean used, int attackSkill) {
        final int localDuration = 21000000;
        final List<Triple<BuffStats, Integer, Boolean>> stat = Collections.singletonList(new Triple<BuffStats, Integer, Boolean>(BuffStats.Larkness, 100, false));
        final long startTime = System.currentTimeMillis();

        if (used && (applyto.getBuffedValue(BuffStats.Larkness) != null && applyto.getBuffedValue(BuffStats.Larkness) == 20040217)) {
            if (GameConstants.isDarkSkills(attackSkill)) {
                applyto.send(MainPacketCreator.checkEclipseSkill(applyto.addPlusOfGlassCTS_Morph(GameConstants.isDarkSkillsGaugeCheck(attackSkill))));
            }
        } else if (GameConstants.isLightSkills(attackSkill)) {
            applyto.send(MainPacketCreator.giveEclipseBuff(stat, -1, effects.getStats("time")));
            applyto.send(MainPacketCreator.checkEclipseSkill(1));
            applyto.cancelEffect(this, true, -1);
            applyto.registerEffect(this, startTime, null);
            applyto.setBuffedValue(BuffStats.Larkness, -1, 20040217);
        }
    }

    public void applyequilibriumBuff(final MapleCharacter applyto, boolean sunfire) {
        for (MapleCoolDownValueHolder m : applyto.getAllCooldowns()) {
            final int skil = m.skillId;
            if (skil == 27111303 || skil == 27121303) {
                applyto.removeCooldown(skil);
                applyto.getClient().send(MainPacketCreator.skillCooldown(skil, 0));
            }
        }

        if (sunfire) {
            applyto.send(MainPacketCreator.giveEquilibriumBuff(20040220, 20040216, 20040217));
            applyto.send(MainPacketCreator.checkSunfireSkill(1));
            applyto.setBuffedValue(BuffStats.Larkness, -1, 20040219);
        } else {
            applyto.send(MainPacketCreator.giveEquilibriumBuff(20040220, 20040216, 20040217));
            applyto.send(MainPacketCreator.checkEclipseSkill(1));
            applyto.setBuffedValue(BuffStats.Larkness, -1, 20040220);
        }

        tools.Timer.EventTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (applyto.getBuffedValue(BuffStats.Larkness) == null || applyto.getBuffedValue(BuffStats.Larkness) == 20040220) {
                    applyto.send(MainPacketCreator.cancelEquilbriam());
                    applyto.setBuffedValue(BuffStats.Larkness, -1, 20040216);
                    applyto.glass_plusCTS_Morph = 1;
                    applySunfireBuff(applyto, false, 27001201);
                } else if (applyto.getBuffedValue(BuffStats.Larkness) == 20040219) {
                    applyto.send(MainPacketCreator.cancelEquilbriam());
                    applyto.setBuffedValue(BuffStats.Larkness, -1, 20040217);
                    applyto.glass_minusCTS_Morph = 9999;
                    applyEclipseBuff(applyto, false, 27001100);
                }
            }
        }, 10000);
    }

    public final void applyBuffEffectz(final MapleCharacter applyfrom, final MapleCharacter applyto, final boolean primary, final boolean lightCharge) {
        this.applyBuffEffect(applyfrom, applyto, primary, lightCharge);
    }

    public final void applyBuffEffect(final MapleCharacter applyfrom, final MapleCharacter applyto, final boolean primary, final boolean lightCharge) {
        if (sourceid == 5311005) {
            final int DoubleDice = applyto.getSkillLevel(SkillFactory.getSkill(5320007));
            if (DoubleDice > 0) {
                setSourceId(5320007);
            }
        } else if (sourceid == 5301003) {
            final int HyperMonkey = applyto.getSkillLevel(SkillFactory.getSkill(5320008));
            if (HyperMonkey > 0) {
                setSourceId(5320008);
            } else {
                setSourceId(5301003);
            }
        }
        int localDuration = time;
        if (sourceid == 21110016) {
            localDuration = 15000;
        }
        boolean normal = true;
        switch (sourceid) {
            case 5111007:
            case 5120012:
            case 5211007:
            case 5220014:
            case 35111013:
            case 35120014:
            case 15111011:
            case 5311005:
            case 5320007:
            case 400051001: // ��Ű ���̽�(5��)
            {
                int diceid = 0;
                int doublediceid = 0;
                int loadeddiceid = 0;
                int rand1 = 0, rand2 = 0, backupDice = -1;
                if (applyto.loadedDice != -1) {
                    if (isDoubleDice(sourceid) && makeChanceResult()) {
                        rand1 = Randomizer.rand(1, 6);
                        rand2 = Randomizer.rand(1, 6);
                    } else {
                        rand1 = Randomizer.rand(1, 6);
                        rand2 = 1;
                    }
                    loadeddiceid = applyto.loadedDice * 100 + rand1 * 10 + rand2;
                    backupDice = applyto.loadedDice;
                    applyto.loadedDice = -1;
                } else if (isDoubleDice(sourceid) && makeChanceResult()) {
                    rand1 = Randomizer.rand(1, 6);
                    rand2 = Randomizer.rand(1, 6);
                    doublediceid = rand1 * 10 + rand2;
                } else {
                    diceid = Randomizer.rand(1, 6);
                }

                if (loadeddiceid > 0) {
                    applyto.getMap().broadcastMessage(applyto, MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, backupDice, 5, true, 0), false);
                    applyto.getMap().broadcastMessage(applyto, MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand1, 5, true, 1), false);
                    applyto.getMap().broadcastMessage(applyto, MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand2, 5, true, 2), false);
                    applyto.getClient().getSession().writeAndFlush(MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, backupDice, 5, false, 0));
                    applyto.getClient().getSession().writeAndFlush(MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand1, 5, false, 1));
                    applyto.getClient().getSession().writeAndFlush(MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand2, 5, false, 2));
                    if (rand1 == 1 && rand2 == 1) {
                        applyto.dropMessage(5, "���� ��Ű ���̽� ��ų�� �����Ͽ����ϴ�.");
                        return;
                    }
                    if (backupDice == 1) {
                        applyto.dropMessage(5, "0 ���� ��Ű ���̽� ��ų�� �����Ͽ����ϴ�.");
                    } else {
                        applyto.dropMessage(5, "0 ���� ��Ű ���̽� ��ų�� [" + backupDice + "]�� ȿ���� �ߵ� ���׽��ϴ�.");
                    }
                    if (rand1 == 1) {
                        applyto.dropMessage(5, "1 ���� ��Ű ���̽� ��ų�� �����Ͽ����ϴ�.");
                    } else {
                        applyto.dropMessage(5, "1 ���� ��Ű ���̽� ��ų�� [" + rand1 + "]�� ȿ���� �ߵ� ���׽��ϴ�.");
                    }
                    if (rand2 == 1) {
                        applyto.dropMessage(5, "2 ���� ��Ű ���̽� ��ų�� �����Ͽ����ϴ�.");
                    } else {
                        applyto.dropMessage(5, "2 ���� ��Ű ���̽� ��ų�� [" + rand2 + "]�� ȿ���� �ߵ� ���׽��ϴ�.");
                    }
                    final List<Triple<BuffStats, Integer, Boolean>> stat = Collections.singletonList(new Triple<BuffStats, Integer, Boolean>(BuffStats.Dice, loadeddiceid, false));
                    applyto.getClient().getSession().writeAndFlush(MainPacketCreator.giveDoubleDice(loadeddiceid, sourceid, localDuration, stat));
                } else if (doublediceid > 0) {
                    applyto.getMap().broadcastMessage(applyto, MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand1, 5, true, 0), false);
                    applyto.getMap().broadcastMessage(applyto, MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand2, 5, true, 1), false);
                    applyto.getClient().getSession().writeAndFlush(MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand1, 5, false, 0));
                    applyto.getClient().getSession().writeAndFlush(MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand2, 5, false, 1));
                    if (rand1 == 1 && rand2 == 1) {
                        applyto.dropMessage(5, "���� ��Ű ���̽� ��ų�� [" + rand2 + "], [" + rand1 + "] �� ���� �ƹ��� ȿ���� ���� ���߽��ϴ�.");
                        return;
                    } else if (rand1 == 1) {
                        applyto.dropMessage(5, "���� ��Ű ���̽� ��ų�� [" + rand2 + "] �� ȿ���� �ߵ� ���׽��ϴ�.");
                    } else if (rand2 == 1) {
                        applyto.dropMessage(5, "���� ��Ű ���̽� ��ų�� [" + rand1 + "] �� ȿ���� �ߵ� ���׽��ϴ�.");
                    } else {
                        applyto.dropMessage(5, "���� ��Ű ���̽� ��ų�� [" + rand2 + "], [" + rand1 + "] �� ȿ���� �ߵ� ���׽��ϴ�.");
                    }
                    final List<Triple<BuffStats, Integer, Boolean>> stat = Collections.singletonList(new Triple<BuffStats, Integer, Boolean>(BuffStats.Dice, doublediceid, false));
                    applyto.getClient().getSession().writeAndFlush(MainPacketCreator.giveDoubleDice(doublediceid, sourceid, localDuration, stat));
                } else {
                    int tempsource = sourceid == 5320007 ? 5311005 : sourceid == 5220014 ? 5211007 : sourceid == 5120012 ? 5111007 : sourceid;
                    applyto.getMap().broadcastMessage(applyto, MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, diceid, 5, true, 0), false);
                    applyto.getClient().getSession().writeAndFlush(UIPacket.showWZEffect("Skill/1511.img/skill/15111011/affected/" + diceid));
                    if (diceid <= 1) {
                        applyto.dropMessage(5, "��Ű ���̽� ��ų�� [1] �� ���� �ƹ��� ȿ���� ���� ���߽��ϴ�.");
                        return;
                    }
                    final List<Triple<BuffStats, Integer, Boolean>> stat = Collections.singletonList(new Triple<BuffStats, Integer, Boolean>(BuffStats.Dice, Integer.valueOf(diceid), false));
                    applyto.getClient().getSession().writeAndFlush(MainPacketCreator.giveDice(diceid, tempsource, localDuration, stat));
                    applyto.dropMessage(5, "��Ű ���̽� ��ų�� [" + diceid + "] �� ȿ���� �ߵ� ���׽��ϴ�.");
                }
                normal = false;
                break;
            }
            case 5311004: {
                int Oakid = Randomizer.rand(1, 4);
                applyto.getMap().broadcastMessage(applyto, MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, Oakid, 0, true, 0), false);
                applyto.getClient().getSession().writeAndFlush(MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, Oakid, 0, false, 0));
                final List<Triple<BuffStats, Integer, Boolean>> stat = Collections.singletonList(new Triple<BuffStats, Integer, Boolean>(BuffStats.Roulette, Oakid, false));
                this.statups = stat;
                int overlap_magic = (int) (System.currentTimeMillis() % 1000000000);
                Map<BuffStats, List<StackedSkillEntry>> stacked = applyto.getStackSkills();
                for (Triple<BuffStats, Integer, Boolean> statup : stat) {
                    if (statup.getThird()) {
                        if (!stacked.containsKey(statup.getFirst())) {
                            stacked.put(statup.getFirst(), new ArrayList<StackedSkillEntry>());
                        }
                        stacked.get(statup.getFirst()).add(new StackedSkillEntry(getSourceId(), statup.getSecond(), overlap_magic, localDuration));
                    }
                }
                applyto.getClient().getSession().writeAndFlush(MainPacketCreator.giveBuff(sourceid, localDuration, stat, this, stacked, SkillFactory.getSkill(sourceid).getAnimationTime(), applyto));
                normal = false;
                break;
            }
            case 3101009: { // ���� īƮ����
                if (applyto.quiver && applyto.quivermode < 3) {
                    applyto.quivermode += 1;
                } else {
                    if (!applyto.quiver) {
                        applyto.quivercount[0] += applyto.getSkillLevel(3121016);
                        applyto.quivercount[1] += applyto.getSkillLevel(3121016);
                        applyto.quivercount[2] += applyto.getSkillLevel(3121016) * 3;
                    }
                    applyto.quiver = true;
                    applyto.quivermode = 1;
                }
                applyto.getClient().getSession().writeAndFlush(UIPacket.showWZEffect("Skill/310.img/skill/3101009/mode/" + (applyto.quivermode - 1), 1));
                applyto.getClient().getSession().writeAndFlush(UIPacket.showWZEffect("Skill/310.img/skill/3101009/modeStatus/" + (applyto.quivermode - 1) + "/" + (applyto.quivercount[applyto.quivermode - 1] * 1), 1));
                applyto.getMap().broadcastMessage(applyto, UIPacket.broadcastWZEffect(applyto.getId(), "Skill/310.img/skill/3101009/mode/" + (applyto.quivermode - 1), 1), applyto.getPosition());
                applyto.getMap().broadcastMessage(applyto, UIPacket.broadcastWZEffect(applyto.getId(), "Skill/310.img/skill/3101009/modeStatus/" + (applyto.quivercount[applyto.quivermode - 1] * 1), 1), applyto.getPosition());
                statups.clear();
                statups.add(new Triple<>(BuffStats.QuiverCatridge, (applyto.quivercount[0] * 10000) + (applyto.quivercount[1] * 100) + (applyto.quivercount[2] * 1), false));
                break;
            }
            case 60001216: // �����ý���ġ : �����
            case 60001217: { // �����ý���ġ : ���� ���
                if (applyto.getBuffedValue(BuffStats.ReshuffleSwitch) != null) {
                    if (applyto.getSkillLevel(60001216) > 0) {
                        applyto.cancelEffectFromBuffStat(BuffStats.ReshuffleSwitch, 60001217);
                    }
                    if (applyto.getSkillLevel(60001217) > 0) {
                        applyto.cancelEffectFromBuffStat(BuffStats.ReshuffleSwitch, 60001216);
                    }
                }
                break;
            }
            case 61101002:
            case 61110211:
            case 61120007:
            case 61121217: {
                if (applyto.getCooldownLimit(61101002) == 0) {
                    SkillStatEffect effect = SkillFactory.getSkill(61101002).getEffect(applyfrom.getSkillLevel(61101002));
                    applyto.send(MainPacketCreator.skillCooldown(61101002, effect.getCooldown()));
                    applyto.addCooldown(61101002, System.currentTimeMillis(), effect.getCooldown() * 1000);
                }
                break;
            }
            case 61111008: // ���̳� �ǱԷ��̼� (3��)
            case 61120008:
            case 61121053: { // ���̳� �ǱԷ��̼� (4��)
                if (applyto.getJob() == 6112 && sourceid == 61111008) {
                    if (applyto.getSkillLevel(61120007) < 0) {
                        applyto.changeSkillLevel(61120007, (byte) 30, (byte) 30);
                    }
                    SkillFactory.getSkill(61120008).getEffect(applyto.getSkillLevel(61111008)).applyTo(applyto);
                    return;
                }
                applyto.isFinalFiguration = true;
                applyto.changeKaiserTransformKey();
                applyto.getStat().setMorph(0);
                applyto.getClient().send(KaiserSkill.giveMorphGauge(applyto.getStat().addMorph(0)));
                if (applyto.getBuffedValue(BuffStats.StopForceAtomInfo) != null) {
                    applyto.cancelEffectFromBuffStat(BuffStats.StopForceAtomInfo, -1);
                    if (sourceid == 61120008 || sourceid == 61121053) {
                        SkillFactory.getSkill(61121217).getEffect(applyto.getSkillLevel(61120007)).applyTo(applyto);
                    } else {
                        SkillFactory.getSkill(61110211).getEffect(applyto.getSkillLevel(61101002)).applyTo(applyto);
                    }
                }
                break;
            }
            case 400051044: {
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.stack, applyto.aa, false));
                applyto.send(SecondaryStat.stack(statups, this, applyto));
                break;
            }
            case 400021071: {
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.LightOrDark, applyto.bb, false));
                applyto.send(SecondaryStat.stack2(statups, this, applyto));
                this.time = Integer.MAX_VALUE;
                break;
            }
            case 2320011:
            case 2220010:
            case 2120010:
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ArcaneAim, getSkillStats().getStats("x") * applyto.acaneAim, false));
                break;
            case 64120007:
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.CriticalBuff, 2 * applyto.KADENA_WEAPON_STACK, false));
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EnrageCrDamMin, 5 * applyto.KADENA_WEAPON_STACK, false));
                break;
            case 1211006: // ����Ʈ�� ���� �̿�
            case 1211004:
            case 1221004:
                List<Triple<BuffStats, Integer, Boolean>> statt = Collections.singletonList(new Triple<BuffStats, Integer, Boolean>(BuffStats.WeaponCharge, 1, false));
                if (applyto.getBuffedValue(BuffStats.WeaponCharge) != null && applyto.getBuffedValue(BuffStats.WeaponCharge, 1211008) != null) {
                    applyto.cancelEffectFromBuffStat(BuffStats.WeaponCharge, -1);
                    SkillFactory.getSkill(1211008).getEffect(applyto.getSkillLevel(1211008)).applyBuffEffect(applyto, applyto, primary, true);
                }
                break;
            case 1211008: // ����Ʈ�� ����
                if ((applyto.getBuffedValue(BuffStats.WeaponCharge) != null && applyto.getBuffedValue(BuffStats.WeaponCharge, 1211008) == null) || lightCharge) {
                    this.statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MAD, getSkillStats().getStats("mad"), false));
                }
                break;
            case 3220005: { // ���Ǹ� ��ũ : ������
                SkillStatEffect eff_ = SkillFactory.getSkill(3211005).getEffect(applyto.getSkillLevel(3211005));
                time = eff_.time;
                localDuration = eff_.getDuration();
                applyto.send(MainPacketCreator.giveSpiritLink(localDuration, 3211005, 3220005));
                normal = false;
                break;
            }
            case 3120006: { // ���Ǹ� ��ũ : �Ǵн�
                SkillStatEffect eff_ = SkillFactory.getSkill(3111005).getEffect(applyto.getSkillLevel(3111005));
                time = eff_.time;
                localDuration = eff_.getDuration();
                applyto.send(MainPacketCreator.giveSpiritLink(localDuration, 3111005, 3120006));
                normal = false;
                break;
            }
            case 20031210: { // ���� ������Ʈ - 120725 �߰�
                if (applyto.getCardStack() < 40) {
                    applyto.getClient().getSession().close();
                    return;
                }
                applyto.setCardStack(0);
                int skillid = 0;
                if (applyto.getSkillLevel(24120002) > 0) {
                    skillid = 24120002;
                } else if (applyto.getSkillLevel(24100003) > 0) {
                    skillid = 24100003;
                }
                if (skillid == 0) {
                    System.err.println("phantom judgement returned...");
                    return;
                }
                int rand = Randomizer.nextBoolean() ? 1 : 0;
                applyto.getMap().broadcastMessage(applyto, MainPacketCreator.absorbingCardStack(applyto.getId(), 0, skillid, true, 5), true);
                applyto.getMap().broadcastMessage(MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand, 1, true, 0));
                applyto.getMap().broadcastMessage(applyto, MainPacketCreator.absorbingCardStack(applyto.getId(), 2, skillid, true, 5), true);
                applyto.getMap().broadcastMessage(MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand, 3, true, 0));
                applyto.getMap().broadcastMessage(applyto, MainPacketCreator.absorbingCardStack(applyto.getId(), 4, skillid, true, 5), true);
                applyto.send(MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand, 1, false, 0));

                final List<Triple<BuffStats, Integer, Boolean>> stat = Collections.singletonList(new Triple<BuffStats, Integer, Boolean>(BuffStats.DebuffTolerance, rand == 0 ? 5 : 10, false));
                applyto.send(MainPacketCreator.givePhantomJudgement(sourceid, getDuration(), stat, rand + 1));
                statups = stat;
                normal = false;
                break;
            }
            case 20031209: { // ������Ʈ
                if (applyto.getCardStack() < 20) {
                    applyto.getClient().getSession().close();
                    return;
                }
                applyto.setCardStack(0);
                int skillid = 0;
                if (applyto.getSkillLevel(24120002) > 0) {
                    skillid = 24120002;
                } else if (applyto.getSkillLevel(24100003) > 0) {
                    skillid = 24100003;
                }
                if (skillid == 0) {
                    System.err.println("phantom judgement returned...");
                    return;
                }
                int rand = Randomizer.nextBoolean() ? 1 : 0;
                applyto.getMap().broadcastMessage(applyto, MainPacketCreator.absorbingCardStack(applyto.getId(), 0, skillid, true, 5), true);
                applyto.getMap().broadcastMessage(MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand, 1, true, 0));
                applyto.send(MainPacketCreator.showRandBuffEffect(applyto.getId(), sourceid, rand, 1, false, 0));

                final List<Triple<BuffStats, Integer, Boolean>> stat = Collections.singletonList(new Triple<BuffStats, Integer, Boolean>(BuffStats.DebuffTolerance, rand == 0 ? 5 : 10, false));
                applyto.send(MainPacketCreator.givePhantomJudgement(sourceid, getDuration(), stat, rand + 1));
                statups = stat;
                normal = false;
                break;
            }
            case 27100003: { // ���� ���� ��ũ�Ͻ�
                final List<Triple<BuffStats, Integer, Boolean>> stat = Collections.singletonList(new Triple<BuffStats, Integer, Boolean>(BuffStats.BlessOfDarkness, Integer.valueOf(applyto.getBlessOfDark()), false));
                applyto.getClient().getSession().writeAndFlush(MainPacketCreator.giveBuff(sourceid, localDuration, stat, this, null, SkillFactory.getSkill(sourceid).getAnimationTime(), applyto));
                normal = false;
                break;
            }
            case 31011001: {
                applyto.exeedCount = 0;
                applyto.cancelEffectFromBuffStat(BuffStats.OverloadCount);
                applyto.addHP(applyto.getStat().getCurrentMaxHp());
                break;
            }
            case 27121054: {
                applyto.luminusskill[0] = 20040216;
                applyto.luminusskill[1] = 20040217;
                boolean onoff = applyto.getBuffedValue(BuffStats.Larkness) == null || applyto.getBuffedValue(BuffStats.Larkness) == 20040216;
                final SkillStatEffect a = SkillFactory.getSkill(onoff ? 20040220 : 20040219).getEffect(1);
                a.applyequilibriumBuff(applyto, onoff);

                normal = false;
                break;
            }
            case 36111003: {
                statups.clear();
                if (applyto.dualBrid != 0) {
                    effects.setStats("prop", effects.getStats("prop") - effects.getStats("y"));
                }
                effects.setStats("x", applyto.dualBrid);
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.StackBuff, applyto.dualBrid, false));
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.PDD, 100 * applyto.dualBrid, false));
                break;
            }
            case 27121005: {
                statups.clear();
                applyto.acaneAim = applyto.acaneAim == 0 ? 1 : applyto.acaneAim;
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.StackBuff, applyto.acaneAim, false));
                effects.setStats("x", applyto.acaneAim);
                break;
            }
            case 4221054: {
                statups.clear();
                if (applyto.flipTheCoin < 5) {
                    applyto.flipTheCoin++;
                }
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.FlipTheCoin, applyto.flipTheCoin, false));
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, effects.getStats("indieDamR") * applyto.flipTheCoin, true));
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCr, effects.getStats("x") * applyto.flipTheCoin, true));
                break;
            }
            case 100000276: // ���ǵ� Ÿ��(����Ʈ)
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.TimeFastABuff, applyto.RapidTimeCount, false));
                break;
            case 100000277: // ���ǵ� Ÿ��(�Ĺ�)
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.TimeFastBBuff, applyto.RapidTimeCount, false));
                break;
            case 5121055: {
                statups.clear();
                applyto.unitiyOfPower++;
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.UnityOfPower, applyto.unitiyOfPower > 4 ? 4 : applyto.unitiyOfPower, false));
                break;
            }
            case 1310016: {
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieCr, effects.getStats("indieCr"), true));
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EPAD, effects.getStats("epad"), false));
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EPDD, effects.getStats("epdd"), false));
                break;
            }
            case 15120003:
            case 15111022: {
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.DamR, applyto.lightning * getY(), true));
                applyto.lightning = 0;
                applyto.cancelEffectFromBuffStat(BuffStats.CygnusElementSkill, 15001022);
                break;
            }
            case 1200014: {
                //if (applyto.GetCount() > 0) {
                statups.clear();
                effects.setStats("x", applyto.GetCount());
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.ElementalCharge, effects.getStats("x"), false));
                //}
                break;
            }
            case 1301013: {
                effects.setStats("sBeholder", 0); // �ӽ�ó��
                break;
            }
            case 65000003: { // ���Ǵ�Ƽ I
                applyto.send(MainPacketCreator.giveAffinity(sourceid, this.getSkillStats().getStats(("psdSpeed")), 5000));
                applyto.send(MainPacketCreator.giveAffinity(sourceid, this.getSkillStats().getStats(("psdJump")), 5000));
                applyto.send(MainPacketCreator.giveAffinity(sourceid, this.getSkillStats().getStats(("speedMax")), 5000));
                break;
            }
            case 65100005: { // ���Ǵ�Ƽ II
                applyto.send(MainPacketCreator.giveAffinity(sourceid, this.getSkillStats().getStats(("asrR")), 5000));
                applyto.send(MainPacketCreator.giveAffinity(sourceid, this.getSkillStats().getStats(("terR")), 5000));
                break;
            }
            case 65110006: { // ���Ǵ�Ƽ III
                applyto.send(MainPacketCreator.giveAffinity(sourceid, this.getSkillStats().getStats(("dexX")), 5000));
                applyto.send(MainPacketCreator.giveAffinity(sourceid, this.getSkillStats().getStats(("damR")), 5000));
                break;
            }
            case 65120006: { // ���Ǵ�Ƽ IV
                applyto.send(MainPacketCreator.giveAffinity(sourceid, this.getSkillStats().getStats(("y")), 5000));
                break;
            }
            case 100001268: { // ������ ��ȣ
                statups.clear();
                if (GameConstants.isZero(applyto.getJob())) {
                    statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BasicStatUp, effects.getStats("x"), false));
                }
                time = Integer.MAX_VALUE;
                break;
            }
            case 2121004:
            case 2221004:
            case 2321004: { //Infinity
                applyto.setInfinitytime(System.currentTimeMillis());
                applyto.startInfinityRegen(this);
                break;
            }
            case 1321015: {
                applyto.addHP(applyto.getStat().getCurrentMaxHp() - applyto.getStat().getHp());
                applyto.cancelEffect(applyto.getBuffedSkillEffect(BuffStats.Beholder, 1301013), false, -1);
                break;
            }
            case 1320019: {
                effects.setStats("z", applyto.getReincarnationCount());
                if (applyto.getReincarnationCount() == 0) {
                    localDuration = 10000;
                }
                break;
            }
            case 2111011: {
                effects.setStats("y", applyto.elementalAdep);
                break;
            }
            case 21110000: {
                statups.clear();
                statups.add(new Triple<>(BuffStats.ComboAbilityBuff, (int) (applyto.getCombo() > 50 ? 50 : applyto.getCombo()), false));
                break;
            }
            case 25121209: {
                statups.clear();
                statups.add(new Triple<>(BuffStats.SpiritGuard, applyto.SpiritGuard, false));
                break;
            }
            case 27121052: {
                applyto.giveDebuff(DiseaseStats.STUN, MobSkillFactory.getMobSkill(123, 1));
                break;
            }
            case 32001014: // ����
            case 32100010: // ���� ��Ʈ��Ʈ
            case 32110017: // ���� ��Ʈ��Ʈ2
            case 32120019: { // ���� ��Ʈ��Ʈ3
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.BMageDeath, applyto.deathCount, false));
                break;
            }
            case 4221013: {
                statups.clear();
                statups.add(new Triple<>(BuffStats.IndiePAD, effects.getStats("x") + (applyto.KillingPoint * 10), true));
                applyto.KillingPoint = 0;
                applyto.send(MainPacketCreator.KillingPoint(applyto.KillingPoint));
                break;
            }
            case 31011000: // �ͽõ� : ���� ������
            case 31201000: // �ͽõ� : ���� ��Ʈ����ũ
            case 31211000: // �ͽõ� : ������Ʈ ������
            case 31221000: {// �ͽõ� : ����ť��
                statups.clear();
                statups.add(new Triple<>(BuffStats.Exceed, Math.max(applyto.exeedAttackCount, 4), false));
                break;
            }
            case 30010230: {
                statups.clear();
                statups.add(new Triple<>(BuffStats.OverloadCount, applyto.exeedCount, false));
                break;
            }
            case 37110009:
            case 37120012: {
                if (sourceid == 37110009) {
                    if (applyto.getSkillLevel(37120012) > 0) {
                        SkillFactory.getSkill(37120012).getEffect(applyto.getSkillLevel(37120012)).applyTo(applyto);
                        return;
                    }
                }
                applyto.combination++;
                if (applyto.combination > 10) {
                    applyto.combination = 10;
                }
                statups.clear();
                statups.add(new Triple<>(BuffStats.RWCombination, applyto.combination, false));
                break;
            }
            case 400031006: { // Ʈ�� ��������
                applyto.trueSniping = getX();
                break;
            }
            case 400041032: { // ���� �� ����
                statups.clear();
                switch (applyto.readyToDie) {
                    case 0:
                        statups.add(new Triple<>(BuffStats.IndieEVAR, -effects.getStats("x"), true));
                        statups.add(new Triple<>(BuffStats.IndieAsrR, -effects.getStats("z"), true));
                        statups.add(new Triple<>(BuffStats.IndieTerR, -effects.getStats("z"), true));
                        statups.add(new Triple<>(BuffStats.IndiePMdR, effects.getStats("y"), true));
                        break;
                    case 1:
                        statups.add(new Triple<>(BuffStats.IndieEVAR, -effects.getStats("w"), true));
                        statups.add(new Triple<>(BuffStats.IndieAsrR, -effects.getStats("s"), true));
                        statups.add(new Triple<>(BuffStats.IndieTerR, -effects.getStats("s"), true));
                        statups.add(new Triple<>(BuffStats.IndiePMdR, effects.getStats("q"), true));
                        break;
                    default:
                        applyto.cancelEffect(this, false, -1);
                        return;
                }
                applyto.readyToDie++;
                statups.add(new Triple<>(BuffStats.ReadyToDie, applyto.readyToDie, false));
                break;
            }
            case 400041002: { // ������ ���Ʈ

                statups.clear();
                statups.add(new Triple<>(BuffStats.ShadowAssault, 3, false));
                applyto.shadowAssault = 1;
                applyto.send(MainPacketCreator.giveBuff(sourceid, this.getDuration(), statups, this, null, time, applyto));
                break;
            }
            case 400041003: {

                statups.clear();
                statups.add(new Triple<>(BuffStats.ShadowAssault, 2, false));
                applyto.shadowAssault++;
                applyto.send(MainPacketCreator.giveBuff(sourceid, this.getDuration(), statups, this, null, time, applyto));
                break;
            }
            case 400041004: {
                statups.clear();
                statups.add(new Triple<>(BuffStats.ShadowAssault, 1, false));
                applyto.shadowAssault++;
                applyto.send(MainPacketCreator.giveBuff(sourceid, this.getDuration(), statups, this, null, time, applyto));
                break;
            }
            case 400041005: {
                if (applyto.shadowAssault != 3) {
                    return;
                }

                applyto.cancelEffectFromBuffStat(BuffStats.ShadowAssault);
                return;
            }
            case 400051033: // ���� ����̺�
                statups.clear();
                IEquip eqpWeapon = (IEquip) applyto.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                if (eqpWeapon != null) {
                    statups.add(new Triple<>(BuffStats.IndiePAD, (int) (eqpWeapon.getWatk() * (double) getX() / 100), true));
                }
                statups.add(new Triple<>(BuffStats.OverDrive, 1, false));
                break;
            case 400051002: // Ʈ���� ��
                applyto.transformEnergyOrb = effects.getStats("w");
                statups.clear();
                statups.add(new Triple<>(BuffStats.IndiePMdR, effects.getStats("indiePMdR"), true));
                statups.add(new Triple<>(BuffStats.Transform, applyto.transformEnergyOrb, false));
                applyto.acaneAim = 10000;
                applyto.send(MainPacketCreator.giveEnergyCharge(applyto.acaneAim, 10000, 5120018, 10000, false, false));
                break;
            case 400011052: // ������ �ظ�
                statups.clear();
                statups.add(new Triple<>(BuffStats.BlessedHammer, applyto.GetCount(), false));
                break;
            case 400011053: // ������ �ظ� (ū��)
                statups.clear();
                statups.add(new Triple<>(BuffStats.BlessedHammerBig, applyto.GetCount(), false));
                if (applyto.getCooldownLimit(400011052) == 0) {
                    SkillStatEffect effect = SkillFactory.getSkill(400011052).getEffect(applyfrom.getSkillLevel(400011052));
                    applyto.addCooldown(400011052, System.currentTimeMillis(), effect.getCooldown() * 1000);
                }
                break;
            case 400011003: // Ȧ�� ����Ƽ
            {
                statups.clear();
                {
                    final Rectangle bounds = calculateBoundingBox(applyfrom.getPosition(), applyfrom.isFacingLeft());
                    final List<MapleMapObject> affecteds = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.PLAYER));
                    final List<MapleCharacter> chrs = new LinkedList<MapleCharacter>();
                    if (applyfrom.getParty() != null) {
                        for (MapleMapObject mo : affecteds) {
                            MapleCharacter hp = (MapleCharacter) mo;
                            if (hp.getPartyId() == applyfrom.getPartyId() && hp.getJob() != 122) {
                                chrs.add(hp);
                            }
                        }
                    }
                    if (applyto.getParty() == null || chrs.size() <= 0) {
                        statups.add(new Triple<>(BuffStats.IndiePMdR, getZ(), true));
                    } else {
                        boolean found = false;
                        for (MapleCharacter player : chrs) {
                            if (player == applyto) {
                                continue;
                            }

                            if (player.getBuffedValue(BuffStats.HolyUnity) != null) {
                                continue;
                            }
                            statups.add(new Triple<>(BuffStats.IndiePMdR, getU(), true));
                            statups.add(new Triple<>(BuffStats.HolyUnity, player.getId(), false));
                            found = true;
                            break;
                        }
                        if (!found) {
                            statups.add(new Triple<>(BuffStats.IndiePMdR, getZ(), true));
                        }
                    }
                }
                break;
            }
            case 152000007:
            case 152110009:
            case 152120012: {
                statups.clear();
                applyto.BLESS_MARK++;
                int limit = 5;
                if (applyto.getSkillLevel(152110009) > 0) {
                    limit = 6;
                }
                if (applyto.getSkillLevel(152120012) > 0) {
                    limit = 9;
                }
                if (applyto.BLESS_MARK > limit) {
                    applyto.BLESS_MARK = limit;
                }
                statups.add(new Triple<>(BuffStats.BlessMark, applyto.BLESS_MARK, false));
                statups.add(new Triple<>(BuffStats.IndiePAD, getW() * applyto.BLESS_MARK, true));
                statups.add(new Triple<>(BuffStats.IndieMAD, getW() * applyto.BLESS_MARK, true));
                break;
            }
            case 152121043: {
                localDuration = applyto.BLESS_MARK * 1000;
                if (localDuration < 1000) {
                    localDuration = 1000;
                }
                statups.clear();
                statups.add(new Triple<>(BuffStats.NotDamaged, 1, false));
                break;
            }
            case 152101000: {
                localDuration = Integer.MAX_VALUE;
                statups.clear();
                statups.add(new Triple<>(BuffStats.IndieIliumStack, 1, true));
                break;
            }
            case 64100004:
            case 64110005:
            case 64120006: {
                statups.clear();
                statups.add(new Triple<>(BuffStats.KadenaStack, applyto.KADENA_STACK, false));
                break;
            }
            case 142121008: {
                int charge = applyto.PPoint - 30;
                if (charge < 0) {
                    charge = 0;
                }
                charge /= effects.getStats("x");
                applyto.givePP(charge);
                break;
            }
            case 14121052: {
                applyto.DOMINION_MAP = applyto.getMapId();
                applyto.DOMINION_TIME = System.currentTimeMillis() + 30000;
                break;
            }
            case 400001012: {
                for (Entry<Integer, Pair<Integer, MapleSummon>> summon : applyto.getSummons().entrySet()) {
                    if (summon.getValue().right.getSkill() == 3211005 || summon.getValue().right.getSkill() == 3111005) {
                        summon.getValue().right.updateSummon(applyto.getMap(), true);
                        summon.getValue().right.setSkill(400001012, this.level);
                        summon.getValue().right.updateSummon(applyto.getMap(), false);
                        break;
                    }
                }
                break;
            }
            case 15121054: // õ������
            {
                statups.clear();
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.StrikerHyperElectric, effects.getStats("x"), false));
                statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieDamR, effects.getStats("indieDamR") * applyto.lightning, true));
                break;
            }
            case 51001005: {
                int pad = 0;
                switch (applyto.royalGuard) {
                    case 1:
                        pad = 4;
                        break;
                    case 2:
                        pad = 15;
                        break;
                    case 3:
                        pad = 25;
                        break;
                    case 4:
                        pad = 30;
                        break;
                    case 5:
                        pad = 45;
                        break;
                }
                localDuration = 12000;
                statups.clear();
                statups.add(new Triple<>(BuffStats.IndiePAD, pad, true));
                statups.add(new Triple<>(BuffStats.RoyalGuardState, applyto.royalGuard, false));
                break;
            }

            case 155001001:
                statups.clear();
                statups.add(new Triple<>(BuffStats.Speed, applyto.isActiveBuffedValue(155121043) ? effects.getStats("speed") * 2 : effects.getStats("speed"), false));
                statups.add(new Triple<>(BuffStats.IndieStance, applyto.isActiveBuffedValue(155121043) ? effects.getStats("indieStance") * 2 : effects.getStats("indieStance"), true));
                break;
            case 155101003:
                statups.clear();
                statups.add(new Triple<>(BuffStats.IndiePAD, applyto.isActiveBuffedValue(155121043) ? effects.getStats("indiePad") * 2 : effects.getStats("indiePad"), true));
                statups.add(new Triple<>(BuffStats.IndieCr, applyto.isActiveBuffedValue(155121043) ? effects.getStats("indieCr") * 2 : effects.getStats("indieCr"), true));
                break;
            case 155111005:
                statups.clear();
                statups.add(new Triple<>(BuffStats.IndieBooster, applyto.isActiveBuffedValue(155121043) ? -2 : -1, true));
                statups.add(new Triple<>(BuffStats.IndieEVAR, applyto.isActiveBuffedValue(155121043) ? effects.getStats("indieEvaR") * 2 : effects.getStats("indieEvaR"), true));
                break;
            case 155121005:
                statups.clear();
                statups.add(new Triple<>(BuffStats.IndieDamR, applyto.isActiveBuffedValue(155121043) ? effects.getStats("indieDamR") * 2 : effects.getStats("indieDamR"), true));
                statups.add(new Triple<>(BuffStats.IndieBDR, applyto.isActiveBuffedValue(155121043) ? effects.getStats("indieBDR") * 2 : effects.getStats("indieBDR"), true));
                statups.add(new Triple<>(BuffStats.IndieIgnoreMobpdpR, applyto.isActiveBuffedValue(155121043) ? effects.getStats("indieIgnoreMobpdpR") * 2 : effects.getStats("indieIgnoreMobpdpR"), true));
                break;
            default:
                if (isMonsterRiding()) {
                    if (sourceid == 80001490 || sourceid == 80001491) {
                        if (applyto.getMap().getId() != 910048100) {
                            applyto.dropMessage(5, "�ش� ��ų�� �̺�Ʈ �ʿ����� ��� ���� �մϴ�.");
                            applyto.ea();
                            return;
                        }
                    }
                    if (sourceid != 400031017) {
                        statups.clear();
                    }
                    int mountid = (sourceid == 33001001 ? GameConstants.getJaguarIdByMob(applyfrom) : parseMountInfo(applyfrom, sourceid));
                    if (sourceid == 33001001) { // ��Ծ� ���̵�
                        SkillStatEffect sjagur = applyto.getBuffedSkillEffect(BuffStats.JaguarSummoned);
                        if (sjagur != null) {
                            applyto.cancelEffect(sjagur, false, applyto.getBuffedStarttime(BuffStats.JaguarSummoned, sjagur.getSourceId()));
                        }
                        SkillStatEffect ja_eff = applyto.getBuffedSkillEffect(BuffStats.JaguarSummoned);
                        if (ja_eff != null) {
                            applyto.cancelEffect(ja_eff, true, -1);
                        }
                        mountid = GameConstants.getJaguarIdByMob(applyto.getKeyValue2("CapturedJaguar"));
                        statups.add(new Triple<>(BuffStats.MonsterRiding, mountid, false));

                    } else if (sourceid == 35001002) { // ��Ż�Ƹ� : �޸�
                        // MP #mpCon �Һ�, ž�� �� �ִ� HP #emhp, �ִ� MP #emmp, �̵��ӵ� 20, ���ݷ� #epad, ���� #epdd ����
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieBooster, -1, true));
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieSpeed, 30, true));
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.IndieMHPR, 30, true));
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EPDD, effects.getStats("epdd"), false));
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EPAD, effects.getStats("epad"), false));
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EMMP, effects.getStats("emmp"), false));
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.EMHP, effects.getStats("emhp"), false));
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Mechanic, 0, false));
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MonsterRiding, mountid, false));
                        applyto.setKeyValue2("mountid", mountid);
                        applyto.setKeyValue2("mountskillid", sourceid);
                    } else if (sourceid == 35111003) { // ��Ż�Ƹ� : ��ũ
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.MonsterRiding, mountid, false));
                        statups.add(new Triple<BuffStats, Integer, Boolean>(BuffStats.Mechanic, 1, false));
                        applyto.setKeyValue2("mountid", mountid);
                        applyto.setKeyValue2("mountskillid", sourceid);
                    } else if (sourceid == 22171080) {
                        statups.add(new Triple<>(BuffStats.RideVehicleExpire, mountid, false));
                        statups.add(new Triple<>(BuffStats.NewFlying, 1, false));
                    } else if (mountid != 0) {
                        if (!statups.contains(BuffStats.MonsterRiding)) {
                            statups.add(new Triple<>(BuffStats.MonsterRiding, mountid, false));
                        }
                        if (SkillFactory.getSkill(sourceid).getDecs().contains("���")) {
                            if (!statups.contains(BuffStats.NewFlying)) {
                                statups.add(new Triple<>(BuffStats.NewFlying, 1, false));
                            }
                        }
                    } else {
                        return;
                    }
                    normal = true;
                }
                break;

        }
        if (localDuration < 0) {
            localDuration = Integer.MAX_VALUE;
        }
        if (sourceid == 152001005) {
            localDuration = 8000;
        }
        if (sourceid == 5) {
            applyto.acaneAim = 10000;
            applyto.�������������� = 1;
        }
        if (SkillFactory.getSkill(sourceid) != null && sourceid != 21110016 && sourceid / 100000000 != 4 && sourceid != 51001005 && sourceid != 3121054) {
            if (SkillFactory.getSkill(sourceid).notCancel()) {
                localDuration = Integer.MAX_VALUE;
            } else if (SkillFactory.getSkill(sourceid).isNotRemoved() && sourceid != 1200014 && sourceid != 1321015 && sourceid != 2211012 && sourceid != 2311009 && sourceid != 1210016 && sourceid != 1321054 && sourceid != 1121054 && sourceid != 31121054 && sourceid != 4341054 && sourceid != 5321054 /*&& sourceid != 4341002*/ && sourceid != 11121054 && sourceid != 15120003 && sourceid != 15111022 && sourceid != 400021006) {
                boolean c = false;
                for (Triple<BuffStats, Integer, Boolean> v : statups) {
                    if (v.getFirst().equals(BuffStats.NotDamaged)) {
                        c = true;
                        break;
                    }
                }
                if (time > 1000) {
                    c = true;
                }
                if (!c) {
                    localDuration = Integer.MAX_VALUE;
                }
            }
        }
        if (sourceid == 2201009 || sourceid == 400011052) {
            localDuration = Integer.MAX_VALUE;
        }
        if (sourceid == 21110016 || sourceid == 21121058) {
            applyto.royalGuard = 1;
        }

        applyto.cancelEffect(this, true, -1);
        if (sourceid == 35001002 || sourceid == 35111003) {
            PlayerHandler.CancelBuffHandler(35001002, applyto);
            PlayerHandler.CancelBuffHandler(35111003, applyto);
            applyto.send(MechanicSkill.giveHuman(statups, sourceid, localDuration, parseMountInfo(applyto, sourceid)));
        } else if (normal && statups.size() > 0) {
            long overlap_magic = (long) (System.currentTimeMillis() % 1000000000);
            Map<BuffStats, List<StackedSkillEntry>> stacked = applyto.getStackSkills();
            for (Triple<BuffStats, Integer, Boolean> statup : statups) {
                if (statup.getThird()) {
                    if (!stacked.containsKey(statup.getFirst())) {
                        stacked.put(statup.getFirst(), new ArrayList<StackedSkillEntry>());
                    }
                    stacked.get(statup.getFirst()).add(new StackedSkillEntry(skill ? getSourceId() : -getSourceId(), statup.getSecond(), overlap_magic, localDuration));
                }
            }
            applyto.getClient().getSession().writeAndFlush(MainPacketCreator.giveBuff((skill ? sourceid : -sourceid), localDuration, statups, this, stacked, 0, applyto));
        }
        final long starttime = System.currentTimeMillis();
        final CancelEffectAction cancelAction = new CancelEffectAction(applyto, this, starttime);
        ScheduledFuture<?> schedule = null;
        if (localDuration != Integer.MAX_VALUE) {
            schedule = tools.Timer.BuffTimer.getInstance().schedule(cancelAction, ((starttime + localDuration) - System.currentTimeMillis()));
        }
        applyto.registerEffect(this, starttime, schedule);
        applyto.refreshMaxHpMp();
        //if (applyto.getMap().getId() != 100000000) {
        applyto.getMap().broadcastMessage(applyto, MainPacketCreator.giveForeignBuff(applyto, statups), false);
        //}
    }

    public final void applyToBMDeath(MapleCharacter applyto) {
        statups.clear();
        statups.add(new Triple<>(BuffStats.BMageDeath, applyto.deathCount, false));
        int localDuration = time;
        if (localDuration < 0) {
            localDuration = Integer.MAX_VALUE;
        }
        if (SkillFactory.getSkill(sourceid).notCancel()) {
            localDuration = Integer.MAX_VALUE;
        }
        for (Entry<Integer, Pair<Integer, MapleSummon>> summons : applyto.getSummons().entrySet()) {
            if (summons.getValue().left == sourceid) {
                return;
            }
        }
        if (statups.size() > 0) {
            long overlap_magic = (long) (System.currentTimeMillis() % 1000000000);
            Map<BuffStats, List<StackedSkillEntry>> stacked = applyto.getStackSkills();
            for (Triple<BuffStats, Integer, Boolean> statup : statups) {
                if (statup.getThird()) {
                    if (!stacked.containsKey(statup.getFirst())) {
                        stacked.put(statup.getFirst(), new ArrayList<StackedSkillEntry>());
                    }
                    stacked.get(statup.getFirst()).add(new StackedSkillEntry(skill ? getSourceId() : -getSourceId(), statup.getSecond(), overlap_magic, localDuration));
                }
            }
            applyto.getClient().getSession().writeAndFlush(MainPacketCreator.giveBuff((skill ? sourceid : -sourceid), localDuration, statups, this, stacked, SkillFactory.getSkill(sourceid).getAnimationTime(), applyto));
        }
        if (applyto.deathCount == getX()) {
            Point pos = applyto.getPosition();
            final MapleSummon tosummon = new MapleSummon(applyto, sourceid, pos, getSummonMovementType(), System.currentTimeMillis());
            tosummon.setPosition(pos);
            applyto.getMap().spawnSummon(tosummon, true, getDuration());
            applyto.getSummons().put(tosummon.getObjectId(), new Pair<>(sourceid, tosummon));
            tosummon.addHP(effects.getStats("x"));
        }
    }

    public final void applyToQuiverCatridge(MapleCharacter applyto, int v1) {
        statups.clear();
        statups.add(new Triple<>(BuffStats.QuiverCatridge, v1, false));
        int localDuration = time;
        if (localDuration < 0) {
            localDuration = Integer.MAX_VALUE;
        }
        if (SkillFactory.getSkill(sourceid).notCancel()) {
            localDuration = Integer.MAX_VALUE;
        }
        applyto.cancelEffect(this, true, -1);
        if (statups.size() > 0) {
            long overlap_magic = (long) (System.currentTimeMillis() % 1000000000);
            Map<BuffStats, List<StackedSkillEntry>> stacked = applyto.getStackSkills();
            for (Triple<BuffStats, Integer, Boolean> statup : statups) {
                if (statup.getThird()) {
                    if (!stacked.containsKey(statup.getFirst())) {
                        stacked.put(statup.getFirst(), new ArrayList<StackedSkillEntry>());
                    }
                    stacked.get(statup.getFirst()).add(new StackedSkillEntry(skill ? getSourceId() : -getSourceId(), statup.getSecond(), overlap_magic, localDuration));
                }
            }
            applyto.getClient().getSession().writeAndFlush(MainPacketCreator.giveBuff((skill ? sourceid : -sourceid), localDuration, statups, this, stacked, SkillFactory.getSkill(sourceid).getAnimationTime(), applyto));
        }
        final long starttime = System.currentTimeMillis();
        final CancelEffectAction cancelAction = new CancelEffectAction(applyto, this, starttime);
        final ScheduledFuture<?> schedule = tools.Timer.BuffTimer.getInstance().schedule(cancelAction, ((starttime + localDuration) - System.currentTimeMillis()));
        applyto.registerEffect(this, starttime, schedule);
        applyto.refreshMaxHpMp();
        applyto.getMap().broadcastMessage(applyto, MainPacketCreator.giveForeignBuff(applyto, statups), false);
    }

    public static final int parseMountInfo(final MapleCharacter player, final int skillid) {
        int veid = 0;
        if (SkillFactory.getSkill(skillid) != null) {
            veid = SkillFactory.getSkill(skillid).getVehicleID();
        }
        if (veid > 0) {
            return veid;
        }
        switch (skillid) {
            case 1004:
            case 10001004:
            case 20001004:
            case 20011004:
            case 30001004:
            case 20021004:
            case 20031004:
            case 30011004:
            case 50001004:
                if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -122) != null) {
                    return player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -122).getItemId();
                }
                if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -22) != null) {
                    return player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -22).getItemId();
                }
                return 0;
            case 35001002:
            case 35111003:
            case 35120000:
                return 1932016;
            case 22171080:
                return 1939007;
            default:
                return GameConstants.getMountItem(skillid, player);
        }
    }

    private final int calcHPChange(final MapleCharacter applyfrom, final boolean primary) {
        int hpchange = 0;
        if (effects.getStats("hp") != 0) {
            if (!skill) {
                hpchange += effects.getStats("hp");
                if (applyfrom.hasDisease(DiseaseStats.ZOMBIFY)) {
                    hpchange /= 2;
                }
                if (applyfrom.getSkillLevel(30000002) > 0) { // ���Ǽǽ�
                    double percent = (double) (SkillFactory.getSkill(30000002).getEffect(1).getX() / 100.0D);
                    hpchange = (int) ((double) hpchange * percent);
                } else if (applyfrom.getSkillLevel(30010002) > 0) { // ���Ǽǽ�
                    double percent = (double) (SkillFactory.getSkill(30010002).getEffect(1).getX() / 100.0D);
                    hpchange = (int) ((double) hpchange * percent);
                }
            } else {
                if (isHeal()) {
                    int hpPercent = effects.getStats("hp");
                    hpchange += (int) (applyfrom.getStat().getCurrentMaxHp() * ((double) hpPercent / 100.0D));
                    if (applyfrom.hasDisease(DiseaseStats.ZOMBIFY)) {
                        hpchange = -hpchange;
                    }
                } else {
                    hpchange += effects.getStats("hp");
                }
            }
        }
        if (effects.getStats("hpR") != 0) {
            hpchange += (int) (applyfrom.getStat().getCurrentMaxHp() * (effects.getStats("hpR") / 100.0D));
        }
        if (effects.getStats("hpRCon") != 0) {
            hpchange -= (int) (applyfrom.getStat().getCurrentMaxHp() * (effects.getStats("hpRCon") / 100.0D));
        }
        if (primary) {
            if (effects.getStats("hpCon") != 0) {
                hpchange -= effects.getStats("hpCon");
            }
        }
        return hpchange;
    }

    private static final int getElementalAmp(final int job) {
        switch (job) {
            case 211:
            case 212:
                return 2110001;
            case 221:
            case 222:
                return 2210001;
            case 1211:
            case 1212:
                return 12110001;
            case 2215:
            case 2216:
            case 2217:
            case 2218:
                return 22150000;
        }
        return -1;
    }

    private final int calcMPChange(final MapleCharacter applyfrom, final boolean primary) {
        int mpchange = 0;
        if (isTeleport()) {
            if (applyfrom.getBuffedValue(BuffStats.TeleportMasteryOn) != null) {
                mpchange -= applyfrom.getBuffedValue(BuffStats.TeleportMasteryOn).intValue();
            }
        }

        if (effects.getStats("mp") != 0) {
            mpchange += effects.getStats("mp");
        }

        if (effects.getStats("mpR") != 0) {
            mpchange += (int) (applyfrom.getStat().getCurrentMaxMp() * effects.getStats("mpR"));
        }
        if (primary) {
            if (effects.getStats("mpCon") != 0) {
                double mod = 1.0;

                final int ElemSkillId = getElementalAmp(applyfrom.getJob());
                if (ElemSkillId != -1) {
                    final ISkill amp = SkillFactory.getSkill(ElemSkillId);
                    final int ampLevel = applyfrom.getSkillLevel(amp);
                    if (ampLevel > 0) {
                        SkillStatEffect ampStat = amp.getEffect(ampLevel);
                        mod = ampStat.getX() / 100.0;
                    }
                }
                if (applyfrom.getBuffedValue(BuffStats.Infinity) != null) {
                    mpchange = 0;
                } else if (applyfrom.getBuffedValue(BuffStats.AdvancedBless) != null) {
                    SkillStatEffect eff = applyfrom.getBuffedSkillEffect(BuffStats.AdvancedBless);
                    int reduce = eff.getSkillStats().getStats("mpConReduce");
                    mpchange -= (int) (mpchange * ((double) reduce / 100.0D));
                } else {
                    mpchange -= effects.getStats("mpCon") * mod;
                }
            }
            if (effects.getStats("forceCon") != 0) {
                if (applyfrom.getBuffedValue(BuffStats.InfinityForce) != null) {
                    mpchange = 0;
                } else {
                    mpchange = -effects.getStats("forceCon");
                }
            }
            if (applyfrom.getStat().mpconReduce > 0) {
                int reduce = applyfrom.getStat().mpconReduce;
                mpchange -= (int) (mpchange * ((double) reduce / 100.0D));
            }
        }
        if (!skill && sourceid / 1000000 == 2 && GameConstants.isDemonSlayer(applyfrom.getJob()) || GameConstants.isDemonAvenger(applyfrom.getJob()) || GameConstants.isZero(applyfrom.getJob())) { // ���� ���δ� ȸ���� ����x
            mpchange = 0;
        }
        return mpchange;
    }

    private final int alchemistModifyVal(final MapleCharacter chr, int val, final boolean withX) {
        if (!skill) {
            final SkillStatEffect alchemistEffect = getAlchemistEffect(chr);
            if (alchemistEffect != null) {
                return (int) (val * ((withX ? alchemistEffect.getX() : alchemistEffect.getY()) / 100.0));
            }
        }
        return val;
    }

    private final SkillStatEffect getAlchemistEffect(final MapleCharacter chr) {
        ISkill al;
        switch (chr.getJob()) {
            case 411:
            case 412:
                al = SkillFactory.getSkill(4110000);
                if (chr.getSkillLevel(al) == 0) {
                    return null;
                }
                return al.getEffect(chr.getSkillLevel(al));
            case 1411:
            case 1412:
                al = SkillFactory.getSkill(14110003);
                if (chr.getSkillLevel(al) == 0) {
                    return null;
                }
                return al.getEffect(chr.getSkillLevel(al));
            case 3000:
            case 3200:
            case 3210:
            case 3211:
            case 3212:
            case 3300:
            case 3310:
            case 3311:
            case 3312:
            case 3500:
            case 3510:
            case 3511:
            case 3512:
                al = SkillFactory.getSkill(30000002);
                if (chr.getSkillLevel(al) == 0) {
                    return null;
                }
                return al.getEffect(chr.getSkillLevel(al));
            case 3001:
            case 3100:
            case 3110:
            case 3111:
            case 3112:
                al = SkillFactory.getSkill(30010002);
                if (chr.getSkillLevel(al) == 0) {
                    return null;
                }
                return al.getEffect(chr.getSkillLevel(al));

        }
        return null;
    }

    public final void setSourceId(final int newid) {
        sourceid = newid;
    }

    private final boolean isGmBuff() {
        switch (sourceid) {
            case 1005:
            case 10001005:
            case 20001005:
            case 20011005:
            case 9001000:
            case 9001001:
            case 9001002:
            case 9001003:
            case 9001005:
            case 9001008:
                return true;
            default:
                return false;
        }
    }

    private final boolean isEnergyCharge() {
        return skill && (sourceid == 5100015 || sourceid == 15100004);
    }

    public final boolean isMonsterBuff() {
        switch (sourceid) {
            case 1211013:
            case 2121006:
            case 2221011:
            case 4111003:
            case 4121015:
            case 4321002:
            case 22141003:
            case 11111023:
            case 22121000:
            case 22161002:
            case 22151001:
            case 25111206:
            case 35111005:
            case 32120000:
            case 32120001:
            case 22110013:
                return skill;
        }
        return false;
    }

    public final boolean isMonsterRiding_() {
        return skill
                && (sourceid == 1004
                || sourceid == 10001004
                || sourceid == 20001004
                || sourceid == 20011004
                || sourceid == 30001004
                && (sourceid >= 80001000 && sourceid <= 80001033)
                || sourceid == 80001037
                || sourceid == 80001038
                || sourceid == 80001039
                || sourceid == 80001044
                || (sourceid >= 80001082 && sourceid <= 80001090)
                || sourceid == 30011159
                || sourceid == 30011109 || sourceid == 33001001 || sourceid == 35001002 || sourceid == 35111003 || sourceid == 22171080);
    }

    public final boolean isMonsterRiding() {
        return (skill && (isMonsterRiding_() || GameConstants.checkMountItem(sourceid) != 0)) || (SkillFactory.getSkill(sourceid) != null && SkillFactory.getSkill(sourceid).getVehicleID() > 0);
    }

    private final boolean isPartyBuff() {
        if (lt == null || rb == null) {
            return false;
        }
        switch (sourceid) {
            case 1211003:
            case 1211004:
            case 1211005:
            case 1211006:
            case 1211007:
            case 1211008:
            case 1221003:
            case 1221004:
            case 1301007:
            case 4311001:
            case 11111007:
            case 12101005:
            case 400011003:
            case 400011021:
                return false;
        }
        return true;
    }

    public final boolean isHeal() {
        return sourceid == 2301002 || sourceid == 9001000;
    }

    public final boolean isResurrection() {
        return sourceid == 9001005 || sourceid == 2321006;
    }

    public final boolean isTimeLeap() {
        return sourceid == 5121010;
    }

    public final boolean isTimeHolding() {
        return sourceid == 100001274;
    }

    public final int getHp() {
        return effects.getStats("hp");
    }

    public final int getMp() {
        return effects.getStats("m");
    }

    public final int getMastery() {
        return effects.getStats("mastery");
    }

    public final int getWatk() {
        return effects.getStats("pad");
    }

    public final int getMatk() {
        return effects.getStats("mad");
    }

    public final int getWdef() {
        return effects.getStats("pdd");
    }

    public final int getMdef() {
        return effects.getStats("mdd");
    }

    public final int getAcc() {
        return effects.getStats("acc");
    }

    public final int getAvoid() {
        return effects.getStats("eva");
    }

    public final int getHands() {
        return effects.getStats("hands");
    }

    public final int getSpeed() {
        return effects.getStats("speed");
    }

    public final int getJump() {
        return effects.getStats("jump");
    }

    public final int getStatusDuration() {
        if (sourceid == 31121003) { // ����ũ����
            return effects.getStats("subTime") * 1000;
        }
        if (effects.getStats("subTime") > 0) {
            return effects.getStats("subTime") * 1000;
        }
        if (sourceid == 1211013) {
            return 8000;
        }
        return time;
    }

    public final int getDuration() {
        if (sourceid == 21121058) {
            return 15000;
        }
        if (sourceid == 2121005) {
            return 260 * 1000;
        }
        if (sourceid == 12121005) {
            return 30 * 1000;
        }
        if (sourceid == 31121003) { // ����ũ����
            return effects.getStats("subTime") * 1000;
        }
        if (sourceid == 152101000) {
            return Integer.MAX_VALUE;
        }
        if (effects.getStats("dotTime") > 0) {
            return effects.getStats("dotTime") * 1000;
        }
        if (time < 100) {
            return time * 1000;
        }
        return time;
    }

    public final int getDotTime() {
        return effects.getStats("dotTime");
    }

    public final int getDotInterval() {
        return effects.getStats("dotInterval") * 1000;
    }

    public final boolean isOverTime() {
        return overTime;
    }

    public final List<Triple<BuffStats, Integer, Boolean>> getStatups() {
        return statups;
    }

    public final boolean sameSource(final SkillStatEffect effect) {
        return this.sourceid == effect.sourceid && this.skill == effect.skill;
    }

    public final int getX() {
        return effects.getStats("x");
    }

    public final int getV() {
        return effects.getStats("v");
    }

    public final int getValue(String value) {
        return effects.getStats(value);
    }

    public final int getY() {
        return effects.getStats("y");
    }

    public final int getZ() {
        return effects.getStats("z");
    }

    public final int getW() {
        return effects.getStats("w");
    }

    public final int getU() {
        return effects.getStats("u");
    }

    public final int getDamage() {
        return effects.getStats("damage");
    }

    public final byte getAttackCount() {
        return (byte) effects.getStats("attackCount");
    }

    public final byte getBulletCount() {
        return (byte) effects.getStats("bulletCount");
    }

    public final int getBulletConsume() {
        return effects.getStats("bulletConsume");
    }

    public final int getOnActive() {
        return effects.getStats("onActive");
    }

    public final byte getMobCount() {
        return (byte) effects.getStats("mobCount");
    }

    public final int getMoneyCon() {
        return effects.getStats("moneyCon");
    }

    public final int getCooldown() {
        if (sourceid == 25101004 || sourceid == 25101003 || sourceid == 25101003 || sourceid == 25101004) {
            return 1;
        } else if (sourceid == 142001000 || sourceid == 142100000 || sourceid == 142110000) {
            return 20;
        } else if (sourceid == 400011001 || sourceid == 400011002) {
            return effects.getStats("x");
        } else if (sourceid == 400041025) {
            return 14;
        } else if (sourceid == 33100016 || sourceid == 33101115 || sourceid == 33101215) {
            return 7;
        } else if (sourceid == 33120056 || sourceid == 33121052 || sourceid == 33121255 || sourceid == 33120056 || sourceid == 33121052 || sourceid == 33121155) {
            return 14;
        } else if (sourceid == 400011010) {
            return effects.getStats("z");
        } else if (sourceid == 142001000 || sourceid == 142100000 || sourceid == 142110000) {
            return 1;
        } else if (sourceid == 400011015) {
            return 240;
        }
        return effects.getStats("cooltime");
    }

    public final int getMaxDamageOver() {
        return effects.getStats("indieMaxDamageOver");
    }

    public final int getPowerEnergy() {
        return effects.getStats("powerCon");
    }

    public final int getPPRecovery() {
        return effects.getStats("ppRecovery");
    }

    public final int getPPCon() {
        if (sourceid == 142120002) {
            return 5;
        }
        return effects.getStats("ppCon");
    }

    public final Map<MonsterStatus, Integer> getMonsterStati() {
        return monsterStatus;
    }

    public final boolean isHide() {
        return skill && sourceid == 9001004;
    }

    public final boolean isBerserk() {
        return skill && sourceid == 1320006;
    }

    public final boolean isComboRecharge() {
        return skill && sourceid == 21111009;
    }

    public final boolean isMPRecovery() {
        return skill && sourceid == 5101005;
    }

    public final boolean isMagicDoor() {
        return skill && sourceid == 2311002;
    }

    public final boolean isMesoGuard() {
        return skill && sourceid == 4201011;
    }

    public final boolean isMechDoor() {
        return skill && sourceid == 35101005;
    }

    public final boolean isMechPassive() {
        switch (sourceid) {
            case 35121013:
                return true;
        }
        return false;
    }

    public final boolean isCharge() {
        switch (sourceid) {
            case 1211006:
            case 1211003:
            case 1211004:
            case 1211005:
            case 1211007:
            case 1211008:
            case 1221003:
            case 1221004:
            case 15101006:
            case 21101006:
                return skill;
        }
        return false;
    }

    public final boolean isFinalAttack() {
        switch (sourceid) {
            case 13101002:
            case 11101002:
                return skill;
        }
        return false;
    }

    public final boolean isMistPoison() {
        switch (sourceid) {
            case 2111003:
            case 14111006:
            case 22181002:
            case 80001431:
                return true;
        }
        return false;
    }

    public final boolean isRecovery() {
        switch (sourceid) {
            case 22161003:
                return true;
        }
        return false;
    }

    public final boolean isBurningRegion() {
        return sourceid == 12121005;
    }

    public final boolean isTimeCapsule() {
        switch (sourceid) {
            case 36121007:
                return true;
        }
        return false;
    }

    public final boolean isPoison() {
        switch (sourceid) {
            case 2111003:
            case 2101005:
            case 2111006:
            case 2221003:
            case 3111003:
            case 25111206:
            case 80001431:
                return skill;
        }
        return false;
    }

    public final boolean isMist() {
        return sourceid == 152121041 || sourceid == 400021041 || sourceid == 400021049 || sourceid == 400021050
                || // �۴Ͻ�																								// ���ҳ�����
                sourceid == 400010010
                || // ����� ������
                sourceid == 400011060
                || sourceid == 400031012 || sourceid == 400011098
                || sourceid == 400001017 || sourceid == 400020002 || sourceid == 400030002 || sourceid == 2311011
                || sourceid == 2101010 || sourceid == 36121007 || sourceid == 35121052 || sourceid == 2100010
                || sourceid == 2111003 || sourceid == 4121015 || sourceid == 4221006
                || sourceid == 21121068 || sourceid == 25111206 || sourceid == 32121006 || sourceid == 33111013
                || sourceid == 33121016 || sourceid == 51120057 || sourceid == 24121052 || sourceid == 100001261
                || sourceid == 155121006 || sourceid == 400041041
           || sourceid == 400031037 || sourceid == 400031039 || sourceid == 400031040 || sourceid == 400020051 || sourceid == 400020046;

    }

    public final boolean isSpiritClaw() {
        return skill && (sourceid == 4111009 || sourceid == 5201008 || sourceid == 14111025);
    }

    private final boolean isDispel() {
        return skill && (sourceid == 2311001 || sourceid == 9001000);
    }

    private final boolean isHeroWill() {
        switch (sourceid) {
            case 1121011:
            case 1221012:
            case 1321010:
            case 2121008:
            case 2221008:
            case 2321009:
            case 3121009:
            case 3221008:
            case 4121009:
            case 4221008:
            case 5121008:
            case 5221010:
            case 21121008:
            case 22171004:
            case 4341008:
            case 80001478: // ���� ����Ʈ ��
                return skill;
        }
        return false;
    }

    public final void applyMist(final MapleCharacter applyfrom, final Point pos) {
        final Rectangle bounds = calculateBoundingBox(pos != null ? pos : applyfrom.getPosition(), applyfrom.isFacingLeft());
        final MapleMist mist = new MapleMist(bounds, applyfrom, this, effects.getLevel(), pos == null ? applyfrom.getPosition() : pos);
        applyfrom.getMap().spawnMist(mist, getTime(), isMistPoison(), false, isRecovery(), isBurningRegion(), isTimeCapsule());
        if (isTimeCapsule()) {
            applyfrom.send(MainPacketCreator.TimeCapsule());
            applyfrom.setChairText(null);
            applyfrom.setChair(3010587);
            applyfrom.getMap().broadcastMessage(applyfrom, MainPacketCreator.showChair(applyfrom.getId(), applyfrom.getChair(), applyfrom.getChairText()), false);
        }
    }

    public final boolean isAranCombo() {
        return sourceid == 21000000;
    }

    public final boolean isMechChange() {
        switch (sourceid) {
            case 35121054:
            case 35111004:
            case 35001001:
            case 35101009:
            case 35121013:
            case 35121005:
            case 35100008:
                return skill;
        }
        return false;
    }

    public final boolean isPirateMorph() {
        switch (sourceid) {
            case 15111002:
            case 5111005:
            case 5121003:
                return skill;
        }
        return false;
    }

    public final boolean isMorph() {
        return effects.getStats("morph") > 0;
    }

    public final boolean isInflation() {
        return effects.getStats("inflation") > 0;
    }

    public final int getMorph() {
        return effects.getStats("morph");
    }

    public final int getInflation() {
        return effects.getStats("inflation");
    }

    public final int getMorph(final MapleCharacter chr) {
        switch (effects.getStats("morph")) {
            case 1000:
            case 1100:
                return effects.getStats("morph") + chr.getGender();
            case 1003:
                return effects.getStats("morph") + (chr.getGender() * 100);
        }
        return effects.getStats("morph");
    }

    public final SummonMovementType getSummonMovementType() {
        switch (sourceid) {
            case 400001022:
            case 400021067:
            case 400021073:
                return SummonMovementType.STATIONARY;
            case 13120007:
            case 13111024:
            case 3211002:
            case 3111002:
            case 33111003:
            case 13111004:
            case 5211001:
            case 5220002:
            case 5321052:
            case 4341006:
            case 35111002:
            case 35111005:
            case 35111011:
            case 35121009:
            case 35121010:
            case 4111007:
            case 4211007:
            case 35121003:
            case 3120012:
            case 3220012:
            case 5321003:
            case 5321004:
            case 5320011:
            case 5211014:
            case 33101008:
            case 61111002:
            case 61111220:
            case 3221014:
            case 22171052:
            case 36121002:
            case 36121013:
            case 36121014:
            case 14121003:
            case 35111008:
            case 35120002:
            case 35101012:
            case 5221022:
            case 400011065: // �����ÿ�
            case 400051022:
            case 400051023:
            case 400011057: // �丣������
            case 400021005: // ������ ��
            case 400021047: // �� ���� ����
            case 400041033: // A.D ����ͽ�
            case 400051011: // ������ ����Ʈ
            case 400041028: // ������ ����Ʈ �ͽ��ٵ�
            case 400001019:
            case 400041038:
                return SummonMovementType.STATIONARY;
            case 3101007:
            case 3201007:
            case 33111005:
            case 33101011:
            case 23111008:
            case 23111009:
            case 23111010:
                return SummonMovementType.CIRCLE_FOLLOW;
            case 5211002:
                return SummonMovementType.CIRCLE_STATIONARY;
            case 5201012:
            case 5201013:
            case 5201014:
            case 5210015:
            case 5210016:
            case 5210017:
            case 5210018:
            case 5211011:
            case 5211015:
            case 5211016:
            case 35121011:
            case 2111010:
            case 22171081:
            case 400011012: // ����� ���� ���
            case 400011013: // ����� ���� ���
            case 400011014: // ����� ���� ���
            case 400001013:
                return SummonMovementType.WALK_STATIONARY;
            case 400051038:
                return SummonMovementType.WALK_FOLLOW;
            case 152101000:
            case 400011088:
            case 400011002:
                return SummonMovementType.ILIUME_CRISTAL;
            case 1301013:
            case 1321007:
            case 2121005:
            case 2221005:
            case 2211011:
            case 2321003:
            case 3111005:
            case 3211005:
            case 12111004:
            case 35111001:
            case 35111010:
            case 35111009:
            case 1085:
            case 1087:
            case 1090:
            case 1179:
            case 10001085:
            case 10001087:
            case 10001090:
            case 10001179:
            case 20001085:
            case 20001087:
            case 20001090:
            case 20001179:
            case 20011085:
            case 20011087:
            case 20011090:
            case 20011179:
            case 20021085:
            case 20021087:
            case 20021090:
            case 20021179:
            case 20031085:
            case 20031087:
            case 20031090:
            case 20031179:
            case 20041085:
            case 20041087:
            case 20041090:
            case 20041179:
            case 30001085:
            case 30001087:
            case 30001090:
            case 30001179:
            case 30011085:
            case 30011087:
            case 30011090:
            case 30011179:
            case 30021085:
            case 30021087:
            case 30021090:
            case 30021179:
            case 50001085:
            case 50001087:
            case 50001090:
            case 50001179:
            case 60001085:
            case 60001087:
            case 60001090:
            case 60001179:
            case 60011085:
            case 60011087:
            case 60011090:
            case 60011179:
            case 80001217:
            case 80001219:
            case 80001266:
            case 80001269:
            case 80001270:
            case 80001281:
            case 80001282:
            case 80001322:
            case 80001323:
            case 80001341:
            case 80001395:
            case 80001396:
            case 80001493:
            case 80001494:
            case 80001495:
            case 80001496:
            case 80001497:
            case 80001498:
            case 80001499:
            case 80001500:
            case 80001501:
            case 80001502:
            case 32001014: // ����
            case 32100010: // ���� ��Ʈ��Ʈ
            case 32110017: // ���� ��Ʈ��Ʈ2
            case 32120019:
            case 400011001:
            case 400021032: // ���� ���� ����� (��Ȱ��ȭ)
            case 400021033: // ���� ���� ����� (Ȱ��ȭ)
            case 400051009: // ��Ƽ�� �ɼ� : M-FL
            case 400051017: // ����ũ�� �̻��� �����̳�
            case 152001003:
            case 80002231: // ��õ�ҿ�
            case 80001985: // ���̾ȼҿ�
            case 80001697: // ����ҿ�
            case 152121005:
            case 400051046:
            case 400031001:
                return SummonMovementType.FOLLOW;
            case 101100100:
            case 101100101:
            case 400011006: // ���� �����ũ��
            case 400011007: // ���� �����ũ��
            case 400011008: // ���� �����ũ��
            case 400011009: // ���� �����ũ��
                return SummonMovementType.ZEROWEAPON;
            case 14120008:
            case 14110029:
            case 14100027:
            case 14000027: // ������ ��Ʈ
            case 152101008:
            case 3311009:
                return SummonMovementType.BIRD_FOLLOW;
            case 14111024: // ������ ����Ʈ
            case 14121054: // ������ �Ϸ���
            case 14121055: // ������ �Ϸ���
            case 14121056: // ������ �Ϸ���
            case 400011005: // ������Ƽ�� ��
            case 400031007: // ������Ż ��Ʈ
            case 400031008:
            case 400031009:
                return SummonMovementType.SHADOW_SERVANT;
            case 33001007:
            case 33001008:
            case 33001009:
            case 33001010:
            case 33001011:
            case 33001012:
            case 33001013:
            case 33001014:
            case 33001015:
                return SummonMovementType.SUMMON_JAGUAR;
            case 12120013:
            case 12120014:
                return SummonMovementType.FLAME_SUMMON;
        }
        if (!skill) {
            return null;
        }
        return null;
    }

    public final boolean isSoaring() {

        switch (sourceid) {
            case 1026:
            case 10001026:
            case 20001026:
            case 20011026:
                return skill;
        }
        return false;
    }

    public final boolean isSkill() {
        return skill;
    }

    public final int getSourceId() {
        return sourceid;
    }

    public final boolean makeChanceResult() {
        return effects.getStats("prop") == 100 || Randomizer.nextInt(100) < effects.getStats("prop");
    }

    public SkillStats getSkillStats() {
        return effects;
    }

    public final int getProb() {
        return effects.getStats("prop");
    }

    public final int getCr() {
        return effects.getStats("cr");
    }

    public final int getCriticalMin() {
        return effects.getStats("criticaldamageMin");
    }

    public final int getCriticalMax() {
        return effects.getStats("criticaldamageMax");
    }

    public final int getAttackX() {
        return effects.getStats("padX");
    }

    public boolean isPhantomSkill() {
        return sourceid / 1000000 == 24;
    }

    public boolean isMagicCrash() {
        return sourceid == 1111007 || sourceid == 1211009 || sourceid == 1311007 || sourceid == 11111008 || sourceid == 51111005;
    }

    public boolean isComboAttack() {
        return sourceid == 1101013 || sourceid == 11111001;
    }

    public boolean isSoulStone() {
        return sourceid == 22181003;
    }

    public boolean isTeleport() {
        switch (sourceid) {
            case 32001002:
            case 22101001:
            case 12101003:
            case 2301001:
            case 2201002:
            case 2101002:
                return true;
        }
        return false;
    }

    public boolean isSTEP() {
        switch (sourceid) {
            case 2201002:
                return true;
        }
        return false;
    }

    public boolean isStaticSummon() {
        switch (sourceid) {
            case 22141017:
            case 13120007:
            case 13111024:
            case 3211002:
            case 3111002:
            case 33111003:
            case 13111004:
            case 5211001:
            case 5220002:
            case 4341006:
            case 35111002:
            case 35111005:
            case 35111011:
            case 35121009:
            case 35121010:
            case 4111007:
            case 4211007:
            case 35121003:
            case 3120012:
            case 3220012:
            case 5321003:
            case 5321004:
            case 5320011:
            case 5211014:
            case 33101008:
            case 61111002:
            case 61111220:
            case 22171081:
            case 5321052:
            case 14121003:
            case 36121002: // Ȧ�α׷� �׷���Ƽ : ����
            case 36121013: // Ȧ�α׷� �׷���Ƽ : ����
            case 36121014: // Ȧ�α׷� �׷���Ƽ : ����
            case 2111010:
            case 400051023:
                return true;
        }
        return false;
    }

    public final boolean isInfinity() {
        return skill && (sourceid == 2121004 || sourceid == 2221004 || sourceid == 2321004);
    }

    /*public boolean isInfinity() {
        switch (sourceid) {
            case 2121004:
            case 2221004:
            case 2321004:
                return true;
        }
        return false;
    }*/
    public boolean isSoulSkill() {
        switch (sourceid) {
            case 80001219:
            case 80001266:
            case 80001269:
            case 80001270:
            case 80001322:
            case 80001323:
            case 80001341:
            case 80001395:
            case 80001396:
            case 80001493:
            case 80001494:
            case 80001495:
            case 80001496:
            case 80001497:
            case 80001498:
            case 80001499:
            case 80001500:
            case 80001501:
            case 80001502:
                return true;
        }
        return false;
    }

    public boolean isDoubleDice(int id) {
        switch (id) {
            case 5120012:
            case 5220014:
            case 5320007:
            case 35120014:
                return true;
        }
        return false;
    }

    public static class CancelEffectAction implements Runnable {

        private final SkillStatEffect effect;
        private final WeakReference<MapleCharacter> target;
        private final long startTime;

        public CancelEffectAction(final MapleCharacter target, final SkillStatEffect effect, final long startTime) {
            this.effect = effect;
            this.target = new WeakReference<MapleCharacter>(target);
            this.startTime = startTime;
        }

        @Override
        public void run() {
            final MapleCharacter realTarget = target.get();
            if (realTarget != null && realTarget.getClient() != null) {
                realTarget.cancelEffect(effect, false, startTime);
            }
        }
    }

    public final byte getLevel() {
        return (byte) effects.getStats("level");
    }

    public final short getIgnoreMob() {
        return (short) effects.getStats("ignoreMob");
    }

    public final short getER() {
        return (short) effects.getStats("er");
    }

    public final int getPercentHP() {
        return effects.getStats("mhpR");
    }

    public final int getDAMRate() {
        return effects.getStats("damR");
    }

    public int getWDEFRate() {
        return effects.getStats("pddR");
    }

    public final int getLevelToWatk() {
        return effects.getStats("lv2pdX");
    }

    public final int getPercentMP() {
        return effects.getStats("mmpR");
    }

    public final int getLevelToMatk() {
        return effects.getStats("lv2mdX");
    }

    public final int getMPConsumeEff() {
        return effects.getStats("mpConEff");
    }

    public final int getPercentAcc() {
        return effects.getStats("accR");
    }

    public final int getPassiveSpeed() {
        return effects.getStats("psdSpeed");
    }

    public final int getPercentAvoid() {
        return effects.getStats("evaR");
    }

    public final int getPassiveJump() {
        return effects.getStats("psdJump");
    }

    public final int getLevelToDamage() {
        return effects.getStats("lv2damX");
    }

    public final int getSummonTimeInc() {
        return effects.getStats("summonTimeR");
    }

    public final int getEXPLossRate() {
        return effects.getStats("expLossReduceR");
    }

    public final int getASRRate() {
        return effects.getStats("asrR");
    }

    public final int getTERRate() {
        return effects.getStats("terR");
    }

    public final int getBuffTimeRate() {
        return effects.getStats("bufftimeR");
    }

    public final int getSuddenDeathR() {
        return effects.getStats("suddenDeathR");
    }

    public final int getCooltimeReduceR() {
        return effects.getStats("coolTimeR");
    }

    public final int getMesoAcquisition() {
        return effects.getStats("mesoR");
    }

    public final int getHpToDamage() {
        return effects.getStats("mhp2damX");
    }

    public final int getMpToDamage() {
        return effects.getStats("mmp2damX");
    }

    public final int getStrX() {
        return effects.getStats("strX");
    }

    public final int getDexX() {
        return effects.getStats("dexX");
    }

    public final int getIntX() {
        return effects.getStats("intX");
    }

    public final int getLukX() {
        return effects.getStats("lukX");
    }

    public final int getMaxHpX() {
        return effects.getStats("mhpX");
    }

    public final int getMaxMpX() {
        return effects.getStats("mmpX");
    }

    public final int getMagicX() {
        return effects.getStats("madX");
    }

    public int getBossDamage() {
        return effects.getStats("bdR");
    }

    public final int getPrice() {
        return effects.getStats("price");
    }

    public final int getExtendPrice() {
        return effects.getStats("extendPrice");
    }

    public final int getPeriod() {
        return effects.getStats("period");
    }

    public final int getReqGuildLevel() {
        return effects.getStats("reqGuildLevel");
    }

    public final int getKillingPoint() {
        return effects.getStats("kp");
    }

    public final byte getEXPRate() {
        return (byte) effects.getStats("expR");
    }

    public int getMDEFRate() {
        return effects.getStats("mddR");
    }

    public final int getDOT() {
        return effects.getStats("dot");
    }

    public final int getTime() {
        return time;
    }

    public final int getSoulMPCon() {
        return effects.getStats("soulmpCon");
    }

    public final int getComboCon() {
        return effects.getStats("aranComboCon");
    }

    public final int getRange() {
        return effects.getStats("range");
    }

    public final boolean isMistEruption() {
        switch (sourceid) {
            case 2121003:
                return skill;
        }
        return false;
    }

    public final void applyPassive(final MapleCharacter applyto, final MapleMapObject obj) {
        if (makeChanceResult()) {
            switch (sourceid) {
                case 2100000:
                case 2200000:
                case 2300000:
                    if (obj == null || obj.getType() != MapleMapObjectType.MONSTER) {
                        return;
                    }
                    final MapleMonster mob = (MapleMonster) obj;
                    if (!mob.getStats().isBoss()) {
                        final int absorbMp = Math.min((int) (mob.getMobMaxMp() * (getX() / 100.0)), mob.getMp());
                        if (absorbMp > 0) {
                            mob.setMp(mob.getMp() - absorbMp);
                            applyto.getStat().setMp(applyto.getStat().getMp() + absorbMp);
                            applyto.getClient().getSession().writeAndFlush(MainPacketCreator.showSkillEffect(-1, applyto.getLevel(), sourceid, level, (byte) 0, 1, null, null));
                            applyto.getMap().broadcastMessage(applyto, MainPacketCreator.showSkillEffect(applyto.getId(), applyto.getLevel(), sourceid, level, (byte) 0, 1, null, null), false);
                        }
                    }
                    break;
            }
        }
    }

    public final int getStackSkill() {
        return stackskill;
    }

    public final int getMhpX() {
        return this.mhpX;
    }

    public final int getMhpR() {
        return this.mhpR;
    }

    public final int getLv2Mhp() {
        return this.lv2mhp;
    }

    public final int getMmpX() {
        return this.mmpX;
    }

    public final int getMmpR() {
        return this.mmpR;
    }

    public final int getLv2Mmp() {
        return this.lv2mmp;
    }

    public final double getHpR() {
        return this.hpR;
    }

    public final double getMpR() {
        return this.mpR;
    }

    public final int getStat(String pr) {
        return effects.getStats(pr);
    }

    public final void setStat(String pr, int value) {
        this.effects.setStats(pr, value);
    }

    public int dotDamage(int minattack) {
        float multiplier = getDOT() / 1000.0f;
        return (Randomizer.rand((int) (minattack * multiplier), (int) ((minattack * 1.5) * multiplier))) / 10;
    }

    public long getDamage(int minattack) {
        float multiplier = this.getDamage();
        return (Randomizer.rand((int) (minattack * multiplier), (int) ((minattack * 1.5) * multiplier))) / 10;
    }

    public static int parseEval(String data, int level) {
        String variables = "x";
        String dddd = data.replace(variables, String.valueOf(level));
        if (dddd.substring(0, 1).equals("-")) {
            if (dddd.substring(1, 2).equals("u") || dddd.substring(1, 2).equals("d")) {
                dddd = "n(" + dddd.substring(1, dddd.length()) + ")";
            } else {
                dddd = "n" + dddd.substring(1, dddd.length());
            }
        } else if (dddd.substring(0, 1).equals("=")) {
            dddd = dddd.substring(1, dddd.length());
        }
        return (int) (new CaltechEval(dddd).evaluate());
    }

    public final int getLocalDuraction(final MapleCharacter applyfrom) {
        return alchemistModifyVal(applyfrom, getTime(), false);
    }

    public static final boolean isFreezStackSkill(int skillid) {
        return skillid == 2201008 || skillid == 2211002 || skillid == 2211010 || skillid == 2201009;
    }

    public byte getSkillLevel() {
        return level;
    }
}
