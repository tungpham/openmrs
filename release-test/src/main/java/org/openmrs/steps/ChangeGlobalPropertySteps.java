/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.steps;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.openqa.selenium.lift.Finders.button;
import static org.openqa.selenium.lift.Finders.div;
import static org.openqa.selenium.lift.Finders.link;
import static org.openqa.selenium.lift.Finders.textbox;
import static org.openqa.selenium.lift.Finders.title;
import static org.openqa.selenium.lift.Matchers.text;
import static org.openqa.selenium.lift.match.AttributeMatcher.attribute;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openmrs.Steps;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.lift.find.HtmlTagFinder;


public class ChangeGlobalPropertySteps extends ViewGlobalPropertySteps {
    public ChangeGlobalPropertySteps(WebDriver driver) {
        super(driver);
    }
    
    @When("I type $name as name")
	public void enterGlobalPropertyName(String name) {
		type(random(name), into(finderByXpath("//tbody[@id='globalPropsList']/tr[count(//tbody[@id='globalPropsList']/tr) - 5]/td[1]/input")));
		type("warn", into(finderByXpath("//tbody[@id='globalPropsList']/tr[@class='evenRow'][85]/td[2]/input")));
	}
    
    @When("I type $value as value")
	public void enterGlobalPropertyValue(String value) {
		type(random(value), into(finderByXpath("//tbody[@id='globalPropsList']/tr[count(//tbody[@id='globalPropsList']/tr) - 5]/td[2]/input")));
	}
    
    @When("I click on $save button")
	public void clickOnSave(String save) {
		clickOn(button(save));
	}
    
    @Then("display message $successMessage")
	public void verifySuccessMessage(String successMessage) {
		waitAndAssertFor(div().with(text(containsString(successMessage))));
	}
}
