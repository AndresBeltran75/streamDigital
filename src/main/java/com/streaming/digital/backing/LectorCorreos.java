package com.streaming.digital.backing;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SubjectTerm;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.springframework.stereotype.Component;

@Component
public class LectorCorreos {
	
	public String lectorCorreos(String pCorreo, String pClave, String pAsunto) {

		String mensaje = null;
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.trust", "*");
        properties.put("mail.imaps.ssl.protocols", "TLSv1.2");

        Session emailSession = Session.getDefaultInstance(properties);
        
        try {
        	
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
        	
            Store store = emailSession.getStore("imap");
            store.connect("imap.gmail.com", pCorreo, pClave);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.search(new SubjectTerm(pAsunto));

            for (Message message : messages) {
                MimeMessage mimeMessage = (MimeMessage) message;
                System.out.println("Asunto: " + mimeMessage.getSubject());
                System.out.println("Fecha: " + mimeMessage.getSentDate());
                System.out.println("De: " + mimeMessage.getFrom()[0]);
                System.out.println("Contenido: " + mimeMessage.getContent().toString());
                mensaje = "salida:" + mimeMessage.getContent().toString();
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
