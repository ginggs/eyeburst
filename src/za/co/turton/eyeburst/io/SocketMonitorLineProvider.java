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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.config.InjectionConstructor;

/**
 * Line provider connected to a UTD through a TCP/IP socket
 * @author james
 */
public class SocketMonitorLineProvider extends MonitorLineProvider {
    
    private Socket socket;
    
    private InputStream in;
    private Reader reader;
    private LineNumberReader lineReader;
    
    private OutputStream out;
    private Writer writer;
    
    private boolean connected;
        
    private int readTimeout;
    
    private InetSocketAddress utdIPAddress;
    
    private int connectTimeout;
    
    private String utdDebugOn;
    
    private String utdDebugOff;
    
    private String utdCurrentTower;
    
    
    /** Creates a new instance of SocketDebugLineProvider */
    public @InjectionConstructor SocketMonitorLineProvider (
            @Inject("logger") Logger logger,
            @Inject("readTimeout") int readTimeout,
            @Inject("utdIPAddress") InetSocketAddress utdIPAddress,
            @Inject("connectTimeout") int connectTimeout,
            @Inject("utdDebugOn") String utdDebugOn,
            @Inject("utdDebugOff") String utdDebugOff,
            @Inject("utdCurrentTower") String utdCurrentTower) {
        
        super(logger);
        this.readTimeout = readTimeout;
        this.utdIPAddress = utdIPAddress;
        this.utdCurrentTower = utdCurrentTower;
        this.utdDebugOff = utdDebugOff;
        this.utdDebugOn = utdDebugOn;
        this.connectTimeout = connectTimeout;
        
        socket = new Socket();
        connected = false;
        
        try {
            socket.setSoTimeout(readTimeout);
        } catch (SocketException e) {
            logger.log(Level.WARNING, "Could not set read timeout on socket: ", e);
        }
    }
    
    /**
     *
     * @see MonitorLineProvider#connect()
     */
    public void connect() throws IOException {
        socket.connect(utdIPAddress, connectTimeout);
        in = socket.getInputStream();
        reader = new InputStreamReader(in);
        lineReader = new LineNumberReader(reader);
        
        out = socket.getOutputStream();
        writer = new OutputStreamWriter(out);
        writeLine(utdDebugOn);
        
        connected = true;
        logger.info("Connection open");
    }
    
    /**
     *
     * @see MonitorLineProvider#disconnect()
     */
    public void disconnect() throws IOException {
        
        writeLine(utdDebugOff);
        
//        if (lineReader != null)
//            lineReader.close();
//        
//        if (reader != null)
//            reader.close();
//        
//        if (in != null)
//            in.close();
//        
//        if (writer != null)
//            writer.close();
//        
//        if (out != null)
//            out.close();
        
        if (socket != null && !socket.isClosed())
            socket.close();
        
        connected = false;
        logger.info("Connection closed");
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
        logger.fine("Read: "+ line);
        return line;
    }
    
    /**
     *
     * @see MonitorLineProvider#writeLine(String)
     */
    public void writeLine(String line) throws IOException {
        out.write((line+"\n").getBytes());
        logger.fine("Wrote: "+line);
    }
    
    /**
     *
     * @see MonitorLineProvider#requestCurrentTower()
     */
    public void requestCurrentTower() throws IOException {
        writeLine(utdCurrentTower);
    }
}
