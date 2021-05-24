package alo.jfx.fxml.raytracing;

import alo.jfx.controller.MyClosable;
import alo.jfx.fxml.hub.ControllerHub;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

public class ControllerRayTracing implements MyClosable {

    private static final Logger logger = LogManager.getLogger(ControllerHub.class);

    private final Random rand = new Random();
    private final List<Point2D[]> listLines = new ArrayList<Point2D[]>();
    List<Point2D> listRay;
    @FXML
    private Canvas canvasRayTracing;
    private GraphicsContext gc;

    @Override
    public void onCloseEvent() {
        logger.info("Controller onCloseEvent: " + this.getClass().getCanonicalName());
    }

    public void initialize() throws IOException {
        logger.info("Controller initialize...");

        gc = canvasRayTracing.getGraphicsContext2D();

        //sampleLines();
        setupListLines();
        drawLines();

        canvasRayTracing.addEventHandler(MouseEvent.ANY, this::eventActionOnCanvas);
    }

    @FXML
    private void onClickStart() {
        logger.debug("Start Ray Tracing Animation...");
        drawRef();
    }

    private void drawRef() {
        gc.setStroke(Color.BLUE);
        gc.strokeLine(0, 0, 300, 0);

        gc.setStroke(Color.RED);
        gc.strokeLine(0, 0, 0, 300);
    }


    private void drawLine(Point2D a, Point2D b) {
        gc.setStroke(Color.BLUE);
        gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
    }

    private void sampleLines() {

        // Horizontal
        Point2D a = new Point2D(100, 250);
        Point2D b = new Point2D(250, 250);
        drawLine(a, b);

        // Vertical
        Point2D c = new Point2D(300, 150);
        Point2D d = new Point2D(300, 450);
        drawLine(c, d);

        // Cross should be at (300, 250) ...
        Point2D p = getIntersection(a, b, c, d);

        if (p != null) {
            drawLine(a, p);
        }
    }

    private Point2D getIntersection(Point2D a, Point2D b, Point2D c, Point2D d) {

        Point2D i = new Point2D(b.getX() - a.getX(), b.getY() - a.getY());
        Point2D j = new Point2D(d.getX() - c.getX(), d.getY() - c.getY());

        double denominator = (i.getX() * j.getY()) - (i.getY() * j.getX());

        // No intersection point
        if (denominator == 0f)
            return null;

        double numerator = i.getY() * (c.getX() - a.getX()) - i.getX() * (c.getY() - a.getY());

        double m = numerator / denominator;

        // Point is not on segment [CD]
        if (m < 0 || m > 1)
            return null;

        Point2D p = new Point2D(
                c.getX() + m * j.getX(),
                c.getY() + m * j.getY()
        );

        return p;
    }

    private void setupListLines() {
        int nbLines = rand.nextInt(10);
        int xMax = (int) canvasRayTracing.getWidth();
        int yMax = (int) canvasRayTracing.getHeight();


        for (int index = 0; index < nbLines; index++) {
            listLines.add(
                    new Point2D[]{
                            new Point2D(rand.nextInt(xMax), rand.nextInt(yMax)),
                            new Point2D(rand.nextInt(xMax), rand.nextInt(yMax))
                    });
        }
    }

    private void drawLines() {
        for (Point2D[] line :
                listLines) {
            drawLine(line[0], line[1]);
        }
    }

    private List<Point2D> setupRay(double x, double y) {
        List<Point2D> listRay = new ArrayList<>();

        double nbRays = 18;
        double angleBetweenRays = 360 / nbRays;
        double length = 10;

        for (int index = 0; index < nbRays; index++) {
            listRay.add(new Point2D(
                    x + cos(toRadians(angleBetweenRays * index)) * length,
                    y + sin(toRadians(angleBetweenRays * index)) * length));
        }

        return listRay;
    }

    private void drawRay(double x, double y) {
        for (Point2D point :
                listRay) {
            drawLine(new Point2D(x, y), point);
        }
    }

    private void eventActionOnCanvas(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            listRay = setupRay(event.getX(), event.getY());
            drawRay(event.getX(), event.getY());

            listRay = null;
        }
    }
}
