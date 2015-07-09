package utils;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.swing.JComponent;

/**
 * ScrollListener could be added to any JComponent,<br />
 * It will let you know the amount of length that could be scrolled.<br />
 *
 * @author Afzalex
 */
public class FZScrollListener {

    private final JComponent compt;
    private Point pressed, dragged, released;
    private int loc, temp;
    private final float speed, wheelspeed;

    ArrayList<Predicate<Integer>> preMoveCheckList = new ArrayList<>();
    ArrayList<Consumer<Integer>> postMoveNotifyList = new ArrayList<>();

    Predicate<Integer> preMoveCheckDefault = (moveToLoc) -> {
        return true;
    };

    /**
     * It will initialize the ScrollListener with : <br />
     * initial position = 0<br />
     * speed = 1
     *
     * @param compt The Component whose scrolling is to listen
     */
    public FZScrollListener(JComponent compt) {
        this(compt, 0, 1);
    }

    /**
     * It will initialize the ScrollListener with : <br />
     * speed = 1
     *
     * @param compt The Component whose scrolling is to listen
     * @param initialPosition The starting position
     */
    public FZScrollListener(JComponent compt, int initialPosition) {
        this(compt, initialPosition, 1);
    }

    /**
     * It will initialize the ScrollListener for the given Component and with
     * given initial position and speed
     *
     * @param compt The Component whose scrolling is to listen
     * @param initialPosition The starting position
     * @param speed The speed of scrolling
     */
    public FZScrollListener(JComponent compt, int initialPosition, float speed) {
        this(compt, initialPosition, speed, speed);
    }

    /**
     * It will initialize the ScrollListener for the given Component and with
     * given initial position and speed
     *
     * @param compt The Component whose scrolling is to listen
     * @param initialPosition The starting position
     * @param speed The speed of scrolling
     */
    public FZScrollListener(JComponent compt, int initialPosition, float speed, float wheelspeed) {
        this.compt = compt;
        this.speed = speed;
        loc = initialPosition;
        this.wheelspeed = wheelspeed;
        preMoveCheckList.add(preMoveCheckDefault);
        addListeners();
    }

    private MouseAdapter mouseadapter = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            pressed = e.getLocationOnScreen();
            temp = getLoc();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (dragged != null) {
                released = e.getLocationOnScreen();
                checkIfToMove(released);
                dragged = pressed = null;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (pressed != null) {
                dragged = e.getLocationOnScreen();
                checkIfToMove(dragged);
            }
        }
    };

    private MouseWheelListener wheeladapter = new MouseWheelListener() {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            checkIfToScroll(e.getScrollAmount() * e.getWheelRotation());
        }
    };

    private void checkIfToScroll(int distance) {
        int tomoveloc = (int) (getLoc() + (wheelspeed * distance));
        if (preMoveCheckList(tomoveloc)) {
            move(tomoveloc);
            postMoveNotifyList(getLoc());
        }
    }

    private void checkIfToMove(Point wrtPoint) {
        int tomoveloc = toMovePoint(wrtPoint);
        if (preMoveCheckList(tomoveloc)) {
            move(tomoveloc);
            postMoveNotifyList(getLoc());
        }
    }

    private int toMovePoint(Point wrtPoint) {
        return temp + (int) (speed * (wrtPoint.y - pressed.y));
    }

    public void addPreMoveCheck(Predicate<Integer> preMoveCheck) {
        preMoveCheckList.add(preMoveCheck);
    }

    public void removePreMoveCheck(Predicate<Integer> preMoveCheck) {
        preMoveCheckList.remove(preMoveCheck);
    }

    /**
     * this method will be called before moving the point.
     * <br />
     * And the move will be performed if the true is returned
     *
     * @param movetoloc new position supposed to be set
     * @return true if the move is to be performed, false otherwise
     */
    public boolean preMoveCheckList(int movetoloc) {
        for (Predicate<Integer> preMoveCheck : preMoveCheckList) {
            if (!preMoveCheck.test(movetoloc)) {
                return false;
            }
        }
        return true;
    }

    private void move(int movetoloc) {
        setLoc(moveToLoc(movetoloc));
    }

    /**
     * To be overriden if wants to change move instructions.
     * @param movetoloc the value that is supposed to be set
     * @return the value you want to set
     */
    public int moveToLoc(int movetoloc) {
        return movetoloc;
    }

    private void addListeners() {
        compt.addMouseListener(mouseadapter);

        compt.addMouseMotionListener(mouseadapter);

        compt.addMouseWheelListener(wheeladapter);
    }

    /**
     * It will remove all listeners added by ScrollListener
     */
    public void removeListeners() {
        for (MouseListener listener : compt.getMouseListeners()) {
            if (listener == mouseadapter) {
                compt.removeMouseListener(listener);
            }
        }
        for (MouseMotionListener listener : compt.getMouseMotionListeners()) {
            if (listener == mouseadapter) {
                compt.removeMouseMotionListener(listener);
            }
        }

        for (MouseWheelListener listener : compt.getMouseWheelListeners()) {
            if (listener == wheeladapter) {
                compt.removeMouseWheelListener(listener);
            }
        }
    }

    public void addPostMoveNotify(Consumer<Integer> postMoveNotify) {
        postMoveNotifyList.add(postMoveNotify);
    }

    public void removePostMoveNotify(Consumer<Integer> postMoveNotify) {
        postMoveNotifyList.remove(postMoveNotify);
    }

    /**
     * This function will be called iff the move is performed
     *
     * @param loc new location
     */
    private void postMoveNotifyList(int loc) {
        postMoveNotifyList.stream().forEach((postMoveNotify) -> {
            postMoveNotify.accept(loc);
        });
    }

    /**
     * @return the loc
     */
    public int getLoc() {
        return loc;
    }

    /**
     * @param loc the loc to set
     */
    public void setLoc(int loc) {
        this.loc = loc;
    }
}
