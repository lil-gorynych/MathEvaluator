import java.sql.SQLOutput;
import java.util.ArrayList;

public class MathEvaluator {

    public static void main(String[] args) {
        String string = "(5 +  6 + 765) -(3 / 5)";
        System.out.println(calculate(string));
    }

    public static double calculate (String string) {
        Expression expr = new Expression(string);
        expr.parseMinuses();

        for (String i : expr.expression) {
            System.out.print(i + "");
        }
        System.out.println();

        while (expr.expression.size() != 1) {
            expr.doBigAction();
        }


        return Double.parseDouble(expr.expression.get(0));
    }

    public static class Expression {
        public ArrayList<String> expression;
        String signs = "+-*/()";

        public Expression (String string) {
            this.expression = parseToExpression(string);
        }

        private ArrayList<String> parseToExpression (String string) {
            String number = "", letter;
            ArrayList<String> result = new ArrayList<>();

            for (int i = 0; i < string.length(); i++) {
                letter = string.charAt(i) + "";

                if (letter.equals(" ")) { continue; }
                if (this.signs.contains(letter)) {
                    if (number.length() > 0) { result.add(number); number = ""; }

                    result.add(letter);
                } else {
                    number += letter;
                }
            }

            if (number.length() > 0) { result.add(number); }

            result.add(0, "("); result.add(")");

            return result;
        }

        public void parseMinuses () {
            boolean number = false;
            boolean sign = false;
            boolean brackets = false;


        }

        public void doBigAction () {
            int[] box = findBox ();
            String res;
            ArrayList<String> subExpression = new ArrayList<>();

            if (box[1] - box[0] == 1) {
                this.expression.remove(box[1]);
                this.expression.remove(box[0]);
            }
            if (box[1] - box[0] == 2) {
                this.expression.remove(box[1]);
                this.expression.remove(box[0]);
            }
            if (box[1] - box[0] > 2) {
                for (int i = box[0] + 1; i < box[1]; i++) {
                    subExpression.add(this.expression.get(i));
                }
                res = calculateSub (subExpression);
                this.expression.subList(box[0], box[1] + 1).clear();
                this.expression.add(box[0], res);
            }
        }

        private int[] findBox () {
            int[] box = new int[]{0, this.expression.size() - 1};

            for (int i = 0; i < this.expression.size(); i++) {
                if (this.expression.get(i).equals(")")) { box[1] = i; break; }
            }
            for (int i = box[1]; i >= 0; i--) {
                if (this.expression.get(i).equals("(")) { box[0] = i; break; }
            }

            return box;
        }

        private String calculateSub (ArrayList<String> array) {
            while (array.size() != 1) {
                int position = findPosition(array);

                String num1 = array.get(position - 1);
                String sign = array.get(position + 0);
                String num2 = array.get(position + 1);

                array.subList(position-1, position+2).clear();
                array.add(position-1, doOperation(num1, sign, num2));
            }
            return array.get(0);
        }

        private String doOperation (String num1, String sign, String num2) {
            double left = Double.parseDouble(num1);
            double right = Double.parseDouble(num2);

            switch (sign) {
                case "+":
                    return String.valueOf(left + right);
                case "-":
                    return String.valueOf(left - right);
                case "*":
                    return String.valueOf(left * right);
                case "/":
                    return String.valueOf(left / right);
            }
            return "!";
        }

        private int findPosition (ArrayList<String> array) {
            int position = array.indexOf("*");
            if (position == -1) { position = array.indexOf("/"); }
            if (position == -1) { position = array.indexOf("+"); }
            if (position == -1) { position = array.indexOf("-"); }

            return position;
        }

    }
}
