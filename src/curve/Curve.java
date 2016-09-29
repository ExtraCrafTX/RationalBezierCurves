package curve;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Kaamil Jasani
 */
public class Curve {
    
    public static Color CURVE_COLOR = Color.BLACK;
    
    private CopyOnWriteArrayList<ControlPoint> controlPoints;
    
    private boolean animated;
    private boolean selected;
    private float t;
    
    public Curve(ControlPoint[] points, boolean selected){
        init();
        this.selected = selected;
        controlPoints = new CopyOnWriteArrayList<>(points);
    }
    
    public Curve(int x, int y){
        init();
        controlPoints = new CopyOnWriteArrayList<>(new ControlPoint[]{new ControlPoint(x*2, y*2)});
    }
    
    private void init(){
        animated = false;
        selected = true;
        t = 1;
    }

    public CopyOnWriteArrayList<ControlPoint> getControlPoints() {
        return controlPoints;
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public void draw(Graphics2D g){
        if(controlPoints.size() > 1){
            for(int i = 0; i < controlPoints.size() - 1; i++){
                g.setColor(selected ? controlPoints.get(i).selectedColor : controlPoints.get(i).color);
                g.drawLine(controlPoints.get(i).getX(), controlPoints.get(i).getY(), controlPoints.get(i+1).getX(), controlPoints.get(i+1).getY());
            }
        }
        if(controlPoints.size() > 1)
            connectInterpolated(g);
        for(ControlPoint point : controlPoints){
            point.draw(g, selected);
        }
    }
    
    public void update(){
        for(ControlPoint point : controlPoints){
            point.update();
        }
    }
    
    public void addControlPoint(int x, int y){
        controlPoints.add(new ControlPoint(x*2, y*2));
    }
    
    public void removeControlPoint(ControlPoint point){
        controlPoints.remove(point);
    }
    
    private double interpolate(double percentage, double... coords){
        if(coords.length == 1){
            return coords[0];
        }else if(coords.length == 2){
            return coords[0] + (coords[1] - coords[0]) * percentage;
        }else{
            double[] p1 = new double[coords.length - 1];
            double[] p2 = new double[coords.length - 1];
            for(int i = 0; i < coords.length; i++){
                if(i == 0){
                    p1[i] = coords[i];
                }else if(i == coords.length - 1){
                    p2[i - 1] = coords[i];
                }else{
                    p1[i] = coords[i];
                    p2[i-1] = coords[i];
                }
            }
            return interpolate(percentage, interpolate(percentage, p1), interpolate(percentage, p2));
        }
    }

    private void connectInterpolated(Graphics2D g) {
        g.setColor(CURVE_COLOR);
        double xLast = 0;
        double yLast = 0;
        for(float t = 0; t < this.t; t += 0.005f){
            double[] coordsZ = new double[controlPoints.size()];
            for(int k = 0; k < controlPoints.size(); k++){
                coordsZ[k] = controlPoints.get(k).getWeight();
            }
            double z = interpolate(t, coordsZ);
            double[] coordsX = new double[controlPoints.size()];
            for(int k = 0; k < controlPoints.size(); k++){
                coordsX[k] = controlPoints.get(k).getWeightedX();
            }
            double x = interpolate(t, coordsX)/z;
            double[] coordsY = new double[controlPoints.size()];
            for(int k = 0; k < controlPoints.size(); k++){
                coordsY[k] = controlPoints.get(k).getWeightedY();
            }
            double y = interpolate(t, coordsY)/z;
            if(t != 0){
                g.drawLine((int)xLast, (int)yLast, (int)x, (int)y);
            }
            xLast = x;
            yLast = y;
        }
        double[] coordsZ = new double[controlPoints.size()];
        for(int k = 0; k < controlPoints.size(); k++){
            coordsZ[k] = controlPoints.get(k).getWeight();
        }
        double z = interpolate(t, coordsZ);
        double[] coordsX = new double[controlPoints.size()];
        for(int k = 0; k < controlPoints.size(); k++){
            coordsX[k] = controlPoints.get(k).getWeightedX();
        }
        double x = interpolate(t, coordsX)/z;
        double[] coordsY = new double[controlPoints.size()];
        for(int k = 0; k < controlPoints.size(); k++){
            coordsY[k] = controlPoints.get(k).getWeightedY();
        }
        double y = interpolate(t, coordsY)/z;
        g.fillOval((int)x - ControlPoint.POINT_INNER_RADIUS, (int)y - ControlPoint.POINT_INNER_RADIUS, ControlPoint.POINT_INNER_RADIUS*2, ControlPoint.POINT_INNER_RADIUS*2);
        if(t != 0)
            g.drawLine((int)xLast, (int)yLast, (int)x, (int)y);
    }
    
    public Curve[] split(){
        Curve[] curves = new Curve[2];
        double[] zCoords1 = new double[controlPoints.size()];
        for(int i = 0; i < controlPoints.size(); i++){
            double[] coords = new double[i + 1];
            for(int j = 0; j < i + 1; j++){
                coords[j] = controlPoints.get(j).getWeight();
            }
            zCoords1[i] = interpolate(t, coords);
        }
        double[] xCoords1 = new double[controlPoints.size()];
        for(int i = 0; i < controlPoints.size(); i++){
            double[] coords = new double[i + 1];
            for(int j = 0; j < i + 1; j++){
                coords[j] = controlPoints.get(j).getWeightedX();
            }
            xCoords1[i] = interpolate(t, coords)/zCoords1[i];
        }
        double[] yCoords1 = new double[controlPoints.size()];
        for(int i = 0; i < controlPoints.size(); i++){
            double[] coords = new double[i + 1];
            for(int j = 0; j < i + 1; j++){
                coords[j] = controlPoints.get(j).getWeightedY();
            }
            yCoords1[i] = interpolate(t, coords)/zCoords1[i];
        }
        ControlPoint[] points1 = new ControlPoint[controlPoints.size()];
        for(int i = 0; i < points1.length; i++){
            points1[i] = new ControlPoint(Math.round((float) xCoords1[i]), Math.round((float) yCoords1[i]), zCoords1[i]);
        }
        curves[0] = new Curve(points1, false);
        
        double[] zCoords2 = new double[controlPoints.size()];
        for(int i = 0; i < controlPoints.size(); i++){
            double[] coords = new double[i + 1];
            for(int j = 0; j < i + 1; j++){
                coords[j] = controlPoints.get(controlPoints.size() - 1 - j).getWeight();
            }
            zCoords2[i] = interpolate(1-t, coords);
        }
        double[] xCoords2 = new double[controlPoints.size()];
        for(int i = 0; i < controlPoints.size(); i++){
            double[] coords = new double[i + 1];
            for(int j = 0; j < i + 1; j++){
                coords[j] = controlPoints.get(controlPoints.size() - 1 - j).getWeightedX();
            }
            xCoords2[i] = interpolate(1-t, coords)/zCoords2[i];
        }
        double[] yCoords2 = new double[controlPoints.size()];
        for(int i = 0; i < controlPoints.size(); i++){
            double[] coords = new double[i + 1];
            for(int j = 0; j < i + 1; j++){
                coords[j] = controlPoints.get(controlPoints.size() - 1 - j).getWeightedY();
            }
            yCoords2[i] = interpolate(1-t, coords)/zCoords2[i];
        }
        ControlPoint[] points2 = new ControlPoint[controlPoints.size()];
        for(int i = 0; i < points2.length; i++){
            points2[i] = new ControlPoint(Math.round((float) xCoords2[i]), Math.round((float) yCoords2[i]), zCoords2[i]);
        }
        curves[1] = new Curve(points2, false);
        return curves;
    }
    
}
