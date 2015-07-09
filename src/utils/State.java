package utils;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Date;

/**
 * It will keep in account the location, size and 
 * other information of the window when it is last opened.
 * @author Afzalex
 */
public class State implements Serializable {
    private Rectangle area;
    private Account selectedac;
    private Date firstopened;
    private Date lastopened;
    
    State(Rectangle area, Account selectedac){
        this.area = area;
        this.selectedac = selectedac;
        this.firstopened = new Date();
        this.lastopened = new Date();
    }

    /**
     * @return the area of window
     */
    public Rectangle getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(Rectangle area) {
        this.area = area;
    }

    /**
     * @return the account which is selected
     */
    public Account getSelectedac() {
        return selectedac;
    }

    /**
     * @param selectedac the selected account
     */
    public void setSelectedac(Account selectedac) {
        this.selectedac = selectedac;
    }

    /**
     * @return date when application is first opened
     */
    public Date getFirstopened() {
        return firstopened;
    }

    /**
     * @return date when application is last opened
     */
    public Date getLastopened() {
        return lastopened;
    }

    /**
     * @param lastopened date when application is last opened
     */
    public void setLastopened() {
        lastopened = new Date();
    }
    
}
