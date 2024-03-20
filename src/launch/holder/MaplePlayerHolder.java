package launch.holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import client.MapleCharacter;
import constants.subclasses.CheaterData;
import launch.helpers.ChracterTransfer;

public class MaplePlayerHolder {

    private final ReentrantReadWriteLock mutex = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock mutex2 = new ReentrantReadWriteLock();
    private final Lock rL = mutex.readLock(), wL = mutex.writeLock();
    private final Lock wL2 = mutex2.writeLock();
    private final Map<String, MapleCharacter> nameToChar = new HashMap<>();
    private final Map<Integer, MapleCharacter> idToChar = new HashMap<>();
    private final Map<Integer, ChracterTransfer> PendingCharacter = new HashMap<>();
    private final int channel;
    private final Map<Integer, Object> effects = new HashMap<Integer, Object>();

    public MaplePlayerHolder(int channel) {
        this.channel = channel;
    }

    public final Object getEffect(final int id) {
        return effects.get(id);
    }

    public final ArrayList<MapleCharacter> getAllCharacters() {
        rL.lock();
        try {
            return new ArrayList<>(idToChar.values());
        } finally {
            rL.unlock();
        }
    }

    public final void registerPlayer(final MapleCharacter chr) {
        wL.lock();
        try {
            nameToChar.put(chr.getName().toLowerCase(), chr);
            idToChar.put(chr.getId(), chr);
            if (effects.get(chr.getId()) == null) {
                effects.put(chr.getId(), new Object());
            }
        } finally {
            wL.unlock();
        }
    }

    public final void registerPendingPlayer(final ChracterTransfer chr, final int playerid) {
        wL2.lock();
        try {
            PendingCharacter.put(playerid, chr);
        } finally {
            wL2.unlock();
        }
    }

    public final void deregisterPlayer(final MapleCharacter chr) {
        wL.lock();
        try {
            nameToChar.remove(chr.getName().toLowerCase());
            idToChar.remove(chr.getId());
        } finally {
            wL.unlock();
        }
    }

    public final void deregisterPlayer(final int idz, final String namez) {
        wL.lock();
        try {
            nameToChar.remove(namez.toLowerCase());
            idToChar.remove(idz);
        } finally {
            wL.unlock();
        }
    }

    public final int pendingCharacterSize() {
        return PendingCharacter.size();
    }

    public final ChracterTransfer getPendingCharacter(final int charid) {
        wL2.lock();
        try {
            return PendingCharacter.remove(charid);
        } finally {
            wL2.unlock();
        }
    }

    public final MapleCharacter getCharacterByName(final String name) {
        rL.lock();
        try {
            return nameToChar.get(name.toLowerCase());
        } finally {
            rL.unlock();
        }
    }

    public final MapleCharacter getCharacterById(final int id) {
        rL.lock();
        try {
            return idToChar.get(id);
        } finally {
            rL.unlock();
        }
    }

    public final int getConnectedClients() {
        return idToChar.size();
    }

    public final List<CheaterData> getCheaters() {
        final List<CheaterData> cheaters = new ArrayList<>();

        rL.lock();
        try {
            final Iterator<MapleCharacter> itr = nameToChar.values().iterator();
            MapleCharacter chr;
            while (itr.hasNext()) {
                chr = itr.next();
            }
        } finally {
            rL.unlock();
        }
        return cheaters;
    }

    public final List<CheaterData> getReports() {
        final List<CheaterData> cheaters = new ArrayList<>();

        rL.lock();
        try {
            final Iterator<MapleCharacter> itr = nameToChar.values().iterator();
            MapleCharacter chr;
            while (itr.hasNext()) {
                chr = itr.next();
            }
        } finally {
            rL.unlock();
        }
        return cheaters;
    }

    public final void saveAll() {
        rL.lock();
        try {
            for (MapleCharacter hp : nameToChar.values()) {
                hp.saveToDB(false, false);
            }
        } finally {
            rL.unlock();
        }
    }

    public final void disconnectAll() {
        wL.lock();
        try {
            for (MapleCharacter chr : nameToChar.values()) {
                chr.getClient().disconnect(true, false); 
                chr.getClient().getSession().close();
            }
        } finally {
            wL.unlock();
        }
    }


    /*
        
	public final void disconnectAll()
	{
		disconnectAll(false);
	}

	public final void disconnectAll(final boolean checkGM)
        {
	wL.lock();
	try
        {
			final Iterator<MapleCharacter> itr = nameToChar.values().iterator();
			MapleCharacter chr;
			while (itr.hasNext())
            {
				chr = itr.next();

				if (!chr.isGM() || !checkGM)
				{
		chr.getClient().disconnect(true, false);
		chr.getClient().getSession().close();
					itr.remove();
                }
	    }
	}
        finally
        {
	    wL.unlock();
	}
    }

     */
    public final String getOnlinePlayers(final boolean byGM) {
        final StringBuilder sb = new StringBuilder();
        if (byGM) {
            rL.lock();
            try {
                final Iterator<MapleCharacter> itr = nameToChar.values().iterator();
                while (itr.hasNext()) {
                    sb.append(", ");
                }
            } finally {
                rL.unlock();
            }
        } else {
            rL.lock();
            try {
                final Iterator<MapleCharacter> itr = nameToChar.values().iterator();
                MapleCharacter chr;
                while (itr.hasNext()) {
                    chr = itr.next();

                    if (!chr.isGM()) {
                        sb.append(", ");
                    }
                }
            } finally {
                rL.unlock();
            }
        }
        return sb.toString();
    }

    public final void broadcastPacket(final byte[] data) {
        rL.lock();
        try {
            final Iterator<MapleCharacter> itr = nameToChar.values().iterator();
            while (itr.hasNext()) {
                itr.next().getClient().getSession().write(data);
            }
        } finally {
            rL.unlock();
        }
    }

    public final void broadcastSmegaPacket(final byte[] data) {
        rL.lock();
        try {
            final Iterator<MapleCharacter> itr = nameToChar.values().iterator();
            MapleCharacter chr;
            while (itr.hasNext()) {
                chr = itr.next();

                if (chr.getClient().isLoggedIn() && chr.getSmega()) {
                    chr.getClient().getSession().write(data);
                }
            }
        } finally {
            rL.unlock();
        }
    }

    public final void broadcastGMPacket(final byte[] data) {
        rL.lock();
        try {
            final Iterator<MapleCharacter> itr = nameToChar.values().iterator();
            MapleCharacter chr;
            while (itr.hasNext()) {
                chr = itr.next();
                if (chr.getClient().isLoggedIn()) {
                    chr.getClient().getSession().write(data);
                }
            }
        } finally {
            rL.unlock();
        }
    }

    private class PersistingTask implements Runnable {

        @Override
        public void run() {
            wL2.lock();
            try {
                final long currenttime = System.currentTimeMillis();
                final Iterator<Map.Entry<Integer, ChracterTransfer>> itr = PendingCharacter.entrySet().iterator();

                while (itr.hasNext()) {
                    if (currenttime - itr.next().getValue().TranferTime > 40000) {
                        System.out.println("Removing you from the pending character list!");
                        itr.remove();
                    }
                }
            } finally {
                wL2.unlock();
            }
        }
    }
}
