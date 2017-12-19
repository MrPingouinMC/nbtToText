package com.mrpingouin.nbttotext;

import java.util.*;

public class ArgParser {

    List<String> arguments = new ArrayList<>();
    Map<String, String> optionsParam = new HashMap<>();
    Set<String> optionsSimple = new HashSet<>();


    public ArgParser(String[] args) {

        boolean lastOption = false;
        String lastOptionName = null;
        for(String str : args){
            if(lastOption){
                optionsParam.put(lastOptionName, str);
                lastOption = false;
            }else if(isOptionSimple(str)){
                optionsSimple.add(stripOption(str));
            }else if(isOptionParam(str)){
                lastOption = true;
                lastOptionName = stripOption(str);
            }else{
                arguments.add(str);
            }
        }



    }

    private boolean isOptionParam(String str) {
        return str.startsWith("-");
    }

    private boolean isOptionSimple(String str) {
        return str.startsWith("--");
    }

    private String stripOption(String str) {
        if(str.startsWith("--")){
            return str.substring(2);
        }
        return str.substring(1);
    }

    private boolean isOption(String str) {
        return str.startsWith("-");
    }

    public int size() {
        return arguments.size();
    }

    public boolean hasOption(String str) {
        return optionsSimple.contains(str);
    }

    public String getOption(String str, String def) {
        if(!optionsParam.containsKey(str)){
            return def;
        }
        return optionsParam.get(str);
    }

    public String get(int i, String def) {
        if (arguments.size() <= i) {
            return def;
        }
        return arguments.get(i);
    }
}