package com.mindtree.flightdelay.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.mindtree.flightdelay.dto.DelayedFlightDetails;
import com.mindtree.flightdelay.dto.DelayedFlightDetailsRequest;
import com.mindtree.flightdelay.dto.NotificationResponseDTO;
import com.opencsv.CSVReader;

/**
 * 
 * @author M1021548
 *
 */
@Service
public class FlightDelayPredictionServiceImpl implements FlightDelayPredictionService {

	static final String file_path = "D://Osmosis-2020//Finalfile.csv";
	
	Session mailSession;

	/**
	 * 
	 */
	@Override
	public List<DelayedFlightDetails> getDelayInfoForScheduledFlights(DelayedFlightDetailsRequest request) {
		
		return readFlightDetails(request);
	}
	
	/**
	 * 
	 */
	@Override
	public NotificationResponseDTO sendNotification(DelayedFlightDetails delayedFlightDetails) {
		NotificationResponseDTO notificationResponseDTO = new NotificationResponseDTO();
		try {
			System.out.println("Inside sendNotification");
			setMailServerProperties();
			draftEmailMessage(delayedFlightDetails);
			sendEmail(delayedFlightDetails);
			notificationResponseDTO.setMessage("Notification sent");
		} catch (MessagingException e) {
			notificationResponseDTO.setFailed("Send Notification Failed");
			e.printStackTrace();
		}
		return notificationResponseDTO;
	}

	/**
	 * 
	 */
	private List<DelayedFlightDetails> readFlightDetails(DelayedFlightDetailsRequest request) {
		BufferedReader reader = null;
		File file = new File(file_path);
		System.out.println(file.getAbsolutePath());
		List<DelayedFlightDetails> delayedFlightDetailsList = new ArrayList<DelayedFlightDetails>();
		try {
			reader = readFlightDetails(file, delayedFlightDetailsList, request);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("ArrayList Size is"+ delayedFlightDetailsList.size());
		return delayedFlightDetailsList;
	}

	/**
	 * 
	 * @param file
	 * @param delayedFlightDetailsList
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private BufferedReader readFlightDetails(File file, List<DelayedFlightDetails> delayedFlightDetailsList,
			DelayedFlightDetailsRequest request) throws FileNotFoundException, IOException {
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(file));
		int iteration = 0;
		FileReader filereader = new FileReader(file);
		CSVReader csvReader = new CSVReader(filereader);
		String[] nextRecord;
		while ((nextRecord = csvReader.readNext()) != null) {
			if (iteration == 0) {
				iteration++;
				continue;
			}
			if (request.getAirline() != null && request.getAirline().equalsIgnoreCase(nextRecord[8])
					&& request.getFlightDate().equalsIgnoreCase(nextRecord[7])) {
				 String[] splitCity = nextRecord[14].split(",");
				 if (splitCity != null && splitCity[0].equalsIgnoreCase(request.getOriginCity())) {
					 DelayedFlightDetails delayedFlightDetails = new DelayedFlightDetails();
					 delayedFlightDetails.setAirline(nextRecord[8]);
					 delayedFlightDetails.setFlightNumber(nextRecord[13]);
					 delayedFlightDetails.setFlightDate(nextRecord[7]);
					 delayedFlightDetails.setPredictedDepartureDelay(nextRecord[82]);
					 delayedFlightDetailsList.add(delayedFlightDetails);
				 }
			}
		}

		return reader;
	}
	
	/**
	 * 
	 */
	private void setMailServerProperties() {
		System.out.println("Server Mail Properties");
		Properties emailProperties = System.getProperties();
		String host = "smtp.gmail.com";
		emailProperties.put("mail.smtp.auth", "true");
		emailProperties.put("mail.smtp.ssl.enable", "true");
		emailProperties.put("mail.smtp.starttls.enable", "true");
		emailProperties.put("mail.smtp.host", host);
		emailProperties.put("mail.smtp.port", 465);
		emailProperties.put("mail.smtp.ssl.trust", host);
		emailProperties.put("mail.smtp.timeout", "10000");    
		emailProperties.put("mail.smtp.connectiontimeout", "10000");  
		
		
		mailSession = Session.getDefaultInstance(emailProperties,
				new javax.mail.Authenticator() {

				protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("airlineautomatednotification@gmail.com", "3@passWORD3!");
				}
				});
	}

	/**
	 * 
	 * @param delayedFlightDetails 
	 * @return
	 * @throws AddressException
	 * @throws MessagingException
	 */
	private MimeMessage draftEmailMessage(DelayedFlightDetails delayedFlightDetails) throws AddressException, MessagingException {
		System.out.println("Server Mail Properties");
		String[] toEmails = { "noreply@gmail.com" };
		String emailSubject = "Flight Delay Notification";
		String emailBody = "[Flight=" + delayedFlightDetails.getAirline() + "" + delayedFlightDetails.getFlightNumber()
				+ ", FlightDate=" + delayedFlightDetails.getFlightDate() + ", DepartureDelay="
				+ delayedFlightDetails.getPredictedDepartureDelay().subSequence(0, 3) + "mins]";
		MimeMessage emailMessage = new MimeMessage(mailSession);
		for (int i = 0; i < toEmails.length; i++) {
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
		}
		emailMessage.setSubject(emailSubject);
		emailMessage.setContent(emailBody, "text/html");
		return emailMessage;
	}

	/**
	 * 
	 * @param delayedFlightDetails 
	 * @throws AddressException
	 * @throws MessagingException
	 */
	private void sendEmail(DelayedFlightDetails delayedFlightDetails) throws AddressException, MessagingException {
		/**
		 * Sender's credentials
		 */
		String fromUser = "airlineautomatednotification@gmail.com";
		String fromUserEmailPassword = "3@passWORD3!";
		System.out.println("Sending Email");
		String emailHost = "smtp.gmail.com";
		Transport transport = mailSession.getTransport("smtp");
		transport.connect(emailHost, fromUser, fromUserEmailPassword);
		MimeMessage emailMessage = draftEmailMessage(delayedFlightDetails);
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		System.out.println("Email sent successfully.");
	}
}