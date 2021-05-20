package de.mrcloud.main;


import de.mrcloud.command.Registerer;
import de.mrcloud.listeners.AutoChannelCreation;
import de.mrcloud.listeners.CommandExecutor;
import de.mrcloud.listeners.JoinListener;
import de.mrcloud.listeners.amongus.AmongUsLFGVoice;
import de.mrcloud.listeners.amongus.AmongUsLookingForGroupListener;
import de.mrcloud.listeners.moderation.DefenseListener;
import de.mrcloud.listeners.other.JokeListener;
import de.mrcloud.listeners.statistics.ChannelTimeListener;
import de.mrcloud.listeners.statistics.MessageCountListener;
import de.mrcloud.listeners.statistics.StatisticsHandler;
import de.mrcloud.sql.DatabaseConnectionHandler;
import de.mrcloud.utils.ChannelDuet;
import de.mrcloud.utils.Settings;
import de.mrcloud.utils.Utils;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


@SuppressWarnings("SpellCheckingInspection")

public class CloudCityBot2 {
    private static CloudCityBot2 instance;
    private boolean initalizationFinished = false;
    private Guild server;
    private long roleIDEveryone;
    private ShardManager shardMan;
    private DatabaseConnectionHandler dbHandler;
    private StatisticsHandler timeHandler;

    public CloudCityBot2() throws LoginException {
        instance = this;
        dbHandler = new DatabaseConnectionHandler();
        dbHandler.handleConnection();
        timeHandler = new StatisticsHandler();
        DefaultShardManagerBuilder builder;
        File file = new File("forcerestarted.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter myWriter = new FileWriter(file, false);
                while (!file.exists()) {
                    myWriter = new FileWriter(file, false);
                }

                myWriter.write("false");
                myWriter.flush();
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (!Utils.readString(file).equals("true")) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Main or testing token?");


            if (sc.next().equals("main")) {
                builder = DefaultShardManagerBuilder.createDefault(de.mrcloud.utils.Settings.TOKEN, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES).setMemberCachePolicy(MemberCachePolicy.ALL).enableCache(CacheFlag.ACTIVITY);

            } else {
                builder = DefaultShardManagerBuilder.createDefault(Settings.TESTING_TOKEN, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES).setMemberCachePolicy(MemberCachePolicy.ALL).enableCache(CacheFlag.ACTIVITY);
            }
        } else {
            builder = DefaultShardManagerBuilder.createDefault(Settings.TOKEN, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES).setMemberCachePolicy(MemberCachePolicy.ALL).enableCache(CacheFlag.ACTIVITY);
        }


        builder.setAutoReconnect(true);
        builder.setRequestTimeoutRetry(true);
        builder.addEventListeners(new AutoChannelCreation(), new JoinListener(), new CommandExecutor(), new AmongUsLookingForGroupListener(), new AmongUsLFGVoice(), new DefenseListener(), new JokeListener(), new ChannelTimeListener(), new MessageCountListener());

        Settings.loadSettings();

        new Registerer();
        //builder.build() connect the bot to Discord
        shardMan = builder.build();
        TurnOffListener();


    }

    public static void main(String[] args) throws LoginException {
        new CloudCityBot2();
    }

    public static CloudCityBot2 getInstance() {
        return instance;
    }

    public void TurnOffListener() {
        new Thread(() -> {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(shardMan.getGuilds().get(0));
            if (shardMan.getGuilds().get(0) != null) {
                server = shardMan.getGuilds().get(0);
                List<Role> roles = new ArrayList<>(server.getRoles());
                Collections.reverse(roles);
                Settings.ROLE_ID_EVERYONE = roles.get(0).getIdLong();
                List<VoiceChannel> list = server.getVoiceChannels().stream()
                        .filter(channel -> channel.getName().matches("╠☕Chill Lounge \\d*"))
                        .collect(Collectors.toList());

                List<VoiceChannel> toRemoveFromList = new ArrayList<>();

                for (VoiceChannel vc : list) {
                    if (vc.getMembers().size() == 0) {
                        int channelNumber = Integer.parseInt(vc.getName().split("\\s")[2]);
                        vc.delete().queue();
                        toRemoveFromList.add(vc);
                        if (server.getTextChannelsByName("channel-settings-" + channelNumber, true).size() >= 1) {
                            server.getTextChannelsByName("channel-settings-" + channelNumber, true).get(0).delete().queue();
                        }
                    }
                }

                list.removeAll(toRemoveFromList);

                int runs = 0;
                for (VoiceChannel vc : list) {
                    runs++;
                    int channelNumber = Integer.parseInt(vc.getName().split("\\s")[2]);
                    vc.getManager().setName("╠☕Chill Lounge " + runs).queue();
                    if (!(server.getTextChannelsByName("channel-settings-" + channelNumber, true).size() >= 1)) {
                        TextChannel newTextChannel = vc.getParent().createTextChannel("channel-settings" + channelNumber).addMemberPermissionOverride(vc.getMembers().get(0).getIdLong(), AutoChannelCreation.allow, null).addRolePermissionOverride(roleIDEveryone, null, AutoChannelCreation.deny).complete();
                        AutoChannelCreation.channelDuets.add(new ChannelDuet(newTextChannel, vc, runs, vc.getMembers().get(0).getIdLong()));
                    } else {
                        AutoChannelCreation.channelDuets.add(new ChannelDuet(server.getTextChannelsByName("channel-settings-" + channelNumber, true).get(0), vc, runs, vc.getMembers().get(0).getIdLong()));
                    }

                }

                for (VoiceChannel channel : server.getVoiceChannels()) {
                    for (Member member : channel.getMembers()) {
                        ChannelTimeListener.inVoiceChannel.put(member.getIdLong(), new Date());
                    }
                }

                roleIDEveryone = shardMan.getGuilds().get(0).getRoles().get(shardMan.getGuilds().get(0).getRoles().size() - 1).getIdLong();
                initalizationFinished = true;
                System.out.println("   _____  _                    _     _____  _  _             ____          _      _____                        _  _    _               \n" +
                        "  / ____|| |                  | |   / ____|(_)| |           |  _ \\        | |    |  __ \\                      (_)| |  | |              \n" +
                        " | |     | |  ___   _   _   __| |  | |      _ | |_  _   _   | |_) |  ___  | |_   | |__) | ___ __      __ _ __  _ | |_ | |_  ___  _ __  \n" +
                        " | |     | | / _ \\ | | | | / _` |  | |     | || __|| | | |  |  _ <  / _ \\ | __|  |  _  / / _ \\\\ \\ /\\ / /| '__|| || __|| __|/ _ \\| '_ \\ \n" +
                        " | |____ | || (_) || |_| || (_| |  | |____ | || |_ | |_| |  | |_) || (_) || |_   | | \\ \\|  __/ \\ V  V / | |   | || |_ | |_|  __/| | | |\n" +
                        "  \\_____||_| \\___/  \\__,_| \\__,_|   \\_____||_| \\__| \\__, |  |____/  \\___/  \\__|  |_|  \\_\\\\___|  \\_/\\_/  |_|   |_| \\__| \\__|\\___||_| |_|\n" +
                        "                                                     __/ |                                                                             \n" +
                        " __      __               _                   __    |___/_      __                      _         _                                    \n" +
                        " \\ \\    / /              (_)                 /_ |    |___ \\    /_ |              /\\    | |       | |                                   \n" +
                        "  \\ \\  / /___  _ __  ___  _   ___   _ __      | |      __) |    | |  ______     /  \\   | | _ __  | |__    __ _                         \n" +
                        "   \\ \\/ // _ \\| '__|/ __|| | / _ \\ | '_ \\     | |     |__ <     | | |______|   / /\\ \\  | || '_ \\ | '_ \\  / _` |                        \n" +
                        "    \\  /|  __/| |   \\__ \\| || (_) || | | |    | | _   ___) |_   | |           / ____ \\ | || |_) || | | || (_| |                        \n" +
                        "     \\/  \\___||_|   |___/|_| \\___/ |_| |_|    |_|(_) |____/(_)  |_|          /_/    \\_\\|_|| .__/ |_| |_| \\__,_|                        \n" +
                        "                                                                                          | |                                          \n" +
                        "                                                                                          |_|                                          ");

                System.out.println("\n \n \n \n \n" +
                        "   ____    ___         __  __         ___  _                _ \n" +
                        "  / __ \\  | _ ) _  _  |  \\/  | _ _   / __|| | ___  _  _  __| |\n" +
                        " / / _| \\ | _ \\| || | | |\\/| || '_| | (__ | |/ _ \\| || |/ _` |\n" +
                        " \\ \\__| / |___/ \\_, | |_|  |_||_|    \\___||_|\\___/ \\_,_|\\__,_|\n" +
                        "  \\____/        |__/                                          \n");
            }

            try {
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("Stop")) {
                        if (shardMan != null) {
                            shardMan.setStatus(OnlineStatus.OFFLINE);
                            shardMan.setActivity(Activity.listening("offline"));
                            shardMan.shutdown();

                            Date date = new Date();
                            System.out.println("Speichere Channel Zeit für " + ChannelTimeListener.inVoiceChannel.entrySet() + " User...");
                            for (Map.Entry<Long, Date> entry : ChannelTimeListener.inVoiceChannel.entrySet()) {

                                long diff = date.getTime() - entry.getValue().getTime();
                                timeHandler.updateTime(diff, entry.getKey());
                            }
                            System.out.println("Bot wird heruntergefahren");
                            System.exit(1);

                        } else {
                            System.out.println("There are still boosts open!");
                        }
                    } else {
                        System.out.println("Bitte benutz stop!");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public boolean isInitalizationFinished() {
        return initalizationFinished;
    }

    public Guild getServer() {
        return server;
    }

    public long getRoleIDEveryone() {
        return roleIDEveryone;
    }

    public ShardManager getShardMan() {
        return shardMan;
    }

    public DatabaseConnectionHandler getDbHandler() {
        return dbHandler;
    }

    public StatisticsHandler getTimeHandler() {
        return timeHandler;
    }
}

