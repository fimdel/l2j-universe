/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2012.
 */

package l2p.gameserver.geodata.geoeditorcon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 04.11.12
 * Time: 14:48
 */
public class GeoEditorListener extends Thread {
    private static GeoEditorListener _instance;
    private static final int PORT = 9011;
    private static Logger _log = Logger.getLogger(GeoEditorListener.class.getName());
    private final ServerSocket _serverSocket;
    private GeoEditorThread _geoEditor;

    public static GeoEditorListener getInstance() {
        synchronized (GeoEditorListener.class) {
            if (_instance == null) {
                try {
                    _instance = new GeoEditorListener();
                    _instance.start();
                    _log.info("GeoEditorListener Initialized.");
                } catch (IOException e) {
                    _log.log(Level.SEVERE, "Error creating geoeditor listener! " + e.getMessage(), e);
                    System.exit(1);
                }
            }
        }
        return _instance;
    }

    private GeoEditorListener() throws IOException {
        _serverSocket = new ServerSocket(PORT);
    }

    public GeoEditorThread getThread() {
        return _geoEditor;
    }

    public String getStatus() {
        if ((_geoEditor != null) && _geoEditor.isWorking()) {
            return "Geoeditor connected.";
        }
        return "Geoeditor not connected.";
    }

    @Override
    public void run() {
        try {
            Socket connection = _serverSocket.accept();
            while (!isInterrupted()) {
                if ((_geoEditor != null) && _geoEditor.isWorking()) {
                    _log.warning("Geoeditor already connected!");
                    connection.close();
                    continue;
                }
                _log.info("Received geoeditor connection from: " + connection.getInetAddress().getHostAddress());
                _geoEditor = new GeoEditorThread(connection);
                _geoEditor.start();
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "GeoEditorListener: " + e.getMessage());
        } finally {
            try {
                _serverSocket.close();
            } catch (Exception e) {
                _log.log(Level.WARNING, "GeoEditorListener: " + e.getMessage(), e);
            }
            _log.warning("GeoEditorListener Closed!");
        }
    }
}

