package curve;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import util.MathUtil;

/**
 *
 * @author Kaamil Jasani
 */
public class ControlPoint {
    
    public Color color;
    public Color outerColor;
    
    public Color selectedColor;
    public Color selectedOuterColor;
    
    public Color selectedPointColor;
    public Color selectedPointOuterColor;
    
    public static final int POINT_INNER_RADIUS = 6;
    public static final int POINT_OUTER_RADIUS = 22;
    public static final int POINT_OUTER_RADIUS_SQ = POINT_OUTER_RADIUS * POINT_OUTER_RADIUS;
    public static final float ANIM_SPEED = 6;
    
    private Point pos;
    private double weight;
    private boolean hovered = false;
    private boolean held = false;
    private boolean selected = true;
    private float hoveredTime = 0;
    
    public ControlPoint(int x, int y){
        this.pos = new Point(x, y);
        weight = 1;
        color = new Color(255, 175, 60, 255);
        outerColor = new Color(255, 175, 60, 100);
        selectedColor = new Color(255, 60, 60, 255);
        selectedOuterColor = new Color(255, 60, 60, 100);
        selectedPointColor = new Color(60, 175, 60, 255);
        selectedPointOuterColor = new Color(60, 175, 60, 100);
    }
    
    public ControlPoint(int x, int y, double weight){
        this.pos = new Point(x, y);
        this.weight = weight;
        color = new Color(255, 175, 60, 255);
        outerColor = new Color(255, 175, 60, 100);
        selectedColor = new Color(255, 60, 60, 255);
        selectedOuterColor = new Color(255, 60, 60, 100);
        selectedPointColor = new Color(60, 175, 60, 255);
        selectedPointOuterColor = new Color(60, 175, 60, 100);
    }
    
    public ControlPoint(int x, int y, double weight, boolean selected){
        this.pos = new Point(x, y);
        this.weight = weight;
        this.selected = selected;
        color = new Color(255, 175, 60, 255);
        outerColor = new Color(255, 175, 60, 100);
        selectedColor = new Color(255, 60, 60, 255);
        selectedOuterColor = new Color(255, 60, 60, 100);
        selectedPointColor = new Color(60, 175, 60, 255);
        selectedPointOuterColor = new Color(60, 175, 60, 100);
    }
    
    public void draw(Graphics2D g, boolean selected){
        if(!selected){
            g.setPaint(outerColor);
            g.fillOval(pos.x - POINT_OUTER_RADIUS, pos.y - POINT_OUTER_RADIUS, POINT_OUTER_RADIUS*2, POINT_OUTER_RADIUS*2);
            g.setColor(color);
            int POINT_INNER_RADIUS = (int) (this.POINT_INNER_RADIUS + (this.POINT_OUTER_RADIUS - this.POINT_INNER_RADIUS) * hoveredTime);
            g.fillOval(pos.x - POINT_INNER_RADIUS, pos.y - POINT_INNER_RADIUS, POINT_INNER_RADIUS*2, POINT_INNER_RADIUS*2);
            g.setFont(g.getFont().deriveFont(g.getFont().getSize2D()*2));
            int weightWidth = (int) g.getFont().getStringBounds("w: " + weight, g.getFontRenderContext()).getWidth();
            g.drawString("w: " + weight, pos.x - weightWidth/2, pos.y + g.getFont().getSize() + POINT_OUTER_RADIUS);
            g.setFont(g.getFont().deriveFont(g.getFont().getSize2D()/2));
        }else if(this.selected){
            g.setPaint(selectedPointOuterColor);
            g.fillOval(pos.x - POINT_OUTER_RADIUS, pos.y - POINT_OUTER_RADIUS, POINT_OUTER_RADIUS*2, POINT_OUTER_RADIUS*2);
            g.setColor(selectedPointColor);
            int POINT_INNER_RADIUS = (int) (this.POINT_INNER_RADIUS + (this.POINT_OUTER_RADIUS - this.POINT_INNER_RADIUS) * hoveredTime);
            g.fillOval(pos.x - POINT_INNER_RADIUS, pos.y - POINT_INNER_RADIUS, POINT_INNER_RADIUS*2, POINT_INNER_RADIUS*2);
            g.setFont(g.getFont().deriveFont(g.getFont().getSize2D()*2));
            int weightWidth = (int) g.getFont().getStringBounds("w: " + weight, g.getFontRenderContext()).getWidth();
            g.drawString("w: " + weight, pos.x - weightWidth/2, pos.y + g.getFont().getSize() + POINT_OUTER_RADIUS);
            g.setFont(g.getFont().deriveFont(g.getFont().getSize2D()/2));
        }else{
            g.setPaint(selectedOuterColor);
            g.fillOval(pos.x - POINT_OUTER_RADIUS, pos.y - POINT_OUTER_RADIUS, POINT_OUTER_RADIUS*2, POINT_OUTER_RADIUS*2);
            g.setColor(selectedColor);
            int POINT_INNER_RADIUS = (int) (this.POINT_INNER_RADIUS + (this.POINT_OUTER_RADIUS - this.POINT_INNER_RADIUS) * hoveredTime);
            g.fillOval(pos.x - POINT_INNER_RADIUS, pos.y - POINT_INNER_RADIUS, POINT_INNER_RADIUS*2, POINT_INNER_RADIUS*2);
            g.setFont(g.getFont().deriveFont(g.getFont().getSize2D()*2));
            int weightWidth = (int) g.getFont().getStringBounds("w: " + weight, g.getFontRenderContext()).getWidth();
            g.drawString("w: " + weight, pos.x - weightWidth/2, pos.y + g.getFont().getSize() + POINT_OUTER_RADIUS);
            g.setFont(g.getFont().deriveFont(g.getFont().getSize2D()/2));
        }
    }
    
    public void update(){
        if(hovered){
            hoveredTime = hoveredTime + (1/ANIM_SPEED) > 1 ? 1 : hoveredTime + (1/ANIM_SPEED);
        }else{
            hoveredTime = hoveredTime - (1/ANIM_SPEED) < 0 ? 0 : hoveredTime - (1/ANIM_SPEED);
        }
    }

    public Point getPos() {
        return pos;
    }
    
    public int getX(){
        return pos.x;
    }
    
    public int getY(){
        return pos.y;
    }
    
    public double getWeightedX(){
        return pos.x * weight;
    }
    
    public double getWeightedY(){
        return pos.y * weight;
    }
    
    public void setX(int x){
        pos.x = x;
    }
    
    public void setY(int y){
        pos.y = y;
    }
    
    public boolean isInside(Point point){
        int dx = point.x*2 - pos.x;
        int dy = point.y*2 - pos.y;
        return (dx * dx) + (dy * dy) < POINT_OUTER_RADIUS_SQ;
    }
    
    public void onHover(){
        hovered = true;
    }
    
    public void onExit(){
        hovered = false;
    }
    
    public void onPress(){
        held = true;
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean isHeld() {
        return held;
    }

    public void onRelease() {
        held = false;
    }
    
    public void onDrag(int dx, int dy){
        pos.x += dx*2;
        pos.y += dy*2;
    }

    public void setColor(Color color) {
        this.color = color;
        setOuterColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
    }

    public void setOuterColor(Color outerColor) {
        this.outerColor = outerColor;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = MathUtil.round(weight, 2);
    }

    public void setWeightRaw(double weight) {
        this.weight = weight;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
    
}
