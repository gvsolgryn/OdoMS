package org.extalia.server;

public interface ShutdownServerMBean extends Runnable {
  void shutdown();
}
