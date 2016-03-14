package org.dementhium.task.impl;

import org.dementhium.RS2ServerBootstrap;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.task.Task;

/**
 * @author Graham Edgecombe
 */
public class SessionLoginTask implements Task {

    private final Player player;

    public SessionLoginTask(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        World.getWorld().register(player);
    }

}
