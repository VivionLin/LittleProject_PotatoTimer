package Quartz;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RecordWin extends JFrame {

	private JComboBox<String> cb = new JComboBox<String>();
	private JScrollPane sp = new JScrollPane();
	private JTextArea ta = new JTextArea();

	public RecordWin(String title) {
		Dimension dim = this.getToolkit().getScreenSize();

		cb.addItem("");
		String[] names = getAllLog();
		for (String name : names) {
			cb.addItem(name);
		}
		ta.setEditable(false);
		
		if(names.length == 0) {
			ta.setText(Constant.NO_LOG);
		}
		
		sp.setViewportView(ta);

		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		container.add(cb, BorderLayout.NORTH);
		container.add(sp, BorderLayout.CENTER);

		this.setResizable(false);
		this.setLocation((int) (dim.getWidth() - Constant.WIN_WIDTH) / 2,
				(int) (dim.getHeight() - Constant.WIN_HEIGHT * 4) / 2);
		this.setSize(Constant.WIN_WIDTH, Constant.WIN_HEIGHT * 4);
		this.setTitle(title);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		
		cb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == 1) {
					if(cb.getSelectedItem().equals("")) {
						ta.setText("");
					} else {
						ta.setText(logContent(LogConfig.saveBase + cb.getSelectedItem()));
					}
				}
			}
		});
	}

	public String[] getAllLog() {
		String[] files = new File(LogConfig.saveBase).list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if(filename.contains(new SimpleDateFormat("yyyyMMdd").format(new Date()))) {
					return false;
				} else {
					return filename.matches("^\\d{8}.log$");
				}
			}
		});
		return files;
	}

	public String logContent(String filename) {
		StringBuffer content = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String str;
			while ((str = br.readLine()) != null) {
				content.append(str).append("\n");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}

}
