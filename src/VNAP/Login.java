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
        return ChatColor.GOLD + "/login <Contraseña>" + ChatColor.WHITE + " -- Entra en el servidor !";
    }

    @Override
    public void handleCommand(MC_Player mc_player, String[] args) {
        if (args.length > 1)
            mc_player.sendMessage(this.getHelpLine(mc_player));
        else {
            String password = new Hash(args[0]).getHashedPassword();
            final String playerName = mc_player.getName();

            if (MyPlugin.inPlayers.contains(playerName)) {
                mc_player.sendMessage(ChatColor.RED + "Ya te encuentras logueado !");
                mc_player.setInvulnerable(false);
                return;
            }

            Connect conn = new Connect();
            boolean authed = conn.AuthUser(mc_player.getName(), password);

            if (authed) {
                MyPlugin.inPlayers.add(mc_player.getName());
                mc_player.sendMessage(ChatColor.GREEN + "Bienvenido de vuelta, " + ChatColor.BOLD + playerName
                        + ChatColor.RESET + ChatColor.GREEN + "!");
                mc_player.setInvulnerable(false);
            } else
                mc_player.sendMessage(ChatColor.RED + "Usuario o contraseña incorrecta." + ChatColor.RED + " Por favor, intenta nuevamente.");

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
