package com.raunits.prettier;

import java.util.Stack;

public class Formatter {
    public String format(String code) {
        Stack<Pair> stack = new Stack<Pair>();
        StringBuilder result = new StringBuilder();

        char[] input = code.toCharArray();
        int i=0;

        for (int k=0; k<input.length; k++) {
            if (input[k]=='<' || input[k]=='>' || Character.isWhitespace(input[k]))
                continue;

            if (input[k]=='/') {
                i--;
                StringBuilder tagname = new StringBuilder();
                while (input[k] != '>') {
                    tagname.append(input[k++]);
                }
                stack.push(new Pair(tagname.toString(), i));
                k++;
                continue;
            }

            StringBuilder tagname = new StringBuilder();
            while (input[k] != '>') {
                tagname.append(input[k++]);
            }
            stack.push(new Pair(tagname.toString(), i));
            i++;
        }

        for (Pair p: stack) {
            int indent = 2 * p.indent;
            String tag = "<" + p.tag + ">";
            StringBuilder sb = new StringBuilder();
            while (indent-- > 0) sb.append(" ");
            result.append(sb.toString() + tag + "\n");
        }

        return result.toString();
    }
}

class Pair {
    String tag;
    int indent;

    public Pair(String tag, int in) {
        this.tag = tag;
        this.indent = in;
    }
}