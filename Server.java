import java.io.*;
import java.net.*;

public class Server{ //server
    public static void main(String[] args) throws FileNotFoundException{
        DatagramSocket serverSocket = null;

        try {
            serverSocket = new DatagramSocket(12121);
            byte[] receiveData = new byte[1024];
            String[] vaildEmails = {"zayed@gmail.com", "zaid@gmail.com"}; //valid emails for "TO", checks validity

            InetAddress IP = InetAddress.getLocalHost();
            System.out.println("Mail Server Starting at host: "+ IP.getHostName()); //prints server hostname
            System.out.println("Server is listening on port 12121...");

            while (true) { //infinite loop, Server stays on
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); //empty packet
                serverSocket.receive(receivePacket); //wait till receive, save in receivepacket

                InetAddress clientAddress = receivePacket.getAddress(); //take packet input and convert to address

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String a1[] = message.split("TO:");
                String a2[] = a1[1].split("FROM:");
                String to = a2[0];
                String a3[] = a2[1].split("SUBJECT:");
                String from = a3[0];
                String a4[] = a3[1].split("BODY:");
                String subject = a4[0];
                String a5[] = a4[1].split("HOST:");
                String body = a5[0];
                String hostname = a5[1];

                System.out.println("Mail Received from " + hostname); //print the hostname of the address
                String directoryPath = "./mails/";
                String timestamp = java.time.LocalDateTime.now().toString().replace(":", "-");
                String filename = timestamp + ".txt";
                String relativeFilePath = directoryPath + filename;
                File directory = new File(directoryPath);
                directory.mkdirs();

                System.out.println("FROM: " + from);
                System.out.println("TO: " + to);
                System.out.println("SUBJECT: " + subject);
                System.out.println("TIME: " + timestamp);
                System.out.println(body);

                if (!to.isEmpty() && !from.isEmpty() && to.contains("@") && from.contains("@"))
                {
                    System.out.println("The Header fields are verified.");
                    System.out.println("Sending 250 Ok");

                    String confirmation = "250 OK:" + timestamp;
                    int clientPort = receivePacket.getPort();
                    byte[] sendData = confirmation.getBytes();

                    File f = new File(relativeFilePath);

                    PrintWriter fout = new PrintWriter(f);
                    fout.println("FROM: " + from);
                    fout.println("TO: " + to);
                    fout.println("SUBJECT: " + subject);
                    fout.println("TIME: " + timestamp);
                    fout.println(body);
                    fout.close();

                    int messageBytes = confirmation.getBytes().length;
                    String byteSize = Integer.toString(messageBytes);
                    System.out.println("Message is sending " + byteSize + " Bytes");
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                    serverSocket.send(sendPacket);
                    System.out.println("mail sent to client at " + hostname + ":" + clientAddress);
                }
                else {
                    System.out.println("The Header fields are not valid.");
                    System.out.println("Sending 501 Error");

                    String confirmation = "501 Error";
                    int clientPort = receivePacket.getPort();
                    byte[] sendData = confirmation.getBytes();
                    int messageBytes = confirmation.getBytes().length;
                    String byteSize = Integer.toString(messageBytes);
                    System.out.println("Message is sending " + byteSize + " Bytes");
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                    serverSocket.send(sendPacket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
    }
}
