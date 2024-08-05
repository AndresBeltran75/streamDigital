package com.streaming.digital.backing;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SubjectTerm;

import org.springframework.stereotype.Component;

@Component
public class LectorCorreos {
	

    private String getTextFromMimeMultipart(Multipart mimeMultipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.isMimeType("text/html")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof Multipart) {
                result.append(getTextFromMimeMultipart((Multipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }
	
    private String getTextFromMessage(Message message) throws Exception {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("text/html")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    public String lectorCorreos(String pCorreo, String pClave, String pAsunto) {
        String mensaje = null;
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.host", "imap.gmail.com");
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.auth", "true");

        Session emailSession = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(pCorreo, pClave);
            }
        });

        try {
            Store store = emailSession.getStore("imaps");
            store.connect("imap.gmail.com", pCorreo, pClave);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.search(new SubjectTerm(pAsunto));

            for (Message message : messages) {
                MimeMessage mimeMessage = (MimeMessage) message;
                System.out.println("Asunto: " + mimeMessage.getSubject());
                System.out.println("Fecha: " + mimeMessage.getSentDate());
                System.out.println("De: " + mimeMessage.getFrom()[0]);
                System.out.println("Contenido: " + getTextFromMessage(mimeMessage));
                mensaje = "salida:" + getTextFromMessage(mimeMessage);
                break;
            }

            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            mensaje = "error:" + e.getMessage();
            e.printStackTrace();
        }
        return mensaje;
    }

}
