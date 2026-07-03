Feature: Place order in VR QA Academy

  Scenario: Successful order placement
    Given user is on login page
    When user logs in with valid credentials
    And user adds product to cart
    And user completes checkout
    Then order should be placed successfully
