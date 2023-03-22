package com.raunits.prettier;

import java.util.Stack;

public class Formatter {
    public boolean valid(String code) {
        Stack<String> stack = new Stack<>();
        char[] input = code.toCharArray();
        for (int i = 0; i < input.length; i++) {
            if (input[i] == '<' || input[i] == '>' || Character.isWhitespace(input[i]))
                continue;

            if (input[i] == '/') {
                if (stack.isEmpty()) return false;
                StringBuilder tag = new StringBuilder();
                while (i < input.length && input[i] != '>') {
                    tag.append(input[i++]);
                }
                if (!stack.pop().equals(tag.toString())) return false;
                continue;
            }

            StringBuilder sb = new StringBuilder("/");
            while (i < input.length && input[i] != '>') {
                sb.append(input[i++]);
            }

            stack.push(sb.toString());
        }
        return stack.isEmpty();
    }

    public String format(String code) {
        Stack<Pair> stack = new Stack();
        StringBuilder result = new StringBuilder();
        char last = '*';

        char[] input = code.toCharArray();
        int i = 0;

        for (int k = 0; k < input.length; k++) {
            if (Character.isWhitespace(input[k]))
                continue;

            if (input[k] == '<' || input[k] == '>') {
                last = input[k];
                continue;
            }

            if (input[k] == '/') {
                i--;
                StringBuilder tagname = new StringBuilder();
                while (k < input.length && input[k] != '>') {
                    tagname.append(input[k++]);
                }
                stack.push(new Pair(tagname.toString(), i));
                last = input[k];
                k++;
                continue;
            }

            if (k > 0 && last == '>') {
                StringBuilder textContent = new StringBuilder();

                while (input[k] != '<')
                    textContent.append(input[k++]);


                stack.push(new Pair(textContent.toString().trim(), stack.peek().indent + 1, "content"));
                last = input[k];
                continue;
            }

            StringBuilder tagname = new StringBuilder();
            while (k < input.length) {
                if (input[k] == '>' && input[k-1] != '=') break;
                tagname.append(input[k++]);
            }
            stack.push(new Pair(tagname.toString(), i));
            i++;
            last = input[k];
        }

        for (Pair p : stack) {
            int indent = 2 * p.indent;
            StringBuilder sb = new StringBuilder();
            while (indent-- > 0) sb.append(" ");

            if (p.type == "tag") {
                String tag = "<" + p.tag + ">";
                result.append(sb.toString() + tag + "\n");
            } else {
                String content = p.tag;
                result.append(sb.toString() + content + "\n");
            }
        }

        return result.toString();
    }
}

class Pair {
    String tag;
    int indent;
    String type;

    public Pair(String tag, int in) {
        this.tag = tag;
        this.indent = in;
        this.type = "tag";
    }

    public Pair(String tag, int in, String type) {
        this.tag = tag;
        this.indent = in;
        this.type = type;
    }
}