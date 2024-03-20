package handler.channel;

import client.MapleCharacter;
import client.items.MapleInventoryType;
import packet.creators.MainPacketCreator;
import packet.creators.UIPacket;
import tools.RandomStream.Randomizer;
import tools.Timer;

public class FishingHandler {
    public static int Items[] = {2430996}; //미끼코드
    public static int FishingMap = 100000055;  //맵코드
    public static int FishingChair = 3010432;  //의자코드
    
    public static void GainFishing(final MapleCharacter chr) {
        chr.getClient().send(UIPacket.showWZEffect("Effect/PvPEff.img/GradeUp", 1));
        int itemids[] = {4001187,4001187,4001187,4001188,4001188,4001188,4001189,4001189,4001189};
        int itemid = itemids[Randomizer.nextInt(itemids.length)];
        if (Randomizer.isSuccess(70)) {
            chr.send(UIPacket.getItemTopMsg(itemid, (itemid == 4001187 ? "음치" : itemid == 4001188 ? "몸치" : itemid == 4001189 ? "박치" : "") + "을 낚으셨습니다."));
        chr.gainItem(itemid, (short) 2, false, -1, "낚시로 획득한 아이템");
        chr.gainItem(2430996, (short) -1, false, -1, null);
        } else if (Randomizer.isSuccess(30)) {
                  chr.gainItem(2430996, (short) -1, false, -1, null);
                  chr.getClient().getSession().write(MainPacketCreator.sendHint("아무것도 건지지 못했습니다.", 250, 5));
        }
        if (Randomizer.nextInt(10) <= 3) { 
        }
    }
    
    public static void StartFishing(final MapleCharacter chr) {
        if (chr.getInventory(MapleInventoryType.ETC).getNumFreeSlot() < 5) {
           chr.Message("기타 탭의 공간을 5칸이상 비워주신 후 다시 해주세요.");
           return;
        }
        if (!chr.haveItem(2430996)) {
           chr.Message("아맞다 미끼를 구매안했네?");
           return;
        }
        chr.setFishing(true);
        chr.send(MainPacketCreator.getClock(15));
        Timer.CloneTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (chr.getChair() == FishingChair && chr.getMapId() == FishingMap) {
                    GainFishing(chr);
                    StartFishing(chr);
                } else {
                    StopFishing(chr);
                }
            }
        }, 15000);
    }

 
    public static void StopFishing(final MapleCharacter chr) {
        Timer.BuffTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                chr.setFishing(false);
            }
        }, 5000);
        chr.send(MainPacketCreator.getClock(0));
    }
}
