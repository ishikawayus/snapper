package com.example.snapper;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import com.example.snapper.constant.KeyCode;
import com.example.snapper.jna.JnaUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SnapperApplication {

    public static void main(String[] args) throws Exception {
        new SnapperApplication().run();
    }

    private final Setting setting = new Setting();

    private void run() throws AWTException, InterruptedException, IOException {
        setLookAndFeel();
        setDefaultFont();

        JFrame frame = new JFrame("Snapper");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new GridLayout(3, 2));

        frame.add(new JLabel("Key to Snap"));
        frame.add(createKeyCodeComboBox());

        frame.add(new JLabel("Directory to Save"));
        frame.add(createDirectoryTextField());

        frame.add(new JLabel("File Name to Save"));
        frame.add(createFileNameTextField());

        frame.setVisible(true);

        mainLoop();
    }

    private void setLookAndFeel() {
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            log.warn("Failed to set look and feel: lookAndFeel={}", lookAndFeel, e);
        }
    }

    private void setDefaultFont() {
        FontUIResource font = new FontUIResource("Meiryo", Font.PLAIN, 12);
        for (Object key : UIManager.getLookAndFeelDefaults().keySet()) {
            if (key.toString().endsWith(".font")) {
                UIManager.put(key, font);
            }
        }
    }

    private JComboBox<String> createKeyCodeComboBox() {
        String[] items = Arrays.stream(KeyCode.values()).map(KeyCode::toString).toArray(String[]::new);
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setSelectedItem(setting.getKeyCode());
        comboBox.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                setting.setKeyCode(comboBox.getItemAt(comboBox.getSelectedIndex()));
            }
        });
        return comboBox;
    }

    private JTextField createDirectoryTextField() {
        JTextField textField = new JTextField(setting.getDirectory());
        textField.addActionListener(event -> {
            setting.setDirectory(textField.getText());
        });
        return textField;
    }

    private JTextField createFileNameTextField() {
        JTextField textField = new JTextField(setting.getFileName());
        textField.addActionListener(event -> {
            setting.setFileName(textField.getText());
        });
        return textField;
    }

    private void mainLoop() throws AWTException, InterruptedException, IOException {
        boolean keyState = false;
        Robot robot = new Robot();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd_HHmmss_SSS");
        while (true) {
            boolean newKeyState = JnaUtils.getAsyncKeyState(KeyCode.valueOf(setting.getKeyCode()));
            if (newKeyState && !keyState) {
                int hwnd = JnaUtils.getForegroundWindow();
                Rectangle rectangle = JnaUtils.getWindowRect(hwnd);
                BufferedImage image = robot.createScreenCapture(rectangle);
                String fileName = setting.getFileName() + "." + LocalDateTime.now().format(formatter) + ".png";
                Path path = Paths.get(setting.getDirectory()).resolve(fileName);
                ImageIO.write(image, "PNG", path.toFile());
            }
            keyState = newKeyState;
            Thread.sleep(100);
        }
    }

    @Data
    private static class Setting {

        private String keyCode = KeyCode.VK_NONCONVERT.toString();
        private String directory = System.getenv("USERPROFILE") + "\\Pictures";
        private String fileName = "snapper";
    }
}
