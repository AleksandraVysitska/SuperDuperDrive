package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {
	private static String firstName = "checkFirstName";
	private static String lastName = "checkLastName";
	private static String userName = "root";
	private static String password = "password";
	private static String noteTitle = "Super test title";
	private static String noteDescription = "Super test description";
	private static String credURL = "example.com";

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private WebElement inputFirstName;
	private WebElement inputLastName;
	private WebElement inputUsername;
	private WebElement inputPassword;
	private WebElement signUpButton;
	private WebElement loginButton;
	private WebElement logoutButton;

	private WebElement notesTab;
	private WebElement newNote;
	private WebElement notedescription;
	private WebElement savechanges;


	private WebElement notesTable;
	private List<WebElement> notesList;
	private Boolean created = false;
	private Boolean edited = false;
	private WebElement deleteElement;
	private WebElement editElement;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void getUnauthorizedHomePage() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void newUserAccessTest() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 30);

		signup();
		login();

		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

		Assertions.assertEquals("Home", driver.getTitle());

	}



	@Test
	public void noteCreationTest() {
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, 30);

		// signup
		signup();

		//login
		login();

		//added note
		addNote(jse, wait);
		Assertions.assertEquals("Result", driver.getTitle());

		//check for note
		checkNote(jse);
		Assertions.assertTrue(this.created);
	}




	@Test
	public void noteUpdationTest() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		// signup
		signup();

		//login
		login();
		Assertions.assertEquals("Home", driver.getTitle());

		//added note
		addNote(jse, wait);
		Assertions.assertEquals("Result", driver.getTitle());

		//check for note
		checkNote(jse);
		Assertions.assertTrue(created);

		//update note
		updateNote(jse, wait);
		Assertions.assertEquals("Result", driver.getTitle());

		//check the updated note
		checkUpdatedNote(jse);
		Assertions.assertTrue(edited);
	}


	@Test
	public void noteDeletionTest() {

		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;

		// signup
		signup();
		//login
		login();
		Assertions.assertEquals("Home", driver.getTitle());

		//added note
		addNote(jse, wait);
		Assertions.assertEquals("Result", driver.getTitle());

		//check for note
		checkNote(jse);
		Assertions.assertTrue(created);

		deleteNote(jse);

		wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
		Assertions.assertEquals("Result", driver.getTitle());
	}

	private void deleteNote(JavascriptExecutor jse) {
		this.notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", this.notesTab);
		this.notesTable = driver.findElement(By.id("userTable"));
		this.notesList = this.notesTable.findElements(By.tagName("td"));
		this.deleteElement = null;
		for (int i = 0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			this.deleteElement = element.findElement(By.name("delete"));
			if (this.deleteElement != null){
				break;
			}
		}
	}

	private void signup() {
		driver.get("http://localhost:" + this.port + "/signup");
		this.inputFirstName = driver.findElement(By.id("inputFirstName"));
		this.inputLastName = driver.findElement(By.id("inputLastName"));
		this.inputUsername = driver.findElement(By.id("inputUsername"));
		this.inputPassword = driver.findElement(By.id("inputPassword"));
		this.signUpButton = driver.findElement(By.id("signup-button"));

		inputFirstName.sendKeys(firstName);
		inputLastName.sendKeys(lastName);
		inputUsername.sendKeys(userName);
		inputPassword.sendKeys(password);
		signUpButton.click();
	}

	private void login() {
		driver.get("http://localhost:" + this.port + "/login");
		this.loginButton = driver.findElement(By.id("login-button"));
		this.inputUsername = driver.findElement(By.id("inputUsername"));
		this.inputPassword = driver.findElement(By.id("inputPassword"));
		inputUsername.sendKeys(userName);
		inputPassword.sendKeys(password);
		loginButton.click();
	}

	private void logout(WebDriverWait wait) throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button"))).click();
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
	}

	private void addNote(JavascriptExecutor jse, WebDriverWait wait) {
		this.notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);
		wait.withTimeout(Duration.ofSeconds(30));
		this.newNote = driver.findElement(By.id("add-note-button"));
		wait.until(ExpectedConditions.elementToBeClickable(newNote)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(noteTitle);
		this.notedescription = driver.findElement(By.id("note-description"));
		notedescription.sendKeys(noteDescription);
		this.savechanges = driver.findElement(By.id("save-changes"));
		savechanges.click();
	}

	private void checkNote(JavascriptExecutor jse) {
		driver.get("http://localhost:" + this.port + "/home");

		this.notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);
		this.notesTable = driver.findElement(By.id("userTable"));
		this.notesList = notesTable.findElements(By.tagName("th"));
		this.created = false;
		for (int i=0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			if (element.getAttribute("innerHTML").equals(this.noteTitle)) {
				this.created = true;
				break;
			}
		}
	}

	private void checkUpdatedNote(JavascriptExecutor jse) {
		driver.get("http://localhost:" + this.port + "/home");
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);
		notesTable = driver.findElement(By.id("userTable"));
		notesList = notesTable.findElements(By.tagName("th"));
		for (int i = 0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			if (element.getAttribute("innerHTML").equals("new note title")) {
				edited = true;
				break;
			}
		}
	}

	private void updateNote(JavascriptExecutor jse, WebDriverWait wait) {
		this.notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);
		this.notesTable = driver.findElement(By.id("userTable"));
		this.notesList = notesTable.findElements(By.tagName("td"));
		for (int i = 0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			this.editElement = element.findElement(By.name("edit"));
			if (this.editElement != null){
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(this.editElement)).click();
		WebElement notetitle = driver.findElement(By.id("note-title"));
		wait.until(ExpectedConditions.elementToBeClickable(notetitle));
		notetitle.clear();
		notetitle.sendKeys("new note title");
		savechanges = driver.findElement(By.id("save-changes"));
		savechanges.click();
	}

}
