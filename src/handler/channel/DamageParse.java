package handler.channel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import client.MapleCharacter;
import client.PlayerStats;
import client.items.Equip;
import client.items.IItem;
import client.items.Item;
import client.items.MapleInventoryType;
import client.items.MapleWeaponType;
import client.items.StructPotentialItem;
import client.object.RandomSkillEntry;
import client.skills.ISkill;
import client.skills.Skill;
import client.skills.SkillFactory;
import client.skills.SkillStatEffect;
import client.skills.StackedSkillEntry;
import client.stats.BuffStats;
import client.stats.MonsterStatus;
import client.stats.MonsterStatusEffect;
import client.stats.PlayerStat;
import community.MaplePartyCharacter;
import constants.GameConstants;
import constants.ServerConstants;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import launch.holder.MapleCoolDownValueHolder;
import packet.creators.MainPacketCreator;
import packet.creators.MatrixPacket;
import packet.creators.MobPacket;
import packet.creators.SecondaryStat;
import packet.creators.UIPacket;
import packet.opcode.RecvPacketOpcode;
import packet.skills.AngelicBusterSkill;
import packet.skills.KaiserSkill;
import packet.transfer.read.ReadingMaple;
import scripting.NPCScriptManager;
import server.items.ItemInformation;
import server.life.MapleMonster;
import server.life.MapleMonsterStats;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleMist;
import server.maps.MapleSummon;
import server.maps.MapleWorldMapItem;
import tools.ArrayMap;
import tools.AttackPair;
import tools.Pair;
import tools.Timer.EtcTimer;
import tools.Triple;
import tools.RandomStream.Randomizer;

public class DamageParse {

    public static void doHideAndSeek(MapleCharacter player, AttackInfo attack, boolean catched) {
        for (MapleCharacter attackedPlayers : player.getMap().getNearestPvpChar(player.getPosition(), 91, 40,
                attack.animation >= 0 ? true : false,
                Collections.unmodifiableCollection(player.getMap().getCharacters()))) {
            if (attackedPlayers.isAlive() && attackedPlayers.isCatched && player.isCatching) {
                CatchPlayer(player, attackedPlayers);
            }
        }
    }

    public static void CatchPlayer(MapleCharacter player, MapleCharacter catched) {

        player.getMap().broadcastMessage(MainPacketCreator.showGatherComplete(player.getId(), true));
        player.getMap().broadcastMessage(MainPacketCreator.serverNotice(6,
                "[술래잡기] 늑대 " + player.getName() + "님이 양 " + catched.getName() + "님을 잡으셨습니다."));
        catched.getStat().setHp(0, catched);
        catched.updateSingleStat(PlayerStat.HP, 0);
        boolean alliveCatched = false;
        for (MapleCharacter chr : player.getMap().getCharacters()) {
            if (chr.isAlive() && chr.isCatched) {
                alliveCatched = true;
                break;
            }
        }
        if (!alliveCatched) {
            player.getMap().stopCatch();
            for (MapleCharacter chr : player.getMap().getCharacters()) {
                chr.getStat().setHp(chr.getStat().getMaxHp(), chr);
                chr.updateSingleStat(PlayerStat.HP, chr.getStat().getHp());
                if (chr.isCatching) {
                    chr.changeMap(chr.getClient().getChannelServer().getMapFactory().getMap(109090200),
                            chr.getClient().getChannelServer().getMapFactory().getMap(109090200).getPortalSP().get(0));
                } else {
                    chr.changeMap(chr.getClient().getChannelServer().getMapFactory().getMap(109090101),
                            chr.getClient().getChannelServer().getMapFactory().getMap(109090101).getPortalSP().get(0));
                }
            }
            player.getMap().broadcastMessage(
                    MainPacketCreator.serverNotice(1, "양들이 모두 잡혀서 늑대가 승리하였습니다!\r\n모든 분들은 게임 보상맵으로 이동 됩니다."));
        }
    }

    public static void applyAttack(AttackInfo attack, ISkill theSkill, final MapleCharacter player, int attackCount,
            SkillStatEffect effect, AttackType attack_type) {
        MapleMap map = player.getMap();

        player.lastSkill = attack.skill;
//        for (MapleCoolDownValueHolder m : player.getAllCooldowns()) { //쿨탐제거
//            final int skil = m.skillId;
//            player.removeCooldown(skil);
//            player.getClient().send(MainPacketCreator.skillCooldown(skil, 0));
//        }
        if (GameConstants.isNightLord(player.getJob())) {
            IItem nk = player.getInventory(MapleInventoryType.USE).getItem(attack.slot);
            player.setKeyValue2("표창", nk.getItemId());
        }
        FinalAttackHandler.FianlAttack(attack, player);

        if(attack.skill == 400021071) {
            player.setbb(0);
            SkillFactory.getSkill(400021071).getEffect(player.getSkillLevel(400021071)).applyTo(player);
        }
        if (player.isEquilibrium()) {
            ISkill death = SkillFactory.getSkill(27111303);
            player.removeCooldown(death.getId());
            player.getClient().send(MainPacketCreator.skillCooldown(death.getId(), 0));
        }

        if (player.getMapId() == 109090300) {
            doHideAndSeek(player, attack, true);
            return;
        }
        if (attack.skill == 400041027 || attack.skill == 400041026 || attack.skill == 400041025) {
            player.addCooldown(400041025, System.currentTimeMillis(), 14000);
        }
        if (player.isEquippedSoulWeapon() && attack.skill == player.getEquippedSoulSkill()) {
            player.checkSoulState(true, player.getEquippedSoulSkill());
        }
        if (player.getBuffedValue(BuffStats.DarkSight) != null && player.getSkillLevel(4330001) > 0) { // 어드밴스드 다크사이트 은신해제
            if (!SkillFactory.getSkill(4330001).getEffect(player.getSkillLevel(4330001)).makeChanceResult()) {
                player.cancelEffectFromBuffStat(BuffStats.DarkSight, 4330001);
            }
        }

        if (GameConstants.isAran(player.getJob()) && attack.skill != 0) {
            player.useComboSkill(attack.skill);
        }
        if (attack.skill == 4341002) {
            SkillStatEffect eff = SkillFactory.getSkill(4341002).getEffect(player.getSkillLevel(4341002));
            eff.applyTo(player);
        }
        if ((attack.skill != 0) && (GameConstants.isBlaster(player.getJob()))) {
            player.giveBulletGauge(attack.skill, false);
        }
        if (player.isActiveBuffedValue(400011016) && !player.skillisCooling(400011020)) {
            player.getClient().send(MainPacketCreator.ToAttackkerinfo(400011020));
            player.addCooldown(400011020, System.currentTimeMillis(), 3000);
        }
        if (attack.skill > 0 && GameConstants.isIliume(player.getJob()) && !attack.allDamage.isEmpty()) {
            int summonOid = 0;
            for (MapleMapObject ob : player.getMap().getAllSummon()) {
                MapleSummon summon = (MapleSummon) ob;
                if (summon.getOwner() != null && summon.getOwner().getId() == player.getId()
                        && summon.getSkill() == 152101000) {
                    summonOid = summon.getObjectId();
                    break;
                }
            }
            player.getClient().sendPacket(MainPacketCreator.IliumeCrystal(player.getId(), summonOid));
        }
        if (attack.skill == 64001009 || attack.skill == 64001010 || attack.skill == 64001011) {
            player.getClient().sendPacket(MainPacketCreator.KadenaMove(-1, attack.skill, attack.plusPosition.x,
                    attack.plusPosition.y, player.isFacingLeft() ? 1 : 0, (short) player.getFH()));
            player.getMap().broadcastMessage(
                    player, MainPacketCreator.KadenaMove(player.getId(), attack.skill, attack.plusPosition.x,
                            attack.plusPosition.y, player.isFacingLeft() ? 1 : 0, (short) player.getFH()),
                    player.getPosition());
        }
        if (attack.skill == 400051025) { // ICBM
            SkillStatEffect subEffect = SkillFactory.getSkill(400051026).getEffect(player.getSkillLevel(400051024));

            player.getMap().spawnMist(new MapleMist(effect.calculateBoundingBox(attack.position, player.isFacingLeft()), player, subEffect, player.getSkillLevel(400051024), attack.position), subEffect.getStat("time"), false, false, false, false, false);
        }

        if (attack.skill == 31221052) {
            player.exeedCount += 5;
            if (player.exeedCount > 20) {
                player.exeedCount = 20;
            }
            SkillFactory.getSkill(30010230).getEffect(1).applyTo(player);
        }

        if (GameConstants.isArk(player.getJob())) {
            player.AddArkMable(attack.skill);

            if (attack.skill == 155001000) {
                //if (!player.isActiveBuffedValue(155001001)) {
                SkillStatEffect eff = SkillFactory.getSkill(155001001).getEffect(player.getSkillLevel(155001000));
                eff.applyTo(player);
                //}
            } else if (attack.skill == 155101002) {
                //if (!player.isActiveBuffedValue(155101003)) {
                SkillStatEffect eff = SkillFactory.getSkill(155101003).getEffect(player.getSkillLevel(155101002));
                eff.applyTo(player);
                //}
            } else if (attack.skill == 155111003) {
                //if (!player.isActiveBuffedValue(155111005)) {
                SkillStatEffect eff = SkillFactory.getSkill(155111005).getEffect(player.getSkillLevel(155111003));
                eff.applyTo(player);
                //}
            } else if (attack.skill == 155121003) {
                //if (!player.isActiveBuffedValue(155121005)) {
                SkillStatEffect eff = SkillFactory.getSkill(155121005).getEffect(player.getSkillLevel(155121003));
                eff.applyTo(player);
                //}
            }

            if (player.isActiveBuffedValue(155101008) && attack.skill != 155100009 && attack.allDamage.size() > 0 && player.isActiveBuffedValue(155101006)) {
                SkillStatEffect eff = SkillFactory.getSkill(155101008).getEffect(155101008);
                int max = eff.getZ();
                int count = player.getBuffedValue(BuffStats.IndieIliumStack, 400051036) != null ? 3 : 0;
                final List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 200000, Arrays.asList(MapleMapObjectType.MONSTER));
                final List<MapleMonster> monsters = new ArrayList<>();
                for (MapleMapObject o : objs) {
                    if (monsters.size() < max) {
                        monsters.add((MapleMonster) o);
                    } else {
                        break;
                    }
                }
                if (monsters.size() > 0) {
                    player.getMap().broadcastMessage(MainPacketCreator.ComingOfDeath(player.getId(), 155100009, monsters, count));
                    player.getClient().send(MainPacketCreator.resetActions(player));
                }
            }

            if (attack.skill == 155100009) {
                MapleMonster monster = player.getMap().getMonsterByOid(attack.allDamage.get(0).objectid);
                if (monster != null) {

                    if (player.arkatom < 12 && Randomizer.isSuccess(20)) {
                        player.arkatom++;
                        player.send(MainPacketCreator.CreateArKToken(player, 155111207, monster.getTruePosition()));
                        player.AtomPos.add(monster.getTruePosition());
                    }
                }
            }
        }

        if (GameConstants.isPassPinder(player.getJob()) && attack.skill != 0) {
            player.GivePassPinderShape(attack.skill);
            player.setRelicCount(player.getRelicCount() + 10);
        }
        if (attack.skill == 3321012) {
            player.GivePassPinderShape(attack.skill);
            player.setRelicCount(player.getRelicCount() - 110);
        }
        if (attack.skill == 3321016 || attack.skill == 3321018 || attack.skill == 3321021) {
            player.GivePassPinderShape(attack.skill);
            player.setRelicCount(player.getRelicCount() - 160);
        }
        if (attack.skill == 3321040) {
            player.GivePassPinderShape(attack.skill);
            player.setRelicCount(player.getRelicCount() - 90);
        }
        if (attack.skill == 400031035) {
            player.GivePassPinderShape(attack.skill);
            player.setRelicCount(player.getRelicCount() - 210);
        }
        switch (attack.skill) {
            case 3321035://에인션트 아스트라
            case 3321036://에인션트 아스트라(디스차지)
            case 3321037://에인션트 아스트라(디스차지)
            case 3321038://에인션트 아스트라(블래스트)
            case 3321039://에인션트 아스트라(블래스트)
            case 3321040://에인션트 아스트라(트랜지션)
                player.GivePassPinderShape(attack.skill);
                player.setRelicCount(player.getRelicCount() - 80);
                break;
        }
        if (attack.skill == 3321016) {
            player.getMap().broadcastMessage(MainPacketCreator.ToAttackkerinfo(3321017));
        } else if (attack.skill == 3321018) {
            player.getMap().broadcastMessage(MainPacketCreator.ToAttackkerinfo(3321019));
        } else if (attack.skill == 3321020) {
            player.getMap().broadcastMessage(MainPacketCreator.ToAttackkerinfo(3321021));
        }
        if (attack.skill == 3301008) {
            final List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 4000000, Arrays.asList(MapleMapObjectType.MONSTER));
            final List<MapleMonster> monsters = new ArrayList<>();
            int i = 0;
            for (MapleMapObject o : objs) {
                monsters.add((MapleMonster) o);
                i++;
                if (i == 2) {
                    break;
                }
            }
            player.getMap().broadcastMessage(MainPacketCreator.스플릿미스텔(player, monsters, 3301009));
        }
        if (attack.skill == 3321005) {
            if (player.shape == 1 && Randomizer.isSuccess(60)) {
                final List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 400000, Arrays.asList(MapleMapObjectType.MONSTER));
                final List<MapleMonster> monsters = new ArrayList<>();
                int i = 0;
                for (MapleMapObject o : objs) {
                    monsters.add((MapleMonster) o);
                    i++;
                    if (i == 1) {
                        monsters.add((MapleMonster) o);
                    }
                    if (player.getBuffedValue(BuffStats.IndieIliumStack, 3321034) == null) {
                        if (i == 2) {
                            break;
                        }
                    } else {
                        if (i == 3) {
                            break;
                        }
                    }
                }
                player.getMap().broadcastMessage(MainPacketCreator.에디셔널디스차지(player, monsters, 3300005));
            }
        }
        if (attack.skill == 3321036) {
            final List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 4000000, Arrays.asList(MapleMapObjectType.MONSTER));
            final List<MapleMonster> monsters = new ArrayList<>();
            int i = 0;
            for (MapleMapObject o : objs) {
                monsters.add((MapleMonster) o);
                i++;
                if (i == 2) {
                    break;
                }
            }
            player.getClient().send(MainPacketCreator.스플릿미스텔(player, monsters, 3321037));
        }

        if (attack.skill == 400041042) {
            List<Integer> subskillid = new ArrayList<>();
            subskillid.add(400041042);
            subskillid.add(400041043);
            player.getClient().send(MainPacketCreator.ConnectionSkill(attack.position, attack.skill, subskillid, player.isFacingLeft() ? 1 : 0));
        }

        if (attack.skill == 400051042) {
            if (player.HowlingGaleCount <= 0) {
                return;
            }

            SkillStatEffect eff = SkillFactory.getSkill(400051042).getEffect(player.getSkillLevel(400051042));

            if (player.HowlingGaleCount == 5) {
                player.lastHowlingGaleTime = System.currentTimeMillis();
            }
            player.HowlingGaleCount--;

            List<Triple<BuffStats, Integer, Boolean>> statups = new ArrayList<>();
            statups.add(new Triple<>(BuffStats.BigHugeGiganticCanonBall, player.HowlingGaleCount, false));

            player.send(MainPacketCreator.giveBuff(400051042, Integer.MAX_VALUE, statups, eff, player.getStackSkills(), 0,
                    player));
        }

        long fixeddmg;
        long totDamage = 0, totDamageToOneMonster = 0;
        PlayerStats stats = player.getStat();

        MapleMonster monster;
        MapleMonsterStats monsterstats;
        boolean isBoss = false;
        int mobs = 0;
        int last = 0;
        Point lastP = null;
        int lastE = 0;

        if (GameConstants.isAran(player.getJob()) && !player.isActiveBuffedValue(21110016)) {
            player.setCombo((short) (player.getCombo() + attack.allDamage.size()));
            player.updateCombo(player.getCombo(), System.currentTimeMillis());
        }
        for (final AttackPair mob : attack.allDamage) {
            long mdmg = 0;
            monster = map.getMonsterByOid(mob.objectid);
            if (monster != null && monster.getStats().isBoss()) {
                isBoss = true;
            }
            for (Pair<Long, Boolean> dmg : mob.attack) {
                mdmg += dmg.left;
                if (player.getKeyValue("qkdekdcnepa") != null) {
                    if (player.getKeyValue("qkdekdcnepa").equals("buy")) {
                        if (player.getAddDamage() > 0) {
                            long dam = player.getAddDamage() * (player.getDamageHit() + player.getDamageHit2()) / 100;
                            mdmg += dam;
                        }
                    }
                }
            }
            if (player.getKeyValue("qkdekdcnepa") == null) {
                if (player.getAddDamage() > 0) {
                    long dam = player.getAddDamage() * (player.getDamageHit() + player.getDamageHit2()) / 100;
                    mdmg += dam;
                }
            }
            if (monster != null && !monster.getStats().isBoss()) {
                if (monster.getMobMaxHp() - mdmg < 0) {
                    mobs++;
                    last = monster.getObjectId();
                    lastE = monster.getMobExp();
                    lastP = monster.getPosition();
                }
            }
        }

        if (attack.allDamage.isEmpty()) {
            if (attack.skill == 3211010 || attack.skill == 3111010 || attack.skill == 1100012) {
                player.send(MainPacketCreator.showSkillEffect(-1, player.getLevel(), attack.skill, attack.skillLevel, 0, 1, player.getPosition(), null, !(attack.animation >= 0)));
                player.getMap().broadcastMessage(player, MainPacketCreator.showSkillEffect(player.getId(), player.getLevel(), attack.skill, attack.skillLevel, 0, 1, player.getPosition(), null, !(attack.animation >= 0)), false);
            }
        }

        for (final AttackPair oned : attack.allDamage) {
            monster = map.getMonsterByOid(oned.objectid);
            if (monster != null) {
                totDamageToOneMonster = 0;
                monsterstats = monster.getStats();
                fixeddmg = monsterstats.getFixedDamage();
                if (player.haveItem(3994514, 1, false, true)) {
                    if (monster.getStats().getLevel() + 20 > player.getLevel()
                            && monster.getStats().getLevel() - 20 <= player.getLevel()) {
                        player.getStat().addSaintSaver(1);
                    }
                }
                if (player.isActiveBuffedValue(400041035) && attack.skill != 40041036) {
                    if (System.currentTimeMillis() - player.lastChainArtsFuryTime > 600) {
                        player.lastChainArtsFuryTime = System.currentTimeMillis();
                        player.send(MainPacketCreator.kadenaChainArtsFury(monster.getPosition().x,
                                monster.getPosition().y));
                    }
                }
                if (player.isActiveBuffedValue(400031028) && attack.skill != 3100010 && attack.skill != 400031029) {
                    for (int i = 0; i < 6; i++) {
                        player.getMap().broadcastMessage(MainPacketCreator.퀴버풀버스트(player, monster, 400031029));
                    }
                }
                if (player.isActiveBuffedValue(400031030) && attack.skill != 400031031 && attack.skill != 400031001 && attack.skill != 13111020 && attack.skill != 13121054 && attack.skill != 13101022 && attack.skill != 13110022 && attack.skill != 13120003 && attack.skill != 13111020) {
                    final List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 200000, Arrays.asList(MapleMapObjectType.MONSTER));
                    if (objs.size() > 0) {
                        for (int i = 0; i < 3; i++) {
                            player.getMap().broadcastMessage(MainPacketCreator.WindWall(player.getId(), objs.get(Randomizer.rand(0, objs.size() - 1)).getObjectId()));
                        }
                    }
                }
                if (attack.skill == 3211010 || attack.skill == 3111010 || attack.skill == 1100012) {
                    player.send(
                            MainPacketCreator.showSkillEffect(-1, player.getLevel(), attack.skill, attack.skillLevel,
                                    monster.getObjectId(), 1, monster.getPosition(), null, !(attack.animation >= 0)));
                    player.getMap().broadcastMessage(player,
                            MainPacketCreator.showSkillEffect(player.getId(), player.getLevel(), attack.skill,
                                    attack.skillLevel, monster.getObjectId(), 1, monster.getPosition(), null,
                                    !(attack.animation >= 0)),
                            false);
                }

                if (GameConstants.isKaiser(player.getJob())) {
                    if (!player.isFinalFiguration) {
                        player.getClient().send(KaiserSkill.giveMorphGauge(player.getStat().addMorph(5)));
                    }
                } else if (GameConstants.isLuminous(player.getJob())) {
                    Integer Gauge = player.getBuffedValue(BuffStats.Larkness);
                    if (player.getBuffedValue(BuffStats.Larkness) == null || player.getBuffedValue(BuffStats.Larkness) == -1) {
                        if (GameConstants.isDarkSkills(attack.skill)) {
                            player.getSunfireBuffedValue(20040216, attack.skill, Gauge);
                        } else {
                            player.getEclipseBuffedValue(20040217, attack.skill, Gauge);
                        }
                    } else if (player.getBuffedValue(BuffStats.Larkness) == 20040216) {
                        player.getSunfireBuffedValue(20040216, attack.skill, Gauge);
                    } else if (player.getBuffedValue(BuffStats.Larkness) == 20040217) {
                        player.getEclipseBuffedValue(20040217, attack.skill, Gauge);
                    }
                }

                Long eachd;
                for (Pair<Long, Boolean> eachde : oned.attack) {
                    eachd = eachde.left;
                    if (fixeddmg != -1) {
                        if (monsterstats.getOnlyNoramlAttack()) {
                            eachd = attack.skill != 0 ? 0 : fixeddmg;
                        } else {
                            eachd = fixeddmg;
                        }
                    } else if (monsterstats.getOnlyNoramlAttack()) {
                        eachd = attack.skill != 0 ? 0 : eachd;
                    }
                    totDamageToOneMonster += (long) eachd;
                    if (player.getKeyValue("qkdekdcnepa") != null) {
                        if (player.getKeyValue("qkdekdcnepa").equals("buy")) {
                            if (player.getAddDamage() > 0) {
                                long dam = player.getAddDamage() * (player.getDamageHit() + player.getDamageHit2()) / 100;
                                player.send(UIPacket.detailShowInfo("현재 " + player.getName() + "님의 < " + player.getAddDamage() + " * " + (player.getDamageHit() + player.getDamageHit2()) + " %> 총데미지 < " + dam + " > ", true));
                                monster.damage(player, dam, true);
                            }
                        }
                    }
                }
                if (player.isActiveBuffedValue(400031000)) {
                    player.getMap().broadcastMessage(MatrixPacket.GuidedArrow(player, monster.getObjectId()));
                }

                if (player.getKeyValue("qkdekdcnepa") == null) {
                    if (player.getAddDamage() > 0) {
                        long dam = player.getAddDamage() * (player.getDamageHit() + player.getDamageHit2()) / 100;
                        player.send(UIPacket.detailShowInfo("현재 " + player.getName() + "님의 < " + player.getAddDamage() + " * " + (player.getDamageHit() + player.getDamageHit2()) + " %> 총데미지 < " + dam + " > ", true));
                        monster.damage(player, dam, true);
                    }
                }
                totDamage += totDamageToOneMonster;
                player.checkMonsterAggro(monster);

                MonsterStatusEffect monsterStatusEffect;

                if (attack.skill == 400011058 || attack.skill == 400011059) {
                    SkillStatEffect a = SkillFactory.getSkill(400011060).getEffect(25);
                    player.getMap().spawnMist(new MapleMist(effect.calculateBoundingBox(monster.getPosition(), player.isFacingLeft()), player, a, 25, attack.position), 2000, false, false, false, false, false);
                }

                if (attack.skill == 400051011) {
                    player.cancelEffectFromBuffStat(BuffStats.EnergyBurst);
                }

                if (attack.assist != 1) {
                    switch (attack.skill) {
                        case 101001200:
                        case 101000200:
                        case 101000201:
                        case 101101200:
                        case 101100200:
                        case 101100201:
                        case 101111200:
                        case 101110200:
                        case 101110202:
                        case 101110203:
                        case 101120201:
                        case 101120202:
                        case 101120204:
                            if (player.isActiveBuffedValue(100000277)) {
                                player.RapidTimeCount = 0;
                                player.cancelEffectFromBuffStat(BuffStats.TimeFastBBuff);
                            } else if (player.isActiveBuffedValue(100000276)) {
                                if (player.RapidTimeCount < 10) {
                                    player.RapidTimeCount++;
                                }
                            } else {
                                player.RapidTimeCount = 1;
                            }
                            SkillStatEffect eff = SkillFactory.getSkill(100000276).getEffect(10);
                            eff.applyTo(player);
                            break;
                        case 101001100:
                        case 101000100:
                        case 101000101:
                        case 101101100:
                        case 101100100:
                        case 101100101:
                        case 101111100:
                        case 101110100:
                        case 101110102:
                        case 101110103:
                        case 101120101:
                        case 101120102:
                        case 101120104:
                            if (player.isActiveBuffedValue(100000276)) {
                                player.RapidTimeCount = 0;
                                player.cancelEffectFromBuffStat(BuffStats.TimeFastABuff);
                            } else if (player.isActiveBuffedValue(100000277)) {
                                if (player.RapidTimeCount < 10) {
                                    player.RapidTimeCount++;
                                }
                            } else {
                                player.RapidTimeCount = 1;
                            }
                            SkillStatEffect eff2 = SkillFactory.getSkill(100000277).getEffect(10);
                            eff2.applyTo(player);
                            break;
                    }
                }

                if (player.getSkillLevel(37110009) > 0) {
                    SkillFactory.getSkill(37110009).getEffect(player.getSkillLevel(37110009)).applyTo(player);
                }

                if (player.getSkillLevel(3210013) > 0 && (player.getJob() == 321 || player.getJob() == 322)) {
                    SkillStatEffect sse = SkillFactory.getSkill(3210013).getEffect(player.getSkillLevel(3210013));
                    sse.applyToDamageReversing(player, totDamageToOneMonster);
                }


                /*
				 * if (player.getSkillLevel(36110003) > 0) { ISkill skill =
				 * SkillFactory.getSkill(36110003); SkillStatEffect effs =
				 * skill.getEffect(player.getSkillLevel(skill)); if (player.getLastCombo() +
				 * 5000 < System.currentTimeMillis()) { player.acaneAim = 0;
				 * player.clearDamageMeters(); } if (effs.makeChanceResult()) {
				 * player.setLastCombo(System.currentTimeMillis()); if (player.acaneAim < 3) {
				 * player.acaneAim++; } Map<MonsterStatus, Integer> stat = new
				 * ArrayMap<MonsterStatus, Integer>(); stat.put(MonsterStatus.DARKNESS,
				 * effs.getX()); stat.put(MonsterStatus.TRIANGLE_FOMATION, player.acaneAim);
				 * stat.put(MonsterStatus.TRIANGLE_FOMATION_S, player.acaneAim);
				 * monsterStatusEffect = new MonsterStatusEffect(stat, skill, null, false);
				 * monster.applyStatus(player, monsterStatusEffect, false, effs.getDuration(),
				 * true); } }
                 */
 /* 픽파킷 */
                if (player.getBuffedValue(BuffStats.PickPocket) != null) {
                    switch (attack.skill / 10000) {
                        case 0:
                        case 400:
                        case 420:
                        case 421:
                        case 422:
                            handlePickPocket(player, monster, oned);
                            break;
                    }
                }

                if (totDamageToOneMonster > 0) {

                    if (attack.skill == 4201004) { // Steal
                        monster.handleSteal(player);
                    }
                    if (player.getSkillLevel(30010111) > 0) { // 데쓰 커스
                        SkillStatEffect effs = SkillFactory.getSkill(30010111).getEffect(1);
                        if (effs.makeChanceResult()) {
                            if (!monster.getStats().isBoss() && player.getMapId() != 900000000
                                    && player.getMapId() != 109040004) { // 보스가 아니어야함.
                                totDamageToOneMonster = 999999999;
                                player.addHP((int) (player.getStat().getCurrentMaxHp() * (effs.getX() / 100.0D))); // 체력
                                // 회복
                            }
                        }
                        if (monster.getHp() <= totDamageToOneMonster) {
                            player.handleForceGain(monster.getObjectId(), 30010111, 5);
                        }
                    }
                    if (player.isActiveBuffedValue(21101005)) {
                        SkillStatEffect eff = player.getBuffedSkillEffect(BuffStats.ComboDrain, 21101005);
                        player.addHP((int) (player.getStat().getCurrentMaxHp() * (eff.getX() / 100.0D)));
                    }
                    if (attack.skill == 31211001) {
                        player.addHP((int) (player.getStat().getCurrentMaxHp() * (effect.getY() / 100.0D)));
                    }

                    if (player.isActiveBuffedValue(400051010)) {
                        if (System.currentTimeMillis() - player.lastChainArtsFuryTime > 2000) {
                            player.lastChainArtsFuryTime = System.currentTimeMillis();
                            Skill skill = SkillFactory.getSkill1(400051010);
                            SkillStatEffect eff = null;
                            List<Pair<Integer, Integer>> skillList = new ArrayList<>();
                            List<RandomSkillEntry> rse = skill.getRSE();
                            for (RandomSkillEntry info : rse) {
                                if (Randomizer.isSuccess(info.getProb())) {
                                    if (info.getSkillList().size() > 0) {
                                        skillList.addAll(info.getSkillList());
                                    } else {
                                        skillList.add(new Pair<>(info.getSkillId(), 0));
                                    }
                                    player.getClient().send(MainPacketCreator.AutoAttack(skillList));
                                    break;
                                }
                            }
                        }
                    }

                    if (attack.skill == 27121303) {
                        if (!monster.getStats().isBoss()) {
                            totDamageToOneMonster = 999999999;
                        }
                    }
                    if (GameConstants.isLuminous(player.getJob())) {
                        SkillStatEffect dkeffect = player.getBuffedSkillEffect(BuffStats.StackBuff, 27121005);
                        if (dkeffect != null) {
                            if (player.getLastCombo() + 5000 < System.currentTimeMillis()) {
                                player.acaneAim = 0;
                                player.clearDamageMeters();
                            }

                            if (dkeffect.makeChanceResult()) {
                                player.setLastCombo(System.currentTimeMillis());
                                if (player.acaneAim < 15) {
                                    player.acaneAim++;
                                    dkeffect.applyTo(player);
                                }
                            }
                        }
                    }
                    if (player.getJob() >= 310 && player.getJob() <= 312 && attack.skill != 3100010) {
                        boolean modeChange = false;
                        if (player.quiver) {
                            if (player.quivermode == 1) {
                                if (player.quivercount[0] > 1) {// 흡혈
                                    if (Randomizer.nextInt(100) < 50) {
                                        if (!player.isActiveBuffedValue(3121016)) {
                                            player.quivercount[0] -= 1;
                                        }
                                        player.refreshMaxHp();
                                    }
                                } else if (player.quivercount[0] == 1) {// 흡혈끝
                                    player.quivercount[0] += (9 + player.getSkillLevel(3121016));
                                    player.quivermode = 2;
                                    modeChange = true;
                                }
                            } else if (player.quivermode == 2) {
                                if (player.quivercount[1] > 1) {// 독
                                    if (Randomizer.nextInt(20) < 100) {
                                        if (!player.isActiveBuffedValue(3121016)) {
                                            player.quivercount[1] -= 1;
                                        }
                                        monster.applyStatus(player,
                                                new MonsterStatusEffect(
                                                        Collections.singletonMap(MonsterStatus.POISON,
                                                                (int) Randomizer.rand(100, 500)),
                                                        SkillFactory.getSkill(3101009),
                                                        player.getSkillLevel(attack.skill), null, false),
                                                (long) 10 * 1000);
                                    }
                                } else if (player.quivercount[1] == 1) {// 독끝
                                    player.quivercount[1] += (9 + player.getSkillLevel(3121016));
                                    player.quivermode = 3;
                                    modeChange = true;
                                }
                            } else if (player.quivermode == 3) {
                                if (player.quivercount[2] > 1) {// 마법 화살
                                    if (player.getBuffedSkillEffect(BuffStats.QuiverCatridge).makeChanceResult()) {
                                        if (!player.isActiveBuffedValue(3121016)) {
                                            player.quivercount[2] -= 1;
                                        }
                                        SkillFactory.getSkill(3100010).getEffect(1).applyAtom(player, 10);
                                    }
                                } else if (player.quivercount[2] == 1) {// 마법 화살끝
                                    player.quivercount[2] += (9 + (player.getSkillLevel(3121016) * 3));
                                    player.quivermode = 1;
                                    modeChange = true;
                                }
                            }
                            if (modeChange) {
                                player.getClient().getSession().writeAndFlush(UIPacket.showWZEffect(
                                        "Skill/310.img/skill/3101009/mode/" + (player.quivermode - 1), 1));
                                player.getClient().getSession()
                                        .writeAndFlush(UIPacket.showWZEffect(
                                                "Skill/310.img/skill/3101009/modeStatus/" + (player.quivermode - 1)
                                                + "/" + (player.quivercount[player.quivermode - 1] * 1),
                                                1));
                                player.getMap().broadcastMessage(player,
                                        UIPacket.broadcastWZEffect(player.getId(),
                                                "Skill/310.img/skill/3101009/mode/" + (player.quivermode - 1), 1),
                                        player.getPosition());
                                player.getMap().broadcastMessage(player,
                                        UIPacket.broadcastWZEffect(player.getId(),
                                                "Skill/310.img/skill/3101009/modeStatus/" + (player.quivermode - 1)
                                                + "/" + (player.quivercount[player.quivermode - 1] * 1),
                                                1),
                                        player.getPosition());
                            }
                            player.getBuffedSkillEffect(BuffStats.QuiverCatridge).applyToQuiverCatridge(player,
                                    (player.quivercount[0] * 10000) + (player.quivercount[1] * 100)
                                    + (player.quivercount[2] * 1));
                        }
                    }

                    if (player.isActiveBuffedValue(65121011)) {
                        SkillStatEffect eff = player.getBuffedSkillEffect(BuffStats.AngelicBursterSoulSeeker, 65121011);
                        if (eff.makeChanceResult() && attack.skill != 65111007 && attack.skill != 65120011) {
                            eff.applyAtom(player, 3);
                        }
                    }

                    if (player.getBuffedValue(BuffStats.NightWalkerBat, 14001027) != null) {
                        SkillStatEffect eff = SkillFactory.getSkill(14000028).getEffect(1);
                        SkillStatEffect b_eff = SkillFactory.getSkill(14000027).getEffect(player.getSkillLevel(14001027));
                        int skillid = 14000027;
                        int BatLimit = 2;
                        int mobCount = 3;
                        int Chance = b_eff.getProb();
                        int skillids[] = {14100027, 14110029, 14120008};
                        for (int skill : skillids) {
                            if (player.getSkillLevel(skill) > 0) {
                                if (skill != 14110029) {
                                    Chance += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill)).getProb();
                                }
                                BatLimit += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill)).getY();
                                mobCount += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill)).getMobCount();
                                skillid = skill;
                            }
                        }
                        SkillStatEffect batskill = SkillFactory.getSkill(skillid).getEffect(player.getSkillLevel(skillid));
                        List<MapleSummon> summons = new ArrayList<MapleSummon>();
                        for (Pair<Integer, MapleSummon> summon : player.getSummons().values()) {
                            if (summon.getRight().getSkill() == 14000027
                                    || summon.getRight().getSkill() == 14100027
                                    || summon.getRight().getSkill() == 14110029
                                    || summon.getRight().getSkill() == 14120008) {
                                summons.add(summon.getRight());
                            }
                        }
                        player.setShadowBatMobCount(mobCount);
                        final List<MapleMapObject> mobs_ = player.getMap().getMapObjectsInRange(player.getPosition(), 800 * 800, Arrays.asList(MapleMapObjectType.MONSTER));
                        final List<Integer> mobList = new LinkedList<Integer>();
                        int i = 0;
                        for (final MapleMapObject mo : mobs_) {
                            MapleMonster mons = (MapleMonster) mo;
                            mobList.add(mons.getObjectId());
                            break;
                        }
                        if (summons.size() >= 1) {
                            if (mobList.size() > 0) {
                                if (Randomizer.nextInt(100) < Chance) {
                                    player.getMap().broadcastMessage(MainPacketCreator.giveShadowBat(player.getId(), mobList.get(0), skillid, player.getPosition(), 15));
                                    summons.get(Randomizer.nextInt(summons.size())).removeSummon(player.getMap());
                                }
                            }
                        }
                        if (GameConstants.isNightWalkerThrowingSkill(attack.skill)) {
                            if (summons.size() < 5) {
                                player.setNightWalkerAttackCount(player.getNightWalkerAttackCount() + 1);
                                if (player.getNightWalkerAttackCount() >= 3) {
                                    batskill.setStat("time", b_eff.getTime());
                                    batskill.applyTo(player, player.getPosition());
                                    player.setNightWalkerAttackCount(0);
                                }
                            }
                        }
                    }

                    if (GameConstants.isKinesis(player.getJob()) && attack.skill != 142110011) {
                        short skillLevel = (short) player.getSkillLevel(142110011);
                        if (skillLevel > 0) {
                            SkillStatEffect eff = SkillFactory.getSkill(142110011).getEffect(skillLevel);
                            if (eff.makeChanceResult()) {
                                eff.applyAtom(player, 22);
                            }
                        }
                    }
                    if (player.getJob() >= 1410 && player.getJob() <= 1412) {
                        int[] skillid = {14100026, 14110028, 14120007};
                        int idx = player.getJob() == 1410 ? 0 : player.getJob() == 1411 ? 1 : 2;
                        monster.applyStatus(player,
                                new MonsterStatusEffect(
                                        Collections.singletonMap(MonsterStatus.POISON, (int) Randomizer.rand(100, 500)),
                                        SkillFactory.getSkill(skillid[idx]), player.getSkillLevel(skillid[idx]), null,
                                        false),
                                (long) 10 * 1000);
                    }

                    if (player.getJob() == 2312 && player.getBuffedValue(BuffStats.SUMMON, 400031007) != null) {
                        if (System.currentTimeMillis() - player.lastChainArtsFuryTime > 10000) {
                            player.lastChainArtsFuryTime = System.currentTimeMillis();

                            player.getClient().send(MainPacketCreator.ToAttackkerinfo(400031011));
                        }
                    }
                    if (player.isActiveBuffedValue(14001027)) {
                        SkillStatEffect eff = SkillFactory.getSkill(14000028).getEffect(1);
                        SkillStatEffect b_eff = SkillFactory.getSkill(14000027)
                                .getEffect(player.getSkillLevel(14001027));
                        int skillid = 14000027;
                        int BatLimit = 1;
                        int Chance = b_eff.getProb();
                        int skillids[] = {14100027, 14110029, 14120008};
                        for (int skill : skillids) {
                            if (player.getSkillLevel(skill) > 0) {
                                Chance += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill)).getProb();
                                BatLimit += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill)).getY();
                                skillid = skill;
                            }
                        }
                        SkillStatEffect batskill = SkillFactory.getSkill(skillid)
                                .getEffect(player.getSkillLevel(skillid));
                        List<MapleSummon> summons = new ArrayList<MapleSummon>();
                        for (Pair<Integer, MapleSummon> summon : player.getSummons().values()) {
                            if (summon.getRight().getSkill() == skillid) {
                                summons.add(summon.getRight());
                            }
                        }
                        if (summons.size() == BatLimit) {
                            if (Randomizer.nextInt(100) < Chance) {
                                eff.applyAtom(player, 15);
                                summons.get(Randomizer.nextInt(summons.size())).removeSummon(player.getMap());
                            }
                        } else if (summons.size() < BatLimit) {
                            batskill.setStat("time", b_eff.getTime());
                            batskill.applyTo(player, player.getPosition());
                        }
                    }
                    if (player.getJob() == 222 || player.getJob() == 212 || player.getJob() == 232) { // 아케인 에임
                        if (attack.skill != 2221012) {
                            int[] skills = {2120010, 2220010, 2320011};
                            for (int d : skills) {
                                if (player.getSkillLevel(d) > 0) {
                                    if (player.getLastCombo() + 5000 < System.currentTimeMillis()) {
                                        player.acaneAim = 0;
                                        player.clearDamageMeters();
                                    }
                                    if (SkillFactory.getSkill(d).getEffect(player.getSkillLevel(d)).makeChanceResult()) {
                                        player.setLastCombo(System.currentTimeMillis());
                                        if (player.acaneAim < 5) {
                                            player.acaneAim++;
                                        }
                                        SkillFactory.getSkill(d).getEffect(player.getSkillLevel(d)).applyTo(player);
                                    }
                                }
                            }
                        }
                    }
                    if (player.getSkillLevel(4120011) > 0) {
                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, (int) Randomizer.rand(100, 500)), SkillFactory.getSkill(4120011), player.getSkillLevel(4120011), null, false), (long) 10 * 1000);
                    }

                    if (player.getSkillLevel(4220011) > 0) {
                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, (int) Randomizer.rand(100, 500)), SkillFactory.getSkill(4220011), player.getSkillLevel(4220011), null, false), (long) 10 * 1000);
                    }
                    if (player.isActiveBuffedValue(12101024)) {
                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, (int) Randomizer.rand(100, 500)), SkillFactory.getSkill(12101024), player.getSkillLevel(12101024), null, false), (long) 10 * 1000);
                        if (player.getSkillLevel(400021042) > 0 && player.skillisCooling(400021042) == false && attack.skill != 400021045) {
                            if (player.FlameDischarge < 6) {
                                player.FlameDischarge++;
                            }
                            player.getClient().send(MainPacketCreator.FlameDischarge((byte) player.FlameDischarge, 10000));
                        }
                    }
                    if (player.isActiveBuffedValue(4121054)) {
                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, (int) Randomizer.rand(100, 500)), SkillFactory.getSkill(4121054), player.getSkillLevel(4121054), null, false), (long) 10 * 1000);
                    }

                    if (player.isActiveBuffedValue(32121018)) {
                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.WDEF, -SkillFactory.getSkill(32121018).getEffect(player.getSkillLevel(32121018)).getX()), SkillFactory.getSkill(32121018), player.getSkillLevel(32121018), null, false), (long) 10 * 1000);
                    }

                    if (attack.skill == 142121031) {
                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.STUN, 1), SkillFactory.getSkill(142121031), player.getSkillLevel(142121031), null, false), (long) 10 * 1000);
                    }
                    if (attack.skill == 31101003) {
                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.STUN, 1), SkillFactory.getSkill(31101003), player.getSkillLevel(31101003), null, false), (long) 10 * 1000);
                    }
                    if (player.getSkillLevel(4330007) > 0) { // 바이탈 스틸
                        SkillStatEffect effects = SkillFactory.getSkill(4330007)
                                .getEffect(player.getSkillLevel(4330007));
                        if (effects.makeChanceResult()) {
                            int fhp = (int) (totDamageToOneMonster / effects.getX());
                            int shp = (int) (player.getStat().getCurrentMaxHp() / 100.0D) * 20;
                            if (fhp > shp) { // 자신의 HP의 15%이상은 흡수 불가
                                fhp = shp;
                            }
                            if (fhp < 0) {
                                fhp *= -1;
                            }
                            if (player.getStat().getCurrentMaxHp() <= player.getStat().getHp()) {
                                fhp = 0;
                            }
                            player.addHP(fhp);
                        }
                    }

                    if (player.getSummonCount(400051011) > 0) {
                        if (attack.skill != 65111007 && attack.skill != 65120011 && attack.skill != 400051011) {
                            MapleSummon summon = (MapleSummon) player.getMap().getSummonObjects(player, 400051011)
                                    .get(0);

                            player.send(MainPacketCreator.angelicBusterEnergyBurst(player, summon.getPosition()));
                        }
                    }

                    if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11) != null) { //잠재
                        Equip equip = (Equip) player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                        if (equip.getState() > 1) {
                            int[] potentials = {equip.getPotential1(), equip.getPotential2(), equip.getPotential3(), equip.getPotential4(), equip.getPotential5(), equip.getPotential6()};
                            ItemInformation ii = ItemInformation.getInstance();
                            StructPotentialItem pot = null;
                            for (int i : potentials) {
                                if (i > 0) {
                                    if (ii.getPotentialInfo(i).get(ii.getReqLevel(equip.getItemId() / 10)) != null) {
                                        pot = ii.getPotentialInfo(i).get(ii.getReqLevel(equip.getItemId() / 10));
                                        if (pot != null) {
                                            if (Randomizer.isSuccess(pot.prop)) {
                                                switch (i) {
                                                    case 10201:
                                                    case 20201:
                                                        player.addHP(pot.HP);
                                                        break;
                                                    case 10202:
                                                    case 20206:
                                                        player.addMP(pot.MP);
                                                        break;
                                                    case 10221:
                                                        //시간을 잘몰라서 5초라 생각하고 구현
                                                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, (int) pot.level), SkillFactory.getSkill(90001003), player.getSkillLevel(attack.skill), null, false), (long) 5 * 1000);
                                                        break;
                                                    case 10226:
                                                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.STUN, (int) pot.level), SkillFactory.getSkill(90001001), player.getSkillLevel(attack.skill), null, false), (long) 5 * 1000);
                                                        break;
                                                    case 10231:
                                                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.SPEED, (int) pot.level), SkillFactory.getSkill(90001002), player.getSkillLevel(attack.skill), null, false), (long) 5 * 1000);
                                                        break;
                                                    case 10236:
                                                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.DARKNESS, (int) pot.level), SkillFactory.getSkill(90001004), player.getSkillLevel(attack.skill), null, false), (long) 5 * 1000);
                                                        break;
                                                    case 10241:
                                                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.FREEZE, (int) pot.level), SkillFactory.getSkill(90001006), player.getSkillLevel(attack.skill), null, false), (long) 5 * 1000);
                                                        break;
                                                    case 10246:
                                                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.SEAL, (int) pot.level), SkillFactory.getSkill(90001005), player.getSkillLevel(attack.skill), null, false), (long) 5 * 1000);
                                                        break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (player.getJob() == 422) {
                        int critical = player.acaneAim;
                        if (attack.skill > 0) {
                            player.send(packet.creators.MainPacketCreator.CriticalGrowing(critical));
                        }
                        player.acaneAim++;
                        if (player.acaneAim > 24) {
                            player.acaneAim = 24;
                        }
                    }
                    if (player.getJob() == 422) {
                        if (player.getBuffedValue(BuffStats.IndiePAD, 4221013) != null) {
                            if (attack.skill > 0) {
                                player.send(packet.creators.MainPacketCreator.KillingPoint(player.KillingPoint));
                                player.KillingPoint++;
                                if (player.KillingPoint > 5) {
                                    player.KillingPoint = 5;
                                }
                            }
                        }
                    }
                    if (SkillStatEffect.isFreezStackSkill(attack.skill)) {
                        monster.setFreezeStack(monster.getFreezeStack() + 1);
                        monster.applyStatus(player,
                                new MonsterStatusEffect(
                                        Collections.singletonMap(MonsterStatus.SPEED, monster.getFreezeStack()),
                                        SkillFactory.getSkill(attack.skill), attack.skillLevel, null, false),
                                effect.getTime());
                    }
                    if (player.getBuffedValue(BuffStats.SpiritLink, 31121002) != null) { // 뱀피릭 터치
                        SkillStatEffect effs = SkillFactory.getSkill(31121002)
                                .getEffect(player.getSkillLevel(31121002));
                        if (player.getParty() == null) {
                            int recover = (int) (totDamageToOneMonster * (effs.getX() / 100.0D));
                            Math.min(recover, (int) (player.getStat().getCurrentMaxHp() * 0.03D));
                            player.addHP(recover);
                        } else {
                            for (MaplePartyCharacter hpc : player.getParty().getMembers()) {
                                if (hpc.isOnline() && hpc.getMapid() == player.getMapId()) {
                                    MapleCharacter pchar = player.getClient().getChannelServer().getPlayerStorage()
                                            .getCharacterById(hpc.getId());
                                    if (pchar != null) {
                                        int recover = (int) (totDamageToOneMonster * (effs.getX() / 100.0D));
                                        Math.min(recover, (int) (pchar.getStat().getCurrentMaxHp() * 0.03D));
                                        pchar.addHP(recover);
                                    }
                                }
                            }
                        }
                    }
                    /* 엔젤릭버스터 스킬 비활성 */
                    if (GameConstants.isAngelicBuster(player.getJob())) {
                        switch (attack.skill) {
                            case 65001100: // 버블 스타
                            case 65101100: // 스팅 익스플로전
                            case 65111101: // 폴링 스타
                            case 65121100: // 프라이멀 로어
                            case 65121101: // 트리니티
                                player.getClient().send(AngelicBusterSkill.lockSkill(attack.skill));
                                player.getClient().send(MainPacketCreator.resetActions(player));
                                break;
                        }
                    }

                    if (player.isActiveBuffedValue(400041008) && GameConstants.isDarkAtackSkill(attack.skill)) {
                        SkillStatEffect a = SkillFactory.getSkill(400040008).getEffect(player.getSkillLevel(400040008));
                        player.getMap().spawnMist(new MapleMist(a.calculateBoundingBox(monster.getPosition(), player.isFacingLeft()), player, a, player.getSkillLevel(400040008), player.getPosition()), 3000, false, false, false, false, false);

                        if (System.currentTimeMillis() - player.lastChainArtsFuryTime > 3000) {
                            player.lastChainArtsFuryTime = System.currentTimeMillis();
                            player.getMap().broadcastMessage(MainPacketCreator.NightWalkerShadowSpearBig(monster.getTruePosition().x, monster.getTruePosition().y));
                        }
                    }

                    if (GameConstants.isAngelicBuster(player.getJob())) {
                        int prop = SkillFactory.getSkill(attack.skill).getEffect(attack.skillLevel).getOnActive();
                        if (player.getSkillLevel(65000003) > 0) {
                            prop += 30;
                        }
                        if (player.getSkillLevel(65100005) > 0) {
                            if (player.Recharge != 10) {
                                player.Recharge++;
                            } else {
                                player.Recharge = 0;
                                prop = 100;
                            }
                        }
                        if (Randomizer.isSuccess(prop)) {
                            player.getClient().send(AngelicBusterSkill.showRechargeEffect());
                            player.getClient().send(AngelicBusterSkill.unlockSkill());
                            player.RechargeFill = 0;
                        } else {
                            if (player.getSkillLevel(65110006) > 0) {
                                if (player.getSkillLevel(65120006) < 0) {
                                    if (player.RechargeFill != 2) {
                                        player.RechargeFill++;
                                    } else {
                                        player.Recharge = 10;
                                        player.RechargeFill = 0;
                                    }
                                } else if (player.getSkillLevel(65120006) > 0) {
                                    if (player.RechargeFill != 1) {
                                        player.RechargeFill++;
                                    } else {
                                        player.Recharge = 10;
                                        player.RechargeFill = 0;
                                    }
                                }
                            }
                        }
                    }

                    if (attack.skill != 400031020 && player.getJob() == 312) {
                        if (attack.skill != 0) {
                            player.InhumanSpeed(1);
                        }
                    }
                    if (GameConstants.isDemonSlayer(player.getJob())) { // 포스 흡수
                        player.handleForceGain(monster.getObjectId(), attack.skill);
                    }
                    if (player.getSkillLevel(30010112) > 0) { // 데몬스퓨리 보스에서 포스 흡수.
                        if (monster.getStats().isBoss()) {
                            stats.addForce(SkillFactory.getSkill(30010112).getEffect(1).getX());
                        }
                    }
                    if (GameConstants.isPhantom(player.getJob())) {
                        if (attack.skill != 24120002 && attack.skill != 24100003 && attack.skill != 400041010) {
                            if (player.getSkillLevel(24120002) > 0) { // 느와르 카르트
                                if (SkillFactory.getSkill(24120002).getEffect(player.getSkillLevel(24120002))
                                        .makeChanceResult()) {
                                    player.addCardStack(1);
                                    int cardid = player.addCardStackRunningId();
                                    player.getMap().broadcastMessage(player, MainPacketCreator
                                            .absorbingCardStack(player.getId(), cardid, 24120002, false, 1), true);

                                }
                            } else if (player.getSkillLevel(24100003) > 0) { // 블랑 카르트
                                if (SkillFactory.getSkill(24100003).getEffect(player.getSkillLevel(24100003))
                                        .makeChanceResult()) {
                                    player.addCardStack(1);
                                    int cardid = player.addCardStackRunningId();
                                    player.getMap().broadcastMessage(player, MainPacketCreator
                                            .absorbingCardStack(player.getId(), cardid, 24100003, false, 1), true);
                                }
                            }
                        }
                    }
                }

                if (totDamageToOneMonster > 0) { // 트라이플링 윔
                    if (GameConstants.isWindBreaker(player.getJob()) && player.isActiveBuffedValue(13101022)) {
                        int skillid = 0;
                        if (player.getSkillLevel(SkillFactory.getSkill(13120003)) > 0) {
                            skillid = 13120003;
                        } else if (player.getSkillLevel(SkillFactory.getSkill(13110022)) > 0) {
                            skillid = 13110022;
                        } else if (player.getSkillLevel(SkillFactory.getSkill(13101022)) > 0) {
                            skillid = 13100022;
                        }
                        if (skillid != 0) {
                            SkillStatEffect eff = SkillFactory.getSkill(skillid).getEffect(1);
                            if (eff != null) {
                                if (eff.makeChanceResult()) {
                                    eff.applyAtom(player, 7);
                                }
                            }
                        }
                    }
                }

                if (player.isActiveBuffedValue(13121054)) {
                    if (player.getBuffedSkillEffect(BuffStats.StormBringer, 13121054).makeChanceResult()) {
                        player.getMap().broadcastMessage(
                                MainPacketCreator.StormBlinger(player.getId(), monster.getObjectId()));
                    }
                }

                if (attack.skill == 1321012) { // 다크 임페일
                    int y = 0;
                    int z = 0;
                    if (SkillFactory.getSkill(1321012).getEffect(player.getSkillLevel(1321012)).makeChanceResult()) {
                        if (!monster.getStats().isBoss()) {
                            if (player.getSkillLevel(1321012) > 0) {
                                y = SkillFactory.getSkill(3110001).getEffect(player.getSkillLevel(1321012)).getY();
                                z = SkillFactory.getSkill(3110001).getEffect(player.getSkillLevel(1321012)).getZ();
                            } else if (Randomizer.nextInt(100) < y) {
                                totDamageToOneMonster = 99999999;
                            }
                        }
                    }
                }
                if (player.getSkillLevel(1310009) > 0) { // 드래곤 저지먼트
                    SkillStatEffect eff = SkillFactory.getSkill(1310009).getEffect(player.getSkillLevel(1310009));
                    if (eff.makeChanceResult()) {
                        player.addHP((int) Math.min((totDamageToOneMonster * (eff.getX() / 100.0D)),
                                player.getStat().getCurrentMaxHp() / 2)); // 체력 회복
                    }
                }
                if (player.getSkillLevel(31010002) > 0) { // 앱졸브 라이프
                    SkillStatEffect eff = SkillFactory.getSkill(31010002).getEffect(player.getSkillLevel(31010002));
                    if (eff.makeChanceResult()) {
                        if (player.exeedCount
                                / 2 > ((player.getSkillLevel(31210006) > 0 ? player.getSkillLevel(31210006) + 5 : 0)
                                + eff.getX())) {
                            player.addHP((int) Math.min((totDamageToOneMonster
                                    * ((((player.getSkillLevel(31210006) > 0 ? player.getSkillLevel(31210006) + 5 : 0)
                                    + eff.getX()) - ((int) (player.exeedCount / 2))) / 100.0D))
                                    * -1, player.getStat().getCurrentMaxHp() / 2)); // 체력 차감
                        } else {
                            player.addHP((int) Math.min((totDamageToOneMonster
                                    * ((((player.getSkillLevel(31210006) > 0 ? player.getSkillLevel(31210006) + 5 : 0)
                                    + eff.getX()) - ((int) (player.exeedCount / 2))) / 100.0D)),
                                    player.getStat().getCurrentMaxHp() / 2)); // 체력 회복
                        }
                    }
                }

                if (player.isEquilibrium()) {
                    if (GameConstants.isLightSkills(attack.skill)) {
                        player.addHP((int) Math.min((totDamageToOneMonster * (1 / 100.0D)),
                                player.getStat().getCurrentMaxHp() / 2)); // 체력 회복
                    }
                }

                if (attack.skill == 21120006) { // 생츄어리 or 콤보 템페스트
                    totDamageToOneMonster = (int) (monster.getStats().isBoss() ? 500000 : (monster.getHp() - 1));
                }

                if (attack.skill == 3111008) { // 애로우
                    int x = SkillFactory.getSkill(3111008).getEffect(player.getSkillLevel(3111008)).getX();
                    int recoverhp = (int) (player.getStat().getCurrentMaxHp() * (x / 100.0D));
                    recoverhp = Math.min(recoverhp, player.getStat().getCurrentMaxHp() / 2);
                    recoverhp = (int) Math.min(recoverhp, monster.getMobMaxHp());
                    player.addHP(recoverhp);
                }

                if (attack.skill == 33111006) { // 클로우 컷
                    int x = SkillFactory.getSkill(33111006).getEffect(player.getSkillLevel(33111006)).getX();
                    int recoverhp = (int) (player.getStat().getCurrentMaxHp() * (x / 100.0D));
                    recoverhp = Math.min(recoverhp, (int) (player.getStat().getCurrentMaxHp() * 0.15D));
                    recoverhp = (int) Math.min(recoverhp, monster.getMobMaxHp());
                    player.addHP(recoverhp);
                }

                if (attack.skill == 5011002) { // 기간틱 백스탭
                    SkillStatEffect eff = SkillFactory.getSkill(5011002).getEffect(player.getSkillLevel(5011002));
                    if (eff.makeChanceResult()) {
                        monsterStatusEffect = new MonsterStatusEffect(
                                Collections.singletonMap(MonsterStatus.SPEED, eff.getSkillStats().getStats("z")),
                                SkillFactory.getSkill(5011002), player.getSkillLevel(attack.skill), null, false);
                        monster.applyStatus(player, monsterStatusEffect, eff.getDuration());
                    }
                }

                if (attack.skill == 5221016) {
                    if (!monster.getStats().isBoss()) {
                        if (totDamageToOneMonster > 0) {
                            totDamageToOneMonster = 99999999;
                        }
                    }
                }

                if (player.getSkillLevel(13111006) > 0) { // 윈드 피어싱
                    int x = 0;
                    if (player.getSkillLevel(13110006) > 0) {
                        x = SkillFactory.getSkill(13110006).getEffect(player.getSkillLevel(13110006)).getX();
                    }
                    int recoverhp = (int) (player.getStat().getCurrentMaxHp() * (x / 100.0D));
                    recoverhp = Math.min(recoverhp, player.getStat().getCurrentMaxHp());
                    recoverhp = (int) Math.min(recoverhp, monster.getMobMaxHp());
                    player.addHP(recoverhp);
                }

                if (player.getBuffedValue(BuffStats.Roulette) != null) {
                    // 오크통 룰렛 효과
                    int oakid = player.getBuffedValue(BuffStats.Roulette).intValue();
                    SkillStatEffect eff = SkillFactory.getSkill(5311004).getEffect(player.getSkillLevel(5311004));
                    if (!monster.getStats().isBoss()) {
                        if (oakid == 4) { // 암흑 확률 상승
                            if (Randomizer.nextInt(100) < eff.getX()) {
                                monsterStatusEffect = new MonsterStatusEffect(
                                        Collections.singletonMap(MonsterStatus.DARKNESS, 1),
                                        SkillFactory.getSkill(5311004), player.getSkillLevel(attack.skill), null,
                                        false);
                                monster.applyStatus(player, monsterStatusEffect,
                                        eff.getSkillStats().getStats("v") * 1000 * 1000);
                            }
                        } else if (oakid == 3) { // 슬로우 확률 상승
                            if (Randomizer.nextInt(100) < eff.getX()) {
                                monsterStatusEffect = new MonsterStatusEffect(
                                        Collections.singletonMap(MonsterStatus.SPEED,
                                                eff.getSkillStats().getStats("u")),
                                        SkillFactory.getSkill(5311004), player.getSkillLevel(attack.skill), null,
                                        false);
                                monster.applyStatus(player, monsterStatusEffect,
                                        eff.getSkillStats().getStats("v") * 1000);
                            }
                        } else if (oakid == 2) { // 스턴 확률 상승
                            if (Randomizer.nextInt(100) < eff.getX()) {
                                monsterStatusEffect = new MonsterStatusEffect(
                                        Collections.singletonMap(MonsterStatus.STUN, 1), SkillFactory.getSkill(5311004),
                                        player.getSkillLevel(attack.skill), null, false);
                                monster.applyStatus(player, monsterStatusEffect,
                                        eff.getSkillStats().getStats("v") * 1000);
                            }
                        } else if (oakid == 1) { // 결빙 확률 상승 (20%)
                            if (Randomizer.nextInt(100) < eff.getY()) {
                                monsterStatusEffect = new MonsterStatusEffect(
                                        Collections.singletonMap(MonsterStatus.FREEZE, 1),
                                        SkillFactory.getSkill(5311004), player.getSkillLevel(attack.skill), null,
                                        false);
                                monster.applyStatus(player, monsterStatusEffect,
                                        eff.getSkillStats().getStats("v") * 1000);
                            }
                        }
                    }
                }
                if (attack.skill == 64121016) {
                    monsterStatusEffect = new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.STUN, 1),
                            SkillFactory.getSkill(64121003), player.getSkillLevel(attack.skill), null, false);
                    monster.applyStatus(player, monsterStatusEffect,
                            SkillFactory.getSkill(64121003).getEffect(attack.skillLevel).getStat("s"));
                }

                if (attack.skill == 64001001) {
                    monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.SPEED, -10), SkillFactory.getSkill(64001001), player.getSkillLevel(64001001), null, false), 5000);
                }
                if (attack.skill == 64001009) {
                    monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.SPEED, -10), SkillFactory.getSkill(64001009), player.getSkillLevel(64001009), null, false), 5000);
                }
                if (attack.skill == 64111009) {
                    monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.WDEF, -30), SkillFactory.getSkill(attack.skill), attack.skillLevel, null, false), 10000);
                }
                if (attack.skill == 64121003) {
                    monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.WATK, -30), SkillFactory.getSkill(attack.skill), attack.skillLevel, null, false), 10000);
                }
                if (GameConstants.isKadena(player.getJob())) {

                    if (player.getSkillLevel(64120007) > 0) {
                        if (monster.getBuff(MonsterStatus.WDEF) != null) {
                            if (player.getLastCombo() + 5000 < System.currentTimeMillis()) {
                                player.KADENA_WEAPON_STACK = 0;
                                player.clearDamageMeters();
                            }
                            player.setLastCombo(System.currentTimeMillis());
                            if (player.KADENA_WEAPON_STACK < 10) {
                                player.KADENA_WEAPON_STACK++;
                            }
                            SkillFactory.getSkill(64120007).getEffect(player.getSkillLevel(64120007)).applyTo(player);
                            if (Randomizer.isSuccess(SkillFactory.getSkill(64120007).getEffect(player.getSkillLevel(64120007)).getProb())) {
                                monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, SkillFactory.getSkill(64120007).getEffect(player.getSkillLevel(64120007)).getDotInterval()), SkillFactory.getSkill(64120007), attack.skillLevel, null, false), 30000);
                            }
                        } else if (monster.getBuff(MonsterStatus.SPEED) != null) {
                            if (player.getLastCombo() + 5000 < System.currentTimeMillis()) {
                                player.KADENA_WEAPON_STACK = 0;
                                player.clearDamageMeters();
                            }
                            player.setLastCombo(System.currentTimeMillis());
                            if (player.KADENA_WEAPON_STACK < 10) {
                                player.KADENA_WEAPON_STACK++;
                            }
                            SkillFactory.getSkill(64120007).getEffect(player.getSkillLevel(64120007)).applyTo(player);
                            if (Randomizer.isSuccess(SkillFactory.getSkill(64120007).getEffect(player.getSkillLevel(64120007)).getProb())) {
                                monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, SkillFactory.getSkill(64120007).getEffect(player.getSkillLevel(64120007)).getDotInterval()), SkillFactory.getSkill(64120007), attack.skillLevel, null, false), 30000);
                            }
                        } else if (monster.getBuff(MonsterStatus.WATK) != null) {
                            if (player.getLastCombo() + 5000 < System.currentTimeMillis()) {
                                player.KADENA_WEAPON_STACK = 0;
                                player.clearDamageMeters();
                            }
                            player.setLastCombo(System.currentTimeMillis());
                            if (player.KADENA_WEAPON_STACK < 10) {
                                player.KADENA_WEAPON_STACK++;
                            }
                            SkillFactory.getSkill(64120007).getEffect(player.getSkillLevel(64120007)).applyTo(player);
                            if (Randomizer.isSuccess(SkillFactory.getSkill(64120007).getEffect(player.getSkillLevel(64120007)).getProb())) {
                                monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, SkillFactory.getSkill(64120007).getEffect(player.getSkillLevel(64120007)).getDotInterval()), SkillFactory.getSkill(64120007), attack.skillLevel, null, false), 30000);
                            }
                        }
                    }
                }

                if (player.getJob() == 511 || player.getJob() == 512 || player.getJob() == 1511) { // 스턴 마스터리
                    if (player.getSkillLevel(5110000) > 0) {
                        SkillStatEffect eff = SkillFactory.getSkill(5110000).getEffect(player.getSkillLevel(5110000));
                        if (Randomizer.nextInt(100) < eff.getSkillStats().getStats("subProp")) {
                            monsterStatusEffect = new MonsterStatusEffect(
                                    Collections.singletonMap(MonsterStatus.STUN, 1), SkillFactory.getSkill(5110000),
                                    player.getSkillLevel(attack.skill), null, false);
                            monster.applyStatus(player, monsterStatusEffect, 3000);
                        }
                    }
                    if (player.getSkillLevel(15110010) > 0) {
                        SkillStatEffect eff = SkillFactory.getSkill(15110010).getEffect(player.getSkillLevel(15110010));
                        if (Randomizer.nextInt(100) < eff.getSkillStats().getStats("subProp")) {
                            monsterStatusEffect = new MonsterStatusEffect(
                                    Collections.singletonMap(MonsterStatus.STUN, 1), SkillFactory.getSkill(15110010),
                                    player.getSkillLevel(attack.skill), null, false);
                            monster.applyStatus(player, monsterStatusEffect, 3000);
                        }
                    }
                    if (player.getSkillLevel(27101101) > 0) {
                        SkillStatEffect eff = SkillFactory.getSkill(27101101).getEffect(player.getSkillLevel(27101101));
                        if (Randomizer.nextInt(100) < eff.getSkillStats().getStats("subProp")) {
                            monsterStatusEffect = new MonsterStatusEffect(
                                    Collections.singletonMap(MonsterStatus.STUN, 1), SkillFactory.getSkill(27101101),
                                    player.getSkillLevel(attack.skill), null, false);
                            monster.applyStatus(player, monsterStatusEffect, 3000);
                        }
                    }
                }

                if (player.getSkillLevel(3110001) > 0 || player.getSkillLevel(3210001) > 0
                        || player.getSkillLevel(13110009) > 0) { // 모탈 블로우
                    if (!monster.getStats().isBoss()) {
                        if (monster.getHp() < monster.getMobMaxHp() * 0.3D) {
                            int y = 0;
                            int z = 0;
                            if (player.getSkillLevel(3110001) > 0) {
                                y = SkillFactory.getSkill(3110001).getEffect(player.getSkillLevel(3110001)).getY();
                                z = SkillFactory.getSkill(3110001).getEffect(player.getSkillLevel(3110001)).getZ();
                            } else if (player.getSkillLevel(3210001) > 0) {
                                y = SkillFactory.getSkill(3210001).getEffect(player.getSkillLevel(3210001)).getY();
                                z = SkillFactory.getSkill(3210001).getEffect(player.getSkillLevel(3210001)).getZ();
                            } else if (player.getSkillLevel(13110009) > 0) {
                                y = SkillFactory.getSkill(13110009).getEffect(player.getSkillLevel(13110009)).getY();
                                z = SkillFactory.getSkill(13110009).getEffect(player.getSkillLevel(13110009)).getZ();
                            }
                            if (Randomizer.nextInt(100) < y) { // 즉사효과 발동
                                totDamageToOneMonster = 99999999;
                                int recoverhp = (int) (player.getStat().getCurrentMaxHp() * (z / 100.0D));
                                int recovermp = (int) (player.getStat().getCurrentMaxMp() * (z / 100.0D));
                                player.addHP(recoverhp);
                                player.addMP(recovermp);
                            }
                        }
                    }
                }

                if (player.getBuffedValue(BuffStats.BMageAura, 32101004) != null) {
                    ISkill skill = SkillFactory.getSkill(32101004);
                    player.addHP((int) Math.min(totDamage / 5,
                            (totDamage * skill.getEffect(player.getSkillLevel(skill)).getX()) / 100));
                }
                if (player.isActiveBuffedValue(1321054)) {
                    ISkill skill = SkillFactory.getSkill(1321054);
                    player.addHP((int) Math.min(totDamage / 5,
                            (totDamage * skill.getEffect(player.getSkillLevel(skill)).getX()) / 100));
                }
                // effects
                switch (attack.skill) {
                    case 5211006:
                    case 5220011:// homing
                    case 22151002: {// killer wing
                        player.setLinkMid(attack.skill, monster.getObjectId());
                        break;
                    }
                    case 4341002: {
                        if (Randomizer.rand(1, 30) <= effect.getZ()) {
                            totDamageToOneMonster = 99999999;
                        }
                        break;
                    }
                    case 21000004: // Combo Smash
                    case 21000006:
                    case 21001010:
                    case 21100007: // Combo Fenrir
                    case 21000002: // Double attack
                    case 21100001: // Triple Attack
                    case 21100002: // Pole Arm Push
                    case 21100004: // Pole Arm Smash
                    case 21110002: // Full Swing
                    case 21110003: // Pole Arm Toss
                    case 21110004: // Fenrir Phantom
                    case 21110006: // Whirlwind
                    case 21110007: // (hidden) Full Swing - Double Attack
                    case 21110008: // (hidden) Full Swing - Triple Attack
                    case 21120002: // Overswing
                    case 21120005: // Pole Arm finale
                    case 21120006: // Tempest
                    case 21120009: // (hidden) Overswing - Double Attack
                    case 21120010: // (hidden) Overswing - Triple Attack
                    case 21101003: // 바디 프레셔
                    {
                        if (player.getBuffedValue(BuffStats.WeaponCharge) != null) {
                            ISkill skill = SkillFactory.getSkill(21101006);
                            SkillStatEffect eff = skill.getEffect(player.getSkillLevel(skill));
                            monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.SPEED, 1), SkillFactory.getSkill(21101006), player.getSkillLevel(21101006), null, false), eff.getY() * 1000);

                        }
                        if (player.getBuffedValue(BuffStats.BodyPressure) != null) {
                            ISkill skill = SkillFactory.getSkill(21101003);
                            SkillStatEffect eff = skill.getEffect(player.getSkillLevel(skill));
                            if (eff.makeChanceResult()) {
                                monster.applyStatus(player,
                                        new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.BodyPressure, 1),
                                                skill, player.getSkillLevel(attack.skill), null, false),
                                        eff.getX() * 1000);
                            }
                        }
                        if (player.getBuffedValue(BuffStats.BMageAura) != null) {
                            ISkill skill = SkillFactory.getSkill(21100005);
                            player.addHP((int) Math.min(totDamage / 5,
                                    (totDamage * skill.getEffect(player.getSkillLevel(skill)).getX()) / 100));
                        }
                        if (attack.skill == 21100002) {
                            SkillStatEffect effzs = SkillFactory.getSkill(21100002)
                                    .getEffect(player.getSkillLevel(21100002));
                            if (effzs.makeChanceResult() && !monster.getStats().isBoss()) {
                                monsterStatusEffect = new MonsterStatusEffect(
                                        Collections.singletonMap(MonsterStatus.STUN, 1), SkillFactory.getSkill(21100002),
                                        player.getSkillLevel(attack.skill), null, false);
                                monster.applyStatus(player, monsterStatusEffect, effzs.getStatusDuration());
                            }
                        }
                        if (attack.skill == 21120006) {
                            SkillStatEffect effzs = SkillFactory.getSkill(21120006)
                                    .getEffect(player.getSkillLevel(21120006));
                            monsterStatusEffect = new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.FREEZE, 1),
                                    SkillFactory.getSkill(21120006), player.getSkillLevel(attack.skill), null, false);
                            monster.applyStatus(player, monsterStatusEffect, effzs.getStatusDuration());
                        }
                        break;
                    }
                    default:
                        if (totDamageToOneMonster > 0) {
                            if (player.getBuffedValue(BuffStats.IllusionStep) != null) {
                                ISkill skill = SkillFactory.getSkill(3121007);
                                SkillStatEffect eff = skill.getEffect(player.getSkillLevel(skill));
                                if (eff.makeChanceResult()) {
                                    monsterStatusEffect = new MonsterStatusEffect(
                                            Collections.singletonMap(MonsterStatus.SPEED, eff.getX()), skill,
                                            player.getSkillLevel(attack.skill), null, false);
                                    monster.applyStatus(player, monsterStatusEffect, eff.getY() * 1000);
                                }
                            }
                        }
                        break;
                }

                if (player.isActiveBuffedValue(11001022)) {
                    SkillStatEffect eff = player.getBuffedSkillEffect(BuffStats.ElementSoul, 11001022);
                    if (eff.makeChanceResult()) {
                        monster.applyStatus(player, new MonsterStatusEffect(eff.getMonsterStati(),
                                SkillFactory.getSkill(11001022), player.getSkillLevel(11001022), null, false),
                                eff.getStatusDuration());
                    }
                }

                if (effect != null && effect.getMonsterStati().size() > 0) {
                    if (effect.makeChanceResult()) {
                        int duration = effect.getStatusDuration();
                        if (attack.skill == 13121052 || attack.skill == 2101005) {
                            duration = effect.getDotTime() * 1000;
                        }
                        monster.applyStatus(player, new MonsterStatusEffect(effect.getMonsterStati(), theSkill,
                                attack.skillLevel, null, false), duration);
                    }
                }

                if (monster.getBuff(MonsterStatus.WEAPON_DAMAGE_REFLECT) != null) {
                    player.addHP(-(7000 + Randomizer.nextInt(8000))); // this is what it seems to be?
                }
                if (player.isActiveBuffedValue(2101010) && attack.skill != 2101010 && attack.skill != 2100010) {
                    SkillStatEffect ignight = SkillFactory.getSkill(2100010).getEffect(player.getSkillLevel(2101010));
                    if (ignight.makeChanceResult()) {
                        ignight.applyTo(player, monster.getTruePosition());
                    }
                }

                // 애로우 레인
                if (player.isActiveBuffedValue(400031002) && player.ARROW_RAIN <= System.currentTimeMillis()) {
                    SkillStatEffect arrowRain = SkillFactory.getSkill(400030002).getEffect(1);
                    player.ARROW_RAIN = System.currentTimeMillis() + 5000;

                    arrowRain.applyTo(player, monster.getTruePosition());
                }

                if (player.isActiveBuffedValue(400051007)) {
                    player.send(MainPacketCreator.lightningUnionSubAttack(attack.skill, 400051007,
                            player.getSkillLevel(400051007)));
                }

                if (attack.skill == 400011056 && player.getSummonCount(400011065) <= 0
                        && player.getBuffedValue(BuffStats.Ellision, 400011055) != null) {
                    SkillFactory.getSkill(400011065).getEffect(player.getSkillLevel(400011055)).applyTo(player,
                            monster.getTruePosition());
                }

                if (mobs > 2) {
                    long comboexp = monster.getStats().getExp() / 6;
                    player.send(MainPacketCreator.multikill(mobs, comboexp));
                    player.gainExp(comboexp, false, false, false);
                }

                if (attack.skill == 142101003) {
                    if (monster.getKinesisUltimateDeep() < System.currentTimeMillis()) {
                        for (final MonsterStatusEffect mseff : monster.getStati().values()) {
                            mseff.CancelEffect();
                        }
                        monster.setKinesisUltimateDeep(System.currentTimeMillis() + (120 * 1000));
                    }
                }
                if (GameConstants.isKinesis(player.getJob()) && attack.skill != 142110011) {
                    short skillLevel = (short) player.getSkillLevel(142110011);
                    if (skillLevel > 0) {
                        SkillStatEffect eff = SkillFactory.getSkill(142110011).getEffect(skillLevel);
                        if (eff.makeChanceResult()) {
                            eff.applyAtom(player, 22);
                        }
                    }
                }
                IItem nk = player.getInventory(MapleInventoryType.USE).getItem(attack.slot);
                if (player.isActiveBuffedValue(4101011) && nk != null && attack.skill != 4120019 && attack.skill != 4100012) {
                    int skillid = 4100011;
                    int skillid2 = 4100012;
                    if (player.getSkillLevel(4120018) > 0) {
                        skillid = 4120018;
                        skillid2 = 4120019;
                    }
                    if (attack.skill == 400041020) {
                        if (Randomizer.nextInt(100) < (int) (SkillFactory.getSkill(skillid)
                                .getEffect(player.getSkillLevel(skillid)).getProb()
                                * (double) (SkillFactory.getSkill(400041020).getEffect(player.getSkillLevel(400041020))
                                        .getW() / 100))) {
                            SkillFactory.getSkill(skillid).getEffect(player.getSkillLevel(skillid)).applyMarkOf(player,
                                    monster, nk.getItemId(), skillid2);
                        }
                    } else {
                        if (SkillFactory.getSkill(skillid).getEffect(player.getSkillLevel(skillid)).makeChanceResult()) {
                            SkillFactory.getSkill(skillid).getEffect(player.getSkillLevel(skillid)).applyMarkOf(player,
                                    monster, nk.getItemId(), skillid2);
                        }
                    }
                }
                if (attack.skill == 22110025 || attack.skill == 22110024 || attack.skill == 22110014 || attack.skill == 22140015 || attack.skill == 22140024 || attack.skill == 22140014 || attack.skill == 22140023 || attack.skill == 22170066 || attack.skill == 22170065 || attack.skill == 22170067 || attack.skill == 22170094 || attack.skill == 400020046) {
                    if (player.getSkillLevel(22170070) > 0) {
                        if (player.마법잔해 >= 0 && player.마법잔해 < 15) {
                            player.마법잔해++;
                            player.getMap().broadcastMessage(MainPacketCreator.Evanruins(player, monster.getPosition(), 22170070, player.마법잔해));
                        }
                    } else {
                        if (player.마법잔해 >= 0 && player.마법잔해 < 15) {
                            player.마법잔해++;
                            player.getMap().broadcastMessage(MainPacketCreator.Evanruins(player, monster.getPosition(), 22141017, player.마법잔해));
                        }
                    }
                }
                monster.damage(player, totDamageToOneMonster, true);
                if (GameConstants.isEvan(player.getJob())) {
                    if (player.getSkillLevel(22000015) > 0) {
                        SkillStatEffect eff = SkillFactory.getSkill(22000015).getEffect(player.getSkillLevel(22000015));
                        if (Randomizer.isSuccess(eff.getProb())) {
                            if (GameConstants.getWeaponType(player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId()) == MapleWeaponType.WAND) {
                                player.getClient().getSession().writeAndFlush(MainPacketCreator.finalAttackReqeust(attack.skill, 22000015, 37));
                            } else if (GameConstants.getWeaponType(player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId()) == MapleWeaponType.STAFF) {
                                player.getClient().getSession().writeAndFlush(MainPacketCreator.finalAttackReqeust(attack.skill, 22000015, 38));
                            }
                        }
                    }
                }

                if (player.getBuffedValue(BuffStats.HiddenPossession, 25101009) != null) {

                    SkillStatEffect eff = SkillFactory.getSkill(25101009).getEffect(player.getSkillLevel(25101009));
                    if (Randomizer.isSuccess(eff.getProb())) {
                        if (player.getSkillLevel(25120110) > 0) {
                            player.getClient().getSession().writeAndFlush(MainPacketCreator.absorbingFG(player.getId(), 25120115, monster.getObjectId()));
                        } else {
                            player.getClient().getSession().writeAndFlush(MainPacketCreator.absorbingFG(player.getId(), 25100010, monster.getObjectId()));
                        }
                    }

                }

                if (player.getJob() == 132) {
                    final SkillStatEffect reincarnationEffect = player.getBuffedSkillEffect(BuffStats.Reincarnation,
                            1320019);
                    if (reincarnationEffect != null) {
                        if (reincarnationEffect.getZ() >= 0) {
                            if (!monster.isAlive()) {
                                player.checkReincarnationBuff(true);
                            }
                        }
                    }
                }
                if (!monster.isAlive()) {
                    SkillStatEffect death_eff = player.getBuffedSkillEffect(BuffStats.BMageDeath);
                    if (death_eff != null) {
                        if (player.deathCount != 10) {
                            player.deathCount++;
                            death_eff.applyToBMDeath(player);
                        }
                    }
                }
            } else {
                if (attack.skill == 400011027) {
                    player.getClient().getSession().writeAndFlush(MainPacketCreator.deathEffect(oned.objectid, 400011027, player.getId()));
                }
                player.send(MobPacket.killMonster(oned.objectid, 1, GameConstants.isAswanMap(player.getMapId())));
            }
        }

        if (player.getSkillLevel(1200014) > 0) {
            int skillid[] = {1201011, 1201012, 1211004, 1211006, 1211008, 1221009, 1221004};
            if (player.GetCount() < 5) {
                for (int i = 0; i < skillid.length; i++) {
                    if (attack.skill == skillid[i]) {
                        if (player.GetSkillid() != skillid[i]) {
                            player.SetSkillid(attack.skill);
                            if (player.GetSkillid() != 0) {
                                player.elementalChargeHandler(1);
                            }
                        }
                    }
                }
            }
        }

        if (attack.skill == 2321008) {
            SkillFactory.getSkill(2321001).getEffect(player.getSkillLevel(2321008)).applyTo(player);
        }
        if (attack.skill == 36121052) {
            SkillFactory.getSkill(36121055).getEffect(player.getSkillLevel(36121052)).applyTo(player);
        }
        if (attack.skill == 24121052) {
            SkillStatEffect subEffect = SkillFactory.getSkill(24121052).getEffect(player.getSkillLevel(24121052));
            player.getMap().spawnMist(new MapleMist(effect.calculateBoundingBox(attack.position, player.isFacingLeft()), player, subEffect, player.getSkillLevel(24121052), attack.position), subEffect.getStat("time") * 1000, false, false, false, false, false);
        }

        if (effect != null) {
            if (attack.skill != 0 && !effect.isMist()) {
                if (attack.skill != 400051007 && attack.skill != 400051013 && attack.skill != 400051006 && attack.skill != 400021041 && attack.skill != 400021049
                        && attack.skill != 400021050 && attack.skill != 400051007 // 신뇌합일
                        && attack.skill != 400011052 && attack.skill != 400011053 // 블래스트 해머
                        && attack.skill != 12120011 && attack.skill != 12121001 // 블레이징 익스팅션
                        && attack.skill != 14000027 && attack.skill != 14000028 && attack.skill != 14000029 && attack.skill != 14001027// 쉐도우 배트
                        && attack.skill != 35101002 && attack.skill != 35110017 && attack.skill != 35120017// 호밍 미사일
                        && attack.skill != 65111007 && attack.skill != 65111100 && attack.skill != 65121011// 소울 시커
                        && attack.skill != 400051008 //빅 휴즈 기간틱
                        && attack.skill != 400051043 && attack.skill != 25121005 //귀참 , 진 귀참
                        && attack.skill != 13121054 && attack.skill != 400031003 && attack.skill != 400031004 && attack.skill != 400031030 && attack.skill != 400031031 && attack.skill != 400031030 && attack.skill != 400031031 //윈드 월 , 스톰 브링어, 하울링 게일
                        && attack.skill != 13100022 && attack.skill != 13100027 && attack.skill != 13101022 && attack.skill != 13110022 && attack.skill != 13110027 && attack.skill != 13120003 && attack.skill != 13120010 //트라이플링 윔
                        && attack.skill != 4221052 // 베일 오브 섀도우
                        && attack.skill != 400051040 && attack.skill != 400051049 && attack.skill != 400051050 //노틸러스 어썰트
                        && attack.skill != 400031036 //레이븐 템페스트
                        && attack.skill != 3111013 && attack.skill != 3121013 //애로우 플래터
                        && attack.skill != 400051039 && attack.skill != 22171083
                        && attack.skill != 2221012 && attack.skill != 400051015 && attack.skill != 400041038
                        && attack.skill != 95001000 && attack.skill != 400030002 && attack.skill != 400041020
                        && attack.skill != 400031020 && attack.skill != 400031021 && attack.skill != 400041001
                        && attack.skill != 4100011 && attack.skill != 4100012 && attack.skill != 4120018
                        && attack.skill != 4120019 && attack.skill != 3100010 && attack.skill != 2101010
                        && attack.skill != 2100010 && attack.skill != 400031003 && attack.skill != 400031004
                        && attack.skill != 400051018 && attack.skill != 400051019 && attack.skill != 400051020 // 스포트라이트
                        && attack.skill != 4341052 && attack.skill != 35111003 && attack.skill != 35001002
                        && attack.skill != 400041018
                        && attack.skill != 31121005 && attack.skill != 2121054 && attack.skill != 35121003
                        && attack.skill != 400031000) { // 메타모포시스 재사용 방지 
                    if (attack.skill == 1221009) {
                        if (player.GetCount() == 5) {
                            if (player.getBuffedValue(BuffStats.IgnoreMobpdpR, 1221009) == null) {
                                player.elementalChargeHandler(-1);
                                effect.applyTo(player);
                                //player.send(MainPacketCreator.SkillUseResult((byte) 0));
                            }
                        }
                    } else {
                        effect.applyTo(player, attack.position);
                        //player.send(MainPacketCreator.SkillUseResult((byte) 0));
                    }
                }
            } else if (attack.skill == 35121052) {
                effect.applyTo(player, attack.position);
                //player.send(MainPacketCreator.SkillUseResult((byte) 0));
            }
        }

        if (mobs > 0) {
            MapleCharacter from = player;
            if (from.getMonsterCombo() == 0) {
                from.setMonsterComboTime(System.currentTimeMillis());
            }
            if (from.getMonsterComboTime() < System.currentTimeMillis() - 10000) {
                from.setMonsterCombo(0);
            }
            from.addMonsterCombo(1);
            if (from.getMonsterCombo() > 1) {
                from.send(MainPacketCreator.combokill(from.getMonsterCombo(), last));
            }
            from.setMonsterComboTime(System.currentTimeMillis());

            if (from.getMonsterCombo() % 50 == 0) {
                int combo = from.getMonsterCombo() / 50;
                int id = 2023484;
                if (combo >= 40) {
                    id = 2023669;
                } else if (combo >= 15) {
                    id = 2023495;
                } else if (combo >= 7) {
                    id = 2023494;
                }
                from.combokillexp = lastE * ServerConstants.defaultExpRate;
                Item toDrop = new Item(id, (byte) 0, (short) 1, (byte) 0);
                from.getMap().spawnItemDrop(from, from, toDrop, lastP, false, false, false, true, 0, 0);
            }
        }

        // 트루 스나이핑 화살 감소
        if (player.isActiveBuffedValue(400031006)) {

            final ArrayList<Triple<BuffStats, Integer, Boolean>> statups = new ArrayList<Triple<BuffStats, Integer, Boolean>>();
            SkillStatEffect trueSniping = SkillFactory.getSkill(400031006).getEffect(player.getSkillLevel(400031006));

            player.trueSniping--;
            statups.add(new Triple<>(BuffStats.TrueSniping, player.trueSniping, false));
            player.setBuffedValue(BuffStats.TrueSniping, 400031006, player.trueSniping);
            player.send(SecondaryStat.encodeForLocal(
                    400031006, (int) (player.getBuffedStarttime(BuffStats.TrueSniping, 400031006)
                    + trueSniping.getDuration() - System.currentTimeMillis()),
                    statups, effect, player.getStackSkills(), 0, player));

            if (player.trueSniping <= 0) {
                player.cancelBuffStats(400031006, BuffStats.TrueSniping);
            }
        }

        if (player.isActiveBuffedValue(15001022)) {
            if (attack.skill == 400051016) {
                SkillStatEffect dkeffect = player.getBuffedSkillEffect(BuffStats.CygnusElementSkill, 15001022);
                player.lightning -= 2;
                dkeffect.applyToStrikerStack(player, player.lightning);
            } else {
                SkillStatEffect dkeffect = player.getBuffedSkillEffect(BuffStats.CygnusElementSkill, 15001022);
                int prop = player.getSkillLevel(15001022), maxcount = 1;
                if (player.getSkillLevel(15000023) > 0) {
                    prop += player.getSkillLevel(15001023);
                    maxcount++;
                }
                if (player.getSkillLevel(15100025) > 0) {
                    prop += player.getSkillLevel(15100025);
                    maxcount++;
                }
                if (player.getSkillLevel(15110026) > 0) {
                    prop += player.getSkillLevel(15110026);
                    maxcount++;
                }
                if (player.getSkillLevel(15120008) > 0) {
                    prop += player.getSkillLevel(15120008);
                    maxcount++;
                }

                if (Randomizer.nextInt(100) < prop) {
                    if (player.lightning < maxcount) {
                        player.lightning++;
                        dkeffect.applyToStrikerStack(player, player.lightning);
                    }
                }
            }
        }
        int prop = 0;
        if (player.getBuffedValue(BuffStats.DarkSight) != null && player.getBuffedValue(BuffStats.DarkSight, 400001023) == null) {
            if (player.getSkillLevel(4210015) > 0) {
                prop = SkillFactory.getSkill(4210015).getEffect(player.getSkillLevel(4210015)).getProb();
            }

            if (player.getSkillLevel(4330001) > 0) {
                prop = SkillFactory.getSkill(4330001).getEffect(player.getSkillLevel(4330001)).getProb();
            }
            if (!Randomizer.isSuccess(prop)) {
                player.cancelEffectFromBuffStat(BuffStats.DarkSight);
            }
        }

        if (player.isActiveBuffedValue(36001005) && attack.skill != 36110004 && attack.skill != 36001005 && attack.skill != 36111004) {
            if (System.currentTimeMillis() - player.lastChainArtsFuryTime > 2000) {
                player.lastChainArtsFuryTime = System.currentTimeMillis();
                player.getBuffedSkillEffect(BuffStats.PinPointRocket).applyAtom(player, 6);
            }
        }
        if (attack.skill == 142121004) {
            player.acaneAim = attack.targets;
            SkillFactory.getSkill(142121004).getEffect(player.getSkillLevel(142121004)).applyTo(player);
        }

        if (player.isActiveBuffedValue(65121011) && attack.skill != 65111007 && attack.targets > 0) {
            SkillStatEffect a = SkillFactory.getSkill(65121011).getEffect(player.getSkillLevel(65121011));
            List<MapleMapObject> objs = null;
            MapleMonster mob = null;
            List<Integer> moblist = new ArrayList<>();
            objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 100000, Arrays.asList(MapleMapObjectType.MONSTER));
            if (!objs.isEmpty()) {
                mob = (MapleMonster) objs.get(Randomizer.rand(0, objs.size() - 1));
                if (mob != null && mob.isAlive()) {
                    moblist.add(mob.getObjectId());
                }
            }
            int suc = attack.skill == 65121100 ? 45 : 35; // 성공 확률
            int ex = player.isActiveBuffedValue(65121054) ? 15 : 0; // 익절트 키고 확률
            if (Randomizer.isSuccess(suc + ex)) {
                player.getMap().broadcastMessage(AngelicBusterSkill.SoulSeeker(player, 65120011, (moblist.size() >= 2 ? 2 : moblist.size()), moblist.get(0), moblist.size() > 1 ? moblist.get(1) : 0));
            }
        }

        if (attack.skill == 3321014 || attack.skill == 3321016 || attack.skill == 3321018 || attack.skill == 3321020) { //콤보 어썰트
            if (attack.skill == 3321014) {
                attack.skill = 3321015;
            } else if (attack.skill == 3321016) {
                attack.skill = 3321017;
            } else if (attack.skill == 3321018) {
                attack.skill = 3321019;
            } else if (attack.skill == 3321020) {
                attack.skill = 3321021;
            }
            player.send(MainPacketCreator.ToAttackkerinfo(attack.skill));
        }

        if (GameConstants.isKadena(player.getJob())) {
            int skillid = 0;
            switch (player.getJob()) {
                case 6410:
                    skillid = 64100004;
                    break;
                case 6411:
                    skillid = 64110005;
                    break;
                case 6412:
                    skillid = 64120006;
                    break;
            }
            if (player.getSkillLevel(skillid) > 0) {
                switch (attack.skill) {
                    case 64001002:
                    case 64101001:
                    case 64101002:
                    case 64101008:
                    case 64111002:
                    case 64111003:
                    case 64111012:
                    case 64121023:
                    case 64121003:
                    case 64121001:
                        if (player.getLastCombo() + 20000 < System.currentTimeMillis()) {
                            player.KADENA_STACK = 0;
                            player.clearDamageMeters();
                        }
                        player.setLastCombo(System.currentTimeMillis());
                        if (player.KADENA_STACK < 8) {
                            player.KADENA_STACK++;
                        }
                        int id = player.getSkillLevel(64120006) > 0 ? 64121020 : player.getSkillLevel(64110005) > 0 ? 64111013 : 64101009;
                        if (player.lastSkillTime + 200 < System.currentTimeMillis()) {
                            player.getClient().send(MainPacketCreator.ToAttackkerinfo(id));
                            player.lastSkillTime = System.currentTimeMillis();
                        }
                        SkillFactory.getSkill(64100004).getEffect(player.getSkillLevel(skillid)).applyTo(player);
                }
            }
        }
        if (attack.skill == 155121306) {
            SkillFactory.getSkill(155121006).getEffect(player.getSkillLevel(155121306)).applyTo(player);
        }
        if ((attack.skill == 1121008 || attack.skill == 1120017) && player.getBuffedValue(BuffStats.ComboInstings) != null) {
            player.getClient().send(MainPacketCreator.ToAttackkerinfo(400011074));
            player.getClient().send(MainPacketCreator.ToAttackkerinfo(400011075));
            player.getClient().send(MainPacketCreator.ToAttackkerinfo(400011076));
        }
        if (attack.skill == 400011089) {
            player.cancelBuffStats(400011088, BuffStats.IndieIliumStack);
        }

        if (attack.skill == 400011079) {
            List<Integer> subskillid = new ArrayList<>();
            subskillid.add(400011079);
            subskillid.add(400011081);
            player.getClient().send(MainPacketCreator.ConnectionSkill(attack.position, attack.skill, subskillid, player.isFacingLeft() ? -1 : 1));
        } else if (attack.skill == 400011080) {
            List<Integer> subskillid = new ArrayList<>();
            subskillid.add(400011080);
            subskillid.add(400011082);
            player.getClient().send(MainPacketCreator.ConnectionSkill(attack.position, attack.skill, subskillid, player.isFacingLeft() ? -1 : 1));
            player.getClient().send(MainPacketCreator.skillCooldown(400011079, 6));
        }

        if (attack.skill == 155120000 && !player.skillisCooling(400051047)) {
            player.getClient().send(MainPacketCreator.ToAttackkerinfo(400051047));

        } else if (attack.skill == 155120001 && !player.skillisCooling(400051048)) {
            player.getClient().send(MainPacketCreator.ToAttackkerinfo(400051048));

        }
        if (attack.skill == 4221016 && player.getSkillLevel(400041025) > 0) {
            if (player.ObjectId != 0 && player.ObjectId != attack.allDamage.get(0).objectid) {
                player.acaneAim = 0;
            }
            if (player.acaneAim < 3) {
                player.acaneAim++;
            }
            player.ObjectId = attack.allDamage.get(0).objectid;
            SkillStatEffect eff = SkillFactory.getSkill(4221016).getEffect(player.getSkillLevel(4221016));
            List<Triple<BuffStats, Integer, Boolean>> statups = new ArrayList<>();
            statups.add(new Triple<>(BuffStats.KillMonster, player.acaneAim, false));
            player.send(MainPacketCreator.giveBuff(4221016, 10000, statups, eff, null,
                    0, player));
        }

    }

    public static void applyAttackMagic(AttackInfo attack, ISkill theSkill, MapleCharacter player,
            SkillStatEffect effect) {
        Point attackPos = attack.position;
        long totDamage = 0, totDamageToOneMonster = 0;
        int last = 0;
        int lastE = 0;
        Point lastP = null;
        MapleMonsterStats monsterstats;
        final ISkill eaterSkill = SkillFactory.getSkill(GameConstants.getMPEaterForJob(player.getJob()));
        final int eaterLevel = player.getSkillLevel(eaterSkill);
        FinalAttackHandler.FianlAttack(attack, player);
        //System.out.println("skillid : " + attack.skill);
        MapleMap map = player.getMap();
        Long eachd;
        int mobs = 0;
        boolean isBoss = false;
        if(attack.skill == 27121303) {
            player.setbb(player.getbb() + 1);
            SkillFactory.getSkill(400021071).getEffect(player.getSkillLevel(400021071)).applyTo(player);
        }
        if(player.getbb() == 12) {
            player.send(MainPacketCreator.skillCooldown(400021071, 0));
        }
        if(attack.skill == 400021071) {
            player.setbb(0);
            SkillFactory.getSkill(400021071).getEffect(player.getSkillLevel(400021071)).applyTo(player);
        }
        for (final AttackPair mob : attack.allDamage) {
            MapleMonster monster = map.getMonsterByOid(mob.objectid);
            long mdmg = 0;
            monster = map.getMonsterByOid(mob.objectid);
            if (monster != null && monster.getStats().isBoss()) {
                isBoss = true;
            }
            for (Pair<Long, Boolean> dmg : mob.attack) {
                mdmg += dmg.left;
                if (player.getKeyValue("qkdekdcnepa") != null) {
                    if (player.getKeyValue("qkdekdcnepa").equals("buy")) {
                        if (player.getAddDamage() > 0) {
                            long dam = player.getAddDamage() * (player.getDamageHit() + player.getDamageHit2()) / 100;
                            mdmg += dam;
                        }
                    }
                }
            }
            if (player.getKeyValue("qkdekdcnepa") == null) {
                if (player.getAddDamage() > 0) {
                    long dam = player.getAddDamage() * (player.getDamageHit() + player.getDamageHit2()) / 100;
                    mdmg += dam;
                }
            }
            if (monster != null && !monster.getStats().isBoss()) {
                if (monster.getMobMaxHp() - mdmg < 0) {
                    mobs++;
                    last = monster.getObjectId();
                    lastP = monster.getPosition();
                    lastE = monster.getMobExp();
                }
            }
        }
        if (attack.skill == 12121052 && player.getMapId() == 1000000) {
            return;
        }

        if (attack.skill > 0 && GameConstants.isIliume(player.getJob()) && !attack.allDamage.isEmpty()) {
            int summonOid = 0;
            for (MapleMapObject ob : player.getMap().getAllSummon()) {
                MapleSummon summon = (MapleSummon) ob;
                if (summon.getOwner() != null && summon.getOwner().getId() == player.getId()
                        && summon.getSkill() == 152101000) {
                    summonOid = summon.getObjectId();
                    break;
                }
            }
            player.getClient().sendPacket(MainPacketCreator.IliumeCrystal(player.getId(), summonOid));
        }
        if ((attack.skill != 0) && (GameConstants.isBlaster(player.getJob()))) {
            player.giveBulletGauge(attack.skill, false);
        }
        for (AttackPair oned : attack.allDamage) {
            MapleMonster monster = map.getMonsterByOid(oned.objectid);
            if (monster != null) {
                totDamageToOneMonster = 0;
                monsterstats = monster.getStats();

                if (player.haveItem(3994514, 1, false, true) && monster.getStats() != null
                        && monster.getStats().getLevel() + 20 > player.getLevel()
                        && monster.getStats().getLevel() - 20 <= player.getLevel()) {
                    player.getStat().addSaintSaver(1);
                }

                if (attack.skill == 400021002) {
                    SkillStatEffect iceAge = SkillFactory.getSkill(400020002).getEffect(1);
                    int y = monster.getTruePosition().y + 25;
                    int xMin = monster.getTruePosition().x - 250;
                    int xMax = xMin + 500;
//                        int max = Randomizer.rand(3, 5);
//                        for (int i = 0; i < max; i++) {
                    iceAge.applyTo(player, new Point(Randomizer.rand(xMin, xMax), y));
//                        }
                }

                for (Pair<Long, Boolean> eachde : oned.attack) { // 방당추뎀 박히게 해주는 for문
                    eachd = eachde.left;
                    totDamageToOneMonster += (long) eachd;
                    if (player.getKeyValue("qkdekdcnepa") != null) {
                        if (player.getKeyValue("qkdekdcnepa").equals("buy")) {
                            if (player.getAddDamage() > 0) {
                                long dam = player.getAddDamage() * (player.getDamageHit() + player.getDamageHit2()) / 100;
                                player.send(UIPacket.detailShowInfo("현재 " + player.getName() + "님의 < " + player.getAddDamage() + " * " + (player.getDamageHit() + player.getDamageHit2()) + " %> 총데미지 < " + dam + " > ", true));
                                monster.damage(player, dam, true);
                            }
                        }
                    }
                }

                if (player.getKeyValue("qkdekdcnepa") == null) { // 이건 for문 밖에 있으니까 추뎀만 박힘
                    if (player.getAddDamage() > 0) {
                        long dam = player.getAddDamage() * (player.getDamageHit() + player.getDamageHit2()) / 100;
                        player.send(UIPacket.detailShowInfo("현재 " + player.getName() + "님의 < " + player.getAddDamage() + " * " + (player.getDamageHit() + player.getDamageHit2()) + " %> 총데미지 < " + dam + " > ", true));
                        monster.damage(player, dam, true);
                    }
                }
                totDamage += totDamageToOneMonster;
                player.checkMonsterAggro(monster);

                if (GameConstants.isLuminous(player.getJob())) {
                    Integer Gauge = player.getBuffedValue(BuffStats.Larkness);
                    if (player.getBuffedValue(BuffStats.Larkness) == null
                            || player.getBuffedValue(BuffStats.Larkness) == -1) {
                        if (!GameConstants.isDarkSkills(attack.skill)) {
                            player.getSunfireBuffedValue(20040216, attack.skill, Gauge);
                        } else {
                            player.getEclipseBuffedValue(20040217, attack.skill, Gauge);
                        }
                    } else if (player.getBuffedValue(BuffStats.Larkness) == 20040216) {
                        player.getSunfireBuffedValue(20040216, attack.skill, Gauge);
                    } else if (player.getBuffedValue(BuffStats.Larkness) == 20040217) {
                        player.getEclipseBuffedValue(20040217, attack.skill, Gauge);
                    }
                }
                if (totDamageToOneMonster > 0) {
                    if (player.getJob() == 221 || player.getJob() == 222) {
                        if (player.getSkillLevel(2210000) > 0) { // 익스트림 매직 : 썬/콜
                            if (attack.skill > 0) { // 평타는 효과 대상에서 제외
                                final MapleMonster mob = monster;
                                final SkillStatEffect eff = SkillFactory.getSkill(2210000)
                                        .getEffect(player.getSkillLevel(2210000));
                                if (Randomizer.nextInt(100) < eff.getY()) { // y값 = 즉사가 터질 확률이므로, 확률계산을 해서 확률을 통과한다면,
                                    if (mob.getHp() < (int) (mob.getHp() * (eff.getX() / 100.0D))) { // x값을 통해 x퍼센트의 체력보다 낮은지 확인.
                                        if (!monster.getStats().isBoss()) { // 만약 통과한다면, 해당 몬스터는 보스가 아니어야함.
                                            totDamageToOneMonster = 999999999;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (player.isEquilibrium()) {
                        if (GameConstants.isLightSkills(attack.skill)) {
                            player.addHP((int) Math.min((totDamageToOneMonster * (1 / 100.0D)),
                                    player.getStat().getCurrentMaxHp() / 2)); // 체력 회복
                        }
                    }

                    try { // 속성 효과.
                        if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11) != null) { //잠재
                            Equip equip = (Equip) player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                            if (equip.getState() > 1) {
                                int[] potentials = {equip.getPotential1(), equip.getPotential2(), equip.getPotential3(), equip.getPotential4(), equip.getPotential5(), equip.getPotential6()};
                                ItemInformation ii = ItemInformation.getInstance();
                                StructPotentialItem pot = null;
                                for (int i : potentials) {
                                    if (i > 0) {
                                        if (ii.getPotentialInfo(i).get(ii.getReqLevel(equip.getItemId() / 10)) != null) {
                                            pot = ii.getPotentialInfo(i).get(ii.getReqLevel(equip.getItemId() / 10));
                                            if (pot != null) {
                                                if (Randomizer.isSuccess(pot.prop)) {
                                                    switch (i) {
                                                        case 10201:
                                                        case 20201:
                                                            player.addHP(pot.HP);
                                                            break;
                                                        case 10202:
                                                        case 20206:
                                                            player.addMP(pot.MP);
                                                            break;
                                                        case 10221:
                                                            //시간을 잘몰라서 5초라 생각하고 구현
                                                            monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, (int) pot.level), SkillFactory.getSkill(90001003), player.getSkillLevel(attack.skill), null, false), (long) 5 * 1000);
                                                            break;
                                                        case 10226:
                                                            monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.STUN, (int) pot.level), SkillFactory.getSkill(90001001), player.getSkillLevel(attack.skill), null, false), (long) 5 * 1000);
                                                            break;
                                                        case 10231:
                                                            monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.SPEED, (int) pot.level), SkillFactory.getSkill(90001002), player.getSkillLevel(attack.skill), null, false), (long) 5 * 1000);
                                                            break;
                                                        case 10236:
                                                            monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.DARKNESS, (int) pot.level), SkillFactory.getSkill(90001004), player.getSkillLevel(attack.skill), null, false), (long) 5 * 1000);
                                                            break;
                                                        case 10241:
                                                            monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.FREEZE, (int) pot.level), SkillFactory.getSkill(90001006), player.getSkillLevel(attack.skill), null, false), (long) 5 * 1000);
                                                            break;
                                                        case 10246:
                                                            monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.SEAL, (int) pot.level), SkillFactory.getSkill(90001005), player.getSkillLevel(attack.skill), null, false), (long) 5 * 1000);
                                                            break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (SkillStatEffect.isFreezStackSkill(attack.skill)) {
                            monster.setFreezeStack(monster.getFreezeStack() + 1);
                            monster.applyStatus(player,
                                    new MonsterStatusEffect(
                                            Collections.singletonMap(MonsterStatus.SPEED, monster.getFreezeStack()),
                                            SkillFactory.getSkill(attack.skill), attack.skillLevel, null, false),
                                    effect.getTime());
                        }
                        if (player.getJob() == 222 || player.getJob() == 212 || player.getJob() == 232) { // 아케인 에임
                            int[] skills = {2120010, 2220010, 2320011};
                            for (int d : skills) {
                                if (player.getSkillLevel(d) > 0) {
                                    if (player.getLastCombo() + 5000 < System.currentTimeMillis()) {
                                        player.acaneAim = 0;
                                        player.clearDamageMeters();
                                    }
                                    if (SkillFactory.getSkill(d).getEffect(player.getSkillLevel(d)).makeChanceResult()) {
                                        player.setLastCombo(System.currentTimeMillis());
                                        if (player.acaneAim < 5) {
                                            player.acaneAim++;
                                        }
                                        SkillFactory.getSkill(d).getEffect(player.getSkillLevel(d)).applyTo(player);
                                    }
                                }
                            }
                        }

                        if (GameConstants.isLuminous(player.getJob())) {
                            SkillStatEffect dkeffect = player.getBuffedSkillEffect(BuffStats.StackBuff, 27121005);
                            if (dkeffect != null) {
                                if (player.getLastCombo() + 5000 < System.currentTimeMillis()) {
                                    player.acaneAim = 0;
                                    player.clearDamageMeters();
                                }

                                if (dkeffect.makeChanceResult()) {
                                    player.setLastCombo(System.currentTimeMillis());
                                    if (player.acaneAim <= 29) {
                                        player.acaneAim++;
                                        dkeffect.applyTo(player);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        if (!ServerConstants.realese) {
                            e.printStackTrace();
                        }
                    }

                    if (monster.getBuff(MonsterStatus.MAGIC_DAMAGE_REFLECT) != null) {
                        player.addHP(-(7000 + Randomizer.nextInt(8000))); // this is what it seems to be?
                    }

                    if (GameConstants.isKinesis(player.getJob()) && attack.skill != 142110011) {
                        short skillLevel = (short) player.getSkillLevel(142110011);
                        if (skillLevel > 0) {
                            SkillStatEffect eff = SkillFactory.getSkill(142110011).getEffect(skillLevel);
                            if (eff.makeChanceResult()) {
                                eff.applyAtom(player, 22);
                            }
                        }
                    }

                    if ((effect != null) && (effect.getMonsterStati().size() > 0) && (effect.makeChanceResult())) {
                        try {
                            int duration = effect.getStatusDuration();
                            if (attack.skill == 13121052 || attack.skill == 2101005) {
                                duration = effect.getDotTime() * 1000;
                            }
                            monster.applyStatus(player,
                                    new MonsterStatusEffect(effect.getMonsterStati(), theSkill,
                                            player.getSkillLevel(attack.skill), null, false),
                                    duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    int[] venomskills = {4110011, 4210010, 4320005, 4120005, 4120011, 4220005, 4220011, 4340001,
                        4340012, 14110004};
                    for (int i : venomskills) {
                        if (i == 4110011) {
                            if (player.getSkillLevel(4120011) > 0) {
                                i = 4120011;
                            }
                        } else if (i == 4210010) {
                            if (player.getSkillLevel(4220011) > 0) {
                                i = 4220011;
                            }
                        }
                        final ISkill skill = SkillFactory.getSkill(i);
                        if (player.getSkillLevel(skill) > 0) {
                            final SkillStatEffect venomEffect = skill.getEffect(player.getSkillLevel(skill));
                            monster = map.getMonsterByOid(oned.objectid);
                            if (venomEffect.makeChanceResult() && monster != null) {
                                monster.applyStatus(player,
                                        new MonsterStatusEffect(
                                                Collections.singletonMap(MonsterStatus.POISON,
                                                        venomEffect.getSkillStats().getStats("dot")),
                                                SkillFactory.getSkill(i), player.getSkillLevel(attack.skill), null,
                                                false),
                                        venomEffect.getStatusDuration());
                            }
                            break;
                        }
                    }
                    if (player.getBuffedValue(BuffStats.BMageAura, 32101004) != null) { // 콤보 드레인
                        ISkill skill = SkillFactory.getSkill(32101004);
                        player.addHP((int) (Math.min(totDamage / 5, (totDamage * skill.getEffect(player.getSkillLevel(skill)).getX()) / 100)));
                    }
                    if (player.isActiveBuffedValue(32121018)) {
                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.WDEF, -SkillFactory.getSkill(32121018).getEffect(player.getSkillLevel(32121018)).getX()), SkillFactory.getSkill(32121018), player.getSkillLevel(32121018), null, false), (long) 10 * 1000);
                    }
                    if (eaterLevel > 0) {
                        eaterSkill.getEffect(eaterLevel).applyPassive(player, monster);
                    }
                    if (player.isActiveBuffedValue(2101010) && attack.skill != 2101010 && attack.skill != 2100010) {
                        SkillStatEffect ignight = SkillFactory.getSkill(2100010).getEffect(player.getSkillLevel(2101010));
                        if (ignight.makeChanceResult()) {
                            ignight.applyTo(player, monster.getPosition());
                        }
                    }

                    if (attack.skill == 142001000 || attack.skill == 142100000 || attack.skill == 142110000) {
                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, (int) Randomizer.rand(100, 500)), SkillFactory.getSkill(attack.skill), player.getSkillLevel(attack.skill), null, false), (long) 10 * 1000);

                    }
                    if (attack.skill == 142121031) {
                        monster.applyStatus(player, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.STUN, 1), SkillFactory.getSkill(142121031), player.getSkillLevel(142121031), null, false), (long) 10 * 1000);
                    }
                    if (attack.skill == 2121011) {
                        player.FlameHayzePositoin = monster.getPosition();
                    }
                    try { // 몬스터 2마리 이상 겉뎀 수정 및 멀티킬.
                        if (mobs > 2) {
                            int comboexp = (int) (monster.getStats().getExp() * ((6 * 100) / 100.0D));
                            player.send(MainPacketCreator.multikill(mobs, comboexp));
                            player.gainExp(comboexp, false, false, false);
                        }
                        if (attack.skill == 22110025 || attack.skill == 22110024 || attack.skill == 22110014 || attack.skill == 22140015 || attack.skill == 22140024 || attack.skill == 22140014 || attack.skill == 22140023 || attack.skill == 22170066 || attack.skill == 22170065 || attack.skill == 22170067 || attack.skill == 22170094 || attack.skill == 400020046) {
                            if (player.getSkillLevel(22170070) > 0) {
                                if (player.마법잔해 >= 0 && player.마법잔해 < 15) {
                                    player.마법잔해++;
                                    player.getMap().broadcastMessage(MainPacketCreator.Evanruins(player, monster.getPosition(), 22170070, player.마법잔해));
                                }
                            } else {
                                if (player.마법잔해 >= 0 && player.마법잔해 < 15) {
                                    player.마법잔해++;
                                    player.getMap().broadcastMessage(MainPacketCreator.Evanruins(player, monster.getPosition(), 22141017, player.마법잔해));
                                }
                            }
                        }

                        monster.damage(player, totDamageToOneMonster, true); // 위에선 추가데미지만 넣고난 후 여기선 원래 데미지를 넣어줌
                        if (GameConstants.isEvan(player.getJob())) {
                            if (player.getSkillLevel(22000015) > 0) {
                                SkillStatEffect eff = SkillFactory.getSkill(22000015).getEffect(player.getSkillLevel(22000015));
                                if (Randomizer.isSuccess(eff.getProb())) {
                                    if (GameConstants.getWeaponType(player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId()) == MapleWeaponType.WAND) {
                                        player.getClient().getSession().writeAndFlush(MainPacketCreator.finalAttackReqeust(attack.skill, 22000015, 37));
                                    } else if (GameConstants.getWeaponType(player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId()) == MapleWeaponType.STAFF) {
                                        player.getClient().getSession().writeAndFlush(MainPacketCreator.finalAttackReqeust(attack.skill, 22000015, 38));
                                    }
                                }
                            }
                        }
                        if (!monster.isAlive()) {
                            SkillStatEffect death_eff = player.getBuffedSkillEffect(BuffStats.BMageDeath);
                            if (death_eff != null) {
                                if (player.deathCount != 10) {
                                    player.deathCount++;
                                    death_eff.applyToBMDeath(player);
                                }
                            }
                        }
                    } catch (Exception e) {
                        if (!ServerConstants.realese) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (attack.skill == 2111003) {
            if (player.FlameHayzePositoin != null) {
                attackPos = player.FlameHayzePositoin;
                player.FlameHayzePositoin = null;
            }
        }

        //제네시스 쓰면 빅뱅 노쿨
        if (attack.skill == 2321008) {
            SkillFactory.getSkill(2321001).getEffect(player.getSkillLevel(2321008)).applyTo(player);
        }

        if (attack.skill != 400021047 && attack.skill != 400051007 && attack.skill != 21110016 && attack.skill != 400051013
                && effect != null && attack.skill != 2101010 && attack.skill != 2100010 && attack.skill != 2121054
                && attack.skill != 2321001 && attack.skill != 2221012) {
            effect.applyTo(player, attackPos);
        }
        if (attack.skill != 2221011 && attack.skill != 2221012) {
            player.send(MainPacketCreator.SkillUseResult((byte) 0));
        }
        if (attack.skill == 142121004) {

            player.acaneAim = attack.targets;
            SkillFactory.getSkill(142121004).getEffect(player.getSkillLevel(142121004)).applyTo(player);
        }

        if ((attack.skill != 0 && attack.skill != 142111002 && attack.skill != 142110003 && attack.skill != 142001002 && attack.skill != 142111007 && attack.skill != 142120000 && attack.skill != 142120001
                && attack.skill != 142120002 && attack.skill != 400021048 && attack.skill != 400021008) && (GameConstants.isKinesis(player.getJob()))) {
            if (player.getSkillLevel(142120033) > 0 && isBoss) {
                player.givePPoint(effect, true);
            } else {
                player.givePPoint(effect, false);
            }
        }

        if (mobs > 0) {
            MapleCharacter from = player;
            if (from.getMonsterCombo() == 0) {
                from.setMonsterComboTime(System.currentTimeMillis());
            }
            if (from.getMonsterComboTime() < System.currentTimeMillis() - 10000) {
                from.setMonsterCombo(0);
            }
            from.addMonsterCombo(1);
            if (from.getMonsterCombo() > 1) {
                from.send(MainPacketCreator.combokill(from.getMonsterCombo(), last));
            }
            from.setMonsterComboTime(System.currentTimeMillis());

            if (from.getMonsterCombo() % 50 == 0) {
                int combo = from.getMonsterCombo() / 50;
                int id = 2023484;
                if (combo >= 40) {
                    id = 2023669;
                } else if (combo >= 15) {
                    id = 2023495;
                } else if (combo >= 7) {
                    id = 2023494;
                }
                from.combokillexp = lastE * ServerConstants.defaultExpRate;
                Item toDrop = new Item(id, (byte) 0, (short) 1, (byte) 0);
                from.getMap().spawnItemDrop(from, from, toDrop, lastP, false, false, false, true, 0, 0);
            }
        }
    }

    private static void handlePickPocket(final MapleCharacter player, final MapleMonster mob, AttackPair oned) {
        ISkill skill = SkillFactory.getSkill(4211003);
        SkillStatEffect s = skill.getEffect(player.getSkillLevel(skill));
        for (final Pair<Long, Boolean> eachde : oned.attack) {
            final Long eachd = eachde.left;
            if (s.makeChanceResult()) {
                EtcTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (player.mesoCount < 15) {
                            player.getMap().spawnMesoDrop(1, new Point((int) (mob.getPosition().getX()), (int) (mob.getPosition().getY())), mob, player, true, (byte) 0);
                            player.mesoCount++;
                        }
                        player.addpocket();
                    }
                }, 100L);
            }
        }
    }

    public static AttackInfo parseDmg(MapleCharacter chr, ReadingMaple lea, short type, short type2) {
        AttackInfo ret = new AttackInfo();
        if (type == RecvPacketOpcode.RANGED_ATTACK.getValue()) {
            lea.readByte();
        }
        lea.readByte();
        int oid = 0;
        ret.tbyte = lea.readByte();
        ret.targets = (byte) ((ret.tbyte >>> 4) & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        lea.readInt();
        ret.skill = lea.readInt();
        ret.skillLevel = (byte) lea.readInt();
        
        if(ret.skill == 400051334) {
            lea.skip(38);
            int a = lea.readInt();
            if(a > 0) {
                lea.skip(21);
            }
            lea.skip(26);
        } else {
        if (true) {
            if ((type != RecvPacketOpcode.MAGIC_ATTACK.getValue() && type == type2) || type2 == RecvPacketOpcode.DMG_FLAME.getValue()) {
                lea.readByte();
            }
            lea.readInt();
            lea.readInt();
            lea.readByte();
            ret.slot = (byte) lea.readShort();
            ret.item = lea.readInt();
            lea.readByte();
            lea.readByte();
            lea.readByte(); // 
            int c = lea.readInt();
            for (int i = 0; i < c; i++) {
                lea.readInt();
            }
            lea.readInt(); //
            int value = -1;
            boolean check = lea.readByte() == 1;
            if (check) {
                value = lea.readInt(); // -1이면 멈추는데 코딩 귀찬
            }
            if (ret.skill == 155101104 || ret.skill == 155121306
                    /*스칼렛 차지드라이브 겉뎀수정*/ || ret.skill == 155101101 || ret.skill == 155101100 || ret.skill == 155101112
                    /*멈출 수 없는 본능 겉뎀수정*/ || ret.skill == 155101204 || ret.skill == 155101214
                    /*지워지지 않는 상처 겉뎀수정*/ || ret.skill == 155101200 || ret.skill == 155101201 || ret.skill == 155101212) {
                //lea.readInt();
                lea.readByte();
                lea.readInt();
                lea.readByte();
                lea.readInt();
                lea.readByte();
                lea.readInt(); // x
                lea.readInt(); // y
                lea.readInt();
                lea.readInt();
                lea.readInt(); // -1
            } else if (ret.skill == 155121341 || ret.skill == 155001100) {
                //lea.readInt();
                lea.readByte();
                lea.readInt();
                lea.readByte();
                lea.readInt(); // -1
            } else if (ret.skill == 155121041) {
                //lea.readInt();
                lea.readByte();
                lea.readByte();
                lea.readByte();
                lea.readInt();
                lea.readInt();
                lea.readInt();
                lea.readByte();
                lea.readInt();
                lea.readByte();
                lea.readInt(); // -1
            } /*else if (ret.skill == 400041039 || ret.skill == 400041042 || ret.skill == 400051049 || ret.skill == 400051050 || ret.skill == 400021072) {
                lea.readInt(); // -1
            } */ else if (ret.skill == 400051042) {
                //lea.readInt();
                lea.readByte();
                lea.readInt();
                lea.readInt();
                lea.readInt();
                lea.readInt();
                lea.readInt(); // -1
            } else if (check && value != -1) {
                while (value != -1) {
                    lea.readByte();
                    value = lea.readInt();
                    if (lea.available() < 26) {
                        break;
                    }
                }
            }

            if (ret.skill != 0 && (ret.skill == 22140024 || GameConstants.sub_6DEC70(ret.skill) || (type == RecvPacketOpcode.CLOSE_RANGE_ATTACK.getValue() && GameConstants.is_super_nova_skill(ret.skill)))) {
                ret.charge = lea.readInt();
            } else {
                ret.charge = 0;
            }

            if (ret.skill == 155121341 || ret.skill == 400011072 || ret.skill == 400041039 || ret.skill == 400021072
                    || ret.skill == 400031024 || ret.skill == 400011068 || ret.skill == 400031036 || ret.skill == 400051334
                    /*에인션트 아스트라 겉뎀수정*/ || ret.skill == 3321036 || ret.skill == 3321040 || ret.skill == 400051041) {
                ret.charge = lea.readInt();
            }

            if (type == RecvPacketOpcode.CLOSE_RANGE_ATTACK.getValue()) {
                if (GameConstants.sub_73E770(ret.skill) || ret.skill == 5300007 || ret.skill == 27120211
                        || ret.skill == 14111023 || ret.skill == 400031003 || ret.skill == 400031004
                        || ret.skill == 64101002 || ret.skill == 64101008 || ret.skill == 400031024) {
                    lea.readInt();
                }
            }
            if (type != RecvPacketOpcode.MAGIC_ATTACK.getValue()) {
                if (GameConstants.isZeroSkill(ret.skill)) {
                    ret.assist = lea.readByte();
                }

                if (GameConstants.is_userclone_summoned_able_skill(ret.skill)) {
                    lea.readInt();
                }
                if (ret.skill == 23121052) {
                    lea.readInt();
                }

            }

            if (type == RecvPacketOpcode.CLOSE_RANGE_ATTACK.getValue()) {
                if (ret.skill == 400031010 || ret.skill == 400041019 || ret.skill == 400040008) {
                    lea.readInt();
                    lea.readInt();
                }
            }

            ret.flag1 = lea.readByte();
            ret.flag2 = lea.readByte();
            byte flag3 = 0;
            if (type == RecvPacketOpcode.RANGED_ATTACK.getValue()) {
                lea.readInt();
                flag3 = lea.readByte();

                if (ret.skill == 95001000 || ret.skill == 3111013 || ret.skill == 400051025 || ret.skill == 5311010) {
                    lea.readInt();
                    lea.readShort();
                    lea.readShort();
                }
            }
            ret.display = lea.readByte();
            ret.animation = lea.readByte();
            lea.readInt();
            lea.readByte();

            if (type == RecvPacketOpcode.RANGED_ATTACK.getValue()) {
                if (ret.skill == 23111001 || ret.skill == 80001915 || ret.skill == 36111010) {
                    lea.readInt();
                    lea.readInt();
                    lea.readInt();
                }
            }

            if (type == RecvPacketOpcode.MAGIC_ATTACK.getValue()) {
                if (GameConstants.is_evan_force_skill(ret.skill)) {
                    lea.readByte();
                }
            }

            ret.speed = lea.readByte();
            ret.lastAttackTickCount = lea.readInt();
            lea.readInt();
            if (type == RecvPacketOpcode.RANGED_ATTACK.getValue()) {
                int lastFinalAttackSkillID = lea.readInt();
                lea.readShort();
                lea.readByte();
                lea.readShort();
                lea.readShort();
                lea.readShort();
                lea.readShort();
                if (lastFinalAttackSkillID > 0) {
                    lea.readByte();
                }

            } else if (type == RecvPacketOpcode.CLOSE_RANGE_ATTACK.getValue() && type2 != RecvPacketOpcode.PASSIVE_ENERGY.getValue() && type2 != RecvPacketOpcode.SPOTLIGHT_ATTACK.getValue()) {
                int lastFinalAttackSkillID = lea.readInt();

                if (lastFinalAttackSkillID > 0) {
                    lea.readByte();
                }

                if (ret.skill == 5111009) {
                    lea.readByte();
                }

                if (ret.skill == 25111005) {
                    lea.readInt();
                }
            }

            if (ret.skill == 5220023 || ret.skill == 5220024 || ret.skill == 5220025 || ret.skill == 5221022) {
                lea.readInt();
                lea.readInt();
            }
        }
        }
        ret.allDamage = new ArrayList<AttackPair>();
        List<Pair<Long, Boolean>> allDamageNumbers;
        byte hitAction;
        for (int i = 0; i < ret.targets; i++) {
            oid = lea.readInt();
            hitAction = lea.readByte();
            lea.readByte();
            lea.readByte();
            lea.readByte();
            lea.readByte();
            lea.readInt();
            lea.readByte();
            lea.readShort();
            lea.readShort();
            lea.readShort();
            lea.readShort();

            if (type == RecvPacketOpcode.MAGIC_ATTACK.getValue()) {
                lea.readByte();
            }
            allDamageNumbers = new ArrayList<>();

            if (type == RecvPacketOpcode.MAGIC_ATTACK.getValue() && ret.skill == 80001835) {
                int cc = lea.readByte();

                for (int ii = 0; ii < cc; ii++) {
                    lea.readLong();
                }
            } else {
                lea.readShort();
            }
            lea.readInt();
            lea.readInt();

            for (int j = 0; j < ret.hits; j++) {
                long damage = lea.readLong();
                if (damage < 0) {
                    damage = damage & 0xFFFFFFFFL;
                }
                allDamageNumbers.add(new Pair<>(damage, false));
            }
            lea.readInt();
            lea.readInt();
            if (ret.skill == 142120001 || ret.skill == 142120002) {
                lea.readInt();
                lea.readInt();
            }
            lea.readByte();
            lea.readByte();

            lea.readShort();
            lea.readShort();
            lea.readShort();
            lea.readShort();
            lea.readShort(); //
            lea.readShort(); //

            ret.allDamage.add(new AttackPair(oid, allDamageNumbers, hitAction));
        }

        if (ret.skill == 61121052 || ret.skill == 36121052 || GameConstants.is_screen_center_attack_skill(ret.skill) || ret.skill == 400041024) {
            ret.position = lea.readPos();
        } else {
            if (GameConstants.is_super_nova_skill(ret.skill)) {
                ret.position = lea.readPos();
            } else if (ret.skill == 101000102) {
                ret.position = lea.readPos();
            } else if (ret.skill == 400051025) {
                ret.position = lea.readPos();
            } else {
                ret.position = lea.readPos();
            }
        }
        if (ret.skill == 13111020) {
            ret.plusPosition = lea.readPos();
        }

        if (ret.skill == 21120019 || ret.skill == 37121052 || GameConstants.sub_6E0060(ret.skill) || ret.skill == 11121014 || ret.skill == 5101004) {
            lea.skip(1);
            ret.plusPosition = new Point(lea.readInt(), lea.readInt());
        }

        if ((GameConstants.is_evan_force_skill(ret.skill) || GameConstants.isEvan(ret.skill / 10000)) && ret.skill != 22110025 && ret.skill != 22110024 && ret.skill != 22110014 && ret.skill != 22171080 && ret.skill != 22171083) {
            lea.readByte();
            lea.readPos();
            lea.readPos();
            lea.readByte();
            ret.nMoveAction = lea.readByte();
            ret.bShowFixedDamage = lea.readByte();
        }

        if (type == RecvPacketOpcode.CLOSE_RANGE_ATTACK.getValue()) {
            if (ret.skill == 400020009 /*|| ret.skill == 4210014*/ || ret.skill == 400020010 || ret.skill == 400020011 || ret.skill == 64111012 || ret.skill == 400021029 || ret.skill == 400021053) {
                lea.readInt();
                lea.readShort();
                lea.readShort();
                lea.readByte();
            }
        }

        if (ret.skill == 64001009 || ret.skill == 64001010 || ret.skill == 64001011) {
            lea.readByte();
            ret.plusPosition = lea.readPos();
        }
        return ret;
    }
}
