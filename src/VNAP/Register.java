package VNAP;

import PluginReference.ChatColor;
import PluginReference.MC_Command;
import PluginReference.MC_Player;

import java.util.Arrays;
import java.util.List;

public class Register implements MC_Command {
    @Override
    public String getCommandName() {
        return "register";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("registrar");
    }

    @Override
    public String getHelpLine(MC_Player mc_player) {
        return ChatColor.GOLD + "/register <Contraseña> <Confirmar contraseña>" + ChatColor.WHITE + " -- Registra tu usuario";
    }

    @Override
    public void handleCommand(MC_Player mc_player, String[] args) {
        if (args.length != 2)
            mc_player.sendMessage(this.getHelpLine(mc_player));
        else {
            String firstPassword = new Hash(args[0]).getHashedPassword(),
                   confirmedPassword = new Hash(args[1]).getHashedPassword();

            String playerName = mc_player.getName();

            if (!firstPassword.equals(confirmedPassword)) {
                mc_player.sendMessage(ChatColor.RED + "Las contraseñas no coinciden. Intenta de nuevo.");
                return;
            }

            Connect conn = new Connect();
            if (conn.CheckIfExists(playerName) > 0) {
                mc_player.sendMessage(ChatColor.RED + "Ya te encuentras registrado !");
                mc_player.sendMessage(ChatColor.RED + "Inicia sesión con /login <Contraseña>");
            } else {
                if (!MyPlugin.inPlayers.contains(mc_player.getName())) {
                    conn.RegisterUser(playerName, firstPassword);
                    MyPlugin.inPlayers.add(mc_player.getName());
                    mc_player.sendMessage(ChatColor.GREEN + "Bienvenido a " + MyPlugin.serverName + " !");
                } else
                    mc_player.sendMessage(ChatColor.RED + "Ya te encuentras logueado.");

                mc_player.setInvulnerable(false);
            }

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
