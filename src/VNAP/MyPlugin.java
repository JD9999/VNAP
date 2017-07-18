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

import PluginReference.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;

public class MyPlugin extends PluginBase {
    public static MC_Server server = null;

    public static String serverName = "";
    public static String dbUrl = "";
    public static String dbUsrName = "";
    public static String dbPassword = "";
    public static String dbTable = "";

    public static ArrayList<String> inPlayers = new ArrayList<String>();

    private MC_Location playerLocation;
    private final String fileName = "Config/db.json";
    private final String dbModel =
              "{"
            + "    \"server_name\": \"your server name\","
            + "    \"db\": \"db_name_here\","
            + "    \"user\": \"db_user_here\","
            + "    \"password\": \"db_password_here\","
            + "    \"table\": \"db_table_here\""
            + "}";
    private File jsonFile;

    @Override
    public void onStartup(MC_Server server) {
        this.server = server;

        JSONParser parser = new JSONParser();
        jsonFile = new File(fileName);

        try {
            if (!jsonFile.exists()) {
                System.out.println(" [*] " + fileName + " was not detected... Creating.");
                PrintWriter pw = null;

                try {
                    pw = new PrintWriter(jsonFile, "UTF-8");
                    pw.write(dbModel);
                    System.out.println(" [*] " + fileName + " created !");
                } catch (Exception e) {
                    System.out.println(" [*] " + fileName + " could not be created !");
                } finally {
                    if (pw != null)
                        pw.close();
                }
            } else
                System.out.println(" [*] " + fileName + " detected !");

            Object obj = parser.parse(new FileReader(fileName));
            JSONObject jsonObj = (JSONObject)obj;

            serverName = (String)jsonObj.get("server_name");
            dbUrl = (String)jsonObj.get("db");
            dbUsrName = (String)jsonObj.get("user");
            dbPassword = (String)jsonObj.get("password");
            dbTable = (String)jsonObj.get("table");
        } catch (Exception e) {
            e.printStackTrace();
        }

        server.registerCommand(new Register());
        server.registerCommand(new Login());

        System.out.println("=== VNAP loaded ===");
    }

    @Override
    public void onShutdown() {
        if (!inPlayers.isEmpty())
            inPlayers.clear();

        System.out.println("=== VNAP disabled ===");
    }

    private void printRequestLoginMessage(MC_Player mc_player) {
        mc_player.sendMessage(ChatColor.RED + "Por favor usa /login Contraseña para iniciar sesión.");
        mc_player.sendMessage(ChatColor.RED + "¿No tienes una cuenta? Puedes crearla utilizando:");
        mc_player.sendMessage(ChatColor.RED + "/register Contraseña Contraseña");
    }

    @Override
    public void onPlayerJoin(MC_Player player) {
        playerLocation = player.getLocation();

        Connect conn = new Connect();
        if (conn.CheckIfExists(player.getName()) < 1) {
            player.sendMessage(ChatColor.RED + "Bienvenido " +
                    ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.AQUA + player.getName() +
                    ChatColor.RESET + " a " + serverName + " !");
            player.sendMessage(ChatColor.RED + "Por favor, usa /register Contraseña Contraseña para crear una cuenta.");
        } else {
            player.sendMessage(ChatColor.RED + "Bienvenido de nuevo, " + ChatColor.AQUA + ChatColor.BOLD + ChatColor.UNDERLINE +
                    player.getName() + ChatColor.RESET + " !");
            player.sendMessage(ChatColor.RED + "Por favor usa /login Contraseña para iniciar sesión.");
        }

        player.setInvulnerable(true);
    }

    @Override
    public void onPlayerInput(MC_Player mc_player, String msg, MC_EventInfo ei) {
        if (!inPlayers.contains(mc_player.getName())) {
            if (!(msg.contains("/login") || msg.contains("/entrar") || msg.contains("/register") || msg.contains("/registrar"))) {
                ei.isCancelled = true;
                printRequestLoginMessage(mc_player);
            }
        }
    }

    @Override
    public void onAttemptPlayerMove(MC_Player mc_player, MC_Location locFrom, MC_Location locTo, MC_EventInfo ei) {
        if (!inPlayers.contains(mc_player.getName())) {
            if (!mc_player.getLocation().equals(playerLocation))
                mc_player.teleport(playerLocation);
        }
    }

    @Override
    public void onAttemptItemDrop(MC_Player mc_player, MC_ItemStack is, MC_EventInfo ei) {
        if (!inPlayers.contains(mc_player.getName())) {
            ei.isCancelled = true;
            printRequestLoginMessage(mc_player);
        }
    }

    @Override
    public void onAttemptItemPickup(MC_Player mc_player, MC_ItemStack is, boolean isXpOrb, MC_EventInfo ei) {
        if (!inPlayers.contains(mc_player.getName())) {
            ei.isCancelled = true;
            printRequestLoginMessage(mc_player);
        }
    }

    @Override
    public void onAttemptEntityInteract(MC_Player mc_player, MC_Entity ent, MC_EventInfo ei) {
        if (!inPlayers.contains(mc_player.getName())) {
            ei.isCancelled = true;
            printRequestLoginMessage(mc_player);
        }
    }

    @Override
    public void onInteracted(MC_Player plr, MC_Location loc, MC_ItemStack isHandItem) {
        plr.sendMessage(ChatColor.GOLD + "ABRISTE EL INVENTARIO");
    }

    @Override
    public void onAttemptBlockBreak(MC_Player mc_player, MC_Location loc, MC_EventInfo ei) {
        if (!inPlayers.contains(mc_player.getName())) {
            ei.isCancelled = true;
            printRequestLoginMessage(mc_player);
        }
    }

    @Override
    public void onAttemptPlaceOrInteract(MC_Player mc_player, MC_Location loc, MC_DirectionNESWUD dir, MC_Hand hand, MC_EventInfo ei) {
        if (!inPlayers.contains(mc_player.getName())) {
            ei.isCancelled = true;
            printRequestLoginMessage(mc_player);
        }
    }

    @Override
    public void onPlayerLogout(String playerName, UUID uuid) {
        if (inPlayers.contains(playerName))
            inPlayers.remove(playerName);
    }

    @Override
    public PluginInfo getPluginInfo() {
        PluginInfo info = new PluginInfo();
        info.description = "Register and login players to your server";
        info.name = "VNAP";
        info.version = "1.0";
        return info;
    }
}
