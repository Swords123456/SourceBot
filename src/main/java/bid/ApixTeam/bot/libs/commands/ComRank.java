package bid.ApixTeam.bot.libs.commands;

import bid.ApixTeam.bot.utils.BotAPI;
import bid.ApixTeam.bot.utils.api.EmbedMessageManager;
import bid.ApixTeam.bot.utils.api.SettingsManager;
import bid.ApixTeam.bot.utils.vars.enums.Settings;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * TSC-Bot was created by ApixTeam (C) 2017
 * in association with TheSourceCode (C) 2017
 */
public class ComRank implements CommandExecutor {
    @Command(aliases = {"rank", "level"}, async = true)
    public void onCommand(User user, MessageChannel messageChannel, Message message, Object[] objects) {
        BotAPI botAPI = new BotAPI();
        EmbedMessageManager embedManager = botAPI.getEmbedMessageManager();
        SettingsManager sm = botAPI.getSettingsManager();

        if (sm.getSetting(Settings.CHAN_RANK_CHECK) != null && !sm.getSetting(Settings.CHAN_RANK_CHECK).equals(messageChannel.getId()))
            return;

        if (messageChannel.getType().isGuild()) {
            if (objects.length == 0)
                botAPI.getMessageManager().sendMessage(messageChannel, embedManager.getRankEmbed(botAPI, user));
            else if (message.getMentionedUsers().size() == 1)
                botAPI.getMessageManager().sendMessage(messageChannel, embedManager.getRankEmbed(botAPI, message.getMentionedUsers().get(0)));
            else
                botAPI.getMessageManager().sendMessage(messageChannel, embedManager.getUsage("!rank [@mention]"));
        } else {
            if (objects.length == 0)
                botAPI.getPrivateMessageManager().sendMessage(user, embedManager.getRankEmbed(botAPI, user));
            else
                botAPI.getPrivateMessageManager().sendMessage(user, embedManager.getAsDescription("yeah.... i think you'd actually need to do that on the TSC discord server..."));
        }
    }
}