package Meriam.ServiceRest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Meriam.dao.ContactRepository;
import Meriam.entities.Contact;
@RestController
@CrossOrigin("*")
public class ContactRestService {
	
	private String path;
public String getPath() {
		return this.path;
	}


	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	private String originUrl;
	public String getOriginUrl()
	{
		return this.originUrl;
	}
	public void setOriginUrl(String originUrl) {
		this.originUrl=originUrl;
	}

@Autowired
private ContactRepository contactRepository ;

@RequestMapping(value="/sendPath",method=RequestMethod.POST)
public String sendPath(@RequestBody String path)
{System.out.println(path); 
return path;}











@RequestMapping(value="/contacts",method=RequestMethod.GET)
public List<Contact>getContact()

{
	return contactRepository.findAll();
}
@RequestMapping(value="/contacts/{id}",method=RequestMethod.GET)
public Optional<Contact> getContactById(@PathVariable Long id)
{
	return contactRepository.findById(id);
	
}

@RequestMapping(value="/chercherContact",method=RequestMethod.GET)
public List<Contact> chercherContact(@RequestParam (name="mc") String mc)
{
	return contactRepository.chercherContact(mc);
	
}

@RequestMapping(value="/contacts",method=RequestMethod.POST)
public Contact save(@RequestBody Contact c)
{return contactRepository.save(c); }

@RequestMapping(value="/contacts/{id}",method=RequestMethod.DELETE)
public void save(@PathVariable Long id)
{
	contactRepository.deleteById(id); 
}

@RequestMapping(value="/contacts/{id}",method=RequestMethod.PUT)
public Contact update(@PathVariable Long id,@RequestBody Contact c)
{
	c.setId(id);
	return contactRepository.save(c);
	
	
}
/////////////////////////////////////////
@RequestMapping(value="/getProcess",method=RequestMethod.GET)
public List<String> getProcess(@RequestParam (name="mc") String mc,@RequestParam (name="oUrl") String oUrl) throws IOException, InterruptedException
{
	setPath(mc);
	setOriginUrl(oUrl);
	System.out.println(oUrl);
	gitInit(Paths.get(this.getPath()));
	gitAddOrigin(Paths.get(this.getPath()),this.getOriginUrl());
	gitStage(Paths.get(this.getPath()),"talend.project");
	System.out.println(mc);
	
	System.out.println("theUrl is"+oUrl);
	
	System.out.println("path is "+getPath());
	System.out.println("realpath is "+Paths.get(getPath()));
	List<String>process = new ArrayList();

	try {
		File[] file = new File(mc).listFiles();
		 return filterProcess(file);
	} catch (Exception e) {
		System.out.println("unvalid path");
	}
	return process;

 }

public static List<String> filterProcess(File[] files) throws IOException, InterruptedException {
	List<String>firstList=new ArrayList();
    for (File file : files) {
    	if(file.getName().equals("process")==true) {
    		files=file.listFiles();
    		for (File file2 : files) {
    			
    			firstList.add(file2.getName());
    		
    		}
    		
    	}
    		 
   	         
    }
    List<String>finalList=new ArrayList();
    for(int i=0;i<firstList.size();i++) {
    	if(i%3==0) { finalList.add(firstList.get(i));
    		
    	}
    }
    System.out.println(finalList);
    return finalList;
}




/////////////////



@RequestMapping(value="/upload",method=RequestMethod.GET)
public void upload(@RequestParam (name="pn") String pn,@RequestParam (name="msg") String msg) throws IOException, InterruptedException
{
	
	StringBuilder processItem= new StringBuilder(pn);
	

	
	String processShortPath="process/"+pn;
	String processProperties="process/"+pn.substring(0,pn.indexOf("item"))+"properties";
	String processScreenShot="process/"+pn.substring(0,pn.indexOf("item"))+"screenshot";
	
	gitInit(Paths.get(this.getPath()));
	gitStage(Paths.get(this.getPath()), processShortPath);
	gitStage(Paths.get(this.getPath()), processProperties);
	gitStage(Paths.get(this.getPath()), processScreenShot);
	gitCommit(Paths.get(this.getPath()),msg) ;
	gitCreateBranch(Paths.get(this.getPath()));

	gitPush(Paths.get(this.getPath()));
	System.out.println(processShortPath);
	System.out.println(msg);
	System.out.println(processProperties);
	System.out.println("Index"+pn.indexOf("item"));
	System.out.println(pn.substring(0,pn.indexOf("item")));
	

 }
///////////////////////////////


@RequestMapping(value="/download",method=RequestMethod.GET)
public void download(@RequestParam (name="pd") String pd) throws IOException, InterruptedException
{
	
	System.out.println("pull directory is"+pd);

	
	gitInit(Paths.get(pd));
	gitAddOrigin(Paths.get(pd),this.getOriginUrl());
	gitPull(Paths.get(pd));
	
	

 }

public static void gitInit(Path directory) throws IOException, InterruptedException {
	runCommand(directory, "git", "init");
}

public static void gitStage(Path directory, String psp) throws IOException, InterruptedException {
	
	runCommand(directory, "git", "add",psp);
	System.out.println(directory);
	
}

public static void gitCommit(Path directory, String message) throws IOException, InterruptedException {
	runCommand(directory, "git", "commit", "-m", message);
}

public static void gitAddOrigin(Path directory,String url) throws IOException, InterruptedException
{
	runCommand(directory,"git","remote","add","origin",url);
}

public static void gitPush(Path directory) throws IOException, InterruptedException {
	runCommand(directory, "git", "push","-u","origin","master");
	System.out.println("pushing here");
}

public static void gitPull(Path directory) throws IOException, InterruptedException {
	runCommand(directory, "git", "pull","origin","master");
	System.out.println("pulling here");
}

public static void gitCreateBranch(Path directory) throws IOException, InterruptedException {
	runCommand(directory, "git", "branch","-M","master");
}
/////////////////////////////////////////////////////

public static void runCommand(Path directory, String... command) throws IOException, InterruptedException {
	Objects.requireNonNull(directory, "directory");
	if (!Files.exists(directory)) {
		throw new RuntimeException("can't run command in non-existing directory '" + directory + "'");
	}
	ProcessBuilder pb = new ProcessBuilder()
			.command(command)
			.directory(directory.toFile());
	Process p = pb.start();
StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
	StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
	outputGobbler.start();
	errorGobbler.start();
	int exit = p.waitFor();
	errorGobbler.join();
	outputGobbler.join();
	if (exit != 0) {
		//throw new AssertionError(String.format("runCommand returned %d", exit));
		System.out.println("not ok");
	}
}







private static class StreamGobbler extends Thread {

	private final InputStream is;
	private final String type;

	private StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	@Override
	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(type + "> " + line);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}










}
