package Meriam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import Meriam.WtfApplication;
import Meriam.dao.ContactRepository;
import Meriam.entities.Contact;

@SpringBootApplication
public class WtfApplication implements CommandLineRunner{

	@Autowired
	private ContactRepository contactRepository;
	public static void main(String[] args)  {
		SpringApplication.run(WtfApplication.class, args);
		//System.out.println(org.hibernate.Version.getVersionString());
	}

	@Override
	public void run(String... args) throws Exception {
		
	/*	DateFormat df= new SimpleDateFormat("dd/MM/yyyy");
		contactRepository.save(new Contact("meriam","zayani",df.parse("12/10/1995"),"meriam.zayani@esprit.tn",50609021, "meriam.png"));
		contactRepository.save(new Contact("meriam1","zayani",df.parse("12/10/1995"),"meriam.zayani@esprit.tn",50609021, "meriam.png"));
		contactRepository.save(new Contact("meriam1","zayani",df.parse("12/10/1995"),"meriam.zayani@esprit.tn",50609021, "meriam.png"));
		contactRepository.findAll().forEach(c->{
			System.out.println(c.getNom());
		});*/
	}

}
