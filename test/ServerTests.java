
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * I'm not 100% sure what the requirements for the JUnit tests are,
 * but they have descriptive names, and I think it should be obvious
 * what I'm trying to cover. 
 * 
 * I ran into problems with my simple design when it comes to testing.
 * I didn't break my items into multiple distinct objects so they are 
 * harder to test individually, but hey are more cohesive.  I believe
 * I have still covered the basic functionality.  
 * 
 * When I tried to start two sockets to the same port, it overwrote the
 * first one not allowing me to test multiple connections without going
 * to multiple clients to do it which seemed counter productive.  So there
 * are a few gaps remaining.
 * 
 * @author Michael Claar
 */
public class ServerTests {

    public ServerTests() {
    }

    @Test
    public void connectionSendsSubmitNamePhrase() throws IOException {
        Socket socket = new Socket("192.168.1.223", 9876);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        String line = in.readLine();
        socket.close();
        assertTrue(line.equals("SUBMITNAME"));
    }

    @Test
    public void ServerAcceptsName() throws IOException {
        Socket socket = new Socket("192.168.1.223", 9876);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String line = "";
        line = in.readLine();
        out.println("BobTheTomato");
        line = in.readLine();
        socket.close();
        assert (line.equals("NAMEACCEPTED"));
    }

    @Test
    public void ServerRejectsBlankName() throws IOException {
        Socket socket = new Socket("192.168.1.223", 9876);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String line = "";
        line = in.readLine();
        out.println("");
        line = in.readLine();
        socket.close();
        assert (line.equals("SUBMITNAME"));  //Not name accepted
    }

    @Test
    public void ReturnsPeopleTyping() throws IOException {
        Socket socket = new Socket("192.168.1.223", 9876);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String line = "";

        line = in.readLine();
        out.println("LarryTheCumcumber");
        while (true) {
            line = in.readLine();
            System.out.println("Line: " + line);
            if (line.contains("TYPING_")) {
                break;
            }
            out.println("TYPING_Any random message");
        }
        socket.close();
        assert (true);
    }

    @Test
    public void ReturnsPeopleMessageCorrectly() throws IOException {
        Socket socket = new Socket("192.168.1.223", 9876);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String line = "";

        line = in.readLine();
        out.println("LarryTheCumcumber");
        out.println("MESSAGE_Any random message");
        while (true) {
            line = in.readLine();
            System.out.println("Message Line: " + line);
            if (line.contains("MESSAGE LarryTheCumcumber:")) {
                break;
            }
        }
        socket.close();
        assert (line.equals("MESSAGE LarryTheCumcumber: Any random message"));
    }
}
