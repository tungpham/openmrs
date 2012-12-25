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

import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openmrs.Steps;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.openqa.selenium.lift.Finders.*;
import static org.openqa.selenium.lift.Matchers.text;
import static org.openqa.selenium.lift.match.AttributeMatcher.attribute;

public class VerifyPatientDashboardSteps extends Steps {

	public VerifyPatientDashboardSteps(WebDriver driver) {
		super(driver);
	}
	
	@Given("I am on Find/Create Patient Page with $title as title")
	public void iAmOnFindCreatePatientPage(String title){
		assertPresenceOf(title().with(text(equalTo("OpenMRS - " +title))));
	}
	
	@When ("I search for a  patient $name")
	public void searchForPatient(String name){
		type(name, into(textbox().with(attribute("id", equalTo("inputNode")))));
        
	}
	
	@When ("view that patient's dashboard")
	@Alias("choose the patient")
	public void viewSelectedPatient(){
        String patientRecordXpath = "//table[@id=\'openmrsSearchTable\']/tbody/tr/td[3]";
		waitFor(finderByXpath(patientRecordXpath));
        clickOn(finderByXpath(patientRecordXpath));
			
	}
	
	@Then("the dashboard header should contain name, age, bmi, CD4, regimens, last encounter, Old identification number and OpenMRS identification number")
	public void verifyPatientDashBoard(){
    	assertPresenceOf(div().with(attribute("id",equalTo("patientHeaderPatientName"))).with(text(equalTo("Mr. Horatio L Hornblower Esq."))));
		assertPresenceOf(cell().with(attribute("id",equalTo("patientHeaderPatientAge"))).with(text(containsString("71 yrs"))));
		assertPresenceOf(table().with(attribute("id",equalTo("patientHeaderObs"))));
		assertPresenceOf(cell().with(attribute("id",equalTo("patientHeaderObsCD4"))));
		assertPresenceOf(cell().with(attribute("id", equalTo("patientHeaderObsRegimen"))));
		assertPresenceOf(div().with(attribute("id",equalTo("patientHeaderPreferredIdentifier"))).with(text(containsString("101-6"))));
		assertPresenceOf(cell().with(attribute("id", equalTo("patientHeaderOtherIdentifiers"))).with(text(containsString("Old Identification Number: 101"))));
		assertPresenceOf(title().with(text(equalTo("OpenMRS - Patient Dashboard"))));
	}
}
