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
package server.life;

import java.awt.Point;
import java.util.concurrent.atomic.AtomicInteger;

import server.MapleCarnivalFactory;
import server.MapleCarnivalFactory.MCSkill;
import server.maps.MapleMap;
import server.maps.MapleReactor;
import server.maps.MapleSummon;
import server.MapleStatEffect;
import client.SkillFactory;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import handling.channel.ChannelServer;
import tools.MaplePacketCreator;
import java.util.Map;
import server.Randomizer;

public class SpawnPoint extends Spawns {

    private MapleMonsterStats monster;
    private Point pos;
    private long nextPossibleSpawn;
    private int mobTime, carnival = -1, fh, f, id, level = -1;
    private AtomicInteger spawnedMonsters = new AtomicInteger(0);
    private String msg;
    private byte carnivalTeam;

    public SpawnPoint(final MapleMonster monster, final Point pos, final int mobTime, final byte carnivalTeam, final String msg) {
        this.monster = monster.getStats();
        this.pos = pos;
        this.id = monster.getId();
        this.fh = monster.getFh();
        this.f = monster.getF();
        this.mobTime = (mobTime < 0 ? -1 : (mobTime * 1000));
        this.carnivalTeam = carnivalTeam;
        this.msg = msg;
        this.nextPossibleSpawn = System.currentTimeMillis();
    }

    public final void setCarnival(int c) {
        this.carnival = c;
    }

    public final void setLevel(int c) {
        this.level = c;
    }

    public final int getF() {
        return f;
    }

    public final int getFh() {
        return fh;
    }
    
    public void setRespawnTime() {
        nextPossibleSpawn = System.currentTimeMillis();
        try {
            if (mobTime > 0) {
                nextPossibleSpawn += Randomizer.rand(1800000, mobTime);
            }
        } catch (IllegalArgumentException aaa) {
            nextPossibleSpawn += 60000L;
        }
    }    
    
    @Override
    public final boolean isFieldBoss() {
        return false;
    }

    @Override
    public final Point getPosition() {
        return pos;
    }

    @Override
    public final MapleMonsterStats getMonster() {
        return monster;
    }

    @Override
    public final byte getCarnivalTeam() {
        return carnivalTeam;
    }

    @Override
    public final int getCarnivalId() {
        return carnival;
    }

    @Override
    public final boolean shouldSpawn(long time) {
        if (mobTime < 0) {
            return false;
        }
        // regular spawnpoints should spawn a maximum of 3 monsters; immobile spawnpoints or spawnpoints with mobtime a
        // maximum of 1
        if (((mobTime != 0 || !monster.getMobile()) && spawnedMonsters.get() > 0) || spawnedMonsters.get() > 1) {
            return false;
        }
        return nextPossibleSpawn <= time;
    }

    @Override
    public final MapleMonster spawnMonster(final MapleMap map) {
        final MapleMonster mob = new MapleMonster(id, monster);
        mob.setPosition(pos);
        mob.setCy(pos.y);
        mob.setRx0(pos.x - 50);
        mob.setRx1(pos.x + 50); //these dont matter for mobs
        mob.setFh(fh);
        mob.setF(f);
        mob.setCarnivalTeam(carnivalTeam);
        
        if (ChannelServer.isElite(map.getChannel())) {
            if (mob.getStats().getLevel() >= 170) {
                long initHP = Math.max(mob.getMobMaxHp(), Math.min(mob.getMobMaxHp() * 10, Long.MAX_VALUE));
                int initEXP = (int) Math.max(mob.getMobExp(), Math.min((long) mob.getMobExp() * 5, Integer.MAX_VALUE));

                OverrideMonsterStats overrideStats = new OverrideMonsterStats(initHP, mob.getMobMaxMp(), initEXP, false);
                mob.setHp(initHP);
                mob.setOverrideStats(overrideStats);
            }
        }
        
        spawnedMonsters.incrementAndGet();

        mob.addListener(new MonsterListener() {
        
            @Override
            public void monsterKilled() {
                int d = 4000;
                int d2 = 4000 / 10;
                int maxLowInterval = (int) (d * 0.5);
                d = Math.max(maxLowInterval, d - map.getCharactersSize() * d2);
                nextPossibleSpawn = d;
                if (carnivalTeam > -1) {
                    nextPossibleSpawn = 3000;
                }
                if (Randomizer.nextBoolean()) {
                    nextPossibleSpawn *= 1;
                }
                switch (map.getId()) {
                    case 230040000: //깊협1
                    case 230040100: //깊협2
                    case 230040200: //위협1
                    case 230040300: //위협2
                        nextPossibleSpawn *= 1;
                        break;
                    case 240040520: //망둥
                    case 240040521: //위둥
                    case 240040511: //남둥
                    case 240040510: //죽둥
                    case 240040310: //레와 둥지
                    case 240040210: //블와 둥지
                    case 240040400: //와이번의 협곡
                        nextPossibleSpawn /= 1;
                        break;
                }
                nextPossibleSpawn += System.currentTimeMillis();
                if (mobTime > 0) {
                    nextPossibleSpawn += mobTime;
                }
                spawnedMonsters.decrementAndGet();
                map.monsterKilled();
            }
        });
        map.spawnMonster(mob, -2);
        if (carnivalTeam > -1) {
            for (MapleReactor r : map.getAllReactorsThreadsafe()) { //parsing through everytime a monster is spawned? not good idea
                if (r.getName().startsWith(String.valueOf(carnivalTeam)) && r.getReactorId() == (9980000 + carnivalTeam) && r.getState() < 5) {
                    final int num = Integer.parseInt(r.getName().substring(1, 2)); //00, 01, etc
                    final MCSkill skil = MapleCarnivalFactory.getInstance().getGuardian(num);
                    if (skil != null) {
                        skil.getSkill().applyEffect(null, mob, false, (short) 0);
                    }
                }
            }
        }
        for (MapleSummon s : map.getAllSummonsThreadsafe()) {
            if (s.getSkill() == 35111005) {
                final MapleStatEffect effect = SkillFactory.getSkill(s.getSkill()).getEffect(s.getSkillLevel());
                for (Map.Entry<MonsterStatus, Integer> stat : effect.getMonsterStati().entrySet()) {
                    mob.applyStatus(s.getOwner(), new MonsterStatusEffect(stat.getKey(), stat.getValue(), s.getSkill(), null, false), false, effect.getDuration(), true, effect);
                }
                break;
            }
        }
        if (msg != null) {
            map.broadcastMessage(MaplePacketCreator.serverNotice(6, msg));
        }
        return mob;
    }

    @Override
    public final int getMobTime() {
        return mobTime;
    }
}
