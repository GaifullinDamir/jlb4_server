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
        try{
            log(receivedStr);
            System.out.println(receivedStr);
            Checker.checkExpression(receivedStr);
            Calculator calc = new Calculator();
            var result = Double.toString(calc.calculate(receivedStr));
            System.out.println("Результат вычисления: " + result);
            sendToConnection(tcpConnection, "Результат вычисления: " + result);
        }catch(Exception e){
            System.out.println(e);
            sendToConnection(tcpConnection, e.toString());
            log(e.toString());
        }
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
        try {
            System.out.println("Server journal file path: ");
            logPath = stdin.readLine();
            PORT = Integer.parseInt(port);
            System.out.println(PORT);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void log(String request) {
        try {
            FileWriter serverJournalFileWriter = new FileWriter(logPath, true);
            serverJournalFileWriter.write(request);
            serverJournalFileWriter.write('\n');
            serverJournalFileWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void sendToConnection(TCPConnection tcpConnection, String str) {
        tcpConnection.sendMessage(str);
    }
}
