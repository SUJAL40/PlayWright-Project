# Playwright with Java — Complete Course Notes (Part 1)
### Lectures 0 to 13 | Beginner-Friendly | Interview Ready

---

> **How to Use These Notes:**
> - Read "Definition" first to understand the concept.
> - Read "Why We Use It" to understand the purpose.
> - Read "Example" to see how it works in code.
> - Read "Interview Q&A" before your interview.
> - Use "Quick Revision" at the end of each topic for fast review.

---

# LECTURE 0 — Introduction to Playwright

---

## 1. What is Playwright?

**Definition:**
Playwright is a modern tool used to automate web browsers. It was created by Microsoft in 2020. Think of it like a robot that can open a browser, click buttons, fill forms, and check if a website is working correctly — all automatically.

**Why We Use It:**
- To test websites automatically without clicking manually every time.
- Saves time — runs hundreds of tests in minutes.
- More reliable than Selenium for modern websites.

**Key Facts:**
- Created by: Microsoft
- Year: 2020
- Type: Open Source (free to use)
- Supported Languages: Java, JavaScript, TypeScript, Python, C#, .NET
- Official Website: https://playwright.dev/java/docs/intro

---

## 2. Key Features of Playwright

| Feature | What It Means in Simple Words |
|---|---|
| Auto-Wait | Playwright waits for elements to be ready automatically. No need for `Thread.sleep()`. |
| Multi-browser | Works with Chrome, Firefox, Safari (WebKit), Edge |
| Multi-tab | Can handle multiple tabs in one browser |
| Network Mocking | Can fake/control API responses for testing |
| Screenshots | Can take pictures of the page automatically |
| Video Recording | Can record the full test as a video |
| Tracing | Records every action for debugging |
| Headless Mode | Can run tests without opening a visible browser |

---

## 3. Playwright Architecture (How It Works)

**Simple Explanation:**
Playwright talks to the browser using **WebSocket** — like a phone call that stays connected the whole time.

Selenium used **HTTP** — like sending individual text messages. Every message needs a new connection.

```
Your Test Code  ---(WebSocket)----> Playwright ---> Chrome/Firefox/Safari
```

**Why WebSocket is Better:**
- One connection for the entire test
- Faster communication
- Less chance of errors
- No need to reconnect for every action

**Selenium vs Playwright Architecture:**

| Point | Selenium | Playwright |
|---|---|---|
| Connection Type | HTTP (new request every time) | WebSocket (one connection always open) |
| Speed | Slower | Faster |
| Reliability | More flaky | More stable |
| Auto-wait | No | Yes |

---

## 4. Playwright Steps (PBP Pattern)

Remember: **P → B → P**

```
P = Playwright (entry point)
B = Browser (Chrome/Firefox/WebKit)
P = Page (tab inside browser)
```

```java
// Step 1: Create Playwright
Playwright pw = Playwright.create();

// Step 2: Launch Browser
Browser browser = pw.chromium().launch();

// Step 3: Create a Tab (Page)
Page page = browser.newPage();

// Step 4: Navigate to URL
page.navigate("https://www.saucedemo.com");
```

**Memory Trick:** "**P**lease **B**ring **P**izza" = Playwright, Browser, Page

---

## 5. First Playwright Program

```java
package org.sujalkhot;

import com.microsoft.playwright.*;

public class LaunchBrowser {

    public static void main(String[] args) {

        // Creates the Playwright engine — must be first step
        Playwright pw = Playwright.create();

        // Opens Chromium browser with visible UI (headed mode)
        Browser browser = pw.chromium()
            .launch(new BrowserType.LaunchOptions().setHeadless(false));

        // Opens a new tab inside the browser
        Page page = browser.newPage();

        // Goes to the website URL
        page.navigate("https://www.saucedemo.com");

        // Prints the title of the page in console
        System.out.println(page.title());

        // Types "standard_user" in the username field (using CSS ID selector)
        page.fill("#user-name", "standard_user");

        // Types the password
        page.fill("#password", "secret_sauce");

        // Clicks the Login button
        page.click("#login-button");

        // Always close browser at the end
        browser.close();
        pw.close();
    }
}
```

**Line by Line Explanation:**
- `Playwright.create()` — Starts the Playwright engine. Always first.
- `setHeadless(false)` — Opens browser with visible window. Default is headless (invisible).
- `browser.newPage()` — Opens a new tab (like pressing Ctrl+T).
- `page.navigate(url)` — Types the URL and opens the website.
- `page.fill("#id", "text")` — Types text into an input field.
- `page.click("#id")` — Clicks an element.
- `browser.close()` + `pw.close()` — Cleans up and closes everything.

---

## 6. Headed vs Headless Mode

| Mode | What You See | Speed | When to Use |
|---|---|---|---|
| Headed (`setHeadless(false)`) | Browser opens visibly | Slower | Debugging, Development |
| Headless (`setHeadless(true)`) | No visible browser | Faster | CI/CD, Final Runs |

**Default:** Playwright runs in **Headless** mode by default.

---

## 7. Launching Different Browsers

```java
// Chromium (bundled with Playwright)
Browser browser = pw.chromium().launch();

// Real Google Chrome (must be installed on your computer)
Browser browser = pw.chromium().launch(
    new BrowserType.LaunchOptions().setChannel("chrome"));

// Microsoft Edge
Browser browser = pw.chromium().launch(
    new BrowserType.LaunchOptions().setChannel("msedge"));

// Firefox
Browser browser = pw.firefox().launch();

// WebKit (Safari engine)
Browser browser = pw.webkit().launch();
```

**Chrome vs Chromium:**

| Chrome | Chromium |
|---|---|
| Made by Google | Open source project |
| Has Google branding | No branding |
| Auto-updates | No auto-updates |
| Installed by user | Bundled with Playwright |

---

## 8. Maximize Browser Window

```java
// For Chrome/Edge/Chromium — use argument
Browser browser = pw.chromium().launch(
    new BrowserType.LaunchOptions()
        .setHeadless(false)
        .setArgs(Arrays.asList("--start-maximized"))
);

// Must set viewport to null for maximized to work
BrowserContext context = browser.newContext(
    new Browser.NewContextOptions().setViewportSize(null)
);

Page page = context.newPage();
```

**Important:** Firefox and WebKit do NOT support `--start-maximized`. For them, set viewport size manually:

```java
// For Firefox/WebKit fullscreen
context = browser.newContext(
    new Browser.NewContextOptions().setViewportSize(1920, 1080)
);
```

---

## 9. Interview Questions — Lecture 0

**Q1: What is Playwright and who created it?**
A: Playwright is a modern web automation framework created by Microsoft in 2020. It supports multiple browsers and multiple programming languages.

**Q2: What is the difference between Playwright and Selenium?**
A: Playwright uses WebSocket (one persistent connection), while Selenium uses HTTP (new connection for every action). Playwright has built-in auto-waiting, Selenium requires manual waits. Playwright is faster and more stable.

**Q3: What languages does Playwright support?**
A: Java, JavaScript, TypeScript, Python, C#, and .NET.

**Q4: What is the difference between Headless and Headed mode?**
A: Headed mode opens a visible browser window. Headless mode runs without any visible window. Headless is faster and used in CI/CD pipelines.

**Q5: What is the PBP pattern in Playwright?**
A: PBP = Playwright → Browser → Page. First create a Playwright instance, then launch a Browser, then open a Page (tab). This is the mandatory setup order.

---

# LECTURE 1 — Core Concepts & Selenium vs Playwright

---

## 1. Default Auto-Wait

Playwright waits **30 seconds** by default for any action to complete before failing.

```java
// To change default timeout for all actions
page.setDefaultTimeout(45000);  // 45 seconds

// To change timeout for navigation only
page.setDefaultNavigationTimeout(60000);  // 60 seconds
```

---

## 2. Selenium vs Playwright Comparison

| Feature | Playwright | Selenium |
|---|---|---|
| Auto-waiting | Built-in (30 seconds default) | Must write manually |
| Browser Support | Chromium, Firefox, WebKit | All major browsers |
| Speed | Faster | Slower |
| Modern Web Features | Better (single-page apps, shadow DOM, iframes) | Needs workarounds |
| Installation | Simple Maven dependency | More configuration needed |
| JavaScript Alerts | `page.onDialog()` | `driver.switchTo().alert()` |
| Multiple Tabs | Built-in | `switchTo().window()` |
| Shadow DOM | Auto-piercing | Manual JavaScript workaround |
| Network Mocking | Built-in | Not available natively |

---

# LECTURE 2 — Java Fundamentals for Automation

---

## 1. Types of Variables in Java

| Variable Type | Where Declared | Example |
|---|---|---|
| Local Variable | Inside a method | `int x = 5;` inside `main()` |
| Instance Variable | Inside class, outside method | `String name;` inside class |
| Static Variable | Inside class with `static` keyword | `static String browser;` |

**Important:** Local variables MUST be initialized before use, or you get a compile error. Instance/static variables get default values (null for String, 0 for int).

---

## 2. Common Java Data Types for Automation

```java
String url = "https://example.com";     // Text
int count = 5;                           // Whole number
boolean isVisible = true;               // True or False
long timeout = 30000L;                  // Large number (note the L at end)
```

---

# LECTURE 3 — Maven Project Setup

---

## 1. What is Maven?

**Definition:** Maven is a build tool that automatically downloads and manages Java libraries (called dependencies or JAR files) for your project.

**Without Maven:** You download JAR files manually and add them to your project. Very tedious.

**With Maven:** Just add a few lines to `pom.xml` and Maven downloads everything automatically.

---

## 2. Maven Project Structure

```
PlaywrightProject/
│
├── pom.xml                          ← Add dependencies here
│
├── src/
│   ├── main/
│   │   ├── java/                   ← Main application code
│   │   └── resources/              ← Config files (properties, etc.)
│   │
│   └── test/
│       ├── java/                   ← Your test code goes here
│       └── resources/              ← Test data (Excel, properties files)
│
└── target/                         ← Compiled output (auto-generated)
```

---

## 3. Playwright Maven Dependency

Add this to your `pom.xml` inside `<dependencies>`:

```xml
<dependency>
    <groupId>com.microsoft.playwright</groupId>
    <artifactId>playwright</artifactId>
    <version>1.58.0</version>
</dependency>
```

---

## 4. How to Add Imports in Eclipse

- Press **Ctrl + Shift + O** to automatically add all needed imports.
- ⚠️ Common Mistake: Sometimes Eclipse imports the wrong package. Always double-check the import matches `com.microsoft.playwright`.

---

# LECTURE 11 — Exception Handling in Java

---

## 1. What is an Exception?

**Simple Definition:** An exception is an unexpected error that happens while your program is running. Without handling it, your program stops completely.

**Real-World Example:**
Imagine a test script with 10 steps. If step 5 fails due to an element not found:
- **Without exception handling:** Steps 6–10 never run. Program crashes.
- **With exception handling:** The error is caught, handled, and the remaining steps can continue.

---

## 2. Exception Handling Keywords

| Keyword | Simple Meaning | When to Use |
|---|---|---|
| `try` | "Try to run this risky code" | Wrap code that might fail |
| `catch` | "If it fails, do this instead" | Handle the error |
| `finally` | "Always run this no matter what" | Cleanup code (close browser, etc.) |
| `throw` | "Manually create an error" | Throw a custom error |
| `throws` | "This method might throw an error" | Warn the caller |

---

## 3. Basic Exception Handling Pattern

```java
try {
    // Code that might fail
    page.click("#login-btn");

} catch (TimeoutException e) {
    // What to do if it fails
    System.out.println("Element not clickable, trying again...");
    page.click("#login-btn");

} finally {
    // Always runs — perfect for cleanup
    System.out.println("Login action attempted");
    browser.close();  // Always close browser
}
```

---

## 4. Common Exceptions in Automation

| Exception | When It Happens |
|---|---|
| `TimeoutException` | Element not found within timeout period |
| `NullPointerException` | Trying to use an object that is null |
| `NoSuchElementException` | Element not found in DOM |
| `ArithmeticException` | Dividing by zero |
| `ArrayIndexOutOfBoundsException` | Accessing invalid array index |

---

## 5. Exception Order Rule

Always catch **specific exceptions first**, then general `Exception`:

```java
try {
    // code
} catch (TimeoutException e) {   // Specific first
    // handle timeout
} catch (Exception e) {          // General last
    // handle anything else
}
```

⚠️ **Common Mistake:** If you put `catch (Exception e)` first, Java gives a compile error because it would catch everything and the specific catches below it would never run. Always go: Child → Parent.

---

## 6. Interview Questions — Exception Handling

**Q1: What is the difference between checked and unchecked exceptions?**
A: Checked exceptions are caught at compile time (e.g., `IOException`, `FileNotFoundException`). Unchecked exceptions happen at runtime (e.g., `NullPointerException`, `ArithmeticException`).

**Q2: Can a try block exist without a catch block?**
A: Yes, if a `finally` block is present. `try-finally` is valid.

**Q3: Can the finally block be skipped?**
A: No, except when `System.exit()` is called.

**Q4: Why use specific exceptions instead of just catching `Exception`?**
A: Specific exceptions give more control, better error messages, easier debugging, and avoid accidentally hiding serious errors.

---

# LECTURE 11 (Continued) — Java Collections for Automation

---

## 1. What is the Collection Framework?

**Simple Definition:** Collections are like containers that store groups of data. Instead of creating 10 separate variables, you store all 10 values in one collection.

**Memory Trick:** Think of collections as different types of boxes:
- **List** = Ordered box (items stay in order, duplicates allowed)
- **Set** = Unique box (no duplicates allowed)
- **Map** = Labeled box (each item has a key and value)

---

## 2. Most Important Collections for Automation

### ArrayList — Most Used

```java
// Storing multiple product names
List<String> products = new ArrayList<>();
products.add("Laptop");
products.add("Phone");
products.add("Headphones");
products.add("Laptop");   // Duplicates allowed!

System.out.println(products);  // [Laptop, Phone, Headphones, Laptop]
System.out.println(products.get(0));  // Laptop (index-based access)
```

**When to use in automation:**
- Store all product titles from a page
- Store multiple URLs to test
- Store test data

---

### HashMap — Second Most Used

```java
// Storing test credentials
Map<String, String> credentials = new HashMap<>();
credentials.put("username", "admin");
credentials.put("password", "admin123");
credentials.put("url", "https://example.com");

System.out.println(credentials.get("username"));  // admin
```

**When to use in automation:**
- Store config data (key-value pairs)
- Store API request parameters
- Store environment-specific values

---

### HashSet — Third Most Used

```java
// Storing unique URLs (no duplicates)
Set<String> urls = new HashSet<>();
urls.add("https://example.com");
urls.add("https://example.com");  // Duplicate - ignored!
urls.add("https://google.com");

System.out.println(urls.size());  // 2 (not 3)
```

**When to use in automation:**
- Remove duplicate test data
- Store unique browser names
- Verify no duplicate elements on page

---

## 3. Collections Comparison Table

| Collection | Duplicates | Order | Access | Best For |
|---|---|---|---|---|
| ArrayList | ✅ Yes | ✅ Maintained | By index | Test data lists |
| LinkedList | ✅ Yes | ✅ Maintained | By position | Dynamic data |
| HashSet | ❌ No | ❌ Random | No index | Unique values |
| LinkedHashSet | ❌ No | ✅ Maintained | No index | Unique + ordered |
| TreeSet | ❌ No | ✅ Sorted | No index | Sorted data |
| HashMap | Keys unique | ❌ Random | By key | Config/credentials |
| LinkedHashMap | Keys unique | ✅ Maintained | By key | Ordered config |

---

## 4. Iterator — Loop Through Any Collection

```java
ArrayList<String> browsers = new ArrayList<>();
browsers.add("Chrome");
browsers.add("Firefox");
browsers.add("Edge");

Iterator<String> itr = browsers.iterator();
while (itr.hasNext()) {
    System.out.println(itr.next());
}
```

---

## 5. Interview Questions — Collections

**Q1: Why is ArrayList used in automation?**
A: ArrayList has dynamic size, allows duplicates, maintains insertion order, and supports fast index-based access. Perfect for storing test data, page titles, or product lists.

**Q2: Difference between List and Set?**

| List | Set |
|---|---|
| Allows duplicates | No duplicates |
| Ordered (insertion order) | Unordered (HashSet) |
| Index-based access | No index |

**Q3: Difference between HashMap and HashSet?**

| HashMap | HashSet |
|---|---|
| Stores key-value pairs | Stores only values |
| Uses `put()` | Uses `add()` |
| Allows one null key | Allows one null value |

---

# LECTURE 12 — Playwright Codegen (Recorder)

---

## 1. What is Codegen?

**Definition:** Codegen is Playwright's built-in recorder. When you perform actions on a website manually, Playwright automatically writes the Java code for you.

**Real-World Use:** You perform a login manually (click, type username, type password, click Login) — Codegen generates all the Playwright Java code automatically.

---

## 2. How to Start Codegen

**Method 1: Terminal Command**
```bash
cd "C:\Your\Playwright\Project\Path"

mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="codegen https://yourwebsite.com"
```

**Method 2: Using `page.pause()` in Code (Easier)**
```java
page.navigate("https://yourwebsite.com");
page.pause();  // Opens Playwright Inspector — record and debug here
```

---

## 3. Playwright Inspector Toolbar

| Button | Icon | Purpose |
|---|---|---|
| Pick Locator | Mouse pointer | Click any element to get its locator |
| Assert Visibility | Eye 👁️ | Check if element is visible |
| Assert Text | "ab" | Verify text of an element |
| Assert Value | Textbox | Verify input field value |
| Record | Red circle | Start/stop recording actions |
| Inspector | Grid | Explore page elements |

---

## 4. Generated Code Example

After recording a login:
```java
assertThat(page.getByRole(AriaRole.TEXTBOX, 
    new Page.GetByRoleOptions().setName("Username"))).isVisible();

page.getByRole(AriaRole.TEXTBOX, 
    new Page.GetByRoleOptions().setName("Username")).fill("admin");

page.getByRole(AriaRole.TEXTBOX, 
    new Page.GetByRoleOptions().setName("Password")).fill("admin123");

page.getByRole(AriaRole.BUTTON, 
    new Page.GetByRoleOptions().setName("Login")).click();
```

---

## 5. Interview Questions — Codegen

**Q1: What is Playwright Codegen?**
A: Codegen is a built-in recorder in Playwright that automatically generates Java/Python/JavaScript code by recording your manual actions on a website. You can start it with the `codegen` CLI command or use `page.pause()` in your script.

**Q2: What is `page.pause()` used for?**
A: `page.pause()` pauses script execution and opens the Playwright Inspector for debugging, element inspection, and generating locators interactively.

---

# LECTURE 13 — Browser Management

---

## 1. Browser Factory Design Pattern (Preview)

**Definition:** Instead of writing browser launch code in every test, you create one reusable `BrowserFactory` class that handles everything.

**Memory Trick:** Think of BrowserFactory as a **car rental service**. You tell it what car (browser) you want, and it gives you the car ready to drive.

---

## 2. Browser Architecture in Playwright

```
Playwright (main API)
    ↓
Browser (Chrome/Firefox/WebKit instance)
    ↓
BrowserContext (like an incognito session)
    ↓
Page (a tab inside the context)
```

| Component | Description | Real-World Analogy |
|---|---|---|
| Playwright | Main controller | Remote control |
| Browser | Running browser | TV |
| BrowserContext | Isolated session | Separate user profile |
| Page | One tab | One channel on TV |

---

## 3. Multiple Browser Contexts

```java
// Two isolated sessions in ONE browser
BrowserContext adminContext = browser.newContext();
BrowserContext userContext = browser.newContext();

Page adminPage = adminContext.newPage();
Page userPage = userContext.newPage();

// Both can be running simultaneously
adminPage.navigate("https://example.com/admin");
userPage.navigate("https://example.com/user");
```

**When to use:**
- Test admin and user simultaneously
- Test chat applications (sender and receiver)
- Test multi-user scenarios

---

## 4. Interview Questions — Browser Management

**Q1: What is BrowserContext in Playwright?**
A: BrowserContext is an isolated browser session — like an incognito window. It has its own cookies, localStorage, and session data. Multiple contexts can exist in one browser instance.

**Q2: What is the difference between Browser and BrowserContext?**

| Browser | BrowserContext |
|---|---|
| Actual running browser | Isolated session inside browser |
| More memory | Lightweight |
| Separate browser launch | Created from same browser |
| Different engine | Multiple sessions |

---

## PART 1 — QUICK REVISION BEFORE INTERVIEW

### Most Important Points to Remember

```
1. Playwright = Microsoft, 2020, WebSocket, Auto-Wait 30 seconds

2. PBP Pattern = Playwright → Browser → Page

3. Headless = No visible window (CI/CD)
   Headed = Visible window (debugging)

4. setHeadless(false) = opens browser visibly
   setHeadless(true) = runs in background

5. Firefox/WebKit = NO --start-maximized
   Use setViewportSize(width, height) instead

6. BrowserContext = isolated session (like incognito)

7. Codegen = auto-generates code from your actions
   page.pause() = opens inspector for debugging

8. Collection most used in automation:
   - ArrayList (test data lists)
   - HashMap (config/credentials)
   - HashSet (unique values)

9. Exception handling:
   try → risky code
   catch → handle error
   finally → always runs (cleanup)

10. Maven = manages dependencies (JAR files) automatically
```

---

*Part 1 Complete — Covers Lectures 0 to 13*
*Continue to Part 2 for Lectures 14 to 20 (Locators, Alerts, JS, Actions)*
