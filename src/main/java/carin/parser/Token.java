package carin.parser;

import org.jetbrains.annotations.NotNull;

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
        return "value='" + value +
                ", type=" + type;
    }

    public static int toNumber(@NotNull Token tk) {
        return Integer.parseInt(tk.val());
    }

    public static boolean isAssign(@NotNull Token tk) {
        return tk.type() == Type.IDENTIFIER;
    }

    public static boolean isAction(@NotNull Token tk) {
        return tk.val().equals("move") || tk.val().equals("shoot");
    }

    public static boolean isCommand(Token tk) {
        return isAssign(tk) || isAction(tk);
    }

    public static boolean isNUM(@NotNull Token tk) {
        return tk.type() == Type.NUM;
    }

    public static boolean isIDENTIFIER(@NotNull Token tk) {
        return tk.type() == Type.IDENTIFIER;
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

