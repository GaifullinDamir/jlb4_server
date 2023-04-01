package Server;

import Connection.ITCPConnectionListener;
import Connection.TCPConnection;
import Services.Checker;
import Services.Calculator;

import java.io.*;
import java.net.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;

public class TCPServer implements ITCPConnectionListener {

    public static void main(String[] args) {
        new TCPServer(args[0]);
    }
    private static Integer PORT;
    private String logPath;
    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    private final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

    private TCPServer(String port) {
        installInitialValues(port);

        System.out.println("Server has been started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToConnection(tcpConnection, "Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String receivedStr) {
        System.out.println(receivedStr);
        Calculator calc = new Calculator();

        System.out.println(calc.calculate(receivedStr));
        log(receivedStr);


    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        System.out.println("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void installInitialValues(String port) {
        try /*BufferedReader serverPortFileReader = new BufferedReader(new FileReader("config.txt"))*/ {
            System.out.print("server journal file path: ");
            logPath = stdin.readLine();

            PORT = Integer.parseInt(port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void log(String request) {
        try {
            FileWriter serverJournalFileWriter = new FileWriter(logPath, true);
            serverJournalFileWriter.write(request);
            serverJournalFileWriter.write('\n');
            serverJournalFileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendToConnection(TCPConnection tcpConnection, String str) {
        tcpConnection.sendMessage(str);
    }

    private <T> String arrayToString(T[][] initArr) {
        String resultStr = "[";
        for (T[] arr : initArr) {
            resultStr += "[";
            for (T obj : arr) {
                resultStr += obj.toString() + ", ";
            }
            resultStr = resultStr.substring(0, resultStr.length() - 2);
            resultStr += "], ";
        }
        resultStr = resultStr.substring(0, resultStr.length() - 2);
        resultStr += "]";

        return resultStr;
    }

    private <T> void readCell(T[][] arr, TCPConnection tcpConnection, Integer externalIndex, Integer internalIndex) {
        if (externalIndex < arr.length) {
            if (internalIndex < arr[externalIndex].length) {
                sendToConnection(tcpConnection, "arr["
                        + externalIndex
                        + "]["
                        + internalIndex
                        + "] = "
                        + arr[externalIndex][internalIndex]);
            } else {
                sendToConnection(tcpConnection, "WRONG INTERNAL INDEX!");
            }
        } else {
            sendToConnection(tcpConnection, "WRONG EXTERNAL INDEX!");
        }
    }

    private <T> void writeCell(T[][] arr, Integer[] forbiddenRange, TCPConnection tcpConnection, Integer externalIndex, Integer internalIndex, T value) {
        if (externalIndex < arr.length) {
            if (internalIndex < arr[externalIndex].length) {
                if (!(externalIndex == forbiddenRange[0] && internalIndex >= forbiddenRange[1] && internalIndex <= forbiddenRange[2])) {
                    sendToConnection(tcpConnection, "\nold arr:\n"
                            + arrayToString(arr));
                    arr[externalIndex][internalIndex] = value;
                    sendToConnection(tcpConnection, "\nnew arr:\n"
                            + arrayToString(arr) + "\n");
                } else {
                    sendToConnection(tcpConnection, "PERMISSION ERROR!");
                }
            } else {
                sendToConnection(tcpConnection, "WRONG INTERNAL INDEX!");
            }
        } else {
            sendToConnection(tcpConnection, "WRONG EXTERNAL INDEX!");
        }
    }
}
