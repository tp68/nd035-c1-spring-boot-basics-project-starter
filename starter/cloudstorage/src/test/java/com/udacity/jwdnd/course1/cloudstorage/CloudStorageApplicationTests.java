package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

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

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();


		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success-msg")));

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());		
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();

        var errorMessageSpan = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("upload-error-message")));

        final String expectedErrorMessage = "Upload error: The file is too large! The maximum size is 1MB.";
        String actualErrorMessage = errorMessageSpan.getText();
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage, "The upload error message text did not match the expected value.");
	}

 
    /**
     * Helper method to click the Logout button and wait for redirection to the login page.
     */
    private void doLogOut() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        
        // Assuming the logout button has the ID "logout-button" on the Home page.
        // Adjust the selector if your actual button has a different ID or class.
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("logout-button")));
        WebElement logoutButton = driver.findElement(By.id("logout-button"));
        logoutButton.click();
        
        // Wait for redirection back to the login page title
        webDriverWait.until(ExpectedConditions.titleContains("Login"));
    }


    /**
	 * Requirement: 
	 * 1. Write Tests for User Signup, Login, and Unauthorized Access Restrictions.
	 * 
	 * Write a test that verifies that an unauthorized user can only access the login and signup pages.
	 * 
     * Test to verify that an unauthorized user is correctly redirected to the login page
     * when attempting to access any secure endpoint, while still being able to access 
     * public endpoints.
     */
    @Test
    public void testUnauthorizedAccess() {
        String baseUrl = "http://localhost:" + this.port;
        String loginUrl = baseUrl + "/login";
        String signupUrl = baseUrl + "/signup";
        
        // 1. Try to access the secure Home page (/)
        driver.get(baseUrl + "/");
        // Expect to be redirected to the login page
        Assertions.assertEquals(loginUrl, driver.getCurrentUrl(), 
            "Unauthorized user accessing '/' should be redirected to /login.");

        // 2. Try to access another secure/random page
        driver.get(baseUrl + "/some-secure-page");
        // Expect to be redirected to the login page
        Assertions.assertEquals(loginUrl, driver.getCurrentUrl(), 
            "Unauthorized user accessing a secure URL should be redirected to /login.");
            
        // 3. Try to access the public Login page
        driver.get(loginUrl);
        // Expect to stay on the login page
        Assertions.assertEquals(loginUrl, driver.getCurrentUrl(), 
            "Login page should be accessible.");
            
        // 4. Try to access the public Signup page
        driver.get(signupUrl);
        // Expect to stay on the signup page
        Assertions.assertEquals(signupUrl, driver.getCurrentUrl(), 
            "Signup page should be accessible.");
    }	


    /**
	 * Requirement: 
	 * 1. Write Tests for User Signup, Login, and Unauthorized Access Restrictions.
	 * 
	 * Write a test that signs up a new user, logs in, verifies that the home page is accessible, 
	 * logs out, and verifies that the home page is no longer accessible.
	 * 
     * New test verifying the complete authentication life-cycle:
     * Sign Up -> Log In -> Verify Home Access -> Log Out -> Verify Home Protected.
     */
    @Test
    public void testFullAuthenticationFlow() {
        String testUser = "AuthTestUser";
        String testPass = "AuthTestPass1";
        String baseUrl = "http://localhost:" + this.port;
        String loginUrl = baseUrl + "/login";
        
        // 1. Sign up a new user
        doMockSignUp("Auth", "Test", testUser, testPass);
        
        // 2. Log in (redirects to Home)
        doLogIn(testUser, testPass);
        
        // Verification after login: Should be on the Home page
        Assertions.assertEquals("Home", driver.getTitle(), 
            "Should be on the Home page after successful login.");

        // 3. Log out
        doLogOut();
        
        // 4. Verify Home page is no longer accessible (should redirect to login)
        driver.get(baseUrl + "/"); 
        
        // Verification after logout: Should be redirected to the Login page
        Assertions.assertEquals(loginUrl, driver.getCurrentUrl(), 
            "After logout, accessing a secure page should redirect to /login.");
        Assertions.assertEquals("Login", driver.getTitle(), 
            "After logout, the browser should display the Login page.");
    }	



    /**
     * Helper method to get a standard WebDriverWait object.
     */
    private WebDriverWait getWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /**
     * Test that creates a note and verifies it is displayed in the table.
     */
    @Test
    public void testCreateNoteAndVerify() {
        String testUser = "NoteCreateUser";
        String testPass = "nCp123";
        String noteTitle = "My First Note";
        String noteDescription = "This is a test description.";
        
        // 1. Sign up and Log in
        doMockSignUp("Note", "Create", testUser, testPass);
        doLogIn(testUser, testPass);

        WebDriverWait wait = getWait();
        
        // 2. Navigate to Notes tab
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
        
        // 3. Open Note Modal
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-new-note-btn"))).click();
        
        // 4. Fill and submit Note
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title"))).sendKeys(noteTitle);
        driver.findElement(By.id("note-description")).sendKeys(noteDescription);
        driver.findElement(By.id("note-submit-btn")).click();
        
        // 5. Verify note is displayed (after closing modal and page refresh)
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click(); // Re-activate tab if needed
        
        WebElement displayedTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody tr td:nth-child(2)")));
        WebElement displayedDescription = driver.findElement(By.cssSelector("table tbody tr td:nth-child(3)"));
        
        Assertions.assertEquals(noteTitle, displayedTitle.getText(), "The created note title should be displayed.");
        Assertions.assertEquals(noteDescription, displayedDescription.getText(), "The created note description should be displayed.");
    }

    /**
     * Test that edits an existing note and verifies the changes are displayed.
     */
    @Test
    public void testEditNoteAndVerify() {
        String testUser = "NoteEditUser";
        String testPass = "nEp123";
        String originalTitle = "Old Note";
        String updatedTitle = "New Title";
        String updatedDescription = "Updated Description Text";
        
        // 1. Sign up, Log in, and Create initial Note
        doMockSignUp("Note", "Edit", testUser, testPass);
        doLogIn(testUser, testPass);
        
        WebDriverWait wait = getWait();
        
        // Create initial note
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-new-note-btn"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title"))).sendKeys(originalTitle);
        driver.findElement(By.id("note-description")).sendKeys("temporary text");
        driver.findElement(By.id("note-submit-btn")).click();
        
        // 2. Click Edit button for the first note
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click(); // Re-activate tab
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tbody/tr[1]//button[@data-action='note-edit']"))).click();        
		
        // 3. Verify original data is loaded and update fields in the modal
        WebElement titleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        
        // Clear and send new values
        titleInput.clear();
        titleInput.sendKeys(updatedTitle);
        
        WebElement descriptionInput = driver.findElement(By.id("note-description"));
        descriptionInput.clear();
        descriptionInput.sendKeys(updatedDescription);
        
        // 4. Submit the modal form (using the same save button)
        driver.findElement(By.id("note-submit-btn")).click();

        // 5. Verify updated note is displayed
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click(); // Re-activate tab if needed
        
        WebElement displayedTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody tr td:nth-child(2)")));
        WebElement displayedDescription = driver.findElement(By.cssSelector("table tbody tr td:nth-child(3)"));
        
        Assertions.assertEquals(updatedTitle, displayedTitle.getText(), "The note title should be updated.");
        Assertions.assertEquals(updatedDescription, displayedDescription.getText(), "The note description should be updated.");
    }

    /**
     * Test that deletes a note and verifies that the note is no longer displayed.
     */
    @Test
    public void testDeleteNoteAndVerify() {
        String testUser = "NoteDeleteUser";
        String testPass = "nD_p123";
        String noteTitle = "Note to be Deleted";
        
        // 1. Sign up, Log in, and Create initial Note
        doMockSignUp("Note", "Delete", testUser, testPass);
        doLogIn(testUser, testPass);
        
        WebDriverWait wait = getWait();
        
        // Create initial note
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-new-note-btn"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title"))).sendKeys(noteTitle);
        driver.findElement(By.id("note-description")).sendKeys("This note should disappear.");
        driver.findElement(By.id("note-submit-btn")).click();
        
        // 2. Verify creation (before deletion)
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click(); 
        
        // Wait for the note title to appear
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("table tbody tr td:nth-child(2)"), noteTitle));

        // 3. Click Delete button for the first note        
        WebElement deleteButton = driver.findElement(By.xpath("//tbody/tr[1]//button[@data-action='note-delete']"));
        deleteButton.click();

        // Wait for and accept the JavaScript confirmation alert (due to onclick="return confirm(...)")
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();		

        // 4. Verify note is deleted (wait for the note title to disappear)
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click(); 
        
        // We check if the note title is no longer in the page source
        Assertions.assertFalse(driver.getPageSource().contains(noteTitle), 
            "The deleted note title should no longer be present in the page source.");
    }


    /**
     * Helper method to create a credential for reuse in other tests.
     */
    private void doCreateCredential(String url, String username, String password) {
        WebDriverWait wait = getWait();
        
        // Navigate to Credentials tab
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
        
        // Open Credentials Modal
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-new-credential-btn"))).click();
        
        // Fill out credentials form
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url"))).sendKeys(url);
        driver.findElement(By.id("credential-username")).sendKeys(username);
        driver.findElement(By.id("credential-password")).sendKeys(password);
        
        // Submit
        driver.findElement(By.id("credential-submit-btn")).click();
        
        // Navigate back to Credentials tab to refresh table
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
    }


    /**
     * Write Tests for Credential Creation, Viewing, Editing, and Deletion.
     * 
     * Write a test that creates a set of credentials, verifies that they are displayed, 
     * and verifies that the displayed password is encrypted.
     */
    @Test
    public void testCreateCredentialAndVerifyEncryption() {
        String testUser = "CredCreateUser";
        String testPass = "cC_p123";
        String testUrl = "https://test.example.com";
        String testUsername = "mytestuser";
        String testPassword = "PlainTextPassword123";

        // 1. Sign up and Log in
        doMockSignUp("Credential", "Create", testUser, testPass);
        doLogIn(testUser, testPass);

        // 2. Create a credential
        doCreateCredential(testUrl, testUsername, testPassword);
        
        WebDriverWait wait = getWait();
        
        // 3. Verify the credential is displayed
        WebElement urlCell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable tbody tr td:nth-child(2)")));
        WebElement usernameCell = driver.findElement(By.cssSelector("#credentialTable tbody tr td:nth-child(3)"));
        WebElement passwordCell = driver.findElement(By.cssSelector("#credentialTable tbody tr td:nth-child(4)"));
        
        Assertions.assertEquals(testUrl, urlCell.getText(), "The created credential URL should be displayed.");
        Assertions.assertEquals(testUsername, usernameCell.getText(), "The created credential username should be displayed.");
        
        // 4. Verify that the displayed password is NOT the plaintext original password (i.e., it is encrypted/masked)
        Assertions.assertNotEquals(testPassword, passwordCell.getText(), 
            "The displayed password on the main page must be encrypted or masked, not the plaintext.");
        
        // Additionally, verify that some content is present (e.g., the encrypted hash or masked stars)
        Assertions.assertTrue(passwordCell.getText().length() > 0, 
            "The password field should contain the encrypted/masked value.");
    }

    /**
     * Write Tests for Credential Creation, Viewing, Editing, and Deletion.
     * 
     * Write a test that views an existing set of credentials, verifies that the 
     * viewable password is unencrypted, edits the credentials, and verifies that the 
     * changes are displayed.
     */
    @Test
    public void testEditCredentialAndVerifyDecryption() {
        String testUser = "CredEditUser";
        String testPass = "cE_p123";
        String originalUrl = "https://old.site.com";
        String originalPassword = "OriginalPass";
        
        String updatedUrl = "https://new.site.com/updated";
        String updatedPassword = "UpdatedPassword456";

        // 1. Sign up, Log in, and create initial credential
        doMockSignUp("Credential", "Edit", testUser, testPass);
        doLogIn(testUser, testPass);
        doCreateCredential(originalUrl, "testuser", originalPassword);
        
        WebDriverWait wait = getWait();
        
        // 2. Click the Edit button for the first credential
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.credential-edit-btn"))).click();
        
        // 3. Verify that the password field in the modal is UNENCRYPTED (decrypted)
        WebElement urlInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement passwordInput = driver.findElement(By.id("credential-password"));
        
        // The decrypted password should be set as the 'value' attribute of the password input
        Assertions.assertEquals(originalPassword, passwordInput.getAttribute("value"),
            "The password in the edit modal should be the decrypted plaintext password.");
        
        // 4. Edit the fields
        urlInput.clear();
        urlInput.sendKeys(updatedUrl);
        passwordInput.clear();
        passwordInput.sendKeys(updatedPassword); // This new password will be encrypted upon submit
        
        // 5. Submit the form
        driver.findElement(By.id("credential-submit-btn")).click();
        
        // 6. Verify updated changes are displayed
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
        
        WebElement urlCell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable tbody tr td:nth-child(2)")));
        WebElement passwordCell = driver.findElement(By.cssSelector("#credentialTable tbody tr td:nth-child(4)"));
        
        Assertions.assertEquals(updatedUrl, urlCell.getText(), "The credential URL should be updated.");
        
        // Verify the newly set password is now also encrypted/masked on the main page
        Assertions.assertNotEquals(updatedPassword, passwordCell.getText(),
            "The newly updated password should be encrypted/masked on the main page.");
    }
    
    /**
     * Write Tests for Credential Creation, Viewing, Editing, and Deletion.
     * 
     * Write a test that deletes an existing set of credentials and verifies that 
     * the credentials are no longer displayed.
     */
    @Test
    public void testDeleteCredentialAndVerify() {
        String testUser = "CredDeleteUser";
        String testPass = "cD_p123";
        String testUrl = "https://delete.me";

        // 1. Sign up, Log in, and create a credential
        doMockSignUp("Credential", "Delete", testUser, testPass);
        doLogIn(testUser, testPass);
        doCreateCredential(testUrl, "todelete", "pass");
        
        WebDriverWait wait = getWait();
        
        // 2. Verify creation (before deletion)
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
        
        // Wait for the URL to appear
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("#credentialTable tbody tr td:nth-child(2)"), testUrl));

        // 3. Click the Delete button and accept the confirmation
        WebElement deleteButton = driver.findElement(By.cssSelector("button.credential-delete-btn"));
        deleteButton.click();
        
        // Handle confirmation alert (if the application uses onclick="return confirm(...)")
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        // 4. Verify deletion
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
        
        // Check if the URL is no longer present in the page source
        Assertions.assertFalse(driver.getPageSource().contains(testUrl), 
            "The deleted credential URL should no longer be present in the page source.");
    }

}
