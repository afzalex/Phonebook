package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;
import javax.swing.JPanel;

public class FZScrollBar extends JPanel {

    private boolean toshow = true;

    private final Component container;
    private FZScrollListener containerScrollListener;
    private Dimension defaultSize = new Dimension(10, 10);
    private final Consumer<Integer> ownPostMoveNotify = (toMoveLoc) -> {
        repaint();
    };
    private final Consumer<Integer> containerPostMoveNotify = (toMoveLoc) -> {
        System.out.println("Container moved");
    };
    ComponentAdapter containerComponentAdapter = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            System.out.println("resized");
        }
    };
    private FZScrollListener ownScrollListener = new FZScrollListener(this, 0, 1, 0) {
        @Override
        public int moveToLoc(int movetoloc) {
            if (movetoloc < 0) {
                return 0;
            }
            int lim = container.getHeight() - 10;
            if (movetoloc > lim) {
                return lim;
            }
            return movetoloc;
        }
    };

    public FZScrollBar(Component comp) {
        this.container = comp;
        setOpaque(true);
        setLayout(null);
        setPreferredSize(defaultSize);
        container.addComponentListener(containerComponentAdapter);
        ownScrollListener.addPostMoveNotify(ownPostMoveNotify);
        setBackground(transparent);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        setDefaultSize(preferredSize);
    }

    int span = 5;
    Color transparent = new Color(0, 0, 0, 0);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(transparent);
        super.paintComponent(g);
        g2d.setColor(new Color(0.5f, 0.5f, 0f, 0.75f));
        int barWidth = 3 * defaultSize.width / 4;
        RoundRectangle2D rect = new RoundRectangle2D.Float(0, span, barWidth,
                getHeight() - 2 * span, 5, 5);
        RoundRectangle2D block = new RoundRectangle2D.Float(0, ownScrollListener.getLoc(),
                barWidth, 20, 5, 5);
        g2d.fill(rect);
        g2d.setColor(Color.BLACK);
        updateUI();
    }

    /**
     * @return the containerScrollListener
     */
    public FZScrollListener getContainerScrollListener() {
        return containerScrollListener;
    }

    /**
     * @param containerScrollListener the containerScrollListener to set
     */
    public void setContainerScrollListener(FZScrollListener containerScrollListener) {
        if (containerScrollListener != null) {
            containerScrollListener.removePostMoveNotify(containerPostMoveNotify);
        }
        this.containerScrollListener = containerScrollListener;
        this.containerScrollListener.addPostMoveNotify(containerPostMoveNotify);
    }

    public void disassamble() {
        container.removeComponentListener(containerComponentAdapter);
        containerScrollListener.removePostMoveNotify(containerPostMoveNotify);
    }

    /**
     * @return the defaultSize
     */
    public Dimension getDefaultSize() {
        return defaultSize;
    }

    /**
     * @param defaultSize the defaultSize to set
     */
    public void setDefaultSize(Dimension defaultSize) {
        this.defaultSize = defaultSize;
    }
}
