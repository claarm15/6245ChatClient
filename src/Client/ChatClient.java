package Client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient {

    int typingCount;
    BufferedReader in;
    PrintWriter out;
    JFrame frame;
    JTextField typingArea;
    JTextArea messageArea;
    JTextArea whoIsTypingArea;
    JTextField nameArea;
    JButton submit;
    JLabel topLeft;
    boolean keepRunning;
    String myName;
    long lastTypedCharacter;
    static boolean JUnitAuth;
    int submitCount;
    
    public ChatClient() {
        JUnitAuth = false;
        lastTypedCharacter = 0;
        keepRunning = true;
        frame = new JFrame("6245 Chat Client");
        typingArea = new JTextField(40);
        nameArea = new JTextField(15);
        messageArea = new JTextArea(8, 40);
        whoIsTypingArea = new JTextArea(1, 38);
        JPanel topBox = new JPanel(new BorderLayout());
        JPanel topRow = new JPanel();
        topRow.setLayout(new GridLayout(1, 3));
        submit = new JButton("Connect");
        topLeft = new JLabel("Enter your name: ");
        topRow.add(topLeft);
        topRow.add(nameArea);
        topRow.add(submit);

        topBox.add(topRow, "North");
        topBox.add(typingArea, "South");
        typingArea.setEditable(false);
        messageArea.setEditable(false);
        whoIsTypingArea.setEditable(false);

        frame.getContentPane().add(topBox, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.getContentPane().add(new JScrollPane(whoIsTypingArea), "South");
        frame.pack();
        typingArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                long rightNow = System.currentTimeMillis();
                if (rightNow > lastTypedCharacter + 2000) {
                    out.println("TYPING_" + nameArea.getText());
                    lastTypedCharacter = rightNow;
                }
            }
        });
        typingArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("MESSAGE_" + typingArea.getText());
                typingArea.setText("");
            }
        });
        submit.addActionListener((ActionEvent e) -> {
            out.println(nameArea.getText());
        });
    }

    public void getName(int whichTime) {
        if (whichTime < 1) {
            messageArea.append("Choose a screen name: \n");
        } else {
            messageArea.append("Name taken, Please choose a different screen name:\n");
        }
    }

    public void run() throws IOException {

        Socket socket = new Socket("192.168.1.223", 9876);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        submitCount = 0;
        while (true) {
            if (keepRunning) {
                String line = in.readLine();
                proccessInput(line);
            } else {
                break;
            }
        }
    }
    private void proccessInput(String line) {
        
                if (line.startsWith("SUBMITNAME") && submitCount == 0) {
                    getName(submitCount);
                    submitCount++;
                } else if (line.startsWith("SUBMITNAME")) {
                    getName(submitCount);
                    submitCount++;
                } else if (line.startsWith("NAMEACCEPTED")) {
                    myName = nameArea.getText();
                    typingArea.setEditable(true);
                    nameArea.setEditable(false);
                    submit.setEnabled(false);
                    topLeft.setText("6245 Chat Client");
                } else if (line.startsWith("MESSAGE")) {
                    messageArea.append(line.substring(8) + "\n");
                } else if (line.startsWith("TYPING_")) {
                    String names = line.substring(7);
                    if (names.contains(myName)) {
                        names = names.replace(myName + " ", "");
                    }
                    whoIsTypingArea.setText("Currently Typing: " + names + "\n");
                }
    }
    /**
     * This is meant to be as the only access to the class. The methods below
     * the main are public, but are protected as they are only meant for JUnit
     * testing. I have decided to try to eliminate people getting to the tests
     * by an unconventional means of comparing time stamps and a password. The
     * idea of the time stamp is that there isn't time for network traffic so it
     * can only be run locally.
     *
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }

    /**
     * Not for public use... For JUnit access only. Must have both number pass
     * code and string password to authenticate.
     *
     * @param numberCode
     * @param password
     */
    public static void JUnitAuthorize(long numberCode, String password) {
        if (password.equals("6245ChatTester")) {
            if (Math.abs(numberCode - System.currentTimeMillis()) < 5) {
                JUnitAuth = true;
                System.out.println("Authenticated");
            }
        }
    }

    public static boolean JUnitIsAuthorized() {
        return JUnitAuth;
    }

    public static boolean JUnitConnectionVariablesSet() {
        boolean result = false;
        if (JUnitAuth) {
//            if (socket != null && out != null and writer != null) {
//            result = true;
//        }
        }
        return result;
    }

    public void startUp() throws IOException {
        if (JUnitAuth) {
            this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.frame.setVisible(true);
            this.run();
        }
    }
    
    public String testJunitProcessInput() {
        String result = "";
        if (JUnitAuth) {
            
        }
        return result;
    }
    
    public void JUnitTestingSetNameBox (String name) {
        if (JUnitAuth) {
            nameArea.setText(name);
        }
    }
    
    public String JUnitTestingSubmitNameIncrementsCount () {
        String result = "";
        if (JUnitAuth) {
            result = nameArea.getText();
        }
        return result;
    }
    public int JUnitTestingGetSubmitCount () {
        int result = -1;
        if (JUnitAuth) {
            result = submitCount;
        }
        return result;
    }
    public void JUnitTestingSendProcessInput(String input) {
        if (JUnitAuth) {
            proccessInput(input);
        }
    }
}
