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
    
    public static int getDistSq(int x, int y, int x1, int y1){
        int dx = x - x1;
        int dy = y - y1;
        return (dx * dx) + (dy * dy);
    }
    
}
