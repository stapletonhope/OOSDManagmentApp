

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class managementApplication extends JFrame implements ActionListener {
	JMenu userMenu, reviewMenu, orderMenu;
	JMenuItem signItem, loginItem, logoutItem, quitItem, addReviewItem, viewReviewItem, placeOrderItem;
	JMenuBar menuBar;
	JLabel welcomeLabel;
	JDialog reviewDialog;
	JTextArea txtArea1, txtArea2;
	JSlider slider;
	JButton btn;

	boolean isLoggedIn = false;
	ArrayList<User> users = new ArrayList<>();
	ArrayList<Review> reviews = new ArrayList<>();

	public managementApplication() {
		super("Management Application");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(400, 400);

		welcomeLabel = new JLabel("", SwingConstants.CENTER);
		welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		this.add(welcomeLabel, BorderLayout.NORTH);

		// Initialize menus
		userMenu = new JMenu("User");
		reviewMenu = new JMenu("Review");
		orderMenu = new JMenu("Order");

		signItem = new JMenuItem("Sign Up");
		signItem.setMnemonic(KeyEvent.VK_U);
		signItem.addActionListener(this);

		loginItem = new JMenuItem("Log In");
		loginItem.setMnemonic(KeyEvent.VK_L);
		loginItem.addActionListener(this);

		logoutItem = new JMenuItem("Log Out");
		logoutItem.setMnemonic(KeyEvent.VK_O);
		logoutItem.addActionListener(this);

		addReviewItem = new JMenuItem("Add Review");
		addReviewItem.addActionListener(this);

		viewReviewItem = new JMenuItem("View Review");
		viewReviewItem.addActionListener(this);

		placeOrderItem = new JMenuItem("Place Order");
		placeOrderItem.addActionListener(this);

		quitItem = new JMenuItem("Quit");
		quitItem.setMnemonic(KeyEvent.VK_Q);
		quitItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
		quitItem.addActionListener(e -> System.exit(0));

		userMenu.add(signItem);
		userMenu.add(loginItem);
		userMenu.add(logoutItem);
		userMenu.add(quitItem);

		reviewMenu.add(addReviewItem);
		reviewMenu.add(viewReviewItem);
		reviewMenu.setEnabled(false);

		orderMenu.add(placeOrderItem);
		orderMenu.setEnabled(false);

		menuBar = new JMenuBar();
		menuBar.add(userMenu);
		menuBar.add(reviewMenu);
		menuBar.add(orderMenu);
		setJMenuBar(menuBar);
	}

	public static void main(String[] args) {
		managementApplication gui = new managementApplication();
		gui.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("Sign Up")) {
			JTextField userN = new JTextField();
			JPasswordField passField = new JPasswordField();
			String[] customerTypes = { "Individual", "Corporate", "Nonprofit Organisation" };
			JComboBox<String> customerTypeBox = new JComboBox<>(customerTypes);

			JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
			panel.add(new JLabel("Username:"));
			panel.add(userN);
			panel.add(new JLabel("Password:"));
			panel.add(passField);
			panel.add(new JLabel("Customer Type:"));
			panel.add(customerTypeBox);

			int result = JOptionPane.showConfirmDialog(this, panel, "Registration", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				String username = userN.getText();
				String password = new String(passField.getPassword());
				String customerType = (String) customerTypeBox.getSelectedItem();

				if (username.isBlank() || password.isBlank()) {
					JOptionPane.showMessageDialog(this, "Invalid input. Fields cannot be empty or blank.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				for (User user : users) {
					if (user.getUsername().equalsIgnoreCase(username)) {
						JOptionPane.showMessageDialog(this, "Username already exists.", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				users.add(new User(username, password, customerType));
				JOptionPane.showMessageDialog(this, "Registration successful!");
			}
		} else if (command.equals("Log In")) {
			JTextField userF = new JTextField();
			JPasswordField passField = new JPasswordField();

			JPanel panel = new JPanel(new GridLayout(0, 1));
			panel.add(new JLabel("Username:"));
			panel.add(userF);
			panel.add(new JLabel("Password:"));
			panel.add(passField);

			int result = JOptionPane.showConfirmDialog(this, panel, "Log In", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				String username = userF.getText();
				String password = new String(passField.getPassword());

				for (User user : users) {
					if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
						isLoggedIn = true;
						welcomeLabel.setText("Welcome, " + username);
						reviewMenu.setEnabled(true);
						orderMenu.setEnabled(true);
						JOptionPane.showMessageDialog(this, "Login successful!");
						return;
					}
				}

				JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (command.equals("Add Review")) {
			reviewDialog = new JDialog(this, "Add Review", true);
			reviewDialog.setSize(400, 300);

			JPanel pnl = new JPanel(new GridLayout(0, 2, 10, 10));
			JTextField dateField = new JTextField();
			JTextArea commentArea = new JTextArea(5, 20);
			JSlider ratingSlider = new JSlider(1, 5, 3);

			pnl.add(new JLabel("Date:"));
			pnl.add(dateField);
			pnl.add(new JLabel("Comment:"));
			pnl.add(new JScrollPane(commentArea));
			pnl.add(new JLabel("Rating:"));
			pnl.add(ratingSlider);

			JButton submitBtn = new JButton("Submit");
			submitBtn.addActionListener(evt -> {
				String date = dateField.getText();
				String comment = commentArea.getText();
				int rating = ratingSlider.getValue();

				reviews.add(new Review(date, comment, rating));
				JOptionPane.showMessageDialog(this, "Review added successfully!");
				reviewDialog.dispose();
			});

			reviewDialog.add(pnl, BorderLayout.CENTER);
			reviewDialog.add(submitBtn, BorderLayout.SOUTH);
			reviewDialog.setVisible(true);
		} else if (command.equals("View Review")) {
			StringBuilder reviewText = new StringBuilder();
			for (Review review : reviews) {
				reviewText.append(review.toString()).append("\n");
			}

			JOptionPane.showMessageDialog(this,
					reviewText.length() > 0 ? reviewText.toString() : "No reviews available.", "Reviews",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (command.equals("Log Out")) {
			if (isLoggedIn) {
				isLoggedIn = false;
				welcomeLabel.setText("");
				reviewMenu.setEnabled(false);
				orderMenu.setEnabled(false);
				JOptionPane.showMessageDialog(this, "Logged out successfully!");
			}
		}
		else if (command.equals("Place Order")) {
				// New "Place Order" functionality
				JDialog orderDialog = new JDialog(this, "Place Order", true);
				orderDialog.setSize(400, 300);

				String[] productList = { "Plants", "Picture Frame", "Decorative Clock", "Wall Art", "Scented Candles",
						"Ceramic Pots" };
				JComboBox<String> productCombo = new JComboBox<>(productList);
				JTextField quantityField = new JTextField();
				JLabel discountLabel = new JLabel("Discount: ");
				JLabel totalLabel = new JLabel("Total: €0.00");

				// Calculate Discount Based on Customer Type
				JButton calculateBtn = new JButton("Calculate Total");
				calculateBtn.addActionListener(evt -> {
					try {
						int quantity = Integer.parseInt(quantityField.getText());
						if (quantity <= 0)
							throw new NumberFormatException();

						double pricePerItem = 15.0;
						double total = pricePerItem * quantity;
						double discountRate = 0.0;

						// Determine discount based on customer type
						for (User user : users) {
							if (user.getUsername().equalsIgnoreCase(welcomeLabel.getText().replace("Welcome, ", ""))) {
								String customerType = user.getCustomerType();
								if (customerType.equalsIgnoreCase("Corporate")) {
									discountRate = 0.03;
								} else if (customerType.equalsIgnoreCase("Nonprofit Organisation")) {
									discountRate = 0.06;
								}
								break;
							}
						}

						double discount = total * discountRate;
						double finalTotal = total - discount;

						discountLabel.setText(String.format("Discount: €%.2f", discount));
						totalLabel.setText(String.format("Total: €%.2f", finalTotal));
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(orderDialog, "Please enter a valid quantity.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				});

				// Layout for the Order Dialog
				JPanel orderPanel = new JPanel(new GridLayout(0, 2, 5, 5));
				orderPanel.add(new JLabel("Product:"));
				orderPanel.add(productCombo);
				orderPanel.add(new JLabel("Quantity:"));
				orderPanel.add(quantityField);
				orderPanel.add(discountLabel);
				orderPanel.add(totalLabel);

				JButton placeOrderBtn = new JButton("Place Order");
				placeOrderBtn.addActionListener(evt -> {
					JOptionPane.showMessageDialog(orderDialog, "Order placed successfully!");
					orderDialog.dispose();
				});

				JPanel buttonPanel = new JPanel();
				buttonPanel.add(calculateBtn);
				buttonPanel.add(placeOrderBtn);

				orderDialog.add(orderPanel, BorderLayout.CENTER);
				orderDialog.add(buttonPanel, BorderLayout.SOUTH);
				orderDialog.setVisible(true);
		}
		}
	}

