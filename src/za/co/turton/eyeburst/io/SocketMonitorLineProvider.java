/*
 * SocketDebugLineProvider.java
 *
 * Created on June 15, 2006, 10:13 AM
 *
 */

package za.co.turton.eyeburst.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import za.co.turton.eyeburst.config.Configuration;

/**
 * Line provider connected to a UTD through a TCP/IP socket
 * @author james
 */
public class SocketMonitorLineProvider implements MonitorLineProvider {
    
    private Socket socket;
    
    private InputStream in;
    private Reader reader;
    private LineNumberReader lineReader;
    
    private OutputStream out;
    private Writer writer;
    
    private boolean connected;
    
    /** Creates a new instance of SocketDebugLineProvider */
    public SocketMonitorLineProvider() {
        super();
        socket = new Socket();
        connected = false;
        
        try {
            socket.setSoTimeout(Configuration.getReadTimeout());
        } catch (SocketException e) {
            Configuration.getLogger().log(Level.WARNING, "Could not set read timeout on socket: ", e);
        }
    }
    
    /**
     *
     * @see MonitorLineProvider#connect()
     */
    public void connect() throws IOException {
        socket.connect(Configuration.getUtdIPAddress(), Configuration.getConnectTimeout());
        in = socket.getInputStream();
        reader = new InputStreamReader(in);
        lineReader = new LineNumberReader(reader);
        
        out = socket.getOutputStream();
        writer = new OutputStreamWriter(out);
        writeLine(Configuration.getUtdDebugOn());
        
        connected = true;
        Configuration.getLogger().info("Connection open");
    }
    
    /**
     *
     * @see MonitorLineProvider#disconnect()
     */
    public void disconnect() throws IOException {
        
        writeLine(Configuration.getUtdDebugOff());
        
        if (lineReader != null)
            lineReader.close();
        
        if (reader != null)
            reader.close();
        
        if (in != null)
            in.close();
        
        if (writer != null)
            writer.close();
        
        if (out != null)
            out.close();
        
        if (socket != null)
            socket.close();
        
        connected = false;
        Configuration.getLogger().info("Connection closed");
    }
    
    /**
     *
     * @see MonitorLineProvider#isConnected()
     */
    public boolean isConnected() {
        return connected;
    }
    
    /**
     *
     * @see MonitorLineProvider#readLine()
     */
    public String readLine() throws IOException {
        String line = lineReader.readLine();
        Configuration.getLogger().fine("Read: "+ line);
        return line;
    }
    
    /**
     *
     * @see MonitorLineProvider#writeLine(String)
     */
    public void writeLine(String line) throws IOException {
        out.write((line+"\n").getBytes());
        Configuration.getLogger().fine("Wrote: "+line);
    }
    
    /**
     *
     * @see MonitorLineProvider#requestCurrentTower()
     */
    public void requestCurrentTower() throws IOException {
        writeLine(Configuration.getUtdCurrentTower());
    }
}
