import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.*; 

/*
 * Created on Oct 6, 2004
 */


/**
 * @author Jonathan
 */
	public class MenuClass implements ActionListener, ItemListener {
	    public JMenuBar createMenuBar() {
			JMenuBar menuBar;
			JMenu menu, submenu;
			JMenuItem menuItem;

			//Create the menu bar.
			menuBar = new JMenuBar();

			//Build the first menu.
			menu = new JMenu("File");
			menu.setMnemonic('F');
			menuBar.add(menu);

			//menuItem = new JMenuItem("New Scene", KeyEvent.VK_T);
			//menuItem.setMnemonic('N');
			//menuItem.addActionListener(this);
			//menu.add(menuItem);
			//menu.addSeparator();
			
			//menuItem = new JMenuItem("Open Scene", KeyEvent.VK_T);
			//menuItem.setMnemonic('O');
			//menuItem.addActionListener(this);
			//menu.add(menuItem);
			
			//menuItem = new JMenuItem("Save Scene As", KeyEvent.VK_T); 				////////
			//menuItem.setMnemonic('A');
			//menuItem.addActionListener(this);
			//menu.add(menuItem);
				
			//menu.addSeparator();
			
			//menuItem = new JMenuItem("Insert Object", KeyEvent.VK_T);
			//menuItem.setMnemonic('N');
			//menuItem.addActionListener(this);
			//menu.add(menuItem);
			//menu.addSeparator();
			
			menuItem = new JMenuItem("Exit", KeyEvent.VK_T); 				////////
			menuItem.setMnemonic('X');
			menuItem.addActionListener(this);
			menu.add(menuItem);

			/* Build the help menu */
			menu = new JMenu("Help");
			menu.setMnemonic('H');

			menuItem = new JMenuItem("About", KeyEvent.VK_T);
			menuItem.setMnemonic('A');
			menuItem.addActionListener(this);
			menu.add(menuItem);

			menuBar.add(menu);
			return menuBar;
		}

		public void actionPerformed(ActionEvent ev) {
			JMenuItem source = (JMenuItem) (ev.getSource());

			/*if (source.getText() == "New Scene") {
				System.out.println("NEW menuitem called");
			}
			if (source.getText() == "Open Scene") {
				System.out.println("OPEN menuitem called");
				//fileOpen();
			}
			if (source.getText() == "Save Scene As") {                  
				System.out.println("SAVE VECTOR AS menuitem called");    
				//fileSave();
			}
			if (source.getText() == "Export JPEG image") {               //////////
				System.out.println("EXPORT JPEG IMAGE menuitem called");
				//fileSaveJPEG();
			}
		
			if (source.getText() == "Print") {
				System.out.println("PRINT menuitem called");
				//filePrint();
			}
			*/
			if (source.getText() == "Exit") {
				System.out.println("EXIT menuitem called");
				System.exit(0);
			}
			if (source.getText() == "About") {
				System.out.println("ABOUT menuitem called");
				JOptionPane aboutframe = null;
				JOptionPane.showMessageDialog(aboutframe, "Cretatous Rendersaurus v0.1 \n\n " +
						"Created by Geoff Brown, Jonathan Coulam and Chris Smith");
			}
		}
		public void itemStateChanged(ItemEvent ev) {
			System.out.println(ev.getSource());
		}
	}
	
