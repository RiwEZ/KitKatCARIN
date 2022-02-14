package carin.parser;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Token {
    enum Type {
        OP,
        NUM,
        RESERVED,
        IDENTIFIER,
        DELIMITER
    }
    private final String value;
    private final Type type;
    private static final Map<String, Integer> directions = Map.of(
            "down", 2, "downleft", 1,
            "downright", 3, "left", 4,
            "right", 6, "up", 8,
            "upleft", 7, "upright", 9
    );
    private static Set<String> reserved = null;

    public Token(String value, Type type) {
        this.value = value;
        this.type = type;
    }

    public String val() {
        return value;
    }

    public Type type() {
        return type;
    }

    @Override
    public String toString() {
        return value + "  :" + type;
    }

    public static int getDirection(String str) {
        return directions.get(str);
    }

    public static Set<String> getReserved() {
        if (reserved == null) {
            reserved = new HashSet<>(List.of(
                    "antibody", "else", "if", "move", "nearby", "shoot", "then", "virus", "while"));
            reserved.addAll(directions.keySet());
        }
        return reserved;
    }
    public static int toNumber(@NotNull Token tk) {
        return Integer.parseInt(tk.val());
    }

    public static boolean isAssign(@NotNull Token tk) {
        return tk.type() == Type.IDENTIFIER;
    }

    public static boolean isMove(@NotNull Token tk) {
        return tk.val().equals("move");
    }

    public static boolean isAttack(@NotNull Token tk) {
        return tk.val().equals("shoot");
    }

    public static boolean isDirection(@NotNull Token tk) {
        return directions.containsKey(tk.val());
    }

    public static boolean isAction(@NotNull Token tk) {
        return isMove(tk) || isAttack(tk);
    }

    public static boolean isCommand(@NotNull Token tk) {
        return isAssign(tk) || isAction(tk);
    }

    public static boolean isBlock(@NotNull Token tk) {
        return tk.type() == Type.DELIMITER && tk.val().equals("{");
    }

    public static boolean isIf(@NotNull Token tk) {
        return isRESERVED(tk) && tk.val().equals("if");
    }

    public static boolean isWhile(@NotNull Token tk) {
        return isRESERVED(tk) && tk.val().equals("while");
    }

    public static boolean isStatement(@NotNull Token tk) {
        return isCommand(tk) || isBlock(tk) || isIf(tk) || isWhile(tk);
    }

    public static boolean isNUM(@NotNull Token tk) {
        return tk.type() == Type.NUM;
    }

    public static boolean isIDENTIFIER(@NotNull Token tk) {
        return tk.type() == Type.IDENTIFIER;
    }

    public static boolean isRESERVED(@NotNull Token tk) {
        return tk.type() == Type.RESERVED;
    }

    public static boolean isSensor(@NotNull Token tk) {
        return tk.type() == Type.RESERVED
                && (tk.val().equals("virus")
                || tk.val().equals("antibody"));
    }

    public static boolean isPower(@NotNull Token tk) {
        return isNUM(tk) || isIDENTIFIER(tk);
    }
}

