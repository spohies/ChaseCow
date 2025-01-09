import java.awt.*;
public class Line {
    private Point start;
    private Point end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
    
    public boolean intersects(Line l) {
        int d1 = direction(this.start, this.end, l.start);
        int d2 = direction(this.start, this.end, l.end);
        int d3 = direction(l.start, l.end, this.start);
        int d4 = direction(l.start, l.end, this.end);

        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
            ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
            return true;
        }

        return d1 == 0 && onSegment(this.start, l.start, this.end) ||
                d2 == 0 && onSegment(this.start, l.end, this.end) ||
                d3 == 0 && onSegment(l.start, this.start, l.end) ||
                d4 == 0 && onSegment(l.start, this.end, l.end);
    }

    public boolean onSegment(Point p1, Point p2, Point p3) {
        return p2.x <= Math.max(p1.x, p3.x) && p2.x >= Math.min(p1.x, p3.x) &&
                p2.y <= Math.max(p1.y, p3.y) && p2.y >= Math.min(p1.y, p3.y);
    }

    public int direction(Point p1, Point p2, Point p3) {
        return (p2.x - p1.x) * (p3.y - p1.y) - (p3.x - p1.x) * (p2.y - p1.y);
    }

    public boolean containsPoint(Point p) {
        return direction(this.start, this.end, p) == 0 && onSegment(this.start, p, this.end);
    }

    public double getDistance(Point p){
        double xlength = this.end.x - this.start.x;
        double ylength = this.end.y - this.start.y;
        double u = ((p.x - this.start.x) * xlength + (p.y - this.start.y) * ylength) / (xlength * xlength + ylength * ylength);
        if (u < 0) {
            return Math.sqrt((p.x - this.start.x) * (p.x - this.start.x) + (p.y - this.start.y) * (p.y - this.start.y));
        }
        if (u > 1) {
            return Math.sqrt((p.x - this.end.x) * (p.x - this.end.x) + (p.y - this.end.y) * (p.y - this.end.y));
        }
        return Math.sqrt((p.x - this.start.x - u * xlength) * (p.x - this.start.x - u * xlength) + (p.y - this.start.y - u * ylength) * (p.y - this.start.y - u * ylength));
    }

    public double length(){
        return Math.sqrt((this.end.x - this.start.x) * (this.end.x - this.start.x) + (this.end.y - this.start.y) * (this.end.y - this.start.y));
    }

    public Point closestPoint(Point p) {
        double x1 = start.x, y1 = start.y;
        double x2 = end.x, y2 = end.y;
        double x3 = p.x, y3 = p.y;

        double dx = x2 - x1;
        double dy = y2 - y1;
        double t = ((x3 - x1) * dx + (y3 - y1) * dy) / (dx * dx + dy * dy);

        if (t < 0) {
            return start;
        } 
        else if (t > 1) {
            return end;
        } 
        else {
            return new Point((int)(x1 + t * dx), (int)(y1 + t * dy));
        }
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
