# NBT TO TEXT

Convert file from NBT format (or region file) to text (JSON or text NBT)

The .mca file reader and nbt part comes from here :
    https://github.com/LB--/MCModify


# How to use

java -jar nbtToText.jar [filename] -format [format] -out [output file] --pretty

    [filename] input file to convert, must have the extension .mca, .dat or .nbt

    --pretty            optionnal, the output text will be pretty printed (otherwise, everything will be on a single line)
    -format [format]    optionnal, output format, json or nbt (default to json)
    -out [output file]  optionnal, output file, (default to out.json)
