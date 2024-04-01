var enter = "\r\n";
var seld = -1, seld2 = -1;

var cate = ["잔잔한", "경쾌한", "신나는"]

var cast = false;

var music = [
    //["Title", "Sound/Bgm48.img/Title", 0], // 3번째 숫자는 cate의 index
    ["Elfwood", "Sound/Bgm03.img/Elfwood", 0],
    ["WhiteChristams", "Sound/Bgm04.img/WhiteChristams", 0],
    ["WarmREgard", "Sound/Bgm04.img/WarmREgard", 0],
    ["ElinForest", "Sound/Bgm15.img/ElinForest", 0],
    ["QueensGarden", "Sound/Bgm18.img/QueensGarden", 0],
    ["WindAndFlower", "Sound/Bgm25.img/WindAndFlower", 0],
    ["TimeTempleInMirror", "Sound/Bgm33.img/TimeTempleInMirror", 0],
    ["LeafreInMirror", "Sound/Bgm33.img/LeafreInMirror", 0],
    ["MureungHilllnMirror", "Sound/Bgm33.img/MureungHilllnMirror", 0],
    ["RabbitsDream", "Sound/Bgm33.img/RabbitsDream", 0],
    ["PromiseOfHeaven_vocaless", "Sound/Bgm33.img/PromiseOfHeaven_vocaless", 0],
    ["This too shall pass away", "Sound/Bgm43.img/This too shall pass away", 0],
    ["Nowhere", "Sound/Bgm48.img/Nowhere", 0],
    ["EveningPrimrose", "Sound/Bgm48.img/EveningPrimrose", 0],
    ["Memory", "Sound/Bgm48.img/Memory", 0],
    ["SecretElodin", "Sound/Bgm51.img/SecretElodin", 0],
    ["PlayWithMe", "Sound/Bgm04.img/PlayWithMe", 1],
    ["Fantasia", "Sound/Bgm07.img/Fantasia", 1],
    ["FairyTalediffvers", "Sound/Bgm09.img/FairyTalediffvers", 1],
    ["BlueWorld", "Sound/Bgm11.img/BlueWorld", 1],
    ["ShiningSea", "Sound/Bgm11.img/ShiningSea", 1],
    ["TowerOfGoddess", "Sound/Bgm13.img/TowerOfGoddess", 1],
    ["battleBGMTypeC", "Sound/Bgm24.img/battleBGMTypeC", 1],
    ["PeacefulWoods", "Sound/Bgm26.img/PeacefulWoods", 1],
    ["helisiumMysticforest", "Sound/Bgm28.img/helisiumMysticforest", 1],
    ["StarPlaneWaitField", "Sound/Bgm39.img/StarPlaneWaitField", 1],
    ["GoFight!ShowYourEnergy", "Sound/Bgm34.img/GoFight!ShowYourEnergy!", 1],
    ["neneSound", "Sound/Bgm00.img/neneSound", 2],
    ["RaindropFlower", "Sound/Bgm18.img/RaindropFlower", 2],
    ["WolfAndSheep", "Sound/Bgm18.img/WolfAndSheep", 2],
    ["RienVillage", "Sound/Bgm19.img/RienVillage", 2],
    ["KerningSquareField", "Sound/Bgm21.img/KerningSquareField", 2],
    ["MPBonusMap", "Sound/Bgm23.img/MPBonusMap", 2],
    ["battleEntrance", "Sound/Bgm24.img/battleEntrance", 2],
    ["battleBGMTypeB", "Sound/Bgm24.img/battleBGMTypeB", 2],
    ["PantheonField", "Sound/Bgm27.img/PantheonField", 2],
    ["5thSpotLightFlyAway", "Sound/Bgm30.img/5thSpotLightFlyAway", 2],
    ["scholarsLibrary", "Sound/Bgm31.img/scholarsLibrary", 2],
    ["SmileZero", "Sound/Bgm31.img/SmileZero", 2],
    ["FlowerVioleta", "Sound/Bgm38.img/FlowerVioleta", 2],
    ["Catch Your Dreams! full", "Sound/Bgm38.img/Catch Your Dreams! full", 2],
    ["God of Control - stage", "Sound/Bgm45.img/God of Control - stage", 2],
    ["ChewChew MainTheme", "Sound/Bgm46.img/ChewChew MainTheme", 2],
    ["ChewChew WildWorld", "Sound/Bgm46.img/ChewChew WildWorld", 2],
    ["VEvent", "Sound/BgmEvent2.img/VEvent", 2],
    ["Mashup", "Sound/BgmEvent2.img/Mashup", 2]

]

function start() {
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, sel) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        var msg = "#fs11#갈매기 음악 감상실에 오신 것을 환영해요.\r\n듣고 싶은 장르를 선택해주세요." + enter;
        for (i = 0; i < cate.length; i++)
            msg += "#L" + i + "#" + cate[i] + " 음악" + enter;
        cm.sendSimple(msg);
    } else if (status == 1) {
        seld = sel;
        var msg = "#fs11#원하시는 음악을 선택하세요.#fs11##b" + enter;
        for (i = 0; i < music.length; i++) {
            if (music[i][2] == seld)
                msg += "#L" + i + "#" + music[i][0] + enter;
        }
        cm.sendSimple(msg);
    } else if (status == 2) {
        seld2 = sel;
        var msg = "#fs11#선택하신 음악을 해당 맵에 있는 모든 유저에게 재생하시겠어요?#b" + enter;
        msg += "#L1##fs11#네, 모든 유저에게 들려줄래요." + enter;
        msg += "#L2##fs11#아니오, 저 혼자만 들을래요." + enter;

        cm.sendSimple(msg);
    } else if (status == 3) {
        cast = sel == 1 ? true : false;
        cm.sendYesNo("정말로 " + music[seld2][0] + "을(를) 재생하시겠어요?");
    } else if (status == 4) {
        cm.changeMusic(cast, music[seld][1]);
        cm.dispose();
    }
}