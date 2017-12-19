package com.lb_stuff.mcmodify.nbt;

public class TagFormater {

    public enum Format{
        JSON,
        NBT;

        private static Format fromString(String name) {
            if(name.toLowerCase().equals("nbt")){
                return Format.NBT;
            }
            return Format.JSON;
        }
    }


    private Format format;
    private String tab;

    public TagFormater(Format format, String tab){
        this.format = format;
        this.tab = tab;
    }

    public TagFormater(String format, String tab){
        this(Format.fromString(format), tab);
    }

    public TagFormater(String format){
        this(Format.fromString(format), null);
    }

    public TagFormater(String format, boolean pp) {
        this(format, pp? "  " : null);
    }


    public String format (Tag tag){
        return getTagAsString(tag, "");
    }

    protected String getTagAsString(Tag tag, String tabulation){
        String result = "" + tabulation;
        if(tag.getName() != null){
            result += quotedName(tag) +  ":";
        }
        result += getTagValue(tag, shift(tabulation));
        if(format == Format.NBT){
            result += getTagTypeMarker(tag);
        }
        return result;
    }

    protected String getTagTypeMarker(Tag tag) {
        if(tag instanceof Tag.Byte){
            return "b";
        }else if(tag instanceof Tag.Short){
            return "s";
        }else if(tag instanceof Tag.Long){
            return "l";
        }else if(tag instanceof Tag.Float){
            return "f";
        }else if(tag instanceof Tag.Double){
            return "d";
        }
        return "";
    }


    protected String getTagValue(Tag tag, String tabulation){

        if(tag instanceof Tag.Byte) return Byte.toString(((Tag.Byte)tag).getData());
        if(tag instanceof Tag.Short) return Short.toString(((Tag.Short)tag).getData());
        if(tag instanceof Tag.Int) return Integer.toString(((Tag.Int)tag).getData());
        if(tag instanceof Tag.Long) return Long.toString(((Tag.Long)tag).getData());
        if(tag instanceof Tag.Float) return Float.toString(((Tag.Float)tag).getData());
        if(tag instanceof Tag.Double) return Double.toString(((Tag.Double)tag).getData());
        if(tag instanceof Tag.String) return quoteString(((Tag.String)tag).getData());

        if(tag instanceof  Tag.List){
            Tag.List list = (Tag.List)tag;
            StringBuilder builder = new StringBuilder();
            for(Tag t : list.getData()){
                if(builder.length() > 0){
                    builder.append(", ");
                    if(this.tab != null){
                        builder.append("\n");
                    }
                }
                builder.append(getTagAsString(t, shift(tabulation)));
            }
            if(this.tab == null){return "[" + builder.toString() + "]";}
            return "[\n" + builder.toString() + "\n" + unshift(tabulation) + "]";
        }
        if(tag instanceof Tag.Compound){
            Tag.Compound compound = (Tag.Compound)tag;
            StringBuilder builder = new StringBuilder();
            for(String key : compound.getData()){
                if(builder.length() > 0){
                    builder.append(", ");
                    if(this.tab != null){
                        builder.append("\n");
                    }
                }
                builder.append(getTagAsString(compound.get(key), shift(tabulation)));
            }
            if(this.tab == null){return "{" + builder.toString() + "}";}
            return "{\n" + builder.toString() + "\n" + unshift(tabulation) + "}";
        }

        if(tag instanceof  Tag.ByteArray){
            Tag.ByteArray list = (Tag.ByteArray)tag;
            StringBuilder builder = new StringBuilder();
            for(byte b : list.getData()){
                if(builder.length() > 0){builder.append(", ");}
                builder.append(b);
            }
            if(format == Format.NBT){
                return "[B;" + builder.toString() + "]";
            }
            return "[" + builder.toString() + "]";
        }

        if(tag instanceof  Tag.IntArray){
            Tag.IntArray list = (Tag.IntArray)tag;
            StringBuilder builder = new StringBuilder();
            for(int b : list.getData()){
                if(builder.length() > 0){builder.append(", ");}
                builder.append(b);
            }
            if(format == Format.NBT){
                return "[I;" + builder.toString() + "]";
            }
            return "[" + builder.toString() + "]";
        }

        if(tag instanceof  Tag.LongArray){
            Tag.LongArray list = (Tag.LongArray)tag;
            StringBuilder builder = new StringBuilder();
            for(long b : list.getData()){
                if(builder.length() > 0){builder.append(", ");}
                builder.append(b);
            }
            if(format == Format.NBT){
                return "[L;" + builder.toString() + "]";
            }
            return "[" + builder.toString() + "]";
        }
        return "";
    }

    protected String quoteString(String str) {
        return "\"" + str.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }


    protected String quotedName(Tag tag) {
        if(tag.getName()!= null)
        {
            return quoteString(tag.getName());
        }
        return "";
    }

    private String shift(String tabulation) {
        if(this.tab != null){
            return this.tab + tabulation;
        }
        return tabulation;
    }

    private String unshift(String tabulation) {
        return tabulation.substring(this.tab.length());
    }

}