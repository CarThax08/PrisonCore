package com.github.carthax08.servercore.rankup;

import com.github.carthax08.servercore.data.files.RanksFileHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Set;

public class RankHandler {

    private static ArrayList<Rank> ranks = new ArrayList<>();

    public static void deserialize(ConfigurationSection config, int index){
        ranks.add(index, new Rank(config.getString("name"), config.getDouble("cost"), config.getString("groupName"), config.getString("prefix")));
    }

    public static Rank getRank(int index){
        if(ranks.size() >= index + 1){
            return ranks.get(index);
        }
        return null;
    }

    public static Rank getNextRank(Rank rank){
        if(ranks.size() > ranks.indexOf(rank) + 1){
            return ranks.get(ranks.indexOf(rank) + 1);
        }
        return null;
    }

    public static boolean loadRanks(){

        try{
            YamlConfiguration config = RanksFileHandler.ranksConfig;
            ConfigurationSection ranksSec = config.getConfigurationSection("ranks");
            Set<String> ranks = ranksSec.getKeys(false);
            for (String rank : ranks){
                deserialize(ranksSec.getConfigurationSection(rank), Integer.parseInt(rank));
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public static int getIndex(Rank rank) {
        return ranks.indexOf(rank);
    }
}
