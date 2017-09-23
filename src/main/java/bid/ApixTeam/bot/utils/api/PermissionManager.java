package bid.ApixTeam.bot.utils.api;

import bid.ApixTeam.bot.utils.vars.Lists;
import bid.ApixTeam.bot.utils.vars.enums.SimpleRank;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TSC-Bot was created by ApixTeam (C) 2017
 * in association with TheSourceCode (C) 2017
 */
public class PermissionManager extends DatabaseManager {
    private static PermissionManager permissionManager = new PermissionManager();

    public static PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public boolean isMember(User user) {
        if (Lists.getUsers().contains(user.getIdLong()))
            return true;

        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `members` WHERE `UserID` = ?");
            ps.setLong(1, user.getIdLong());
            ResultSet rs = ps.executeQuery();
            boolean b = rs.next();
            closeConnection(connection, ps, rs);
            if (b)
                Lists.getUsers().add(user.getIdLong());
            return b;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void createMember(User user) {
        if(isMember(user))
            return;

        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `members` (`UserID`, `Username`, `UserTag`, `AvatarUrl`, `AvatarId`) VALUES (?, ?, ?, ?, ?)");
            ps.setLong(1, user.getIdLong());
            ps.setString(2, user.getName());
            ps.setInt(3, Integer.parseInt(user.getDiscriminator()));
            ps.setString(4, user.getAvatarUrl());
            ps.setString(5, user.getAvatarId());
            ps.executeUpdate();
            closeConnection(connection, ps, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUserRank(User user, SimpleRank simpleRank){
        Lists.getUserPermissions().put(user.getIdLong(), simpleRank);
    }

    public void setUserRank(long userId, SimpleRank simpleRank){
        Lists.getUserPermissions().put(userId, simpleRank);
    }

    public void setUserPermission(User user, SimpleRank simpleRank) {
        if (!Lists.getUsers().contains(user.getIdLong()))
            createMember(user);

        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE `members` SET `Permission` = ? WHERE `UserID` = ?");
            ps.setString(1, simpleRank.getDescription());
            ps.setLong(2, user.getIdLong());
            ps.executeUpdate();
            setUserRank(user, simpleRank);
            closeConnection(connection, ps, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SimpleRank getUserPermission(User user){
        return Lists.getUserPermissions().get(user.getIdLong());
    }

    public SimpleRank getRolePermission(Role role){
        return Lists.getRolePermissions().get(role.getIdLong());
    }

    public boolean userAtLeast(User user, SimpleRank simpleRank) {
        SimpleRank rank = getUserPermission(user);
        return rank != null && rank.isAtLeast(simpleRank);
    }

    public boolean userHigherThan(User user, SimpleRank simpleRank) {
        SimpleRank rank = getUserPermission(user);
        return rank != null && rank.isHigherThan(simpleRank);
    }

    public boolean userLowerThan(User user, SimpleRank simpleRank) {
        SimpleRank rank = getUserPermission(user);
        return rank == null || rank.isLowerThan(simpleRank);
    }

    public boolean roleAtLeast(Role role, SimpleRank simpleRank) {
        SimpleRank rank = getRolePermission(role);
        return rank != null && rank.isAtLeast(simpleRank);
    }

    public boolean roleHigherThan(Role role, SimpleRank simpleRank) {
        SimpleRank rank = getRolePermission(role);
        return rank != null && rank.isHigherThan(simpleRank);
    }

    public boolean roleLowerThan(Role role, SimpleRank simpleRank) {
        SimpleRank rank = getRolePermission(role);
        return rank == null || rank.isLowerThan(simpleRank);
    }
}