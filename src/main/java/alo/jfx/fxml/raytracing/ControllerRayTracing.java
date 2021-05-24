package alo.jfx.fxml.raytracing;

import alo.jfx.controller.MyClosable;
import alo.jfx.fxml.hub.ControllerHub;
import alo.jfx.model.GameOfLife;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
    private final List<Point2D[]> listLines = new ArrayList<>();

    private final List<Point2D[]> listRays = new ArrayList<>();
    /**
     * {@link Slider} to modify the number of Rays
     */
    @FXML
    Slider sliderNbRays;
    private double nbRays = 180;
    /**
     * If Selected, then lines will be drawn on each side of the {@link Canvas}
     */
    @FXML
    private CheckBox checkBoxWalls;

    /**
     * {@link Label} used to display the value selected via
     * the {@link Slider} sliderNbRays
     */
    @FXML
    private Label labelNbRays;
    private Point2D caster = null;

    /**
     * Canvas for displaying the {@link GameOfLife} result of the Ray Casting
     */
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

        setupNbRaySlider();
        setupListLines();
        setupNbRaysLabel();

        refresh();

        canvasRayTracing.addEventHandler(MouseEvent.ANY, this::eventActionOnCanvas);
    }

    @FXML
    public void onClickStart() {
        logger.debug("Start Ray Tracing Animation...");

        setupListLines();

        refresh();
    }

    /**
     * Draw a single line, on the {@link Canvas}, between {@link Point2D} A and {@link Point2D} B
     *
     * @param a {@link Point2D} A
     * @param b {@link Point2D} B
     */
    private void drawLine(Point2D a, Point2D b) {
        gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
    }

    /**
     * Draw lines stored in a list<Point2D[]> on the {@link Canvas}.
     * All lines will be drawn using the same {@link Color}
     *
     * @param listLines lines to draw
     * @param color     color used to draw the lines
     */
    private void drawLines(List<Point2D[]> listLines, Color color) {
        gc.setStroke(color);
        for (Point2D[] line :
                listLines) {
            drawLine(line[0], line[1]);
        }
    }

    /**
     * Clear the {@link Canvas} and draw all the lines (not the Rays)
     */
    private void refresh() {
        gc.clearRect(0, 0, canvasRayTracing.getWidth(), canvasRayTracing.getHeight());
        drawLines(listLines, Color.BLUE);
    }

    private void refreshAll() {
        refresh();
        drawLines(listRays, Color.RED);
    }

    /**
     * Given a single Ray (identified by two {@link Point2D}) this method
     * will return the nearest collision {@link Point2D} with a line, if any,
     * from the Caster
     *
     * @param caster    Caster of the ray
     * @param direction indicate the direction of the ray (same Ref as the caster)
     * @return nearest {@link Point2D} of collision or null if no collision detected
     */
    private Point2D testCollision(Point2D caster, Point2D direction) {
        Point2D collision;
        Point2D nearest = null;
        double distMin = 99999;
        double distCurrent;

        for (Point2D[] line :
                listLines) {
            collision = getIntersection(caster, direction, line[0], line[1]);

            if (collision != null) {
                distCurrent = getDistance(caster, collision);

                if (distCurrent < distMin) {
                    distMin = distCurrent;
                    nearest = collision;
                }
            }
        }

        return nearest;
    }

    /**
     * Given four {@link Point2D} A, B, C and D, this method will return the intersection
     * point P (if exists) between the Ray AB and the Segment CD. It means that this is part
     * of the Segment CD but not necessarily a par of the Segment AB.
     *
     * @param a {@link Point2D} point A
     * @param b {@link Point2D} point B
     * @param c {@link Point2D} point C
     * @param d {@link Point2D} point D
     * @return {@link Point2D} intersection point P
     */
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

        double k = (c.getY() + m * j.getY() - a.getY()) / i.getY();

        // Point is not on segment [AB[
        if (k < 0)
            return null;

        Point2D p = new Point2D(
                c.getX() + m * j.getX(),
                c.getY() + m * j.getY()
        );

        return p;
    }

    /**
     * Calculate and return the distance between two {@link Point2D}
     *
     * @param a first {@link Point2D}
     * @param b second {@link Point2D}
     * @return distance between A and B
     */
    private double getDistance(Point2D a, Point2D b) {
        return sqrt(pow(b.getX() - a.getX(), 2) + pow(b.getY() - a.getY(), 2));
    }

    private void setupNbRaySlider() {
        sliderNbRays.setMin(0);
        sliderNbRays.setMax(360);
        sliderNbRays.setValue(nbRays);

        sliderNbRays.setShowTickMarks(true);
        sliderNbRays.setShowTickLabels(true);

        sliderNbRays.setMajorTickUnit(90);
        sliderNbRays.setMinorTickCount(4);

        sliderNbRays.setBlockIncrement(1);

        sliderNbRays.setSnapToTicks(true);

        sliderNbRays.addEventHandler(MouseEvent.ANY, this::eventChangeNbRays);
    }

    private void setupNbRaysLabel() {
        labelNbRays.setText("" + (int) nbRays);
    }

    /**
     * Generate and store (don't draw) a random number of lines.
     * Those lines will be used to test collisions with rays.
     */
    private void setupListLines() {
        listLines.clear();

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

        if (checkBoxWalls.isSelected())
            addSurroundingWalls();
    }

    private void addSurroundingWalls() {
        // North wall
        listLines.add(new Point2D[]{
                new Point2D(1, 1),
                new Point2D(canvasRayTracing.getWidth() - 1, 1)
        });

        // South wall
        listLines.add(new Point2D[]{
                new Point2D(1, canvasRayTracing.getHeight() - 1),
                new Point2D(canvasRayTracing.getWidth() - 1, canvasRayTracing.getHeight() - 1)
        });

        // East wall
        listLines.add(new Point2D[]{
                new Point2D(canvasRayTracing.getWidth() - 1, 1),
                new Point2D(canvasRayTracing.getWidth() - 1, canvasRayTracing.getHeight() - 1)
        });

        // West wall
        listLines.add(new Point2D[]{
                new Point2D(1, 1),
                new Point2D(1, canvasRayTracing.getHeight() - 1)
        });
    }

    /**
     * Ray casting: cast Rays around the Caster (identified by its coordinates)
     * and store them in a list.
     * If the Ray collide with multiple lines, the Ray stored will
     * be the smallest one (comparing distances between the caster
     * and the points of the collision).
     * If no collision are detected, then a small line will be store instead.
     *
     * @param x x-coordinate of the Caster
     * @param y y-coordinate of the Caster
     */
    private void setupListRays(double x, double y) {
        caster = new Point2D(x, y);

        listRays.clear();

        double angleBetweenRays = 360 / nbRays;
        double length = 10;

        for (int index = 0; index < nbRays; index++) {
            Point2D b = new Point2D(
                    caster.getX() + cos(toRadians(angleBetweenRays * index)) * length,
                    caster.getY() + sin(toRadians(angleBetweenRays * index)) * length);

            Point2D collision = testCollision(caster, b);

            if (collision != null)
                listRays.add(new Point2D[]{caster, collision});
            else
                listRays.add(new Point2D[]{caster, b});
        }
    }

    private void eventActionOnCanvas(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED
                || event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            setupListRays(event.getX(), event.getY());
            refreshAll();
        }
    }

    private void eventChangeNbRays(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_RELEASED
                || event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            nbRays = sliderNbRays.getValue();
            setupNbRaysLabel();
            setupListRays(caster.getX(), caster.getY());
            refreshAll();
        }
    }
}
