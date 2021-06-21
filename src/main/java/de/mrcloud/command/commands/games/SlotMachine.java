package de.mrcloud.command.commands.games;

import de.mrcloud.command.Command;
import de.mrcloud.command.GameCommand;
import de.mrcloud.main.CloudCityBot2;
import de.mrcloud.utils.discord.JDAUtils;
import de.mrcloud.utils.settings.Settings;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Random;

public class SlotMachine extends GameCommand {

    public SlotMachine() {
        super("slot", "Play the slot machine with a specific amount of coins as your entry. Requires level 1", Command.PREFIX + "slot <COIN_AMOUNT>", 1);
    }

    @Override
    public boolean execute(GuildMessageReceivedEvent e, String[] args) {
        Member member = e.getMember();
        TextChannel textChannel = e.getChannel();
        e.getMessage().delete().queue();
        ResultSet userData;
        ResultSet userMoney;
        Connection connection = CloudCityBot2.getInstance().getDbHandler().getConnection();
        try {


            Statement statement = connection.createStatement();


            if (args.length != 1) {
                JDAUtils.redBuilder("Error", String.format(Settings.getLanguageTextByMember(e.getMember(), "args-missmatch"), 1, args.length), member, textChannel, 20);
                return false;
            }

            userData = statement.executeQuery("SELECT * FROM UserStatistics WHERE userId = " + member.getIdLong());
            userMoney = statement.executeQuery("SELECT * FROM Users WHERE UserId = " + member.getIdLong());

            userData.first();
            userMoney.first();
            int level = userData.getInt("level");
            int coins = userMoney.getInt("coins");

            if (level >= 1) {
                if (coins >= Integer.parseInt(args[0])) {
                    int[][] slots = new int[4][4];
                    for (int i = 0; i < 4; i++) {
                        for (int x = 0; x < 4; x++) {
                            Random random = new Random();
                            int randomInt = random.nextInt(4);
                            while (numberThisRowEquals(i, x, randomInt, slots)) {
                                randomInt = random.nextInt(4);
                            }
                            slots[i][x] = randomInt;
                        }
                    }

                    System.out.println(Arrays.toString(slots[0]));
                    System.out.println(Arrays.toString(slots[1]));
                    System.out.println(Arrays.toString(slots[2]));
                    System.out.println(Arrays.toString(slots[3]));

                    Message message = e.getChannel().sendMessage("⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "\n" +
                            "⬜" + intToEmote(slots[0][0]) + "\uD83D\uDFE8" + intToEmote(slots[1][0]) + "\uD83D\uDFE8" + intToEmote(slots[2][0]) + "\uD83D\uDFE8" + intToEmote(slots[3][0]) + "⬜" + "\n"
                            + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜").complete();


                    new Thread(() -> {
                        int counter = 0;
                        int currentSlot = 0;
                        while (counter <= new Random().nextInt(5) + 5) {
                            System.out.println("editing");
                            System.out.println(currentSlot);
                            message.editMessage("⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "\n" +
                                    "⬜" + intToEmote(slots[0][currentSlot]) + "\uD83D\uDFE8" + intToEmote(slots[1][currentSlot]) + "\uD83D\uDFE8" + intToEmote(slots[2][currentSlot]) + "\uD83D\uDFE8" + intToEmote(slots[3][currentSlot]) + "⬜" + "\n"
                                    + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜" + "⬜").queue();
                            counter++;
                            if(currentSlot != 3) {

                                currentSlot++;
                            }
                            else {
                                currentSlot = 0;
                            }

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    }).start();

                } else {
                    JDAUtils.redBuilder("Error", "Du hast nicht genug Coins für dieses Minispiel", member, textChannel);
                }
            } else {
                JDAUtils.redBuilder("Error", "Dein Level ist nicht hoch genug für diese Minispiel", member, textChannel);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private boolean numberThisRowEquals(int i, int x, int randomInt, int[][] slots) {

        for (int k = x - 1; k > -1; k--) {
            if (slots[i][k] == randomInt)
                return true;
        }
        return false;
    }


    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        return false;
    }


    public String intToEmote(int number) {
        //Raindrop
        if (number == 0) {
            return "\uD83D\uDCA7";
            //Snowflake
        } else if (number == 1) {
            return "❄️";
            //Icecube
        } else if (number == 2) {
            return "\uD83E\uDDCA";
            //Snowman
        } else {
            return "☃️";
        }
    }
}
