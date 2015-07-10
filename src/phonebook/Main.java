package phonebook;

import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;
import java.util.TreeSet;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import resources.ResourceHelper;
import utils.Account;
import utils.Contact;
import utils.State;

public final class Main extends JFrame {

    public static void main(String[] args) {

        //</editor-fold>
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main main = new Main();
            }
        });
    }

    private utils.MainUtils utils;
    private TreeSet<Account> accounts;
    private State mainstate;
    private final Main maininstance = this;
    private Account selectedaccount;
    private Phonebook phonebook;
    private JDialog addEditDialog;
    private AddEditContact addEditContactPanel;
    public final Account DEFAULT_ACCOUNT;
    public final String DEFAULT_ACCOUNT_NAME = "default";

    Main() {
        this(null);
    }

    Main(String account) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 1, 0, 0));
        utils = new utils.MainUtils();
        accounts = utils.readAccounts();

        try {
            setIconImage(ResourceHelper.getImageObject("icon.png"));
        } catch (Exception ex) {

        }

        //Checking for existance of default account and create if not found.
        {
            Account defac = findAccount(DEFAULT_ACCOUNT_NAME);
            if (defac == null) {
                DEFAULT_ACCOUNT = createAccount("default");
            } else {
                DEFAULT_ACCOUNT = defac;
            }
        }
        utils.restoreState(this);
        mainstate.setLastopened();
        if (account == null) {
            selectedaccount = mainstate.getSelectedac();
        } else {
            selectedaccount = findAccount(DEFAULT_ACCOUNT_NAME);
        }
        setTitle(selectedaccount.getName());
        phonebook = new Phonebook(maininstance, getSelectedaccount());
        add(phonebook);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e
            ) {
                utils.storeState(maininstance);
            }
        });
        setMainMenuBar();
        prepareAllDialogs();
    }

    private Account findAccount(String acName) {
        final Account defac;
        Optional<Account> optional = accounts.stream().filter(ac -> {
            if (ac.getName().equals(acName)) {
                return true;
            } else {
                return false;
            }
        }).findFirst();
        if (optional.isPresent()) {
            defac = optional.get();
        } else {
            defac = null;
        }
        return defac;
    }

    public void prepareAllDialogs() {
        //preparing addEditDialog
        addEditDialog = new JDialog(this, ModalityType.APPLICATION_MODAL);
        addEditContactPanel = new AddEditContact() {
            @Override
            public void close() {
                addEditDialog.dispose();
            }

            @Override
            public JDialog getContainingDialog() {
                return addEditDialog;
            }

            @Override
            public void onCreate(Contact contact) {
                getSelectedaccount().loadContacts().add(contact);
                getSelectedaccount().storeContacts();
                phonebook.reset();
            }

            @Override
            public void onEdit(Contact contact) {
                Contact prev = getReceivedContact();
                Contact now = contact;
                TreeSet<Contact> contacts = getSelectedaccount().loadContacts();
                if (contacts.contains(prev)) {
                    contacts.remove(prev);
                    contacts.add(now);
                    getSelectedaccount().storeContacts();
                    phonebook.reset();
                }
            }
        };
        addEditDialog.add(addEditContactPanel);
        addEditDialog.setSize(addEditContactPanel.getPreferredSize());
        Point loc = getLocationOnScreen();
        addEditDialog.setLocation(loc.x + getWidth() / 4, loc.y + getHeight() / 4);
    }

    JMenu accountsmenu = new JMenu("Accounts");

    private void setMainMenuBar() {
        javax.swing.JMenuBar menubar = new javax.swing.JMenuBar();

        //adding a menu "File"
        JMenu filemenu = new JMenu("File");
        menubar.add(filemenu);

        //adding menu item "Add Account" in "File"
        JMenuItem filemenu_addAccount = new JMenuItem("Add Account");
        filemenu_addAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccount();
            }
        });
        filemenu.add(filemenu_addAccount);

        JMenuItem filemenu_loadContacts = new JMenuItem("Load Contacts");
        filemenu_loadContacts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadContacts();
            }
        });
        filemenu.add(filemenu_loadContacts);

        // adding menu item "Exit" in "File"
        JMenuItem filemenu_exit = new JMenuItem("Exit");
        filemenu_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maininstance.dispose();
            }
        });
        filemenu.add(filemenu_exit);

        resetAccountsInMenuBar();

        menubar.add(accountsmenu);
        setJMenuBar(menubar);
    }

    private void resetAccountsInMenuBar() {
        accountsmenu.removeAll();
        for (final Account ac : accounts) {
            JMenuItem accountsmenu_account = new JMenuItem(ac.getName());
            accountsmenu_account.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changeAccount(ac.getName());
                }
            });
            accountsmenu.add(accountsmenu_account);
        }
    }

    protected void deleteAccount() {
        int resp = JOptionPane.showConfirmDialog(this, "Are you confirm to delete this account?",
                "Delete Account", JOptionPane.WARNING_MESSAGE);
        if (resp == 0) {
            accounts.remove(getSelectedaccount());
            utils.storeAccounts(accounts);
            resetAccountsInMenuBar();
            changeAccount(DEFAULT_ACCOUNT_NAME);
        }
    }

    protected void createAccount() {
        String acname = JOptionPane.showInputDialog(this,
                "Enter the name of account to create", "Create Account", JOptionPane.PLAIN_MESSAGE);
        acname = acname.trim();
        if (acname == null) {
            return;
        }
        Account ac = findAccount(acname);
        if (acname.length() > 1 && ac == null) {
            createAccount(acname);
            resetAccountsInMenuBar();
            changeAccount(acname);
        } else {
            JOptionPane.showMessageDialog(this, "Please Enter a valid Account Name");
            deleteAccount();
        }
    }

    private Account createAccount(String acName) {
        Account ac = new Account(acName);
        accounts.add(new Account(acName));
        utils.storeAccounts(accounts);
        return ac;
    }

    public void changeAccount(String acName) {
        Account ac = findAccount(acName);
        if (ac == null) {
            ac = DEFAULT_ACCOUNT;
        }
        selectedaccount = ac;
        setTitle(selectedaccount.getName());
        phonebook.changeAccount(ac);
    }

    protected void addEditContact(Contact contact) {
        addEditContactPanel.reset(contact);
        addEditDialog.setVisible(true);
    }

    protected void deleteContact(Contact contact) {
        int doDelete = JOptionPane.showConfirmDialog(this, "Confirm to delete selected contact", "Delete", JOptionPane.YES_NO_OPTION);
        if (doDelete == 0) {
            getSelectedaccount().getContacts().remove(contact);
            getSelectedaccount().storeContacts();
            phonebook.reset();
        }
    }

    protected void loadContacts() {
        JFileChooser fc = new JFileChooser();
        if (fc.showDialog(fc, "Load") == 0) {

        }
    }

    /**
     * @return get last state of application (location and dimension)
     */
    public State getMainstate() {
        return mainstate;
    }

    /**
     * @param mainstate Set last state of application (location and dimension)
     */
    public void setMainstate(State mainstate) {
        this.mainstate = mainstate;
    }

    /**
     * @return the selectedaccount
     */
    public Account getSelectedaccount() {
        return selectedaccount;
    }
}
