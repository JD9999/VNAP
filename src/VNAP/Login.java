/*
 * Copyright 2017 Alvaro Stagg [alvarostagg@protonmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package VNAP;

import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;

import java.util.Arrays;
import java.util.List;

public class Login implements MC_Command {
    @Override
    public String getCommandName() {
        return "login";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("entrar");
    }

    @Override
    public String getHelpLine(MC_Player mc_player) {
        return ChatColor.GOLD + "/login <Pass>" + ChatColor.WHITE + " -- Login into the server";
    }

    @Override
    public void handleCommand(MC_Player mc_player, String[] args) {
        if (args.length > 1)
            mc_player.sendMessage(this.getHelpLine(mc_player));
        else {
            String password = new Hash(args[0]).getHashedPassword();
            final String playerName = mc_player.getName();

            if (MyPlugin.inPlayers.contains(playerName)) {
                mc_player.sendMessage(ChatColor.RED + "You'r already login!");
                mc_player.setInvulnerable(false);
                return;
            }

            Connect conn = new Connect();
            boolean authed = conn.AuthUser(mc_player.getName(), password);

            if (authed) {
                MyPlugin.inPlayers.add(mc_player.getName());
                mc_player.sendMessage(ChatColor.GREEN + "Welcome back, " + ChatColor.BOLD + playerName
                        + ChatColor.RESET + ChatColor.GREEN + "!");
                mc_player.setInvulnerable(false);
            } else
                mc_player.sendMessage(ChatColor.RED + "Incorrect user or password. " + ChatColor.RED + "Please, try again");

            conn.Close();
        }
    }

    @Override
    public boolean hasPermissionToUse(MC_Player mc_player) {
        return mc_player == null ? false : true;
    }

    @Override
    public List<String> getTabCompletionList(MC_Player mc_player, String[] strings) {
        return null;
    }
}
