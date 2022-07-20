package com.github.carthax08.servercore.rankup;

import java.util.List;

public class Rank {
    public String name;
    public double cost;
    public String groupName;
    public String prefix;
    public List<String> permissions;

    public Rank(String _name, double _cost, String _groupName, String _prefix, List<String> permissions) {
        name = _name;
        cost = _cost;
        groupName = _groupName;
        prefix = _prefix;
        this.permissions = permissions;
    }


}
