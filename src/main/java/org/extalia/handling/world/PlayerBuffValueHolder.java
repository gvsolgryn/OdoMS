package org.extalia.handling.world;

import org.extalia.client.SecondaryStat;
import org.extalia.server.SecondaryStatEffect;
import org.extalia.tools.Pair;

import java.io.Serializable;
import java.util.Map;

public class PlayerBuffValueHolder implements Serializable {
  private static final long serialVersionUID = 9179541993413738569L;
  
  public long startTime;
  
  public int localDuration;
  
  public int cid;
  
  public SecondaryStatEffect effect;
  
  public Map<SecondaryStat, Pair<Integer, Integer>> statup;
  
  public PlayerBuffValueHolder(long startTime, SecondaryStatEffect effect, Map<SecondaryStat, Pair<Integer, Integer>> statup, int localDuration, int cid) {
    this.startTime = startTime;
    this.effect = effect;
    this.statup = statup;
    this.localDuration = localDuration;
    this.cid = cid;
  }
}
