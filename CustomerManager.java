package labs.lab9;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerManager extends JFrame {
    private DefaultListModel<Customer> customerListModel;
    private JList<Customer> customerList;
    private JTextField nameField, emailField, amountSpentField;
    private JCheckBox dogCheckBox, catCheckBox, birdCheckBox, fishCheckBox, otherCheckBox;
    private JComboBox<String> locationComboBox;
    private JTextArea notesTextArea;

    public CustomerManager() {
        setTitle("Harshvardhan Sharaff - 75508974");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel(new BorderLayout());
        customerListModel = new DefaultListModel<>();
        customerList = new JList<>(customerListModel);
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(customerList);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);


        JPanel rightPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rightPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        rightPanel.add(nameField);

        rightPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        rightPanel.add(emailField);

        rightPanel.add(new JLabel("Pets:"));
        JPanel petsPanel = new JPanel(new GridLayout(1, 5));
        dogCheckBox = new JCheckBox("Dog(s)");
        catCheckBox = new JCheckBox("Cat(s)");
        birdCheckBox = new JCheckBox("Bird(s)");
        fishCheckBox = new JCheckBox("Fish");
        otherCheckBox = new JCheckBox("Other");
        petsPanel.add(dogCheckBox);
        petsPanel.add(catCheckBox);
        petsPanel.add(birdCheckBox);
        petsPanel.add(fishCheckBox);
        petsPanel.add(otherCheckBox);
        rightPanel.add(petsPanel);

        rightPanel.add(new JLabel("Total Amount Spent:"));
        amountSpentField = new JTextField("0.0");
        rightPanel.add(amountSpentField);

        rightPanel.add(new JLabel("Home Store Location:"));
        String[] locations = {"Irvine", "Los Angeles", "Paris", "Shanghai", "New York", "London"};
        locationComboBox = new JComboBox<>(locations);
        rightPanel.add(locationComboBox);

        rightPanel.add(new JLabel("Notes:"));
        notesTextArea = new JTextArea();
        JScrollPane notesScrollPane = new JScrollPane(notesTextArea);
        rightPanel.add(notesScrollPane);


        add(leftPanel);
        add(rightPanel);


        JButton newCustomerButton = new JButton("New Customer");
        JButton saveCustomerButton = new JButton("Save Customer");
        JButton deleteButton = new JButton("Delete");
        newCustomerButton.setPreferredSize(new Dimension(100, 30));
        saveCustomerButton.setPreferredSize(new Dimension(100, 30));
        deleteButton.setPreferredSize(new Dimension(100, 30));


        newCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        saveCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCustomer();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });


        rightPanel.add(newCustomerButton);
        rightPanel.add(saveCustomerButton);
        leftPanel.add(deleteButton, BorderLayout.SOUTH);

        updateCustomerList();


        customerList.addListSelectionListener(e -> displayCustomerDetails());


        setSize(1400, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        dogCheckBox.setSelected(false);
        catCheckBox.setSelected(false);
        birdCheckBox.setSelected(false);
        fishCheckBox.setSelected(false);
        otherCheckBox.setSelected(false);
        amountSpentField.setText("0.0");
        locationComboBox.setSelectedItem("Irvine");
        notesTextArea.setText("");
        customerList.clearSelection();
    }

    private void saveCustomer() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();


        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
            return;
        }

        boolean dog = dogCheckBox.isSelected();
        boolean cat = catCheckBox.isSelected();
        boolean bird = birdCheckBox.isSelected();
        boolean fish = fishCheckBox.isSelected();
        boolean other = otherCheckBox.isSelected();
        double amountSpent = 0.0;

        try {
            amountSpent = Double.parseDouble(amountSpentField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
            return;
        }

        String location = (String) locationComboBox.getSelectedItem();
        String notes = notesTextArea.getText().trim();

        Customer customer = new Customer(name, email, dog, cat, bird, fish, other, amountSpent, location, notes);

        int selectedIndex = customerList.getSelectedIndex();
        if (selectedIndex != -1) {

            Customer existingCustomer = customerListModel.getElementAt(selectedIndex);


            if (!name.equals(existingCustomer.getName()) && customerListModel.contains(customer)) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
                return;
            }
            customerListModel.removeElement(existingCustomer);
        } else {

            if (customerListModel.contains(customer)) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
                return;
            }
        }


        customerListModel.addElement(customer);
        updateCustomerList();
        JOptionPane.showMessageDialog(this, "Customer saved!");
        clearFields();
    }

    private void deleteCustomer() {
        int selectedIndex = customerList.getSelectedIndex();
        if (selectedIndex != -1) {
            customerListModel.remove(selectedIndex);
            updateCustomerList();
            JOptionPane.showMessageDialog(this, "Customer deleted!");
        }
    }



    private void updateCustomerList() {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < customerListModel.size(); i++) {
            customers.add(customerListModel.getElementAt(i));
        }
        Collections.sort(customers);
        customerListModel.clear();
        for (Customer customer : customers) {
            customerListModel.addElement(customer);
        }
    }

    private void displayCustomerDetails() {
        Customer selectedCustomer = customerList.getSelectedValue();
        if (selectedCustomer != null) {
            nameField.setText(selectedCustomer.getName());
            emailField.setText(selectedCustomer.getEmail());
            dogCheckBox.setSelected(selectedCustomer.hasDog());
            catCheckBox.setSelected(selectedCustomer.hasCat());
            birdCheckBox.setSelected(selectedCustomer.hasBird());
            fishCheckBox.setSelected(selectedCustomer.hasFish());
            otherCheckBox.setSelected(selectedCustomer.hasOther());
            amountSpentField.setText(String.valueOf(selectedCustomer.getAmountSpent()));
            locationComboBox.setSelectedItem(selectedCustomer.getLocation());
            notesTextArea.setText(selectedCustomer.getNotes());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerManager());
    }
}

class Customer implements Comparable<Customer> {
    private String name;
    private String email;
    private boolean dog;
    private boolean cat;
    private boolean bird;
    private boolean fish;
    private boolean other;
    private double amountSpent;
    private String location;
    private String notes;

    public Customer(String name, String email, boolean dog, boolean cat, boolean bird,
                    boolean fish, boolean other, double amountSpent, String location, String notes) {
        this.name = name;
        this.email = email;
        this.dog = dog;
        this.cat = cat;
        this.bird = bird;
        this.fish = fish;
        this.other = other;
        this.amountSpent = amountSpent;
        this.location = location;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean hasDog() {
        return dog;
    }

    public boolean hasCat() {
        return cat;
    }

    public boolean hasBird() {
        return bird;
    }

    public boolean hasFish() {
        return fish;
    }

    public boolean hasOther() {
        return other;
    }

    public double getAmountSpent() {
        return amountSpent;
    }

    public String getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return name.equals(customer.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Customer o) {
        return this.name.compareTo(o.name);
    }
}
