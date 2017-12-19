package com.mrpingouin.nbttotext;

import com.lb_stuff.mcmodify.minecraft.CompressionScheme;
import com.lb_stuff.mcmodify.minecraft.MemoryRegion;
import com.lb_stuff.mcmodify.nbt.Tag;
import com.lb_stuff.mcmodify.nbt.TagFormater;

import java.io.*;

public class Main {


    public static void main(String[] args) throws InterruptedException {

        ArgParser parser = new ArgParser(args);


        if(parser.size() == 0 || parser.hasOption("help")){
            System.out.println("Convert file from NBT format (or region file) to text (JSON or text NBT)");
            System.out.println("");
            System.out.println("java -jar nbtToText.jar [filename] -format [format] -out [output file] --pretty");
            System.out.println("");
            System.out.println("\t[filename] input file to convert, must have the extension .mca, .dat or .nbt");
            System.out.println("");
            System.out.println("\t--pretty\toptionnal, if present, the output text will be pretty printed (if not present, everything will be in a single line)");
            System.out.println("\t-format [format]\toptionnal output format, json or nbt (default to json)");
            System.out.println("\t-out [output file]\toptionnal output file, (default to out.json)");
            return;
        }

        boolean pp = parser.hasOption("pretty");
        String format = parser.getOption("format", "JSON");
        String outputFile = parser.getOption("out", "out.json");
        String inputFile = parser.get(0, "");


        File f = new File(inputFile);
        if (!f.exists()) {
            System.out.println("File not found " + f.getAbsolutePath());
            return;
        }
        String name = f.getName();
        String ext = name.substring(name.lastIndexOf(".") + 1);

        if (ext.equals("mca")) {
            CompressionScheme comp = CompressionScheme.GZip;
            Tag.Compound main = new Tag.Compound(null);
            try {
                MemoryRegion reg = new MemoryRegion(f, comp);
                for (int i = 0; i < MemoryRegion.MAX_CHUNKS; i++) {
                    if (reg.chunks[i] != null) {
                        Tag.Compound tag = (Tag.Compound) Tag.deserialize(comp.getInputStream(new ByteArrayInputStream(reg.chunks[i])));
                        tag.setName("chunk" + i);
                        main.add(tag);
                    }
                }

                TagFormater formater = new TagFormater(format, pp);

                File out = new File(outputFile);
                out.createNewFile();

                BufferedWriter writer = new BufferedWriter(new FileWriter(out));
                writer.write(formater.format(main));
                writer.close();

            } catch (IOException e) {
                System.out.println("Something went wrong : ");
                e.printStackTrace();
            }
        } else if (ext.equals("nbt") || ext.equals("dat") || ext.equals("schematic") || ext.equals("dat_old")) {

            CompressionScheme comp = CompressionScheme.GZip;
            if (name.equals("idcounts.dat") || name.equals("servers.dat")) {
                comp = CompressionScheme.None;
            }

            try {
                Tag.Compound tag = (Tag.Compound) Tag.deserialize(comp.getInputStream(new FileInputStream(f)));
                tag.setName(null);
                TagFormater formater = new TagFormater(format, pp);

                File out = new File(outputFile);
                out.createNewFile();

                BufferedWriter writer = new BufferedWriter(new FileWriter(out));
                writer.write(formater.format(tag));
                writer.close();

            } catch (IOException e) {
                System.out.println("Something went wrong : ");
                e.printStackTrace();
            }
        }
    }
}