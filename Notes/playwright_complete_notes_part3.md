# Playwright with Java — Complete Course Notes (Part 3)
### Lectures 22 to 30 | Trace Viewer, iFrames, Shadow DOM, POM, TestNG, Reports

---

# LECTURE 21 (Continued) — Playwright Trace Viewer

---

## 1. What is Trace Viewer?

**Definition:** Trace Viewer is Playwright's built-in debugging tool that records EVERYTHING that happened during a test — like a "time machine" for test execution.

**Real-World Analogy:** Imagine having a flight recorder (black box) for your tests. If something goes wrong, you can replay exactly what happened, step by step.

**What it records:**
- Every action (click, fill, check)
- Screenshot at every step
- DOM snapshot (what the page HTML looked like)
- Network requests and responses
- Console logs and JavaScript errors
- Timeline of all events

**Why Trace Viewer > Regular Video:**

| Feature | Regular Video | Trace Viewer |
|---|---|---|
| Shows what happened | ✅ Yes | ✅ Yes |
| DOM state at each step | ❌ No | ✅ Yes |
| Network requests | ❌ No | ✅ Yes |
| Console logs | ❌ No | ✅ Yes |
| Step-by-step replay | ❌ No | ✅ Yes |
| Inspect element at any moment | ❌ No | ✅ Yes |

---

## 2. Enable Trace Recording

```java
// STEP 1: Start tracing BEFORE test steps
context.tracing().start(
    new Tracing.StartOptions()
        .setScreenshots(true)   // Screenshot at every step
        .setSnapshots(true)     // DOM snapshot at every step
        .setSources(true)       // Include source code info
);

// STEP 2: Run your test steps
page.navigate("https://example.com");
page.locator("#username").fill("admin");
page.click("#login");

// STEP 3: Stop and save trace
context.tracing().stop(
    new Tracing.StopOptions()
        .setPath(Paths.get("traces/LoginTest.zip"))
);
```

---

## 3. Open Trace Viewer

**Method 1: Terminal command**
```bash
npx playwright show-trace traces/LoginTest.zip
```

**Method 2: Online (upload ZIP)**
Visit: https://trace.playwright.dev/
Upload your ZIP file.

---

## 4. Best Practice — Trace Only on Failure

```java
context.tracing().start(new Tracing.StartOptions()
    .setScreenshots(true).setSnapshots(true));

try {
    // Test steps
} catch (Exception e) {
    // Save trace only on failure
    context.tracing().stop(new Tracing.StopOptions()
        .setPath(Paths.get("traces/" + testName + ".zip")));
    throw e;
}
```

**Why not trace every test?** Trace files are large and slow down execution. Record only for failures and flaky tests.

---

## 5. Interview Questions — Trace Viewer

**Q1: What is Playwright Trace Viewer?**
A: Trace Viewer is an advanced debugging tool that records the complete test execution including screenshots, DOM snapshots, network requests, console logs, and timeline. You can replay the test step-by-step to find exactly where and why it failed.

**Q2: When should you use Trace Viewer?**
A: Use it for debugging failed or flaky tests, especially in CI/CD environments. Record traces only for failures to avoid large file sizes and slower execution.

---

# LECTURE 22 — iFrames & Shadow DOM

---

## 1. What is an iFrame?

**Definition:** An iFrame is a web page embedded INSIDE another web page. Think of it like a TV screen inside a TV screen.

```html
<!-- Main page has an iFrame embedded -->
<iframe src="payment.html"></iframe>
```

**Why iFrames are tricky:**
- The iFrame has its OWN separate DOM
- You CANNOT directly use `page.locator()` for elements inside an iFrame
- You must TARGET the frame first, then find elements

**How to identify an iFrame in DevTools:**
- Right-click → Inspect
- Look for `<iframe>` tag in Elements panel

---

## 2. FrameLocator (Recommended Method)

```java
// Basic iFrame using ID or CSS selector
FrameLocator frame = page.frameLocator("#loginFrame");
frame.locator("#username").fill("admin");
frame.locator("#password").fill("secret");
frame.locator("#loginBtn").click();

// iFrame using source URL
page.frameLocator("[src='default.xhtml']").locator("#Click").first().click();

// Nested iFrame (iFrame inside iFrame)
page.frameLocator("iframe:nth-of-type(2)")
    .frameLocator("iframe")
    .locator("button")
    .click();
```

**Structure of Nested iFrame:**
```
Main Page
  └── Parent iFrame
        └── Child iFrame
              └── Button you want to click
```

---

## 3. Frame Object (Older Method)

```java
// Get frame by name or ID
Frame frame = page.frame("frameNameOrId");
frame.locator("#username").fill("admin");

// Get frame by URL (useful when no ID)
Frame frame = page.frames().stream()
    .filter(f -> f.url().contains("payment"))
    .findFirst()
    .orElseThrow();
frame.locator("#cardNumber").fill("4111111111111111");

// Index-based frame access
List<Frame> frames = page.frames();
// frames.get(0) = MAIN PAGE always!
// frames.get(1) = First iFrame
// frames.get(2) = Second iFrame
Frame firstIframe = page.frames().get(1);
firstIframe.locator("#username").fill("admin");
```

---

## 4. FrameLocator vs Frame Comparison

| Feature | Frame | FrameLocator |
|---|---|---|
| Type | Actual iframe object | Smart lazy locator |
| Auto-wait | ❌ No | ✅ Yes |
| Auto-retry | ❌ No | ✅ Yes |
| Can become stale | ✅ Yes (after page reload) | ❌ No |
| Recommended | ❌ No | ✅ Yes |

---

## 5. Switch Back to Main Page

```java
// Selenium needs:
driver.switchTo().defaultContent();  // Required in Selenium

// Playwright — NO switching needed!
// Just use page.locator() for main page and frameLocator() for iframe
page.locator("#mainElement").click();        // Main page
frame.locator("#iframeElement").click();     // Inside iframe
```

---

## 6. iFrame Index Rules

```
frames.get(0)  →  Main page (always!)
frames.get(1)  →  First iFrame on page
frames.get(2)  →  Second iFrame on page
```

---

## 7. What is Shadow DOM?

**Definition:** Shadow DOM is a HIDDEN DOM attached to an element. It creates a separate, isolated HTML structure inside an element that is protected from outside CSS and JavaScript.

**Real-World Analogy:** Shadow DOM is like a TV screen with its own remote control — it manages itself independently from everything else.

**Common in:** Salesforce, ServiceNow, modern web components

```html
<login-component>
    #shadow-root (open)     ← This is the Shadow DOM
        <input id="username">
        <button id="btn">Login</button>
</login-component>
```

---

## 8. Shadow DOM Types

| Type | Automation Access | Notes |
|---|---|---|
| Open Shadow DOM | ✅ Yes — Playwright auto-pierces | Most common |
| Closed Shadow DOM | ❌ Cannot access | Very rare |

---

## 9. Playwright Shadow DOM Handling

```java
// Playwright automatically pierces Open Shadow DOM
// These work even if element is inside Shadow DOM:
page.locator("#username").fill("admin");

// Using >> operator to traverse shadow boundaries
page.locator("shadow-login >> #user").fill("Venkat");
page.locator("shadow-login >> #btn").click();

// Nested Shadow DOM (shadow inside shadow)
page.locator("shadow-parent >> shadow-child >> #childInput").fill("Playwright");
page.locator("shadow-parent >> shadow-child >> #childBtn").click();

// Shadow DOM inside iFrame
FrameLocator frame = page.frameLocator("#shadowIframe");
frame.locator("[id='user']").last().fill("Sujal");
frame.locator("[id='btn']").last().click();
```

**Meaning of `>>`:** Tells Playwright to "enter the Shadow DOM" of the preceding element.

---

## 10. iFrame vs Shadow DOM Comparison

| Feature | iFrame | Shadow DOM |
|---|---|---|
| Type | Separate page inside page | Hidden DOM inside same page |
| Own DOM | ✅ Completely separate | ✅ Hidden but same document |
| JavaScript context | Separate | Same page |
| Access method | `frameLocator()` | Auto-piercing or `>>` |
| Can inspect in DevTools | ✅ Yes | ✅ Yes (if open) |
| Performance | Slower | Faster |

---

## 11. Interview Questions — iFrames & Shadow DOM

**Q1: What is the recommended way to handle iFrames in Playwright?**
A: Use `FrameLocator` with `page.frameLocator()`. It automatically waits and re-locates the frame. Unlike Selenium, you don't need to switch back to the main page — just use `page.locator()` for main page and `frameLocator()` for iframe elements.

**Q2: What is Shadow DOM and why does Playwright handle it better than Selenium?**
A: Shadow DOM is a hidden DOM that provides encapsulation for web components. Playwright automatically pierces Open Shadow DOM without needing JavaScript workarounds (which Selenium requires). Closed Shadow DOM cannot be accessed by any automation tool.

**Q3: What does frames.get(0) return?**
A: The main page! frames.get(0) is always the main page document. frames.get(1) is the first iFrame, get(2) is the second, etc.

---

# LECTURE 23 — Properties File

---

## 1. What is a Properties File?

**Definition:** A `.properties` file stores configuration data in **key=value** format. Instead of hardcoding values like URLs and passwords in your Java code, you store them in this file.

**Benefits:**
- No hardcoding in tests
- Easy to change environment (DEV → QA → PROD)
- CI/CD friendly
- One place to update all config

**Example `config.properties`:**
```properties
url=https://swautomationtester-dot.github.io/IshaShopping/
username=admin
password=admin123
browser=chrome
timeout=30000
```

---

## 2. Read from Properties File

```java
import java.io.FileInputStream;
import java.util.Properties;

public class ReadProperties {
    public static void main(String[] args) throws IOException {

        // Step 1: Point to the file
        FileInputStream fis = new FileInputStream(
            "src\\main\\resources\\properties\\config.properties");

        // Step 2: Create Properties object
        Properties prop = new Properties();

        // Step 3: Load the file
        prop.load(fis);

        // Step 4: Read values
        String username = prop.getProperty("username");   // "admin"
        String password = prop.getProperty("password");   // "admin123"
        String url = prop.getProperty("url");

        // Use in test
        page.navigate(url);
        page.fill("#username", username);
        page.fill("#password", password);
    }
}
```

---

## 3. Write to Properties File

```java
// Always load first to preserve existing properties!
FileInputStream fis = new FileInputStream("config.properties");
Properties prop = new Properties();
prop.load(fis);  // Read existing data first

// Update or add new value
prop.setProperty("password", "newPassword123");

// Save back to file
FileOutputStream fos = new FileOutputStream("config.properties");
prop.store(fos, "Updated Password");  // Second param is a comment
```

**⚠️ IMPORTANT:** If you call `setProperty()` and `store()` WITHOUT `load()` first, ALL existing properties will be DELETED and only the new ones will be saved!

---

## 4. Interview Questions — Properties File

**Q1: Why use a properties file in automation frameworks?**
A: Properties files store configuration data (URLs, credentials, browser type, timeouts) separately from test code. This avoids hardcoding, enables easy environment switching, and makes the framework CI/CD friendly.

**Q2: What happens if you don't call `prop.load()` before writing?**
A: Only the newly added properties will be saved. All existing properties in the file will be deleted and lost.

---

# LECTURE 24 — Apache POI (Excel Operations)

---

## 1. What is Apache POI?

**Definition:** Apache POI is a Java library that allows you to read and write Microsoft Excel files from Java code. Used for Data-Driven Testing — reading test data from Excel.

**File Types:**

| Extension | Class to Use | Excel Version |
|---|---|---|
| `.xls` | `HSSFWorkbook` | Old (97-2003) |
| `.xlsx` | `XSSFWorkbook` | New (2007+) |

**Memory Trick:**
- H = Historic (old .xls)
- X = XML (new .xlsx)

---

## 2. Excel Hierarchy

```
Workbook (Excel File)
    └── Sheet (Tab/Page)
            └── Row (Horizontal line)
                    └── Cell (Individual box)
```

---

## 3. Maven Dependencies for POI

```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.5.1</version>
</dependency>

<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.5.1</version>
</dependency>
```

---

## 4. Read from Excel

```java
// Step 1: Open file
FileInputStream fis = new FileInputStream("src\\main\\resources\\excel\\TestData.xlsx");

// Step 2: Load workbook
Workbook workbook = new XSSFWorkbook(fis);

// Step 3: Get sheet
Sheet sheet = workbook.getSheet("Sheet1");  // By name

// Step 4: Get row (0-indexed!)
Row row = sheet.getRow(1);  // Row 0 = header, Row 1 = first data row

// Step 5: Get cells
Cell nameCell = row.getCell(0);   // First column
Cell cityCell = row.getCell(1);   // Second column

// Step 6: Print values
System.out.println("Name: " + nameCell.toString());
System.out.println("City: " + cityCell.toString());

// Step 7: Close
workbook.close();
fis.close();
```

---

## 5. Read ALL Rows from Excel (Data-Driven)

```java
FileInputStream fis = new FileInputStream("LoginData.xlsx");
Workbook workbook = new XSSFWorkbook(fis);
Sheet sheet = workbook.getSheet("Login");

int lastRow = sheet.getLastRowNum();  // Get last row index

// Start from 1 to skip header row
for (int i = 1; i <= lastRow; i++) {
    Row row = sheet.getRow(i);

    String username = row.getCell(0).toString();
    String password = row.getCell(1).toString();

    System.out.println(username + " / " + password);
    // Use in test: loginPage.login(username, password);
}

workbook.close();
fis.close();
```

---

## 6. Write to Excel

```java
// Create new workbook
Workbook workbook = new XSSFWorkbook();

// Create sheet
Sheet sheet = workbook.createSheet("Data");

// Create rows and cells
Row headerRow = sheet.createRow(0);
headerRow.createCell(0).setCellValue("Name");
headerRow.createCell(1).setCellValue("City");

Row dataRow = sheet.createRow(1);
dataRow.createCell(0).setCellValue("Sujal");
dataRow.createCell(1).setCellValue("Kolhapur");

// Save to file
FileOutputStream fos = new FileOutputStream("src\\main\\resources\\excel\\Output.xlsx");
workbook.write(fos);
workbook.close();
fos.close();
System.out.println("Excel file created successfully!");
```

---

## 7. Interview Questions — Apache POI

**Q1: What classes are used for .xls vs .xlsx files in Apache POI?**
A: `HSSFWorkbook` for `.xls` (old format). `XSSFWorkbook` for `.xlsx` (new format). The hierarchy goes: Workbook → Sheet → Row → Cell.

**Q2: Why do we start reading from row index 1 instead of 0?**
A: Row index 0 is typically the header row (Name, Password, etc.). Test data starts from row 1. We skip row 0 to avoid using header text as test data.

---

# LECTURE 24 (Continued) — Assertions in Playwright

---

## 1. What are Assertions?

**Definition:** Assertions check if the application behaved as expected. They compare the ACTUAL result with the EXPECTED result and determine if the test PASSES or FAILS.

**Import needed:**
```java
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
```

---

## 2. Page Assertions

```java
// Verify page title
assertThat(page).hasTitle("Dashboard");

// Verify exact URL
assertThat(page).hasURL("https://example.com/dashboard");

// Verify URL contains a pattern (regex)
assertThat(page).hasURL(Pattern.compile(".*dashboard.*"));
```

---

## 3. Locator Assertions (Most Used)

```java
// Visibility
assertThat(page.locator("#loginBtn")).isVisible();
assertThat(page.locator("#errorMsg")).isHidden();

// Text
assertThat(page.locator("h1")).hasText("Welcome");           // Exact text
assertThat(page.locator(".msg")).containsText("Success");    // Partial text

// Count
assertThat(page.locator(".product")).hasCount(5);

// Attribute
assertThat(page.locator("#email")).hasAttribute("placeholder", "Enter email");

// State
assertThat(page.locator("#submit")).isEnabled();
assertThat(page.locator("#submit")).isDisabled();
assertThat(page.locator("#name")).isEditable();
assertThat(page.locator("#agree")).isChecked();

// Input value
assertThat(page.locator("#username")).hasValue("admin");

// CSS class
assertThat(page.locator("#submit")).hasClass("btn-primary");

// Negative assertions using .not()
assertThat(page.locator("#error")).not().isVisible();
assertThat(page.locator(".row")).not().hasCount(0);
```

---

## 4. Playwright Assertions are Hard Assertions

**Definition:**
- **Hard Assertion:** If it fails, test STOPS immediately. No more steps run.
- Playwright's built-in `assertThat()` = Hard assertion

**For Soft Assertions in Playwright:** Use TestNG's `SoftAssert` (covered in TestNG section).

---

## 5. Web Tables Handling

```java
// Get number of rows
Locator rows = page.locator("#users tbody tr");
int rowCount = rows.count();

// Read all data
for (int i = 0; i < rows.count(); i++) {
    Locator cells = rows.nth(i).locator("td");
    for (int j = 0; j < cells.count(); j++) {
        System.out.print(cells.nth(j).textContent() + " ");
    }
    System.out.println();
}

// Get specific cell (Row 2, Column 3)
String value = page.locator(
    "#users tbody tr:nth-child(2) td:nth-child(3)").textContent();

// Find row by text and click its button
Locator row = page.locator("#users tbody tr",
    new Page.LocatorOptions().setHasText("Kumar"));
row.locator("text=Edit").click();

// Read all values from one column (column 2)
List<String> names = page.locator(
    "#users tbody tr td:nth-child(2)").allTextContents();

// Verify value exists in table
assertThat(page.locator("#users tbody tr")).containsText("Ravi");

// Reusable method to get any cell
public String getCellValue(String tableSelector, int row, int col) {
    return page.locator(tableSelector + 
        " tbody tr:nth-child(" + row + ") td:nth-child(" + col + ")")
        .textContent();
}
```

---

## 6. Interview Questions — Assertions

**Q1: What is the difference between Hard and Soft assertions?**
A: Hard assertions stop test execution immediately when they fail. Soft assertions (TestNG `SoftAssert`) continue execution after failure and report all failures together at `assertAll()`.

**Q2: What is auto-waiting in Playwright assertions?**
A: Playwright assertions automatically wait until the expected condition is met or the timeout is reached. You don't need to add explicit waits before assertions.

---

# LECTURE 25 — Extent Reports

---

## 1. What is Extent Reports?

**Definition:** Extent Reports generates beautiful HTML test execution reports that show PASS/FAIL status, screenshots, logs, charts, and summary — much better than plain console output.

**What it shows:**
- Test case names and status (PASS/FAIL/SKIP)
- Step-by-step execution logs
- Failure screenshots
- Execution time
- Categories (Smoke/Regression)
- System info (OS, Browser, Tester)
- Dashboard charts

---

## 2. Maven Dependency

```xml
<dependency>
    <groupId>com.aventstack</groupId>
    <artifactId>extentreports</artifactId>
    <version>5.1.1</version>
</dependency>
```

---

## 3. Three Core Components

**ExtentSparkReporter** → Creates the HTML report file
**ExtentReports** → Manages all test results
**ExtentTest** → Represents one test case

```java
// 1. Create HTML reporter (defines output file)
ExtentSparkReporter reporter = new ExtentSparkReporter("reports/report.html");

// 2. Configure report appearance
reporter.config().setDocumentTitle("Test Report");
reporter.config().setReportName("Login Validation");
reporter.config().setTheme(Theme.DARK);

// 3. Create main reports manager
ExtentReports extent = new ExtentReports();

// 4. Connect reporter to extent
extent.attachReporter(reporter);

// 5. Add system info
extent.setSystemInfo("Application", "Swag Labs");
extent.setSystemInfo("Browser", "Chrome");
extent.setSystemInfo("Environment", "QA");
extent.setSystemInfo("OS", System.getProperty("os.name"));
extent.setSystemInfo("Tester", "Sujal");

// 6. Create a test entry
ExtentTest test = extent.createTest("Login Test")
    .assignAuthor("Sujal Khot")
    .assignCategory("Smoke")
    .assignDevice("Chrome");

// 7. Log execution steps
test.info("Browser launched");
test.info("Navigated to login page");
test.info("Entered username");
test.info("Clicked login button");

// 8. Log test result
test.pass("Login successful");
// OR
test.fail("Login failed - " + e.getMessage());

// 9. Attach screenshot
String screenshotPath = "screenshots/login.png";
page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
test.addScreenCaptureFromPath(screenshotPath);

// 10. MANDATORY - Generate final HTML report
extent.flush();
```

---

## 4. Log Status Types

| Method | Color | Meaning |
|---|---|---|
| `test.pass("message")` | 🟢 Green | Test step succeeded |
| `test.fail("message")` | 🔴 Red | Test step failed |
| `test.info("message")` | 🔵 Blue | Informational log |
| `test.warning("message")` | 🟠 Orange | Warning (not a failure) |
| `test.skip("message")` | 🟡 Yellow | Test skipped |

---

## 5. Best Practice Pattern with try-catch

```java
test = extent.createTest("Login Test").assignCategory("Smoke");
test.info("Browser launched");

try {
    page.navigate("https://example.com");
    test.info("Navigated to site");

    page.fill("#username", "admin");
    test.info("Entered username");

    page.click("#login");
    test.info("Clicked login");

    assertThat(page).hasURL("dashboard");
    test.pass("Login successful!");

} catch (Exception e) {
    // Capture screenshot on failure
    String path = "screenshots/failure_" + System.currentTimeMillis() + ".png";
    page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)));
    test.addScreenCaptureFromPath(path);
    test.fail("Test failed: " + e.getMessage());
    throw e;  // Re-throw so TestNG marks the test as failed
}
```

---

## 6. ⚡ extent.flush() is MANDATORY

Without `extent.flush()`:
- HTML report may be empty
- Logs will not appear in report
- Report may not be saved to disk

Always call `extent.flush()` at the end of all tests (in `@AfterSuite`).

---

## 7. Interview Questions — Extent Reports

**Q1: What are the three core components of Extent Reports?**
A: `ExtentSparkReporter` creates the HTML file and defines its appearance. `ExtentReports` is the main manager that collects all test results and logs. `ExtentTest` represents one test case. They are connected using `extent.attachReporter(reporter)`.

**Q2: What happens if you forget `extent.flush()`?**
A: The HTML report is not generated or is empty. All logged information is lost. `flush()` is mandatory — it writes all collected data to the HTML file.

---

# LECTURE 26 — Page Object Model (POM)

---

## 1. What is POM?

**Definition:** Page Object Model is a design pattern where each web page of your application is represented as a separate Java class. Each class contains the page's locators and the actions you can perform on that page.

**Simple Rule:**
- `LoginPage.java` → Stores login form locators + login actions
- `DashboardPage.java` → Stores dashboard locators + dashboard actions
- `LoginTest.java` → Calls page methods and has assertions

---

## 2. Without POM vs With POM

**Without POM (Bad — everything in test):**
```java
// Test has locators + actions + assertions mixed together — messy!
page.fill("#username", "admin");
page.fill("#password", "admin123");
page.click("#loginBtn");
page.click("#menu");
page.click("#logout");
assertThat(page).hasURL("login");
```

**With POM (Good — clean separation):**
```java
LoginPage loginPage = new LoginPage(page);
DashboardPage dashboard = new DashboardPage(page);

loginPage.login("admin", "admin123");
dashboard.logout();

assertThat(page).hasURL("login");  // Only assertion in test
```

---

## 3. Page Class Structure

```java
public class LoginPage {

    private Page page;  // Playwright Page object

    // Locators stored as private fields
    private Locator txtUsername;
    private Locator txtPassword;
    private Locator btnLogin;

    // Constructor — receives the Page object
    public LoginPage(Page page) {
        this.page = page;
        // Initialize locators here
        txtUsername = page.locator("#username");
        txtPassword = page.locator("#password");
        btnLogin = page.locator("#loginBtn");
    }

    // Individual action methods
    public void enterUsername(String username) {
        txtUsername.fill(username);
    }

    public void enterPassword(String password) {
        txtPassword.fill(password);
    }

    public void clickLogin() {
        btnLogin.click();
    }

    // Business method (combines actions)
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // Verification method (can return value for assertion)
    public boolean isErrorVisible() {
        return page.locator(".error-message").isVisible();
    }
}
```

---

## 4. Test Class Structure

```java
public class LoginTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    @Test
    public void testSuccessfulLogin() {
        // Create page object
        LoginPage loginPage = new LoginPage(page);

        // Navigate (can be in page class too)
        page.navigate("https://example.com");

        // Call page method (not locators directly!)
        loginPage.login("admin", "admin123");

        // Assertions only here
        assertThat(page).hasURL("https://example.com/dashboard");
    }

    @AfterMethod
    public void teardown() {
        browser.close();
        playwright.close();
    }
}
```

---

## 5. Recommended POM Framework Structure

```
src/
├── main/java/
│   ├── base/
│   │   └── BasePage.java        ← Common methods (click, fill, scroll)
│   ├── pages/
│   │   ├── LoginPage.java       ← Login page locators + actions
│   │   ├── DashboardPage.java   ← Dashboard locators + actions
│   │   └── ProductPage.java     ← Product page locators + actions
│   └── utils/
│       ├── BrowserFactory.java  ← Browser setup
│       ├── ConfigManager.java   ← Read properties file
│       └── ExcelUtils.java      ← Read Excel test data
│
├── main/resources/
│   └── properties/
│       └── config.properties    ← URL, browser, credentials
│
└── test/java/
    └── tests/
        ├── LoginTest.java       ← Test assertions + flow
        └── CartTest.java        ← Cart test assertions
```

---

## 6. Page Class Responsibilities

| POM Class | Should Contain | Should NOT Contain |
|---|---|---|
| Page Class | Locators, Actions, Page Methods | Assertions, @Test, Test Logic |
| Test Class | Assertions, Test Scenarios | Locators, Raw Actions |
| Utils | Helper methods | Business logic |

---

## 7. POM Benefits

| Benefit | Explanation |
|---|---|
| Reusability | `loginPage.login()` used in 50 tests |
| Easy Maintenance | If `#loginBtn` changes to `#signIn`, change only `LoginPage.java` |
| Readability | Test reads like English — `loginPage.login("admin", "pass")` |
| No Duplication | Locators defined once, used everywhere |
| Team Friendly | BA can understand tests without Java knowledge |

---

## 8. Interview Questions — POM

**Q1: What is Page Object Model and why do we use it?**
A: POM is a design pattern where each web page is a separate Java class with its locators and actions. Tests only call page methods and contain assertions. This avoids code duplication, makes maintenance easy (one locator change = one place to update), and makes tests readable.

**Q2: What should a page class NOT contain?**
A: Page classes should NOT contain assertions (`assertThat()`), `@Test` methods, or test scenarios. These belong in test classes. Page classes should only have locators and reusable action methods.

**Q3: Why do page classes have a constructor that takes `Page page`?**
A: The Page object from Playwright is what allows you to interact with the browser. By passing it through the constructor, the page class can use it to define locators and perform actions. All page classes share the same Page object (same browser tab).

---

# LECTURE 27 & 28 — TestNG Framework

---

## 1. What is TestNG?

**Definition:** TestNG (Testing Next Generation) is a Java testing framework that helps you organize, run, and report your automated tests. In Playwright automation, TestNG acts as the test runner.

**Without TestNG:**
- Run `main()` methods manually one by one
- No automatic reports
- No grouping or parallel execution
- Not suitable for large projects

**With TestNG:**
- One-click execution of all tests
- Automatic reports generated
- Groups (Smoke/Regression)
- Priorities, dependencies
- Parallel execution
- Retry failed tests

---

## 2. TestNG Annotations (Most Important)

| Annotation | Runs When | How Many Times |
|---|---|---|
| `@BeforeSuite` | Before all tests in suite | Once |
| `@AfterSuite` | After all tests in suite | Once |
| `@BeforeClass` | Before all tests in class | Once per class |
| `@AfterClass` | After all tests in class | Once per class |
| `@BeforeMethod` | Before EACH test method | Before every `@Test` |
| `@AfterMethod` | After EACH test method | After every `@Test` |
| `@Test` | Actual test method | Each test |

**Execution Order:**
```
@BeforeSuite → @BeforeClass → @BeforeMethod → @Test → @AfterMethod → @AfterClass → @AfterSuite
```

---

## 3. Playwright + TestNG Setup Pattern

```java
public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeClass   // Browser launch — once per class
    public void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeMethod  // New page — before every test
    public void createPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterMethod(alwaysRun = true)  // Always close page
    public void closePage() {
        context.close();
    }

    @AfterClass(alwaysRun = true)  // Always close browser
    public void closeBrowser() {
        browser.close();
        playwright.close();
    }
}
```

**What `alwaysRun = true` does:** Forces the method to run even if the test before it failed. Ensures cleanup always happens — browser closes properly, reports get generated.

---

## 4. TestNG Features

### Priority

```java
@Test(priority = 0)   // Runs first
public void launchBrowser() {}

@Test(priority = 1)   // Runs second
public void login() {}

@Test(priority = 2)   // Runs third
public void addToCart() {}
```

**Rule:** Lower priority number = runs first. Default priority = 0.

### Dependency

```java
@Test
public void login() {}

@Test(dependsOnMethods = {"login"})   // Runs ONLY if login() passes
public void dashboard() {}

@Test(dependsOnMethods = {"dashboard"})  // Runs only if dashboard() passes
public void checkout() {}
```

If `login()` fails → `dashboard()` is SKIPPED.

### Groups

```java
@Test(groups = {"Smoke"})
public void loginTest() {}

@Test(groups = {"Regression"})
public void paymentTest() {}

@Test(groups = {"Smoke", "Regression"})  // Belongs to multiple groups
public void homePageTest() {}

@Test(enabled = false)  // Skip this test
public void brokenTest() {}
```

**testng.xml to run specific group:**
```xml
<groups>
    <run>
        <include name="Smoke"/>
    </run>
</groups>
```

### Parameters (from testng.xml)

```xml
<!-- testng.xml -->
<parameter name="UserName" value="admin"/>
<parameter name="Password" value="admin123"/>
```

```java
@Test
@Parameters({"UserName", "Password"})
public void loginTest(String user, String pass) {
    loginPage.login(user, pass);
}
```

### DataProvider (Multiple Data Sets)

```java
@DataProvider(name = "loginData")
public Object[][] loginData() {
    return new Object[][] {
        {"admin", "admin123"},   // Data set 1
        {"user", "user123"}      // Data set 2
    };
}

@Test(dataProvider = "loginData")
public void loginTest(String username, String password) {
    // Runs TWICE — once for each data set
    loginPage.login(username, password);
}
```

---

## 5. Parallel Execution in testng.xml

```xml
<suite name="Suite" parallel="tests" thread-count="3">
    <test name="ChromeTests">
        <parameter name="browser" value="chrome"/>
        <classes><class name="tests.LoginTest"/></classes>
    </test>
    <test name="FirefoxTests">
        <parameter name="browser" value="firefox"/>
        <classes><class name="tests.LoginTest"/></classes>
    </test>
    <test name="EdgeTests">
        <parameter name="browser" value="edge"/>
        <classes><class name="tests.LoginTest"/></classes>
    </test>
</suite>
```

**Parallel modes:**

| Mode | What Runs in Parallel |
|---|---|
| `parallel="methods"` | Test methods simultaneously |
| `parallel="classes"` | Test classes simultaneously |
| `parallel="tests"` | `<test>` tags simultaneously (most common) |

---

## 6. Re-Run Failed Tests

**Method 1: Manual — testng-failed.xml**

After a run, TestNG creates `test-output/testng-failed.xml` with only failed tests.
Right-click → Run As → TestNG Suite → selects failed tests only.

**Method 2: Automatic — Retry Analyzer**

```java
// RetryAnalyzer class
public class RetryAnalyzer implements IRetryAnalyzer {
    int counter = 0;
    int retryLimit = 1;  // Retry 1 time

    @Override
    public boolean retry(ITestResult result) {
        if (counter < retryLimit) {
            counter++;
            return true;   // true = retry
        }
        return false;  // false = don't retry again
    }
}

// AnnotationTransformer — applies retry to ALL tests automatically
public class AnnotationTransformer implements IAnnotationTransformer {
    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
            Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
```

**Add to testng.xml:**
```xml
<listeners>
    <listener class-name="utils.AnnotationTransformer"/>
</listeners>
```

---

## 7. TestNG Assertions

### Hard Assertions (org.testng.Assert)

```java
Assert.assertEquals("Dashboard", "Dashboard");   // PASS
Assert.assertEquals("Admin", "Dashboard");        // FAIL — stops immediately

Assert.assertTrue(10 > 5);       // PASS
Assert.assertFalse(5 > 10);      // PASS
Assert.assertNotNull(page);       // PASS
Assert.assertNull(null);          // PASS
Assert.assertNotEquals("a", "b"); // PASS
Assert.fail("Forced fail");       // Always FAIL
```

### Soft Assertions (org.testng.asserts.SoftAssert)

```java
SoftAssert sa = new SoftAssert();

sa.assertEquals("Login", "Login");    // Check 1 — continues even if fails
sa.assertEquals("Wrong", "Right");    // Check 2 — FAILS but continues
sa.assertTrue(5 > 2);                 // Check 3 — PASS
sa.assertFalse(5 < 2);                // Check 4 — PASS

sa.assertAll();  // MANDATORY — reports all failures here
```

**Hard vs Soft:**

| Feature | Hard Assert | Soft Assert |
|---|---|---|
| On failure | Test STOPS | Test CONTINUES |
| Reports failures | At point of failure | At `assertAll()` |
| Class | `Assert` | `SoftAssert` |
| Use case | Critical checks | Validate multiple UI elements |

---

## 8. Interview Questions — TestNG

**Q1: What is the difference between @BeforeClass and @BeforeMethod in Playwright?**
A: `@BeforeClass` runs once per class — ideal for browser launch. `@BeforeMethod` runs before every `@Test` — ideal for creating a new BrowserContext and Page for each test (fresh state).

**Q2: What is `alwaysRun = true`?**
A: It forces a configuration method to execute even if a previous test or configuration method fails. Used with `@AfterMethod` and `@AfterSuite` to ensure cleanup always happens.

**Q3: What is the difference between `@Parameters` and `@DataProvider`?**
A: `@Parameters` passes one set of data from testng.xml — test runs once. `@DataProvider` passes multiple sets of data from Java code — test runs multiple times, once per data set.

**Q4: What is IRetryAnalyzer?**
A: It's a TestNG interface for automatically re-running failed tests. The `retry()` method returns `true` to retry, `false` to stop. Combined with `IAnnotationTransformer`, it applies automatically to all tests.

---

# LECTURE 30 — Cucumber BDD Framework

---

## 1. What is Cucumber?

**Definition:** Cucumber is a BDD (Behavior Driven Development) framework that lets you write tests in plain English that both technical and non-technical people can understand.

**What is BDD?** BDD = Behavior Driven Development. Tests are written from the user's perspective describing the behavior of the application.

---

## 2. Gherkin Language

Cucumber uses **Gherkin** — a simple English-like language with special keywords:

| Keyword | Meaning | Example |
|---|---|---|
| `Feature` | What functionality we're testing | `Feature: Login Functionality` |
| `Scenario` | One specific test case | `Scenario: Valid Login` |
| `Given` | Pre-condition (starting state) | `Given User is on Login Page` |
| `When` | Action performed | `When User enters username and password` |
| `Then` | Expected result | `Then User should see Dashboard` |
| `And` | Additional step | `And User clicks Login button` |
| `But` | Negative condition | `But Error message should not appear` |

---

## 3. Feature File Example

```gherkin
Feature: Login Functionality

  Scenario: Valid Login
    Given User is on Login Page
    When User enters username "admin"
    And User enters password "admin123"
    And User clicks Login button
    Then User should see Dashboard page
    
  Scenario: Invalid Login
    Given User is on Login Page
    When User enters username "wrong"
    And User enters password "wrong"
    And User clicks Login button
    Then User should see error message
```

---

## 4. Cucumber Framework Structure

```
src/test/java/
├── stepdefinitions/
│   └── LoginSteps.java      ← Java code for each Gherkin step
├── runners/
│   └── TestRunner.java      ← Runs the feature files
└── hooks/
    └── Hooks.java           ← @Before (setup) and @After (teardown)

src/test/resources/
└── features/
    └── login.feature        ← Gherkin scenarios
```

---

## 5. Step Definition Example

```java
// LoginSteps.java
public class LoginSteps {

    Page page;

    @Given("User is on Login Page")
    public void userIsOnLoginPage() {
        // Launch browser and navigate
        page.navigate("https://example.com");
    }

    @When("User enters username {string}")
    public void userEntersUsername(String username) {
        page.fill("#username", username);
    }

    @When("User enters password {string}")
    public void userEntersPassword(String password) {
        page.fill("#password", password);
    }

    @When("User clicks Login button")
    public void userClicksLogin() {
        page.click("#loginBtn");
    }

    @Then("User should see Dashboard page")
    public void userShouldSeeDashboard() {
        assertThat(page).hasURL("dashboard");
    }
}
```

---

## 6. TestRunner Example

```java
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "stepdefinitions",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber.html",
        "json:target/cucumber-reports/cucumber.json"
    }
)
public class TestRunner {}
```

**Plugin Options:**

| Plugin | Output | Use |
|---|---|---|
| `pretty` | Console | Formatted readable output |
| `html:path` | HTML file | Browser-friendly report |
| `json:path` | JSON file | For Allure/Extent integration |
| `junit:path` | XML file | For CI/CD (Jenkins) |

---

## 7. Interview Questions — Cucumber

**Q1: What is BDD and how is Cucumber related to it?**
A: BDD (Behavior Driven Development) focuses on application behavior from a user's perspective. Cucumber implements BDD by allowing tests to be written in Gherkin (plain English) that both developers and business people can understand.

**Q2: What is the difference between Feature file and Step Definitions?**
A: Feature file contains the Gherkin scenarios in plain English (what to test). Step Definition file contains the Java code that automates those scenarios (how to test).

---

# FINAL SECTION — COMPLETE QUICK REVISION

---

## ONE-DAY INTERVIEW REVISION

### Most Important 20% Concepts (Covers 80% of Interview Questions)

```
1. PLAYWRIGHT BASICS:
   - Created by Microsoft, 2020
   - WebSocket connection (vs HTTP in Selenium)
   - PBP: Playwright → Browser → Page
   - Default timeout: 30 seconds

2. LOCATORS (Priority Order):
   1. getByRole()   2. getByLabel()   3. getByPlaceholder()
   4. getByText()   5. getByTestId()  6. CSS   7. XPath

3. AUTO-WAIT vs THREAD.SLEEP():
   - Auto-wait: built into every action (click, fill, check)
   - Thread.sleep(): NEVER use — always waits full time
   - Explicit wait: for loaders, API data, text changes

4. FILL() vs TYPE():
   - fill() = clears + types (use for most cases)
   - type() = appends without clearing

5. ALERTS:
   - Register onDialog() BEFORE the click (critical!)
   - onceDialog() = one-time, safer than onDialog()
   - HTTP Auth = BrowserContext.setHttpCredentials()
   - Toast = use locator() directly

6. IFRAMES:
   - Use frameLocator() (recommended)
   - frames.get(0) = main page always
   - No need to switchTo().defaultContent() in Playwright

7. SHADOW DOM:
   - Playwright auto-pierces Open Shadow DOM
   - >> operator to traverse shadow boundaries
   - Closed Shadow DOM = cannot access

8. HARD vs SOFT ASSERTION:
   - Hard: test stops immediately on failure
   - Soft: continues, reports all failures at assertAll()

9. POM:
   - Page class = locators + actions (NO assertions)
   - Test class = assertions + test logic (NO locators)
   - Constructor receives Page object

10. TESTNG:
    - @BeforeClass = browser launch
    - @BeforeMethod = new page for each test
    - @AfterMethod(alwaysRun=true) = close page always
    - @DataProvider = multiple data sets
    - @Parameters = single data from XML

11. DROPDOWNS:
    - <select>: selectOption()
    - Auto-suggest: fill() + click suggestion
    - Custom/Bootstrap: click() + getByText()

12. FILE OPERATIONS:
    - Upload (input visible): setInputFiles()
    - Upload (popup): FileChooser
    - Download: waitForDownload() + saveAs()

13. EXTENT REPORTS:
    - 3 components: ExtentSparkReporter, ExtentReports, ExtentTest
    - extent.flush() is MANDATORY (generates HTML)
    - Screenshot on failure: test.addScreenCaptureFromPath()

14. TRACE VIEWER:
    - context.tracing().start() before test
    - context.tracing().stop() after test
    - Open: npx playwright show-trace trace.zip

15. VIDEO RECORDING:
    - Enable: setRecordVideoDir() on BrowserContext
    - Saved ONLY after context.close()
```

---

## COMMON MISTAKES TO AVOID

| Mistake | Correct Approach |
|---|---|
| Using `Thread.sleep()` | Use Playwright auto-wait or `locator.waitFor()` |
| `.btn primary` (space) | `.btn.primary` (no space = same element) |
| XPath index starting at 0 | XPath starts at 1: `(//input)[1]` |
| Playwright `.nth()` starting at 1 | `.nth(0)` is the first element |
| `initBroswer` (typo) | `initBrowser` (correct spelling) |
| Forgetting `extent.flush()` | Always call at end — report won't generate |
| Forgetting `context.close()` for video | Video file won't save without it |
| Registering `onDialog()` after click | Must register BEFORE the click |
| Using `..` XPath traversal | Use parent-child XPath for readability |
| No `prop.load()` before writing | Always load first or existing data is deleted |
| Using `ElementHandle` for dynamic elements | Use `Locator` — it re-checks DOM every time |

---

## INTERVIEW ANSWERS IN ONE LINE

| Question | One-Line Answer |
|---|---|
| What is Playwright? | Microsoft's 2020 WebSocket-based automation framework with built-in auto-waiting |
| Auto-wait? | Playwright automatically waits for elements to be visible, enabled, and stable before acting |
| POM? | Each web page = one Java class containing locators and actions (no assertions) |
| Hard vs Soft assertion? | Hard = stops on failure; Soft = continues, reports at `assertAll()` |
| iFrame handling? | Use `frameLocator()` — auto-waits and re-locates, no switchTo needed |
| Shadow DOM? | Playwright auto-pierces Open Shadow DOM; use `>>` for explicit traversal |
| Alert handling? | Register `onDialog()` BEFORE the action that triggers the alert |
| HTTP Auth popup? | Set credentials on BrowserContext using `.setHttpCredentials()` |
| fill() vs type()? | fill() clears and types; type() appends without clearing |
| BrowserContext? | Isolated browser session — like an incognito window |

---

*All 3 Parts Complete — Covers Lectures 0 to 30*
*Good luck with your interviews!*
