package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	/* class level member objects */
	Dao dao = new Dao(); /* For CRUD stuff */
	Boolean chkIfAdmin = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	/* Sub menu item objects for all Main menu item objects */
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;

	/* Extras */
	JMenuItem mnuItemCloseTicket;

	public Tickets(Boolean isAdmin) {

		chkIfAdmin = isAdmin;
		createMenu();
		prepareGUI();

	}

	private void createMenu() {

		/* Initialize sub menu items */
		/* initialize sub menu item for File main menu */
		mnuItemExit = new JMenuItem("Exit");

		/* adding to File main menu item */
		mnuFile.add(mnuItemExit);

		/* initialize first sub menu items for Admin main menu */
		mnuItemUpdate = new JMenuItem("Update Ticket");

		/* adding to Admin main menu item */
		mnuAdmin.add(mnuItemUpdate);

		/* initialize second sub menu items for Admin main menu */
		mnuItemDelete = new JMenuItem("Delete Ticket");

		/* adding to Admin main menu item */
		mnuAdmin.add(mnuItemDelete);

		/* initialize first sub menu item for Tickets main menu */
		mnuItemOpenTicket = new JMenuItem("Open Ticket");

		/* adding to Ticket Main menu item */
		mnuTickets.add(mnuItemOpenTicket);

		/* initialize second sub menu item for Tickets main menu */
		mnuItemViewTicket = new JMenuItem("View Ticket");

		/* adding to Ticket Main menu item */
		mnuTickets.add(mnuItemViewTicket);

		/* initialize sub menu item for Admin main menu */
		mnuItemCloseTicket = new JMenuItem("Close Ticket");

		/* adding to Admin Main menu item */
		mnuAdmin.add(mnuItemCloseTicket);

		/* Add action listeners for each desired menu item */
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);

		/* Extras */
		mnuItemCloseTicket.addActionListener(this);
	}

	private void prepareGUI() {

		/* create JMenu bar */
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); /* add main menu items in order, to JMenuBar */

		if (chkIfAdmin == true) {
			bar.add(mnuAdmin);
			mnuAdmin.setVisible(true);
		}

		else {
			mnuAdmin.setVisible(false);
		}
		
		bar.add(mnuTickets);

		/* add menu bar components to frame */
		setJMenuBar(bar);
		addWindowListener(new WindowAdapter() {
			
			/* define a window close operation */
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});

		/* set frame options */
		setSize(500, 500);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		/* implement actions for sub menu items */
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} 
		
		/* Opening a ticket */
		else if (e.getSource() == mnuItemOpenTicket) {

			/* get ticket info */
			String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

			/* insert ticket information to database */
			int id = dao.insertRecords(ticketName, ticketDesc);

			/* display results if successful or not to console / dialog box */
			if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created!");
			} 
			
			else
				System.out.println("Ticket cannot be created.");
		}

		/* Viewing tickets */
		else if (e.getSource() == mnuItemViewTicket) {

			/* retrieve all tickets details for viewing in JTable */
			try {
				/* Use JTable built in functionality to build a table model and
				display the table model off your result set!!! */
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
				jt.setBounds(30, 40, 200, 400);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); /* refreshes or repaints frame on screen */

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		/* Updating tickets */
		else if (e.getSource() == mnuItemUpdate) {

			try {
				int ticketID = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the ticket id you want to update"));
				String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

				int i = dao.updateRecords(ticketID, ticketDesc);

				if (i != 0) {
					JOptionPane.showMessageDialog(null, "Update successful!");
				}

				else {
					JOptionPane.showMessageDialog(null, "Please try again.");
				}
			}

			catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		/* Closing tickets */
		else if (e.getSource() == mnuItemCloseTicket) {
			int ticketID = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the ticket ID for the ticket you wish to close"));

			try {
				dao.closeRecords(ticketID);
				JOptionPane.showMessageDialog(null, "Ticket closed successfully!");
			}

			catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		/* Deleting a ticket */
		else if (e.getSource() == mnuItemDelete) {
			
			try {
				int ticketID = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ticket ID you wish to delete"));

				String delete = JOptionPane.showInputDialog(null, "To delete ticket id " + ticketID + " type 'DELETE' into the field below");

				if (delete.equals("DELETE")) {
					dao.deleteRecords(ticketID);
					JOptionPane.showMessageDialog(null, "Ticket has been deleted!");	
				}

				else {
					JOptionPane.showMessageDialog(null, "Please try again");
				}
			}

			catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}
