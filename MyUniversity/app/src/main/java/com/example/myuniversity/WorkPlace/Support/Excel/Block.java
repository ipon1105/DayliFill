package com.example.myuniversity.WorkPlace.Support.Excel;

import java.util.ArrayList;

public class Block {
    private ArrayList<Group> groupList;

    public Block(ArrayList<Group> groupList) {
        this.groupList = groupList;
    }

    public ArrayList<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<Group> groupList) {
        this.groupList = groupList;
    }

    @Override
    public String toString() {
        String answer = "\nBlock{";

        for(int i = 0; i < groupList.size(); i++)
            answer += "\n" + String.valueOf(i) + ": " + groupList.get(i).toString();

        return answer + "\n}";
    }
}
