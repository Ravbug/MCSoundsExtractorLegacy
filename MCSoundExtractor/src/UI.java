import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UI extends JFrame implements ActionListener,EngineCallback{
	
	private static final long serialVersionUID = 4618050888689493696L;
	
	//for platform-specific stuff
	private String OS = System.getProperty("os.name").toLowerCase();

	//UI elements
	private JTextField mcDirectory;
	private JTextField outDirectory;
	private JButton btnChooseMcDir;
	private JButton btnChooseOutDir;
	private JComboBox<String> mcVersionCombo;
	private JButton btnExtractSounds;
	private JProgressBar progressBar;

	public UI() {
		
		//set window title
		super("Minecraft Sounds Extractor by Ravbug");
		
		//closure listener
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		//resize horizontal only
		setSize(500,237);
		setMinimumSize(new Dimension(320,220));
		setMaximumSize(new Dimension(Integer.MAX_VALUE,220));
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		//UI components
		JLabel lblSelectMinecraftDirectory = new JLabel("Minecraft Directory");
		getContentPane().add(lblSelectMinecraftDirectory);
		
		mcDirectory = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, mcDirectory, 32, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, mcDirectory, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblSelectMinecraftDirectory, 0, SpringLayout.WEST, mcDirectory);
		springLayout.putConstraint(SpringLayout.SOUTH, lblSelectMinecraftDirectory, -6, SpringLayout.NORTH, mcDirectory);
		mcDirectory.setToolTipText("Path to the folder where Minecraft is installed");
		getContentPane().add(mcDirectory);
		mcDirectory.setColumns(10);
		mcDirectory.setEditable(false);
		
		btnChooseMcDir = new JButton("Choose");
		springLayout.putConstraint(SpringLayout.EAST, mcDirectory, -6, SpringLayout.WEST, btnChooseMcDir);
		springLayout.putConstraint(SpringLayout.WEST, btnChooseMcDir, -127, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnChooseMcDir, -10, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, btnChooseMcDir, 32, SpringLayout.NORTH, getContentPane());
		getContentPane().add(btnChooseMcDir);
		btnChooseMcDir.addActionListener(this);
		
		JLabel lblOutputDirectory = new JLabel("Output Directory");
		springLayout.putConstraint(SpringLayout.NORTH, lblOutputDirectory, 64, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblOutputDirectory, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, mcDirectory, -6, SpringLayout.NORTH, lblOutputDirectory);
		getContentPane().add(lblOutputDirectory);
		
		outDirectory = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, outDirectory, 80, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, outDirectory, 10, SpringLayout.WEST, getContentPane());
		outDirectory.setToolTipText("Path to the folder you want to put the exported sounds");
		getContentPane().add(outDirectory);
		outDirectory.setColumns(10);
		outDirectory.setEditable(false);
		
		btnChooseOutDir = new JButton("Choose");
		springLayout.putConstraint(SpringLayout.EAST, outDirectory, -6, SpringLayout.WEST, btnChooseOutDir);
		springLayout.putConstraint(SpringLayout.NORTH, btnChooseOutDir, 22, SpringLayout.SOUTH, btnChooseMcDir);
		springLayout.putConstraint(SpringLayout.WEST, btnChooseOutDir, -127, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnChooseOutDir, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnChooseOutDir);
		btnChooseOutDir.addActionListener(this);
		
		JLabel lblMinecraftVersion = new JLabel("Minecraft Version");
		springLayout.putConstraint(SpringLayout.WEST, lblMinecraftVersion, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, outDirectory, -6, SpringLayout.NORTH, lblMinecraftVersion);
		springLayout.putConstraint(SpringLayout.NORTH, lblMinecraftVersion, 112, SpringLayout.NORTH, getContentPane());
		getContentPane().add(lblMinecraftVersion);
		
		mcVersionCombo = new JComboBox<String>();
		springLayout.putConstraint(SpringLayout.NORTH, mcVersionCombo, 0, SpringLayout.SOUTH, lblMinecraftVersion);
		springLayout.putConstraint(SpringLayout.WEST, mcVersionCombo, 10, SpringLayout.WEST, getContentPane());
		mcVersionCombo.setToolTipText("Minecraft version to extract the sounds");
		getContentPane().add(mcVersionCombo);
		
		progressBar = new JProgressBar();
		springLayout.putConstraint(SpringLayout.NORTH, progressBar, 5, SpringLayout.SOUTH, mcVersionCombo);
		springLayout.putConstraint(SpringLayout.WEST, progressBar, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, progressBar, -10, SpringLayout.EAST, getContentPane());
		progressBar.setToolTipText("Current progress");
		getContentPane().add(progressBar);
		
		btnExtractSounds = new JButton("Extract");
		springLayout.putConstraint(SpringLayout.EAST, mcVersionCombo, -6, SpringLayout.WEST, btnExtractSounds);
		springLayout.putConstraint(SpringLayout.NORTH, btnExtractSounds, 21, SpringLayout.SOUTH, btnChooseOutDir);
		springLayout.putConstraint(SpringLayout.WEST, btnExtractSounds, -127, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnExtractSounds, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnExtractSounds);
		btnExtractSounds.addActionListener(this);
		setVisible(true);
		
		//set the system look and feel if possible
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		
		//on mac only allow folder picking
		System.setProperty("apple.awt.fileDialogForDirectories", "true");

		//set UI defaults and attempt to load versions
		setPlatformSpecificData();
		loadMcVersions(mcDirectory.getText());
	}

	@Override
	/**
	 * Called automatically on UI Action events
	 * @param e: ActionEvent object
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnChooseMcDir) {
			String dir = chooseDirectory(mcDirectory.getText(),"Choose the folder where Minecraft is installed.");
			if (dir != null) {
				mcDirectory.setText(dir);
				loadMcVersions(dir);
			}
			
		}
		else if (e.getSource() == btnChooseOutDir) {
			String dir = chooseDirectory(outDirectory.getText(),"Choose the folder to export the sound files.");
			if (dir != null) {
				outDirectory.setText(dir);
			}
			
		}
		else if (e.getSource() == btnExtractSounds) {
			//sanity check input
			//mc directory has versions?
			if (!loadMcVersions(mcDirectory.getText())) {
				return;
			}
			//output directory is writable? (if (Files.isWriteable(path){continue;})
			if (!Files.isWritable(new File(outDirectory.getText()).toPath())) {
				JOptionPane.showMessageDialog(null, "The folder " + outDirectory.getText() + " cannot be written to.\n\nPlease select a different folder.");
				return;
			}
			//item is selected?
			try {
				//get selected item
				String selected = mcVersionCombo.getSelectedItem().toString();
			
				//Set up the engine thread
				Engine en = new Engine(mcDirectory.getText(),outDirectory.getText(),selected);
				//register callbacks
				en.registerCallbacks(this);
				//run
				en.start();
				
				//disable the button
				btnExtractSounds.setEnabled(false);
			
			}catch(NullPointerException ex) {
				JOptionPane.showMessageDialog(null, "Please select a Minecraft version first.");
			}
		}		
	}
	
	/**
	 * Opens a file browser to allow the user to pick a folder
	 * @param baseFolder the folder to set the file browser to on open
	 * @param message instructions for the user to display in the browser
	 * @return the absolute path to the file, or null if no file was picked
	 */
	private String chooseDirectory(String baseFolder,String message) {
		
		//on Mac use the "native" open dialog
		if (OS.contains("mac")) {
			//Prep the FileDialog
			FileDialog fd = new FileDialog(this, message, FileDialog.LOAD);
			fd.setDirectory(baseFolder);
			
			//show the FileDialog
			fd.setVisible(true);
			
			//If something was picked, return the fully-qualified path
			if (fd.getFile() != null) {	
				return fd.getDirectory() + fd.getFile();
			}
			
			//if nothing picked, return null
			return null;	
		}
		else {
			//windows and linux get JFileChooser
			final JFileChooser jfc = new JFileChooser(baseFolder);
		    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    int returnValue = jfc.showOpenDialog(null);
		    
		    //if picked a directory
		    if (returnValue == JFileChooser.APPROVE_OPTION) {
		    	return jfc.getSelectedFile().getAbsolutePath();
		    }
		    
		    //no file chosen return null
		   	return null;   
		}
	}
	
	/**
	 * Sets the default UI data based on the user's operating system
	 */
	private void setPlatformSpecificData() {

		//mac
		if (OS.contains("mac")) {
			mcDirectory.setText(System.getProperty("user.home")+"/Library/Application Support/minecraft");
		}
		//windows
		else if (OS.contains("win")) {
			mcDirectory.setText(System.getProperty("user.home") + "/AppData/Roaming/.minecraft");
		}
		//linux
		else {
			//warn them that the linux case is not tested
			JOptionPane.showMessageDialog(null, "Warning:\nThis program has not been tested on Linux. \nBe sure to check paths.");

			mcDirectory.setText(System.getProperty("user.home")+"/.minecraft");
		}
		outDirectory.setText(System.getProperty("user.home")+"/Desktop");

	}
	
	/**
	 * Loads the valid MC versions from the MC directory
	 * @param path: path to the MC directory
	 * @return true if could load versions, false otherwise
	 */
	private boolean loadMcVersions(String path) {
		//clear existing items in list
		mcVersionCombo.removeAllItems();
		
		//load files in path
		File f = new File(path + "/assets/indexes");
		
		//list only JSON files
		FilenameFilter fnf = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.endsWith(".json"));
			}
		};
		
		try {
			//load the files into the list
			ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles(fnf)));
				
			for (int i = 0; i < files.size(); i++) {
				mcVersionCombo.addItem(files.get(i).getName().replaceFirst("[.][^.]+$", ""));
			}
			return true;
					
		}catch(NullPointerException e) {
			//no compatible files found
			//if statement works around some first launch weirdness with JList
			JOptionPane.showMessageDialog(null, "Unable to find versions in " + path + "\n\nMake sure at least one version of the game is installed there.");
			
			return false;
		}		
	}

	/* Note regarding the following methods
	 * These methods update the UI based on cues from the Engine thread. However, these methods
	 * are executed on the Engine thread, NOT the UI thread. This means that the UI is running on
	 * two threads, which is bad. Because this program is so simple, this splitting won't cause
	 * crashing, but in practice the UI should not be split across independent threads.
	 */
	
	@Override
	/**
	 * Called when the copy completes successfully or fails
	 * @param message: String containing a stack trace, or null if copy finished successfully
	 */
	public void successEvent(String message) {
		//if message is null, success
		if (message == null) {
			//open success dialog
			JOptionPane.showMessageDialog(null, "Export completed successfully!");
		}
		//if message has contents, export failed
		else {
			//open dialog with errors
			JOptionPane.showMessageDialog(null, "Export failed with errors: \n\n" + message);
		}
		//re-enable the button
		btnExtractSounds.setEnabled(true);
		btnExtractSounds.setText("Extract");		
	}
	
	@Override
	/**
	 * Called on progress updates from the background thread
	 * @param progress: integer 0-100 representing the progress
	 */
	public void progressEvent(int progress) {
		progressBar.setValue(progress);
		btnExtractSounds.setText(progress + "%");
	}
}

