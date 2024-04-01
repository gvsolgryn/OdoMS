importPackage(Packages.database);
importPackage(java.lang);

var Time = new Date();
var Year = Time.getFullYear() + "";
var Month = Time.getMonth() + 1 + "";
var Date = Time.getDate() + "";
if (Month < 10) {
    Month = "0" + Month;    
}
if (Date < 10) {
    Date = "0" + Date;    
}
var Today = parseInt(Year + Month + Date);

var boss_map = 350111399;
var outmap = 993059201;

function init() {
    em.setProperty("entry", "true");
}

function monsterValue(eim, mobId) {
    return 1;
}

function setup(eim) {
    em.getProperties().clear();
    var eim = em.newInstance("WorldBoss");
    var map = eim.setInstanceMap(boss_map); //측정맵
    // 아래 테스트용도
    // map.resetFully();
    // map.killMonster(9300800); //몬스터코드
    // var mob = em.getMonster(9300800); //몬스터코드
    // mob.setHp(9000000000000000000);
    // eim.registerMonster(mob);
    // map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(-325, 142)); //좌표
    eim.startEventTimer(180000); //시간 (1s = 1000)
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapFactory().getMap(boss_map); //측정맵
    player.changeMap(map, map.getPortal(0));
    em.setProperty("entry", "false");
}

function changedMap(eim, player, mapid) {
    if (mapid != boss_map) { //측정맵
        em.setProperty("entry", "true");
        eim.unregisterPlayer(player);
        eim.dispose();
    }
}

function playerRevive(eim, player) {}

function end(eim) {
    var player = eim.getPlayers().get(0);
    var map = eim.getMapFactory().getMap(outmap); //나갈맵v
    dbset(eim);
    player.changeMap(map, map.getPortal(0));
    eim.unregisterPlayer(player);
    eim.dispose();
}

function scheduledTimeout(eim) {
    var player = eim.getPlayers().get(0);
    if(player.WorldbossDamage != 0){
        player.dropMessage(5, "총 나의 데미지 : " + ConvertNumber(player.WorldbossDamage));
    }
    end(eim);
} 

function playerDead(eim, player) {
    dbset(eim);
    end(eim);
}
function playerDisconnected(eim, player) {
    end(eim);
    return 0;
}
function allMonstersDead(eim) {}
function cancelSchedule() {}
function leftParty(eim, player) {}
function disbandParty(eim, player) {}


function ConvertNumber(number) { 
    var inputNumber  = number < 0 ? false : number;
    var unitWords    = ['', '만 ', '억 ', '조 ', '경 '];
    var splitUnit    = 10000;
    var splitCount   = unitWords.length;
    var resultArray  = [];
    var resultString = '';
    if (inputNumber == false) {
        //cm.sendOk("오류가 발생하였습니다. 다시 시도해 주세요.\r\n(파싱오류)");
        //cm.dispose();
        resultString = '0';
        return resultString;
    }
    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
        unitResult = Math.floor(unitResult);
        if (unitResult > 0){
            resultArray[i] = unitResult;
        }
    }
    for (var i = 0; i < resultArray.length; i++) {
        if(!resultArray[i]) continue;
        resultString = String(resultArray[i]) + unitWords[i] + resultString;
    }
    return resultString;
}

function dbset(eim){
    var player = eim.getPlayers().get(0);
    try {
        var damage = 0;
		var name = null;
        em.setProperty("entry", "true");
        //if (player.getGMLevel() > 5) {
            var con = null;
            var ps = null;
            var rs = null;
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM world_boss_damage where date = "+ Today +" and characterid = ?");
            ps.setInt(1, player.getId());
            rs = ps.executeQuery();
            if(rs.next()) {
                damage = rs.getLong("damage");
                name = rs.getString("name");
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("SELECT * FROM world_boss_team where characterid = ?");
            ps.setInt(1, player.getId());
            rs = ps.executeQuery();
            if(rs.next()) {
                teamid = rs.getInt("teamid");
                teamname = rs.getString("teamname");
            }
            rs.close();
            ps.close();
            if(name == null){
                ps = con.prepareStatement("INSERT INTO world_boss_damage(teamid, teamname, characterid, name, damage, date) VALUES(?,?,?,?,?,?)");
                ps.setInt(1, teamid);
                ps.setString(2, teamname);
                ps.setInt(3, player.getId());
                ps.setString(4, player.getName());
                ps.setLong(5, player.WorldbossDamage);
                ps.setInt(6, Today);
                ps.executeUpdate();
                ps.close();
            } else {
                ps = con.prepareStatement("UPDATE world_boss_damage SET damage = ? where date = "+ Today +" and characterid = "+ player.getId());
                ps.setLong(1, player.WorldbossDamage);
                ps.executeUpdate();
                ps.close();
            }
            
            con.close();
        //}
    } catch (e) {
        player.dropMessage(5, "오류가 발생하였습니다.\r\n" + e);
        return;
    } finally {
        if (ps != null) {
            try {
                ps.close();
            } catch (e) {

            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (e) {

            }
        }
    }
}