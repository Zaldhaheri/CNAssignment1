public class Test
{
    public static void main(String args[])
    {
        String request = "TO:z@gmailFROM:zaid@gmailSUBJECT:networksBODY:man cmonHOST:Desktop";
        String a1[] = request.split("TO:");
        String a2[] = a1[1].split("FROM:");
        String to = a2[0];
        String a3[] = a2[1].split("SUBJECT:");
        String from = a3[0];
        String a4[] = a3[1].split("BODY:");
        String subject = a4[0];
        String a5[] = a4[1].split("HOST:");
        String body = a5[0];
        String hostname = a5[1];
        System.out.println("TO:" + to + "FROM:" + from + "SUBJECT:" + subject + "BODY:" + body + "HOST:" + hostname);

    }
}