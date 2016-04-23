/*
 * Going to fire up the GUI, but give myself test access methods that will 
 * simulate events.  Maybe run a currentTimeMillis and make sure it is tight
 * enough that it has to be local and not network traffic for authentication
 * purposes.
 * 
 * Maybe I should add a couple speed tests to make sure the response is quick
 * enough... (Probably to server side). Does performance tests fit in JUnit?
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import Client.ChatClient;

/**
 *
 * @author Michael
 */
public class ClientTests implements Runnable {
    ChatClient client;
    public ClientTests() {
    }

    @BeforeClass
    public static void setUpClass() {
       (new Thread(new ClientTests())).start();
       ChatClient.JUnitAuthorize(System.currentTimeMillis(), "6245ChatTester");
    }
    
    @Override
    public void run() {
        try {
            
            client = new ChatClient();
            client.startUp();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void TestClassAuthorized() {
        assert (ChatClient.JUnitIsAuthorized());
    }

    @Test
    public void TestSubmitNameIncrementsCount() {
        int origSubmitCount = client.JUnitTestingGetSubmitCount();
        client.JUnitTestingSendProcessInput("SUBMITNAME");
        int secondSubmitCount = client.JUnitTestingGetSubmitCount();
        assert (origSubmitCount < secondSubmitCount);
    }
   
}
