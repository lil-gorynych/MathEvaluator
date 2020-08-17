
import java.util.ArrayList;
/*
TODO:
add minimum for '+-' and /*

*/

public class MathEvaluator {

    public static void main(String[] args) {
        String string = "(123.45*(678.90 / (-2.5+ 11.5)-(((80 -(19))) *33.25)) / 20) - (123.45*(678.90 / (-2.5+ 11.5)-(((80 -(19))) *33.25)) / 20) + (13 - 2)/ -(-11) ";
        //String string = "((2.33 / (2.9+3.5)*4) - -6)";

        System.out.println(calculate(string));
    }

    public static double calculate (String string) {
        Expression expr = new Expression(string);
        for (String i : expr.expression) {
            System.out.print(i + " ");
        }
        System.out.println();

        while (expr.expression.size() != 1) {
            int changeIndex = expr.doBigAction();
            expr.recalculateNumbers(changeIndex);

            for (String i : expr.expression) {
                System.out.print(i + " ");
            }
            System.out.println();
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
            boolean startNumber = true;
            ArrayList<String> result = new ArrayList<>();


            for (int i = 0; i < string.length(); i++) {
                letter = string.charAt(i) + "";

                if (letter.equals(" ")) { continue; }
                if (")".contains(letter)) {
                    if (number.length() > 0) { result.add(number); number = ""; }
                    result.add(letter);
                    startNumber = false;
                    continue;
                }
                if ("(/*+".contains(letter)) {
                    if (number.length() > 0) { result.add(number); number = ""; }
                    result.add(letter);
                    startNumber = true;
                    continue;
                }
                if (letter.equals("-") && !startNumber) {
                    if (number.length() > 0) { result.add(number); number = ""; }
                    result.add(letter);
                    startNumber = true;
                    continue;
                }
                number += letter;
                startNumber = false;
            }

            if (number.length() > 0) { result.add(number); }

            result.add(0, "("); result.add(")");
            return result;
        }

        public void recalculateNumbers (int changeIndex) {
            switch (changeIndex) {
                case 0: break;
                case 1:
                    if (this.expression.get(0).equals("-")) {
                        this.expression.remove(0);
                        this.expression.add(0, mirror(this.expression.get(0)));
                        this.expression.remove(1);
                    }
                    break;
                default:
                    if (this.expression.get(changeIndex-1).equals("-")
                            && this.signs.contains(this.expression.get(changeIndex-2))) {
                        this.expression.remove(changeIndex-1);
                        this.expression.add(changeIndex-1, mirror(this.expression.get(changeIndex-1)));
                        this.expression.remove(changeIndex);
                    }
            }
        }

        private String mirror(String str) {
            return String.valueOf(-1 * Double.parseDouble(str));
        }

        public int doBigAction () {
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

            return box[0];
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

                for (String i : array) {
                    System.out.print(i + " ");
                }
                System.out.println();

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

            int position = array.indexOf("/");
            if (position == -1) { position = array.indexOf("*"); }
            if (position == -1) { position = array.indexOf("-"); }
            if (position == -1) { position = array.indexOf("+"); }

            return position;
        }

    }
}
