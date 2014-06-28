import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

public class RegexRenamer {

	private JFrame frmRegexRenamer;
	private JLabel lblDirectory;
	private JTextField txtDirectory;
	private JButton btnOpenDirectory;
	private JLabel lblFileRegex;
	private JTextField txtFileRegex;
	private JButton btnOpenFile;
	private JLabel lblNewName;
	private JTextField txtNewName;
	private JButton btnGo;
	private JTextArea textAreaPreview;
	private JButton btnPreiew;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					RegexRenamer window = new RegexRenamer();
					window.frmRegexRenamer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RegexRenamer() {
		initialize();
	}

	protected void onOpenDirectory() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		//
		if (chooser.showOpenDialog(frmRegexRenamer) == JFileChooser.APPROVE_OPTION) {
			getTxtDirectory().setText(
					chooser.getSelectedFile().getAbsolutePath());
		}
	}

	protected void onOpenFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(getTxtDirectory().getText()));
		int result = chooser.showOpenDialog(frmRegexRenamer);
		if (result == JFileChooser.APPROVE_OPTION) {
			String fileName = chooser.getSelectedFile().getName();
			getTxtFileRegex().setText(escapeFileName(fileName));
			getTxtNewName().setText(fileName);
		}
	}

	protected String escapeFileName(String fileName) {
		return Pattern.compile("(\\(|\\)|\\{|\\}|\\[|\\])").matcher(fileName)
				.replaceAll("\\\\$1");
	}

	protected void onRenameFiles(boolean preview) {

		if (getTxtDirectory().getText().isEmpty()) {
			JOptionPane.showMessageDialog(frmRegexRenamer,
					"Please select a directory");
			return;
		}

		if (getTxtFileRegex().getText().isEmpty()) {
			JOptionPane.showMessageDialog(frmRegexRenamer,
					"Please enter a file regex");
			return;
		}

		if (getTxtNewName().getText().isEmpty()) {
			JOptionPane.showMessageDialog(frmRegexRenamer,
					"Please enter a new name");
			return;
		}

		getTextAreaPreview().setText("");

		String newNamePattern = getTxtNewName().getText();
		Pattern pattern = Pattern.compile(getTxtFileRegex().getText());
		File directory = new File(getTxtDirectory().getText());
		File[] files = directory.listFiles();
		for (File file : files) {

			Matcher matcher = pattern.matcher(file.getName());
			if (!matcher.matches()) {
				continue;
			}

			// Adiciona um porque o grupo 0 é todo o texto
			int groupCount = matcher.groupCount() + 1;
			Object[] arguments = new String[groupCount];
			for (int i = 0; i < groupCount; i++) {
				arguments[i] = matcher.group(i);
			}

			String newName = MessageFormat.format(newNamePattern, arguments);

			if (preview) {
				getTextAreaPreview().append(
						file.getName() + "\n\t-> " + newName + "\n");
			} else {
				file.renameTo(new File(directory, newName));
			}
		}

		getTextAreaPreview().append("Done!");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRegexRenamer = new JFrame();
		JPanel contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		frmRegexRenamer.setContentPane(contentPane);
		frmRegexRenamer.setAlwaysOnTop(true);
		frmRegexRenamer.setTitle("Regex Renamer");
		frmRegexRenamer.setBounds(100, 100, 487, 314);
		frmRegexRenamer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 80, 266, 31, 59, 0 };
		gridBagLayout.rowHeights = new int[] { 23, 23, 23, 133, 23, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		frmRegexRenamer.getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_lblDirectory = new GridBagConstraints();
		gbc_lblDirectory.fill = GridBagConstraints.BOTH;
		gbc_lblDirectory.insets = new Insets(0, 0, 5, 5);
		gbc_lblDirectory.gridx = 0;
		gbc_lblDirectory.gridy = 0;
		frmRegexRenamer.getContentPane().add(getLblDirectory(),
				gbc_lblDirectory);
		GridBagConstraints gbc_txtDirectory = new GridBagConstraints();
		gbc_txtDirectory.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDirectory.insets = new Insets(0, 0, 5, 5);
		gbc_txtDirectory.gridwidth = 2;
		gbc_txtDirectory.gridx = 1;
		gbc_txtDirectory.gridy = 0;
		frmRegexRenamer.getContentPane().add(getTxtDirectory(),
				gbc_txtDirectory);
		GridBagConstraints gbc_btnOpenDirectory = new GridBagConstraints();
		gbc_btnOpenDirectory.anchor = GridBagConstraints.NORTH;
		gbc_btnOpenDirectory.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnOpenDirectory.insets = new Insets(0, 0, 5, 0);
		gbc_btnOpenDirectory.gridx = 3;
		gbc_btnOpenDirectory.gridy = 0;
		frmRegexRenamer.getContentPane().add(getBtnOpenDirectory(),
				gbc_btnOpenDirectory);
		GridBagConstraints gbc_lblFileRegex = new GridBagConstraints();
		gbc_lblFileRegex.fill = GridBagConstraints.BOTH;
		gbc_lblFileRegex.insets = new Insets(0, 0, 5, 5);
		gbc_lblFileRegex.gridx = 0;
		gbc_lblFileRegex.gridy = 1;
		frmRegexRenamer.getContentPane().add(getLblFileRegex(),
				gbc_lblFileRegex);
		GridBagConstraints gbc_txtFileRegex = new GridBagConstraints();
		gbc_txtFileRegex.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFileRegex.insets = new Insets(0, 0, 5, 5);
		gbc_txtFileRegex.gridwidth = 2;
		gbc_txtFileRegex.gridx = 1;
		gbc_txtFileRegex.gridy = 1;
		frmRegexRenamer.getContentPane().add(getTxtFileRegex(),
				gbc_txtFileRegex);
		GridBagConstraints gbc_btnOpenFile = new GridBagConstraints();
		gbc_btnOpenFile.anchor = GridBagConstraints.NORTH;
		gbc_btnOpenFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnOpenFile.insets = new Insets(0, 0, 5, 0);
		gbc_btnOpenFile.gridx = 3;
		gbc_btnOpenFile.gridy = 1;
		frmRegexRenamer.getContentPane().add(getBtnOpenFile(), gbc_btnOpenFile);
		GridBagConstraints gbc_lblNewName = new GridBagConstraints();
		gbc_lblNewName.fill = GridBagConstraints.BOTH;
		gbc_lblNewName.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewName.gridx = 0;
		gbc_lblNewName.gridy = 2;
		frmRegexRenamer.getContentPane().add(getLblNewName(), gbc_lblNewName);
		GridBagConstraints gbc_txtNewName = new GridBagConstraints();
		gbc_txtNewName.anchor = GridBagConstraints.NORTH;
		gbc_txtNewName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNewName.insets = new Insets(0, 0, 5, 5);
		gbc_txtNewName.gridx = 1;
		gbc_txtNewName.gridy = 2;
		frmRegexRenamer.getContentPane().add(getTxtNewName(), gbc_txtNewName);
		GridBagConstraints gbc_btnPreiew = new GridBagConstraints();
		gbc_btnPreiew.anchor = GridBagConstraints.NORTH;
		gbc_btnPreiew.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPreiew.insets = new Insets(0, 0, 5, 0);
		gbc_btnPreiew.gridwidth = 2;
		gbc_btnPreiew.gridx = 2;
		gbc_btnPreiew.gridy = 2;
		frmRegexRenamer.getContentPane().add(getBtnPreiew(), gbc_btnPreiew);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		frmRegexRenamer.getContentPane().add(getScrollPane(), gbc_scrollPane);
		GridBagConstraints gbc_btnGo = new GridBagConstraints();
		gbc_btnGo.anchor = GridBagConstraints.NORTH;
		gbc_btnGo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGo.gridwidth = 2;
		gbc_btnGo.gridx = 2;
		gbc_btnGo.gridy = 4;
		frmRegexRenamer.getContentPane().add(getBtnGo(), gbc_btnGo);
	}

	protected JLabel getLblDirectory() {
		if (lblDirectory == null) {
			lblDirectory = new JLabel("Directory:");
		}
		return lblDirectory;
	}

	protected JTextField getTxtDirectory() {
		if (txtDirectory == null) {
			txtDirectory = new JTextField();
			txtDirectory.setEditable(false);
			txtDirectory.setColumns(10);
		}
		return txtDirectory;
	}

	protected JButton getBtnOpenDirectory() {
		if (btnOpenDirectory == null) {
			btnOpenDirectory = new JButton("...");
			btnOpenDirectory.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onOpenDirectory();
				}
			});
		}
		return btnOpenDirectory;
	}

	protected JLabel getLblFileRegex() {
		if (lblFileRegex == null) {
			lblFileRegex = new JLabel("File Regex:");
		}
		return lblFileRegex;
	}

	protected JTextField getTxtFileRegex() {
		if (txtFileRegex == null) {
			txtFileRegex = new JTextField();
			txtFileRegex.setColumns(10);
		}
		return txtFileRegex;
	}

	protected JButton getBtnOpenFile() {
		if (btnOpenFile == null) {
			btnOpenFile = new JButton("...");
			btnOpenFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onOpenFile();
				}
			});
		}
		return btnOpenFile;
	}

	protected JLabel getLblNewName() {
		if (lblNewName == null) {
			lblNewName = new JLabel("New Name:");
		}
		return lblNewName;
	}

	protected JTextField getTxtNewName() {
		if (txtNewName == null) {
			txtNewName = new JTextField();
			txtNewName.setColumns(10);
		}
		return txtNewName;
	}

	protected JButton getBtnGo() {
		if (btnGo == null) {
			btnGo = new JButton("Go");
			btnGo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onRenameFiles(false);
				}
			});
		}
		return btnGo;
	}

	protected JTextArea getTextAreaPreview() {
		if (textAreaPreview == null) {
			textAreaPreview = new JTextArea();
			textAreaPreview.setEditable(false);
			DefaultCaret caret = (DefaultCaret) textAreaPreview.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		}
		return textAreaPreview;
	}

	protected JButton getBtnPreiew() {
		if (btnPreiew == null) {
			btnPreiew = new JButton("Preview");
			btnPreiew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onRenameFiles(true);
				}
			});
		}
		return btnPreiew;
	}

	protected JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTextAreaPreview());
		}

		return scrollPane;
	}
}
