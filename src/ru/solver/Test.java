package ru.solver;

/**
 * Created with IntelliJ IDEA.
 * User: akuzin
 * Date: 21.08.14
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static int method1 (int a, int n) {
        int p = 0;
        System.out.println("a=" + a + ", n=" + n);
        if (n==0) {
            p=1;
        } else if (n % 2 == 0) {
//'%' in Java - modulus operator
            p = method1(a, n/2)*method1(a, n/2);
        } else {
            p = method1(a, n-1)*a;
        }
        return p;
    }
    public static void main(String[] args){
        System.out.println("return: " + method1(2,5));
    }
}
