package ru.solver;

import java.util.*;

/*
Требуется разработать консольное приложение на языке Java, интерпретирующее математические выражения.
Должны поддерживаться арифметические операции (+­* /), скобки, функции (sin, cos и exp), а также константы PI и E (число Эйлера).
Между операторами и скобками может быть произвольное число пробелов или не быть совсем.
Выражение читается из аргументов командной строки. Входные данные: строка с математическим выражением.
Выходные данные: результат вычисления выражения с точностью до 5 знака после запятой.
Пример:
    Входные данные: "11 + (exp(2.010635 + sin(PI/2)*3) + 50) / 2".
    Выходные данные: 111.00000 (допускается небольшое отклонение).


Решение построено по принципу Обратной польской записи.
 */
public class Solver {

    HashMap<String,Integer> operMap = new HashMap<String, Integer>();
    List<String> func = new ArrayList<String>();
    Map<String, Double> cons = new HashMap<String, Double>();
    String expression = "";
    String oper = "+-*/";
    String redigit = "^[0-9]+\\.?[0-9]*";
    String refunc = "^[a-zA-Z]+";

    public Solver(String expression){
        this.expression = expression;
    }
    public static void main(String[] args) {
        if (args.length == 0){
            System.out.println("Expression expected");
            return;
        }
        StringBuilder param = new StringBuilder();

        for(String s: args){
            param.append(s);
        }
        System.out.println("Receive expression: " + param.toString());

        Solver main = new Solver(param.toString());

        main.init();
        String out = main.getPRR();
        System.out.println("Output: " + out.toString());
        System.out.println("Result: " + (double)Math.round(main.getSolve(out) * 100000) / 100000);
    }

    public void init(){
        cons.put("PI", 3.14159265358);
        cons.put("e", 2.718281828459045);

        operMap.put("-",0);
        operMap.put("+",0);
        operMap.put("*",1);
        operMap.put("/",1);

        func.add("exp");
        func.add("sin");
        func.add("cos");
        func.add("tg");
    }

    public String getPRR(){
        int pos = 0;
        char ch;
        String resdigit = "";
        String resfunc = "";
        StringBuilder output = new StringBuilder("");
        StringBuilder expr = new StringBuilder(expression);
        Stack<String> stack = new Stack<String>();

        //11 + (exp(2.010635 + sin(PI/2)*3) + 50) / 2
        //11 2.010635 3.14 2 / 3 * sin + 50 + exp 2 / +
        //11 2.010635 3.14 2 / sin 3 * + exp 50 + 2 / +
        while(pos < expr.length() && expr.length() > 0){
            resdigit = Utils.getFirstFound(expr.toString(), redigit);
            resfunc = Utils.getFirstFound(expr.toString(), refunc);
            //System.out.println("next step: " + expr.toString());

            if (resdigit != null){
                //change expr string on resdigit length
                expr.delete(0,resdigit.length());
                output.append(" " + resdigit);
            }else if(resfunc != null){
                expr.delete(0,resfunc.length());
                if (func.contains(resfunc)){
                    stack.push(String.valueOf(resfunc));
                }else if(cons.get(resfunc)!=0){
                    output.append(" " + cons.get(resfunc));
                }
            }else{
                ch = expr.charAt(pos);
                expr.delete(0,1);
                if(ch == '('){
                    stack.push(String.valueOf(ch));
                }else if(ch == ')'){
                    while(!stack.isEmpty() && !stack.peek().equals("(")){
                        output.append(" " + stack.pop());
                    }
                    if (!stack.isEmpty()){
                        stack.pop(); // pop opened braker
                        if(stack.size()>0 && func.contains(stack.peek())){
                            output.append(" " + stack.pop());
                        }
                    }else{
                        System.out.println("Not found opened bracket");
                    }
                }else if(oper.indexOf(ch) >= 0){
                    while(!stack.isEmpty() && operMap.get(stack.peek()) != null &&
                            (operMap.get(stack.peek()) >= operMap.get(String.valueOf(ch)))){
                        output.append(" " + stack.pop());
                    }
                    stack.push(String.valueOf(ch));
                }else{
                    System.out.println("Not found anything: " + String.valueOf(ch));
                }
            }
        }
        while(stack.size()>0){
            output.append(" " + stack.pop());
        }
        return output.toString();
    }
    public double getSolve(String output){
        StringTokenizer tokenizer = new StringTokenizer(output.toString(), " ");
        Stack<Double> digits = new Stack<Double>();
        float result = 0f;
        while(tokenizer.hasMoreTokens()){
            String token = tokenizer.nextToken();
            if(token.matches(redigit)){
                digits.push(Double.valueOf(token));
            }else if(token.matches(refunc)){
                //find like function
                if(token.equals("sin")){
                    digits.push(Math.sin(digits.pop()));
                }
                if(token.equals("cos")){
                    digits.push(Math.cos(digits.pop()));
                }
                if(token.equals("tg")){
                    digits.push(Math.tan(digits.pop()));
                }
                if(token.equals("exp")){
                    digits.push(Math.exp(digits.pop()));
                }
            }else if(oper.contains(token)){
                Double f1 = digits.pop();
                Double f2 = digits.pop();

                if(token.equals("-")){
                    digits.push(f2 - f1);
                }
                if(token.equals("+")){
                    digits.push(f2 + f1);
                }
                if(token.equals("*")){
                    digits.push(f2 * f1);
                }
                if(token.equals("/")){
                    digits.push(f2 / f1);
                }
            }
        }
        while(digits.size()>0){
            result += digits.pop();
        }
        return result;
    }
}