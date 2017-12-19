package com.lb_stuff.mcmodify.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import java.lang.reflect.InvocationTargetException;

public abstract class Tag implements Cloneable {
    protected static final java.nio.charset.Charset UTF8;

    static {
        java.nio.charset.Charset temp = null;
        try {
            temp = java.nio.charset.Charset.forName("UTF-8");
        } catch (Throwable t) {
        } finally {
            UTF8 = temp;
        }
    }

    private java.lang.String name;

    public Tag(java.lang.String _name) {
        name = _name;
    }

    public static Tag deserialize(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        final Type type = Type.fromId(dis.readByte());
        if (type == Type.END) {
            return new End();
        }
        final java.lang.String name = readString(dis);
        switch (type) {
            case BYTE:
                return new Byte(name, is);
            case SHORT:
                return new Short(name, is);
            case INT:
                return new Int(name, is);
            case LONG:
                return new Long(name, is);
            case FLOAT:
                return new Float(name, is);
            case DOUBLE:
                return new Double(name, is);
            case BYTEARRAY:
                return new ByteArray(name, is);
            case STRING:
                return new String(name, is);
            case LIST:
                return new List(name, is);
            case COMPOUND:
                return new Compound(name, is);
            case INTARRAY:
                return new IntArray(name, is);
            case LONGARRAY:
                return new LongArray(name, is);
            default:
                throw new IllegalStateException();
        }
    }

    private static java.lang.String readString(DataInputStream dis) throws IOException {
        short length = dis.readShort();
        if (length < 0) {
            throw new FormatException("String length was negative: " + length);
        }
        byte[] str = new byte[length];
        dis.readFully(str);
        return new java.lang.String(str, UTF8);
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public abstract Type getType();

    public final void serialize(OutputStream os) throws IOException {
        preSerialize(os);
        serializePayload(os);
    }

    protected abstract void serializePayload(OutputStream os) throws IOException;

    private void preSerialize(OutputStream os) throws IOException {
        os.write((byte) getType().ordinal());
        if (name != null) {
            new String(null, name).serializePayload(os);
        }
    }

    @Override
    public final int hashCode() {
        if (name == null) {
            return 0;
        }
        return name.hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (o instanceof Tag) {
            Tag t = (Tag) o;
            if (name == null) {
                return t.name == null;
            } else if (t.name != null) {
                return name.equals(t.name);
            }
        }
        return false;
    }

    public final boolean equals(Tag t) {
        if (name == null) {
            return t.name == null;
        } else if (t.name != null) {
            return name.equals(t.name);
        }
        return false;
    }

    @Override
    public Tag clone() {
        try {
            return (Tag) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }

    public final Tag clone(java.lang.String newname) {
        Tag t = clone();
        if (!(this instanceof End)) {
            t.name = newname;
        }
        return t;
    }

    public enum Type {
        END,
        BYTE, SHORT, INT, LONG,
        FLOAT, DOUBLE,
        BYTEARRAY,
        STRING,
        LIST,
        COMPOUND,
        INTARRAY,
        LONGARRAY;

        public static Type fromId(int id) {
            switch (id) {
                case 0:
                    return END;
                case 1:
                    return BYTE;
                case 2:
                    return SHORT;
                case 3:
                    return INT;
                case 4:
                    return LONG;
                case 5:
                    return FLOAT;
                case 6:
                    return DOUBLE;
                case 7:
                    return BYTEARRAY;
                case 8:
                    return STRING;
                case 9:
                    return LIST;
                case 10:
                    return COMPOUND;
                case 11:
                    return INTARRAY;
                case 12:
                    return LONGARRAY;
                default:
                    return null;
            }
        }

        public Class<? extends Tag> getImplementingClass() {
            switch (this) {
                case END:
                    return End.class;
                case BYTE:
                    return Byte.class;
                case SHORT:
                    return Short.class;
                case INT:
                    return Int.class;
                case LONG:
                    return Long.class;
                case FLOAT:
                    return Float.class;
                case DOUBLE:
                    return Double.class;
                case BYTEARRAY:
                    return ByteArray.class;
                case STRING:
                    return String.class;
                case LIST:
                    return List.class;
                case COMPOUND:
                    return Compound.class;
                case INTARRAY:
                    return IntArray.class;
                case LONGARRAY:
                    return LongArray.class;
                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public java.lang.String toString() {
            switch (this) {
                case BYTEARRAY:
                    return "Byte Array";
                case INTARRAY:
                    return "Int Array";
                default:
                    return getImplementingClass().getSimpleName();
            }
        }
    }

    public static final class End extends Tag {
        public End() {
            super(null);
        }

        public End(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            this();
        }

        @Override
        public Type getType() {
            return Type.END;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
        }

        @Override
        public End clone() {
            return this;
        }
    }

    public static final class Byte extends Tag {
        public byte v;

        public Byte(java.lang.String name, byte b) {
            super(name);
            v = b;
        }

        public Byte(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            this(name, (byte) i.read());
        }

        @Override
        public Type getType() {
            return Type.BYTE;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
            o.write(v);
        }

        public byte getData() {
            return v;
        }
    }

    public static final class Short extends Tag {
        public short v;

        public Short(java.lang.String name, short s) {
            super(name);
            v = s;
        }

        public Short(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            this(name, new DataInputStream(i).readShort());
        }

        @Override
        public Type getType() {
            return Type.SHORT;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
            new DataOutputStream(o).writeShort(v);
        }

        public short getData() {
            return v;
        }
    }

    public static final class Int extends Tag {
        public int v;

        public Int(java.lang.String name, int i) {
            super(name);
            v = i;
        }

        public Int(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            this(name, new DataInputStream(i).readInt());
        }

        @Override
        public Type getType() {
            return Type.INT;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
            new DataOutputStream(o).writeInt(v);
        }

        public int getData() {
            return v;
        }

    }

    public static final class Long extends Tag {

        public long v;

        public Long(java.lang.String name, long l) {
            super(name);
            v = l;
        }

        public Long(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            this(name, new DataInputStream(i).readLong());
        }

        @Override
        public Type getType() {
            return Type.LONG;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
            new DataOutputStream(o).writeLong(v);
        }

        public long getData() {
            return v;
        }
    }

    public static final class Float extends Tag {
        public float v;

        public Float(java.lang.String name, float f) {
            super(name);
            v = f;
        }

        public Float(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            this(name, new DataInputStream(i).readFloat());
        }

        @Override
        public Type getType() {
            return Type.FLOAT;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
            new DataOutputStream(o).writeFloat(v);
        }

        public float getData() {
            return v;
        }
    }

    public static final class Double extends Tag {
        public double v;

        public Double(java.lang.String name, double d) {
            super(name);
            v = d;
        }

        public Double(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            this(name, new DataInputStream(i).readDouble());
        }

        @Override
        public Type getType() {
            return Type.DOUBLE;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
            new DataOutputStream(o).writeDouble(v);
        }

        public double getData() {
            return v;
        }
    }

    public static final class ByteArray extends Tag {
        public byte[] v;

        public ByteArray(java.lang.String name, byte[] b) {
            super(name);
            v = b;
        }

        public ByteArray(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            this(name, (byte[]) null);
            DataInputStream dis = new DataInputStream(i);
            int size = dis.readInt();
            if (size < 0) {
                throw new FormatException("Byte Array size was negative: " + size);
            }
            v = new byte[size];
            dis.readFully(v);
        }

        @Override
        public Type getType() {
            return Type.BYTEARRAY;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
            DataOutputStream dos = new DataOutputStream(o);
            dos.writeInt(v.length);
            dos.write(v);
        }

        @Override
        public ByteArray clone() {
            ByteArray ba = (ByteArray) super.clone();
            ba.v = Arrays.copyOf(v, v.length);
            return ba;
        }

        public byte[] getData() {
            return v;
        }
    }

    public static final class String extends Tag {

        public java.lang.String v;

        public String(java.lang.String name, java.lang.String s) {
            super(name);
            v = s;
        }

        public String(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            this(name, readString(new DataInputStream(i)));
        }

        @Override
        public Type getType() {
            return Type.STRING;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
            byte[] sarr = v.getBytes(UTF8);
            new DataOutputStream(o).writeShort((short) sarr.length);
            o.write(sarr);
        }

        public java.lang.String getData() {
            return v;
        }
    }

    public static final class List extends Tag implements Iterable<Tag> {

        private final Type type;

        private java.util.List<Tag> list = new ArrayList<>();

        public List(java.lang.String name, Type _type, Tag... tags) throws IllegalArgumentException {
            super(name);
            if (_type == null) {
                throw new IllegalArgumentException("The tag type was null");
            }
            type = _type;
            if (type != Type.END) {
                for (Tag t : tags) {
                    if (t.getName() != null) {
                        throw new IllegalArgumentException("Tags in Lists must have null names; given tag had name: \"" + t.getName() + "\"");
                    } else if (t.getType() == type) {
                        list.add(t);
                    } else {
                        throw new IllegalArgumentException(type + " required, given " + t.getType());
                    }
                }
            }
        }

        public List(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            super(name);
            type = Type.fromId(i.read());
            int size = new DataInputStream(i).readInt();
            if (size < 0) {
                throw new FormatException("List size is negative: " + size);
            }
            try {
                java.lang.reflect.Constructor<? extends Tag> c = type.getImplementingClass().getConstructor(java.lang.String.class, InputStream.class);
                for (int j = 0; j < size; ++j) {
                    list.add(c.newInstance(null, i));
                }
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new FormatException(e);
            }
        }

        @Override
        public Type getType() {
            return Type.LIST;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
            o.write((byte) type.ordinal());
            new DataOutputStream(o).writeInt(list.size());
            for (int i = 0; i < list.size(); ++i) {
                list.get(i).serializePayload(o);
            }
        }

        public void add(Tag... tags) {
            for (Tag t : tags) {
                if (t.getName() != null) {
                    throw new IllegalArgumentException("Tags in Lists must have null names; given tag had name: \"" + t.getName() + "\"");
                } else if (t.getType() != type) {
                    throw new IllegalArgumentException(type + " required, given " + t.getType());
                }
                list.add(t);
            }
        }

        public void addAll(Tag.List l) {
            if (l.getContainedType() != type) {
                throw new IllegalArgumentException(type + " required, given list of " + l.getContainedType());
            }
            for (Tag t : l.list) {
                list.add(t);
            }
        }

        public void addAll(Collection<? extends Tag> c) {
            for (Tag t : c) {
                add(t);
            }
        }

        public int getSize() {
            return list.size();
        }

        public void set(int index, Tag t) {
            if (t.getType() != type) {
                throw new IllegalArgumentException(type + " required, given " + t.getType());
            }
            list.set(index, t);
        }

        public void insert(int index, Tag... tags) {
            for (Tag t : tags) {
                if (t.getName() != null) {
                    throw new IllegalArgumentException("Tags in Lists must have null names; given tag had name: \"" + t.getName() + "\"");
                } else if (t.getType() != type) {
                    throw new IllegalArgumentException(type + " required, given " + t.getType());
                }
                list.add(index++, t);
            }
        }

        public void insertAll(int index, Tag.List l) {
            if (l.getContainedType() != type) {
                throw new IllegalArgumentException(type + " required, given list of " + l.getContainedType());
            }
            for (Tag t : l.list) {
                list.add(index++, t);
            }
        }

        public void insertAll(int index, Collection<? extends Tag> c) {
            for (Tag t : c) {
                insert(index++, t);
            }
        }

        public Tag get(int index) {
            return list.get(index);
        }

        public Tag remove(int index) {
            return list.remove(index);
        }

        public Type getContainedType() {
            return type;
        }

        @Override
        public Iterator<Tag> iterator() {
            return list.iterator();
        }

        @Override
        public List clone() {
            List li = (List) super.clone();
            li.list = new ArrayList<>();
            for (int i = 0; i < list.size(); ++i) {
                li.list.add(list.get(i).clone());
            }
            return li;
        }

        public java.util.List<Tag> getData() {
            return this.list;
        }
    }

    public static final class Compound extends Tag implements Iterable<Tag> {

        private HashMap<java.lang.String, Tag> tags = new HashMap<>();

        public Compound(java.lang.String name, Tag... tags) throws IllegalArgumentException {
            super(name);
            for (Tag t : tags) {
                java.lang.String n = t.getName();
                if (n == null) {
                    throw new IllegalArgumentException("Tag names cannot be null");
                }
                if (t instanceof End) {
                    throw new IllegalArgumentException("Cannot manually add the End tag!");
                }
                this.tags.put(n, t);
            }
        }

        public Compound(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            this(name);
            Tag t;
            while (!((t = deserialize(i)) instanceof End)) {
                tags.put(t.getName(), t);
            }
        }

        /*default*/
        static java.lang.String preceedLinesWithTabs(java.lang.String s) {
            return "\t" + s.replaceAll("\n", "\n\t");
        }

        @Override
        public Type getType() {
            return Type.COMPOUND;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
            for (Tag t : tags.values()) {
                t.serialize(o);
            }
            new End().serialize(o);
        }

        public void add(Tag... tags) throws IllegalArgumentException {
            for (Tag t : tags) {
                java.lang.String n = t.getName();
                if (n == null) {
                    throw new IllegalArgumentException("Tag names cannot be null");
                }
                if (t.getType() == Type.END) {
                    throw new IllegalArgumentException("Cannot manually add a TAG_End!");
                }
                this.tags.put(n, t);
            }
        }

        public void addAll(Tag.Compound c) {
            tags.putAll(c.tags);
        }

        /**
         * Adds the tags from the given collection to this compound tag.
         *
         * @param c The collection from which to add the tags.
         * @throws IllegalArgumentException if the name of one of the given tags is null.
         */
        public void addAll(Collection<? extends Tag> c) throws IllegalArgumentException {
            for (Tag t : c) {
                add(t);
            }
        }

        public int getSize() {
            return tags.size();
        }

        public Tag get(java.lang.String name) {
            return tags.get(name);
        }


        @Deprecated
        public Tag find(Type type, java.lang.String n) throws FormatException {
            Tag t = tags.get(n);
            if (t == null) {
                throw new FormatException("No tag with the name \"" + n + "\"", this);
            } else if (t.getType() != type) {
                throw new FormatException("\"" + n + "\" is " + t.getType() + " instead of " + type, this);
            }
            return t;
        }

        @Deprecated
        public List findList(java.lang.String n, Type type) throws FormatException {
            Tag t = tags.get(n);
            if (t == null) {
                throw new FormatException("No List tag with the name \"" + n + "\"", this);
            } else if (t.getType() != Type.LIST) {
                throw new FormatException("\"" + n + "\" is " + t.getType() + " instead of a List tag", this);
            }
            List l = (List) t;
            if (l.getContainedType() != type) {
                throw new FormatException("\"" + n + "\" supports " + l.getContainedType() + " instead of " + type, this);
            }
            return l;
        }

        public Tag remove(java.lang.String n) {
            return tags.remove(n);
        }

        @Override
        public Iterator<Tag> iterator() {
            return tags.values().iterator();
        }

        @Override
        public Compound clone() {
            Compound c = (Compound) super.clone();
            c.tags = new HashMap<>();
            for (Map.Entry<java.lang.String, Tag> e : tags.entrySet()) {
                c.tags.put(e.getKey(), e.getValue().clone());
            }
            return c;
        }

        public Set<java.lang.String> getData() {
            return tags.keySet();
        }
    }

    public static final class IntArray extends Tag {

        public int[] v;

        public IntArray(java.lang.String name, int[] i) {
            super(name);
            v = i;
        }

        public IntArray(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            this(name, (int[]) null);
            DataInputStream dis = new DataInputStream(i);
            int size = dis.readInt();
            if (size < 0) {
                throw new FormatException("Integer Array size was negative: " + size);
            }
            v = new int[size];
            for (int j = 0; j < size; ++j) {
                v[j] = dis.readInt();
            }
        }

        @Override
        public Type getType() {
            return Type.INTARRAY;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
            DataOutputStream dos = new DataOutputStream(o);
            dos.writeInt(v.length);
            for (int i : v) {
                dos.writeInt(i);
            }
        }

        @Override
        public IntArray clone() {
            IntArray ia = (IntArray) super.clone();
            ia.v = Arrays.copyOf(v, v.length);
            return ia;
        }

        public int[] getData() {
            return v;
        }
    }

    public static final class LongArray extends Tag {
        public long[] v;

        public LongArray(java.lang.String name, long[] i) {
            super(name);
            v = i;
        }

        public LongArray(java.lang.String name, InputStream i) throws IOException, FormatException //DeserializePayload
        {
            this(name, (long[]) null);
            DataInputStream dis = new DataInputStream(i);
            int size = dis.readInt();
            if (size < 0) {
                throw new FormatException("Long Array size was negative: " + size);
            }
            v = new long[size];
            for (int j = 0; j < size; ++j) {
                v[j] = dis.readLong();
            }
        }

        @Override
        public Type getType() {
            return Type.LONGARRAY;
        }

        @Override
        protected void serializePayload(OutputStream o) throws IOException {
            DataOutputStream dos = new DataOutputStream(o);
            dos.writeInt(v.length);
            for (long i : v) {
                dos.writeLong(i);
            }
        }

        @Override
        public LongArray clone() {
            LongArray ia = (LongArray) super.clone();
            ia.v = Arrays.copyOf(v, v.length);
            return ia;
        }

        public long[] getData() {
            return v;
        }
    }
}
