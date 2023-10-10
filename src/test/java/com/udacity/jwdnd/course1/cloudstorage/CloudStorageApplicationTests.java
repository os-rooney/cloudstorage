package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

	/* I installed and started geckodriver locally, because this didn't work.The Browser couldn't be started
    @BeforeAll
    static void beforeAll() {
        WebDriverManager.firefoxdriver().setup();
    }*/

    @BeforeEach
    public void beforeEach() {
        this.driver = new FirefoxDriver();
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
    public void testUnauthorizedAccess() {

        // Ensure unauthorized user can only access login and signup pages
        driver.get("http://localhost:" + this.port);
        WebElement loginTitle = driver.findElement(By.tagName("h1"));
        assertEquals("Login", loginTitle.getText());


        driver.get("http://localhost:" + this.port + "/signup");
        WebElement signupTitle = driver.findElement(By.tagName("h1"));
        assertEquals("Sign Up", signupTitle.getText());


        // Attempt to access a restricted page
        driver.get("http://localhost:" + this.port + "/home");
        WebElement loginTitleAfterRedirect = driver.findElement(By.tagName("h1"));
        assertEquals("Login", loginTitleAfterRedirect.getText());
        assertFalse(driver.getCurrentUrl().startsWith("http://localhost:" + this.port + "/home"));
    }

    @Test
    public void testUserSignupLoginHomeLogoutHomeNotAccessible() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        doMockSignUp("Test", "Test", "SU", "123");

        doLogIn("SU", "123");

        // Verify that the home page is accessible after login
        webDriverWait.until(ExpectedConditions.titleContains("Home"));
        assertTrue(driver.getCurrentUrl().startsWith("http://localhost:" + this.port + "/home"));

        doLogout();

        // Verify that the home page is accessible after login
        webDriverWait.until(ExpectedConditions.urlContains("http://localhost:" + this.port + "/login"));
        assertFalse(driver.getCurrentUrl().startsWith("http://localhost:" + this.port + "/home"));
    }

    @Test
    public void testUserSignupLoginLogout() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // User signup
        driver.get("http://localhost:" + this.port + "/signup");
        WebElement firstnameInput = driver.findElement(By.id("inputFirstName"));
        WebElement lastnameInput = driver.findElement(By.id("inputLastName"));
        WebElement usernameInput = driver.findElement(By.id("inputUsername"));
        WebElement passwordInput = driver.findElement(By.id("inputPassword"));
        WebElement signupButton = driver.findElement(By.id("buttonSignUp"));

        firstnameInput.sendKeys("Max");
        lastnameInput.sendKeys("Mustermann");
        usernameInput.sendKeys("testuser");
        passwordInput.sendKeys("password");
        signupButton.click();

        // User login
        doLogIn("testuser", "password");

        // Verify that the home page is accessible after login
        webDriverWait.until(ExpectedConditions.titleContains("Home"));
        assertTrue(driver.getCurrentUrl().startsWith("http://localhost:" + this.port + "/home"));

        doLogout();
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
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
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

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
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up! Please continue to the login page."));
        WebElement loginLink = driver.findElement(By.id("redirectLogin"));
        loginLink.click();

        assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
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
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

    private void doLogout() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Step 3: Log out
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout")));
        WebElement logoutButton = driver.findElement(By.id("logout"));
        logoutButton.click();

        // Verify that the home page is no longer accessible after logout
        assertTrue(driver.findElement(By.id("logout-msg")).getText().contains("You have been logged out"));
    }

    @Test
    public void testCreateNote() {
        doMockSignUp("Test", "User", "testuser", "password");
        doLogIn("testuser", "password");
        createNote();
    }

    private void createNote() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Navigate to the Notes tab
        driver.findElement(By.id("nav-notes-tab")).click();

        // Click the "+ Add a New Note" button
        driver.findElement(By.cssSelector(".btn.btn-info.float-right")).click();

        // Fill in the note details
        WebElement titleField = driver.findElement(By.id("note-title"));
        WebElement descriptionField = driver.findElement(By.id("note-description"));

        titleField.sendKeys("Test Note");
        descriptionField.sendKeys("This is a test note.");

        // Click the "Save changes" button in the modal
        driver.findElement(By.cssSelector(".modal-footer .btn.btn-primary")).click();

        driver.findElement(By.tagName("a")).click();
        driver.findElement(By.id("nav-notes-tab")).click();

        // Verify that the note is created and visible in the note list
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//th[text()='Test Note']")));
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[text()='This is a test note.']")));
    }
    @Test
    public void testEditNote() {
        doMockSignUp("Test", "User", "testuser", "password");
        doLogIn("testuser", "password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        createNote();

        // Navigate to the Notes tab
        driver.findElement(By.id("nav-notes-tab")).click();
        webDriverWait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("edit-note"))));

        // Click the "Edit" button of an existing note
        driver.findElement(By.id("edit-note")).click();

        // Modify the note details
        WebElement titleField = driver.findElement(By.id("note-title"));
        WebElement descriptionField = driver.findElement(By.id("note-description"));

        titleField.clear();
        titleField.sendKeys("Updated Test Note");
        descriptionField.clear();
        descriptionField.sendKeys("This is an updated test note.");

        // Click the "Save changes" button in the modal
        driver.findElement(By.id("save-note")).click();

        driver.findElement(By.tagName("a")).click();
        driver.findElement(By.id("nav-notes-tab")).click();

        // Verify that the changes appear in the note list
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//th[text()='Updated Test Note']")));
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[text()='This is an updated test note.']")));
    }
    @Test
    public void testDeleteNote() {
        doMockSignUp("Test", "User", "testuser", "password");
        doLogIn("testuser", "password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        createNote();

        // Navigate to the Notes tab
        driver.findElement(By.id("nav-notes-tab")).click();

        // Click the "Delete" button of an existing note
        driver.findElement(By.id("delete-note")).click();

        driver.findElement(By.tagName("a")).click();
        driver.findElement(By.id("nav-notes-tab")).click();

        // Verify that the note no longer appears in the note list
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//th[text()='Note Title']")));
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[text()='Note Description']")));
    }


    @Test
    public void testCreateCredential() {
        doMockSignUp("Test", "User", "testuser", "password");
        doLogIn("testuser", "password");

        createCredential();

        // Verify that the credential is created and visible in the credential list
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.findElement(By.id("nav-credentials-tab")).click();
        assertEquals(driver.findElement(By.id("example.com")).getText(), "example.com");
        assertEquals(driver.findElement(By.id("testuser")).getText(), "testuser");

    }

    private void createCredential() {
        // Navigate to the Credentials tab
        driver.findElement(By.id("nav-credentials-tab")).click();

        // Click the "+ Add a New Credential" button
        driver.findElement(By.id("add-credential")).click();

        // Fill in the credential details
        WebElement urlField = driver.findElement(By.id("credential-url"));
        WebElement usernameField = driver.findElement(By.id("credential-username"));
        WebElement passwordField = driver.findElement(By.id("credential-password"));

        urlField.sendKeys("example.com");
        usernameField.sendKeys("testuser");
        passwordField.sendKeys("password123");

        // Click the "Save changes" button in the modal
        driver.findElement(By.id("save-credential")).click();

        driver.findElement(By.tagName("a")).click();
        driver.findElement(By.id("nav-credentials-tab")).click();
    }

    @Test
    public void testEditCredential() {
        doMockSignUp("Test", "User", "testuser", "password");
        doLogIn("testuser", "password");

        createCredential();

        // Navigate to the Credentials tab
        driver.findElement(By.id("nav-credentials-tab")).click();

        // Click the "Edit" button of an existing credential
        driver.findElement(By.id("edit-credential")).click();

        // Modify the credential details
        WebElement urlField = driver.findElement(By.id("credential-url"));
        WebElement usernameField = driver.findElement(By.id("credential-username"));
        WebElement passwordField = driver.findElement(By.id("credential-password"));

        urlField.clear();
        urlField.sendKeys("updated-example.com");
        usernameField.clear();
        usernameField.sendKeys("updated-testuser");

        // Click the "Save changes" button in the modal
        driver.findElement(By.id("save-credential")).click();

        driver.findElement(By.tagName("a")).click();
        driver.findElement(By.id("nav-credentials-tab")).click();

        // Verify that the changes appear in the credential list
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        assertEquals(driver.findElement(By.id("updated-example.com")).getText(), "updated-example.com");
        assertEquals(driver.findElement(By.id("updated-testuser")).getText(), "updated-testuser");
    }

    @Test
    public void testDeleteCredential() {
        doMockSignUp("Test", "User", "testuser", "password");
        doLogIn("testuser", "password");

        createCredential();

        // Navigate to the Credentials tab
        driver.findElement(By.id("nav-credentials-tab")).click();

        // Click the "Delete" button of an existing credential
        driver.findElement(By.id("delete-credetial")).click();

        driver.findElement(By.tagName("a")).click();
        driver.findElement(By.id("nav-credentials-tab")).click();

        // Verify that the credential no longer appears in the credential list
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[text()='example.com']")));
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[text()='testuser']")));
    }
}
