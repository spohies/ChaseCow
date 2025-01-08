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
}
