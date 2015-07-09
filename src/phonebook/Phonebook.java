package phonebook;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.function.Consumer;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import utils.Account;
import utils.Contact;

public class Phonebook extends JPanel {

    private Account account;
    protected final Main maininstance;
    private final NamePane namepane;
    private final DetPane detpane;

    Phonebook(Main maininstance, Account account) {
        this.maininstance = maininstance;
        this.account = account;
        account.loadContacts();
        namepane = new NamePane(this);
        detpane = new DetPane(this);
        addComponents();
        setVisible(true);
        namepane.reset();
    }

    private void addComponents() {
        setLayout(new GridLayout(1, 1, 0, 0));
        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, namepane, detpane);
        pane.setDividerSize(5);
        pane.setResizeWeight(0.33f);
        add(pane);
    }

    public void changeAccount(Account account) {
        this.account = account;
        account.loadContacts();
        namepane.reset();
        detpane.reset();
    }

    public void reset(){
        account.loadContacts();
        namepane.reset();
        detpane.reset();
    }
    
    protected TreeSet<Contact> getContacts() {
        return getAccount().getContacts();
    }
    
    ArrayList<Consumer<Contact>> consumersA = new ArrayList<>();
    
    protected void addConsumerA(Consumer<Contact> cons){
        consumersA.add(cons);
    }
    
    protected void callConsumersA(Contact cont){
        for(Consumer<Contact> cons : consumersA){
            cons.accept(cont);
        }
    }

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }
}
