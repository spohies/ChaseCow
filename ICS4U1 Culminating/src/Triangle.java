// triangle object
// Description: for collision detection along diagonal walls
import java.awt.*;

public class Triangle {
    private Point[] vertices;

    // constructor
    public Triangle(Point p1, Point p2, Point p3) {
        vertices = new Point[]{p1, p2, p3};
    }

    // Description: check if triangle intersects with rectangle
    // Parameters: rectangle to check
    // Returns: 0 if no intersection, 1 if rectangle is inside triangle, 2 if triangle is inside rectangle, 3 if there is a line segment intersection
    public int intersects(Rectangle rectangle) {
        // check if any vertex of the rectangle is inside the triangle
        Point[] rectangleVertices = {new Point(rectangle.getLocation().x, rectangle.getLocation().y),
                new Point(rectangle.getLocation().x + rectangle.width, rectangle.getLocation().y),
                new Point(rectangle.getLocation().x + rectangle.width, rectangle.getLocation().y + rectangle.height),
                new Point(rectangle.getLocation().x, rectangle.getLocation().y + rectangle.height)};
        for (Point vertex : rectangleVertices) {
            if (containsPoint(vertex)) {
                return 1;
            }
        }
        
        // check if any vertex of the triangle is inside the rectangle
        for (Point vertex : vertices) {
            if (rectangle.contains(vertex)) {
                return 2;
            }
        }
        
        // check for line segment intersections
        Line[] triangleEdges = {
            new Line(vertices[0], vertices[1]),
            new Line(vertices[1], vertices[2]),
            new Line(vertices[2], vertices[0])
        };

        Line[] rectangleEdges = {
            new Line(rectangleVertices[0], rectangleVertices[1]),
            new Line(rectangleVertices[1], rectangleVertices[2]),
            new Line(rectangleVertices[2], rectangleVertices[3]),
            new Line(rectangleVertices[3], rectangleVertices[0])};

        for (Line triangleEdge : triangleEdges) {
            for (Line rectangleEdge : rectangleEdges) {
                if (triangleEdge.intersects(rectangleEdge)) {
                    return 3;
                }
            }
        }

        return 0;
    }

    // Description: check if point is inside triangle
    // Parameters: point to check
    // Returns: true if point is inside triangle, false otherwise
    public boolean containsPoint(Point p) {
        int sign1 = getSign(p, vertices[0], vertices[1]);
        int sign2 = getSign(p, vertices[1], vertices[2]);
        int sign3 = getSign(p, vertices[2], vertices[0]);

        return (sign1 >= 0 && sign2 >= 0 && sign3 >= 0) || (sign1 <= 0 && sign2 <= 0 && sign3 <= 0);
    }

    // getters
    public int getSign(Point p1, Point p2, Point p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    public Point[] getVertices() {
        return vertices;
    }
}
