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
package org.openmrs.web.controller.maintenance;

import org.apache.commons.lang.StringUtils;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 * Backs the localesAndThemes.jsp page to let the admin change the default locale, default theme,
 * etc
 */
@Controller
public class LocalesAndThemesFormController {
	
	/**
	 * Called for GET requests only on the databaseChangesInfo page. POST page requests are invalid
	 * and ignored.
	 * 
	 * @param model the key value pair that will be accessible from the jsp page
	 * @throws Exception if there is trouble getting the database changes from liquibase
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/admin/maintenance/localesAndThemes")
	public void showPage(ModelMap model) throws Exception {
		String theme = Context.getAdministrationService().getGlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_THEME);
		model.addAttribute("theme", theme);
		
		String locale = Context.getAdministrationService()
		        .getGlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_LOCALE);
		model.addAttribute("locale", locale);
		
		String allowedLocales = Context.getAdministrationService().getGlobalProperty(
		    OpenmrsConstants.GLOBAL_PROPERTY_LOCALE_ALLOWED_LIST);
		model.addAttribute("allowedLocales", allowedLocales);
	}
	
	/**
	 * Called upon save of the page
	 * 
	 * @param theme the theme name to save
	 * @param locale the locale to save (en, en_GB, es, etc)
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/admin/maintenance/localesAndThemes")
	public String saveDefaults(WebRequest request, @RequestParam("theme") String theme, @RequestParam("locale") String locale)
	        throws Exception {
		boolean localeInList = false;
		String allowedLocales = Context.getAdministrationService().getGlobalProperty(
		    OpenmrsConstants.GLOBAL_PROPERTY_LOCALE_ALLOWED_LIST);
		String[] allowedLocalesList = allowedLocales.split(",");
		
		// save the theme
		GlobalProperty themeGP = Context.getAdministrationService().getGlobalPropertyObject(
		    OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_THEME);
		themeGP.setPropertyValue(theme);
		Context.getAdministrationService().saveGlobalProperty(themeGP);
		
		// save the locale		
		for (String loc : allowedLocalesList) {
			loc = loc.trim();
			if (loc.equals(locale)) {
				localeInList = true;
				GlobalProperty localeGP = Context.getAdministrationService().getGlobalPropertyObject(
				    OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_LOCALE);
				localeGP.setPropertyValue(locale);
				Context.getAdministrationService().saveGlobalProperty(localeGP);
				break;
			}
		}
		
		//displaying the success or failure message
		if (localeInList || StringUtils.isBlank(locale)) {
			request.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context.getMessageSourceService().getMessage(
			    "LocalesAndThemes.saved"), WebRequest.SCOPE_SESSION);
		} else {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "LocalesAndThemes.localeError"), WebRequest.SCOPE_SESSION);
		}
		
		return "redirect:/admin/maintenance/localesAndThemes.form";
	}
	
}
