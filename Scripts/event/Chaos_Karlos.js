importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 410005005;
var time = 0;
var bb = 0;
function init() { }

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Chaos_Karlos" + a) != null) {
        a = Randomizer.nextInt();
    }
    bb = map;
    var eim = em.newInstance("Chaos_Karlos" + a);
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 20).resetFully();
    eim.setInstanceMap(map + 100).resetFully();
    eim.setInstanceMap(map + 60).resetFully();
    eim.setInstanceMap(map + 120).resetFully(); // 마지막맵
    eim.setInstanceMap(map + 120).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1800000);
    eim.setProperty("stage", "0");
    var map = eim.getMapInstance(0);
    player.setDeathCount(5);
    player.changeMap(map, map.getPortal(0));
    eim.setProperty("stage", "1");
    player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossKalos/1phase_spine/skeleton", "animation", 0, false, ""));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Sound/SoundEff.img/bossKalos/1phase"));
    if (player.getParty().getLeader().getId() == player.getId()) {
        eim.schedule("WarptoNextStage", 6850);
    }
}

function spawnMonster(eim, instance, mobid, x, y) {
    var map = eim.getMapInstance(instance);
    var mob = em.getMonster(mobid);
    eim.registerMonster(mob);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(x, y));
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    stage = parseInt(eim.getProperty("stage"));
    if (mapid != 410006000 && mapid != 410006020 && mapid != 410006060 && mapid != 410006080 && mapid != 410006100 && mapid != 410006120) {
        player.setDeathCount(0);
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
        player.dropMessageGM(5,"stage : " + stage);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    stage = parseInt(eim.getProperty("stage"));
    if (mobId == 8880800 && stage == 1) {
        eim.setProperty("stage", "2");
        eim.schedule("WarptoNextStage", 6850);
        map = eim.getMapInstance(stage);
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
        map.broadcastMessage(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs32##r#e아직 심판은 끝나지 않았다.", 100, 1000, 4, 0, 0, 1, 4));
        map.killMonster(8880800);
    } else if (mobId == 8880800) {
        eim.restartEventTimer(300000);
        eim.setProperty("stage", "4");
        eim.schedule("WarptoNextStage", 6850);
        map = eim.getMapInstance(stage);
    } else if (mobId == 8880803) {
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
        map.broadcastMessage(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs32##r#e여기까진가... 죄송합니다. 아버지...", 100, 1000, 4, 0, 0, 1, 4));
        eim.restartEventTimer(300000);
        eim.setProperty("stage", "5");
        eim.schedule("WarptoNextStage", 6850);
        map = eim.getMapInstance(stage);
        map.killMonster(8880803);
    }
    return 1;
}

function WarptoNextStage(eim) {
    var stage = parseInt(eim.getProperty("stage"));
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        map = eim.getMapInstance(stage);
        player.changeMap(map.getId(), 0);
        if (stage == 1) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        } else if (stage == 2) {
    	player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    	player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossKalos/2phase_spine/skeleton", "animation", 0, false, ""));
    	player.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Sound/SoundEff.img/kalos/2phase"));
        } else if (stage == 3) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        }
    }
    if (stage == 2) {
        eim.setProperty("stage", "3");
        eim.schedule("WarptoNextStage", 9500);
    } else if (stage == 3) {
        //2페
        spawnMonster(eim, stage, 8880803, 552, 399);
        eim.setProperty("stage", "4");
    } else if (stage == 1) {
        //1페
        spawnMonster(eim, stage, 8880800, 104, 398);
    } else if (stage == 4) {
        eim.setProperty("stage", "5");
        //eim.schedule("WarptoNextStage", 9500);
        spawnMonster(eim, stage, 8880808, -9, 305);
    } else if (stage ==5) {
        spawnMonster(eim, stage, 8880808, -9, 305);
    }
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    eim.disposeIfPlayerBelow(0, 0);
}

function end(eim) {
    eim.disposeIfPlayerBelow(100, outmap);
}


function clearPQ(eim) {
    end(eim);
}


function disposeAll(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        eim.unregisterPlayer(player);
        player.setDeathCount(0);
        player.changeMap(outmap, 0);
    }
    end(eim);
}

function allMonstersDead(eim) {
    //after ravana is dead nothing special should really happen
}

function leftParty(eim, player) {
    disposeAll(eim);
}

function disbandParty(eim) {
    disposeAll(eim);
}

function playerDead(eim, player) { }

function cancelSchedule() { }