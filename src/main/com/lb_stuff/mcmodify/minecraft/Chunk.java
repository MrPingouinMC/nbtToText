package com.lb_stuff.mcmodify.minecraft;

import com.lb_stuff.mcmodify.nbt.FormatException;
import com.lb_stuff.mcmodify.nbt.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @see <a href="http://minecraft.gamepedia.com/Chunk_format">Chunk format</a> on the Minecraft Wiki
 */

public class Chunk{

	private Tag.Compound chunkTag;

	public Chunk(){
		this(new Tag.Compound(""));
	}

	public Chunk(Tag.Compound tag) {
		this.chunkTag = tag;
	}

	public Tag.Compound ToNBT(String name) {
		return new Tag.Compound(name);
	}
}
