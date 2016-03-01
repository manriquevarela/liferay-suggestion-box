package com.rivetlogic.suggestionbox.hook.startupaction;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

import java.util.Date;

public class SuggestionBoxRoleDefinitionAction extends SimpleAction {
	/*
	 * (non-Java-doc)
	 * 
	 * @see com.liferay.portal.kernel.events.SimpleAction#SimpleAction()
	 */
	public SuggestionBoxRoleDefinitionAction() {
		super();
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see com.liferay.portal.kernel.events.SimpleAction#run(String[] ids)
	 */
	public void run(String[] ids) throws ActionException {

		Long defaultCompanyId = PortalUtil.getDefaultCompanyId();
		Role role = null;
		try {
			role = RoleLocalServiceUtil.getRole(defaultCompanyId, "SUGGESTIONBOX_MANAGER");

		} catch (Exception e) {

			_log.info("Missing Role: SUGGESTIONBOX_MANAGER " + e.getMessage());

		}

		if (Validator.isNull(role)) {
			
			_log.info("Creating Role: SUGGESTIONBOX_MANAGER");
			
			try {
				
				
				Date now = new Date();
				Long defaultUserId = UserLocalServiceUtil.getDefaultUserId(defaultCompanyId);
				Long roleClassNameId = ClassNameLocalServiceUtil.getClassNameId(Role.class);
				
				long roleId = CounterLocalServiceUtil.increment(Role.class.getName());

				Role suggestionManagerRole = RoleLocalServiceUtil.createRole(roleId);
				
				suggestionManagerRole.setName("SUGGESTIONBOX_MANAGER");
				suggestionManagerRole.setTitle("Manager for Suggestion Box portlet");
				suggestionManagerRole.setDescription("Autogenerated role from Suggestion Box portlet");
				suggestionManagerRole.setType(RoleConstants.TYPE_REGULAR);
				suggestionManagerRole.setUserId(defaultUserId);
				suggestionManagerRole.setCompanyId(defaultCompanyId);
				suggestionManagerRole.setClassNameId(roleClassNameId);
				suggestionManagerRole.setClassPK(roleId);
				suggestionManagerRole.setCreateDate(now);
				suggestionManagerRole.setModifiedDate(now);

				RoleLocalServiceUtil.addRole(suggestionManagerRole);
				
				_log.info("New Role created: SUGGESTIONBOX_MANAGER");
				
			} catch (SystemException e) {

				_log.info("Error creating Role: SUGGESTIONBOX_MANAGER", e);
			} catch ( PortalException e) {
				
				_log.info("Error creating Role: SUGGESTIONBOX_MANAGER", e);
			}

		}else {
			_log.info("Role SUGGESTIONBOX_MANAGER already exists.");
		}

	}

	private static final Log _log = LogFactoryUtil.getLog(SuggestionBoxRoleDefinitionAction.class);

}