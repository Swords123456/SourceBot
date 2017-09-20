package bid.ApixTeam.bot.utils.vars;

import bid.ApixTeam.bot.utils.vars.enums.PermissionsG;
import bid.ApixTeam.bot.utils.vars.enums.RankingType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TSC-Bot was created by ApixTeam (C) 2017
 * in association with TheSourceCode (C) 2017
 */
public class Lists {
    private static ConcurrentHashMap<Long, Integer> userRankingCooldown = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Long, HashMap<RankingType, Integer>> userLevels = new ConcurrentHashMap<>();
    private static ArrayList<Long> userRankinCooldown = new ArrayList<>();
    private static HashMap<Integer, Integer> levelsMaxExp = new HashMap<>();
    private static HashMap<Long, PermissionsG> userPermissions = new HashMap<>();

    public static ConcurrentHashMap<Long, Integer> getUserRankingCooldown() {
        return userRankingCooldown;
    }

    public static ConcurrentHashMap<Long, HashMap<RankingType, Integer>> getUserLevels() {
        return userLevels;
    }

    public static ArrayList<Long> getUserRankinCooldown() {
        return userRankinCooldown;
    }

    public static HashMap<Integer, Integer> getLevelsMaxExp() {
        return levelsMaxExp;
    }

    static void setLevelsMaxExp(HashMap<Integer, Integer> hashMap){
        levelsMaxExp = hashMap;
    }

    public static HashMap<Long, PermissionsG> getUserPermissions() {
        return userPermissions;
    }
}
