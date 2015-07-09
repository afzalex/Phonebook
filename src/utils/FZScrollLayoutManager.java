package utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.JComponent;

public class FZScrollLayoutManager implements LayoutManager {

    public static final String FZSCROLLPLANE = "FZSCROLLPLANE";
    public static final String FZSCROLLBAR = "FZSCROLLBAR";

    private FZScrollListener scrolllistener;
    private Component plane;
    private FZScrollBar bar;
    private int top = 0;

    @Override
    public void addLayoutComponent(String name, final Component comp) {
        if (name.equals(FZSCROLLPLANE)) {
            plane = comp;
            scrolllistener = new FZScrollListener((JComponent) plane, 0, 1F, 5F) {
                @Override
                public int moveToLoc(int movetoloc) {
                    boolean topoutofline = movetoloc < 0;
                    boolean bottomoutofline = movetoloc + plane.getHeight() > plane.getParent().getHeight();
                    boolean canfitinparent = plane.getHeight() < plane.getParent().getHeight();
                    if (canfitinparent) {
                        if (topoutofline) {
                            return 0;
                        } else if (bottomoutofline) {
                            return plane.getParent().getHeight() - plane.getHeight();
                        }
                    } else if (!canfitinparent) {
                        if (topoutofline && !bottomoutofline) {
                            return plane.getParent().getHeight() - plane.getHeight();
                        } else if (bottomoutofline && !topoutofline) {
                            return 0;
                        }
                    }
                    return movetoloc;
                }
            };
            scrolllistener.addPostMoveNotify((moveToLoc) -> {
                plane.getParent().doLayout();
            });
        } else if (name.equals(FZSCROLLBAR) && bar == null && comp instanceof FZScrollBar) {
            bar = (FZScrollBar) comp;
            bar.setContainerScrollListener(scrolllistener);
            bar.getParent().setComponentZOrder(bar, 0);
            plane.getParent().setComponentZOrder(plane, 1);
        }
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        if (comp == plane) {
            scrolllistener.removeListeners();
        } else if (comp == bar) {
            bar.disassamble();
        }
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return plane.getPreferredSize();
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return plane.getMaximumSize();
    }

    @Override
    public void layoutContainer(Container parent) {
        Dimension parentSize = parent.getSize();
        if (bar != null) {
            Dimension barSize = bar.getDefaultSize();
            bar.setBounds(parentSize.width - barSize.width, 0, barSize.width, parentSize.height);
        }
        if (plane != null) {
            plane.setBounds(0, scrolllistener.getLoc(), parentSize.width, plane.getHeight());
        }
    }
}
