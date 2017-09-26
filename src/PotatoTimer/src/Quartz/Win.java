package Quartz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * 主界面：倒计时、主题显示、开始/停止 功能：开始计时、到时提醒、显示剩下时间、任务主题、主题纪录日志
 * 
 * @author Vivion
 * @time 2016/10/31
 */
public class Win extends JFrame implements ActionListener, MouseListener {

	// 配置
	static {
		Constant.getInstance(null);
	}

	//日志配置
	private LogConfig log = new LogConfig();

	/**
	 * 顶部菜单栏
	 */
	private JMenuBar menuBar = new JMenuBar();
	
	/**
	 * 一级菜单（新增、纪录、语言）
	 */
	private JMenu[] menu = new JMenu[3];
	
	/**
	 * 二级菜单（语言下拉选择）
	 */
	private JMenuItem[] menuItem;

	/**
	 * 标签（任务主题、计时、任务）
	 */
	private JLabel[] label = { new JLabel(""), new JLabel(timeFormat(Constant.TIME_LONG)), new JLabel("") };
	
	/**
	 * 北部任务主题面板
	 */
	private JPanel panel00 = new JPanel();
	
	/**
	 * 中部大面板（包含时间小面板和任务及按钮小面板）
	 */
	private JPanel panel01 = new JPanel();
	
	/**
	 * 时间小面板
	 */
	private JPanel panel1 = new JPanel();
	
	/**
	 * 任务及按钮小面板
	 */
	private JPanel panel2 = new JPanel();
	
	/**
	 * 开始土豆钟按钮
	 */
	private JButton start = new JButton();
	
	/**
	 * 结束土豆钟按钮
	 */
	private JButton end = new JButton();
	
	/**
	 * 中止土豆钟按钮
	 */
	private JButton abort = new JButton();
	
	/**
	 * 按钮小面板（开始、结束、中止）
	 */
	private JPanel btnpanel = new JPanel();

	/**
	 * 新增任务对话框
	 */
	private JDialog dialog = new JDialog(this, "");
	
	/**
	 * 输入框
	 */
	private JTextField input = new JTextField();
	
	/**
	 * 新增任务确定
	 */
	private JButton add = new JButton();
	
	/**
	 * 主题确定
	 */
	private JButton sure = new JButton();
	
	/**
	 * 长/短休息时长计数
	 */
	private int count = 0;

	/*
	 * 0 正常 1 完成 -1 中断
	 */
	private volatile int stop = 0;

	public void setContent() {
		start.setText(Constant.BTN_START);
		end.setText(Constant.BTN_END);
		abort.setText(Constant.BTN_ABORT);
		add.setText(Constant.BTN_ADD);
		sure.setText(Constant.BTN_SVE);
		menu[0].setText(Constant.MENU_NEW);
		menu[1].setText(Constant.MENU_LOG);
		menu[2].setText(Constant.MENU_LANG);
		for (int i = 0; i < Constant.LANGs.length; i++) {
			menuItem[i].setText(Constant.LANGs[i]);
		}
		this.setTitle(Constant.TITLE);
		label[0].setText(Constant.BURNPLAN.equals("")?Constant.NO_PLAN : Constant.BURNPLAN);
	}

	public Win() {
		Dimension dim = this.getToolkit().getScreenSize();
		int x = (int) (dim.getWidth() - Constant.WIN_WIDTH) / 2;
		int y = 50;

		/*
		 * 菜单：新增、查看日志、关闭
		 */
		menu[0] = new JMenu();
		menu[1] = new JMenu();
		menu[2] = new JMenu();

		menuItem = new JMenuItem[Constant.LANGs.length];
		for (int i = 0; i < Constant.LANGs.length; i++) {
			menuItem[i] = new JMenuItem();
			menu[2].add(menuItem[i]);
			menuItem[i].addActionListener(this);
		}

		setContent();

		menu[0].addMouseListener(this);
		menu[1].addMouseListener(this);

		menuBar.add(menu[0]);
		menuBar.add(menu[1]);
		menuBar.add(menu[2]);

		/*
		 * 弹出的对话框
		 */
		dialog.setSize(Constant.DIAWIN_WIDTH, Constant.DIAWIN_HEIGHT);
		dialog.setLocation(x + (Constant.WIN_WIDTH - Constant.DIAWIN_WIDTH) / 2, y
				+ (Constant.WIN_HEIGHT - Constant.DIAWIN_HEIGHT) / 2);
		dialog.setResizable(false);
		Container dcp = dialog.getContentPane();
		dcp.setLayout(new BorderLayout());
		dcp.add(input, BorderLayout.CENTER);
		add.addActionListener(this);
		sure.addActionListener(this);

		label[0].setForeground(new Color(108, 0, 108));
		label[0].setFont(new Font("微软雅黑", Font.BOLD, 15));
		panel00.add(label[0]);
		panel00.addMouseListener(this);

		label[1].setFont(new Font("Arial", 1, 32));
		panel1.add(label[1]);

		panel2.setLayout(new GridLayout(2, 1));
		panel2.add(label[2]);
		panel2.add(btnpanel);
		btnpanel.setLayout(new GridLayout(1, 3));
		btnpanel.add(start);
		btnpanel.add(end);
		btnpanel.add(abort);
		start.setEnabled(false);
		end.setEnabled(false);
		abort.setEnabled(false);
		start.addActionListener(this);
		end.addActionListener(this);
		abort.addActionListener(this);

		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		container.add(panel00, BorderLayout.NORTH);
		container.add(panel01, BorderLayout.CENTER);
		panel01.setLayout(new GridLayout(1, 2));
		panel01.add(panel1);
		panel01.add(panel2);

		this.setJMenuBar(menuBar);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocation(x, y);
		this.setSize(Constant.WIN_WIDTH, Constant.WIN_HEIGHT);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		Win win = new Win();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == add) {
			// 新任务
			label[2].setText(input.getText());
			dialog.setVisible(false);
			start.setEnabled(true);
		} else if (e.getSource() == sure) {
			// 本日计划主题
			String burn = input.getText();
			System.out.println(burn.equals(""));
			if(burn.equals("")) {
				label[0].setText(Constant.NO_PLAN);
				log.logplan(Constant.NO_PLAN);
			} else {
				label[0].setText(burn);
				log.logplan(burn);
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("burnplan", burn);
			Constant.getInstance(params);
			dialog.setVisible(false);
		} else if (e.getSource() == start) {
			// 开始
			stop = 0;
			label[1].setText(timeFormat(Constant.TIME_LONG));
			log.logbegin(input.getText());
			new Timer().start();
			end.setEnabled(true);
			abort.setEnabled(true);
			start.setEnabled(false);
			setAlwaysOnTop(false);
		} else if (e.getSource() == end) {
			// 完成
			if (JOptionPane.showConfirmDialog(null, Constant.END_CONFIRM, Constant.TITLE, JOptionPane.YES_NO_OPTION) == 0) {
				stop = 1;
				abort.setEnabled(false);
			}
		} else if (e.getSource() == abort) {
			// 中断
			if (JOptionPane.showConfirmDialog(null, Constant.ABORT_CONFIRM, Constant.TITLE, JOptionPane.YES_NO_OPTION) == 0) {
				stop = -1;
				end.setEnabled(false);
				start.setEnabled(true);
			}
		} else if (e.getSource() instanceof JMenuItem) {
			// 语言切换
			Map<String, String> params = new HashMap<String, String>();
			params.put("lang", e.getActionCommand());
			Constant.getInstance(params);
			setContent();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == menu[0]) {
			// 新增
			dialog.setTitle(Constant.DIALOG_TITLE);
			dialog.remove(sure);
			dialog.getContentPane().add(add, BorderLayout.EAST);
			dialog.setVisible(true);
		} else if (e.getSource() == menu[1]) {
			// 查看记录
			RecordWin rw = new RecordWin(Constant.MENU_LOG);
		} else if (e.getSource() == panel00) {
			// 修改今日主题
			dialog.setTitle(Constant.TITLE);
			dialog.getContentPane().remove(add);
			dialog.getContentPane().add(sure, BorderLayout.EAST);
			dialog.setVisible(true);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	/*
	 * 定时器
	 */
	class Timer extends Thread {
		@Override
		public void run() {
			try {
				label[1].setForeground(new Color(0, 176, 255));
				for (int i = 0; i < Constant.TIME_LONG / 1000;) {
					if (stop == 0) {
						sleep(1000);
						i++;
						int left = Constant.TIME_LONG - i * 1000;
						if (left < 60000) {
							label[1].setForeground(new Color(0, 200, 0));
						}
						label[1].setText(timeFormat(left));
					} else if (stop == 1 || stop == -1) {
						break;
					}
				}

				label[1].setForeground(Color.BLACK);
				end.setEnabled(false);
				abort.setEnabled(false);
				start.setText(Constant.BTN_CONTINUE);
				setState(Frame.NORMAL); // 窗口弹出
				setAlwaysOnTop(true);

				if (stop == 0 || stop == 1) {
					Object[] clockpoints = { "A+", "A", "B+", "B", "C+", "C" };
					Object rate;
					do {
						rate = JOptionPane.showInputDialog(null, Constant.RATE, Constant.TITLE,
								JOptionPane.INFORMATION_MESSAGE, null, clockpoints, clockpoints[3]);
						System.out.println(rate);
					} while (rate == null);
					log.logend(input.getText(), (String) rate);
					JOptionPane
							.showMessageDialog(null, Constant.ALERT, Constant.TITLE, JOptionPane.INFORMATION_MESSAGE);
					count++;
					new RestTimer().start();
				} else {
					log.logabort(input.getText());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class RestTimer extends Thread {
		@Override
		public void run() {
			menu[0].setEnabled(false);
			int resttime = (count % 4 == 0)? Constant.LONGREST_TIME_LONG : Constant.REST_TIME_LONG;
			
			try {
				label[1].setForeground(new Color(218, 112, 214)); // purple
				label[1].setText(timeFormat(resttime));
				for (int i = 0; i < resttime / 1000;) {
					sleep(1000);
					i++;
					int left = resttime - i * 1000;
					label[1].setText(timeFormat(left));
				}

				label[1].setForeground(Color.BLACK);
				label[1].setText(timeFormat(Constant.TIME_LONG));
				start.setEnabled(true);
				setState(Frame.NORMAL); // 窗口弹出
				setAlwaysOnTop(true);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				menu[0].setEnabled(true);
			}
		}
	}

	private String timeFormat(int time) {
		StringBuffer sb = new StringBuffer();
		int min = time / 1000 / 60;
		int sec = time / 1000 % 60;
		sb.append((min > 9) ? "" : "0").append(min).append(":").append((sec > 9) ? "" : "0").append(sec);
		return sb.toString();
	}
}
