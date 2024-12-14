import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MemoryManagementGUI extends JFrame {
    // Memory Block Class
    static class MemoryBlock {
        int size;
        String id;
        boolean isAllocated;

        MemoryBlock(int size, String id) {
            this.size = size;
            this.id = id;
            this.isAllocated = false;
        }
    }

    // Main data structure for memory blocks
    private final ArrayList<MemoryBlock> memoryBlocks = new ArrayList<>();
    private final DefaultListModel<String> memoryStatusListModel = new DefaultListModel<>();
    
    // Log area for user messages
    private final JTextArea logArea = new JTextArea(10, 30);

    // Constructor for GUI
    public MemoryManagementGUI() {
        setTitle("First Fit Memory Management");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#f0f8ff")); // Background Color: Alice Blue

        // Panels for sections
        JPanel addBlockPanel = new JPanel();
        JPanel allocatePanel = new JPanel();
        JPanel freePanel = new JPanel();
        JPanel memoryStatusPanel = new JPanel();
        JPanel logPanel = new JPanel();

        // Left side layout for add, allocate, and free panels
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 1, 10, 10));  // Grid with spacing
        leftPanel.add(addBlockPanel);
        leftPanel.add(allocatePanel);
        leftPanel.add(freePanel);

        // Add Block Section
        addBlockPanel.setBorder(BorderFactory.createTitledBorder("Add Memory Block"));
        addBlockPanel.setLayout(new GridLayout(3, 2, 10, 10));
        addBlockPanel.setBackground(Color.decode("#e6f7ff"));  // Light Blue

        JTextField blockSizeField = new JTextField();
        JTextField blockIDField = new JTextField();
        JButton addBlockButton = new JButton("Add Block");
        addBlockButton.setBackground(Color.decode("#00cc66"));
        addBlockButton.setForeground(Color.WHITE);
        addBlockButton.setFont(new Font("Arial", Font.BOLD, 14));
        addBlockPanel.add(new JLabel("Block Size:"));
        addBlockPanel.add(blockSizeField);
        addBlockPanel.add(new JLabel("Block ID:"));
        addBlockPanel.add(blockIDField);
        addBlockPanel.add(addBlockButton);

        // Allocate Memory Section
        allocatePanel.setBorder(BorderFactory.createTitledBorder("Allocate Memory"));
        allocatePanel.setLayout(new GridLayout(3, 2, 10, 10));
        allocatePanel.setBackground(Color.decode("#f2d9ff"));  // Lavender

        JTextField processSizeField = new JTextField();
        JTextField processIDField = new JTextField();
        JButton allocateButton = new JButton("Allocate Memory");
        allocateButton.setBackground(Color.decode("#ff6600"));
        allocateButton.setForeground(Color.WHITE);
        allocateButton.setFont(new Font("Arial", Font.BOLD, 14));
        allocatePanel.add(new JLabel("Process Size:"));
        allocatePanel.add(processSizeField);
        allocatePanel.add(new JLabel("Process ID:"));
        allocatePanel.add(processIDField);
        allocatePanel.add(allocateButton);

        // Free Memory Block Section
        freePanel.setBorder(BorderFactory.createTitledBorder("Free Memory Block"));
        freePanel.setLayout(new GridLayout(3, 2, 10, 10));
        freePanel.setBackground(Color.decode("#fff2cc"));  // Light Yellow

        JTextField freeBlockSizeField = new JTextField();
        JTextField freeBlockIDField = new JTextField();
        JButton freeBlockButton = new JButton("Free Block");
        freeBlockButton.setBackground(Color.decode("#ff3333"));
        freeBlockButton.setForeground(Color.WHITE);
        freeBlockButton.setFont(new Font("Arial", Font.BOLD, 14));
        freePanel.add(new JLabel("Block Size:"));
        freePanel.add(freeBlockSizeField);
        freePanel.add(new JLabel("Block ID:"));
        freePanel.add(freeBlockIDField);
        freePanel.add(freeBlockButton);

        // Memory Status Section
        memoryStatusPanel.setBorder(BorderFactory.createTitledBorder("Memory Status"));
        memoryStatusPanel.setLayout(new BorderLayout());
        memoryStatusPanel.setBackground(Color.decode("#d1c4e9"));  // Light Purple

        JList<String> memoryStatusList = new JList<>(memoryStatusListModel);
        memoryStatusList.setBackground(Color.decode("#e1bee7"));
        memoryStatusList.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton refreshButton = new JButton("Refresh Memory Status");
        refreshButton.setBackground(Color.decode("#4caf50"));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        memoryStatusPanel.add(new JScrollPane(memoryStatusList), BorderLayout.CENTER);
        memoryStatusPanel.add(refreshButton, BorderLayout.SOUTH);

        // Log Area for advanced user output
        logPanel.setBorder(BorderFactory.createTitledBorder("Action Log"));
        logPanel.setLayout(new BorderLayout());
        logArea.setEditable(false);
        logArea.setBackground(Color.decode("#2c3e50"));
        logArea.setForeground(Color.decode("#ecf0f1"));
        logArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane logScroll = new JScrollPane(logArea);
        logPanel.add(logScroll, BorderLayout.CENTER);

        // Add panels to main frame
        add(leftPanel, BorderLayout.WEST);
        add(memoryStatusPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.SOUTH);

        // Add Block Button Action
        addBlockButton.addActionListener(e -> {
            try {
                int size = Integer.parseInt(blockSizeField.getText());
                String id = blockIDField.getText();
                memoryBlocks.add(new MemoryBlock(size, id));
                blockSizeField.setText("");
                blockIDField.setText("");
                updateMemoryStatus();
                appendLog("Memory Block " + id + " (" + size + " KB) added successfully.", Color.GREEN);
            } catch (NumberFormatException ex) {
                appendLog("Error: Invalid block size input.", Color.RED);
            }
        });

        // Allocate Memory Button Action
        allocateButton.addActionListener(e -> {
            try {
                int processSize = Integer.parseInt(processSizeField.getText());
                String processID = processIDField.getText();
                boolean allocated = false;
                for (MemoryBlock block : memoryBlocks) {
                    if (!block.isAllocated && block.size >= processSize) {
                        block.isAllocated = true;
                        block.id = processID;
                        allocated = true;
                        break;
                    }
                }
                if (allocated) {
                    appendLog("Memory allocated successfully to process " + processID + " (" + processSize + " KB).", Color.GREEN);
                } else {
                    appendLog("Error: No suitable memory block found for process " + processID + " (" + processSize + " KB).", Color.RED);
                }
                processSizeField.setText("");
                processIDField.setText("");
                updateMemoryStatus();
            } catch (NumberFormatException ex) {
                appendLog("Error: Invalid process size input.", Color.RED);
            }
        });

        // Free Block Button Action
        freeBlockButton.addActionListener(e -> {
            String blockID = freeBlockIDField.getText();
            boolean freed = false;
            for (MemoryBlock block : memoryBlocks) {
                if (block.id.equals(blockID) && block.isAllocated) {
                    block.isAllocated = false;
                    block.id = "Free";
                    appendLog("Memory Block " + blockID + " freed successfully.", Color.WHITE);
                    freed = true;
                    break;
                }
            }
            if (!freed) {
                appendLog("Error: Block " + blockID + " not found or not allocated.", Color.RED);
            }
            freeBlockIDField.setText("");
            updateMemoryStatus();
        });

        // Refresh Button Action
        refreshButton.addActionListener(e -> updateMemoryStatus());
    }

    // Update Memory Status
    private void updateMemoryStatus() {
        memoryStatusListModel.clear();
        for (MemoryBlock block : memoryBlocks) {
            String status = block.isAllocated ? "Allocated to " + block.id : "Free";
            memoryStatusListModel.addElement("Block (" + block.size + " KB): " + status);
        }
    }

    // Append logs to the log area
    private void appendLog(String message, Color color) {
        logArea.setForeground(color);
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());  // Scroll to the bottom
    }

    // Main Method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MemoryManagementGUI gui = new MemoryManagementGUI();
            gui.setVisible(true);
        });
    }
}