package util;

/**
 *
 * @author Kaamil Jasani
 */
public class MathUtil {
    
    public static double round(double number, int places){
        number *= Math.pow(10, places);
        number = Math.round(number);
        return number / Math.pow(10, places);
    }
    
}
