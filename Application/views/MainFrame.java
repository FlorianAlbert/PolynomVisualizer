package views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.parser.ParserDelegator;

import controller.MainFrameController;
import model.GraphPanelModel;
import model.MainFrameModel;
import service.ValueChangedListener;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.ComponentOrientation;
import java.awt.Toolkit;

public class MainFrame extends JFrame implements ValueChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2853729456871453019L;
	private MainFrameModel model;
	private MainFrameController controller;

	private JPanel contentPane;
	private JTextField tfFunctionInput;
	private JTextField tfXMin;
	private JTextField tfXMax;
	private JTextField tfYMin;
	private JTextField tfYMax;

	private JButton btnDelete;
	private JButton btnInsert;

	JList<String> list;
	private JMenu helpMenu;
	private JMenuItem infoMenuItem;

	private InfoDialog infoDialog;
	private ErrorDialog errorDialog;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrameModel model = new MainFrameModel();
					MainFrame frame = new MainFrame("PolyFunctionVisualizer", model);
					model.addValueChangedListener(frame);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public MainFrame(String title, MainFrameModel model) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/views/funktion.png")));
		this.model = model;
		controller = new MainFrameController(model);

		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 838, 439);
		setResizable(false);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setJMenuBar(menuBar);

		helpMenu = new JMenu("Hilfe");
		menuBar.add(helpMenu);

		infoMenuItem = new JMenuItem("Info");
		infoMenuItem.setName("infoMenuItem");
		infoMenuItem.addActionListener(controller);
		helpMenu.add(infoMenuItem);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		GraphPanelModel graphPanelModel = new GraphPanelModel(-5, 5, -5, 5);
		GraphPanel panel = new GraphPanel(graphPanelModel);
		model.setGraphPanelModel(graphPanelModel);
		panel.setBounds(400, 11, 418, 328);
		panel.setBackground(Color.WHITE);
		contentPane.add(panel);

		tfFunctionInput = new JTextField();
		tfFunctionInput.setBounds(400, 350, 310, 23);
		tfFunctionInput.setName("tfFunctionInput");
		tfFunctionInput.addKeyListener(controller);
		contentPane.add(tfFunctionInput);

		btnInsert = new JButton("Bestätigen");
		btnInsert.setBounds(720, 351, 98, 23);
		btnInsert.setName("Bestätigen");
		btnInsert.addActionListener(controller);
		contentPane.add(btnInsert);

		btnDelete = new JButton("Entfernen");
		btnDelete.setBounds(10, 351, 237, 23);
		btnDelete.setName("Entfernen");
		btnDelete.addActionListener(controller);
		contentPane.add(btnDelete);

		list = new JList<String>(model.getListModel());
		list.setBounds(10, 11, 237, 329);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(controller);
		contentPane.add(list);

		JLabel lblXMin = new JLabel("XMin:");
		lblXMin.setBounds(257, 11, 36, 14);
		contentPane.add(lblXMin);

		tfXMin = new JTextField();
		tfXMin.setText(Double.toString(model.getXMin()));
		tfXMin.setBounds(294, 9, 96, 20);
		tfXMin.setName("tfXMin");
		tfXMin.addKeyListener(controller);
		tfXMin.addMouseListener(controller);
		contentPane.add(tfXMin);

		JLabel lblXMax = new JLabel("XMax:");
		lblXMax.setBounds(257, 63, 36, 14);
		contentPane.add(lblXMax);

		tfXMax = new JTextField();
		tfXMax.setText(Double.toString(model.getXMax()));
		tfXMax.setBounds(294, 60, 96, 20);
		tfXMax.setName("tfXMax");
		tfXMax.addKeyListener(controller);
		tfXMax.addMouseListener(controller);
		contentPane.add(tfXMax);

		JLabel lblYMin = new JLabel("YMin:");
		lblYMin.setBounds(257, 115, 36, 14);
		contentPane.add(lblYMin);

		tfYMin = new JTextField();
		tfYMin.setText(Double.toString(model.getYMin()));
		tfYMin.setBounds(294, 111, 96, 20);
		tfYMin.setName("tfYMin");
		tfYMin.addKeyListener(controller);
		tfYMin.addMouseListener(controller);
		contentPane.add(tfYMin);

		JLabel lblYMax = new JLabel("YMax:");
		lblYMax.setBounds(257, 167, 36, 14);
		contentPane.add(lblYMax);

		tfYMax = new JTextField();
		tfYMax.setText(Double.toString(model.getYMax()));
		tfYMax.setBounds(294, 162, 96, 20);
		tfYMax.setName("tfYMax");
		tfYMax.addKeyListener(controller);
		tfYMax.addMouseListener(controller);
		contentPane.add(tfYMax);

		infoDialog = new InfoDialog(controller);
		errorDialog = new ErrorDialog(controller);

		graphPanelModel.addValueChangedListener(panel);
		graphPanelModel.addValueChangedListener(this);
	}

	@Override
	public void onValueChanged() {
		if (model.isValueSetByGraphPanel()) {
			tfXMin.setText(Double.toString((double) Math.round(model.getXMin() * 1000) / 1000));
			tfXMax.setText(Double.toString((double) Math.round(model.getXMax() * 1000) / 1000));
			tfYMin.setText(Double.toString((double) Math.round(model.getYMin() * 1000) / 1000));
			tfYMax.setText(Double.toString((double) Math.round(model.getYMax() * 1000) / 1000));
		}

		if (model.getShowInfoDialog()) {
			this.setEnabled(false);
			infoDialog.setVisible(true);
		} else {
			infoDialog.setVisible(false);
			this.setEnabled(true);
			this.setVisible(true);
		}
		
		if(model.getShowErrorDialog()){
		    this.setEnabled(false);
		    errorDialog.setContent(model.getErrorDialogInfo());
		    errorDialog.setVisible(true);
		} else {
		    errorDialog.setVisible(false);
		    this.setEnabled(true);
		    this.setVisible(true);
		}
		
		if (model.isInputAddedByButton()) {
			tfFunctionInput.requestFocus();
			model.setIsInputAddedByButton(false);
		}

		tfFunctionInput.setText(model.getFunctionInput());

		btnInsert.setEnabled(!model.getFunctionInput().isBlank());
		btnDelete.setEnabled(list.getSelectedIndex() != -1);
	}
}