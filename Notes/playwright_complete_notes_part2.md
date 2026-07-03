# Playwright with Java — Complete Course Notes (Part 2)
### Lectures 14 to 21 | Locators, Alerts, JS, Actions, Screenshots

---

# LECTURE 14 — Browser Factory Design Pattern

---

## 1. What is Browser Factory?

**Definition:** Browser Factory is a design pattern where you write the browser launch code only ONCE in a separate class called `BrowserFactory`, and reuse it everywhere. No need to repeat browser setup in every test.

**Real-World Analogy:** A remote control factory — you describe what remote (browser) you need, the factory builds it and gives it to you ready to use.

**Why We Use It:**
- Avoid writing the same browser setup code in 50+ test files
- Easy to change browser settings in one place
- Follows OOP principles (Encapsulation)
- Supports multiple browsers from one method

---

## 2. Complete BrowserFactory Code (Corrected)

```java
package org.sujalkhot.day14;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;
import com.microsoft.playwright.*;

public class BrowserFactory {

    // private = hidden from outside (Encapsulation)
    // static = shared at class level, no object needed
    private static Playwright playwright;
    private static Browser browser;

    // Call this method to get a Page ready to use
    public static Page initBrowser(String browserName, boolean headless, boolean fullScreen) {

        // Step 1: Start Playwright
        playwright = Playwright.create();

        // Step 2: Get screen size (for Firefox/WebKit fullscreen)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        // Step 3: Set base options
        BrowserType.LaunchOptions option = new BrowserType.LaunchOptions().setHeadless(headless);

        // Step 4: Add --start-maximized ONLY for Chromium browsers
        if (fullScreen && (browserName.equalsIgnoreCase("Chromium")
                || browserName.equalsIgnoreCase("Chrome")
                || browserName.equalsIgnoreCase("edge"))) {
            option.setArgs(Arrays.asList("--start-maximized"));
        }

        // Step 5: Launch the right browser
        switch (browserName.toLowerCase()) {
            case "chromium":
                browser = playwright.chromium().launch(option);
                break;
            case "chrome":
                option.setChannel("chrome");
                browser = playwright.chromium().launch(option);
                break;
            case "edge":
                option.setChannel("edge");
                browser = playwright.chromium().launch(option);
                break;
            case "firefox":
                browser = playwright.firefox().launch(option);
                break;
            case "webkit":
                browser = playwright.webkit().launch(option);
                break;
            default:
                throw new IllegalArgumentException("Invalid Browser: " + browserName);
        }

        // Step 6: Create BrowserContext
        BrowserContext context;

        if (fullScreen && (browserName.equalsIgnoreCase("chromium")
                || browserName.equalsIgnoreCase("chrome")
                || browserName.equalsIgnoreCase("edge"))) {
            // null = let --start-maximized take effect
            context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));

        } else if (fullScreen && (browserName.equalsIgnoreCase("firefox")
                || browserName.equalsIgnoreCase("webkit"))) {
            // Firefox/WebKit: manually set viewport to screen size
            context = browser.newContext(
                new Browser.NewContextOptions().setViewportSize(width, height));

        } else {
            context = browser.newContext(new Browser.NewContextOptions());
        }

        // Step 7: Open page and return it
        return context.newPage();
    }

    public static void closeBrowser() {
        if (playwright != null) {
            playwright.close();   // Closes all browsers too
        }
    }
}
```

---

## 3. Key Points About BrowserFactory

| Point | Details |
|---|---|
| `private static` | Encapsulation — hidden from outside, shared at class level |
| `playwright.close()` | Closes Playwright AND all browsers. Always call this at end. |
| Firefox/WebKit | Do NOT support `--start-maximized`. Use `setViewportSize()` instead. |
| Chromium fullscreen | Use `--start-maximized` + `setViewportSize(null)` |
| `default` in switch | Throws error for invalid browser name — "fail fast" approach |

---

## 4. How to Use BrowserFactory in Tests

```java
// Get a Page object — browser is launched internally
Page page = BrowserFactory.initBrowser("chrome", false, true);

page.navigate("https://example.com");
// ... your test actions ...

BrowserFactory.closeBrowser();  // Always close at end
```

---

## 5. Interview Questions — BrowserFactory

**Q1: What is the Browser Factory design pattern?**
A: BrowserFactory centralizes browser creation logic in one class. Tests call `initBrowser()` to get a ready Page object without knowing browser setup details. This avoids code duplication and makes maintenance easy.

**Q2: Why do Firefox and WebKit not support `--start-maximized`?**
A: `--start-maximized` is a Chromium-specific argument. Firefox and WebKit have different rendering engines and don't accept Chromium flags. Instead, we set viewport size equal to screen dimensions.

**Q3: What is Encapsulation in BrowserFactory?**
A: The `playwright` and `browser` variables are declared `private` — they cannot be accessed or modified from outside the class. This is encapsulation — hiding internal state.

---

# LECTURE 15 — Locators in Playwright

---

## 1. What is a Locator?

**Definition:** A Locator is like the **GPS address** of a web element. It tells Playwright exactly which element to find and interact with on the page.

**Key Properties of Playwright Locators:**
- **Auto-wait:** Waits for element to be ready before acting
- **Auto-retry:** Retries if element is not immediately available
- **Re-find:** Re-finds the element before every action
- **Visibility check:** Ensures element is visible before clicking

---

## 2. All Locator Types with Examples

### Built-in Locators (Use These First — Recommended)

```java
// 1. Role Locator (BEST - ARIA accessibility)
page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).fill("admin");

// 2. Label Locator (for form fields with <label> tags)
page.getByLabel("Email").fill("user@example.com");

// 3. Placeholder Locator (when no ID or label)
page.getByPlaceholder("Enter username").fill("admin");

// 4. Text Locator (finds by visible text)
page.getByText("Login").click();

// 5. Alt Text Locator (for images)
page.getByAltText("Company Logo").isVisible();

// 6. Title Locator (tooltip/title attribute)
page.getByTitle("Close").click();

// 7. Test ID Locator (developer adds data-testid attribute)
page.getByTestId("login-btn").click();
```

### CSS Locators

```java
// By ID (#)
page.locator("#username").fill("admin");

// By Class (.)
page.locator(".login-btn").click();

// By Tag + Attribute
page.locator("input[name='email']").fill("test@test.com");

// By Type
page.locator("input[type='text']").fill("hello");

// CSS starts with, ends with, contains
page.locator("[id^='user']")   // ID starts with "user"
page.locator("[id$='name']")   // ID ends with "name"
page.locator("[id*='user']")   // ID contains "user"
```

### XPath Locators

```java
// By ID
page.locator("//input[@id='username']").fill("admin");

// By Text (exact)
page.locator("//button[text()='Login']").click();

// Partial attribute (contains)
page.locator("//input[contains(@id,'user')]").fill("admin");

// Partial text
page.locator("//button[contains(text(),'Log')]").click();

// Index-based (XPath index starts at 1!)
page.locator("(//input)[1]").fill("first input");

// By AND condition
page.locator("//input[@type='text' and @name='email']").fill("test");

// By OR condition
page.locator("//input[@type='file' or @id='fileinput']");

// Wildcard tag (use when tag name might change)
page.locator("//*[@type='file' or @id='fileinput']");
```

### Index-Based Locators

```java
// nth() uses 0-based index in Playwright!
page.locator("button").nth(0)    // First button
page.locator("button").nth(1)    // Second button
page.locator("button").first()   // First match
page.locator("button").last()    // Last match
```

---

## 3. Locator Priority (Use in This Order)

| Priority | Locator Type | Why |
|---|---|---|
| 1 | `getByRole()` | Accessibility-based, most stable |
| 2 | `getByLabel()` | Stable for form fields |
| 3 | `getByPlaceholder()` | Good fallback for inputs |
| 4 | `getByText()` | Human-readable |
| 5 | `getByTestId()` | Most stable in enterprise projects |
| 6 | CSS Selector | Flexible, widely used |
| 7 | XPath | Use only for complex cases |

---

## 4. CSS Selector Cheatsheet

| Selector | Example | Meaning |
|---|---|---|
| `#id` | `#username` | By ID ⚡ Most specific |
| `.class` | `.login-btn` | By class ⚡ |
| `tag[attr='val']` | `input[name='email']` | Tag + attribute ⚡ |
| `div input` | `div input` | Input anywhere inside div |
| `div > input` | `div > input` | Direct child input of div |
| `li:first-child` | `li:first-child` | First list item |
| `li:last-child` | `li:last-child` | Last list item |
| `li:nth-child(2)` | `li:nth-child(2)` | Second list item |
| `[id^='user']` | `[id^='user']` | ID starts with "user" |
| `[id$='name']` | `[id$='name']` | ID ends with "name" |
| `[id*='user']` | `[id*='user']` | ID contains "user" |

---

## 5. ⚡ CRITICAL: `.btn primary` vs `.btn.primary`

```java
// ❌ WRONG — Space means DESCENDANT selector
// Looks for element with class "primary" INSIDE element with class "btn"
page.locator(".btn primary").click();   // Will throw error!

// ✅ CORRECT — No space = element must have BOTH classes
page.locator(".btn.primary").click();   // Correct!
```

**Memory Trick:** Dot between class names = same element has both classes. Space = parent-child relationship.

---

## 6. XPath vs CSS Comparison

| Feature | CSS | XPath |
|---|---|---|
| Speed | Faster | Slower |
| Readability | Cleaner | Verbose |
| Go to parent | ❌ Cannot | ✅ Yes (`/..`) |
| Sibling access | Limited | ✅ Full |
| Text matching | Limited | ✅ `text()` and `contains()` |
| Recommended | Most cases | Complex DOM only |

---

## 7. Interview Questions — Locators

**Q1: What is the difference between absolute and relative XPath?**
A: Absolute XPath starts from the HTML root (`html/body/div/...`) and breaks easily if the page structure changes. Relative XPath starts directly from the target element using `//` and is more stable. Always use relative.

**Q2: What is the XPath index vs Playwright `.nth()` index?**
A: XPath index starts at **1** — `(//input)[1]` is the first input. Playwright's `.nth()` starts at **0** — `.nth(0)` is the first element. This is a common interview trap!

**Q3: Why is `getByRole()` the most preferred locator?**
A: It's based on ARIA accessibility roles — the same way screen readers identify elements. It's most stable because it finds elements the way real users see them, not by internal technical attributes.

**Q4: What does `data-testid` mean?**
A: It's a custom HTML attribute added by developers specifically for automation testing. It doesn't change with UI redesigns, making it the most stable locator for enterprise frameworks.

---

# LECTURE 16 — Role Locators & Element Getter Methods

---

## 1. Role Locator Deep Dive

**What ARIA Means:** ARIA = Accessible Rich Internet Applications. It's a standard that makes web pages readable for people with disabilities. Screen readers use ARIA roles to announce what elements are.

**How Playwright finds the element name:**
1. `aria-label` attribute
2. `aria-labelledby` reference
3. Associated `<label>` tag
4. Inner text of the element
5. Placeholder attribute

```java
// Always use setName() to be specific
page.getByRole(AriaRole.TEXTBOX, 
    new Page.GetByRoleOptions().setName("Username")).fill("admin");

page.getByRole(AriaRole.BUTTON, 
    new Page.GetByRoleOptions().setName("Login")).click();

page.getByRole(AriaRole.LINK, 
    new Page.GetByRoleOptions().setName("Logout")).click();

page.getByRole(AriaRole.HEADING, 
    new Page.GetByRoleOptions().setName("Dashboard"));

page.getByRole(AriaRole.CHECKBOX, 
    new Page.GetByRoleOptions().setName("Remember me")).check();
```

---

## 2. Handling Table Rows with Role Locator

```java
// Find the row containing "John" and click its Edit button
Locator row = page.getByRole(AriaRole.ROW, 
    new Page.GetByRoleOptions().setName("John"));

row.getByRole(AriaRole.BUTTON, 
    new Page.GetByRoleOptions().setName("Edit")).click();
```

---

## 3. Handling Duplicate Elements Problem

**Problem:** When multiple elements have the same role + name, Playwright throws an error.

**Solutions (in order of preference):**

```java
// Solution 1: Use data-testid (BEST)
page.getByTestId("login-password").fill("123456");

// Solution 2: Be more specific with role + name
page.getByRole(AriaRole.TEXTBOX, 
    new Page.GetByRoleOptions().setName("Password")).fill("12345");

// Solution 3: Use ancestor XPath for product-specific buttons
page.locator("//*[@alt='Isha Test T-Shirt']//ancestor::div//button[@class='btn primary add-btn']").click();

// Solution 4: Use nth() ONLY if UI is fixed (NOT recommended for frameworks)
page.getByPlaceholder("Password").nth(1).fill("1345");
```

---

## 4. Page-Level Getter Methods

```java
// Get browser tab title
String title = page.title();

// Get current URL
String url = page.url();

// Get full HTML of the page
String html = page.content();

// Get current viewport size
Page.ViewportSize size = page.viewportSize();

// Get BrowserContext object
BrowserContext ctx = page.context();
```

---

## 5. Element-Level Getter Methods

```java
// Get ALL text including hidden text and spaces
String text = page.locator("#element").textContent();

// Get VISIBLE text only (better for user-facing content)
String visibleText = page.locator("#element").innerText();

// Get HTML inside the element
String html = page.locator("#element").innerHTML();

// Get a specific attribute value
String id = page.locator("#element").getAttribute("id");
String href = page.locator("a").getAttribute("href");

// Get current value of an input field
String value = page.getByPlaceholder("username").inputValue();

// Count how many elements match
int count = page.locator(".product").count();
```

---

## 6. textContent() vs innerText() Comparison

| Feature | `textContent()` | `innerText()` |
|---|---|---|
| Includes hidden text | ✅ Yes | ❌ No |
| Includes extra spaces | ✅ Yes | ❌ No |
| What it returns | All raw text | Only visible text |
| When to use | Reading raw DOM content | Checking what user sees |

**Best Practice:** Always use `.trim()` after `textContent()` in real projects:

```java
// Remove leading/trailing spaces
String label = page.locator("//label[1]").textContent().trim();
```

---

## 7. Interview Questions — Role & Getters

**Q1: What is ARIA and why does it matter for Playwright?**
A: ARIA (Accessible Rich Internet Applications) adds accessibility metadata to HTML. Playwright's `getByRole()` uses ARIA roles to find elements — the same way screen readers identify them. This makes tests more aligned with real user experience.

**Q2: What does `locator.count()` return if no elements are found?**
A: It returns `0`. Unlike `.nth()` or `.click()`, it does NOT throw an error, making it safe to check if elements exist.

---

# LECTURE 17 — Navigation, Waits, and Form Actions

---

## 1. Navigation Commands

```java
// Open a URL
page.navigate("https://example.com");

// Navigate with custom wait strategy
page.navigate("https://example.com",
    new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

// Refresh the page
page.reload();

// Go back
page.goBack();

// Go forward
page.goForward();

// Wait for navigation after clicking a link
page.waitForNavigation(() -> {
    page.locator("text=Login").click();  // Action that causes navigation
});
```

---

## 2. Page Load States

| State | Waits For | Speed | Use When |
|---|---|---|---|
| `DOMCONTENTLOADED` | HTML parsed only | Fastest | Page structure is enough |
| `LOAD` | HTML + images + CSS | Medium | Normal pages |
| `NETWORKIDLE` | All API calls done | Slowest | React/Angular/Vue pages |

```java
page.waitForLoadState(LoadState.DOMCONTENTLOADED);  // Fast
page.waitForLoadState(LoadState.LOAD);              // Normal
page.waitForLoadState(LoadState.NETWORKIDLE);       // API pages
```

**Memory Trick:**
```
DOMCONTENTLOADED = HTML skeleton loaded
LOAD = Full body loaded (HTML + images + CSS)
NETWORKIDLE = All data loaded (API calls done)
```

---

## 3. Auto-Waiting vs Explicit Waits

### Auto-Waiting (Built-in — Use First)

Playwright automatically checks before EVERY action:
- ✅ Element exists in DOM
- ✅ Element is visible
- ✅ Element is enabled
- ✅ Element is stable (not animating)

```java
// These all have auto-waiting built in:
page.click("#submit");           // Auto-waits
page.fill("#username", "admin"); // Auto-waits
page.check("#checkbox");         // Auto-waits
page.hover(".menu");             // Auto-waits
page.selectOption("#country", "India"); // Auto-waits
```

### Explicit Waits (Use When Auto-Wait is Not Enough)

```java
// Wait for element to appear
page.waitForSelector("#success-message");

// Wait with custom timeout (overrides 30s default)
page.waitForSelector("#success-message",
    new Page.WaitForSelectorOptions().setTimeout(5000));

// Wait for element to become visible
page.locator("#submit")
    .waitFor(new Locator.WaitForOptions()
        .setState(WaitForSelectorState.VISIBLE));

// Wait for loader to disappear
page.waitForSelector(".spinner",
    new Page.WaitForSelectorOptions()
        .setState(WaitForSelectorState.HIDDEN));

// Wait for text to change
page.waitForFunction(
    "() => document.querySelector('#status').innerText === 'Completed'");
```

---

## 4. When to Use Auto-Wait vs Explicit Wait

| Scenario | Best Choice |
|---|---|
| Button click | ✅ Auto-Wait |
| Fill input field | ✅ Auto-Wait |
| Check checkbox | ✅ Auto-Wait |
| Select dropdown | ✅ Auto-Wait |
| Wait for loader to disappear | ⚡ Explicit (HIDDEN state) |
| API response loaded | ⚡ Explicit (NETWORKIDLE) |
| Text changes dynamically | ⚡ Explicit (waitForFunction) |

---

## 5. Why NOT Thread.sleep()

```java
// ❌ NEVER use this in Playwright
Thread.sleep(5000);
```

**Problems:**
- Always waits full 5 seconds even if element loads in 1 second
- Makes tests unnecessarily slow
- If element takes 6 seconds, test still fails
- Not reliable for dynamic content

```java
// ✅ Use Playwright's built-in waiting instead
page.locator("#submit").waitFor();  // Waits until ready
```

---

## 6. Global Timeout Settings

```java
// Set timeout for ALL actions (click, fill, check, etc.)
page.setDefaultTimeout(10000);  // 10 seconds

// Set timeout ONLY for page navigation
page.setDefaultNavigationTimeout(15000);  // 15 seconds
```

---

## 7. fill() vs type()

| Feature | `fill()` | `type()` |
|---|---|---|
| Clears existing text | ✅ Yes | ❌ No |
| Appends to existing | ❌ No | ✅ Yes |
| Speed | Fast (one operation) | Slower (char by char) |
| Use case | Most automation | Special typing behavior |

```java
// fill() — clears field and types new value
page.fill("#username", "admin");
page.locator("#username").fill("admin");

// Clear a field
page.fill("#email", "");

// type() — appends without clearing
// If field has "Hello" and you type " World" → "Hello World"
page.type("#search", " World");

// Get current value of input
String value = page.inputValue("#username");

// Clear using clear()
page.locator("#username").clear();
```

---

## 8. Radio Buttons and Checkboxes

```java
// Select a radio button
page.check("input[value='male']");
page.check("#gender-male");

// Verify radio button is selected
boolean isSelected = page.isChecked("#gender-male");

// Checkbox check
page.getByRole(AriaRole.CHECKBOX, 
    new Page.GetByRoleOptions().setName("Remember me")).check();

// Uncheck
page.uncheck("#remember");
```

---

## 9. Date Picker

```java
// Fill date picker directly (format: YYYY-MM-DD)
page.fill("#datePicker", "2025-10-10");

// Using JavaScript if the field is readonly
page.evaluate("document.querySelector('#datePicker').value = '2026-01-15'");
```

---

## 10. Interview Questions — Navigation & Waits

**Q1: What is the difference between `DOMCONTENTLOADED`, `LOAD`, and `NETWORKIDLE`?**
A: `DOMCONTENTLOADED` waits only for HTML parsing. `LOAD` waits for HTML + images + CSS. `NETWORKIDLE` waits until all network requests stop. Use `LOAD` for most cases and `NETWORKIDLE` for API-driven pages.

**Q2: Why is `fill()` preferred over `type()`?**
A: `fill()` clears existing text and enters new value in one fast operation. `type()` simulates keyboard typing character by character without clearing. Use `fill()` for most scenarios.

**Q3: What is the Retry Mechanism in Playwright?**
A: Playwright automatically retries actions until the element becomes ready or the timeout (default 30s) is reached. No manual retry loops needed. Locators re-check the DOM on every retry.

---

# LECTURE 18 — File Upload, File Download & Dropdowns

---

## 1. File Upload

### Method 1: Direct Input File Upload (Most Common)

```java
// When <input type="file"> is available
page.locator("input[type='file']")
    .setInputFiles(Paths.get("src\\test\\resources\\files\\test.pdf"));

// OR using page directly
page.setInputFiles("#uploadFile",
    Paths.get("C:/files/resume.pdf"));
```

### Method 2: Multiple File Upload

```java
page.setInputFiles("#uploadFile",
    new Path[] {
        Paths.get("src\\test\\resources\\files\\file1.pdf"),
        Paths.get("src\\test\\resources\\files\\file2.pdf")
    });
```

### Method 3: Clear Uploaded File

```java
page.setInputFiles("#uploadFile", new Path[0]);
```

### Method 4: FileChooser (When Button Opens File Picker Popup)

```java
// When clicking a button opens the system file picker dialog
FileChooser fileChooser = page.waitForFileChooser(() -> {
    page.click("#uploadBtn");  // This click opens the dialog
});

fileChooser.setFiles(Paths.get("C:/files/resume.pdf"));
```

**When to use which:**
- `setInputFiles()` → When `<input type="file">` is visible in DOM
- `FileChooser` → When a button opens the OS file picker popup

---

## 2. File Download

```java
// Download triggered by clicking a button
Download download = page.waitForDownload(() -> {
    page.locator("text=Download").click();  // Action that starts download
});

// Save the file
Path downloadedFile = Paths.get("downloads", download.suggestedFilename());
download.saveAs(downloadedFile);

// Print the save location
System.out.println("File saved at: " + downloadedFile.toAbsolutePath());

// Download without clicking (using direct URL)
Download download = page.waitForDownload(() -> {
    page.evaluate("window.open('https://example.com/file.pdf')");
});
download.saveAs(Path.of("src/test/resources/file.pdf"));
```

**Useful Download Methods:**

| Method | What It Returns |
|---|---|
| `download.path()` | Temporary file path |
| `download.suggestedFilename()` | Original filename from server |
| `download.saveAs(path)` | Save to specific location |

---

## 3. Dropdowns — 3 Types

### Type 1: HTML `<select>` Dropdown (Standard)

```java
// Select by value attribute
page.selectOption("#country", "IN");

// Select by visible text (label)
page.selectOption("#country", new SelectOption().setLabel("India"));

// Select by index (0-based)
page.selectOption("#country", new SelectOption().setIndex(2));

// Multiple select
page.selectOption("#skills", new String[]{"java", "playwright", "api"});

// Get selected value
String selected = page.locator("#country").inputValue();

// Get all options
List<String> allOptions = page.locator("#country option").allTextContents();
```

### Type 2: Auto-Suggest Dropdown (Google-style)

```java
// Type text and wait for suggestions
page.fill("#search", "jav");

// Click the correct suggestion
Locator suggestions = page.locator(".suggestions div");
suggestions
    .filter(new Locator.FilterOptions().setHasText("JavaScript"))
    .first()
    .click();

// OR loop through and find the right one
for (int i = 0; i < suggestions.count(); i++) {
    if (suggestions.nth(i).innerText().equals("Java")) {
        suggestions.nth(i).click();
        break;
    }
}
```

### Type 3: Custom/Bootstrap Dropdown (div-based)

```java
// Step 1: Click to open dropdown
page.locator("button#cityDropdown").click();

// Step 2: Click the option
page.getByText("Chennai", new Page.GetByTextOptions().setExact(true)).click();

// OR safer approach — search inside dropdown only
Locator dropdown = page.locator(".options");
dropdown.getByText("Bangalore").click();
```

**How to Identify Dropdown Type:**
- See `<select>` tag → HTML dropdown → use `selectOption()`
- See `<input>` + suggestions div → Auto-suggest → `fill()` + click
- See `<div>` or `<ul>` + options → Custom → `click()` + `getByText()`

---

## 4. Interview Questions — File & Dropdown

**Q1: Difference between `setInputFiles()` and `FileChooser`?**
A: `setInputFiles()` works when the file input element is directly in the HTML DOM. `FileChooser` is used when clicking a button opens the OS file picker dialog — Playwright captures the dialog and uploads the file programmatically.

**Q2: Why does Playwright not need browser profile for downloads?**
A: Playwright handles downloads programmatically using `waitForDownload()`. It captures the download event, holds the file temporarily, and lets you save it anywhere using `saveAs()`. No browser profile needed.

**Q3: How do you handle 3 types of dropdowns in Playwright?**
A: HTML `<select>` → `selectOption()`. Auto-suggest dropdowns → `fill()` + click suggestion. Custom/Bootstrap → `click()` to open + `getByText()` to select option.

---

# LECTURE 19 — Multiple Elements & Alert Handling

---

## 1. Locator vs Selenium's findElement()

| Feature | Selenium | Playwright |
|---|---|---|
| Single element | `findElement()` — immediate search | `locator()` — deferred search |
| Multiple elements | `findElements()` — returns `List<WebElement>` | `locator().all()` — returns `List<Locator>` |
| Not found | Throws `NoSuchElementException` | Creates locator (no error) |
| Auto-wait | ❌ No | ✅ Yes |

```java
// Playwright creates a REFERENCE first, searches when action is performed
Locator username = page.locator("#username");
username.fill("venkat");  // Searches and fills here
```

---

## 2. Multiple Elements

```java
// Get all matching elements
List<Locator> products = page.locator(".prod-title").all();
for (Locator product : products) {
    System.out.println(product.textContent());
}

// Get all text at once (faster — no loop needed)
List<String> titles = page.locator(".prod-title").allTextContents();
System.out.println(titles);  // [Laptop, Phone, Headphones]
```

**When to use which:**

| Situation | Method |
|---|---|
| Need to click each element | `locator().all()` |
| Need only text values | `allTextContents()` |
| Specific element by position | `locator().nth(0)` |
| First match | `locator().first()` |
| Last match | `locator().last()` |

---

## 3. Alert Handling — All 5 Types

| Alert Type | Part of DOM? | Method | Notes |
|---|---|---|---|
| Simple Alert | ❌ No | `onDialog()` + `accept()` | OK button only |
| Confirmation Alert | ❌ No | `onDialog()` + `accept()` or `dismiss()` | OK + Cancel |
| Prompt Alert | ❌ No | `onDialog()` + `accept("text")` | Text input + OK |
| Modern Modal/Toast | ✅ Yes | `locator()` + `click()` | Regular HTML element |
| HTTP Auth Popup | ❌ No | `BrowserContext credentials` | Set before navigation |

### Simple Alert (OK only)

```java
// MUST register listener BEFORE clicking (CRITICAL!)
page.onceDialog(dialog -> {
    System.out.println("Message: " + dialog.message());
    dialog.accept();  // Click OK
});
page.locator("#nativeAlert").click();
```

### Confirmation Alert (OK + Cancel)

```java
// Accept
page.onceDialog(dialog -> {
    dialog.accept();   // Click OK
});
page.click("#nativeConfirm");

// Dismiss
page.onceDialog(dialog -> {
    dialog.dismiss();  // Click Cancel
});
page.click("#nativeConfirm");
```

### Prompt Alert (Text + OK)

```java
page.onDialog(dialog -> {
    dialog.accept("Playwright Java");  // Types text and clicks OK
});
page.click("#nativePrompt");
```

### Modern Modal/Toast

```java
// Toast elements ARE in DOM — use locators directly
page.locator("#modernConfirm").click();
page.locator("#modalOk").click();

// Read toast message
String toastMessage = page.locator("//*[@id='toastContainer']/div").innerText();
System.out.println("Toast: " + toastMessage);
```

---

## 4. ⚡ CRITICAL: Register Listener BEFORE Click

```java
// ✅ CORRECT — Register first, then click
page.onceDialog(dialog -> { dialog.accept(); });
page.click("#alertButton");

// ❌ WRONG — Browser blocks when alert appears, listener never registers
page.click("#alertButton");   // Alert appears, browser freezes!
page.onceDialog(dialog -> { dialog.accept(); });  // Too late!
```

**Why?** JavaScript alerts BLOCK the browser instantly. If there's no listener ready, the test hangs and times out.

---

## 5. onDialog() vs onceDialog()

| Feature | `page.onDialog()` | `page.onceDialog()` |
|---|---|---|
| Type | Permanent listener | One-time listener |
| Handles | ALL dialogs | Only NEXT dialog |
| Auto-removed | ❌ No | ✅ Yes |
| Safety | Medium | High ✅ |
| Use for | Global handling | Specific test |

---

## 6. HTTP Authentication Popup

```java
// ❌ NOT recommended — credentials visible in URL
page.navigate("https://admin:admin@the-internet.herokuapp.com/basic_auth");

// ✅ RECOMMENDED — Use BrowserContext credentials
BrowserContext context = browser.newContext(
    new Browser.NewContextOptions()
        .setViewportSize(null)
        .setHttpCredentials("admin", "admin")  // Username, Password
);
Page page = context.newPage();
page.navigate("https://the-internet.herokuapp.com/basic_auth");
```

**Why not the URL method?** Credentials in URL appear in logs, browser history, and reports — security risk!

---

## 7. How to Find Toast Message (F8 Freeze Trick)

1. Open DevTools (F12)
2. Go to **Sources** tab
3. Expand **Event Listener Breakpoints**
4. Enable **Keyboard** (or Mouse events)
5. Perform the action that shows the toast
6. Press **F8** to freeze/pause the page
7. Go to **Elements** tab and inspect the toast element
8. Find its class/ID
9. Press Resume ▶ button to unfreeze

---

## 8. Interview Questions — Alerts

**Q1: Why must `onDialog()` be registered before the click?**
A: JavaScript alerts block the browser immediately when they appear. No further actions can execute. Playwright must be listening BEFORE the alert appears, otherwise the test hangs and times out.

**Q2: How do you handle HTTP Authentication popup in Playwright?**
A: HTTP Auth popups are browser-native and not in the DOM. Playwright bypasses them by setting credentials at the `BrowserContext` level using `.setHttpCredentials("username", "password")` before navigating to the page.

**Q3: What is the difference between a browser alert and a toast message?**
A: Browser alerts (Simple, Confirmation, Prompt) are native browser popups — not in the DOM, cannot be inspected. Toast messages are HTML elements built into the page — they can be inspected in DevTools and handled with regular Playwright locators.

---

# LECTURE 20 — JavaScript Execution in Playwright

---

## 1. page.evaluate() — The JavaScript Executor

**Playwright equivalent of Selenium's JavascriptExecutor:**

```java
// Selenium way (OLD)
JavascriptExecutor js = (JavascriptExecutor) driver;
js.executeScript("document.getElementById('user').value='admin'");

// Playwright way (NEW)
page.evaluate("document.getElementById('user').value='admin'");
```

---

## 2. Common JavaScript Operations

```java
// Scroll to bottom of page
page.evaluate("window.scrollTo(0, document.body.scrollHeight)");

// Scroll to top
page.evaluate("window.scrollTo(0, 0)");

// Get page title via JavaScript
String title = (String) page.evaluate("() => document.title");

// Set input value directly
page.evaluate("document.querySelector('#username').value='admin'");

// Set date picker (useful when field is readonly)
page.evaluate("document.querySelector('#datePicker').value='2026-01-15'");

// Set readonly field
page.evaluate("document.querySelector('#readonly-input').value='Sujal'");

// Create a JavaScript alert
page.evaluate("(name) => alert(name)", "Venkat");

// Click an element via JavaScript
page.evaluate("document.querySelector('#validateInputs').click()");
```

---

## 3. locator.evaluate() — Element-Level JavaScript

```java
// Highlight element with red border (useful for debugging/demos)
page.locator("#validateInputs")
    .evaluate("el => el.style.border='3px solid red'");

// Get text via JavaScript
String text = page.locator("#validateInputs")
    .evaluate("el => el.textContent").toString();

// Scroll element into view
page.locator("#validateInputs")
    .evaluate("el => el.scrollIntoView()");

// Force click via JavaScript
page.locator("#validateInputs")
    .evaluate("el => el.click()");

// Blink/flash an element (demo effect)
page.locator("[type='submit']")
    .evaluate("el => setInterval(() => { " +
        "el.style.outline = el.style.outline ? '' : '3px solid red'; " +
        "}, 300)");
```

---

## 4. page.evaluate() vs locator.evaluate()

| Feature | `page.evaluate()` | `locator.evaluate()` |
|---|---|---|
| Scope | Whole page | Specific element |
| Find element manually | ✅ Must use JS | ❌ Not needed |
| Auto-wait | ❌ No | ✅ Yes |
| Auto-retry | ❌ No | ✅ Yes |
| Reliability | Lower | Higher |
| Use for | Page-level JS | Element-level JS |

---

## 5. Preferred Methods Over JavaScript

| Task | JavaScript Way | Playwright Preferred Way |
|---|---|---|
| Scroll element into view | `el.scrollIntoView()` | `locator.scrollIntoViewIfNeeded()` |
| Get page title | `document.title` | `page.title()` |
| Get input value | `element.value` | `locator.inputValue()` |
| Get text | `element.textContent` | `locator.textContent()` |
| Click element | `element.click()` | `locator.click()` |
| Scroll page | `window.scrollTo()` | `locator.scrollIntoViewIfNeeded()` |

**Rule:** Always prefer Playwright's built-in methods. Use `page.evaluate()` only when native methods cannot handle the scenario.

---

## 6. Keyboard & Mouse Actions

### Keyboard Actions

```java
// Press a key on focused element
page.locator("#search").press("Enter");

// Keyboard shortcuts
page.keyboard().press("Control+A");   // Select all
page.keyboard().press("Control+C");   // Copy
page.keyboard().press("Control+V");   // Paste
page.keyboard().press("Control+X");   // Cut
page.keyboard().press("Delete");      // Delete

// Type text (use this instead of deprecated page.type())
page.keyboard().type("Hello World");

// Type with delay (simulates human typing)
page.keyboard().type("Hello World", 
    new Keyboard.TypeOptions().setDelay(100));  // 100ms between keys

// Hold key down then up (for text selection)
page.keyboard().down("Shift");
page.keyboard().press("ArrowLeft");
page.keyboard().up("Shift");

// Clear field and type new text
page.locator("#username").press("Control+A");
page.locator("#username").press("Delete");
```

### Mouse Actions

```java
// Hover over element
page.locator(".nav-dropbtn").hover();

// Right click
page.locator("text='Right Click Me'")
    .click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));

// Double click
page.locator("text=Double Click Me").dblclick();

// Coordinate-based mouse move
page.mouse().move(100, 200);

// Drag and Drop — Method 1 (simplest)
page.dragAndDrop("#source", "#target");

// Drag and Drop — Method 2 (using hover)
Locator source = page.locator("#source");
Locator target = page.locator("#target");
source.hover();
page.mouse().down();
target.hover();
page.mouse().up();

// Drag and Drop — Method 3 (coordinates)
page.mouse().move(200, 300);
page.mouse().down();
page.mouse().move(500, 300);
page.mouse().up();

// Scroll using mouse wheel
page.mouse().wheel(0, 1000);  // 0=horizontal, 1000=vertical down
```

### Selenium vs Playwright Mouse Actions

| Action | Selenium | Playwright |
|---|---|---|
| Hover | `moveToElement()` | `hover()` |
| Right-click | `contextClick()` | `setButton(MouseButton.RIGHT)` |
| Double-click | `doubleClick()` | `dblclick()` |
| Drag & Drop | Complex setup | `dragAndDrop()` |

---

## 7. Interview Questions — JavaScript & Actions

**Q1: What is `page.evaluate()` and when should you use it?**
A: `page.evaluate()` executes JavaScript directly inside the browser — similar to Selenium's JavascriptExecutor. Use it only when normal Playwright locators cannot handle the scenario, such as setting readonly field values, manipulating the DOM directly, or scrolling.

**Q2: Why does Playwright rarely need JavaScriptExecutor compared to Selenium?**
A: Playwright has built-in auto-waiting, automatic scrolling into view, and better element interaction support. Most scenarios that required JavaScript in Selenium can be handled with Playwright's native methods.

**Q3: What is `scrollIntoViewIfNeeded()` and why is it better than JavaScript scrolling?**
A: It's a Playwright built-in method that scrolls the element into view only if needed. It has auto-wait and is more reliable than `page.evaluate("el.scrollIntoView()")`.

---

# LECTURE 21 — Screenshots & Video Recording

---

## 1. Taking Screenshots

```java
// Full page screenshot (captures entire page including below the fold)
page.screenshot(
    new Page.ScreenshotOptions()
        .setPath(Paths.get("screenshots/fullpage.png"))
        .setFullPage(true)
);

// Viewport screenshot (only what's visible on screen)
page.screenshot(
    new Page.ScreenshotOptions()
        .setPath(Paths.get("screenshots/viewport.png"))
);

// Element screenshot (capture just one element)
Locator element = page.locator("#logo");
element.screenshot(
    new Locator.ScreenshotOptions()
        .setPath(Paths.get("screenshots/logo.png"))
);
```

---

## 2. Reusable Screenshot Utility Methods

```java
// Full page screenshot with timestamp (prevents file overwriting)
public static void takeScreenshot(Page page, String name, boolean fullPage) {
    String timestamp = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    Path path = Paths.get("screenshots/" + name + "_" + timestamp + ".png");
    page.screenshot(new Page.ScreenshotOptions().setPath(path).setFullPage(fullPage));
}

// Element screenshot with timestamp
public static void takeScreenshotElement(Page page, String locator, String name) {
    String timestamp = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    Path path = Paths.get("screenshots/" + name + "_" + timestamp + ".png");
    page.locator(locator).screenshot(new Locator.ScreenshotOptions().setPath(path));
}

// Usage
takeScreenshot(page, "AfterLogin", true);       // Full page
takeScreenshot(page, "LoginForm", false);        // Viewport only
takeScreenshotElement(page, "#logo", "Logo");   // Element only
```

---

## 3. Video Recording

```java
// Enable video recording at BrowserContext level
BrowserContext context = browser.newContext(
    new Browser.NewContextOptions()
        .setRecordVideoDir(Paths.get("videos/"))      // Where to save
        .setRecordVideoSize(1280, 720)                 // Resolution
);

Page page = context.newPage();

// ... run your test ...

// CRITICAL: Must close context to save the video
context.close();

// Get video path
Path videoPath = page.video().path();
System.out.println("Video saved at: " + videoPath);

browser.close();
playwright.close();
```

**⚡ CRITICAL:** Video is only saved AFTER `context.close()`. Without closing context, the video file may not be generated or may be incomplete.

---

## 4. Screenshot Best Practices

| Practice | Reason |
|---|---|
| Use timestamps in filenames | Prevents overwriting previous screenshots |
| Capture on failure only | Saves disk space in large test runs |
| Use meaningful names | Easy to find and understand in reports |
| Store in `screenshots/` folder | Organized folder structure |

---

## 5. Interview Questions — Screenshots & Video

**Q1: How do you take a screenshot in Playwright?**
A: Use `page.screenshot()` for page screenshots and `locator.screenshot()` for element screenshots. Add `setFullPage(true)` to capture the entire page. Always use timestamps in filenames to avoid overwriting.

**Q2: Why must `context.close()` be called for video recording?**
A: Video recording happens at the BrowserContext level. The video file is finalized and saved only when the context is closed. Without `context.close()`, the recording may be incomplete or the file may not be created.

**Q3: Difference between full-page and viewport screenshot?**
A: Viewport screenshot captures only what's visible on screen. Full-page screenshot captures the entire webpage including content below the visible area. Full-page requires `setFullPage(true)`.

---

## PART 2 — QUICK REVISION BEFORE INTERVIEW

```
BROWSER FACTORY:
- One class to launch all browsers
- Firefox/WebKit: no --start-maximized → use setViewportSize()
- Chromium: --start-maximized + setViewportSize(null)
- Always call playwright.close() at end

LOCATORS (Priority Order):
1. getByRole()      5. getByTestId()
2. getByLabel()     6. CSS Selector
3. getByPlaceholder()  7. XPath
4. getByText()

CRITICAL LOCATOR MISTAKES:
- .btn primary ≠ .btn.primary (space = descendant, no space = same element)
- XPath index starts at 1, Playwright nth() starts at 0

WAITS:
- Auto-wait: click, fill, check, hover (automatic)
- Explicit wait: loaders, API data, text changes
- NEVER use Thread.sleep()
- Default timeout = 30 seconds

ALERTS (MUST KNOW):
- Register onDialog() BEFORE the click that triggers alert
- onDialog() = permanent, onceDialog() = one-time
- Prompt: dialog.accept("text")
- Cancel: dialog.dismiss()
- Toast = HTML element, use locator()
- HTTP Auth = BrowserContext.setHttpCredentials()

FILE OPERATIONS:
- Upload: setInputFiles() or FileChooser
- Download: waitForDownload() + saveAs()

DROPDOWNS:
- <select>: selectOption()
- Auto-suggest: fill() + click suggestion
- Custom: click() + getByText()

SCREENSHOTS:
- Viewport: page.screenshot()
- Full page: setFullPage(true)
- Element: locator.screenshot()
- Video: context.close() must be called to save

JAVASCRIPT:
- page.evaluate() = run JS on whole page
- locator.evaluate() = run JS on specific element
- Use JS only when Playwright methods are not enough
```

---

*Part 2 Complete — Covers Lectures 14 to 21*
*Continue to Part 3 for Lectures 22 to 30 (Frames, Shadow DOM, POM, TestNG, Cucumber, Reports)*
