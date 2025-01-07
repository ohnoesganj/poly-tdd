package org.example;

public class Calculator {
    public int run(String str) {
        str = str.trim().replaceAll("\\s+", "");
        return evaluate(str, 0, str.length() - 1);
    }

    private int evaluate(String str, int start, int end) {
        if (start > end) return 0;

        if (str.charAt(start) == '-') {
            if (start + 1 <= end && str.charAt(start + 1) == '(') {
                int closing = findClosingParenthesis(str, start + 1);

                int evaluated = evaluate(str, start + 2, closing - 1);

                evaluated = -evaluated;

                if (closing + 1 <= end) {
                    String remaining = str.substring(closing + 1);
                    String newExpression = "(" + evaluated + ")" + remaining;
                    return evaluate(newExpression, 0, newExpression.length() - 1);
                }

                return evaluated;
            } else {
                return -evaluate(str, start + 1, end);
            }
        }

        int last = -1;
        int priority = 0;

        for (int i = end; i >= start; i--) {
            char c = str.charAt(i);

            if (c == ')') priority++;
            else if (c == '(') priority--;

            if (priority != 0) continue;

            if (c == '+' || c == '-') {
                if (i == start || "+-*/".indexOf(str.charAt(i - 1)) != -1) continue;
                last = i;
                break;
            } else if ((c == '*' || c == '/') && last == -1) {
                last = i;
            }
        }

        if (last == -1) {
            if (str.charAt(start) == '(' && findClosingParenthesis(str, start) == end) {
                return evaluate(str, start + 1, end - 1);
            }

            String extracted = str.substring(start, end + 1);

            return Integer.parseInt(extracted);
        }

        int left = evaluate(str, start, last - 1);
        int right = evaluate(str, last + 1, end);

        char op = str.charAt(last);

        return operator(left, right, op);
    }

    private static int operator(int left, int right, char operator) {
        // 음수끼리 곱한 경우 양수로 계산
        if (operator == '*' && left < 0 && right < 0) {
            return Math.abs(left) * Math.abs(right);
        }

        return switch (operator) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            case '/' -> left / right;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    private int findClosingParenthesis(String str, int openIndex) {
        int count = 0;
        for (int i = openIndex; i < str.length(); i++) {
            if (str.charAt(i) == '(') count++;
            else if (str.charAt(i) == ')') count--;
            if (count == 0) return i;
        }
        throw new IllegalArgumentException("No matching closing parenthesis for index " + openIndex);
    }
}