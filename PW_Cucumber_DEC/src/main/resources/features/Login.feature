Feature: Login into VR Shopping Site

Scenario: TC001_Login with valid credentials

	Given Open the chrome browser
	And load the application URL
	When enter the valid username
	And enter the valid password
	Then Click on login button

	
Scenario: TC002_Login with Invalid credentials

	Given Open the chrome browser
	And load the application URL
	When enter the valid username
	And enter the valid password
	But Click on login button