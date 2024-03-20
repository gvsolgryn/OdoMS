package constants.programs;

import client.MapleCharacter;
import launch.ChannelServer;

public class WorldTimerRunner implements Runnable {

    @Override
    public void run() {
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter hp : cserv.getPlayerStorage().getAllCharacters()) {
                Run(hp);
            }
        }
    }

    public static void Run(MapleCharacter player) {

    }

}
