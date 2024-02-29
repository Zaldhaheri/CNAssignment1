import java.io.*;
import java.util.*;
import java.net.*;

public class Client{ //Client (Email writter)
    public static void main(String[] args) {
        DatagramSocket clientSocket = null; //create an empty socket
        Scanner console = new Scanner(System.in); //for user input

        try { //error handler to catch io errors
            while(true) //infinite loop until break
            {
                InetAddress IP = InetAddress.getLocalHost();
                String hostname = IP.getHostName();
                System.out.println("Mail Client Starting at host: "+ hostname); //prints the hostname (DESKTOP-XXXX)

                System.out.print("Type name of Mail servers: ");
                String mailServer = console.nextLine(); //get user input for server hostname

                clientSocket = new DatagramSocket(); //create empty socket object
                InetAddress serverAddress = InetAddress.getByName(mailServer); //save mail server ip to server address

                System.out.println("Creating New Email.."); //mail inputs
                System.out.print("To: ");
                String to = console.nextLine();
                System.out.print("From: ");
                String from = console.nextLine();
                System.out.print("Subject: ");
                String subject = console.nextLine();
                System.out.print("Body: ");
                String body = console.nextLine();

                String request = "TO:" + to + "FROM:" + from + "SUBJECT:" + subject + "BODY:" + body + "HOST:" + hostname; //request message

                byte[] sendData = request.getBytes(); //request message to bytes
                int messageBytes = request.getBytes().length; //byte length of request message
                String byteSize = Integer.toString(messageBytes); //parsing int to string
                System.out.println("Message is sending " + byteSize + " Bytes");
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 12121); //create a packet
                clientSocket.send(sendPacket); //send packet(Bytes, Bytes length, address to send, port number)
                System.out.println("Mail Sent to Server, waiting...");

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); //create an empty packet to receive
                clientSocket.receive(receivePacket); //save received packet (ACK)

                String confirmation = new String(receivePacket.getData(), 0, receivePacket.getLength()); //convert packet (bytes) to string
                if (confirmation.contains("250 OK")) //packet received successfully
                {
                    String timestamp[] = confirmation.split("250 OK:"); //split the message and take whats after "250 OK:" (the timestamp)
                    System.out.println("Email received successfully at " + timestamp[1]);
                    System.out.println("Do you want to quit? (quit/no): ");
                    String quitter = console.nextLine();
                    if (quitter.contains("quit")) //quit the loop and end program
                        break ;
                    else if (quitter.contains("no")) //continue the loop
                        continue ;
                    else
                    {
                        System.out.println("Invalid error, quitting"); //quit
                        break ;
                    }
                }
                else if (confirmation.contains("501 Error")) //packet failed
                {
                    System.out.println("501 Error");
                    System.out.println("Header files are invalid");
                    continue ; //rewrite the whole thing
                }
                else //unkown error
                { 
                    System.out.println("Unknown Error");
                    break ; //quit loop
                }
            }
        } catch (IOException e) { //catch IOException (Inputs, files error)
            e.printStackTrace(); //print where the error was
        } finally {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close(); //close socket after code ends
            }
        }

    }
}